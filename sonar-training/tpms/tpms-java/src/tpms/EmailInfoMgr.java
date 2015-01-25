/*
 * Program Name  		:	EmailInfoMgr.java
 * Developed by 		:	STMicroelectronics
 * Date					: 	30-Sep-2007 
 * Description			:	 
 */

package tpms;

import tpms.utils.QueryUtils;
import tpms.utils.SQLInterface;
import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List; 

public class EmailInfoMgr extends QueryUtils   
{	
	
	public static List getEmailDetails () throws TpmsException 
    {       
    	List result = new ArrayList(); 
        String query = "SELECT DISTINCT email_address FROM email ORDER BY email_address";
        debugLog("EmailInfoMgr :: getEmailDetails : query = " + query);
        SQLInterface iface = new SQLInterface();
        CachedRowSet rs = iface.execQuery(query);
        if (rs != null) {
        	try { 
            	while (rs.next()) {
            		EmailDetails emailDetails = new EmailDetails();
            		emailDetails.setEmailaddress(rs.getString(1)); 
            	    result.add(emailDetails);
            	    emailDetails = null;
            	}
        	} 
            catch (SQLException e) 
            {
            	throw new TpmsException("EmailInfoMgr :: getEmailDetails : error while fetching data " + e);
            } 
            finally {
            	if (rs != null) {
            		try {
            			rs.close();
            			rs = null;
            		} 
            		catch (SQLException e) {
            			errorLog("EmailInfoMgr :: getEmailDetails : error while closing resultset", e);
            		}
            	}
            }
            return result;
       } else {
          	return null;
       }
    }   

	public static void deleteEmailAddress(String emailAddress, String sessionId, String tpmsLogin) throws TpmsException{   
		String query = "DELETE FROM email WHERE email_address= '" + emailAddress + "' AND rownum < 2";
		debugLog("EmailInfoMgr :: deleteEmailDetails : query = " + query); 
		executeDeleteQuery(query,sessionId, tpmsLogin);
	}

	public static void insertEmailAddress(String emailAddress, String sessionId, String tpmsLogin) throws TpmsException{
		String query = "INSERT INTO email VALUES( '" + emailAddress +  "')";
		debugLog("EmailInfoMgr :: insertEmailDetails : query = " + query); 
		executeInsertQuery(query,sessionId, tpmsLogin);
	} 
}
