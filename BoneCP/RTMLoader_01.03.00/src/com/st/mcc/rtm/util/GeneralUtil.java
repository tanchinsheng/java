package com.st.mcc.rtm.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.st.mcc.rtm.bean.BeanRTMHeader;
import com.st.mcc.rtm.parser.TTReportParser;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;

import static com.st.mcc.rtm.util.AppConstants.*;


public class GeneralUtil 
{
	static Logger _logger = Logger.getLogger(TTReportParser.class.getName());
	
	
	/** 1.01.01
	 * Convert date time to certain format.
	 * @param dateTime: Date and time to convert
	 * @param format: Pattern of the date and time.
	 * @return
	 */
	public static String convertDateTimeByFormat(Date dateTime, String format)
	{
		String convDateTime = "";

		try 
		{
			 SimpleDateFormat fmt = new SimpleDateFormat(format);
			 convDateTime = fmt.format(dateTime); 
		} 
		catch (Exception ex) 
		{
			_logger.error(ex.getMessage());
		}
		return convDateTime;
	}
	
	
	/** 1.01.01
	 * Calculate the time offset based on time zone.
	 * @param timezone: Time zone (e.g GMT+8) 
	 * @param invertFlag: Flag to determine if the time zone sign need to be inverted
	 * @return
	 */
	public static String calTimeOffset(String timezone, boolean invertFlag)
	{		
		String timeOffset = "";
		
		try
		{
			String zone = timezone.substring(0, 3);
			String sign = "";
			int zoneOffset = 0;
			
			if ("GMT".equals(zone))
			{
				sign = timezone.substring(3, 4);
				zoneOffset = Integer.parseInt(timezone.substring(4));
			}
			
			if (invertFlag)
			{
				if ("GMT".equals(zone))
				{
					
					if ("-".equals(sign))
					{
						timeOffset = "+";
					}
					else
					{
						timeOffset = "-";
					}
					
					timeOffset = timeOffset + zoneOffset +  "/24";
				}
				else if ("CET".equals(zone))
				{
					timeOffset = "-1/24";
				}
			}
			else
			{
				if ("GMT".equals(zone))
				{
					sign = timezone.substring(3, 4);
					if ("-".equals(sign))
					{
						timeOffset = "-";
					}
					else
					{
						timeOffset = "+";
					}
					
					timeOffset = timeOffset + zoneOffset +  "/24";
				}
				else if ("CET".equals(zone))
				{
					timeOffset = "+1/24";
				}
			}
		}
		catch (Exception ex) 
		{
			_logger.error(ex.getMessage());
		}

		return timeOffset;
	}
	
	/** 1.01.01
	 * Generate the sql to convert any date time field from DB to actual date time in TT file
	 * based on timezone.
	 * @param field: Field name in the DB
	 * @param timezone: Time zone in tt file
	 * @return
	 */
	public static String getTTDateFrmDb(String field, String timezone)
	{
		String newStr = "";
		try
		{
			newStr = "TO_TIMESTAMP(TO_CHAR(" + field + ",'" + TT_DATE_TIME_ORACLE_FORMAT + "'), '" 
			         + TT_DATE_TIME_ORACLE_FORMAT + "') " 
			         + calTimeOffset(timezone, false);
		}
		catch (Exception ex) 
		{
			_logger.error(ex.getMessage());
		}
		return newStr;
	}
	
	
	/** 1.01.01
	 * Convert date time field into specific gmt date time before insert to DB
	 * @param field
	 * @param timeOffset
	 * @return
	 */
	public static String setTTDateToDb(String field, String timeOffset)
	{
		String newStr = EMPTY_STR;

		try
		{
			if (field != null)
			{
				newStr = "TO_TIMESTAMP('" + field + "','" + TT_DATE_TIME_ORACLE_FORMAT + "') " + timeOffset;	
			}
		}
		catch (Exception ex) 
		{
			_logger.error(ex.getMessage());
		}
		return newStr;
	}
	
    /** 1.01.01
     * This function uses NDC to push the TT loading infor. to the stack for event logging
     */    
    public static void pushNDC_ttLoad(BeanRTMHeader ttHeader)
    {
    	clearNDC_ttLoad();
    	NDC.push(ttHeader.getLotId()+ LOG_SEP + ttHeader.getStartTime());			 
    }
    
    /** 1.01.01
     * This function clears all TT loading infor. in the stack
     */    
    public static void clearNDC_ttLoad()
    {
        NDC.pop();
        NDC.remove();
    }
    
    
	/** 1.01.01C
	 * @param oldString: String to be replaced
	 * @return
	 */
    public static String replaceReservedChars(String oldString) 
	{
		String newString = EMPTY_STR;
		
		if (StringUtils.isNotBlank(oldString))  
		{ 
			newString = oldString.replace("'", "''");
			newString = newString.replace("&", "&'||'");
		}
		return newString.trim();		
	}

    
	/** 1.03.00
	 * @param oldString: String to be converted to upper case.
	 * @return
	 */
    public static String upper(String oldString) 
	{
		String newString = EMPTY_STR;

		if (StringUtils.isNotBlank(oldString)) 
		{ 
			newString = oldString.toUpperCase();
		}
		return newString;		
	}
    
}
