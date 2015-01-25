package tpms;

import java.io.PrintStream;
import java.io.PrintWriter;

public class TpmsException extends Exception {
    String action;
    String detail = "";
    String sysDetail = "";
    Exception e = null;

    public TpmsException(String msg) {
        super(msg);
    }

    public TpmsException(String msg, String action) {
        super(msg);
        this.action = action;
    }

    public TpmsException(String msg, String action, String detail) {
        super(msg);
        this.action = action;
        this.detail = detail;
    }

    public TpmsException(String msg, String action, Exception e) {
        super(msg);
        this.action = action;
        this.detail = (e.getMessage() != null ? e.getMessage() : "");
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setDetail(String s) {
        this.detail = s;
    }

    public String getDetail() {
        return detail;
    }

    public void setSysDetail(String s) {
        this.sysDetail = s;
    }

    public String getSysDetail() {
        return sysDetail;
    }

    public Exception getException() {
        return e;
    }

    public String getFwdPage() {
        return null;
    }

    public void printStackTrace(){
        if (e != null) {
            e.printStackTrace();

        }
    }

    public void printStackTrace(PrintStream s){
        if (e != null) {
            if (s != null) {
                e.printStackTrace(s);
            } else {
                e.printStackTrace();
            }
        }
    }

    public void printStackTrace(PrintWriter s){
        if (e != null) {
            if (s != null) {
                e.printStackTrace(s);
            } else {
                e.printStackTrace();
            }
        }
    }
}
