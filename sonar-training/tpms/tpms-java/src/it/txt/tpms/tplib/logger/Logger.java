package it.txt.tpms.tplib.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private static final int DEBUG_LEVEL = 0;
	private static final int ERROR_LEVEL = 1;

	public static void error(String msg){
		log(ERROR_LEVEL, msg);
	}
	
	public static void debug(String msg){
		log(DEBUG_LEVEL, msg);
	}
	
	public static void reportError(String msg){
		error(msg + " ### Exiting with code -1");
		System.exit(-1);
	}
	
	private static void log(int level, String msg){	
		String levelText = (level == DEBUG_LEVEL) ? "DEBUG" : "ERROR"; 
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		Date date = new Date();
		String text = sdf.format(date) + " : " + levelText + " : " + msg;
		System.out.println(text);
	}
}
