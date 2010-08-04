package de.jetb.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
/**
 * Diese Klasse erweitert das klassische java.util.Date um die Darstellung in der
 * taktischen Form. Weiterhin wird ermöglicht ein taktisches Datum zu parsen.
 * @author Till Klocke
 */
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

        public TacticalDate(Date date){
            super(date.getTime());
        }
	/**
         * Gibt die taktische Zeit aus.
         * @return String in der taktischen Zeit
         */
        @Override
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

            StringBuilder out = new StringBuilder(12);
            out.append(day<10?"0"+day:day);
            out.append(hour<10?"0"+hour:hour);
            out.append(minutes<10?"0"+minutes:minutes);
            out.append(" ");
            out.append(intToMonth.get(month));
            out.append(yString);

            return out.toString();
	}
        /**
         * Parst eine String, welcher eine gültige taktische Zeit sein muss in ein
         * TacticalDate-Objekt
         * @param s String
         * @return TacticalDate
         */
        public static TacticalDate parseDateFromString(String s){
            try{
                String in = s.trim();
                String firstPart = in.substring(0, 6);
                String secondPart = in.substring(6).trim();

                String dayString = firstPart.substring(0, 2);
                String hourString = firstPart.substring(2, 4);
                String minuteString = firstPart.substring(4,6);

                int day = Integer.parseInt(dayString);
                int hour = Integer.parseInt(hourString);
                int minute = Integer.parseInt(minuteString);

                String monthString = secondPart.substring(0,3);
                String yearString = null;
                if(secondPart.length()>3){
                    yearString = secondPart.substring(3);
                }

                int month = monthToInt.get(monthString);

                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                int year = cal.get(Calendar.YEAR);
                if(yearString!=null){
                    year = Integer.parseInt(yearString);
                    year = year + 2000;
                }

                String newDateString = hour+":"+minute+" "+day+"."+month+1+"."+year;
                DateFormat formatter = new SimpleDateFormat("HH:mm dd.MM.yyyy");

                return new TacticalDate((Date)formatter.parse(newDateString));

            } catch(Exception e){
                throw new IllegalArgumentException("The given String does not "
                        + "comply with tactical date");
            }
        }
}
