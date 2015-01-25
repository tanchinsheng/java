/**
 * 
 */
package com.st.mcc.rtm.dao.manager;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.st.ccam.framework.database.CCAMJDBCConnectionPoolManager;

import static com.st.mcc.rtm.util.AppConstants.*;


/**
 * @author hdle
 *
 */
public class StConnectionManager 
{
	static Logger _logger = Logger.getLogger(StConnectionManager.class.getName());

	// Constants
	private static final String MAIN_DB_PROPERTIES = APP_CONFIG_PATH + "connectionManager.properties";
	private static final String CS_POOLNAME = "poolRTM";   
	public static final String  MANAGER_NAME = "StConnectionManager";
	
	
	// Static instance of the StConnectionManager 
	private static StConnectionManager mo_connectionManager = null;
	
	//	CCAMJDBCConnectionPoolManager instance that will really manage connection pool
	private CCAMJDBCConnectionPoolManager mo_realConnectionManager = null;
	
	static
	{
		init(); 
	}
	
	private static synchronized void init()
	{
//		if (mo_connectionManager == null)  //1.02.00: FindBugs
//		{
			mo_connectionManager = new StConnectionManager();			
//		}		
	}
	
	
	
	// Constructor
	private StConnectionManager()
	{		
		Properties lo_connectionManagerProperties = new Properties();
		
		try	
		{
			FileInputStream fisProps = new FileInputStream(MAIN_DB_PROPERTIES);
			_logger.info("Load DB properties file " + MAIN_DB_PROPERTIES + "...");
			
			BufferedInputStream bisProps = new BufferedInputStream(fisProps);  //1.02.00: FindBugs
			lo_connectionManagerProperties.load(bisProps);
			fisProps.close();
			bisProps.close();  //1.02.00: FindBugs
			
			mo_realConnectionManager = CCAMJDBCConnectionPoolManager.getInstance();
			
			_logger.info("mo_realConnectionManager: " + mo_realConnectionManager);
			mo_realConnectionManager.addPool(CS_POOLNAME, lo_connectionManagerProperties);
		
		} 
		catch (Exception e) 
		{			
		    _logger.error(e.getMessage(), e);
		}
		
		lo_connectionManagerProperties = null;
	}
	
	public synchronized void refresh()
	{
//		mo_connectionManager = null;  //1.02.00: Findbugs
		init();
	}
	
	// Accessor
	public static StConnectionManager getInstance()
	{
		if (mo_connectionManager == null)
		{
			init();
		}
		return mo_connectionManager;
	}
	
	// Instance functions
	public Connection getConnection()
	throws Exception
	{		
		Connection conn;
		
		try
        {
            conn = mo_realConnectionManager.getConnection(CS_POOLNAME);
            
//            _logger.debug("STConn: connection.isClosed(): " + conn.isClosed());
            
            //gsb 1.01.01B: DB connection retry if connection is closed
            for (int i = 0; conn.isClosed()==true && i < DB_CONN_RETRY; i++)
            {
                Thread.sleep(DB_CONN_RETRY_TIME);
                _logger.debug("Retry getting rtm db connection: " + (i+1) + " times");
                conn = mo_realConnectionManager.getConnection(CS_POOLNAME);
            }
            
            if (conn.isClosed() == false)
            {
            	conn.setAutoCommit(true);
    		    _logger.debug("One rtm db connection reserved (" + mo_realConnectionManager.getNumberFreeConnections(CS_POOLNAME) + " free connection(s) left)");
            }
            else
            {
            	throw new SQLException("Unable to get rtm db connection: " + ERR_CLOSE_CONN);
            }
            return conn;
        } 
        catch (SQLException ex)
        {
        	_logger.error("SQL exception: " + ex.getMessage());
            throw ex;
        }
        catch (Exception ex)
        {
        	_logger.error("Connection exception: " + ex.getMessage());
            throw ex;
        }
	}
	
	
	public void freeConnection(Connection po_connection) 
	{
		if (po_connection != null)
		{
	        //Tony 18-May-2009
	        //connection.close helps to clear memory heap
			try
			{
				if (po_connection.isClosed() == false)
				{
					mo_realConnectionManager.freeConnection(CS_POOLNAME, po_connection);
					if (po_connection.isClosed() == false)
					{
						po_connection.commit();
						_logger.debug("Connection committed.");
				        po_connection.close();
				        _logger.debug("Connection closed.");
				    }
					if(_logger.isDebugEnabled()) _logger.debug("One connection freed (" + mo_realConnectionManager.getNumberFreeConnections(CS_POOLNAME) + " free connection(s) left)");
				}
			}
			catch (SQLException ex)
			{
				_logger.error("RTM DB connection close failed: " + ex.getMessage());
				mo_realConnectionManager.release();
			}
			finally
			{
			    po_connection = null;
			}
		}
	}
	
}
