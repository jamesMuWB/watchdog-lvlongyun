package per.cloudy.compenent;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import per.cloudy.net.NetTelnet;
import per.cloudy.utils.DateUtils;
import per.cloudy.utils.FileUtils;

public class IPList extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2341564321L;

	private JPanel topPanel;
	public NetTelnet telnet;
	private JTable table;
	
	DefaultTableModel tableModel;
	private FileUtils fileUtils;
	public IPList(final NetTelnet telnet,String title){
		this.telnet=telnet;
		final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.setTitle(title+"-开始时间："+sdf.format(new Date()));
		final String myTitle=this.getTitle();
		this.setSize(500,500);
		topPanel = new JPanel();
		final String[] columnName = { "IP", "服务", "次数" };
		tableModel = new DefaultTableModel(null,columnName);
	    table = new JTable(tableModel);
		table.getColumnModel().getColumn(0).setWidth(100);
		table.getColumnModel().getColumn(1).setWidth(450);
		table.getColumnModel().getColumn(2).setWidth(50);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.add(Box.createVerticalStrut(10));
		topPanel.add(scrollPane);
        topPanel.add(Box.createVerticalStrut(10));
        
		JPanel panelContainer = new JPanel();
		panelContainer.setLayout(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridx = 0;
		c1.gridy = 0;
		c1.weightx = 1.0;
		c1.weighty = 1.0;

		c1.fill = GridBagConstraints.BOTH;
		panelContainer.add(topPanel, c1);
		this.setContentPane(panelContainer);
		this.fileUtils= new FileUtils("data","ip.list."+title,false);
		final Map<String,Integer> map=new TreeMap<String,Integer>(new Comparator<String>() {
			@Override
			public int compare(String key1, String key2){
				String[] xx1=key1.split(",");
				String[] xx2=key2.split(",");
				int result=xx1[0].compareTo(xx2[0]);
				if(result!=0){
					return result;
				}
				result=xx1[1].compareTo(xx2[1]);
				if(result!=0){
					return result;
				}
				result=xx1[2].compareTo(xx2[2]);
				if(result!=0){
					return result;
				}
				return result;
			}
		});
		final JFrame frame=this;
		new Thread(){
			@Override
			public void run() {
				telnet.sendCommand("time|grep _");
				StringBuffer sb=new StringBuffer();
				int index=0;
				int needRefresh=0;
				while(true){
					String countStr=telnet.readUntil("$");
					if(countStr.charAt(0)==13){
						if(index!=0){
							needRefresh++;
							String str=sb.substring(0,sb.length()-1);
							fileUtils.append(DateUtils.getNowTime()+","+str);
							str=str.substring(str.indexOf(",")+1);
							Integer count=map.get(str);
							if(count==null){
								map.put(str, 1);
							}else{
								map.put(str, count+1);
							}
							if(needRefresh==100){
								Set<String> keySet=map.keySet();
								Object [][] data=new Object[keySet.size()][];
								int i=0;
								for(String key:keySet){
									Object[] ob=new Object[3];
									String[] xx=key.split(",");
									ob[0]=xx[0];
									ob[1]=xx[1]+"."+xx[2];
									ob[2]=map.get(key);
									data[i++]=ob;
								}
								tableModel.setDataVector(data, columnName);
								needRefresh=0;
								frame.setTitle(myTitle+"-最后更新时间："+sdf.format(new Date()));
							}
						}
						index++;
						sb.delete(0, sb.length());
					}
					//第一行 time:1ms
					if(countStr.startsWith("\rtime")){
						sb.append(countStr.split("--")[0].split(":")[1].replace("ms", ""));
					}
					//第二行  fromIP
					else if(countStr.startsWith("fromIP")){
						sb.append(countStr.split(":")[1]);
					}
					//第三行 lookUP:STTInfoServiceImpl 
					else if(countStr.startsWith("lookUP")){
						sb.append(countStr.split(":")[1]);
					}
					//第四行 methodName:insertBasic
					else if(countStr.startsWith("methodName")){
						sb.append(countStr.split(":")[1]);
					}else{
						continue;
					}
					sb.append(",");
				}
			}
		}.start();
	}

}
