package per.cloudy.compenent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.swing.JFrame;

import per.cloudy.entity.Bucket;
import per.cloudy.net.NetTelnet;
import per.cloudy.utils.FileUtils;

public class MonitorFrame extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8156998214546508634L;
	public final GraphicPanel graphic = new GraphicPanel();;
	public NetTelnet telnet;
	public String title;
	public Thread thread;
	public FileUtils logUtils;
	public MonitorFrame(final NetTelnet telnet,String title) {
		super();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		logUtils=new FileUtils(title);
	//	this.addWindowStateListener(this);
		this.telnet=telnet;
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
		this.getContentPane().setLayout(null);
		this.setTitle("监控SCF服务-"+title);
		this.add(graphic);
		final Bucket bucket = new Bucket();
		final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		new Thread(){
			public void run() {
				telnet.sendCommand("count");
				while(true){
					String countStr=telnet.readUntil("$").split("  ")[1].trim();
					bucket.add(Integer.parseInt(countStr));
					logUtils.append(sdf.format(new Date())+",all,"+countStr);
					graphic.redraw(bucket);
				}
			};
		}.start();
	}

	public void test() {
		this.setVisible(true);
		Bucket bucket = new Bucket();
		Random r=new Random();
		int index=0;
		while(true){
			index++;
			bucket.add(r.nextInt(index>500?50:100));
			this.graphic.redraw(bucket);
			try {
				Thread.sleep(1000l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}