package per.cloudy.compenent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import per.cloudy.net.NetTelnet;
import per.cloudy.utils.FileUtils;

public class MainFrame extends JFrame implements ActionListener{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7501800684518483717L;
	final JTextField ipT=new JTextField();
	final JTextField portT=new JTextField();
	public MainFrame(){
		super();
		this.setSize(320, 180);
		this.getContentPane().setLayout(null);
		this.setTitle("监控服务");
		this.setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JLabel ipL=new JLabel();
		ipL.setBounds(10,10,80,20);
		ipL.setText("Address:");
		this.add(ipL);
		JLabel portL=new JLabel();
		portL.setBounds(10,35,40,20);
		portL.setText("Port:");
		this.add(portL);
		JLabel lastL=new JLabel();
		lastL.setBounds(10,60,80,20);
		lastL.setText("最近使用:");
		this.add(lastL);
		
		ipT.setBounds(85,10,200,20);
		this.add(ipT);
		
		portT.setBounds(85,35,200,20);
		this.add(portT);
		
		final JComboBox lastC=new JComboBox();
		lastC.setBounds(85,65,200,20);
		final FileUtils dataFile=new FileUtils("data", "lastData",true);
		List<String> lastList=dataFile.readFile();
		Set<String> hashSet=new TreeSet<String>();
		int count=0;
		for(String line:lastList){
			hashSet.add(line);
		}
		for(String line:hashSet){
			count++;
			if(count==15){
				break;
			}
			lastC.addItem(line);
		}
		lastC.addActionListener(this);
		this.add(lastC);
		
		JButton countB=new JButton();
		countB.setBounds(85, 90, 100, 30);
		countB.setText("查看访问量");
		
		countB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String ip=ipT.getText();
				String port=portT.getText();
				if(ip==null||ip.isEmpty()){
					JOptionPane.showMessageDialog(null, "IP不允许为空", "提示",JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(port==null||port.isEmpty()){
					JOptionPane.showMessageDialog(null, "端口不允许为空", "提示",JOptionPane.ERROR_MESSAGE);
					return;
				}
				int portI=0;
				try{
					portI=Integer.parseInt(port);
				}catch(RuntimeException e1){
					JOptionPane.showMessageDialog(null, "端口为数字", "提示",JOptionPane.ERROR_MESSAGE);
				}
				NetTelnet telnetCount=null;
				NetTelnet telnetList=null;
				try{
					telnetCount=new NetTelnet(ip, portI);
					telnetList=new NetTelnet(ip, portI);
				}catch(Exception e1){
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, e1.getMessage(), "提示",JOptionPane.ERROR_MESSAGE);
					return;
				}
				String title=ip+":"+port;
				MonitorFrame m=new MonitorFrame(telnetCount,title);
				dataFile.append(title);
				m.setVisible(true);
				
				IPList ipList=new IPList(telnetList,title);
				ipList.setVisible(true);
			}
		});
		this.add(countB);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("comboBoxChanged")){
			String[] line=((JComboBox)e.getSource()).getSelectedItem().toString().split(":");
			this.ipT.setText(line[0]);
			this.portT.setText(line[1]);
		}
		
		
	}
	public static void main(String[] args) {
		MainFrame frame=new MainFrame();
		frame.setVisible(true);
	}
}
