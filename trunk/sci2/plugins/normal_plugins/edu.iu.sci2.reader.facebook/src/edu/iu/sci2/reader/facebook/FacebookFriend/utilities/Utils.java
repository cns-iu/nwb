package edu.iu.sci2.reader.facebook.FacebookFriend.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Utils {

	public static String removeDuplicates(String str)
	{
		HashSet<String> hset = new HashSet<String>();
		String[]  strList = str.split(",");
		ArrayList<String> list = new ArrayList<String>();
		for(String val: strList )
		{
			val=val.trim();
			if(val.isEmpty()) continue;
			if(!hset.contains(val))
			{
				hset.add(val);
				list.add(val);
			}
		}
		String[] outStringList =list.toArray(new String[0]);
		String outString = Arrays.toString(outStringList).replace("[", "").replace("]", "");	
		return outString;		
	}
	
	public static String formatDate(String str)
	{
	     String day, month, year;
		
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("January", "01");map.put("February", "02");map.put("March", "03");
		map.put("April", "04");map.put("May", "05");map.put("June", "06");
		map.put("July", "07");map.put("August", "08");map.put("September", "09");
		map.put("October", "10");map.put("November", "11");map.put("December", "12");

		//contains, year, month and day
		if(str.contains(","))
		{
			String[] array = str.split(",");
			String monthDay = array[0];
			String[] mDArray = monthDay.split(" ");
			
			month = map.get(mDArray[0]);
		    year = array[1];
		    day  = mDArray[1];
		    if(day.length() ==1)
		    	day = "0"+day;
		    return (year + "-"+month+"-"+day);
		}
		else
		{
           String[] mDArray = str.split(" ");			
			month = map.get(mDArray[0]);
		    day  = mDArray[1];
		    if(day.length() ==1)
		    	day = "0"+day;
		    return (month+"-"+day);

		}
   }
	
}
