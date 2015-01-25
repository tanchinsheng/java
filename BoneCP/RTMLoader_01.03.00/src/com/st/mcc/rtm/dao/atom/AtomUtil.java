package com.st.mcc.rtm.dao.atom;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.st.mcc.rtm.bean.BeanGlobalSettings;
import com.st.mcc.rtm.bean.BeanLastRun;
import com.st.mcc.rtm.bean.BeanRTMHeader;
import com.st.mcc.rtm.util.exception.TechnicalException;

import static com.st.mcc.rtm.util.AppConstants.*;
import static com.st.mcc.rtm.util.GeneralUtil.*;

public class AtomUtil extends AtomGeneric 
{
//	private static Logger _logger = Logger.getLogger(AtomUtil.class.getName());
	Logger _logger = Logger.getLogger(this.getClass().getName());
	
	private final static String CS_CLASSNAME = "AtomUtil";
	
	public AtomUtil(Connection po_connection) 
	{
		super(po_connection);
	}
	
	public synchronized BeanLastRun selectLastRun(BeanRTMHeader ttHeader) throws TechnicalException 
	{
		StringBuffer sql= new StringBuffer("");
		String sqlStr = "";
		BeanLastRun lastRunRec = new BeanLastRun();	
		String timeZone;
		Timestamp startTime;
		Timestamp tdLastRunTime;
		
		try 
		{
			String startTimeField = getTTDateFrmDb("TS.START_TIME", ttHeader.getTimeZone()) + " START_TIME";
			String tdLastRunTimeField = getTTDateFrmDb("TS.TD_LAST_RUN_TIME", ttHeader.getTimeZone()) + " TD_LAST_RUN_TIME";
			
			sql.append("SELECT ");  // for records with touchdown bins
			sql.append(startTimeField + ", " + tdLastRunTimeField);  
			sql.append(", TS.TIMEZONE, TS.TD_SAME_LAST_RUN_TIME_CNT, TS.TESTED_UNIT_COUNT, TS.GOOD_BIN_CNT," +
					   " TS.ACC_TEST_TIME, TS.ACC_INDEX_TIME, TS.MINOR_STOP, XUT.TD_INDEX");
			sql.append(" FROM TESTER_STATUS TS, (SELECT TD_INDEX, FACIL_ID, PROD_AREA, TESTER_ID, STATION_NO");  //1.03.00: Modify to remove Acc_IT & Acc_TT
			sql.append("                         FROM   (SELECT *"); 
			sql.append("                                 FROM   X_UNIT_TESTTIME");
			sql.append("                                 WHERE  FACIL_ID = '" + ttHeader.getFacilId() + "'");
			sql.append("                                 AND    PROD_AREA = '" + ttHeader.getProdArea() + "'");
			sql.append("                                 AND    TESTER_ID = '" + ttHeader.getTesterId() + "'");
			sql.append("                                 AND    STATION_NO = '" + ttHeader.getStationNo() + "'");
			sql.append("                         ORDER BY TD_INDEX DESC)");
			sql.append("                         WHERE ROWNUM = 1) XUT");
			sql.append(" WHERE TS.FACIL_ID = XUT.FACIL_ID");
			sql.append(" AND TS.PROD_AREA = XUT.PROD_AREA");
			sql.append(" AND TS.TESTER_ID = XUT.TESTER_ID");
			sql.append(" AND TS.STATION_NO = XUT.STATION_NO");
			sql.append(" UNION");
			sql.append(" SELECT ");  // for records without touchdown bins
			sql.append(startTimeField + ", " + tdLastRunTimeField);
			sql.append(", TS.TIMEZONE, TS.TD_SAME_LAST_RUN_TIME_CNT, TS.TESTED_UNIT_COUNT, TS.GOOD_BIN_CNT," +
			           " 0 ACC_TEST_TIME, 0 ACC_INDEX_TIME, TS.MINOR_STOP, 0 TD_INDEX");
			sql.append(" FROM TESTER_STATUS TS");
			sql.append(" WHERE TS.FACIL_ID = '" + ttHeader.getFacilId() + "'");
			sql.append(" AND TS.PROD_AREA = '" + ttHeader.getProdArea() + "'");
			sql.append(" AND TS.TESTER_ID = '" + ttHeader.getTesterId() + "'");
			sql.append(" AND TS.STATION_NO = '" + ttHeader.getStationNo() + "'");
			sql.append(" AND (SELECT COUNT(1) FROM X_UNIT_TESTTIME");
			sql.append(" WHERE FACIL_ID = '" + ttHeader.getFacilId() + "'"); 
			sql.append(" AND PROD_AREA = '" + ttHeader.getProdArea() + "'"); 
			sql.append(" AND TESTER_ID = '" + ttHeader.getTesterId() + "'");
			sql.append(" AND STATION_NO = '" + ttHeader.getStationNo() + "'");
	        sql.append(") = 0");
			
			sqlStr = sql.toString();
					
			this.executeQuery(sqlStr);
			
			if (m_resultSet != null)
			{
				if (m_resultSet.next()) 
				{
					// Get the timezone and calculate the time offset
					timeZone = m_resultSet.getString("TIMEZONE");
					if (timeZone != null) lastRunRec.setTimeZone(timeZone);
				
					startTime = m_resultSet.getTimestamp("START_TIME");
					if (startTime != null) lastRunRec.setStartTime(convertDateTimeByFormat(startTime, TT_DATE_TIME_FORMAT));
					
					tdLastRunTime = m_resultSet.getTimestamp("TD_LAST_RUN_TIME");
					if (tdLastRunTime != null) lastRunRec.setTdLastRunTime(convertDateTimeByFormat(m_resultSet.getTimestamp("TD_LAST_RUN_TIME"), TT_DATE_TIME_FORMAT));
					
					lastRunRec.setTdSameLastRunTimeCnt(m_resultSet.getInt("TD_SAME_LAST_RUN_TIME_CNT"));
	                lastRunRec.setTdIndex(m_resultSet.getInt("TD_INDEX"));
	                lastRunRec.setAccIndexTime(m_resultSet.getLong("ACC_INDEX_TIME"));
	                lastRunRec.setAccTestTime(m_resultSet.getLong("ACC_TEST_TIME"));
	                lastRunRec.setTestedUnitCnt(m_resultSet.getInt("TESTED_UNIT_COUNT"));
	                lastRunRec.setGoodBinCnt(m_resultSet.getInt("GOOD_BIN_CNT"));
	                lastRunRec.setMinorStop(m_resultSet.getInt("MINOR_STOP"));
				}
				_logger.debug("Last run records found in RTM DB: " + m_resultSet.getRow());
			}

		} 
		catch (Exception ex) 
		{
			throw new TechnicalException(CS_CLASSNAME, ex.getMessage() + ": SQL: " + sqlStr, ex);
		} 
		finally 
		{
			freeAll();
		}
	
	    return lastRunRec;
	}
	
	
	/** 1.03.00: Retrieve the global settings
	 * @return
	 * @throws TechnicalException
	 */
	public synchronized BeanGlobalSettings selectGlobalSetting() throws TechnicalException 
	{
		StringBuffer sql= new StringBuffer("");
		String sqlStr = "";
		BeanGlobalSettings gs = new BeanGlobalSettings();	
		double gbit;
		
		try 
		{
			sql.append("SELECT BUDGET_INDEX_TIME_DEFAULT");
			sql.append(" FROM GLOBAL_SETTING");  
			
			sqlStr = sql.toString();
					
			this.executeQuery(sqlStr);
			
			if (m_resultSet != null)
			{
				if (m_resultSet.next()) 
				{
					gbit = m_resultSet.getDouble("BUDGET_INDEX_TIME_DEFAULT");
					gs.setDefaultBudgetIndexTime(gbit);				
				}
			}
		} 
		catch (Exception ex) 
		{
			throw new TechnicalException(CS_CLASSNAME, ex.getMessage() + ": SQL: " + sqlStr, ex);
		} 
		finally 
		{
			freeAll();
		}
	    return gs;
	}
	
	
	/**
	 * Retrieve the next oracle sequence number 
	 * @param oracleSeq: Sequence number name
	 * @return
	 * @throws TechnicalException
	 */
	public long getNextOracleSeqId(String oracleSeq) throws TechnicalException 
	{	
		long nextId = 0;
				
		try {
			String query = "SELECT " + oracleSeq + ".NEXTVAL FROM DUAL";

			this.executeQuery(query);
			
			if (m_resultSet != null && m_resultSet.next()) {
				nextId = m_resultSet.getLong(1);
			}
		} 
		catch (SQLException se) 
		{
			throw new TechnicalException(this.getClass().getName(),
					"Error while trying to get oracle seq id for " + oracleSeq, se);

		} 
		finally 
		{
			freeAll();
		}
		
		if(_logger.isDebugEnabled()) _logger.debug("next id for " + oracleSeq + " is: " + nextId);
		return nextId;
	}


}
