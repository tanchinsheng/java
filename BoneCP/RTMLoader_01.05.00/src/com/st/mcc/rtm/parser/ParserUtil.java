/**
 * Parser utilities class
 */
package com.st.mcc.rtm.parser;

import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;



import com.st.mcc.rtm.manager.Cfg;

/**
 * Parser utilities class contains common methods used by various report parsers
 * @author hdle
 */
public class ParserUtil {

	static Logger _logger = Logger.getLogger(ParserUtil.class.getName());
	
	/**
	 * Parse report header's line.
	 * Parsed result pair (param_name, param_value) is inserted into 
	 * Hashtable parsedHeader.
	 * Eg. one header's line: 
	 * LOGISTICS_03="APPLICATION_NAME=Streetwise Production;APPLICATION_VERSION=3.5.2.2.1;" 
	 * @return 0 if parsing succeeds; !=0 if parsing fails 
	 */
	public static int parseHeaderLine(String line, Hashtable parsedHeader) {
		
		_logger.info("Parse report file's header...");
		
		/* Initialization */
		int result = 0;
		if (line.indexOf("DATA TO USE")>=0){
			String dtu = "";
			dtu = line.substring(line.indexOf("DATA TO USE")+ 12, line.length()-2);
			parsedHeader.put("DATA TO USE", dtu);
		}
		else {
			StringTokenizer st = new StringTokenizer(line, "=\";");
			
			int tokenCnt = 0;
			String key = "";
			String value = "";
			String nextToken = "";
			
			while (st.hasMoreTokens() && (result == 0)) {
				nextToken = st.nextToken();
				tokenCnt++;
				
				if (tokenCnt == 1) {
					key = nextToken;
					if (! key.startsWith("LOGISTICS_")) {
						_logger.error("Header's line does not start with LOGISTICS_");
						result = 1;
						break;
					}
				} else {
					if (tokenCnt % 2 == 0) {
						key = nextToken;
					} else {
						value = nextToken;
						parsedHeader.put(key, value);
					}
				}
		    }
		

			if (result == 0) {
				if (tokenCnt % 2 == 0) {
					_logger.error("Header's line does not have value for one parameter");
					result = 1; 
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Parse report header's line which contains algorithm's names
	 * All algorithm's names will be stored in the algoNames vector.
	 * Eg. one header's line: 
	 * LOGISTICS_06="DATA TO USE=ref=Passing Devices,...,mean8sigma=Passing Devices;"
	 * @return 0 if parsing succeeds; !=0 if parsing fails 
	 */
	public static int parseHeaderAlgoLine(String line, Vector algoNames) {
		
		_logger.info("Parse algorithm's names in report's header...");
		
		if (algoNames == null) {
			_logger.error("Variable algoNames is NULL");
			return 1;
		}
		
		/* Initialization */
		int result = 0;
		
		StringTokenizer st = new StringTokenizer(line, "=\",;");
		
		int tokenCnt = 0;
		String key = "";
		String value = "";
		String nextToken = "";
		
		while (st.hasMoreTokens() && (result == 0)) {
			nextToken = st.nextToken();
			tokenCnt++;
			
			if (tokenCnt == 1) {
				key = nextToken;
				if (! key.startsWith("LOGISTICS_")) {
					_logger.error("Header's line does not start with LOGISTICS_");
					result = 1;
					break;
				}

			} else if (tokenCnt == 2) {
				key = nextToken;
				if (! key.equalsIgnoreCase("DATA TO USE")) {
					_logger.error("Header's line does not have with DATA TO USE");
					result = 1;
					break;
				}
			
			} else {
				if (tokenCnt % 2 != 0) {
					key = nextToken;
					
					// If algorithms in report are UNKNOWN, use config values
					if (key.equals("UNKNOWN")) {
						String confAlgoLst = Cfg.getProperty("rtmloader_conf.load.algolist");
						_logger.info("Algorithms' list is UNKNOWN, use configured values " + confAlgoLst);
						
						StringTokenizer st2 = new StringTokenizer(confAlgoLst, ", ");
						while (st2.hasMoreTokens()) {
							algoNames.add(st2.nextToken());
						}
						
						return 0;
					}
					
				} else {
					value = nextToken;		// the value "Passing Devices" is not in use 
					algoNames.add(key);
				}
			}
	    }

		if (result == 0) {
			if (tokenCnt % 2 != 0) {
				_logger.error("Header's line does not have value for one parameter");
				result = 1; 
			}
		}
		
		return result;
	}
	
	/**
	 * Return parser version to invoke right parseContent.
	 * Eg. version = "1.01" returns "1_1"
	 * @param version: version to convert
	 * @return method version to invoke 
	 */
	public static String getParserVersion(String version) {

		String methodVer = "";
		StringTokenizer st = new StringTokenizer(version, ".");
		String nextToken = ""; 
		
		while (st.hasMoreTokens()) {
			nextToken = st.nextToken();
			try {
				if (methodVer.equals("")) {
					methodVer += Integer.parseInt(nextToken);
				} else {
					methodVer += "_" + Integer.parseInt(nextToken);
				}
			} catch (NumberFormatException e) {
				_logger.error("Version " + version + " is not in right format X.XX");
				return "";
			}
		}
		
		return methodVer;
	}
	
	/**
	 * Parse report file's content header
	 * Eg. "PART_ID" <tab> "X" <tab> "Y"...
	 * @param line: header line to parse
	 * @param colCnt: number of columns to be checked
	 * @param nameMapping: column's name mapping to be returned
	 * @param nameVector: vector of all columns' names in the left-to-right order to be returned if necessary.
	 *                    if nameVector = null, don't need to update it.
	 * @return 0 if parsing succeeds; !=0 if parsing fails 
	 */
	public static int parseContentHeader(String line, int colCnt, Hashtable nameMapping, Vector nameVector) {
		
		_logger.info("Parse report file's content header|colCnt|"+colCnt);
		_logger.info("Parse report file's content header|current lin|"+line+"|");
		
		if (nameMapping == null) {
			_logger.error("nameMapping cannot be NULL");
			return 1;
		}
		
		/* Initialization */
		int result = 0;
		int repeatCnt = 0;
		int tokenCnt = 0;
		String nextToken = "";
		
		String values[] = newTokenizer(line, "\t", true);
		_logger.debug("values.length="+values.length);
		
		if (values.length<colCnt){
			_logger.error("number of content < number of hearder, not match!");
			return 1;
		}else if (values.length>colCnt){
			_logger.debug("number of content > number of hearder, do some merging.");
			tokenCnt = 0;
			repeatCnt = 0;
			for (int i=0; i<values.length; i++){
				nextToken = values[i];
				if ( i>0 ){
					if (values[i-1].trim().length()==0 && values[i].trim().length()==0){
						_logger.debug("both empty value, skip to next value.");
					}
				}
				else 
				{
					tokenCnt++;
					if (nameMapping.containsKey(nextToken)) {
						repeatCnt++;
				    } 
					else {
//						nameMapping.put(nextToken, new Integer(tokenCnt-1));
						nameMapping.put(nextToken, Integer.valueOf(tokenCnt-1));  //1.02.00: FindBugs
						if (nameVector != null) {
							nameVector.add(nextToken);
						}
					}
				}
			}
			
			if ( tokenCnt < colCnt ){
				_logger.error("number of content < number of hearder, not match!");
				return 1;
			}
		}else if (values.length==colCnt){
			_logger.debug("number of content = number of hearder, matched.");
			tokenCnt = 0;
			repeatCnt = 0;
			for (int i=0; i<values.length; i++)
			{
				nextToken = values[i];
				tokenCnt++;
				if (nameMapping.containsKey(nextToken)) {
					if (nextToken.indexOf("TEST_NAME")!=-1) {result=tokenCnt-1;};// record the index of second TEST_NAME's value
					repeatCnt++;
					_logger.debug("repeat item = " + nextToken);
				} 
				else {
					nameMapping.put(nextToken, Integer.valueOf(tokenCnt-1));  //1.02.00L FindBugs
					if (nameVector != null) {
						nameVector.add(nextToken);
					}
				}
			}
		}
		
		if (repeatCnt > 0) {
			_logger.warn("Some columns' names have been repeated, total " + repeatCnt + " repeated items");
			_logger.debug("nameMapping.size()="+nameMapping.size());
		}
		
		// NOTE: "TEST_NAME" appears twice in column' names
		if (nameMapping.size() != (colCnt-repeatCnt)) {
			_logger.error("Header file has total " + nameMapping.size() + " <> column count in summary " + colCnt);
			result = 1;
		}
		_logger.debug("result = " + result);
		return result;
	}

	   /**
	    * Converted chains describing of the parameters (the body of MessageGp for example) into 
	    * table of parameters.      
	    * @param tokenizer chains whose contents represent chains separated by a comma 
	    * @param delim Caractère delimitor    
	    * @param trimmed Boolean indicating if it is wanted that the chains is trimées or not 
	    * @return table of String     
	    * @precondition: delim chains nonempty 
	    */
	   public static String[] newTokenizer(String tokenizer, String delim, boolean trimmed)
	   {
	      String[] params = new String[0];

	      // precondition
	      boolean prec = !delim.equals("");

	      if (!prec)
	      {
	         _logger.error("newTokenizer :: Can't search empty string");
	      }else{
	         int nbParams;

	         if (tokenizer.equals(""))
	         {
	            nbParams = 1;
	         }
	         else
	         {
	            nbParams = ParserUtil.countSubString(tokenizer, delim) + 1;
	         }

	         params = new String[nbParams]; // resultat

	         int indexS = tokenizer.indexOf(delim);

	         int index = 0;

	         for (int i = 0; i < nbParams; i++)
	         {
	            indexS = tokenizer.indexOf(delim, index);

	            if (indexS > index)
	            {
	               params[i] = tokenizer.substring(index, indexS);
	            }
	            else if (i == nbParams - 1) // last one
	            {
	               params[i] = tokenizer.substring(index);
	            }
	            else
	            {
	               params[i] = "";
	            }

	            if (trimmed)
	               params[i] = params[i].trim();
	            index = indexS + 1;
	         }
	      }

	      return params;
	   }
	   
	   /**
	    * Method declaration
	    *
	    *
	    * @param inputString
	    * @param search
	    *
	    * @return
	    *
	    * @throws IllegalArgumentException
	    */
	   public static int countSubString(String inputString, String search) throws IllegalArgumentException
	   {

	      // precondition
	      boolean prec = !search.equals("");
	      int count = 0;

	      if (!prec)
	      {
	         _logger.error("countSubString :: Can't search empty string");
	      }else{
	         int index = inputString.indexOf(search);

	         while (index >= 0)
	         {
	            count++;
	            index = inputString.indexOf(search, index + 1);
	         }
	      }

	      return count;
	   }	   
}
