package com.ted.auctionbay.timeutils;

import java.util.Date;

/*
 * TimeUtilities class for defining time functions
 */
public class TimeUtilities {
	/*
	 * Static function for returning the time difference between two given dates
	 */
	public static String timeDiff(Date startedTime, Date expirationDate) {
		long diff = expirationDate.getTime() - startedTime.getTime();

		long diffHours = diff / (60 * 60 * 1000) % 24;
		long diffDays = diff / (24 * 60 * 60 * 1000);
		long diffMinutes = diff / (60 * 1000) % 60;
		
		if(diffHours  == 0 && diffDays == 0 && diffMinutes == 0)
			return null;
		else
			return diffDays + " " +  "Days" +  " , " + diffHours + " Hours and " + diffMinutes + " " + "Minutes ";
		
	}
}
