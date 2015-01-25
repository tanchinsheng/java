package tpms;

import it.txt.afs.servlets.master.AfsGeneralServlet;
import it.txt.general.utils.GeneralStringUtils;
import it.txt.general.utils.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import tol.LogWriter;

import tpms.utils.TpmsConfiguration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

public class NavDirServlet extends AfsGeneralServlet {
    LogWriter log = null;
    String txtFilesExtList = null;
    private TpmsConfiguration tpmsConfiguration = null;


    public void init() throws ServletException {
        if (tpmsConfiguration == null)
            tpmsConfiguration = TpmsConfiguration.getInstance();
        this.log = (LogWriter) this.getServletContext().getAttribute("log");
        this.txtFilesExtList = this.getServletContext().getInitParameter("txtFilesExtList");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String nextPage = request.getParameter("nextPage");
        String outPage = request.getParameter("outPage");
        String action = (request.getParameter("action") != null ? request.getParameter("action") : "browse");
        String curDirPath = request.getParameter("curDirPath");
        String nextDirPath = request.getParameter("nextDirPath");

        String initDir;
        if (GeneralStringUtils.isEmptyString(request.getParameter("initDirPath")) || request.getParameter("initDirPath").equals("WORK_DIR")) {
            initDir = "WORK_DIR";
        } else {
            initDir = "HOME_DIR";
        }
        boolean fileSelBool = request.getParameter("fileSelBool") != null && request.getParameter("fileSelBool").equals("Y");


        DirNavigator navDir = null;
        try {
            String user = (String) session.getAttribute("user");
            Element userData = CtrlServlet.getUserData(user);
            String initialWorkDirPath = (initDir.equals("WORK_DIR") ? getDefaultWorkDirPath(userData) : getHomeDirPath(userData));
            File initialPath = new File(initialWorkDirPath);

            if (! initialPath.exists()) {
                initialWorkDirPath = getHomeDirPath(userData);
            }

            navDir = new DirNavigator(initialWorkDirPath, log);
            navDir.setTxtFilesExt(this.txtFilesExtList);
            if (action.equals("browse")) {
                if (!curDirPath.equals("")) {
                    if (!nextDirPath.equals("..")) {
                        navDir.setDirAbsolute(nextDirPath);
                    } else {
                        navDir.setDirAbsolute(curDirPath);
                        navDir.setParentDir();
                    }
                }
                Document dirContentDoc;
                try {
                    dirContentDoc = navDir.getDirContent();
                } catch (Exception e) {
                    throw  new TpmsException("Unable to retrieve directory content!", "select path", "The system produce an error while accessing directory content. Please check the rights on the directory!<br>" + nextDirPath);
                }

                request.setAttribute("dirContentXml", dirContentDoc);
            } else {
                String selDir = (fileSelBool ? request.getParameter("editDir") : request.getParameter("editFile"));
                navDir.setDirAbsolute(selDir);
                if (fileSelBool) {
                    String selFile = request.getParameter("editFile");
                    request.setAttribute("curFilePath", selFile);
                }
            }
        } catch (Exception e) {
            manageError(e, request, response);
        }
        request.setAttribute("nextPage", nextPage);
            request.setAttribute("outPage", outPage);
            request.setAttribute("curDirPath", navDir.getDir());
            CtrlServlet.setAttributes(request);
            RequestDispatcher rDsp = getServletContext().getRequestDispatcher("/" + (action.equals("browse") ? nextPage : outPage));
            rDsp.forward(request, response);

    }


    public static String getHomeDirPath(Element userData)  {
        return XmlUtils.getVal(userData, "HOME_DIR");
    }


    public static String getDefaultWorkDirPath(Element userData) {
        String result = XmlUtils.getVal(userData, "WORK_DIR");
        //FF 17 Oct 2005 if the work_dir does not exists the home dir will be returned
        /*File workDir = new File(result);
        if (! workDir.exists() ) {
            result = getHomeDirPath(userData);
            //debug("NavDirServlet :: getDefaultWorkDirPath : user WORK_DIR does not exists... return HOME_DIR = " + result);
        }*/
        return result;
    }

}