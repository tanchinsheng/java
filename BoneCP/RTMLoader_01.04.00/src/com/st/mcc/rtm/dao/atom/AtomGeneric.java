/**
 * 
 */
package com.st.mcc.rtm.dao.atom;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import com.st.mcc.rtm.util.exception.TechnicalException;


public abstract class AtomGeneric 
{	
	private static Logger _logger = Logger.getLogger(AtomGeneric.class.getName());
	
	private final String CS_CLASSNAME = this.getClass().getName();
	
	// Instance member
	protected Connection m_connection = null;

	protected Statement m_statement = null;
	protected Statement m_statement_batch = null;
	protected Statement m_statement_update = null;
	protected ResultSet m_resultSet = null;
//	protected PreparedStatement m_statement = null;
//	protected PreparedStatement m_statement_update = null;
//    protected PreparedStatement ps_batch_insert_testTime = null;
//    protected PreparedStatement ps_batch_insert_unitBin = null;
    
	public AtomGeneric(Connection po_connection)
	{	
		m_connection = po_connection;
	}

	/**
	 * @param m_prepStmt_batch The m_prepStmt_batch to set.
	 */
//	public void setPrepBatchStatement(PreparedStatement m_prepStmt_batch) {
//		this.m_prepStmt_batch = m_prepStmt_batch;
//	}
//
//	/**
//	 * @return Returns the m_prepStmt_batch.
//	 */
//	public Statement getPrepBatchStatement() {
//		return m_prepStmt_batch;
//	}

	/**
	 * @return Returns the m_statement_batch.
	 */
//	public Statement getBatchStatement() {
//		return m_statement_batch;
//	}


	/**
	 * @param m_statement_batch The m_statement_batch to set.
	 */
//	public void setBatchStatement(Statement m_statement_batch) {
//		this.m_statement_batch = m_statement_batch;
//	}


	/**
	 * Execute query statement
	 * 
	 * @throws SQLException
	 * @throws TechnicalException
	 */
	public void executeQuery(String queryStr) throws SQLException, TechnicalException
	{	
		_logger.debug("Query to execute: " + queryStr);
		
		try
		{
			if (m_resultSet != null) {
				m_resultSet.close();
				m_resultSet = null;
			}
			
			if (m_statement == null) {
				m_statement = m_connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			}
			
			m_resultSet = m_statement.executeQuery(queryStr);
//			_logger.debug("Query executed: " + queryStr);
		}
		catch (SQLException se)
		{
			throw new SQLException("executeQuery: " + se.getMessage() + ": SQL: " + queryStr);
		}
		catch (Exception ex)  /* catch for other exception. add on 13Sep11. */
		{
			throw new TechnicalException(CS_CLASSNAME, ex.getMessage(), ex);
		}
		
	}
	
	
	/** 1.01.01B
	 * Add statement for batch execution
	 * 
	 * @throws SQLException
	 * @throws TechnicalException
	 */
	public void addBatch(String queryStr) throws SQLException, TechnicalException	
	{	
		try	
		{
			_logger.debug("Add SQL to batch: " + queryStr);
			
			if (m_statement_batch == null)
			{
				m_statement_batch = m_connection.createStatement();
//				m_statement_batch = m_connection.prepareStatement(queryStr);
			}
			
			m_statement_batch.addBatch(queryStr);
//			m_statement_batch.addBatch();
			
		} 
		catch (SQLException se)
		{
			throw new SQLException(se.getMessage() + ": SQL: " + queryStr);
		}
		catch (Exception ex)
		{
			throw new TechnicalException(CS_CLASSNAME, ex.getMessage() + ": SQL: " + queryStr, ex);
		}
	}
	
	
	/** 1.01.01B
	 * Do batch execution
	 * 
	 * @throws SQLException
	 * @throws TechnicalException
	 */
	public void executeBatch() throws SQLException, TechnicalException	
	{	
		try	
		{
			_logger.debug("Execute batch of statements...");
			m_statement_batch.executeBatch();
			_logger.debug("Batch sql executed.");
		} 
		catch (SQLException se) 
		{
//			throw new TechnicalException(CS_CLASSNAME, "Execute batch SQLs: " + se.getMessage(), se);
			throw new SQLException("Execute batch SQLs: " + se.getMessage());
		}
		catch (Exception ex)
		{
			throw new TechnicalException(CS_CLASSNAME, ex.getMessage(), ex);
		}
		finally 
		{
			freeBatch();
		}
	}


	/**
	 * Commit changes
	 * 
	 * @throws TechnicalException
	 */
	public void commit() throws SQLException, TechnicalException
	{
		try	
		{
			// Release any DB locks
			m_connection.commit();
			_logger.debug("Query committed");	
		} 
		catch (SQLException se) 
		{
			throw new SQLException("Failed to commit changes: " + se.getMessage());
		}
		catch (Exception ex)
		{
			throw new TechnicalException(CS_CLASSNAME, "Failed to commit changes: " + ex.getMessage(), ex);
		}
	}

	/**
	 * Rollback changes
	 * 
	 * @throws TechnicalException
	 */
	public void rollback() throws SQLException, TechnicalException 
	{
		try	
		{
			m_connection.rollback();
			_logger.debug("Rollback changes.");
		} 
		catch (SQLException se) 
		{
			throw new SQLException("Failed to rollback changes: " + se.getMessage());
		}
		catch (Exception ex)
		{
			throw new TechnicalException(CS_CLASSNAME, "Failed to rollback changes: " + ex.getMessage(), ex);
		}
	}
	
	
	public void freeAll() 
	{
		try	
		{
			if (m_resultSet != null) {
				m_resultSet.close();
				m_resultSet = null;
			}
			
			if (m_statement != null) {
				m_statement.close();
				m_statement = null;
			}
		} 
		catch (SQLException se) 
		{
			_logger.error(se.getMessage());
		}
	}
	
	
	public void freeBatch() 
	{
		try	
		{
			if (m_statement_batch != null) {
				m_statement_batch.clearBatch();
				m_statement_batch.close();
				m_statement_batch = null;
			}
			
//			if (ps_batch_insert_testTime != null) {
//				ps_batch_insert_testTime.clearBatch();
//				ps_batch_insert_testTime.close();
//				ps_batch_insert_testTime = null;
//			}	
//			
//			if (ps_batch_insert_unitBin != null) {
//				ps_batch_insert_unitBin.clearBatch();
//				ps_batch_insert_unitBin.close();
//				ps_batch_insert_unitBin = null;
//			}
			
//			if (m_prepStmt_batch != null) {
//				m_prepStmt_batch.clearBatch();
//				m_prepStmt_batch.close();
//				m_prepStmt_batch = null;	
//			}
			
			
		} 
		catch (SQLException se) 
		{
			_logger.error(se.getMessage());
		}
	}
	
	
	public void freeUpdates() 
	{
		try	
		{
			if (m_statement_update != null) {
				m_statement_update.close();
				m_statement_update = null;
			}
		} 
		catch (SQLException se) 
		{
			_logger.error(se.getMessage());
		}
	}
	
	
	/** 1.01.01: NOT USED
	 * Do batch execution
	 * 
	 * @throws SQLException
	 * @throws TechnicalException
	 */
//	public void executePrepBatch() throws SQLException, TechnicalException	
//	{	
//		try	{
//			_logger.debug("Execute batch of Prepare statements...");
//			
//			// replace Statement by PrepareStatement when use executeBatch() function.
//			///m_statement_batch.executeBatch();
//			m_prepStmt_batch.executeBatch();
//			
//		} 
//		catch (SQLException se) {
//			throw new TechnicalException(CS_CLASSNAME, se.getMessage() + ": Failed to execute batch of statements", se);
//		
//		} 
//		finally {
//			freeBatch();
//		}
//	}
	
	
	/** 1.01.01C
	 * Execute updating statement
	 * 
	 * @throws TechnicalException
	 * @return either the row count for INSERT, UPDATE or DELETE statements, 
	 * or 0 for SQL statements that return nothing 
	 * @throws SQLException 
	 */
	public void executeUpdate(String queryStr) throws TechnicalException, SQLException 
	{
		try	
		{
			_logger.debug("Query to execute: " + queryStr);
//			if (m_statement_update == null) m_statement_update = m_connection.prepareStatement(queryStr);			
//			m_statement_update.executeUpdate();
			if (m_statement_update == null) m_statement_update = m_connection.createStatement();	
			m_statement_update.executeUpdate(queryStr);
		} 
		catch (SQLException se) 
		{
			throw new SQLException("executeUpdate: " + se.getMessage());
		} 
		catch (Exception ex)
		{
			throw new TechnicalException(CS_CLASSNAME, ex.getMessage(), ex);
		}
		finally
		{
			freeUpdates();
		}
	}

}
