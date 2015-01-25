/*
 * Program Name  		:	EmailInfoServlet.java
 * Developed by 		:	STMicroelectronics
 * Date					: 	30-Sep-2007 
 * Description			:	 
 */

package tpms;
  
import it.txt.afs.servlets.master.AfsGeneralServlet;
import org.w3c.dom.Element;
import tol.LogWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class EmailInfoServlet extends AfsGeneralServlet 
{
    LogWriter log = null;
 
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {    
        String repPage = getServletConfig().getInitParameter("repPage");
        String viewPage = getServletConfig().getInitParameter("viewPage");
        String editPage = getServletConfig().getInitParameter("editPage");
        String delPage = getServletConfig().getInitParameter("delPage");    
        String action = request.getParameter("action");
        String actionTxt = ""; 
        String nextPage=viewPage; 
        HttpSession session = request.getSession();
        String currentUser = (String) session.getAttribute("user");
      
        try 
        {      
            Element userData = CtrlServlet.getUserData(currentUser); 
          
            if (action.equals("view")) 
            {    
            	actionTxt = "EMAIL ADDRESS VIEW"; 
                nextPage = repPage;  
            } 
            
            if (action.equals("new")) 
            {
                if (CtrlServlet.isUserAdmin(userData)) 
                {   actionTxt = "NEW EMAIL ADDRESS INSERT"; 
                    nextPage = editPage; 
                } 
                else throw new TpmsException("YOU HAVE NOT SUFFICIENT PRIVILEGES", actionTxt + " NOT ALLOWED");
            }
            
            if (action.equals("delete")) 
            {      
                  if (CtrlServlet.isUserAdmin(userData)) 
                  {    
                    actionTxt = "DELETE EMAIL ADDRESS "; 
                    String emailAddress = request.getParameter("emailAddress");
                    EmailInfoMgr.deleteEmailAddress(emailAddress,session.getId(), currentUser);
                    nextPage = delPage;
                  } 
                  else 
                  {
                      TpmsException e = new TpmsException(" YOU HAVE NOT SUFFICIENT PRIVILEGES", actionTxt + " NOT ALLOWED");
                      manageError(e, request, response);
                  }
            }
         
            if (action.equals("report")) 
            {  
                if (CtrlServlet.isUserAdmin(userData)) 
                {   
                	actionTxt = "LIST EMAIL ADDRESS";
                	nextPage = repPage;	
                }
            } 
            
            if ((action.equals("save"))) 
            { 
                    actionTxt = "INSERT EMAIL ADDRESS"; 
                    String emailAddress = request.getParameter("emailAddress");
                    EmailInfoMgr.insertEmailAddress(emailAddress,session.getId(), currentUser);
                    nextPage = repPage;
            }
    
        }   
        catch (Exception e) 
        {   if (e instanceof TpmsException) ((TpmsException) e).setAction(actionTxt + " FAILURE");
            manageError(e, request, response);
        }
        manageRedirect(nextPage, request, response);
    } 
}  


