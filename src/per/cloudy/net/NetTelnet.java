package per.cloudy.net;

import java.io.InputStream;
import java.io.PrintStream;

import org.apache.commons.net.telnet.TelnetClient;

public class NetTelnet {
	private TelnetClient telnet = new TelnetClient();
	private InputStream in;
	private PrintStream out;
	private char prompt = '$';

	// 普通用户结束
	public NetTelnet(String ip, int port) throws Exception{
		telnet.setConnectTimeout(1000);
		telnet.connect(ip, port);
		in = telnet.getInputStream();
		out = new PrintStream(telnet.getOutputStream());
		// 根据root用户设置结束符
	}

	/** * 读取分析结果 * * @param pattern * @return */
	public String readUntil(String pattern) {
		try {
			char lastChar = pattern.charAt(pattern.length() - 1);
			StringBuffer sb = new StringBuffer();
			char ch = (char) in.read();
			while (true) {
				if(ch==10){
					return sb.toString();
				}
				sb.append(ch); 
				if (ch == lastChar) {
					if (sb.toString().endsWith(pattern)) {
						return sb.toString();
					}
				}
				ch = (char) in.read();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** * 写操作 * * @param value */
	private void write(String value) {
		try {
			out.println(value);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** * 向目标发送命令字符串 * * @param command * @return */
	public String sendCommand(String command) {
		try {
			write(command);
			return readUntil(prompt + " ");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** * 关闭连接 */
	public void disconnect() {
		try {
			telnet.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			System.out.println("启动Telnet...");
			String ip = "192.168.1.130";
			int port = 19019;
			NetTelnet telnet = new NetTelnet(ip, port);
			telnet.sendCommand("time|grep _");
			while(true){
				System.out.println(telnet.readUntil("$"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
