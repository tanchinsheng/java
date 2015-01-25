package tpms;

public class NoVobSelectedException extends TpmsException {

    public NoVobSelectedException(String action) {
        super("NO VOB SELECTED", action);
    }

    public String getFwdPage() {
        return "selectVobServlet";
    }

}