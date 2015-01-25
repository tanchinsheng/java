package tpms;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tol.LogWriter;

import tpms.utils.TpmsConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

import it.txt.general.utils.XmlUtils;

import javax.xml.parsers.ParserConfigurationException;


public class DirNavigator {
    LogWriter log;
    String curDirPath;
    String homeDirPath;
    File homeDir;
    File curDir;
    Vector txtFilesExtList;
    private TpmsConfiguration tpmsConfiguration = null;

    public void setLogWriter(LogWriter log) {
        this.log = log;
    }

    public void debug(Object msg) {
        SimpleDateFormat sdf = new SimpleDateFormat(tpmsConfiguration.getLogDateFormat());
        Date now = new Date();
        if (log != null) {
            log.p(sdf.format(now) + msg);
        } else {
            //System.out.println(sdf.format(now) + msg);
        }
    }

    public void debug(String msg) {
        SimpleDateFormat sdf = new SimpleDateFormat(tpmsConfiguration.getLogDateFormat());
        Date now = new Date();
        if (log != null) {
            log.p(sdf.format(now) + msg);
        } else {
            //System.out.println(sdf.format(now) + msg);
        }
    }

    public DirNavigator(String homeDirPath, LogWriter log) throws IOException, TpmsException {
        if (tpmsConfiguration == null)
            tpmsConfiguration = TpmsConfiguration.getInstance();
        this.log = log;
        this.homeDirPath = homeDirPath;
        setDirAbsolute(homeDirPath);
        homeDir = new File(homeDirPath);
        homeDirPath = homeDir.getCanonicalPath();
        txtFilesExtList = new Vector();
    }

    public void setDir(String dir) throws IOException, TpmsException {
        curDirPath = curDirPath + "/" + dir;
        curDir = new File(curDirPath);
        if (!curDir.isDirectory()) throw new IOException("DIRECTORY '" + curDirPath + "' DOESN'T EXIST");
        curDirPath = curDir.getCanonicalPath();
        check(curDirPath);
    }

    public void setDirAbsolute(String dir) throws IOException, TpmsException {
        curDirPath = dir;
        curDir = new File(dir);
        if (!curDir.isDirectory()) throw new IOException("DIRECTORY '" + dir + "' DOESN'T EXIST");
        curDirPath = curDir.getCanonicalPath();
        check(curDirPath);
    }

    public void setParentDir() throws IOException, TpmsException {
        curDir = curDir.getParentFile();
        curDirPath = curDir.getCanonicalPath();
    }

    public void check(String dir) throws TpmsException {
        /*
        debug("check>");
        debug(dir.replace('\\','/'));
        debug(this.homeDirPath.replace('\\','/'));*/
        if (!dir.toLowerCase().replace('\\', '/').startsWith(this.homeDirPath.toLowerCase().replace('\\', '/'))) {
            throw new TpmsException(dir, "CANNOT ACCESS THIS DIRECTORY");
        }
    }

    public String getDir() {
        return curDirPath;
    }

    public Document getDirContent() throws TpmsException {
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.UK);
        Document doc;
        try {
            doc = XmlUtils.getNewDoc("FILES");
            doc.getDocumentElement().setAttribute("path", curDir.getCanonicalPath());
        } catch (ParserConfigurationException e) {
            throw new TpmsException("Unable to retrieve directory content", "select path", "Unable to initialize directory content");
        } catch (IOException e) {
            throw new TpmsException("Unable to retrieve directory content", "select path", "The System produce an error while accessing directory content");
        }

        String[] filesArr;
        try {
            filesArr = curDir.list();
        } catch (SecurityException e) {
            throw  new TpmsException("Unable to retrieve directory content", "select path", "The system produce an error while accessing directory content. Please check the rights on the directory!");
        }


        if (!curDir.equals(homeDir)) {
            Element fileEl = XmlUtils.addEl(doc.getDocumentElement(), "FILE", "..");
            fileEl.setAttribute("dir", "Y");
        }
        for (int i = 0; (i < filesArr.length) && (i < 5000); i++) {
            Element fileEl = XmlUtils.addEl(doc.getDocumentElement(), "FILE", filesArr[i]);
            File f = new File(curDir, filesArr[i]);
            if (this.isTextFile(filesArr[i])) fileEl.setAttribute("type", "TEXT");
            fileEl.setAttribute("dir", (f.isDirectory() ? "Y" : "N"));
            fileEl.setAttribute("dateModL", Long.toString(f.lastModified()));
            fileEl.setAttribute("dateModS", df.format(new Date(f.lastModified())));
            fileEl.setAttribute("size", Long.toString(f.length()));
            fileEl.setAttribute("ext", (filesArr[i].lastIndexOf('.') >= 0 ? filesArr[i].substring(filesArr[i].lastIndexOf('.') + 1) : ""));
        }
        return doc;
    }

    public void setLog(LogWriter log) {
        this.log = log;
    }

    public void setTxtFilesExt(String lst) {
        this.txtFilesExtList = getList(lst);
    }

    public static Vector getList(String lst) {
        Vector v = new Vector();
        StringTokenizer toks = new StringTokenizer(lst, ",", false);
        while (toks.hasMoreElements()) {
            String tok = ((String) toks.nextElement()).trim().toLowerCase();
            v.addElement(tok);
        }
        return v;
    }

    public boolean isTextFile(String fileName) {
        for (int i = 0; i < this.txtFilesExtList.size(); i++) {
            if (fileName.toLowerCase().endsWith("." +  txtFilesExtList.elementAt(i))) return true;
        }
        return false;
    }


}
