package com.st.mcc.rtm.dao.atom;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.st.mcc.rtm.bean.BeanRTMBins;
import com.st.mcc.rtm.bean.BeanRTMHeader;
import com.st.mcc.rtm.bean.BeanRTMContent;
import com.st.mcc.rtm.util.exception.TechnicalException;


public class AtomRTMContent extends AtomGeneric 
{	
	Logger _logger = Logger.getLogger(this.getClass().getName());
	private final static String CS_CLASSNAME = "AtomRTMHeader";

	public AtomRTMContent(Connection po_connection) {
		super(po_connection);
	}
	
	
	/** 1.01.01B
	 * @param rtmHeader: Object that contains the TT file header
	 * @throws TechnicalException
	 * @throws SQLException 
	 */
	public void insertBatch(BeanRTMHeader rtmHeader) throws TechnicalException, SQLException 
	{	
		String sqlStr = "";
		ArrayList<BeanRTMContent> tdBinsList = rtmHeader.getRtmContent();
		BeanRTMContent tdBins;
		
		try 
		{
			this.m_connection.setAutoCommit(false);
			
			for (int i = 0; i < tdBinsList.size(); i++)
	       	{
				tdBins = tdBinsList.get(i);  

				//iv: 10200
				_logger.debug("SOT->EOT, EOT->SOT, SOT->SOT, " + 
						tdBins.getTestTime() + ", " +
				        tdBins.getIndexTime() + ", " +
				        tdBins.getTestIndexTime() + ", ");				
				
				sqlStr = "INSERT INTO X_UNIT_TESTTIME (" +
				         "TD_INDEX, " + 
				         "FACIL_ID, " + 
				         "PROD_AREA, " + 
				         "TESTER_ID, " + 
				         "STATION_NO, " + 
				         "TIME_OF_RUN, " + 						
				         "TEST_MODE, " + 
				         "SOT_EOT, " + 
				         "EOT_SOT, " + 
				         "SOT_SOT) " +
//				         "ACCUMULATED_IT, " +   //1.03.00: REMOVE: UNUSED COL
//				         "ACCUMULATED_TT) " +
				         "VALUES (" +
				         (i + rtmHeader.getTdIndexStart()) + ", " +  //TouchDown Index
				         "'" + rtmHeader.getFacilId() + "', " +
				         "'" + rtmHeader.getProdArea() + "', " +
				         "'" + rtmHeader.getTesterId() + "', " +
				         "'" + rtmHeader.getStationNo() + "', " +
				         "to_timestamp('" + tdBins.getRunTime() + "', 'YYYYMMDDHH24MISS') " + rtmHeader.getTimeOffset() + ", " +
				         "'" + tdBins.getTestMode() + "', " +
				         tdBins.getTestTime() + ", " +
				         tdBins.getIndexTime() + ", " +
				         tdBins.getTestIndexTime() + ")";  
//				         tdBins.getAccumulatedIndexTime() + ", " +  //1.03.00: REMOVE: UNUSED COL
//				         tdBins.getAccumulatedTestTime() + ")";
							
				//iv-10200- new condition before addBatch
				if (tdBins.getTestTime() >= 0 &&
					tdBins.getIndexTime() >= 0 &&
					tdBins.getTestIndexTime() >= 0)
				{	
					this.addBatch(sqlStr);
				}
				
				BeanRTMBins[] allRTMBins = tdBins.getRTMBins();

				for (int j = 0; j <Integer.parseInt(rtmHeader.getSites()); j++) 
				{
					sqlStr = "INSERT INTO X_UNIT_BINNING (" +
					   	     "TD_INDEX, " +
						     "FACIL_ID, " + 
						     "PROD_AREA, " + 
						     "TESTER_ID, " + 
						     "STATION_NO, " + 
						     "TIME_OF_RUN, " + 
						     "SITE_NO, " + 
						     "HW_BINS)" +
//						     "SITE_YIELD, " +  //1.03.00: REMOVE: UNUSED COL
//						     "ACCUMULATED_YIELD) " +
						     "VALUES (" +
						     (i + rtmHeader.getTdIndexStart()) + ", " + //TouchDown Index
						     "'" + rtmHeader.getFacilId() + "', " +
						     "'" + rtmHeader.getProdArea() + "', " +
						     "'" + rtmHeader.getTesterId() + "', " +
						     "'" + rtmHeader.getStationNo() + "', " +
						     "to_timestamp('" + tdBins.getRunTime() + "', 'YYYYMMDDHH24MISS') " + rtmHeader.getTimeOffset() + ", " +
						     allRTMBins[j].getSITE_NO() + ", " +
						     allRTMBins[j].getHW_BIN() + ")";
//						     allRTMBins[j].getSITE_YIELD()+ ", " +  //1.03.00: REMOVE: UNUSED COL
//						     allRTMBins[j].getAccumulatedYield()+ ")";

					this.addBatch(sqlStr);

				}
			}
			
			this.executeBatch();					
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
			freeBatch();
		}
	}
	
	
	/** 1.01.01D: GSB: RESERVED FOR FUTURE
	 * @param rtmHeader: Object that contains the TT file header
	 * @throws TechnicalException
	 * @throws SQLException 
	 */
//	public void insertBatchPrep(BeanRTMHeader rtmHeader) throws TechnicalException, SQLException 
//	{	
//		final int TD_INDEX = 1;
//		final int FACIL_ID = 2;
//		final int PROD_AREA = 3;
//		final int TESTER_ID = 4;
//		final int STATION_NO = 5;
//		final int TIME_OF_RUN = 6;
//		final int TEST_MODE = 7;
//		final int SOT_EOT = 8;
//		final int EOT_SOT = 9;
//		final int SOT_SOT = 10;
//		final int ACCUMULATED_IT = 11;
//		final int ACCUMULATED_TT = 12;
//		
//		final int SITE_NO = 7;
//		final int HW_BINS = 8;
//		final int SITE_YIELD = 9;
//		final int ACCUMULATED_YIELD = 10;
//		
//		String sqlStr = "";
//		ArrayList<BeanRTMContent> tdBinsList = rtmHeader.getRtmContent();
//		BeanRTMContent tdBins;
//		
//		try 
//		{		
//			this.m_connection.setAutoCommit(false);
//
//			sqlStr = "INSERT INTO X_UNIT_TESTTIME ("
//			         + "TD_INDEX, " 
//			         + "FACIL_ID, " 
//			         + "PROD_AREA, " 
//			         + "TESTER_ID, "
//			         + "STATION_NO, "
//			         + "TIME_OF_RUN, "
//			         + "TEST_MODE, " 
//			         + "SOT_EOT, " 
//			         + "EOT_SOT, " 
//			         + "SOT_SOT, "
//			         + "ACCUMULATED_IT, " 
//			         + "ACCUMULATED_TT) "
//			         + "VALUES (?, ?, ?, ?, ?, TO_TIMESTAMP(?, '" + TT_DATE_TIME_ORACLE_FORMAT + "')" + rtmHeader.getTimeOffset() + ", ?, ?, ?, ?, ?, ?)";
//			
////			INSERT INTO X_UNIT_TESTTIME (TD_INDEX, FACIL_ID, PROD_AREA, TESTER_ID, STATION_NO, TIME_OF_RUN, TEST_MODE, SOT_EOT, EOT_SOT, SOT_SOT, ACCUMULATED_IT, ACCUMULATED_TT) VALUES (0, '0959', 'MCC2', 'GSB2', '1', 
////			to_timestamp('20110715193238', 'YYYYMMDDHH24MISS') -1/24, 'N', 11203, 1469, 12672, 1469, 11203)
//			
//			ps_batch_insert_testTime = this.m_connection.prepareStatement(sqlStr);
//			
//			sqlStr = "INSERT INTO X_UNIT_BINNING ("
//       	   	         + "TD_INDEX, "
//		             + "FACIL_ID, " 
//		             + "PROD_AREA, " 
//		             + "TESTER_ID, " 
//		             + "STATION_NO, "
//		             + "TIME_OF_RUN, "
//		             + "SITE_NO, " 
//		             + "HW_BINS, "
//		             + "SITE_YIELD, "
//		             + "ACCUMULATED_YIELD) " 
//		             + "VALUES (?, ?, ?, ?, ?, TO_TIMESTAMP(?, '" + TT_DATE_TIME_ORACLE_FORMAT + "')" + rtmHeader.getTimeOffset() + ", ?, ?, ?, ?)";
//			
//			ps_batch_insert_unitBin = this.m_connection.prepareStatement(sqlStr);
//			
//			for (int i = 0; i < tdBinsList.size(); i++)
//	       	{
//				tdBins = tdBinsList.get(i);  
//								
//				ps_batch_insert_testTime.setInt(TD_INDEX, i + rtmHeader.getTdIndexStart());
//				ps_batch_insert_testTime.setString(FACIL_ID, rtmHeader.getFacilId());
//				ps_batch_insert_testTime.setString(PROD_AREA, rtmHeader.getProdArea());
//				ps_batch_insert_testTime.setString(TESTER_ID, rtmHeader.getTesterId());
//				ps_batch_insert_testTime.setString(STATION_NO, rtmHeader.getStationNo());
//				ps_batch_insert_testTime.setString(TIME_OF_RUN, tdBins.getRunTime());
//				ps_batch_insert_testTime.setString(TEST_MODE, tdBins.getTestMode());
//				ps_batch_insert_testTime.setInt(SOT_EOT, tdBins.getTestTime());
//				ps_batch_insert_testTime.setInt(EOT_SOT, tdBins.getIndexTime());
//				ps_batch_insert_testTime.setInt(SOT_SOT, tdBins.getTestIndexTime());
//				ps_batch_insert_testTime.setLong(ACCUMULATED_IT, tdBins.getAccumulatedIndexTime());
//				ps_batch_insert_testTime.setLong(ACCUMULATED_TT, tdBins.getAccumulatedTestTime());
//			
//				ps_batch_insert_testTime.addBatch();
//		        
//     			BeanRTMBins[] allRTMBins = tdBins.getRTMBins();
//				for (int j = 0; j <Integer.parseInt(rtmHeader.getSites()); j++) 
//				{
//					ps_batch_insert_unitBin.setInt(TD_INDEX, i + rtmHeader.getTdIndexStart());
//					ps_batch_insert_unitBin.setString(FACIL_ID, rtmHeader.getFacilId());
//					ps_batch_insert_unitBin.setString(PROD_AREA, rtmHeader.getProdArea());
//					ps_batch_insert_unitBin.setString(TESTER_ID, rtmHeader.getTesterId());
//					ps_batch_insert_unitBin.setString(STATION_NO, rtmHeader.getStationNo());
//					ps_batch_insert_unitBin.setString(TIME_OF_RUN, tdBins.getRunTime());
//					ps_batch_insert_unitBin.setInt(SITE_NO, allRTMBins[j].getSITE_NO());
//					ps_batch_insert_unitBin.setInt(HW_BINS, allRTMBins[j].getHW_BIN());
//					ps_batch_insert_unitBin.setDouble(SITE_YIELD, allRTMBins[j].getSITE_YIELD());
//					ps_batch_insert_unitBin.setDouble(ACCUMULATED_YIELD, allRTMBins[j].getAccumulatedYield());
//					
//					ps_batch_insert_unitBin.addBatch();
//				}
//			}
//			ps_batch_insert_testTime.executeBatch();
//			ps_batch_insert_unitBin.executeBatch();
//			
//			this.commit();
//		}
//		catch (SQLException ex) 
//		{
//			_logger.debug("sqlEx: Prepare to rollback: " + ex.getMessage());
//			if (this.m_connection.isClosed() == false) this.rollback();
//			throw new SQLException(ex.getMessage());
//		}
//		catch (Exception ex) 
//		{
//			_logger.error("Prepare to rollback: " + ex.getMessage());
//			if (this.m_connection.isClosed() == false) this.rollback();
//			throw new TechnicalException(CS_CLASSNAME, ex.getMessage(), ex);
//		} 
//		finally 
//		{
//			freeBatch();
//		}
//	}
//	
	
	
	/**
	 * Insert a stack record into the database
	 * 
	 * @param reg: BeanStackResult record to insert
	 */
//	public void insert(BeanRTMHeader rtmHeader) throws TechnicalException 
//	{	
//		String sqlStr = "";
//		ArrayList<BeanRTMContent> tdBinsList = rtmHeader.getRtmContent();
//		BeanRTMContent tdBins;
//				
//		try 
//		{
//			for (int i = 0; i < tdBinsList.size(); i++)
//	       	{
//				tdBins = tdBinsList.get(i);  
////			for (int i = 0; i < allRTM.length; i++) 
////			{
////				if (allRTM[i] != null)
////				{
//					/*query = "INSERT INTO X_UNIT_TESTTIME (" +
//					"TD_INDEX, " + 
//					"FACIL_ID, " + 
//					"PROD_AREA, " + 
//					"TESTER_ID, " + 
//					"STATION_NO, " + 
//					"TIME_OF_RUN, " + 						
//					"\"MODE\", " + 
//					"SOT_EOT, " + 
//					"EOT_SOT, " + 
//					"SOT_SOT) " + 
//					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//							
//					if (m_prepStmt_batch == null)
//					{
//						this.m_connection.setAutoCommit(false);
//						m_prepStmt_batch = this.m_connection.prepareStatement(query);
//					}
//					
//				    m_prepStmt_batch.setInt(1, i);
//				    m_prepStmt_batch.setString(2, rtmHeader.getFacilID());
//				    m_prepStmt_batch.setString(3, rtmHeader.getProdArea());
//				    m_prepStmt_batch.setString(4, rtmHeader.getTesterID());
//				    m_prepStmt_batch.setString(5, rtmHeader.getStationNO());
//				    m_prepStmt_batch.setString(6, rtmHeader.getStartTime());
//				    m_prepStmt_batch.setString(7, allRTM[i].getTestMode());
//				    m_prepStmt_batch.setString(8, allRTM[i].getTestTime());
//				    m_prepStmt_batch.setString(9, allRTM[i].getIndexTime());
//				    m_prepStmt_batch.setString(10, allRTM[i].getTestIndexTime());
//				    				
//					// Add to batch
//				    m_prepStmt_batch.addBatch();*/
//					
//					sqlStr = "INSERT INTO X_UNIT_TESTTIME (" +
//					"TD_INDEX, " + 
//					"FACIL_ID, " + 
//					"PROD_AREA, " + 
//					"TESTER_ID, " + 
//					"STATION_NO, " + 
//					"TIME_OF_RUN, " + 						
//					"TEST_MODE, " + 
//					"SOT_EOT, " + 
//					"EOT_SOT, " + 
//					"SOT_SOT, " +
//					"ACCUMULATED_IT, " + 
//					"ACCUMULATED_TT) " +
//					"VALUES (" +
//					(i + rtmHeader.getTdIndexStart()) + ", " +  //TouchDown Index
//					"'" + rtmHeader.getFacilId() + "', " +
//					"'" + rtmHeader.getProdArea() + "', " +
//					"'" + rtmHeader.getTesterId() + "', " +
//					"'" + rtmHeader.getStationNo() + "', " +
//					"to_timestamp('" + tdBins.getRunTime() + "', 'YYYYMMDDHH24MISS') " + rtmHeader.getTimeOffset() + ", " +
//					"'" + tdBins.getTestMode() + "', " +
//					tdBins.getTestTime() + ", " +
//					tdBins.getIndexTime() + ", " +
//					tdBins.getTestIndexTime() + ", " +
//					tdBins.getAccumulatedIndexTime() + ", " +
//					tdBins.getAccumulatedTestTime() + ")";
//
//					// Add to batch
//					//m_prepStmt_batch.addBatch();
//					//this.addBatch(query);
//					this.executeUpdate(sqlStr);
//					this.commit();
//
//					BeanRTMBins[] allRTMBins = tdBins.getRTMBins();
//
//					for (int j = 0; j <Integer.parseInt(rtmHeader.getSites()); j++) {
//						sqlStr = "INSERT INTO X_UNIT_BINNING (" +
//						"TD_INDEX, " +
//						"FACIL_ID, " + 
//						"PROD_AREA, " + 
//						"TESTER_ID, " + 
//						"STATION_NO, " + 
//						"TIME_OF_RUN, " + 
//						"SITE_NO, " + 
//						"HW_BINS, " +
//						"SITE_YIELD, " +
//						"ACCUMULATED_YIELD) " +
//						"VALUES (" +
//						(i + rtmHeader.getTdIndexStart()) + ", " + //TouchDown Index
//						"'" + rtmHeader.getFacilId() + "', " +
//						"'" + rtmHeader.getProdArea() + "', " +
//						"'" + rtmHeader.getTesterId() + "', " +
//						"'" + rtmHeader.getStationNo() + "', " +
//						"to_timestamp('" + tdBins.getRunTime() + "', 'YYYYMMDDHH24MISS') " + rtmHeader.getTimeOffset() + ", " +
//						allRTMBins[j].getSITE_NO() + ", " +
//						allRTMBins[j].getHW_BIN() + ", " +
//						allRTMBins[j].getSITE_YIELD()+ ", " +
//						allRTMBins[j].getAccumulatedYield()+ ")";
//
//						// Add to batch
//						//m_prepStmt_batch.addBatch();
////						this.addBatch(query);
//						this.executeQuery(sqlStr);
//						this.commit();
//				}
//			}
//		} 
//		catch (Exception ex) 
//		{
//			throw new TechnicalException(CS_CLASSNAME, ex.getMessage() + ": SQL: " + sqlStr, ex);
//		} 
//		finally 
//		{
//			freeUpdates();
//		}
//	}
}