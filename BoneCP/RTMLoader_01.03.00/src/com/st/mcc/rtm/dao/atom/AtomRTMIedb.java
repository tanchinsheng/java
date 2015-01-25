package com.st.mcc.rtm.dao.atom;

import java.sql.Connection;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.st.mcc.rtm.bean.BeanGlobalSettings;
import com.st.mcc.rtm.bean.BeanRTMHeader;
import com.st.mcc.rtm.dao.manager.StConnectionManager;
import com.st.mcc.rtm.util.exception.TechnicalException;


public class AtomRTMIedb extends AtomGeneric 
{
	private static Logger _logger = Logger.getLogger(AtomRTMIedb.class.getName());

	private final static String CS_CLASSNAME = "AtomRTMIedb";

	public AtomRTMIedb(Connection po_connection) {
		super(po_connection);
	}

	
	/**
	 * Select a record from IEDB database
	 * 
	 * @param reg: BeanStackResult record to insert
	 */
	public synchronized void query(BeanRTMHeader rtmHeader) throws TechnicalException 
	{
		final String BUDGET_TEST_TIME = "BUDGET_TEST_TIME";
		final String BUDGET_INDEX_TIME = "BUDGET_INDEX_TIME";
		final String BUDGET_INDEX_TIME_DEFAULT = "T_INDX_TM";
		final String BUDGET_YIELD = "FWD_YLD";
		final String GLOBAL_CONFIG = "Global config.";
		
		StringBuffer queryStr = new StringBuffer("");  //1.03.00
		
		try 
		{
			String flowType_1 = "NVL(TB042.MWR_T_INDX_TM, TB042.MTE_T_INDX_TM) " + BUDGET_INDEX_TIME_DEFAULT;
			String flowType_2 = "NVL(TB042.MWR_T_INDX_TM, TB042.MTE_T_INDX_TM) " + BUDGET_INDEX_TIME;  //Default
			double budgetTestTime = -1;
			double budgetIndexTime = -1;  //1.03.00: Default from global setting
			double budgetYield = -1;
			String testType = "";  //1.03.00: Test type
			
			// 1.01.01B
			String handlerId = rtmHeader.getHandlerId();
			String flowType = rtmHeader.getFlowType();
			String arteac = rtmHeader.getArteac();
			
			//1.03.00
			String testerType = rtmHeader.getTesterType();
			String facilId = rtmHeader.getFacilId();  //plant code
			String salesType = rtmHeader.getSalesType();
            String handlerType = "";
            String msgBIT = "IEDB";
            
			if (handlerId == null) 
			{
				_logger.warn("Invalid Handler Id.");
				return; 
			}
			
			if (flowType == null)
			{
				_logger.warn("Invalid Flow Type.");
				return; 
			}
			
			if (arteac == null)
			{
				_logger.warn("Invalid Arteac.");
				return; 
			}
			
			/*** Derive the "Step Type" from "Flow Type" ***/
			// AMB
			//1.03.00: REMOVE: flowType.contains("FT1")
			if (flowType.contains("-AMB"))
			{
				flowType_2 = "NVL(TB042.MWR_T_INDX_AMB, TB042.MTE_T_INDX_AMB) " + BUDGET_INDEX_TIME;
				testType = "T-A";
			}
			
			// HOT
			//1.03.00: REMOVE: flowType.contains("FT2")
			else if (flowType.contains("-HOT"))
			{
				flowType_2 = "NVL(TB042.MWR_T_INDX_HOT, TB042.MTE_T_INDX_HOT) " + BUDGET_INDEX_TIME;
				testType = "T-H";
			}
			
			// COLD
			//1.03.00: REMOVE: flowType.contains("FT3")
			else if (flowType.contains("-COLD"))
			{
				flowType_2 = "NVL(TB042.MWR_T_INDX_CLD, TB042.MTE_T_INDX_CLD) " + BUDGET_INDEX_TIME;
				testType = "T-C";
			}
			
			//1.03.00: Handler Type must decode from Handler ID as shown below
            //		   FW2 handler Id naming convention: HDLR-[BRAND]-[MODEL]-[COUNTER], extract 3rd segment [MODEL]
            //		   Non-FW2 handler Id naming convention: [BRAND]-[MODEL]-[COUNTER], extract 2nd segment [MODEL]
			String[] handlerModel = handlerId.split("-");
			if (handlerModel[0].compareTo("HDLR")==0)
			{
				handlerType = handlerModel[2];  //FW2 handler Id
			}
			else
			{
				handlerType = handlerModel[1];  //Non-FW2 handler Id
			}
		
			if (!rtmHeader.getArteac().equals("NORMAL") && StringUtils.isNotBlank(testType))  //1.03.00 
			{
				testType = testType + "%";
			}

			queryStr.append("SELECT TB042.PROC " + BUDGET_TEST_TIME);
			queryStr.append(", " + flowType_1);
			queryStr.append(", " + flowType_2);
			queryStr.append(", TB041.FWD_YLD " + BUDGET_YIELD);
			queryStr.append(" FROM TB042_IEDB_PRIS_WITH_FORMULA TB042, TB041_IEDB_PRIS TB041");
			queryStr.append(" WHERE TB042.ALT LIKE 'S%'");
			queryStr.append(" AND UPPER(TB042.PLT) = '" + facilId + "'");
			queryStr.append(" AND UPPER(TB042.IPT) LIKE '" + salesType + "'");
			queryStr.append(" AND TB042.MTE IN (");
			queryStr.append(" SELECT DISTINCT MTE FROM PTMIEDBA.VW_PTM_MTE_SBE");
			queryStr.append(" WHERE UPPER(PLT) = '" + facilId + "'");
			queryStr.append(" AND (");
			queryStr.append(" (MCE_MFY = 'TEST' AND UPPER(MCE_BML) = '" + testerType + "' AND SNE_MFY = 'HDLR' AND UPPER(SNE_BML) = '" + handlerType + "')");
			queryStr.append(" OR");
			queryStr.append(" (MCE_MFY = 'HDLR' AND UPPER(MCE_BML) = '" + handlerType + "' AND SNE_MFY = 'TEST' AND UPPER(SNE_BML) = '" + testerType + "'))");
			queryStr.append(")");
			queryStr.append(" AND TB042.PLT = TB041.PLT");
			queryStr.append(" AND TB042.IPT = TB041.IPT");
			queryStr.append(" AND TB042.STY = TB041.STY");
			queryStr.append(" AND TB042.SEQ = TB041.SEQ");
			queryStr.append(" AND TB042.RTG = TB041.RTG");
			queryStr.append(" AND TB042.MTE = TB041.MTE");
			if (StringUtils.isNotBlank(testType)) queryStr.append(" AND UPPER(TB042.STY) = '" + testType + "'");
			
			/*
			-- *** Retrieve IEDB data for testing ***
			SELECT * FROM (
			SELECT TB042.PROC BUDGET_TEST_TIME, NVL(TB042.MWR_T_INDX_TM, TB042.MTE_T_INDX_TM) T_INDX_TM, 
			NVL(TB042.MWR_T_INDX_CLD, TB042.MTE_T_INDX_CLD) BUDGET_INDEX_TIME, TB041.FWD_YLD FWD_YLD,
			TB042.ALT, TB042.PLT, TB042.IPT, TB042.MTE, TB042.STY
			FROM TB042_IEDB_PRIS_WITH_FORMULA@IEDBLINK TB042, TB041_IEDB_PRIS@IEDBLINK TB041 
			WHERE TB042.MTE IN ( SELECT DISTINCT MTE FROM PTMIEDBA.VW_PTM_MTE_SBE@IEDBLINK) 
			AND TB042.PLT = TB041.PLT 
			AND TB042.IPT = TB041.IPT 
			AND TB042.STY = TB041.STY 
			AND TB042.SEQ = TB041.SEQ 
			AND TB042.RTG = TB041.RTG 
			AND TB042.MTE = TB041.MTE )
			WHERE T_INDX_TM <> BUDGET_INDEX_TIME
			*/
			
			//1.03.00: OLD IEDB QUERY
//			query = "select " + 
//			"tb042.proc, " + 
//			flowType_1 + 
//			flowType_2 + 
//			"tb041.fwd_yld fwd_yld  " + 
//			"FROM  " + 
//			"tb042_iedb_pris_with_formula tb042, tb041_iedb_pris tb041 " + 
//			"WHERE  " + 
//			"tb042.alt like 'S%' AND " + 
//			"tb042.plt = '" + rtmHeader.getFacilId() + "' AND " + 
//			"tb042.ipt like '" + rtmHeader.getSalesType()+ "' AND " + 
//			"tb042.mte in (" + 
//				"select DISTINCT(H.MTE) FROM vw_org_mce_sne_eqpt H, vw_org_mce_sne_eqpt T " + 
//				"WHERE " + 
//				"H.PLT = '" + rtmHeader.getFacilId() + "' AND " + 
//				"H.SNE_BML = '" + handlerModel [1] + "' AND " +
//				"H.SNY_VALUE like 'HDLR%' AND " +  
//				"T.PLT = '" + rtmHeader.getFacilId() + "' AND " + 
//				"T.SNE_BML = '" + rtmHeader.getTesterType() + "' AND " +  
//				"T.SNY_VALUE like 'TEST%' AND " +
//				"T.MTE = H.MTE) AND " +
//			"tb042.plt = tb041.plt AND " + 
//			"tb042.ipt = tb041.ipt AND " + 
//			"tb042.sty = tb041.sty AND " + 
//			"tb042.seq = tb041.seq AND " + 
//			"tb042.rtg = tb041.rtg AND " + 
//			"tb042.sty = '" + STY + "' AND " + 
//			"tb042.mte = tb041.mte";
			
			this.executeQuery(queryStr.toString());

			if (m_resultSet.next()) 
			{
				_logger.debug("Record found in IEDB.");
				
				//Retrieve the budget test time
				if (StringUtils.isNotBlank(m_resultSet.getString(BUDGET_TEST_TIME)))  //1.03.00
				{
					budgetTestTime = m_resultSet.getDouble(BUDGET_TEST_TIME);
					rtmHeader.setFlagBudgetTestTime(true);
				}

//				System.out.println("BUDGET_INDEX_TIME: " + m_resultSet.getString(BUDGET_INDEX_TIME));
				
				//1.03.00: Retrieve the budget index time from IEDB
				if (StringUtils.isNotBlank(m_resultSet.getString(BUDGET_INDEX_TIME)))
				{
					budgetIndexTime = m_resultSet.getDouble(BUDGET_INDEX_TIME);
				}
				else
				{
					//1.03.00: Default from IEDB
					if (StringUtils.isNotBlank(m_resultSet.getString(BUDGET_INDEX_TIME_DEFAULT)))
					{
						budgetIndexTime = m_resultSet.getDouble(BUDGET_INDEX_TIME_DEFAULT);
//						msgBIT = msgBIT + " default";
					}
					else  //Retrieve Global default value configured in RTM DB.
					{
			            msgBIT = GLOBAL_CONFIG;
					}
				}
//				rtmHeader.setFlagBudgetIndexTime(true);  //1.03.00: REMOVE
				
				//Retrieve the budget yield
				if (StringUtils.isNotBlank(m_resultSet.getString(BUDGET_YIELD)))
				{
					budgetYield = m_resultSet.getDouble(BUDGET_YIELD);
					rtmHeader.setFlagBudgetYield(true);
				}		
				_logger.info("[DONE]: IEDB: Records retrieved successfully"); 
			}
			else
			{
		        msgBIT = GLOBAL_CONFIG;		
//				rtmHeader.setBudgetTestTime(budgetTestTime);  //1.03.00: Shifted
//				rtmHeader.setBudgetIndexTime(budgetIndexTime);
//				rtmHeader.setBudgetYield(budgetYield);
				_logger.info("[DONE]: IEDB: No records retrieved"); 
			}		
			
			if (msgBIT.compareTo(GLOBAL_CONFIG)==0 || budgetIndexTime == -1)
			{
				//1.03.00: Retrieve Global default value configured in RTM DB.
				Connection connection = StConnectionManager.getInstance().getConnection();
				AtomUtil qryGlobalSetting = new AtomUtil(connection);
				BeanGlobalSettings gs = qryGlobalSetting.selectGlobalSetting();
				budgetIndexTime = gs.getDefaultBudgetIndexTime();
				_logger.debug("Get BIT from global setting: " + budgetIndexTime);
				StConnectionManager.getInstance().freeConnection(connection);
			}	
			
			rtmHeader.setBudgetTestTime(budgetTestTime*1000);	
			rtmHeader.setBudgetIndexTime(budgetIndexTime*1000);
			rtmHeader.setBudgetYield(budgetYield*100);
			
			_logger.info("BudgetTestTime: " + budgetTestTime + " seconds");
			_logger.info("BudgetIndexTime: " + budgetIndexTime + " seconds ("+ msgBIT +")");
			_logger.info("BudgetYield: " + budgetYield);
			
		} 
		catch (Exception ex) 
		{
			throw new TechnicalException(CS_CLASSNAME, ex.getMessage(), ex);
		} 
		finally 
		{
			freeAll();
		}
	}
}