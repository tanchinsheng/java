package com.st.mcc.rtm.parser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.st.mcc.rtm.bean.BeanLastRun;
import com.st.mcc.rtm.bean.BeanRTMBins;
import com.st.mcc.rtm.bean.BeanRTMHeader;
import com.st.mcc.rtm.bean.BeanRTMContent;
import com.st.mcc.rtm.dao.atom.AtomRTMIedb;
import com.st.mcc.rtm.dao.process.ProcessRegistration;
import com.st.mcc.rtm.util.FileUtil;
import com.st.mcc.rtm.util.exception.RTMException;
import com.st.mcc.rtm.util.exception.TechnicalException;

import static com.st.mcc.rtm.util.AppConstants.*;
import static com.st.mcc.rtm.util.GeneralUtil.*;
import com.st.mcc.rtm.util.JDBCUtil;

public class TTReportParser 
{
	static Logger _logger = Logger.getLogger(TTReportParser.class.getName());
	
	// Registration to be filled
	private BeanRTMHeader ttHeader = null;
	
	// Exception returned with error detail if any
	private RTMException rtmEx = null;
	private String ttFileName = "";
	private double []testedCount = new double [64];
	private double []goodBinCount = new double [64];
	private double []sumYield = new double [64];
	private int firstSite = 0;
	private int minorStops = 0;
	
	private String[] goodbinlist = null;
	
	private int totalGoodBin = 0;
	private int testedUnitCount = 0;
	private int untestedUnitCount = 0;
	private int startQty = 0;
	private long accumulatedIndexTime = 0;
	private long accumulatedTestTime = 0;
	private String strLTOR = "";
	private int totalTdCnt = 0;  //1.01.01
	
	public TTReportParser(String filename, BeanRTMHeader rtmHeader, RTMException e) 
	{
		ttFileName = filename;
		ttHeader = rtmHeader;
		rtmEx = e;
	}
		
	/**
	 * Parse tt report file
	 * @param rtmEx: exception returned with error details
	 * @return 0 if parsing succeeds; !=0 if parsing fails 
	 * @throws  
	 */
	public int parse()  
	{	
		_logger.info("Get TT file: " + ttFileName);
		
		// Initialization
		int result = RESULT_NO_ERROR;

		if (ttHeader == null) {
			result = RESULT_ERROR;
			rtmEx.reportError(result, "Registration bean is not initialized", _logger);
			return result;
		}
		
		// Parse outlier report content
		if (result == RESULT_NO_ERROR) {
			String parserVer = "1_1";
			try 
			{				
				// Injection: Can dynamically use different method based on different config.
//				Method m = TTReportParser.class.getMethod("parseContent" + parserVer, null);
//				Object res = m.invoke(this, null);
//				result = (Integer) res;
				Method m = TTReportParser.class.getMethod("parseContent" + parserVer);
				result = (Integer) m.invoke(this);
			} 
			catch (IllegalArgumentException e) {
				result = RESULT_ERROR;
				rtmEx.reportError(result, e.getMessage(), _logger);
			} 
			catch (IllegalAccessException e) {
				result = RESULT_ERROR;
				rtmEx.reportError(result, e.getMessage(), _logger);
			} 
			catch (InvocationTargetException e) {
				result = RESULT_ERROR;
				rtmEx.reportError(result, e.getMessage(), _logger);
			} 
			catch (SecurityException e) {
				result = RESULT_ERROR;
				rtmEx.reportError(result, e.getMessage(), _logger);
			} 
			catch (NoSuchMethodException e) {
				result = RESULT_ERROR;
				rtmEx.reportError(result, e.getMessage(), _logger);
			}
			catch (Exception e) {
				result = RESULT_ERROR;
				rtmEx.reportError(result, e.getMessage(), _logger);
			}
		}
		
		return result;
	}
	
		
	/** 1.01.01: GSB
	 * Parse outlier report file's content
	 * @return 0 if parsing succeeds; !=0 if parsing fails 
	 * @throws Exception 
	 */
	public int parseContent1_1() 
	{	
		String parserVersion = "1.1";
		_logger.debug("Parse TT file's content with parser's version " + parserVersion + "...");

		// Initialization
		int result = RESULT_NO_ERROR;
		File ttFile = null;
		String[] ttFileArray;
		String line = null;
		int tdStart = -1;

		BeanLastRun lastRunRec;
		
		String ttStartTime = null;
		String ttTdLastRunTime = null;  // Last run time from last touchdown bin string in TT file
		int ttTdSameRunTimeCnt = 0;  // Num of duplicate last run time in TT file
		
		String dbStartTime = null;
		String dbTdLastRunTime = null;  // Last run time from last touchdown bin string in DB
		int dbTdSameRunTimeCnt = 0;  // Num of duplicate last run time in DB
		
		try
		{
			//1.01.01: Check for tt file existence
			ttFile = new File(ttFileName);
			if (!ttFile.exists()) throw new Exception("TT file is not existing");

			String content = FileUtils.readFileToString(ttFile);
			ttFileArray = StringUtils.splitByWholeSeparator(content, EOL_TT);		
			
			if (ttFileArray.length <= 1)
			{
				 throw new Exception("Empty file or no line feed in the file.");
			}
					
			// Find the tt header
			for (int i = 0; i < ttFileArray.length; i++)
			{
				line = ttFileArray[i].trim();
          
				if ("".equals(line)) continue;
				if (line.startsWith("===")) continue;
											
				if (line.startsWith("TIME_OF_RUN")) 
				{
					tdStart = i+1;  // Record the start line of touchdown bin string		
					break;
				}
				
				// Read tt header part
				if (tdStart == -1) parseContentHeader(line);
			} 
			
			pushNDC_ttLoad(ttHeader);
			_logger.info("[DONE]: Parse TT header: " + ttFileName);
			
			if (ttHeader.getFacilId() == null
				|| ttHeader.getProdArea() == null
				|| ttHeader.getTesterId() == null
				|| ttHeader.getStationNo() == null 
				|| ttHeader.getTimeZone() == null)
			{
				// Discard the invalid TT file
				ttHeader.setLoadMethod(LOAD_NO);
				throw new Exception("Invalid TT file: Null values");
			}
			
			// Check if there is touchdown bins string in tt file
			if (tdStart > ttFileArray.length || tdStart < 0)
			{
			    // No touchdown bins string in the tt file
				// tdStart > ttFileArray.length => keywd: "TIME_OF_RUN" found
				// tdStart < 0 => keyword: "TIME_OF_RUN" not found
				return RESULT_NO_ERROR;  // Just load the tt header
			}
			
			// Retrieve the last run infor.
			ProcessRegistration processReg = new ProcessRegistration();
			lastRunRec = processReg.getLastRunRec(ttHeader);
			ttStartTime = ttHeader.getStartTime();
			dbStartTime = lastRunRec.getStartTime();
			dbTdLastRunTime = lastRunRec.getTdLastRunTime();
			dbTdSameRunTimeCnt = lastRunRec.getTdSameLastRunTimeCnt();
			
			_logger.debug("tt start time: " + ttStartTime);
			_logger.debug("db start time: " + dbStartTime);
			
			if (ttStartTime == null)
			{
				// Discard the invalid TT file
				ttHeader.setLoadMethod(LOAD_NO);
				throw new Exception("Invalid TT file: Null tt start time");
			}
			
			if (dbStartTime == null)
			{
                // Full load tt file without DB deletion
				ttHeader.setLoadMethod(LOAD_FULL);
			}
	
			// 1.01.01B: Incoming TT file is an invalid/old file
			else if (Long.parseLong(ttStartTime) < Long.parseLong(dbStartTime))
			{
				// Discard the old TT file
				ttHeader.setLoadMethod(LOAD_NO);
				_logger.debug("LN: Invalid or old TT file.");
				return RESULT_NO_ERROR;
			}
			
			// 1.01.01B: Incoming TT file is a newer file
			else if (Long.parseLong(ttStartTime) > Long.parseLong(dbStartTime))
			{
				// Purge all records for this tester and then reload whole TT file into DB
				ttHeader.setLoadMethod(LOAD_FULL_DEL);
			}
			
			// Partial tt loading
			else if (dbStartTime != null)  // Check if it's not 1st time loading
			{			
				_logger.debug("db td last run time: " + dbTdLastRunTime);
				if (ttStartTime.compareTo(dbStartTime)==0)
				{			
					ttHeader.setLoadMethod(LOAD_PARTIAL);
					
					// Not 1st time tt loading
					// touchdown bin string exists
					if (dbTdSameRunTimeCnt > 0)
					{
						testedUnitCount = lastRunRec.getTestedUnitCnt();
						totalGoodBin = lastRunRec.getGoodBinCnt();
						accumulatedTestTime = lastRunRec.getAccTestTime();
						accumulatedIndexTime = lastRunRec.getAccIndexTime();
						minorStops = lastRunRec.getMinorStop();
						
						ttHeader.setTdCnt(lastRunRec.getTdIndex() + 1);
						ttHeader.setTdIndexStart(lastRunRec.getTdIndex() + 1);
						
						// Read the line of touchdown string
						for (int i = tdStart; i < ttFileArray.length; i++)
						{	
							line = ttFileArray[i].trim();
							if ("".equals(line)) continue;
							if (line.startsWith("===")) continue;
							
							ttTdLastRunTime = line.substring(0, 15).trim();
							ttTdLastRunTime = ttTdLastRunTime.replace("-", "");
							
							// Find the 1st run time td bin string
							if (ttTdLastRunTime.compareTo(dbTdLastRunTime)==0)
							{
								tdStart = i + dbTdSameRunTimeCnt;
								break;
							}
						}
						// no loading of same tt file as it's same as previous
						if (tdStart > ttFileArray.length -1 || 
							"".equals(ttFileArray[tdStart]) ||
							Long.parseLong(ttTdLastRunTime) < Long.parseLong(dbTdLastRunTime))
						{
							ttHeader.setLoadMethod(LOAD_NO);
							_logger.debug("LN: Same TT file.");
							return RESULT_NO_ERROR;
						}
					}
				}
			}
			
			// Connect to IEDB to retrieve the budget index time, test time and yield 
			Connection connection_IEDB = null;
			try 
			{
                connection_IEDB = new JDBCUtil("connectionManager_IEDB").getConnection();
				AtomRTMIedb atomRTMIedb = new AtomRTMIedb(connection_IEDB);	
				atomRTMIedb.query(ttHeader);
			}
			catch (TechnicalException e) 
			{
				_logger.error("IEDB: Technical exception: " + e.getMessage());
			}
			catch (SQLException e) 
			{
				_logger.error("IEDB: SQL state: " + e.getSQLState());
			}
			catch (Exception e) 
			{
				_logger.error("IEDB: Other exception: " + e.getMessage());
			}
			finally 
			{
				JDBCUtil.closeConnection(connection_IEDB);
			}
			
			// Find the last test time
			ttTdLastRunTime = "";
			if (tdStart > 0)
			{
				// loop in reverse order
				// to find the last run time, num of touchdown bin string,
				// num of same last run time line
				for (int i = ttFileArray.length-1; i >= tdStart; i--)
				{
					line = ttFileArray[i].trim();
					if ("".equals(line))
					{
						continue;
					}
					else
					{
						if (ttTdLastRunTime.length() == 0) ttTdLastRunTime = line.substring(0, 15).trim();
						
						if (ttTdLastRunTime.compareTo(line.substring(0, 15).trim())==0)
						{
							ttTdSameRunTimeCnt++;  // Record num of duplicates last run time
						}
						else
						{
							break;
						}
					}
				}
				
				int tmpTtTdSameRunTimeCnt = 0;
				
				// Set the last run time in ttHeader
				if (ttTdLastRunTime.compareTo("") != 0)
				{
					String tmpTtTdLastRunTime = ttTdLastRunTime.replace("-", "");
					ttHeader.setTdLastRunTime(tmpTtTdLastRunTime);
				
					tmpTtTdSameRunTimeCnt = ttTdSameRunTimeCnt;
					
					if (dbTdLastRunTime != null)
					{
						if (tmpTtTdLastRunTime.compareTo(dbTdLastRunTime)==0)
						tmpTtTdSameRunTimeCnt = tmpTtTdSameRunTimeCnt + dbTdSameRunTimeCnt;
					}	
				}

				// Set the same last run time count in ttHeader				
                ttHeader.setTdSameRunTimeCnt(tmpTtTdSameRunTimeCnt);
			}		
			
			line = "";	
			// Read each tt touchdown bin string
			for (int i = tdStart; i < ttFileArray.length; i++)
			{
				line = ttFileArray[i].trim();
				if ("".equals(line)) continue;
				if (line.startsWith("===")) continue;
				parseContentValue(line);
			} 
			
			_logger.info("[DONE]: Parse TT touchdown bin string");
			
			// If no touchdown bin string exists previously, set the td index start from 0
			if (dbTdSameRunTimeCnt == 0) ttHeader.setTdIndexStart(0);
			
			// Set Minor Stop
			ttHeader.setMinorStop(minorStops);
	        totalTdCnt = ttHeader.getTdCnt();
				
			// Set MCBA
			if (minorStops == 0)
			{
				ttHeader.setMcba(0);
			}
			else
			{
				if (minorStops > 0) ttHeader.setMcba(totalTdCnt/minorStops);
			}

			startQty = ttHeader.getStartQty();
			untestedUnitCount = startQty - testedUnitCount;
			if (untestedUnitCount < 0) untestedUnitCount = 0;
//				throw new Exception("startQty (" + startQty + ") < testedUnitCount (" + testedUnitCount + ")");

			ttHeader.setTestedUnitCount(testedUnitCount);
			ttHeader.setUntestedUnitCount(untestedUnitCount);
			ttHeader.setGoodBinCnt(totalGoodBin);
			if (totalTdCnt > 0) ttHeader.setAvgTestTime(Double.valueOf(accumulatedTestTime)/Double.valueOf(totalTdCnt));
			
			//1.03.00: CHGH00000052922: Exclude Index time classified as minor stop
			double indxTdCnt = Double.valueOf(totalTdCnt-minorStops);
			if (indxTdCnt > 0) ttHeader.setAvgIndexTime(Double.valueOf(accumulatedIndexTime)/indxTdCnt);
			
			ttHeader.setAccTestTime(accumulatedTestTime);
			ttHeader.setAccIndexTime(accumulatedIndexTime);
			
			if (testedUnitCount > 0 && totalGoodBin > 0) 
			{
				ttHeader.setAvgYield(100*Float.valueOf(totalGoodBin)/Float.valueOf(testedUnitCount));
			}

			///1.03.00
			double budgetTestTime;
			
			if (ttHeader.isFlagBudgetTestTime())
			{
				budgetTestTime = ttHeader.getBudgetTestTime();
			}
			else
			{
				budgetTestTime = ttHeader.getAvgTestTime();
			}
			
			int numOfSites = Integer.parseInt(ttHeader.getSites());
			if (numOfSites > 0 && budgetTestTime >= 0)
			{
				//* 0.001 to convert back to seconds
				double estimatedEnd = (untestedUnitCount/numOfSites)*(budgetTestTime + ttHeader.getBudgetIndexTime())*0.001;  
				
				if (Math.abs(estimatedEnd) == estimatedEnd)
				{
					ttHeader.setEstimatedEnd("+" + Double.toString(estimatedEnd) + "/(24*60*60)");  // in seconds
				}
				else 
				{
					ttHeader.setEstimatedEnd(Double.toString(estimatedEnd) + "/(24*60*60)");  // in seconds
				}
			}
			else
			{
				ttHeader.setEstimatedEnd("");
				_logger.info("IEDB: Estimated end is not available due to missing of BTT and BIT");
			}
		}
		catch (IOException e) 
		{
			result = RESULT_ERROR;
			rtmEx.reportError(result, "Error in file parsing: " + e.toString(), _logger);
		}
		catch (SQLException e) 
		{	
			result = FileUtil.reloadFile(e.getMessage());
			if (result == RESULT_ERROR) rtmEx.reportError(result, e.getMessage(), _logger);	
		}
		catch (Exception e) 
		{
			result = FileUtil.reloadFile(e.getMessage());
			if (result == RESULT_ERROR) rtmEx.reportError(result, e.getMessage(), _logger);
		}
		finally
		{
			ttFile = null;
		}
		
		return result;
	}

	
	private void parseContentHeader (String lineStr)
	{
		String varName = lineStr.substring(0, 15).trim();
		String [] lineValue = lineStr.split("=");

		if (lineValue.length <= 1) return;
		
		for (int i = 0; i < lineValue.length; i ++)
		{
			lineValue[i] = lineValue[i].trim();
		}
		
		if ("START_TIME".equals(varName))
		{
			String [] data_time = lineValue[1].split("-");
			String strTime = data_time[0] + data_time[1];
			ttHeader.setStartTime(strTime);
			ttHeader.setTdLastRunTime(strTime);  //1.01.01: initialize last run time
			return;
		}
		
		if ("LOT_ID".equals(varName) || "LOT_NUMBER".equals(varName))
		{
			ttHeader.setLotId(lineValue[1]);
			return;
		}
		
		if ("SALESTYPE".equals(varName))
		{
			ttHeader.setSalesType(lineValue[1]);
			return;
		}
		
		if ("HOSTNAME".equals(varName))
		{
			ttHeader.setTesterId(lineValue[1]);
			return;
		}
		
		if ("PROGRAM".equals(varName))
		{
			ttHeader.setProgram(lineValue[1]);
			return;
		}
		
		if ("FLOWTYPE".equals(varName))
		{
			ttHeader.setFlowType(lineValue[1]);
			return;
		}
		
		if ("TESTTYPE".equals(varName))
		{
			ttHeader.setTestType(lineValue[1]);
			return;
		}
		
		if ("STATION".equals(varName))
		{
			ttHeader.setStationNo(lineValue[1]);
			return;
		}
		
		if ("HANDLER_DRIVER".equals(varName))
		{
			ttHeader.setHandlerDriver(lineValue[1]);
			return;
		}
		
		if ("HANDLER_ID".equals(varName) || "HANDLER".equals(varName))
		{
			ttHeader.setHandlerId(lineValue[1]);
			return;
		}
		
		if ("TEMPERATURE".equals(varName))
		{
			ttHeader.setTemperature(lineValue[1]);
			return;
		}
		
		if ("OPERATOR_ID".equals(varName))
		{
			ttHeader.setOperatorId(lineValue[1]);
			return;
		}
		
		if ("IN_QTY".equals(varName))
		{
			ttHeader.setStartQty(Integer.parseInt(lineValue[1]));
			return;
		}
		
		if ("TIMEZONE".equals(varName))
		{
			ttHeader.setTimeZone(lineValue[1]);
			return;
		}
		
		if ("FIRSTSITE".equals(varName))
		{
			ttHeader.setFirstSite(lineValue[1]);
			firstSite = Integer.parseInt(lineValue[1]);
			return;
		}
		
		if ("TESTERTYPE".equals(varName))
		{
			ttHeader.setTesterType(lineValue[1]);
			return;
		}
		
		if ("FACIL_ID".equals(varName))
		{
			ttHeader.setFacilId(lineValue[1]);
			return;
		}
		
		if ("PRODAREA".equals(varName))
		{
			ttHeader.setProdArea(lineValue[1]);
			return;
		}
		
		if ("GOODBINCSV".equals(varName))
		{
			ttHeader.setGoodBinCsv(lineValue[1]);
			goodbinlist = lineValue[1].split(",");
			for (int i = 0; i < goodbinlist.length; i ++)
			{
				goodbinlist[i] = goodbinlist[i].trim();
			}
			return; 
		}
		
		if ("OPENSHORTCSV".equals(varName))
		{
			ttHeader.setOpenShortCsv(lineValue[1]);
			return;
		}
		
		if ("ARTEACHBIN".equals(varName))
		{
			ttHeader.setArteachBin(lineValue[1]);
			return;
		}
		
		if ("CMODE_CODE".equals(varName))
		{
			ttHeader.setCmodeCode(lineValue[1]);
			return;
		}
	
		if ("MOD_CODE".equals(varName))
		{
			ttHeader.setModCode(lineValue[1]);
			return;
		}
		
		if ("TP_REVISON".equals(varName))
		{
			ttHeader.setTpRevision(lineValue[1]);
			return;
		}

		if ("SITES".equals(varName))
		{
			ttHeader.setSites(lineValue[1]);
			return;
		}
		
		if ("TECHCODE".equals(varName))
		{
			ttHeader.setTechCode(lineValue[1]);
			return;
		}
		
		if ("DIFFUSION".equals(varName))
		{
			ttHeader.setDifFusion(lineValue[1]);
			return;
		}
		
		if ("PACKAGETYPE".equals(varName))
		{
			ttHeader.setPackageType(lineValue[1]);
			return;
		}
		
		if ("ARTEAC".equals(varName))
		{
			ttHeader.setArteac(lineValue[1]);
			return;
		}
				
		if ("QC_SAMPLE_SIZE".equals(varName))
		{
			//ttHeader.setQcSampleSize(Integer.parseInt(lineValue[1]));
			ttHeader.setQcSampleSize(lineValue[1]);
			return;
		}
		
		if ("OPERATOR_NAME".equals(varName))
		{
			ttHeader.setOperatorName(lineValue[1]);
			return;
		}
	}
	
	
	/**
	 * Parse each touchdown bin string
	 * @param lineStr: Current touchdown bin string
	 */
	private void parseContentValue (String lineStr)
	{
		int result = RESULT_NO_ERROR;
		
		try 
		{		
//			String[] touchdownStr = StringUtils.split(lineStr);  //GSB: 1.01.01
			String tmpStr = lineStr;
			
			// get Run Time
			String strRunTime = StringUtils.left(tmpStr, TD_TIME_OF_RUN_LEN).trim();
			String [] data_time = strRunTime.split("-");
			String strTime = data_time[0] + data_time[1];
			strLTOR = strTime;
			ttHeader.setLtor(strLTOR);
			tmpStr = StringUtils.substring(tmpStr, TD_TIME_OF_RUN_LEN);
			
			// get Test Mode
			String strTestMode = StringUtils.left(tmpStr, TD_MODE_LEN).trim();
			tmpStr = StringUtils.substring(tmpStr, TD_MODE_LEN);

			// get Test Time
			int numTestTime = Integer.parseInt(StringUtils.left(tmpStr, TD_TT_LEN).trim());
			tmpStr = StringUtils.substring(tmpStr, TD_TT_LEN);
			
			// get Index Time
			int numIndexTime = Integer.parseInt(StringUtils.left(tmpStr, TD_IT_LEN).trim());
			tmpStr = StringUtils.substring(tmpStr, TD_IT_LEN);
			
			// get Test Time + Index Time
			int numTestIndexTime = Integer.parseInt(StringUtils.left(tmpStr, TD_TTIT_LEN).trim());
			tmpStr = StringUtils.substring(tmpStr, TD_TTIT_LEN).trim();
			
			BeanRTMContent rtmContent = new BeanRTMContent();
			ArrayList<BeanRTMContent> allRtmContent = ttHeader.getRtmContent(); 
			
			// WJ: Use to filter out the first line for TT file limitation
			// 1.01.01: GSB: Ignore touchdown string if Test time, Index time or TestTime+IndexTime is -ve		
			// 1.02.00: Modify to exclude -ve touchdown string but its 
		    //          respective bin string must be loaded. 			
			if (numTestTime >= 0 &&
			    numIndexTime >= 0 &&
			    numTestIndexTime >= 0)
			{
				accumulatedTestTime = accumulatedTestTime + numTestTime;
				
				int tdCnt = ttHeader.getTdCnt() + 1;  //1.02.00
				ttHeader.setTdCnt(tdCnt);

				if (numIndexTime > ttHeader.getBudgetIndexTime()*5)
				{
					minorStops = minorStops + 1;
				}
				else
				{
					//1.03.00: CHGH00000052922: Include index time only if it's not classified as Minor Stop
					accumulatedIndexTime = accumulatedIndexTime + numIndexTime;
				}
			}

				// set RunTime, TestMode, IndexTime, TestIndexTime
				rtmContent.setRunTime(strTime);
				rtmContent.setTestMode(strTestMode);
				rtmContent.setTestTime(numTestTime);
				rtmContent.setIndexTime(numIndexTime);
				rtmContent.setTestIndexTime(numTestIndexTime);
				rtmContent.setAccumulatedIndexTime(accumulatedIndexTime);
				rtmContent.setAccumulatedTestTime(accumulatedTestTime);	

				String [] SiteBin = tmpStr.split("\\s+");
				
				rtmContent.setRTMBins(new BeanRTMBins[SiteBin.length]);

				for (int site_index = 0; site_index < SiteBin.length; site_index ++)
				{
					BeanRTMBins[] allRTMBins = rtmContent.getRTMBins();
					allRTMBins[site_index] = new BeanRTMBins();
					BeanRTMBins rtmBins = allRTMBins[site_index];

					// 	Accumulate tested bin count which bin_code != -1
					//GSB: REMOVE - if (!SiteBin[site_index].toString().trim().equals("-1"))
					//YJ: 0 value include and exclude negative value.
					if (Integer.parseInt(SiteBin[site_index].trim()) >= 0)
					{
						testedUnitCount = testedUnitCount + 1;
						testedCount[site_index] = testedCount[site_index] + 1;  //+ Integer.parseInt(testedCount.get(1).toString())); // for testing result != -1
						// Accumulate good bin count which bin_code belongs to GOODBINCSV
						for (int i = 0; i < goodbinlist.length; i++)
						{
							if (Integer.parseInt(goodbinlist[i]) == Integer.parseInt(SiteBin[site_index].trim()))
							{
								goodBinCount[site_index] = 1 + goodBinCount[site_index];
								totalGoodBin = totalGoodBin + 1;
								break;
							}
						}
					}

					rtmBins.setSITE_NO(site_index + firstSite);
					rtmBins.setBIN_COUNT(testedCount [site_index]);
					rtmBins.setGOOD_BIN_COUNT(goodBinCount[site_index]);
					rtmBins.setHW_BIN(Integer.parseInt(SiteBin[site_index].trim()));
					rtmBins.setSITE_YIELD();
					sumYield [site_index] = sumYield [site_index] + rtmBins.getSITE_YIELD();
//					rtmBins.setAccumulatedYield(sumYield [site_index] + totalGoodBin);  //1.03.00: REMOVE: UNUSED COL
				}
				allRtmContent.add(rtmContent);
				ttHeader.setRtmContent(allRtmContent);

		}
		catch (Exception ex)
		{
			result = RESULT_ERROR;
			rtmEx.reportError(result, ex.toString() + ": Line: " + lineStr, _logger);
		
		}
	}
}
