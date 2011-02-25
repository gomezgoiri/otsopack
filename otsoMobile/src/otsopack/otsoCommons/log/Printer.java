/*
 * Copyright (C) 2008-2011 University of Deusto
 * 
 * All rights reserved.
 *
 * This software is licensed as described in the file COPYING, which
 * you should have received as part of this distribution.
 * 
 * This software consists of contributions made by many individuals, 
 * listed below:
 *
 * Author: Aitor Gómez Goiri <aitor.gomez@deusto.es>
 */

package otsopack.otsoCommons.log;

import java.util.Calendar;
import java.util.Date;

public class Printer {
	static private IPrintOut printout = null;
	static private IPrintOut printerr = null;
	static private IPrintOut printlog = null;
	
	static public void setPrinterOut(IPrintOut po) {
		printout = po;
	}
	
	static public void setPrinterErr(IPrintOut pe) {
		printerr = pe;
	}
	
	static public void setPrinterLog(IPrintOut pl) {
		printlog = pl;
	}
	
	static public void out_print(String msg) {
		if( printout==null ) System.out.print(msg);
		else printout.print(msg);
	}
	
	static public void out_println(String msg) {
		out_print(msg+"\n");
	}
	
	static public void out_println() {
		out_print("\n");
	}
	
	static public void err_print(String msg) {
		if( printerr==null ) System.err.print(msg);
		else printerr.print(msg);
	}
	
	static public void err_println(String msg) {
		err_print(msg+"\n");
	}
	
	static public void err_println() {
		err_print("\n");
	}
	
	/**
	 * Show a debug message.
	 * @param display
	 * 		¿Should this information be displayed? Though to be used with Props.Debug attributes.
	 * @param msg
	 * 		The message to show.
	 */
	static public void debug(boolean display, String msg) {
		if(display) {
			//Format: 2009-06-28 20:43:19,968
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
	        int nmonth = calendar.get(Calendar.MONTH);
	        int nday = calendar.get(Calendar.DAY_OF_MONTH);
	        int nhour = calendar.get(Calendar.HOUR_OF_DAY);
	        int nmin = calendar.get(Calendar.MINUTE);
	        int nsec = calendar.get(Calendar.SECOND);
	        int nmsec = calendar.get(Calendar.MILLISECOND);
	        String msec = ((nmsec<100)? "0"+nmsec : String.valueOf(nmsec));
	        if(nmsec<10) msec = "0" + msec;
	        String dateStr = calendar.get(Calendar.YEAR) + "-" +
	        				 ((nmonth<10)? "0"+nmonth : String.valueOf(nmonth)) + "-" +
	        				 ((nday<10)? "0"+nday : String.valueOf(nday)) + " " +
	        				 ((nhour<10)? "0"+nhour : String.valueOf(nhour)) + ":" +
	        				 ((nmin<10)? "0"+nmin : String.valueOf(nmin)) + ":" +
	        				 ((nsec<10)? "0"+nsec : String.valueOf(nsec)) + "," + msec;

			if( printlog==null ) System.out.print(dateStr + " " + msg + "\n");
			else printlog.print(dateStr + " " + msg + "\n");
		}
	}
}
