package per.cloudy.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	private static SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
	public static String getNowTime(){
		Date date=new Date();
		return sdf.format(date);
	}

}
