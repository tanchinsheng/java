package com.st.mcc.rtm.manager;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.log4j.Logger;

import static com.st.mcc.rtm.util.AppConstants.*;


public class Cfg 
{

	static Logger _logger = Logger.getLogger(Cfg.class.getName());
	/**
	 * Contains all the properties related to the application
	 */
	private static Hashtable _hashProp = new Hashtable();

	/**
	 * Main setup properties' filename
	 */
	private static final String MAIN_SETUP_PROPERTIES = APP_CONFIG_PATH + "rtmloader_setup.properties";
	
	public static void init() 
	{
		_logger.info("Load application properties...");
		
		_hashProp.clear();
		
		try 
		{
			Properties props = new Properties();
			
			FileInputStream fisProps = new FileInputStream(MAIN_SETUP_PROPERTIES);
			BufferedInputStream bisProps = new BufferedInputStream(fisProps);
			props.load(bisProps);
			fisProps.close();
			bisProps.close();
			
			for (Enumeration enum1 = props.propertyNames(); enum1.hasMoreElements(); ) 
			{
			    String curkey = (String)enum1.nextElement();
			    String curvalue = props.getProperty(curkey).trim();
			    
			    _hashProp.put(curkey, curvalue);
			    _logger.debug(curkey + ": " + curvalue);
			}
		
		} 
		catch (Exception e) {
			_logger.error("Property file loading error: " + e.toString());
		}
		
	}
	
	/**
	 * Return the value defined by this property "key".
	 * 
	 * @param key
	 * @return value for key
	 */
	public static String getProperty(String key) {
		String propValue = "";
		
		if (_hashProp.containsKey(key)){
			propValue =  (String) _hashProp.get(key);
		} else{
			propValue =  '!' + key + '!';
		}
		return propValue;
	}

}
