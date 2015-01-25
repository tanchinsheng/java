package tol;

import java.util.Vector;
import java.sql.SQLException;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import org.w3c.dom.*;

//THIS CLASS DIFFERS FROM rngTxtLst ONLY FOR WHAT CONCERNS THE getWhrCond METHOD.
//SINCE IN THIS CLASS I'VE MODIFIED THE CONSTRUCTOR NAMES, THE UPDATE OF rngTxtLst WILL CONSIST IN
//COPYING THE getWhrCond METHOD in the rngTxtLst CLASS CODE.

public class rngTxtLst extends slctTxtLst
{
 String parName2;
 Vector v2;             //upper bound values vector
 String blankField2;    //upper bound blank field
 protected String initVal2=null;
                        //initial upper bound value (when <> from blankField2)


 public rngTxtLst(String name, String label, char type, String p, String p2, dbRdr d, String t, String f,
                  String blnk, String blnk2, String bt, int rowsN, int s)
 {
  super(name, label, type, p,d,t,f, blnk, bt, rowsN, true, s);
  parName2 = p2;
  blankField2 = blnk2;
  v2=new Vector(); v2.addElement(blankField2);
 }

 /**
 * Defines the L.O.V. Upper Bound initial value.
 * It is intended to provide an initial value different from
 * the blank field2
 */
 public void setInitVal2(String val2)
 {
   this.initVal2=val2;
 }

 public String getInitVal2() {return initVal2;}

 /**
  * Sets the current L.O.V. value to the initial value (initVal)
  */
 public void initVal()
 {
   debug("INITVAL-RNGTXT");
   if (this.getInitVal()!=null)
   {
     v.clear();
     debug("ADD1");
     v.addElement(getInitVal());
   }
   if (getInitVal2()!=null)
   {
     v2.clear();
     debug("ADD1");
     v2.addElement(getInitVal2());
   }
 }

 public String getVal2() { return (String)v2.elementAt(0); }

 public boolean isBlank()
 {
  return ((isBlank1())&&(isBlank2()));
 }


 public boolean isBlank1()
 {
   return ((v.size()==0)||(getVal().equals(blankField)));
 }

 public boolean isBlank2()
 {
   return ((v2.size()==0)||(getVal2().equals(blankField2)));
 }

 public String getSqlVal2()
 //thiis function is the same as getSqlVal, with the only difference
 //that instead of the v vector, the v2 is printed out (the 1st element only)..         
 {
  String s;  
  if (type==_CHAR)
     {s = new String("'"); s = s.concat(getVal2()); s = s.concat("'");}  
  else
   if (type==_DATE)
      {s = dbr.applyTO_DATE(getVal2(),false);}
    else
       {s = getVal2();}
  return s;
 }

/**
 * Returns a string in the form label=val.
 * attrName is not considered.
 */
 public String getBindString(String attrName, String label, boolean labelBool)
 {
   if ((this.isBlank())&&(!this.isAlwaysActive()))
   {
     return new String("");
   }
   else
   {
     String labelStr=(label.equals("") ? "" : label.concat("="));
     String val=this.getVal().concat("-"+this.getVal2());
     return new String(labelBool ? labelStr+val : val);
   }
 }

 public String getWhrCond(String slaveTableName)
 {
  String tempFieldName = (!isExpr ? new String(tableName+"."+fieldName) : new String(fieldName));
  if (this.hasAlternativeWhrCond(slaveTableName))
  {
    String altFieldName=this.getAlternativeWhrCondField(slaveTableName);
    tempFieldName=(altFieldName.indexOf('.')>=0 ? "" : slaveTableName+".")+altFieldName;
  }
  String str = "";
  str = str.concat(tempFieldName);
  if (((!isBlank1())&&(!isBlank2()))||(alwaysActive))
  {
     str = str.concat(" BETWEEN ");
     str = str.concat(getSqlVal());
     str = str.concat(" AND ");
     str = str.concat(getSqlVal2());
     return str;
  }
  if ((!isBlank1())&&(isBlank2()))
  {
     str = str.concat(" >= ");
     str = str.concat(getSqlVal());
     return str;
  }
  if ((isBlank1())&&(!isBlank2()))
  {
     str = str.concat(" <= ");
     str = str.concat(getSqlVal2());
     return str;
  }
  //both bound fields are blank and alwaysActive is false
  return null;
 }



 public void fetch(HttpServletRequest req) throws Exception
 {
  v = new Vector(); v2 = new Vector(); //il vettore si resetta ad ogni fetch
  String[] val; String[] val2;
  if (req.getParameterValues(selButName)!=null)
   {
    fetchDependent(req);
    v2.addElement(blankField2);
    for (int i=1; i<v.size(); i++) {v2.addElement(v.elementAt(i));}
    LstLayoutBool=true;
    return;
   }
  else LstLayoutBool=false;
  val=req.getParameterValues(parName);
  val2=req.getParameterValues(parName2);
  if (((val!=null)||(val2!=null))&&!(masterFetched(req)))
        {
         if (val[0].equals(blankField)) v.addElement(blankField);
         else v.addElement(check(val[0]));
         if (val2[0].equals(blankField2)) v2.addElement(blankField2);
         else v2.addElement(check2(val2[0]));
        }
  else
        {v.addElement(blankField); v2.addElement(blankField2);}
 }


 public String check2(String val) throws NumberFormatException
 {
  return check(val);
 }

 public void toHtmlString(JspWriter out) throws Exception
 //DEPRECATED
 {
  throw new Exception("toHtmlString METHOD NOT SUPPORTED BY CLASS rngDateTxtLst.");
 }

 protected void toHtmlString(Vector v, JspWriter out) throws Exception
 //IT'S THE SAME AS THE OVERRIDDEN slctLsT CLASS's METHOD, BUT IT
 //ACCEPTS THE VECTOR v AS PARAMETER.
 {
   if (v.size()>0)
        {
         String optionTag;
         for (int i = 0; i < v.size(); i++)
                {
                 optionTag = new String(" <option>");
                 if (rowsN > 0)
                        {//gestione selected option
                         if (i==0) optionTag  = new String(" <option selected>");
                        }
                 out.println(optionTag + v.elementAt(i));
                }
        }
 }

 
 public void toHtml_defString(JspWriter out) throws Exception
 { if (!LstLayoutBool)
   {
    out.println(" <INPUT TYPE=\"TEXT\" NAME=\"" + parName +  "\" SIZE=\"" + size + "\" VALUE=\"" + getVal()  +"\" >");           
    out.println("to");
    out.println(" <INPUT TYPE=\"TEXT\" NAME=\"" + parName2 + "\" SIZE=\"" + size + "\" VALUE=\"" + getVal2() +"\" >");           
   }
   else
   {
        String multStr = "";
        if (rowsN > 0) 
        {
          multStr = new String(" multiple size = \"" + rowsN + "\" ");
          out.println("<br>");
        }
        out.println(" <SELECT NAME=\"" + parName + "\" " + multStr + ">");
        toHtmlString(v,out);
        out.println(" </SELECT>");
        if (rowsN <= 0) out.println("to");
        out.println(" <SELECT NAME=\"" + parName2 + "\" " + multStr + ">");
        toHtmlString(v2,out);
        out.println(" </SELECT>");
   }
 }

 public String getOperator()
 {
   if (v.size()==0) return null;
   //--- IF THIS LOV IS MANDATORY A BETWEEN OPERATOR MUST BE ALWAYS RETURNED ---//
   if (this.alwaysActive) return new String("BETWEEN");
   else
   {
     if ((!(getVal().equals(blankField))&&!(getVal2().equals(blankField2)))||(alwaysActive))
        return new String("BETWEEN");
     else if (!(getVal().equals(blankField))&&(getVal2().equals(blankField2)))
             return new String(">=");
          else if ((getVal().equals(blankField))&&!(getVal2().equals(blankField2)))
                   return new String("<=");
               else return null;
   }
 }

 /**
  * Sets the rows from a persistent selcrit in XML form.
  */
 public void setRows(Element fldEl) throws Exception
 {
   boolean mode=false;
   resetVector();
   Element condEl=(Element)fldEl.getParentNode();
   NodeList nl=condEl.getElementsByTagName("VAL");
   String operStr=condEl.getAttribute("operator");
   if ((operStr.equals("BETWEEN"))||(operStr.equals(">=")))
   {
     debug(">="+xmlRdr.getVal(nl.item(0))); //debug
     v.addElement(xmlRdr.getVal(nl.item(0)));
   }
   if ((operStr.equals("BETWEEN"))||(operStr.equals("<=")))
   {
     int k=(operStr.equals("<=") ? 0 : 1);
     debug("<="+xmlRdr.getVal(nl.item(k))); //debug
     v2.addElement(xmlRdr.getVal(nl.item(k)));
   }
   if (operStr.equals("<=")) v.addElement(this.blankField);
   if (operStr.equals(">=")) v2.addElement(this.blankField2);
 }

 public void setBlank()
 {
   v.clear();
   v2.clear();
   v.addElement(this.blankField);
   v2.addElement(this.blankField2);
 }

 public void setBlank(int i)
 {
   if (i==1) setBlank1();
   else if (i==2) setBlank2();
 }

 public void setBlank1()
 {v.clear(); v.addElement(this.blankField);}

 public void setBlank2()
 {v2.clear();v2.addElement(this.blankField2);}

/**
 * Sets the current L.O.V. value
 */
 public void setVal1(String val)
 {
   v.clear();
   v.addElement(val);
 }

/**
 * Sets the current L.O.V. value
 */
 public void setVal2(String val)
 {
   v2.clear();
   v2.addElement(val);
 }

 public void resetVector()
 {v.clear();v2.clear();}

/**
 * Assigns to this L.O.V. (provided its <i>selBut</i> HTML parameter
 * results being set into the given HTTP request) the list of values
 * selected by the user.
 */
  public void fetchGUI(HttpServletRequest req) throws Exception
  {
   if (req.getParameterValues("index")==null) return;
   //means we are at the beginning of data selection

   String  indexStr = (req.getParameterValues("index"))[0];
   Integer indexPar = Integer.valueOf(indexStr);

   if (indexPar.intValue()!=index)
   {
     //master fetching
     if (masterFetched(req))
     {
       this.setBlank();
     }
     return;
   }

   if (req.getParameterValues("nVals")==null) {this.setBlank(); return;}
   //means I've asked for this field fetch

   String  nStr     = (req.getParameterValues("nVals"))[0];
   Integer nVals    = Integer.valueOf(nStr);

   this.resetVector();
   int boundIndex=1;
   //checkboxes management
   for (int i=0; i < (nVals.intValue()>2 ? nVals.intValue() : 2); i++)
   {
       String iStr  = (new Integer(i)).toString();
       String check =  new String("chk"+iStr);
       String val   =  new String("val"+iStr);
       String value = "";
       Vector valuesVect=(boundIndex==1 ? v : v2);

       boolean checkBool = (req.getParameterValues(check)!=null);
       if (checkBool)
       {
         value = ((req.getParameterValues(val))[0]).trim();
         if (!value.equals(""))
         {
           value=(boundIndex==1 ? check(value) : check2(value));
           valuesVect.addElement(value);
         }
       }
       if (checkBool) boundIndex = 2;
   }
   if (v.size()==0) setBlank1();
   if (v2.size()==0) setBlank2();
 }

/**
 * Returns the left panel HTML representation of this L.O.V.
 */
 public void toHtmlGUI(HttpServletRequest req, JspWriter out) throws Exception
 {
   if (req.getParameterValues("index")!=null)
   {
     Integer indexPar = Integer.valueOf((req.getParameterValues("index"))[0]);
     if ((indexPar.intValue()==index)&&(req.getParameterValues("nVals")==null)) return;
     //means I've asked for this field fetch
   }

   if(!isBlank())
   {
     out.print("(");
     out.print(getVal());
     out.print(", ");
     out.print(getVal2());
     out.print(")");
     out.println("<BR>");
   }
 }

}//class
