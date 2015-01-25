/**
 * 
 */
package com.st.mcc.rtm.dao.process;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.st.mcc.rtm.bean.BeanLastRun;
import com.st.mcc.rtm.bean.BeanRTMHeader;
import com.st.mcc.rtm.dao.atom.AtomRTMContent;
import com.st.mcc.rtm.dao.atom.AtomRTMHeader;
import com.st.mcc.rtm.dao.atom.AtomUtil;
import com.st.mcc.rtm.util.JDBCUtil;
import com.st.mcc.rtm.util.exception.TechnicalException;

import static com.st.mcc.rtm.util.AppConstants.*;


public class ProcessRegistration 
{
	
	private static Logger _logger = Logger.getLogger(ProcessRegistration.class.getName());
	
	private static final String CS_CLASSNAME = "ProcessRegistration";
	
	public synchronized void insert(BeanRTMHeader rtmHeader) throws TechnicalException, SQLException 
	{	
		Connection connection = null;
		
		try	
		{						
			// add synchronize on 13 Sep 11. for deadlock issue.
//			synchronized (ProcessRegistration.class) 
//			{	
                connection = new JDBCUtil("connectionManager").getConnection();
				_logger.debug("Loading TT: connection.isClosed(): " + connection.isClosed());
				
				//gsb 1.01.01B: DB connection retry if connection is closed
	            for (int i = 0; connection.isClosed()==true && i < DB_CONN_RETRY; i++)
	            {
	            	wait(DB_CONN_RETRY_TIME);  //1.02.00: FindBugs
//	                Thread.sleep(DB_CONN_RETRY_TIME);
	                _logger.debug("Retry getting db connection: " + (i+1) + " times");
	                connection = new JDBCUtil("connectionManager").getConnection();
	            }
	            if (connection.isClosed() == true)
	            {
	            	throw new SQLException("Unable to get db connection: " + ERR_CLOSE_CONN);
	            }
				
				AtomRTMHeader atomRTMHeader = new AtomRTMHeader(connection);

				_logger.info("Load method: " + rtmHeader.getLoadMethod());
				
				if (rtmHeader.getLoadMethod().compareTo(LOAD_NO)!=0)
				{					
					// Insert TT header
					if (rtmHeader.getLoadMethod().compareTo(LOAD_FULL)==0)
					{
						atomRTMHeader.insert(rtmHeader, false);
						_logger.info("[DONE]: TT header inserted to DB. (1st loading)");
					}
					else if (rtmHeader.getLoadMethod().compareTo(LOAD_FULL_DEL)==0)
					{
						atomRTMHeader.insert(rtmHeader, true);
						_logger.info("[DONE]: TT header inserted to DB.");
					}
					else if (rtmHeader.getLoadMethod().compareTo(LOAD_PARTIAL)==0)
					{
						atomRTMHeader.update(rtmHeader);
						_logger.info("[DONE]: TT header updated to DB.");
					}
					    
					if (rtmHeader.getRtmContent().size() > 0)
					{
						// Insert TT touchdown bin string
					    _logger.debug("ProcessRegistration: Start inserting touchdown bin string...");
					    AtomRTMContent atomRTMContent = new AtomRTMContent(connection);
					    atomRTMContent.insertBatch(rtmHeader);  //1.01.01B					    
//					    atomRTMContent.insertBatchPrep(rtmHeader); //1.01.01D: TODO
					    _logger.info("[DONE]: TT touchdown bin string inserted to DB.");
					}
					else 
					{
						_logger.info("No TT touchdown bin string to insert.");
					}
				    
					//1.01.01B
					atomRTMHeader.updateFinalTtHeaderInfo(rtmHeader);
				    _logger.info("[DONE]: Final TT header info. updated.");
				}
//		    }
		} 
		catch (SQLException se) 
		{
			throw new SQLException(se.getMessage());
		}
		catch (Exception ex) 
		{
			throw new TechnicalException(CS_CLASSNAME, ex.getMessage(), ex);
		}
		finally 
		{
			// The connection is now useless for this operation, release it
            JDBCUtil.closeConnection(connection);
			notify();  //1.02.00: FindBugs
		}
	}
	
	
	/** 1.01.01
	 * Retrieve TT last run records from DB
	 * @param ttHeader: TT object which contains TT file content
	 * @return
	 * @throws Exception
	 */
	public synchronized BeanLastRun getLastRunRec(BeanRTMHeader ttHeader) throws Exception 
	{
		Connection connection = null;
		BeanLastRun lastRunRec = null;
		
		try
		{
//			synchronized (ProcessRegistration.class) 
//			{
				connection = new JDBCUtil("connectionManager").getConnection();
	
	            //gsb 1.01.01B: DB connection retry if connection is closed
	            for (int i = 0; connection.isClosed()==true && i < DB_CONN_RETRY; i++)
	            {
	                wait(DB_CONN_RETRY_TIME);  //1.02.00: FindBugs
//	            	Thread.sleep(DB_CONN_RETRY_TIME);
	                _logger.debug("Retry getting db connection: " + (i+1) + " times");
	                connection = new JDBCUtil("connectionManager").getConnection();
	            }
	            if (connection.isClosed() == true)
	            {
	            	throw new SQLException("Unable to get db connection: " + ERR_CLOSE_CONN);
	            }
				
				AtomUtil lastRunQuery = new AtomUtil(connection);
				lastRunRec = lastRunQuery.selectLastRun(ttHeader);
//			}
		}
		catch (Exception ex) 
		{
			throw new Exception(ex.getMessage());
		}
		finally 
		{
			// The connection is now useless for this operation, release it
			JDBCUtil.closeConnection(connection);
			notify();  //1.02.00: FindBugs
		}
		
		return lastRunRec;
	}
}
