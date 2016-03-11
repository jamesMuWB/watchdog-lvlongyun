package per.cloudy.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FileUtils {
	private  File logFile;
	private  FileOutputStream fos;
	static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	String fileName;
	String fileFolder;
	String lastFilePath;
	boolean noDate;
	public FileUtils(String fileFolder,String fileName){
		this(fileFolder,fileName,false);
	}
	public FileUtils(String fileFolder,String fileName,boolean noDate){
		this.fileFolder=fileFolder;
		this.fileName=fileName.replaceAll(":", "-");
		this.noDate=noDate;
	}
	public FileUtils(String fileName){
		this("monitor_log",fileName);
	}
	public void checkLogFile(String fileName){
		File dir=new File(fileFolder);
		if(!dir.exists()){
			dir.mkdirs();
		}
		String filePath;
		if(noDate){
			 filePath=fileFolder+"/"+fileName.replace(":", "-");
		}else{
			 filePath=fileFolder+"/"+fileName.replace(":", "-")+"."+sdf.format(new Date());
		}
		
		if(filePath.equals(lastFilePath)){
			return;
		}
		try {
				logFile=new File(filePath);
				if(!logFile.exists()){
					logFile.createNewFile();
				}
				fos=new FileOutputStream(logFile,true);
				lastFilePath=filePath;
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	public  void append(Object o,Exception e){
		append(o.toString()+e.toString());
	}
	public void append(String str){
		checkLogFile(fileName);
		try {
			str+="\r\n";
			fos.write(str.getBytes("UTF-8"));
			fos.flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public  List<String> readFile(){
		checkLogFile(fileName);
		InputStreamReader isr;
		try {
			isr= new InputStreamReader(new FileInputStream(logFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
		BufferedReader in = new BufferedReader(isr);
		String line = null;
		List<String> list=new ArrayList<String>();
		try {
			while ((line = in.readLine()) != null) {
				list.add(line);
			}
		} catch (IOException e) {
			return Collections.emptyList();
		}finally{
			try {
				in.close();
				isr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
