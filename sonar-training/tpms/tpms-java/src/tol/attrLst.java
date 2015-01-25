package tol;

import java.io.*;
import java.util.*;
import java.sql.*;
import java.text.*;
import javax.servlet.jsp.JspWriter;
import javax.servlet.http.HttpServletRequest;
import org.w3c.dom.*;

public class attrLst
{
  Vector names;
  Vector labels;
  Vector types;
  Vector fldNames;
  Vector tblNames;
  Vector sqlexprs;   //contains expr with would be put into the sql stmt
  Vector formats;    //defines how field values must be formatted
  Vector funzs;      //contains funz elements
  Vector detailFlags;
  Vector vals;       //contains values for this field attributes
                     //it's a vector of vectors
  Vector detailVals; //contains values for all of the attributes fields
                     //it is that filled out by the detailed report
  Vector hideFlags;  //flag=tre means that the column must be hidden
  int row=0;         //points to the row which must be printed
  int nOfAttrs=0;
  int nOfDetailed=0;
  int nOfNotDetailed=0;
  boolean printKeyAttrInDetRep_bool=true;
  LogWriter log=null;

  public void setLogWriter(LogWriter log) {this.log=log;}
  public void debug(Object msg) {if (log!=null) log.p(msg);} public void debug(String msg) {if (log!=null) log.p(msg);}

 public attrLst()
 {
  names       = new Vector();
  labels      = new Vector();
  types       = new Vector();
  fldNames    = new Vector();
  tblNames    = new Vector();
  sqlexprs    = new Vector();
  formats     = new Vector();
  funzs       = new Vector();
  detailFlags = new Vector();
  hideFlags   = new Vector();
  vals        = new Vector();
  detailVals  = new Vector();
 }

 public String getLabel(int i)
 {
   return (String)this.labels.elementAt(i);
 }

 public String getName(int index)
 {
   if (index>=0) return (String)names.elementAt(index);
   else return null;
 }

 public String getLabel(String attrName)
 {
   if (getAttr(attrName)>=0) return getLabel(getAttr(attrName));
   else return null;
 }

 public String getSqlExpr(int i)
 {
   return (String)sqlexprs.elementAt(i);
 }

 public boolean isComputed(int i)
 {
   return (funzs.elementAt(i)!=null);
 }

 public String getFldName(int i)
 {
  return (String)fldNames.elementAt(i);
 }

 public String getTableName(int i)
 {
  return (String)tblNames.elementAt(i);
 }

 public boolean isDetailed(int i)
 {
   return ((Boolean)detailFlags.elementAt(i)).booleanValue();
 }

 public char getType(int i)
 {
  return ((Character)types.elementAt(i)).charValue();
 }

 public Vector getVals(int i)
 {
  return (Vector)vals.elementAt(i);
 }

 public Vector getVals(String attrName)
 {
  return (Vector)vals.elementAt(getAttr(attrName));
 }

/**
 * Gets the value of the atribute named as <i>attrName</i> within
 * the row whose key attribute's value is <i>keyVal</i>.
 *
 * @return null - if the referenced attribute doesn't exist or
 *   no matching row is found
 */
 public String getVal(String attrName, String keyVal)
 {
   return this.getVal(getAttr(attrName), keyVal);
 }

 public String getVal(int index, String keyVal)
 {
   if (index==-1) return null;
   else
   {
     Vector keyVals=(Vector)vals.elementAt(0);
     Vector attrVals=(Vector)vals.elementAt(index);
     boolean found=false;
     int j;
     for (j=0; j<keyVals.size(); j++)
     {
       if (((String)keyVals.elementAt(j)).equals(keyVal)) {found=true; break;}
     }
     if (!found) return null;
     else
     {
       //catch the eventual exception raised in case attributes columns
       //are not defined
       try {return (String)attrVals.elementAt(j);}
       catch(Exception e) {return null;}
     }
   }
 }

/**
 * Gets the value of the atribute named as <i>attrName</i> within
 * the row whose position is <i>rowN</i> (from 0 to N-1).
 *
 * @return null - if the referenced attribute doesn't exist or
 *   no matching row is found
 */
 public String getVal(String attrName, int rowN)
 {
   return this.getVal(getAttr(attrName), rowN);
 }

 public String getVal(int index, int rowN)
 {
   if (index==-1) return null;
   else
   {
     Vector attrVals=(Vector)vals.elementAt(index);
     try {return (String)attrVals.elementAt(rowN);}
     catch(Exception e) {return null;}
   }
 }

/**
 * return the position of the attribute named as <i>name</i>.
 *
 * @return -1 if no matching attribute exists
 */
 public int getAttr(String name)
 {
   for (int i=0; i<nOfAttrs; i++)
   {
     if (((String)names.elementAt(i)).equals(name)) return i;
   }
   return -1;
 }

 public int size()
 //return the number of attributes
 {
  return nOfAttrs;
 }
 public boolean isEmpty() {return (names.size()==0);}

 public void add(String name, String label, char type, String fieldName, String tableName, String sqlexpr, String format,  Element funzEl)
 {
  names.addElement(name);
  labels.addElement((label==null) ? name : label);
  types.addElement(new Character(type));
  fldNames.addElement(fieldName);
  tblNames.addElement(tableName);
  sqlexprs.addElement(sqlexpr);
  formats.addElement(format);
  funzs.addElement(funzEl);
  vals.addElement(new Vector());
  detailVals.addElement(new Vector());
  detailFlags.addElement(new Boolean(false));
  hideFlags.addElement(new Boolean(false));
  nOfAttrs++;
  nOfNotDetailed++;
 }

 public void addDetail(String name, String label, char type, String fieldName, String tableName, String sqlexpr, String format, Element funzEl)
 {
  names.addElement(name);
  labels.addElement((label==null) ? name : label);
  types.addElement(new Character(type));
  fldNames.addElement(fieldName);
  tblNames.addElement(tableName);
  sqlexprs.addElement(sqlexpr);
  formats.addElement(format);
  funzs.addElement(funzEl);
  vals.addElement(new Vector());
  detailVals.addElement(new Vector());
  detailFlags.addElement(new Boolean(true));
  hideFlags.addElement(new Boolean(false));
  nOfAttrs++;
  nOfDetailed++;
  if (name.equals((String)names.elementAt(0))) printKeyAttrInDetRep_bool=false;
 }

/**
 * Prints an HTML form check boxes raw intended for setting the hide flags.
 */
 public void printHideFlags(JspWriter out) throws Exception
 {
   String prefix="_show_";
   for (int i=0; i<this.nOfAttrs; i++)
   {
     if ((i==0)&&(!printKeyAttrInDetRep_bool)) continue;
     out.println("<TR>");
     out.println("<TD>");
     out.println(this.getLabel(i));
     out.println("</TD>");
     out.println("<TH>");
     boolean hideBool=(((Boolean)this.hideFlags.elementAt(i)).booleanValue());
     out.print("<INPUT TYPE=\"CHECKBOX\" NAME=\""+prefix+this.getName(i)+"\" ");
     if (!hideBool)
     {
       out.print("CHECKED");
     }
     out.println(">");
     out.println("</TH>");
     out.println("</TR>");
   }
 }

/**
 * Returns true if at least one fattribute is currently hidden
 */
 public Properties getHideProps()
 {
   Properties props=new Properties();
   for (int i=0; i<this.nOfAttrs; i++)
   {
     if ((i==0)&&(!printKeyAttrInDetRep_bool)) {continue;}
     boolean hideBool=(((Boolean)this.hideFlags.elementAt(i)).booleanValue());
     if (!hideBool)
     {
       int ind=i+1;
       props.setProperty(new String("fld"+(ind<10 ? "0" : "")+new Integer(ind).toString()), getName(i));
     }
   }
   return props;
 }

/**
 * Returns true if at least one fattribute is currently hidden
 */
 public boolean hasHideFlags()
 {
   boolean hideBool=false;
   for (int i=0; i<this.nOfAttrs; i++)
   {
     hideBool=(((Boolean)this.hideFlags.elementAt(i)).booleanValue());
   }
   return hideBool;
 }


/**
 * Sets the hide Flags from the HTTP request.
 * This must provide one HTTP parameter form each report
 * column to be included within the report. The HTTP parameter name must be the same as the column,
 * with this prefix: '_show_'. This makes it easy to set the hide flags by means of checkBoxes
 * within an HTML form.
 */
 public void setHideFlagsFromRequest(HttpServletRequest request)
 {
   String prefix="_show_";
   for (int i=0; i<this.nOfAttrs; i++)
   {
     if ((i==0)&&(!printKeyAttrInDetRep_bool)) {continue;}
     String httpShow[]=request.getParameterValues(prefix.concat(this.getName(i)));
     if (httpShow==null)
     {
       this.hideFlags.setElementAt(new Boolean(true), i);
     }
     else
     {
       this.hideFlags.setElementAt(new Boolean(false), i);
     }
   }
 }

 public void clear(boolean mode)
 //clears the values vector
 //if mode=true only the detailed report values (detailVals) are cleared;
 //otherwise the vals vector is cleared too
 {
   for (int i=0;i<detailVals.size();i++) ((Vector)detailVals.elementAt(i)).clear();
   if (!mode)
   {
     for (int i=0;i<vals.size();i++) ((Vector)vals.elementAt(i)).clear();
   }
   row=0;
 }

 public void getRow(boolean mode, ResultSet rset) throws SQLException, IOException
 //given a recordset, this methods adds to the 'attrLst' object one row
 //of values;
 //theese values are fetched from the current row of the recordset
 {
   Vector vals=(mode ? this.detailVals : this.vals);
   int j=1; //recordset column index starts from 1 instead of 0
   for (int i=0; i<nOfAttrs; i++)
   {
     if ((!mode)&&(isDetailed(i))) continue;
     if (!isComputed(i))
     {
       ((Vector)vals.elementAt(i)).addElement(rset.getString(j)==null ? "null" : formatVal(i,rset.getString(j)));
     }
     else
     {
       Element funzEl=(Element)funzs.elementAt(i);
       Object compVal=evalFun(funzEl, vals, ((Vector)vals.elementAt(i)).size());
       ((Vector)vals.elementAt(i)).addElement(compVal==null ? "null" : formatVal(i,compVal.toString()));
     }
     j++;
   }
 }

/**
 * It Doesn't fill the primary or secondary attributes vectors, as it is intended
 * to write the recordset as an XML doc onto the <i>out</i> stream.
 */
 public void getRow(boolean mode, ResultSet rset, Writer out) throws SQLException, IOException
 {
   Vector vals=(mode ? this.detailVals : this.vals);
   this.clear(mode);  //the detailed vector is cleared every time
   int j=1; //recordset column index starts from 1 instead of 0
   out.write("<STDATA_RECORD>");
   for (int i=0; i<nOfAttrs; i++)
   {
     if ((mode)&&(i==0)&&(!printKeyAttrInDetRep_bool)) {j++; continue;}
     if ((!mode)&&(isDetailed(i))) continue;
     out.write("<"+names.elementAt(i)+">");
     String value="";
     if (!isComputed(i))
     {
       value=(rset.getString(j)==null ? "null" : formatVal(i,rset.getString(j)));
       out.write(xmlRdr.format(value));
     }
     else
     {
       Element funzEl=(Element)funzs.elementAt(i);
       Object compVal=evalFun(funzEl, vals, 0);
       value=(compVal==null ? "null" : formatVal(i,compVal.toString()));
       out.write(xmlRdr.format(value));
     }
     ((Vector)vals.elementAt(i)).addElement(value);
     out.write("</"+names.elementAt(i)+">");
     j++;
   }
   out.write("</STDATA_RECORD>");
 }

 protected String formatVal(int i, String val)
 {
   String format=(String)formats.elementAt(i);
   if ((format!=null)&&(getType(i)==slctLst._NUM))
   {
       DecimalFormat formatter=new DecimalFormat(format, new DecimalFormatSymbols(Locale.UK));
       val=formatter.format(Double.parseDouble(val));
       if (format.endsWith("%")) val=val.replace('%',' ');
   }
   return val.trim();
 }

 protected Object evalFun(Element funzEl, Vector vals, int row)
 {
   String funz=funzEl.getAttribute("name");
   NodeList argLst=funzEl.getChildNodes();
   Element[] args=new Element[3];
   int i=0;
   for (int j=0; j<argLst.getLength(); j++)
   {
     Node arg=argLst.item(j);
     if (arg instanceof Element)
     {
       args[i]=(Element)arg;
       i++;
     }
   }
   if ((funz.equals("div"))||(funz.equals("/")))
   {
     return computeDiv(args[0], args[1], vals, row);
   }
   if ((funz.equals("mult"))||(funz.equals("*")))
   {
     return computeMult(args[0], args[1], vals, row);
   }
   else return null;
 }

 protected Object eval(Element arg, Vector vals, int row) throws Exception
 {
  if (arg.getNodeName().equals("funz"))
  {
    return evalFun(arg, vals, row);
  }
  else
  {
    String name=arg.getAttribute("name");
    String value=arg.getAttribute("val");
    if (!name.equals(""))
    {
      int ind=this.getAttr(name);
      value=(String)((Vector)vals.elementAt(ind)).elementAt(row);
    }
    Float f=new Float(value);
    return f;
  }
 }

 protected Object computeDiv(Element arg1, Element arg2, Vector vals, int row)
 {
   Float f3;
   try
   {
      Float   f1=(Float)eval(arg1, vals, row);
      Float   f2=(Float)eval(arg2, vals, row);
              f3=(f2.floatValue()>0.0 ? new Float(f1.floatValue()/f2.floatValue()) : null);
   }
   catch(Exception e) {debug(e); f3=null;}
   return f3;
 }

 protected Object computeMult(Element arg1, Element arg2, Vector vals, int row)
 {
   Float f3;
   try
   {
      Float   f1=(Float)eval(arg1, vals, row);
      Float   f2=(Float)eval(arg2, vals, row);
              f3=new Float(f1.floatValue()*f2.floatValue());
   }
   catch(Exception e) {f3=null;}
   return f3;
 }


 public void setRow(Element valEl) throws Exception
 {
   boolean mode=false;
   Vector vals=(mode ? this.detailVals : this.vals);
   NodeList nl=valEl.getElementsByTagName("ATTROW");
   for (int row=0; row<nl.getLength(); row++)
   {
         Element rowEl=(Element)nl.item(row);
         for (int i=0; i<nOfAttrs; i++)
         {
           if ((!mode)&&(isDetailed(i))) continue;
           String attrName=(String)names.elementAt(i);
           String attrVal=xmlRdr.getVal(rowEl, attrName);
           ((Vector)vals.elementAt(i)).addElement(attrVal!=null ? attrVal.trim() : "null");
         }
   }
 }

 public void printWholeReport(JspWriter out) throws Exception
 {
   out.println("<TABLE>");
   this.printReportRows(out);
   out.println("</TABLE>");
 }

 public void printReportRows(JspWriter out) throws Exception
 {
   this.printReportRows(out, null, null, null, null, true, null);
 }

 public void printReportRows(JspWriter out,  String bgCol, String fontFace, String fontSize, String textCol) throws Exception
 {
   this.printReportRows(out, bgCol, fontFace, fontSize, textCol, true, null);
 }

 public void printReportRows(JspWriter out,  String bgCol, String fontFace, String fontSize, String textCol, boolean showAllBool, Vector keyVals) throws Exception
 {
   printReportRows(null, out, bgCol, fontFace, fontSize, textCol, showAllBool, keyVals);
 }



 public void printReportRows(String firstCol, JspWriter out,  String bgCol, String fontFace, String fontSize, String textCol, boolean showAllBool, Vector keyVals) throws Exception
 //prints the detailed report (ie: in a report form all of the rows of the
 //'attrLst' object).
 //the following are HTML parameters:
 //  firstCol is the HTML definition of the first column for each row
 //  bgCol     = cells background color (can be null)
 //  fontFace  = font type (ie: courier, times, etc.)
 //  fontSize  = font's size
 //  textCol   = text color (can be null)
 {
   String bgcolStr   =(bgCol==null    ? "" : " BGCOLOR=\""+bgCol+"\"");
   String textcolStr =(textCol==null  ? "" : " COLOR=\""+textCol+"\"");
   String fontfaceStr=(fontFace==null ? "" : " FACE=\""+fontFace+"\"");
   String fontsizeStr=(fontSize==null ? "" : " SIZE=\""+fontSize+"\"");
   Vector vals=this.detailVals;
   for (int row=0; row<((Vector)vals.elementAt(0)).size(); row++)
   {
      if (detRepRowMustBePrinted(row, keyVals, showAllBool))
      {
        out.println("<TR>");
        if (firstCol!=null)
        {
          out.println(replaceInRow(firstCol, row));
        }
        for (int i=0; i<nOfAttrs; i++)
        {
          if ((i==0)&&(!printKeyAttrInDetRep_bool)) continue;
          if (((Boolean)this.hideFlags.elementAt(i)).booleanValue()) continue; //HIDE FLAGS
          out.println("<TD"+bgcolStr+">");
          out.println("<FONT"+fontfaceStr+fontsizeStr+textcolStr+">");
          out.println((String)((Vector)vals.elementAt(i)).elementAt(row));
          out.println("</FONT>");
          out.println("</TD>");
        }
        out.println("</TR>");
      }
   }
 }

 String replaceInRow(String qryStr, int row)
 {
   String qryStr2="";
   StringTokenizer tok=new StringTokenizer(qryStr, "$&#<>=!^*+-/(),.;' \n\t\r\f\"", true);
   while (tok.hasMoreTokens())
   {
     String s=tok.nextToken();
     if (s.equals("$"))
     {
       String varName=tok.nextToken().toUpperCase();
       if (varName.equals("ROW")) s=new Integer(row).toString();
     }
     qryStr2=qryStr2.concat(s);
   }
   return qryStr2;
 }

 boolean detRepRowMustBePrinted(int row, Vector keyVals, boolean showAllBool)
 {
   if (showAllBool) return true;
   else
   {
     for (int i=0; i<keyVals.size(); i++)
     {
       String rowKeyVal=(String)((Vector)this.detailVals.elementAt(0)).elementAt(row);
       if (rowKeyVal.equals((String)keyVals.elementAt(i))) return true;
     }
     return false;
   }
 }

/**
 * Prints a single row of values.
 * Remark: all of the attributes but the first (coloumns) are printed.
 *         Printinig the first one is up to the invoking procedure!
 * @param mode if false means that detailed attributes shouldn't be printed
 * @param keyVal is the current field value of the
 *           select list which owns the attributes
 * @param freeCls is the number of the coloumns which must be left empty at the
 *           beginning the row which must be printed
 * @newLine tells if the row being printed is a whole new row (it could also
 *           have some cells already printed
 */
 public boolean printRow(boolean mode, JspWriter out, String keyVal, int freeCols, boolean newLine, String bgCol, String fontFace, String fontSize, String textCol) throws Exception
 {
  String bgcolStr   =(bgCol==null    ? "" : " BGCOLOR=\""+bgCol+"\"");
  String textcolStr =(textCol==null  ? "" : " COLOR=\""+textCol+"\"");
  String fontfaceStr=(fontFace==null ? "" : " FACE=\""+fontFace+"\"");
  String fontsizeStr=(fontSize==null ? "" : " SIZE=\""+fontSize+"\"");

  Vector vals=(mode ? this.detailVals : this.vals);

  //if no attribute has been defined which is 'not detailed', an empty report
  //must be print
   if ((!mode)&&(nOfNotDetailed<=1)) return false;

   //The following is a sort of EOF acknowledgement
   if (row>=((Vector)vals.elementAt(0)).size()) return false;

   String curKeyVal=(String)((Vector)vals.elementAt(0)).elementAt(row);
   if (curKeyVal.equals(keyVal))
   {
     if (newLine) out.println("<TR>");

     for (int i=0; i<freeCols; i++)
     {
       out.println("<TD>");
       out.println("</TD>");
     }

     for (int i=1; i<nOfAttrs; i++)
     {
       if ((mode)||(!((Boolean)detailFlags.elementAt(i)).booleanValue()))
       {
         out.println("<TD"+bgcolStr+">");
         out.println("<FONT"+fontfaceStr+fontsizeStr+textcolStr+">");
         out.println((String)((Vector)vals.elementAt(i)).elementAt(row));
         out.println("</FONT>");
         out.println("</TD>");
       }
     }
     if (newLine) out.println("</TR>");
     if (row<((Vector)vals.elementAt(0)).size()) {row++; return true;}
     else return false;
   }
   else return false;
 }

 public void printLabels(JspWriter out) throws Exception
 {
   this.printLabels(true, out, 0, null, null, null, null);
 }

/**
 * Writes the reporth intensive data onto the <i>out</i> stream.
 */
 public void getLabelsAsXml(boolean mode, Writer out) throws IOException
 {
     out.write("<STDATA_COLUMNS>"+"\n");
     for (int i=0; i<labels.size(); i++)
     {
       if ((mode)&&(i==0)&&(!printKeyAttrInDetRep_bool)) continue;
       if ((!mode)&&(((Boolean)detailFlags.elementAt(i)).booleanValue())) continue;
       out.write("<STDATA_COLUMN name=\""+(String)names.elementAt(i)+"\" label=\""+(String)labels.elementAt(i)+"\" lev=\""+(!isDetailed(i) ? "1" : "2")+"\"/>"+"\n");
     }
     out.write("</STDATA_COLUMNS>"+"\n");
 }

/**
 * Returns the report as an XML document (rooted as an XML DataSelection element).
 */
 public Document getReportAsXml(int maxColsN, boolean mode, boolean showAllBool, Vector keyVals) throws Exception
 {
   Document doc=xmlRdr.getNewDoc("STDATA");
   Element dselEl=xmlRdr.addEl(doc.getDocumentElement(), "STDATASELECTION");
   dselEl.setAttribute("DATAID","datasel");
   dselEl.setAttribute("TYPE","ROW");
   //--- TITLE DEFINITION ---//
   String title=new String(((String)labels.elementAt(0)).toUpperCase()+" DETAILED REPORT");
   xmlRdr.setVal(xmlRdr.addEl(dselEl, "TITLE"), title);
   //--- LABELS DEFINITION ---//
   Element labsEl=xmlRdr.addEl(dselEl, "STDATA_COLUMNS");
   int colCnt=0;
   for (int i=0; i<labels.size(); i++)
   {
     if ((mode)&&(i==0)&&(!printKeyAttrInDetRep_bool)) continue;
     if ((!mode)&&(((Boolean)detailFlags.elementAt(i)).booleanValue())) continue;
     if ((mode)&&(((Boolean)this.hideFlags.elementAt(i)).booleanValue())) continue; //HIDE FLAGS
     colCnt++;
     if (colCnt>maxColsN) break;
     Element labEl=xmlRdr.addEl(labsEl, "STDATA_COLUMN");
     labEl.setAttribute("name",  (String)names.elementAt(i));
     labEl.setAttribute("label", (String)labels.elementAt(i));
   }
   //--- ROWS INSERTION ---//
   Vector vals=(mode ? this.detailVals : this.vals);
   for (int row=0; row<((Vector)vals.elementAt(0)).size(); row++)
   {
      if (detRepRowMustBePrinted(row, keyVals, showAllBool))
      {
        Element rowEl=xmlRdr.addEl(dselEl, "STDATA_RECORD");
        colCnt=0;
        for (int i=0; i<nOfAttrs; i++)
        {
          if ((mode)&&(i==0)&&(!printKeyAttrInDetRep_bool)) continue;
          if ((!mode)&&(((Boolean)detailFlags.elementAt(i)).booleanValue())) continue;
          if ((mode)&&(((Boolean)this.hideFlags.elementAt(i)).booleanValue())) continue; //HIDE FLAGS
          colCnt++;
          if (colCnt>maxColsN) break;
          Element colEl=xmlRdr.addEl(rowEl, (String)names.elementAt(i));
          xmlRdr.setVal(colEl, (String)((Vector)vals.elementAt(i)).elementAt(row));
        }
      }
   }
   return doc;
 }

 public void printLabels(boolean mode, JspWriter out, int freeCols, String bgCol, String fontFace, String fontSize, String textCol) throws Exception
 //It prints the attribute list labels
 //keyVal is the current field value of the
 //select list which owns the attributes
 {
     String bgcolStr   =(bgCol==null    ? "" : " BGCOLOR=\""+bgCol+"\"");
     String textcolStr =(textCol==null  ? "" : " COLOR=\""+textCol+"\"");
     String fontfaceStr=(fontFace==null ? "" : " FACE=\""+fontFace+"\"");
     String fontsizeStr=(fontSize==null ? "" : " SIZE=\""+fontSize+"\"");

     if ((!mode)&&(nOfNotDetailed<=1)) return;
     out.println("<TR>");
     for (int i=0; i<freeCols; i++)
     {
       out.println("<TD>");
       out.println("</TD>");
     }

     for (int i=0; i<labels.size(); i++)
     {
       if ((mode)&&(i==0)&&(!printKeyAttrInDetRep_bool)) continue;
       if ((!mode)&&(((Boolean)detailFlags.elementAt(i)).booleanValue())) continue;
       if ((mode)&&(((Boolean)this.hideFlags.elementAt(i)).booleanValue())) continue; //HIDE FLAGS
       out.println("<TH"+bgcolStr+">");
       out.println("<FONT"+fontfaceStr+fontsizeStr+textcolStr+">");
       out.println((String)labels.elementAt(i));
       out.println("</FONT>");
       out.println("</TH>");
     }
     out.println("</TR>");
 }

/**
 * Returns many Elements providing values for all the values selected
 * for the specified L.O.V. and its respective attributes in a recordset
 * form
 */
public void getAttrsAsXMLRecordset(Element targetEl, boolean mode)
{this.getAttrsAsXMLRecordset(targetEl, mode, null);}

public void getAttrsAsXMLRecordset(Element targetEl, boolean mode, Properties fldsLst)
{
     if ((!mode)&&(nOfNotDetailed<=1)) return;
     Vector vals=(mode ? this.detailVals : this.vals);
     for (int row=0; row<((Vector)vals.elementAt(0)).size(); row++)
     {
         Element rowEl=xmlRdr.addEl(targetEl, "VAL");
         for (int i=0; i<nOfAttrs; i++)
         {
           if ((mode)||(!((Boolean)detailFlags.elementAt(i)).booleanValue()))
           {
             String name=(String)names.elementAt(i);
             String val=(String)((Vector)vals.elementAt(i)).elementAt(row);
             if (fldsLst!=null)
             {
               debug(fldsLst);
               if (fldsLst.getProperty(name)!=null) name=fldsLst.getProperty(name);
               else continue;
             }
             xmlRdr.addEl(rowEl, name, val);
           }
         }
     }
}

/**
 * Gets an Element with primary attributes for the given
 * master-field value
 */
 public void getAttrsAsXML(Element targetEl, String value, boolean mode)
 {
     if ((!mode)&&(nOfNotDetailed<=1)) return;
     Vector vals=(mode ? this.detailVals : this.vals);
     for (int row=0; row<((Vector)vals.elementAt(0)).size(); row++)
     {
       if (((String)((Vector)vals.elementAt(0)).elementAt(row)).equals(value))
       {
         Element rowEl=xmlRdr.addEl(targetEl, "ATTROW");
         for (int i=0; i<nOfAttrs; i++)
         {
           if ((mode)||(!((Boolean)detailFlags.elementAt(i)).booleanValue()))
           {
             String val=(String)((Vector)vals.elementAt(i)).elementAt(row);
             xmlRdr.addEl(rowEl, (String)names.elementAt(i), val);
           }
         }
       }
     }
 }

 public static Element orderXmlRecs(Element dataXml, String orderByName, String order, String dataType) throws Exception
 {
   if (!orderByName.equals(""))
   {
     NodeList recs=dataXml.getElementsByTagName("STDATA_RECORD");
     Element tel;
     Element bestEl;
     Float f1, f2, bestF;
     for (int i=0; i<recs.getLength()-1; i++)
     {
       Element e1=(Element)recs.item(i);
       try {f1=new Float(xmlRdr.getVal(e1,orderByName));}
       catch(Exception e) {f1=new Float(0);}
       bestEl=e1; bestF=f1;
       for (int j=i+1; j<recs.getLength(); j++)
       {
         Element e2=(Element)recs.item(j);
         try{f2=new Float(xmlRdr.getVal(e2,orderByName));}
         catch(Exception e) {f2=new Float(0);}
         if (order.equals("ASC") ? (f2.floatValue()<bestF.floatValue()) : (f2.floatValue()>bestF.floatValue()))
         {
           bestEl=e2; bestF=f2;
         }
       }
       if (bestEl!=e1)
       {
         tel=(Element)e1.cloneNode(true);
         e1.getParentNode().replaceChild((Element)bestEl.cloneNode(true), e1);
         bestEl.getParentNode().replaceChild(tel, bestEl);
       }
     }
   }
   return dataXml;
 }

 public static Element dropNullRecs(Element dataXml, String srcCol) throws Exception
 {
   Vector nullRecs=xmlRdr.findEls(dataXml.getElementsByTagName("STDATA_RECORD"), srcCol, "null");
   if (nullRecs!=null)
   {
      for (int i=0; i<nullRecs.size(); i++)
      {
        ((Element)nullRecs.elementAt(i)).getParentNode().removeChild((Element)nullRecs.elementAt(i));
      }
   }
   return dataXml;
 }

 public static Element calcStat(Element dataXml, Element statEl) throws Exception
 {
  String statType=statEl.getAttribute("type");
  if ((statType.equals("COUNT"))||(statType.equals("FREQ")))
  {
    int levels     = (statEl.getAttribute("levels").equals("") ? 0 : Integer.parseInt(statEl.getAttribute("levels")));
    String srcCol  = statEl.getAttribute("src");
    String freqCol = statEl.getAttribute("dest");
    boolean sorted = statEl.getAttribute("sorted").equals("Y");
    return attrLst.calcFreq(statType.equals("FREQ"), dataXml, srcCol, freqCol, levels, !sorted);
  }
  return null;
 }

 public static Element calcFreq(boolean percBool, Element dataXml, String srcCol, String freqCol, int fixNbars, boolean orderBool) throws Exception
 {
   //--- EVENTUAL SORT ---//
   if (orderBool) dataXml=attrLst.orderXmlRecs(dataXml, srcCol, "ASC", "N");
   //-- REMOVE NULL RECS --//
   dataXml=attrLst.dropNullRecs(dataXml, srcCol);
   //--- LEVELS AND DX COMPUTATION ---//
   int nMinBars=8;
   Vector recs=xmlRdr.getVectFromNodeList(dataXml.getElementsByTagName("STDATA_RECORD"));
   int rowsN=recs.size();
   float minVal=Float.parseFloat(xmlRdr.getVal((Element)recs.elementAt(0),srcCol));
   float maxVal=Float.parseFloat(xmlRdr.getVal((Element)recs.elementAt(rowsN-1),srcCol));
   float rangeVal=maxVal-minVal;
   long nLevs=(fixNbars==0 ? (Math.sqrt(rowsN)<nMinBars ? nMinBars : Math.round(Math.sqrt(rowsN))) : fixNbars);
   float dx=rangeVal/nLevs;
   //--- FREQUENCY COMPUTATION LOOP ---//
   Element tickRec=(Element)recs.elementAt(0);
   float tickVal=Float.parseFloat(xmlRdr.getVal(tickRec,srcCol));
   //--- THE FREQ COL IS CREATED IN CASE IT DOESN'T ALREADY EXIST ---//
   if (xmlRdr.getChild(tickRec,freqCol)==null) xmlRdr.addEl(tickRec, freqCol);
   xmlRdr.setVal(tickRec, freqCol, new Integer(1).toString());
   for (int i=1; i<rowsN; i++)
   {
     float val=Float.parseFloat(xmlRdr.getVal((Element)recs.elementAt(i),srcCol));
     if (val<tickVal+dx)
     {
       //--- FREQ INCREMENT ---//
       long freq=Long.parseLong(xmlRdr.getVal((Element)tickRec,freqCol));
       xmlRdr.setVal(tickRec, freqCol, new Long(freq+1).toString());
       //--- REC DELETION MARK ---//
       ((Element)recs.elementAt(i)).getParentNode().removeChild((Element)recs.elementAt(i));
     }
     else
     {
       tickRec=(Element)recs.elementAt(i);
       tickVal=Float.parseFloat(xmlRdr.getVal(tickRec,srcCol));
       if (xmlRdr.getChild(tickRec,freqCol)==null) xmlRdr.addEl(tickRec, freqCol);
       xmlRdr.setVal(tickRec, freqCol, new Integer(1).toString());
     }
   }
   if (percBool)
   {
     NodeList rows=dataXml.getElementsByTagName("STDATA_RECORD");
     for (int i=0; i<rows.getLength(); i++)
     {
        double perc=(Double.parseDouble(xmlRdr.getVal((Element)rows.item(i),freqCol))/rowsN)*100;
        xmlRdr.setVal((Element)rows.item(i), freqCol, new Double(perc).toString());
     }
   }
   return dataXml;
 }

 public void setValsFromReq(String prefix, String val, HttpServletRequest req) throws Exception
 {
   for (int i=0; i<nOfAttrs; i++)
   {
     if (isDetailed(i)) continue;
     String parName = (i==0 ? prefix : prefix+"."+getName(i));
     if (req.getParameterValues(parName)!=null)
     {
       ((Vector)vals.elementAt(i)).addElement(req.getParameterValues(parName)[0]);
     }
     else
     {
       ((Vector)vals.elementAt(i)).addElement("");
     }
   }
 }

 public String toString()
 {
  String s="";
  for (int i=0; i<vals.size(); i++)
  {
   s=s.concat(labels.elementAt(i)+"="+vals.elementAt(i)+"\n");
  }
  return s;
 }

 public static void main(String[] args) throws Exception
 {
   //debug(attrLst.replaceInRow("<a href=\"rep.jsp?n=$ROW\"></a>", 105));
   Element rep=xmlRdr.getRoot("z:/Backups/CCAM-framework/data/report.xml", false);
   //Element stat=xmlRdr.getRoot("z:/Backups/CCAM-framework/data/freqstat.xml", false);
   //rep=attrLst.calcFreq(false, rep, "YIELD", "FREQ", 6, true);
   //rep=attrLst.calcStat(rep, stat);
   //xmlRdr.print(rep.getOwnerDocument(), log.getOut(), false);
   //DecimalFormat formatter=new DecimalFormat("##.#", new DecimalFormatSymbols(Locale.ITALY));
   //String val="60.78";
   //val=formatter.format(Double.parseDouble(val));
   //debug(val+">");
 }
}

