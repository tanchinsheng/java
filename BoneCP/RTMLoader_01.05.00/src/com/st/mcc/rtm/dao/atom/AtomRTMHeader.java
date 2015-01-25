package com.st.mcc.rtm.dao.atom;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.st.mcc.rtm.bean.BeanRTMHeader;
import com.st.mcc.rtm.util.exception.TechnicalException;

import static com.st.mcc.rtm.util.GeneralUtil.*;
import static com.st.mcc.rtm.util.AppConstants.*;


public class AtomRTMHeader extends AtomGeneric 
{
	Logger _logger = Logger.getLogger(this.getClass().getName());
	
	private final static String CS_CLASSNAME = "AtomRTMHeader";

	public AtomRTMHeader(Connection po_connection) {
		super(po_connection);
	}


	/**
	 * Insert a TT record into the database
	 * @param rtmHeader: TT object which contains the TT file content
	 * @param delFlag: Flag to determine if delete of TT record is required
	 * @throws TechnicalException
	 * @throws SQLException 
	 */
	public synchronized void insert(BeanRTMHeader rtmHeader, boolean delFlag) throws TechnicalException, SQLException 
	{
		String sqlStr = "";
		String colEstimatedEnd = EMPTY_TIME;
		
		try 
		{
			this.m_connection.setAutoCommit(false);
			
			if (delFlag)
			{
				sqlStr =  "DELETE FROM TESTER_STATUS WHERE " +
				"FACIL_ID = " + "'" + rtmHeader.getFacilId()+ "' AND " +
				"PROD_AREA = " + "'" + rtmHeader.getProdArea()+ "' AND " +
				"TESTER_ID = " + "'" + rtmHeader.getTesterId()+ "' AND " +
				"STATION_NO = " + "'" + rtmHeader.getStationNo()+ "'";
				
				_logger.debug("Delete TT header: connection.isClosed(): " + this.m_connection.isClosed());
				this.executeUpdate(sqlStr);
				this.commit();
			}
			
//			if (rtmHeader.isFlagBudgetIndexTime()  //1.03.00: REMOVE
//			&&  rtmHeader.isFlagBudgetTestTime()
			//1.02.00: Handle TT w/o touchdown bin string
			if (StringUtils.isNotBlank(rtmHeader.getEstimatedEnd())
			&&  StringUtils.isNotBlank(rtmHeader.getLtor()) )
			{
				colEstimatedEnd = "to_timestamp(to_char(to_timestamp('" + rtmHeader.getLtor() + "', 'YYYYMMDDHH24MISS') " 
				                  + rtmHeader.getTimeOffset() + ", 'YYYYMMDDHH24MISS'), 'YYYYMMDDHH24MISS') " + rtmHeader.getEstimatedEnd();
			}
						
			sqlStr = "INSERT INTO TESTER_STATUS (" +
			"START_TIME, " + 
			"TIMEZONE, " + 
			"FACIL_ID, " + 
			"PROD_AREA, " + 
			"TESTER_TYPE, " + 
			"TESTER_ID, " + 
			"STATION_NO, " + 
			"FIRSTSITE, " + 
			"HANDLER_ID, " + 
			"HANDLER_DRIVER, " + 
			"GOODBINCSV, " + 
			"OPENSHORTCSV, " + 
			"ARTEACHBIN, " + 
			"LOT_ID, " + 
			"MOD_CODE, " + 
			"CMODE_CODE, " + 
			"SALESTYPE, " + 
			"PROGRAM, " + 
			"TP_REVISON, " + 
			"SITES, " + 
			"TEMPERATURE, " + 
			"FLOWTYPE, " + 
			"TESTTYPE, " + 
			"TECHCODE, " + 
			"DIFFUSION, " + 
			"PACKAGETYPE, " + 
			"ARTEAC, " + 
			"START_QTY, " + 
			"QC_SAMPLE_SIZE, " + 
			"OPERATOR_ID, " + 
			"OPERATOR_NAME, " + 
			"AVG_TESTTIME, " + 
			"AVG_INDEXTIME, " + 
			"AVG_YIELD, " + 
			"BUDGET_TESTTIME, " + 
			"BUDGET_INDEXTIME, " + 
			"BUDGET_YIELD, " + 
			"MINOR_STOP, " + 
			"MCBA, " + 
//			"TESTED_UNIT_COUNT, " + 
			"TD_LAST_RUN_TIME, " +  //1.01.01
//			"TD_SAME_LAST_RUN_TIME_CNT, " +   //1.01.01
//			"ACC_TEST_TIME, " +   //1.01.01
//			"ACC_INDEX_TIME, " +   //1.01.01
//			"GOOD_BIN_CNT, " +   //1.01.01
//			"UNTESTED_UNIT_COUNT, " +
			"ESTIMATED_END) " + 
			" VALUES (" + 
			setTTDateToDb(rtmHeader.getStartTime(), rtmHeader.getTimeOffset()) + ", " +
			"'" + rtmHeader.getTimeZone()+ "', " +
			"'" + rtmHeader.getFacilId()+ "', " +
			"'" + rtmHeader.getProdArea()+ "', " +
			"'" + rtmHeader.getTesterType()+ "', " +
			"'" + rtmHeader.getTesterId()+ "', " +
			"'" + rtmHeader.getStationNo()+ "', " +
			"'" + rtmHeader.getFirstSite()+ "', " +
			"'" + rtmHeader.getHandlerId()+ "', " +
			"'" + rtmHeader.getHandlerDriver()+ "', " +
			"'" + rtmHeader.getGoodBinCsv()+ "', " +
			"'" + rtmHeader.getOpenShortCsv()+ "', " +
			"'" + rtmHeader.getArteachBin()+ "', " +
			"'" + rtmHeader.getLotId()+ "', " +
			"'" + rtmHeader.getModCode()+ "', " +
			"'" + rtmHeader.getCmodeCode()+ "', " +
			"'" + rtmHeader.getOrgSalesType()+ "', " +
			"'" + rtmHeader.getProgram()+ "', " +
			"'" + rtmHeader.getTpRevision()+ "', " +
			"'" + rtmHeader.getSites()+ "', " +
			"'" + rtmHeader.getTemperature()+ "', " +
			"'" + rtmHeader.getFlowType()+ "', " +
			"'" + rtmHeader.getTestType()+ "', " +
			"'" + rtmHeader.getTechCode()+ "', " +
			"'" + rtmHeader.getDifFusion()+ "', " +
			"'" + rtmHeader.getPackageType()+ "', " +
			"'" + rtmHeader.getArteac()+ "', " +
			"'" + rtmHeader.getStartQty()+ "', " +
			"'" + rtmHeader.getQcSampleSize()+ "', " +
			"'" + rtmHeader.getOperatorId()+ "', " +
			"'" + rtmHeader.getOperatorName()+ "', " +
			"0, " +
			"0, " +
			"0, " +
//			rtmHeader.getAvgTestTime()+ ", " +
//			rtmHeader.getAvgIndexTime()+ ", " +
//			rtmHeader.getAvgYield()+ ", " +
			rtmHeader.getBudgetTestTime()+ ", " +
			rtmHeader.getBudgetIndexTime()+ ", " +
			rtmHeader.getBudgetYield()+ ", " +
			"0, " +
//			rtmHeader.getMinorStop()+ ", " +
			rtmHeader.getMcba()+ ", " + 
//			rtmHeader.getTestedUnitCount() + ", " +
			setTTDateToDb(rtmHeader.getTdLastRunTime(), rtmHeader.getTimeOffset()) + ", " +  //1.01.01
//			rtmHeader.getTdSameRunTimeCnt() + ", " +  //1.01.01
//			rtmHeader.getAccTestTime() + ", " +  //1.01.01
//			rtmHeader.getAccIndexTime() + ", " +  //1.01.01
//			rtmHeader.getGoodBinCnt() + ", " +  //1.01.01
//			rtmHeader.getUntestedUnitCount() +
			colEstimatedEnd + ")";
		
			_logger.debug("Insert TT header: connection.isClosed(): " + this.m_connection.isClosed());
			this.executeUpdate(sqlStr);
			this.commit();
		} 
		catch (SQLException ex) 
		{
			_logger.debug("sqlEx: Prepare to rollback: " + ex.getMessage());
			if (this.m_connection.isClosed() == false) this.rollback();
			throw new SQLException(ex.getMessage());
		}
		catch (Exception ex) 
		{
			_logger.error("Prepare to rollback: " + ex.getMessage());
			if (this.m_connection.isClosed() == false) this.rollback();
			throw new TechnicalException(CS_CLASSNAME, ex.getMessage(), ex);
		} 
		finally 
		{
			freeUpdates();
		}
	}
	
	
	/** 1.01.01
	 * Update an existing TT record in DB
	 * @param rtmHeader
	 * @throws TechnicalException
	 * @throws SQLException 
	 */
	public synchronized void update(BeanRTMHeader rtmHeader) throws TechnicalException, SQLException 
	{
		StringBuffer sql= new StringBuffer("");
		String sqlStr = "";
		String colEstimatedEnd = EMPTY_TIME;
		
		try 
		{
			this.m_connection.setAutoCommit(false);
			
//			if (rtmHeader.isFlagBudgetIndexTime()  //1.03.00: REMOVE
//			&&  rtmHeader.isFlagBudgetTestTime() 
			//1.02.00: Handle TT w/o touchdown bin string
			if (StringUtils.isNotBlank(rtmHeader.getEstimatedEnd())
			&&  StringUtils.isNotBlank(rtmHeader.getLtor()))
			{
				colEstimatedEnd = "to_timestamp(to_char(to_timestamp('" + rtmHeader.getLtor()+ "', 'YYYYMMDDHH24MISS') " 
				                  + rtmHeader.getTimeOffset() + ", 'YYYYMMDDHH24MISS'), 'YYYYMMDDHH24MISS') " + rtmHeader.getEstimatedEnd();
			}
								
			sql.append("UPDATE TESTER_STATUS SET ");
			sql.append("TD_LAST_RUN_TIME = ");
			sql.append(setTTDateToDb(rtmHeader.getTdLastRunTime(), rtmHeader.getTimeOffset()));  //1.01.01B
//			sql.append(", TD_SAME_LAST_RUN_TIME_CNT = " + rtmHeader.getTdSameRunTimeCnt());
//			sql.append(", GOOD_BIN_CNT = " + rtmHeader.getGoodBinCnt());
//			sql.append(", ACC_TEST_TIME = " + rtmHeader.getAccTestTime());
//			sql.append(", ACC_INDEX_TIME = " + rtmHeader.getAccIndexTime());
//			sql.append(", AVG_TESTTIME = " + rtmHeader.getAvgTestTime());
//			sql.append(", AVG_INDEXTIME = " + rtmHeader.getAvgIndexTime());
//			sql.append(", AVG_YIELD = " + rtmHeader.getAvgYield());
			sql.append(", BUDGET_TESTTIME = " + rtmHeader.getBudgetTestTime());
			sql.append(", BUDGET_INDEXTIME = " + rtmHeader.getBudgetIndexTime());
			sql.append(", BUDGET_YIELD = " + rtmHeader.getBudgetYield());
//			sql.append(", MINOR_STOP = " + rtmHeader.getMinorStop());
			sql.append(", MCBA = " + rtmHeader.getMcba());
			sql.append(", ESTIMATED_END = " + colEstimatedEnd);
//			sql.append(", TESTED_UNIT_COUNT = " + rtmHeader.getTestedUnitCount());
//			sql.append(", UNTESTED_UNIT_COUNT = " + rtmHeader.getUntestedUnitCount());
			sql.append(" WHERE ");
			sql.append("FACIL_ID ='");
			sql.append(rtmHeader.getFacilId());
			sql.append("'");
			sql.append(" AND PROD_AREA ='");
			sql.append(rtmHeader.getProdArea());
			sql.append("'");
			sql.append(" AND TESTER_ID ='");
			sql.append(rtmHeader.getTesterId());
			sql.append("'");
			sql.append(" AND STATION_NO ='");
			sql.append(rtmHeader.getStationNo());
			sql.append("'");
			sqlStr = sql.toString();	

			_logger.debug("Update TT header: connection.isClosed(): " + this.m_connection.isClosed());
            this.executeUpdate(sqlStr);
			this.commit();
		} 
		catch (SQLException ex) 
		{
			_logger.debug("sqlEx: Prepare to rollback: " + ex.getMessage());
			if (this.m_connection.isClosed() == false) this.rollback();
			throw new SQLException(ex.getMessage());
		}
		catch (Exception ex) 
		{
			_logger.error("Prepare to rollback: " + ex.getMessage());
			if (this.m_connection.isClosed() == false) this.rollback();
			throw new TechnicalException(CS_CLASSNAME, ex.getMessage() + ": SQL: " + sqlStr, ex);
		} 
		finally 
		{
			freeUpdates();
		}
	}
	
	
	/** 1.01.01B
	 * Update TD_SAME_LAST_RUN_TIME_CNT in an existing TT record in DB when touchdown
	 * bin string insertion is completed.
	 * @param rtmHeader
	 * @throws TechnicalException
	 * @throws SQLException 
	 */
	public synchronized void updateFinalTtHeaderInfo(BeanRTMHeader rtmHeader) throws TechnicalException, SQLException 
	{
		StringBuffer sql= new StringBuffer("");
		String sqlStr = "";
		
		try 
		{
			this.m_connection.setAutoCommit(false);
								
			sql.append("UPDATE TESTER_STATUS SET ");
			sql.append("TD_SAME_LAST_RUN_TIME_CNT = " + rtmHeader.getTdSameRunTimeCnt());
			sql.append(", TESTED_UNIT_COUNT = " + rtmHeader.getTestedUnitCount());
			sql.append(", UNTESTED_UNIT_COUNT = " + rtmHeader.getUntestedUnitCount());
			sql.append(", GOOD_BIN_CNT = " + rtmHeader.getGoodBinCnt());
			sql.append(", MINOR_STOP = " + rtmHeader.getMinorStop());
			sql.append(", AVG_TESTTIME = " + rtmHeader.getAvgTestTime());
			sql.append(", AVG_INDEXTIME = " + rtmHeader.getAvgIndexTime());
			sql.append(", AVG_YIELD = " + rtmHeader.getAvgYield());
			sql.append(", ACC_TEST_TIME = " + rtmHeader.getAccTestTime());
			sql.append(", ACC_INDEX_TIME = " + rtmHeader.getAccIndexTime());	
			sql.append(" WHERE ");
			sql.append("FACIL_ID ='");
			sql.append(rtmHeader.getFacilId());
			sql.append("'");
			sql.append(" AND PROD_AREA ='");
			sql.append(rtmHeader.getProdArea());
			sql.append("'");
			sql.append(" AND TESTER_ID ='");
			sql.append(rtmHeader.getTesterId());
			sql.append("'");
			sql.append(" AND STATION_NO ='");
			sql.append(rtmHeader.getStationNo());
			sql.append("'");
			sqlStr = sql.toString();
	
			_logger.debug("Final update TT header: connection.isClosed(): " + this.m_connection.isClosed());
			this.executeUpdate(sqlStr);
			this.commit();
		} 
		catch (SQLException ex)
		{
			_logger.debug("sqlEx: Prepare to rollback: " + ex.getMessage());
			this.rollback();
			throw new SQLException(ex.getMessage());
		}
		catch (Exception ex)
		{
			_logger.error("Prepare to rollback: " + ex.getMessage());
			this.rollback();
			throw new TechnicalException(CS_CLASSNAME, ex.getMessage() + ": SQL: " + sqlStr, ex);
		} 
		finally 
		{
			freeUpdates();
		}
	}
	
}