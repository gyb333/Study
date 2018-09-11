package Study.HBase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HBaseUtils {

	//（1）时间戳到时间的转换.单一的时间戳无法给出直观的解释。
	 public static String GetTimeByStamp(String timestamp)
	 {
	  long datatime= Long.parseLong(timestamp);
	  Date date=new Date(datatime);
	  SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
	  String timeresult=format.format(date);
	  System.out.println("Time : "+timeresult);
	  return timeresult;
	 
	 }
	 //（2）时间到时间戳的转换。注意时间是字符串格式。字符串与时间的相互转换 
	 public static String GetStampByTime(String time)
	 {
	  String Stamp="";
	  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  Date date;
	  try
	  {
	   date=sdf.parse(time);
	   Stamp=date.getTime()+"000";
	   System.out.println(Stamp);
	  
	  }catch(Exception e){e.printStackTrace();}
	  return Stamp; 
	 }

}
