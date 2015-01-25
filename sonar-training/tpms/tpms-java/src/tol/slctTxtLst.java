package tol;

import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

public class slctTxtLst extends slctLst
{
 //Vector v;
 //String parName;
 //dbRdr dbr;
 //String fieldName;
 //String tableName;
 //String blankField; 
 //String selButName;   
 //Vector singleDep = new Vector();
 //Vector rangeDep  = new Vector();
 public int size; //per il layout del form html
 boolean LstLayoutBool=false;

 public boolean isList() { return LstLayoutBool; }

 public slctTxtLst(String name, String label, char type, String p, dbRdr d, String t, String f, String blnk, String bt, int rowsN, boolean isMulti, int s)
 {
  super(name, label, type, p,d,t,f,blnk, bt, rowsN, isMulti);
  size=s;
 }

 public void fetch(HttpServletRequest req) throws Exception
 {
  boolean firstCond; //useless 
  v = new Vector(); //il vettore si resetta ad ogni fetch
  String[] val;
  if (req.getParameterValues(selButName)!=null) 
         { fetchDependent(req); LstLayoutBool=true; return; }
  else LstLayoutBool=false;
  val=req.getParameterValues(parName);
  if ((val!=null)&&!(val[0].equals(blankField))&&!(masterFetched(req)))
          if (!isMulti)
        {
          v.addElement(check(val[0]));
        }
        else
        {
          v = cmptParToVect(val);
        }
  else
        {v.addElement(blankField);}
 }


 public void toHtml_defString(JspWriter out) throws Exception
 { if (!LstLayoutBool)
                {
                 out.println(" <INPUT TYPE=\"TEXT\" NAME=\"" + parName + "\" SIZE=\"" + size + "\" VALUE=\"" + getVal() +"\" >");
                }
   else
                {
                 super.toHtml_defString(out);                    
                }
 }
 
 
}//class
