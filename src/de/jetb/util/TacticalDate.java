package de.jetb.util;

import java.util.Calendar;
import java.util.HashMap;

public class TacticalDate extends java.util.Date {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static HashMap<Integer,String> intToMonth = 
		new HashMap<Integer,String>();
	
	private static HashMap<String,Integer> monthToInt =
		new HashMap<String, Integer>();
	
	static{
		intToMonth.put(0, "Jan");
		intToMonth.put(1, "Feb");
		intToMonth.put(2, "Mar");
		intToMonth.put(3, "Apr");
		intToMonth.put(4, "May");
		intToMonth.put(5, "Jun");
		intToMonth.put(6, "Jul");
		intToMonth.put(7, "Aug");
		intToMonth.put(8, "Sep");
		intToMonth.put(9, "Oct");
		intToMonth.put(10, "Nov");
		intToMonth.put(11, "Dec");
		
		for(int i : intToMonth.keySet()){
			monthToInt.put(intToMonth.get(i), i);
		}
	}
	
	public TacticalDate(){
		super();
	}
	
	public TacticalDate(long date){
		super(date);
	}
	
	public String toString(){
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(this);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);
		int month = cal.get(Calendar.MONTH); //Januar ist 0
		int year = cal.get(Calendar.YEAR);
		
		String yString = ""+year;
		yString = yString.substring(yString.length()-2);
		
		StringBuffer out = new StringBuffer(12);
		out.append(day<10?"0"+day:day);
		out.append(hour<10?"0"+hour:hour);
		out.append(minutes<10?"0"+minutes:minutes);
		out.append(" ");
		out.append(intToMonth.get(month));
		out.append(yString);
		
		return null;
	}
}
