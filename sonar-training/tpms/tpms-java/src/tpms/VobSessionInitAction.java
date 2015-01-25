package tpms;

import tol.LogWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

class VobSessionInitAction extends TpAction {
    public VobSessionInitAction(String action, LogWriter log) {
        super(action, log);
    }

    public void writeBackEndInFile(String SID, String fName, String repFileName) throws IOException, Exception {
        PrintWriter out = new PrintWriter(new FileWriter(fName));
        out.println("rectype=" + "system");
        out.println("action=" + this.getCCAction());
        out.println("outfile=" + repFileName);
        out.println("session_id=" + SID);
        out.println("debug=" + TpActionServlet._cc_debug);
        out.flush();
        out.close();
        debug("CC IN FILE CREATED>" + fName);
    }

}