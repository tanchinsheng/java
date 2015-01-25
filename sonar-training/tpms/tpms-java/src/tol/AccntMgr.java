package tol;

import org.w3c.dom.*;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Colecchia
 * Date: Jul 29, 2004
 * Time: 6:54:37 PM
 * To change this template use Options | File Templates.
 */
public class AccntMgr
{
    static String accntXmlFileName = "accounts.xml";
    Element accntsRoot=null;
    NodeList accnts=null;
    LogWriter log;

    public AccntMgr(LogWriter log, String initDir) throws Exception
    {
       this.log = log;
       accntsRoot=xmlRdr.getRoot(initDir+"/"+"local_cfg"+ "/"+accntXmlFileName,false);
       accnts=accntsRoot.getElementsByTagName("USER");
    }

    public Element getUserData(String user) throws Exception
    {
        if (accnts==null) throw new IOException("UNABLE TO PARSE THE ACCOUNTS FILE");
        for (int i=0; i<accnts.getLength(); i++)
        {
         Element el=(Element)accnts.item(i);
         if (xmlRdr.getVal(el,"NAME").equals(user)) return el;
        }
        throw new NumberFormatException("USER "+user+" UNKNOWN: LOGIN FAILED");
    }
}
