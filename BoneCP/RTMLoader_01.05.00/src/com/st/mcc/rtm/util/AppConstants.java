/*******************************************************************************************
'*                                APPLICATION CONSTANTS                                    *
'*******************************************************************************************
'*                                       DESCRIPTION                                       *
'* This class contains generic constants use by the application.                           *
'*                                                                                         *
'*******************************************************************************************
'*                                         HISTORIES                                       *
'* 04/Nov/2011 - 01.01.01 - SB Goh         - First implementation.                         *
'* 08/May/2012 - 01.02.00 - SB Goh         - Add NUM_OF_TT_RELOAD.                         *
'*                                                                                         *
'*******************************************************************************************/

package com.st.mcc.rtm.util;

public class AppConstants 
{
	public final static char CHAR_CR = (char)13;
	public final static char CHAR_LF = (char)10;
	public final static String EOL_TT = Character.toString(CHAR_LF);
    
	//Separator use in the event log
    public final static String LOG_SEP = "|";
	
	public final static String TT_DATE_TIME_FORMAT = "yyyyMMddHHmmss";
	public final static String TT_DATE_TIME_ORACLE_FORMAT = "YYYYMMDDHH24MISS";
	
	public final static int DB_CONN_RETRY = 3;
	public final static int DB_CONN_RETRY_TIME = 10;  //In msecs (1 sec = 1000 msecs)	
	
	public final static String RELOAD_TT_PREFIX = "#";
	
	public final static String EMPTY_STR = "";  //1.03.00
	public final static String EMPTY_TIME = "''";  //1.03.00
	
	public final static int NUM_OF_TT_RELOAD = 10;  //1.02.00: Count the number of times the TT file is allow to reload
	
	//1.02.00B: Re-organize the config. dir and files
	public final static String APP_CONFIG_PATH = System.getProperty("user.dir") + "/config/";
	
	
	//***************
	//*** Results ***
	//***************
	public final static int RESULT_NO_ERROR = 0;
	public final static int RESULT_SAME_TT = 1;
	public final static int RESULT_FILE_RELOAD = 2;
	public final static int RESULT_ERROR = -1;
	
	//***********************
	//*** TT loading type ***
	//***********************
	// 1st time TT full loading
	public final static String LOAD_FULL = "LF";
	
	// TT full loading and deleting old records in DB
	public final static String LOAD_FULL_DEL = "LFD"; 
	
	// TT partial loading with header but not TD bins
	public final static String LOAD_PARTIAL = "LP";
	
	// No TT loading
	public final static String LOAD_NO = "LN";
	
	// Length of each param in the touchdown bin string
	public final static int TD_TIME_OF_RUN_LEN = 15;
	public final static int TD_MODE_LEN = 5;
	public final static int TD_TT_LEN = 11;
	public final static int TD_IT_LEN = 12;
	public final static int TD_TTIT_LEN = 12;
	public final static int TD_BIN_LEN = 3;
	
	//*********************
	//*** Error message ***
	//*********************
	public final static String ERR_CONSTRAINT = " constraint";
	public final static String ERR_DEADLOCK = "ORA-00060";
	public final static String ERR_CLOSE_CONN = "Closed Connection";
	//iv-10200
	public final static String ERR_PARENT_KEY = "ORA-02291";
	
}
