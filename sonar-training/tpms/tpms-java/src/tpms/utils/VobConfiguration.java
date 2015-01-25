package tpms.utils;

import it.txt.afs.clearcase.ClearcaseCommand;
import it.txt.afs.AfsCommonStaticClass;
import it.txt.general.utils.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tpms.TpmsException;

import java.io.File;
import java.util.Vector;


/**
 * Created by IntelliJ IDEA.
 * User: Puglisi
 * Date: Feb 18, 2006
 * Time: 4:03:28 PM
 * To change this template use Options | File Templates.
 */
public class VobConfiguration extends AfsCommonStaticClass {

    private static Element vobsRoot = null;

    private static TpmsConfiguration tpmsConfiguration = TpmsConfiguration.getInstance();


    public static void setVobsRoot(Element root) {
        vobsRoot = root;
    }



    public static NodeList editTVob(String vobName) throws TpmsException {
        String tVobDataFile = tpmsConfiguration.getWebAppDir() + File.separator + "images" + File.separator + "vobs_results.xml";
        //String repFileName = "/users/vobadm/jakarta-tomcat-3.2.3/webapps/tpms_501/images/vobs_results.xml";

        String command;
        if (tpmsConfiguration.getDebug()) {
            command = tpmsConfiguration.getWebAppDir() + File.separator + "images" + File.separator + "temp.bat";
        } else {
            removeFile(tVobDataFile);
            command = tpmsConfiguration.getCcScriptsDir() + File.separator + "vobinfo2xml.tpmsw " + tVobDataFile + " " + vobName + " T" ;
        }
        debugLog("VobConfiguration :: editTVob command : " + command);
        //lanciare il comando UNIX per verificare la configurazione del tipo di VOB specifico
        ClearcaseCommand clearcaseCommand = new ClearcaseCommand(command, false);
        clearcaseCommand.run();
        try {
            waitForBackEndOutFile(tVobDataFile, 1000);
            setVobsRoot(XmlUtils.getRoot(tVobDataFile));

            NodeList nodes;
            try {
                nodes = vobsRoot.getElementsByTagName("VOB");
            }
            catch (Exception e) {
                throw new TpmsException("VOB CONFIGURATION FROM CC Aborted!", "ACTION ABORTED", e);
            }
            if (nodes != null) return nodes;
            else {
                throw new TpmsException("NULL VOB CONFIGURATION FROM CC!", "ACTION ABORTED");
            }
        } catch (TpmsException e) {
            stop(e);
            throw new TpmsException("error1", "");
        } catch (Exception e) {
            String action = "" + " RESULT UNDEFINED";
            String msg = "CC1 OUTPUT FILE DETECTION FAILURE";
            stop(new TpmsException(msg, action, e));
            throw new TpmsException("error1", "");
        }

    }


    public static NodeList getVobConfig(String vobType) throws TpmsException {
        String repFileName = tpmsConfiguration.getWebAppDir() + File.separator + "images" + File.separator + "vobs_results.xml";
        //String repFileName = "/users/vobadm/jakarta-tomcat-3.2.3/webapps/tpms_501/images/vobs_results.xml";

        String command;
        if (tpmsConfiguration.getDebug()) {
            command = tpmsConfiguration.getWebAppDir() + File.separator + "images" + File.separator + "temp.bat";
        } else {
            removeFile(repFileName);
            command = tpmsConfiguration.getCcScriptsDir() + File.separator + "voblist2xml.tpmsw " + repFileName + " " + vobType;
        }

        //lanciare il comando UNIX per verificare la configurazione del tipo di VOB specifico
        ClearcaseCommand clearcaseCommand = new ClearcaseCommand(command, false);
        clearcaseCommand.run();
        try {
            waitForBackEndOutFile(repFileName, 1000);
            setVobsRoot(XmlUtils.getRoot(repFileName));

            NodeList nodes;
            try {
                nodes = vobsRoot.getElementsByTagName("VOB");
            }
            catch (Exception e) {
                throw new TpmsException("VOB CONFIGURATION FROM CC Aborted!", "ACTION ABORTED", e);
            }
            if (nodes != null) return nodes;
            else {
                throw new TpmsException("NULL VOB CONFIGURATION FROM CC!", "ACTION ABORTED");
            }
        } catch (TpmsException e) {
            stop(e);
            throw new TpmsException("error1", "");
        } catch (Exception e) {
            String action = "" + " RESULT UNDEFINED";
            String msg = "CC1 OUTPUT FILE DETECTION FAILURE";
            stop(new TpmsException(msg, action, e));
            throw new TpmsException("error1", "");
        }

    }


    //non lancia il comando Unix ma ritorna l'xml gia richiesto
    public static NodeList getVobsNode() throws TpmsException {
        String repFileName = tpmsConfiguration.getWebAppDir() + "/" + "images" + "/" + "vobs" + "_results.xml";
        try {
            setVobsRoot(XmlUtils.getRoot(repFileName));
        } catch (Exception e) {
            throw new TpmsException("Unable to retrieve vob data", "", e);
        }

        NodeList nodes;
        try {
            nodes = vobsRoot.getElementsByTagName("VOB");
        }
        catch (Exception e) {
            throw new TpmsException("VOB CONFIGURATION FROM CC Aborted!", "ACTION ABORTED", e);
        }
        if (nodes != null) return nodes;
        else {
            throw new TpmsException("NULL VOB CONFIGURATION FROM CC!", "ACTION ABORTED");
        }
    }

    public static Vector getVobNameList(NodeList vobNodes, String vobType) {
        Vector vobNameList = new Vector();
        Element vob;
        String currentVobType;
        for (int i = 0; i < vobNodes.getLength(); i++) {
            vob = (Element) vobNodes.item(i);

            if (vob != null) {
                currentVobType = XmlUtils.getVal(vob, "TYPE");
                if (currentVobType.equals(vobType)) {
                    vobNameList.addElement(XmlUtils.getVal(vob, "VOB_NAME"));
                }
            }
        }
        return vobNameList;
    }

    public static Vector getUnixGroupOfVob(NodeList vobNodes, String vobName) {
        Vector unixGroupList = new Vector();
        for (int j = 0; j < vobNodes.getLength(); j++) {
            Element vob = (Element) vobNodes.item(j);
            if (XmlUtils.getVal(vob, "VOB_NAME").equals(vobName)) {
                NodeList UnixGroupNodes = vob.getElementsByTagName("UNIX_GROUP");
                for (int i = 0; i < UnixGroupNodes.getLength(); i++) {
                    Element unixGroup = (Element) UnixGroupNodes.item(i);
                    if (unixGroup != null) {
                        unixGroupList.addElement(XmlUtils.getVal(unixGroup));
                    }
                }
            }

        }

        return unixGroupList;
    }

    public static void waitForBackEndOutFile(String fName, long timeOut) throws TpmsException, InterruptedException {
        for (int i = 0; (i < timeOut) && (!new File(fName).exists()); i++) {
            /**
             * aspetto che scada il time-out per la risposta
             */

            Thread.sleep(1000);
        }
        if (new File(fName).exists()) {
        } else
            throw new TpmsException("TIMEOUT EXPIRED", "GET VOB DATA" + " ABORTED");
    }

    static void stop(Exception e) {
        //todo da gestire il messaggio d'errore in caso di errore nello script UNIX
        try {
            if (e != null) {
                //session.setAttribute("exception" + "_" + REQID, e);
                //log.p("VOB-ACTION-FAILURE-" + REQID + ">" + (e.getMessage() != null ? e.getMessage() : ""));
            } else {
                /*if (session.getAttribute("exception" + "_" + REQID) != null) {
                    session.removeAttribute("exception" + "_" + REQID);
                }*/
            }
            //session.setAttribute("startBool" + "_" + REQID, new Boolean(false));
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public static void removeFile(String fileName) throws TpmsException {
        try {
            File fi = new File(fileName);
            fi.delete();
            File fo = new File(fileName);
            fo.delete();
        } catch (Exception e) {
            throw new TpmsException("Unable to Delete the previous files!", "ACTION ABORTED", e);
        }
    }
}
