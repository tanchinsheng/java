package tol;

import java.io.*;
import java.util.*;

/**
 * Manages serial access to files
 *
 * @author Daniele Colecchia
 */
public class flRdr {
	public BufferedReader br;
	String lastLine=null;
	LogWriter log=null;
 
	public void debug(Object msg) {if (log!=null) log.p(msg);} public void debug(String msg) {if (log!=null) log.p(msg);}

	public void setLogWriter(LogWriter log) {this.log=log;}

	public flRdr() {
		try {
			File f = new File("slctlst.txt");
			br= new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		}
		catch (IOException e) {
			debug("constructor>"+e);
		}
	}

	
/**
 * Initializes the file reader given a file name
 *
 * @param s the name of the file to be read
 */
	public flRdr(String s) throws IOException {
        File f = new File(s);
        br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
	}


/**
 * Adds to vector v all of the file content - more precisely for each
 * file line adds the corresponding string to the vector. <!-- -->
 * Note: it discard blank lines
 *
 * @param v is the vector to be updated
 */
 	public boolean getRows(Vector v) throws IOException {
 		String s;
 		while(true) {
 			if ((s=br.readLine())!=null) {
 				if (s.trim()!="") v.addElement(s);
 			}
 			else break;
 			}
 		return true;
 	}

 	
/**
 * Adds to vector v all of the file content - more precisely for each
 * file line adds the corresponding string to the vector.
 * Note:
 * it doesn't discard any lines (even if they are blank). In my opinion
 * it's a great thing
 *
 * @param v is the vector to be updated
 */
 	public void getAllRows(Vector v) throws IOException {
 		String s;
 		
 		while(true) {
 			if ((s=br.readLine())!=null) {	
 				v.addElement(s);
 			}
 			else break;
        }
 	}

 	
/**
 * Gets a single file row
 */
 	public String getAnyRow() throws IOException {
 		return br.readLine();
 	}

 	
/**
 * Returns the number of blocks starting within the <i>startStr</i> delimiter
 * and the <i>stopStr</i> delimiter.
 */
 	public int getNofBolcks(String startStr, String stopStr) throws Exception {
 		int i;
 		
 		for (i=0; this.getRows(null, false, startStr, stopStr, null, null); i++);
 			return i;
 	}


/**
 * Returns the number of blocks starting with the <i>startStr</i> string.
 */
 	public int getNofBolcks(String startStr) throws Exception {
 		int i;
 		for (i=0; this.getRows(null, false, startStr, null, null, null); i++);
 		return i;
 	}

/**
 * Fills the vector v with the N-th page delimited by the
 * <i>startStr</i>, the start delimiter, and
 * <i>stopStr</i>, the stop delimiter.
 * The <i>vL</i> vector stores the strings to be replaced by that
 * in the <i>vR</i> vector.
 * Returns false if the document block returned is the last one
 * Remark: first page number is 0
 */
    public boolean getRows(Vector v, int pageN, String startStr, String stopStr, Vector vL, Vector vR) throws IOException {
    	this.lastLine=null;
    	for (int i=0; i<pageN; i++) 
           this.getRows(v, false, startStr, stopStr, vL, vR);
    	this.getRows(v, true, startStr, stopStr, vL, vR);
    	return this.getRows(v, false, startStr, null, vL, vR);
    }


/**
 * Fills the vector v with the N-th page delimited by the
 * <i>startStr</i>, the start delimiter, and
 * <i>stopStr</i>, the stop delimiter.
 * Any occurence of the <i>sL</i> string is replaced by <i>sR</i>.
 * Returns false if the document block returned is the last one
 * Remark: first page number is 0
 */
    public boolean getRows(Vector v, int pageN, String startStr, String stopStr, String sL, String sR) throws IOException {
    	Vector vL=new Vector(); vL.addElement(sL);
    	Vector vR=new Vector(); vR.addElement(sR);
    	return this.getRows(v, pageN, startStr, stopStr, vL, vR);
    } 


/**
 * Extracts the page delimited by the
 * <i>startStr</i>, the start delimiter, and 
 * <i>stopStr</i>, the stop delimiter - if writeBool is set up
 * the extracted page is stored into the v vector.
 * The <i>vL</i> vector stores the strings to be replaced by that
 * in the <i>vR</i> vector. 
 * Remark: There are no constraints for what concerns pages
 * spanning across file lines. 
 */
    boolean getRows(Vector v, boolean writeBool, String startStr, String stopStr, Vector vL, Vector vR) throws IOException {
    	String s=null;
    	boolean startedBool=false;
    	if (startStr!=null) {
    		while (!startedBool) {
    			s=(this.lastLine==null ? br.readLine() : this.lastLine);
    			if (s!=null) {
    				int startIndx=-1;
    				if ((startIndx=s.indexOf(startStr))>-1) {
    					s=s.substring(startIndx+startStr.length());
    					startedBool=true;
    				}
    				this.lastLine=null;
    			}
    			else break;
    		}//while
    	}//if
    	if (!startedBool) return false;
    	boolean stoppedBool=false;
    	while(!stoppedBool) {
    		s=(startedBool ? s : br.readLine());
    		if (s!=null) {
    			if (stopStr!=null) {
    				int startIndx=-1;
    				if ((startIndx=s.indexOf(stopStr))>-1) {
    					this.lastLine=s.substring(startIndx+stopStr.length());
    					s=s.substring(0,startIndx+stopStr.length());
    					stoppedBool=true;
    				}
    			} else if (startStr!=null) {
    				int startIndx=-1;
    				if ((startIndx=s.indexOf(startStr))>-1) {
    					this.lastLine=s;
    					s=s.substring(0,startIndx);
    					stoppedBool=true;              
    				}            
    			}  
    			if (writeBool) {
    				v.addElement(this.replace((startedBool ? startStr+s : s), vL, vR));
    			} 
    			startedBool=false;
    		}
    		else {
    			this.lastLine=null; break;
    		}
    	}//while
    	return true;        
    }


    String replace(String s, Vector vL, Vector vR) {
    	for (int i=0; i<vL.size(); i++) {
    		s=this.replace(s, (String)vL.elementAt(i), (String)vR.elementAt(i));
    	}
    	return s;
    }


    String replace(String s, String sL, String sR) {
    	String retStr="";
    	if ((sL==null)||(sL.equals(""))) return s;
    	int startIndx;
    	do {
    		if ((startIndx=s.indexOf(sL))>-1) {
    			String prefixStr=s.substring(0,startIndx);
    			retStr=retStr.concat(prefixStr).concat(sR);
    			s=s.substring(startIndx+sL.length());
    		}
    		else retStr=retStr.concat(s);
    	}
    	while(startIndx>-1);
    	return retStr;
    }

    public static void main(String[] args) throws Exception {
    	flRdr flrdr=new flRdr("/tomcat/test.html");
    	Vector v=new Vector();
    	Vector vL=new Vector();
    	vL.addElement("DANIELE");
    	vL.addElement("JOHN");
    	Vector vR=new Vector();
    	vR.addElement("<FONT>DANIELE</FONT>");
    	vR.addElement("<FONT SIZE=\"2\">JOHN</FONT>");
    	//debug(flrdr.getRows(v, 1, "<STRONG>","</STRONG>",vL,vR));
    	//debug(v);
    	}
	}
