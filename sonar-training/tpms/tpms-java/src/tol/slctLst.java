package tol;

import java.io.*;
import java.util.Vector;
import java.sql.SQLException;
import java.sql.ResultSet;
import javax.sql.rowset.CachedRowSet;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

import org.w3c.dom.*;

/**
 * Implements a <b>L.O.V.</b>, which is a List Of Values.
 * <br>
 * <h3><a name="dependency">Dependency</a></h3>
 * A slctLst instance can de set dependent on another slctLst instance.
 * The former instance will be referenced as <i>slave</i> whereas the latter
 * will be called <i>master</i>.
 * The dependency relationsheep means that the master L.O.V. affects the fetching
 * of values for the slave L.O.V. - values fetched for the slave
 * must match the SQL condition defined by the master L.O.V. (by all of
 * the master L.O.V.s set for that slave).
 */
public class slctLst
{
 Vector v;                             //values vector
 String parName;                       //HTTP parameter name for value selected
 String selButName;                    //HTTP parameter name for fetch button
 dbRdr dbr;                            //pooled database reader
 dateRd dtrd = new dateRd();           //date reader

/*
 * Advanced Features
 */
 boolean hidden = false;               //hidden flag
 boolean auto = false;                 //when set to yes automatic fetch is triggered by
                                       //dependency relationships
 String fieldName;                     //database fieldname
 boolean isExpr = false;               //either database fieldname or expression
 boolean distinctBool = true;          //true iff only distinct rows must be considered
 String tableName;                     //database tablename
 String factTableName=null;            //database fact tablename
 String blankField;                    //representation for 'no value selected'
 protected String initVal=null;        //initial value (when <> from blankField)
 Vector singleDep = new Vector();      //vector of master fields
 Vector chartBnds=null;                //list of bound charts ('chartSect' instances)
 protected attrLst attrs=null;         //vector of database attribute fields
 int index=-1;                         //in the ordinal number of this object
                                       //within a select list instances array
 boolean alwaysActive=false;           //if set, this field always affects
                                       //depenedent fields, even if it is blank
 char type;                            //field data type (see constants below)
 int rowsN;                            //number of rows visualized in HTML
                                       //SELECT LIST form (not used in NEW GUI)
 String name="";                       //name (used to reference the field
                                       //within a select list array)
 String label="";                      //label
 boolean isMand=false;                 //mandatory.
 boolean isMulti=false;                //multi selection allowed
 boolean isKeyLst=false;               //key list flag
 boolean editable=false;               //if yes it is possible to directly edit
                                       //the L.O.V: without accessing the
                                       //database.
 String grpByList=null;                //overrides the default 'group by'
                                       //clause - it's in the form of a sql
                                       //order by clause without 'GROUP BY'
 String grpByList2=null;               //eventually (if not null) overrides
                                       //grpByList for the detailed report
 String orderList=null;                //overrides the default 'order by'
                                       //clause - it's in the form of a sql
                                       //order by clause without 'ORDER BY'
 String orderList2=null;               //eventually (if not null) overrides
                                       //orderList for the detailed report
 Vector whrCndTables=null;             //list of tables overriding table name
                                       //to the purpose of the where condition
                                       //definition
 Vector whrCndFields=null;             //list of fields which operates in
                                       //conjunction with whrCndTables
 String defaultWhrTable=null;          //table which the where condition is applied
                                       //when it comes to writing/exporting the sel crit
 int maxNofSelKeysAllowed=0;           //Maximum number of selected keys
                                       //allowed

 public static final char _CHAR= 'C';  //constant for character data
 public static final char _NUM = 'N';  //constant for numeric data
 public static final char _DATE= 'D';  //constant for date data

 LogWriter log = null;

 public void setLogWriter(LogWriter log) {this.log=log;}
 public void debug(Object msg) {if (log!=null) log.p(msg);}
 public void debug(String msg) {if (log!=null) log.p(msg);}

/**
 * Returns this L.O.V.'s database reader
 */
 public dbRdr getDbRdr() { return dbr; }

/**
 * Returns the <i>HTML parameter</i> name associated to this L.O.V.
 */
 public String getParName() { return parName; }

/**
 * Returns the <i>selBut</i> name - it's the name of
 * the HTML submit button which triggers this L.O.V. fetches
 */
 public String getButName() { return selButName; }

/**
 * Returns the database field name associated to this L.O.V.
 */
 public String getFieldName() { return fieldName; }

/**
 * Returns the database table name associated to this L.O.V.
 */
 public String getTableName() { return tableName; }

 public void setDistinctBool(boolean b) { this.distinctBool=b; }

/**
 * Returns the <i>blankField</i> value (it is the default value associated
 * to the L.O.V. and usually consists in the empty string)
 */
 public String getBlankField() { return blankField; }

/**
 * Returns true if multiple selection for this L.O.V. is allowed.
 * <br>Multiple selections will be expanded into SQL conditions by
 * means of the <i>IN</i> SQL operator</br>
 */
 public boolean isMulti() { return isMulti; }

/**
 * Returns true if this L.O.V. is a <i>key</i> L.O.V.
 * <br><i>Key</i> L.O.V.s
 * contain that data which is input for further elaboration processes (ie: which
 * act so as a key data for back-end process elaborations).
 * <br>Even if no data has been explicitly selected for the key
 * L.O.V. by the user, the <i>check</i> method (which is called at the end
 * of the data selection process) is responsible for fetching values for this
 * L.O.V. according to the dependency relationsheeps defined for it.
 */
 public boolean isKeyLst() { return isKeyLst; }

/**
 * Returns the values vector.
 */
 public Vector getVector() { return v; }

/**
 * Resets the L.O.V.
 * this vector is merely cleared.
 */
 public void resetVector() { v = new Vector(); }

/**
 * Resets the L.O.V. to the <i>blank</i> value.
 * It precisely resets it to a vector containing
 * only a <i>blankField</i> instance.
 */
 public void setBlank()
 {
   v.clear();
   v.addElement(blankField);
 }

/**
 * Defines the L.O.V. initial value.
 * It is intended to provide an initial value different from
 * the blank field
 */
 public void setInitVal(String val)
 {
   this.initVal=val;
 }

 public String getInitVal() {return initVal;}

 /**
  * Sets the current L.O.V. value to the initial value (initVal)
  */
 public void initVal()
 {
   if (initVal!=null)
   {
     v.clear();
     v.addElement(initVal);
   }
 }


/**
 * Returns this object value.
 * If the L.O.V. (class member <i>v</i>) has got only
 * one entry it just returns that entry (which is a String).
 * If it is not the case returns the first selected item.
 */
 public String getVal()
 {
   return (String)v.elementAt(0);
 }

/**
 * Sets the current L.O.V. value
 */
 public void setVal(String val)
 {
   v.clear();
   v.addElement(val);
 }

/**
 * Sets the current L.O.V. value
 */
 public void setVal(Vector vals)
 {
   v.clear();
   for (int i=0; i<vals.size(); i++)
   {
     v.addElement(vals.elementAt(i));
   }
 }

/**
 * Returns the value of the atribute named as <i>attrName</i> associated to
 * this object.
 *
 * @see #attrLst.getVal(String, String)
 * @return null - if no matching row is found, but throws an exception if
 * the referenced attribute hasn't been defined
 */
 public String getAttrVal(String attrName)
 //if the attributes vector hasn't been set up it returns an exception
 {
   return getAttrVal(attrs.getAttr(attrName), getVal());
 }

 public String getAttrVal(int attrIndex)
 {
   String s=attrs.getVal(attrIndex, getVal());
   return s;
 }

 public String getAttrName(int attrIndex)
 {
   return attrs.getName(attrIndex);
 }

 public String getAttrLabel(int attrIndex)
 {
   return attrs.getLabel(attrIndex);
 }

 public String getAttrLabel(String attrName)
 {
   return attrs.getLabel(attrName);
 }

/**
 * Returns the value of the atribute named as <i>attrName</i> associated to
 * the value <i>keyVal</i> of this L.O.V.
 *
 * @see #attrLst.getVal(String, String)
 * @return null - if no matching row is found, but throws an exception if
 * the referenced attribute hasn't been defined
 */
 public String getAttrVal(String attrName, String keyVal)
 //if the attributes vector hasn't been set up it returns an exception
 {
   return this.getAttrVal(attrs.getAttr(attrName), keyVal);
 }

 public String getAttrVal(int attrIndex, String keyVal)
 {
   String s=attrs.getVal(attrIndex, keyVal);
   return s;
 }

 public attrLst getAttrLst() {return attrs;}

/**
 * Returns the value of the atribute named as <i>attrName</i> associated to
 * the n-th row of the attributes report (the primary report).
 *
 * @see #attrLst.getVal(String, int)
 * @return null - if no matching row is found, but throws an exception if
 * the referenced attribute hasn't been defined
 */
 public String getAttrVal(String attrName, int n)
 //if the attributes vector hasn't been set up it returns an exception
 {
   return this.getAttrVal(attrs.getAttr(attrName), n);
 }

 public String getAttrVal(int attrIndex, int n)
 //if the attributes vector hasn't been set up it returns an exception
 {
   String s=attrs.getVal(attrIndex, n);
   return s;
 }

/**
 * Returns the n. of attributes for this L.O.V. (0 if no attribute has been defined)
 */
 public int getAttrsN() {return (attrs==null ? 0 : attrs.size());}

 public boolean isAlwaysActive() { return(alwaysActive); }

/**
 * Returns the data type associated to this object's L.O.V.
 *
 * @see #_CHAR
 */
 public char getType() { return type; }

/**
 * Returns a SQL condition corresponding to the current L.O.V.
 * <br>If this object's L.O.V. consists of only one entry the string returned is
 * of such a type:<br>
 * <li>tableName.fieldName = value</li>
 * <br>If instead the L.O.V. consists of many elements the string returned is of
 * such a type:<br>
 * <li>tableName.fieldName IN (value#1, ..., value#n)</li>
 */
 public String getSqlVal()
 {
   return getSqlVal(getVal());
 }

 private String getSqlVal(String value)
 {
  String s;
  if (type==_CHAR)
     {s = new String("'"); s = s.concat(value); s = s.concat("'");}
  else
   if (type==_DATE)
      {s = dbr.applyTO_DATE(value,true);}
    else
       {s = value;}
  return s;
 }

/**
 * Returns true iff the L.O.V. is empty.
 * It still returns true if the L.O.V. consists of the <i>blankField</i>
 * instance only.
 */
 public boolean isBlank()
 {return ((v.size()==0)||((v.size()==1)&&((String)v.elementAt(0)).equals(blankField)));}

/**
 * returns true iff the L.O.V. cardinality (i.e.: the number of values)
 * doesn't exceed the maximum allowed (if defined), which is provided
 * by the maxNofSelKeysAllowed member
 */
 public boolean checkNofSelKeys()
 {
  if (this.maxNofSelKeysAllowed>0)
  {
    return (v.size()<=this.maxNofSelKeysAllowed);
  }
  else return true;
 }

 int getMaxNofSelKeysAllowed()
 {return this.maxNofSelKeysAllowed;}

 void setMaxNofSelKeysAllowed(int n)
 {this.maxNofSelKeysAllowed=n;}

/**
 * Returns true if at least one <i>attribute</i> has been defined for this
 * object.
 */
 public boolean hasAttrs() {return attrs!=null;}

/**
 * Returns the number of <i>attribute</i> defined for this object.
 */
 public int nOfAttrs() {return((attrs==null) ? 0 : attrs.size());}

/**
 * Returns the label.
 * It's the description which appears into the data selection left
 * panel and into the reports.
 */
 public String getLabel() {return label;}

/**
 * Returns the name of this L.O.V.
 * Many of the <i>dataSel</i> object's APIs address L.O.V.s by means of their
 * <i>names</i>.
 */
 public String getName() {return name;}

/**
 * Returns a string in the form label=val
 * If attrName==null, the L.O.V. value is considered, otherwise
 * the value of that attribute referenced by attrName is considered.
 * If the referenced attribute doesn't exist, null is returned.
 * If the referenced value is empty, the empty string is returned.
 * If the referenced L.O.V. conists of more than one item selected, an
 * empty string is returned.
 */
 public String getBindString(String attrName, String label, boolean labelBool)
 {
   if ((attrName!=null)&&(this.getAttrVal(attrName)==null)) return null;
   else
   {
     String labelStr=(label.equals("") ? "" : label.concat("="));
     String val=(attrName==null ? (this.v.size()==1 ? this.getVal() : "") : this.getAttrVal(attrName));
     if (!val.equals("")) return new String(labelBool ? labelStr+val : val);
     else return val;
   }
 }

/**
 * Returns true iff this L.O.V. is <i>mandatory</i>.
 * The user can't proceed  with the elaboration process unless he has
 * selected values for each of the mandatory L.O.V.s
 */
 public boolean isMand() {return isMand;}

 public void setMand() {isMand=true;}

 public void setKey()  {isKeyLst=true;}

 public void setGrpByLst(String s, int level)
 {
   if (level==1) this.grpByList=s;
   else if (level==2) this.grpByList2=s;
 }

 public void setOrderLst(String s, int level)
 {
   if (level==1) this.orderList=s;
   else if (level==2) this.orderList2=s;
 }

 public boolean isEditable() {return editable;}

 public void setEditable() {editable=true;}

 public boolean isHidden() {return hidden;}

 public void setAsHidden(boolean b) {hidden=b;}

 public boolean isAuto() {return auto;}

 public void setAuto(boolean b) {auto=b;}

/**
 * Sets a dependency between this object and <i>lst</i>.
 * It makes this object <a href="#dependency">depend</a> on <i>lst</i>.
 */
 public void addDep(slctLst lst)
 {
  singleDep.addElement(lst);
 }

/**
 * Returns true if any bound chart exists
 */
 public boolean hasCharts() {return (chartBnds!=null);}

/**
 * Binds a chartSect instance to this object.
 */
 public void addChart(chartSect chart)
 {
  if (chartBnds==null) chartBnds=new Vector();
  chartBnds.addElement(chart);
 }

/**
 * Gets the chartSect list
 */
 public Vector getCharts() {return chartBnds;}

/**
 * Associates an attribute to this L.O.V.
 * <b>Remark:</b> the first time this method is invoked for a stated L.O.V.,
 * an additional attribute is then added: this <i>implicit</i> atttribute is
 * bound to the same database field as the L.O.V. itself and it is employed
 * as a sort of <i>join</i> key between the L.O.V. values and their respective
 * attributes rows.
 * <b>Remark:</b> attributes are fetched together with the L.O.V. values.
 */
 public void addAttr(String name, String label, char type, String fieldName, String tableName, String sqlexpr, String format, Element funzEl)
 {
  //an attribute list is allocated provided that
  //almost one attribute is defined
  if (attrs==null)
  {
    attrs=new attrLst();
    attrs.setLogWriter(log);
    attrs.add(this.name, this.label, this.type, this.fieldName, this.tableName, this.tableName+"."+this.fieldName, null, null);
  }
  attrs.add(name,  label,  type, fieldName, tableName, sqlexpr, format, funzEl);
 }

/**
 * It is the same as the {@link #addAttr(String, String, char, String, String, String)}
 * methods but it deals with <i>detailed attributes</i> which appears into the
 * detailed reports only.
 * <b>Remark:</b>When a detailed report is requested another fetch is then
 * performed in order to retrieve values for the detailed attrubutes too
 * (which aren't feched at the same time as the L.O.V. values)
 */
 public void addDetail(String name, String label, char type, String fieldName, String tableName, String sqlexpr, String format, Element funzEl)
 {
  //an attribute list is allocated provided that
  //almost one attribute is defined
  if (attrs==null)
  {
    attrs=new attrLst();
    attrs.setLogWriter(log);
    attrs.add(this.name, this.label, this.type, this.fieldName, this.tableName, this.tableName+"."+this.fieldName, null, null);
  }
  attrs.addDetail(name,  label,  type, fieldName, tableName, sqlexpr, format, funzEl);
 }

/**
 * Clears all of the detailed attributes values (clears all of the rows
 * returned by the detailed report.
 */
 public void clearDetails()
 {
   if (attrs!=null) attrs.clear(true);
 }

/**
 * Clears all of the primary attributes values.
 */
 public void clearAttrs()
 {
   if (attrs!=null) attrs.clear(false);
 }

 public slctLst() {}

 /**
 * Constructor.
 *
 * @param p name of that HTML parameter associated to this L.O.V.
 * @param d database reader
 * @param t name of the database table bound to this L.O.V.
 * @param f name of the database field bound to this L.O.V.
 * @param blnk <i>blankField</i> value (it is the default value associated
 *        to the L.O.V. and usually consists in the empty string)
 * @param bt it's the <i>selButton</i> name: precisely it's the name of
 *        that HTML submit button which triggers this L.O.V. fetches
 * @param rowsN defines the number of visible rows which the associated
 *        HTML form consists of
 * @param isMulti if true allows multiple selection for this L.O.V.
 *        (multiple selections will be expanded into SQL conditions by
 *        means of the <i>IN</i> SQL operator)
 */
 public slctLst(String name, String label, char type, String p, dbRdr d, String t, String f,
                String blnk, String bt, int rowsN, boolean isMulti)
 {
  parName = p;
  dbr = d;
  this.name = name;
  this.label = label;
  tableName = t;
  fieldName = f;
  blankField = blnk;
  selButName = bt;
  index = getIndex(); //called after having set selButName
  this.type = type;
  this.rowsN = rowsN;
  this.isMulti = isMulti;
  //initializes the values vector
  v=new Vector(); v.addElement(blankField);
  this.isExpr=false;
 }

 public void setDefaultWhrTable(String s) {this.defaultWhrTable=s;}

 public String getDefaultWhrTable() {return this.defaultWhrTable;}

 public void setAlternativeWhrCond(String tblname, String fldname)
 {
  if (this.whrCndTables==null) this.whrCndTables=new Vector();
  this.whrCndTables.addElement(tblname);
  if (this.whrCndFields==null) this.whrCndFields=new Vector();
  this.whrCndFields.addElement(fldname);
 }

 public String getDefaultWhrField()
 {
     if (this.defaultWhrTable.equals(this.tableName)) return this.fieldName;
     else return getAlternativeWhrCondField(this.defaultWhrTable);
 }

 public String getAlternativeWhrCondField(String slaveTableName)
 {
  int i=0;
  boolean tblFound=false;
  if ((this.whrCndTables==null)||(this.whrCndFields==null)) return null;
  for (i=0; i<this.whrCndTables.size(); i++)
  {
    tblFound=(((String)whrCndTables.elementAt(i)).equals(slaveTableName));
    if (tblFound) break;
  }
  return (tblFound ? (String)this.whrCndFields.elementAt(i) : null);
 }


 public boolean hasAlternativeWhrCond(String slaveTableName)
 {
  return (this.getAlternativeWhrCondField(slaveTableName)!=null);
 }

 public String getWhrCond(String slaveTableName)
 {
   return this.getWhrCond(slaveTableName, null);
 }

/**
 * Returns a string consisting in the SQL condition defined by current
 * values selected for this L.O.V.
 */
 public String getWhrCond(String slaveTableName, Vector keyVals)
 {
  if ((alwaysActive)||(!isBlank())||(keyVals!=null))
  {
         Vector v=(keyVals==null ? this.v : keyVals);
         String tempFieldName = (!isExpr ? new String(this.tableName+"."+fieldName) : new String(fieldName));
         if (this.hasAlternativeWhrCond(slaveTableName))
         {
           String altFieldName=this.getAlternativeWhrCondField(slaveTableName);
           tempFieldName=(altFieldName.indexOf('.')>=0 ? "" : slaveTableName+".")+altFieldName;
         }
         String str="";
         str = str.concat(tempFieldName);
         if (v.size()==1)
         {
           String val0=(String)v.elementAt(0);
           if (((val0.startsWith("%"))||(val0.endsWith("%")))&&(getType()=='C'))
           {
             str = str.concat(" LIKE ");
           }
           else
           {
             str = str.concat("=");
           }
           str = str.concat(getSqlVal(val0));
         }
         else
         {
           str = str.concat(" IN(");
           for (int i=0; i<v.size(); i++)
           {
             str = str.concat(getSqlVal((String)v.elementAt(i)));
             if (i<v.size()-1) str = str.concat(", ");
           }
           str = str.concat(")");
         }
         return str;
  }
  else return null;
 }

 public void fetch() throws Exception
 {
  this.fetchDependent(false, null, null);
 }

 public void fetch(boolean mode, Writer out) throws Exception
 {
  this.fetchDependent(mode, null, out);
 }

 public void fetch(boolean mode) throws Exception
 {
  this.fetchDependent(mode, null, null);
 }


 public void fetch(boolean mode, Writer out, String addWhrStr) throws Exception
 {
  this.fetchDependent(mode, null, out, true, null, addWhrStr);
 }


 protected boolean masterFetched(HttpServletRequest req)
 //tells if the button associated to a master field
 //has been clicked - it lets you know if the slave field's value
 //must be reset to 'blanlField'
 {
  for (int i=0; i<singleDep.size(); i++)
  {
   if (req.getParameterValues(((slctLst)singleDep.elementAt(i)).getButName())!=null)
      return true;
  }
  return false;
 }

/**
 * @see #fetchDependent(boolean, HttpServletRequest)
 */
 public boolean fetchDependent(HttpServletRequest req) throws Exception
 {
   return fetchDependent(false, req, null, true, null);
 }

 public boolean fetchDependent(boolean mode, HttpServletRequest req) throws Exception
 {
   return fetchDependent(mode, req, null, true, null);
 }

 public boolean fetchDependent(boolean mode, HttpServletRequest req, Writer out) throws Exception
 {
   return fetchDependent(mode, req, out, true, null);
 }

 public boolean fetchDependent(boolean mode, HttpServletRequest req, Writer out, boolean showAllBool, Vector keyVals) throws Exception
 {
   return fetchDependent(mode, req, out, showAllBool, keyVals, null);
 }

/**
 * Fetches this L.O.V. values according to the filtering conditions
 * defined by the <i>master</i> L.O.V.s.
 *
 * @param mode if false means that no detailed attributes must be retrieved
 *        (the main level attributes, defined by the <i>addAttr</i> method, are
 *         fetched anyway)
 *
 public boolean fetchDependent(boolean mode, HttpServletRequest req, Writer out, boolean showAllBool, Vector keyVals, String addWhrStr) throws Exception
 //mode = false, means that no detailed attributes must be retrieved
 //PS: req is not employed
 {
  boolean firstCond;
  Vector tbls = new Vector();
  slctLst mLst; String whrStr=""; String whrCond;
  firstCond = false;

  for (int i = 0; i<singleDep.size(); i++)
  {
    mLst = (slctLst)singleDep.elementAt(i);
    if ((whrCond=mLst.getWhrCond(this.tableName))!=null)
    {//in case the condition is relevant:
      if (!firstCond) whrStr=whrStr.concat(" WHERE ");
      else whrStr=whrStr.concat(" AND ");
      firstCond = true;
      whrStr = whrStr.concat(whrCond);
      if (!mLst.hasAlternativeWhrCond(this.tableName)) popTable(tbls, mLst.getTableName());
    }
    else continue;
  }//for
  if (addWhrStr!=null) whrStr=whrStr+(!firstCond ? " WHERE " : " AND ")+addWhrStr;
  if (!showAllBool)
  {
    if (keyVals==null) keyVals=this.getVector();
    if (!firstCond) whrStr=whrStr.concat(" WHERE ");
    else whrStr=whrStr.concat(" AND ");
    whrStr = whrStr.concat(this.getWhrCond(this.tableName, keyVals));
  }
  //attribute list tables check
  if (attrs!=null)
  {
    for (int i=0; i<attrs.size(); i++)
    {
      if ((mode)||!(attrs.isDetailed(i))) popTable(tbls, attrs.getTableName(i));
    }
  }
  if (!mode)
  {
    v.clear();
    this.clearAttrs();
  }
  getRows(mode, v, whrStr, tbls, out);
  //debug(v);
  if (v.size()==0) v.addElement(blankField);
  return firstCond;
}*/

  /**
   * Fetches this L.O.V. values according to the filtering conditions
   * defined by the <i>master</i> L.O.V.s.
   *
   * @param mode if false means that no detailed attributes must be retrieved
   *        (the main level attributes, defined by the <i>addAttr</i> method, are
   *         fetched anyway)
   */
  public boolean fetchDependent(boolean mode, HttpServletRequest req, Writer out, boolean showAllBool, Vector keyVals, String addWhrStr) throws Exception
  //mode = false, means that no detailed attributes must be retrieved
  //PS: req is not employed
  {
     boolean firstCond;
     String whrStr = getWhereCls(mode, showAllBool, keyVals, addWhrStr);
     Vector tbls = getTablesList(mode);
     if (!mode)
     {
       v.clear();
       this.clearAttrs();
     }
     getRows(mode, v, whrStr, tbls, out);
     if (v.size()==0) v.addElement(blankField);
     return true;
  }

  public int fetchCount() throws Exception
  {return fetchCount(false, false, null, null);}

  /* Old Code 
  public int fetchCount(boolean mode, boolean showAllBool, Vector keyVals, String addWhrStr) throws Exception
  //mode = false, means that no detailed attributes must be retrieved
  //PS: req is not employed
  {
     String whrStr = getWhereCls(mode, showAllBool, keyVals, addWhrStr);
     Vector tbls = getTablesList(mode);
     String query=dbr.getQuery(tableName, factTableName, tableName+"."+fieldName, whrStr, getOrderCls(false), tbls, distinctBool);
     boolean limitBool=true;
     ResultSet rs=dbr.getRecordset(query, limitBool);
     int i;
     for (i=0; rs.next(); i++) {}
     rs.close();
     return i;
  }
  */

  public int fetchCount(boolean mode, boolean showAllBool, Vector keyVals, String addWhrStr) throws Exception
  //mode = false, means that no detailed attributes must be retrieved
  //PS: req is not employed
  {
     String whrStr = getWhereCls(mode, showAllBool, keyVals, addWhrStr);
     Vector tbls = getTablesList(mode);
     String query=dbr.getQuery(tableName, factTableName, tableName+"."+fieldName, whrStr, getOrderCls(false), tbls, distinctBool);
     boolean limitBool=true;
     CachedRowSet rs=dbr.getCacheset(query, limitBool);
     int i;
     for (i=0; rs.next(); i++) {}
     rs.close();
     return i;
  }  
  
public Vector getTablesList(boolean mode)
{
    boolean firstCond;
    Vector tbls = new Vector();
    slctLst mLst; String whrStr=""; String whrCond;
    firstCond = false;

    for (int i = 0; i<singleDep.size(); i++)
    {
      mLst = (slctLst)singleDep.elementAt(i);
      if ((whrCond=mLst.getWhrCond(this.tableName))!=null)
      {
        //in case the condition is relevant:
        if (!mLst.hasAlternativeWhrCond(this.tableName)) popTable(tbls, mLst.getTableName());
      }
      else continue;
    }//for
    //attribute list tables check
    if (attrs!=null)
    {
      for (int i=0; i<attrs.size(); i++)
      {
        if ((mode)||!(attrs.isDetailed(i))) popTable(tbls, attrs.getTableName(i));
      }
    }
    return tbls;
}

public String getWhereCls(boolean mode) throws Exception
{
  return getWhereCls(mode, true, null, null);
}

public String getWhereCls(boolean mode, boolean showAllBool, Vector keyVals, String addWhrStr) throws Exception
{
    boolean firstCond;
    slctLst mLst; String whrStr=""; String whrCond;
    firstCond = false;

    for (int i = 0; i<singleDep.size(); i++)
    {
      mLst = (slctLst)singleDep.elementAt(i);
      //Auto-fetch field case
      if (mLst.isAuto())
      {
          mLst.fetch();
      }
      if ((whrCond=mLst.getWhrCond(this.tableName))!=null)
      {
        //in case the condition is relevant:
        if (!firstCond) whrStr=whrStr.concat(" WHERE ");
        else whrStr=whrStr.concat(" AND ");
        firstCond = true;
        whrStr = whrStr.concat(whrCond);
      }
      else continue;
    }//for
    if (addWhrStr!=null) whrStr=whrStr+(!firstCond ? " WHERE " : " AND ")+addWhrStr;
    if (!showAllBool)
    {
      if (keyVals==null) keyVals=this.getVector();
      if (!firstCond) whrStr=whrStr.concat(" WHERE ");
      else whrStr=whrStr.concat(" AND ");
      whrStr = whrStr.concat(this.getWhrCond(this.tableName, keyVals));
    }
    return whrStr;
}


/**
 * Refreshes db data currently selected for this L.O.V.
 */
 public void refreshSelData(boolean mode, HttpServletRequest req) throws Exception
 {
  fetchDependent(mode, req, null, false, null);
 }

 private void popTable(Vector tbls, String t)
 {
   if (t.equals(tableName)) return;
   for (int i=0; i<tbls.size(); i++)
   {
     if (((String)tbls.elementAt(i)).equals(t)) return;
   }
   tbls.addElement(t);
 }

 protected String getFldLst(boolean mode)
 {
  String fldStr="";
  if (attrs==null)
  {
    if (isExpr) fldStr=new String(fieldName);
    else
    {
      String fName=new String(tableName+"."+fieldName);
      fldStr=fldStr.concat((type==_DATE) ? dbr.applyTO_CHAR(fName,false) : fName);
    }
  }
  else
  {
   for (int i=0; i<attrs.size(); i++)
   {
    if ((!mode)&&(attrs.isDetailed(i))) continue;
    if (attrs.isComputed(i)) continue;
    if (i>0) fldStr=fldStr.concat(", ");
    fldStr=fldStr.concat(attrs.getSqlExpr(i));
   }
  }
  return fldStr;
 }

 private String getGrpByCls(boolean mode)
 {
  String grpByCls=" GROUP BY ";
  String actualGrpByLst=this.grpByList;
  if (mode) actualGrpByLst=(this.grpByList2!=null ? this.grpByList2 : this.grpByList);
  if (actualGrpByLst==null) return new String("");
  else return grpByCls.concat(actualGrpByLst);
 }

 private String getOrderCls(boolean mode)
 {
  String orderCls=" ORDER BY ";
  String actualOrdLst=this.orderList;
  if (mode) actualOrdLst=(this.orderList2!=null ? this.orderList2 : this.orderList);
  if (actualOrdLst==null)
  {
    if (fieldName==null) return new String("");
    else
    {
      if (isExpr) return orderCls.concat(fieldName);
      else
      {
        String fName=new String(tableName+"."+fieldName);
        return orderCls.concat((type==_DATE) ? dbr.applyTO_CHAR(fName,false) : fName);
      }
    }
  }
  else return orderCls.concat(actualOrdLst);
 }

 public String getQuery(boolean mode) throws Exception
 {
    return dbr.getQuery (   tableName,
                            factTableName,
                            getFldLst(mode),
                            getWhereCls(mode),
                            getGrpByCls(mode)+" "+getOrderCls(mode),
                            getTablesList(mode),
                            this.distinctBool
                        );
 }


/**
 * Sets the rows from a persistent criterion XML element.
 */
 public void setRows(Element fldEl) throws Exception
 {
   boolean mode=false;
   resetVector();
   this.clearAttrs();
   Element condEl=(Element)fldEl.getParentNode();
   NodeList nl=condEl.getElementsByTagName("VAL");
   for (int row=0; row<nl.getLength(); row++)
   {

         Element rowEl=(Element)nl.item(row);
         v.addElement(xmlRdr.getVal(rowEl));
         if (attrs!=null)
         {
           attrs.setRow(rowEl);
         }
   }
 }

 protected void getRows(boolean mode, Vector v, String whrStr, Vector tbls, Writer out) throws Exception
 //retries the data fetch for up to three times - if the fetch fails
 //for the third time, this method returns a SQLException.
 {
  String query=dbr.getQuery(tableName, factTableName, getFldLst(mode), whrStr, getGrpByCls(mode).concat(" "+getOrderCls(mode)), tbls, this.distinctBool);
  //Logging removed on 1st July 2008
  //debug("");
  debug("slctLst :: getRows : QUERY>"+query);
  SQLException sqlErr=null;
  boolean committed;
  int i=0;
  if (out!=null)
  {
    out.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+"\n");
    out.write("<STDATA>"+"\n");
    out.write("<STDATASELECTION DATAID=\"datasel\" TYPE=\"ROW\">"+"\n");
    out.write("<TITLE>"+"FIELD "+"'"+getLabel()+"'"+(mode ? " DETAILED" : "")+" REPORT"+"</TITLE>"+"\n");
    if (attrs!=null) attrs.getLabelsAsXml(mode, out);
    else
    {
     out.write("<STDATA_COLUMNS>"+"\n");
       out.write("<STDATA_COLUMN name=\""+this.getName()+"\" label=\""+this.getLabel()+"\"/>"+"\n");
     out.write("</STDATA_COLUMNS>"+"\n");
    }
  }
  long queryStartTime=System.currentTimeMillis();
  //-- DB FETCH (LOOP) --//
  do
  {
    committed=true;
    //only if I've not requested the detailed report
    //the field values are cleared
    try {dbr.getRows(v, out, mode, attrs, query);}
    catch(SQLException e) {sqlErr=e; committed=false;}
    i++;
  }
  while ((!committed)&&(i<2));
  //-- IF FETCH OPERATION WAS OK --//
  if (out!=null)
  {
    out.write("</STDATASELECTION>"+"\n");
    out.write("</STDATA>"+"\n");
  }
  if (!committed) throw sqlErr;
  else
  {
    Long queryDuration = new Long(System.currentTimeMillis()-queryStartTime);
    debug("DURATION [msec]>"+queryDuration.toString()); 
  }
 }

/**
 * Checks that <i>val</i> is a valid value for this L.O.V. associated
 * data type - eventually changes its format.
 *
 * @exception NumberFormatException if <i>val</i> is not a valid value
 */
 public String check(String val) throws NumberFormatException
 {
  String msg = "";
  String formatErrStr = "";
  String str;
  //TRIM !
  val = val.trim();
  try
  {
     if (type == _CHAR)
     {
       return val;
     }
     else
       if (type == _NUM)
       {
         formatErrStr = new String("FIELD '"+label+"' - NUMERIC FORMAT EXPECTED");
                     Double.valueOf(val);

                     return val;
       }
       else //type == _DATE
       {
         formatErrStr = new String("FIELD '"+label+"' - DATE FORMAT EXPECTED.");
         str=dtrd.getDate(val);
         return str;
       }
  }
  catch (Exception e)
  {
   msg = formatErrStr;
   throw new NumberFormatException(msg);
  }
 }


 protected Vector cmptParToVect(String s[])
 {
  Vector vCmpt = new Vector();
  try
   {
    for (int i=0;true;i++)
    {
     String str = s[i];
     vCmpt.addElement(check(str));
    }
   }
  catch (ArrayIndexOutOfBoundsException  e) {}
  return vCmpt;
 }

 protected Vector cmptStrToVect(String s, String optns)
 {
  StringTokenizer st = new StringTokenizer(s,"\n\r\t\f ",false);
  String str;
  Vector v = new Vector();
  while (st.hasMoreTokens())
  {
    str = st.nextToken().trim();
    str = str.concat(optns);
    v.addElement(str);
  }
  return v;
 }

 protected Vector cmptStrToVect(String s)
 {
  return cmptStrToVect(s,"");
 }

 protected String cmptParToString()
 {
  String sCmpt = new String();
    for (int i=0;i<v.size();i++)
    {
     sCmpt = sCmpt.concat((String)v.elementAt(i));
     sCmpt = sCmpt.concat("\n");
    }
  return sCmpt;
 }


/**
 * Returns an HTML form list representation of this L.O.V.
 * There's no default option defined.
 */
 public static void toHtmlString(JspWriter out, Vector v) throws Exception
 {
   if (v.size()>0)
   {
         String optionTag;
         for (int i = 0; i < v.size(); i++)
           {
                  optionTag = new String(" <option>");
                  out.println(optionTag + v.elementAt(i));
           }
   }
 }

/**
 * @see calls overloaded method processing a list of XML nodes
 */
 public static void toHtmlString(JspWriter out, String xmlFileName) throws Exception
 {
   slctLst.toHtmlString(out, xmlRdr.getRoot(xmlFileName, false));
 }

/**
 * @see calls overloaded method processing a list of XML nodes
 */
 public static void toHtmlString(JspWriter out, Element n) throws Exception
 {
  slctLst.toHtmlString(out, n.getChildNodes());
 }

/**
 * Returns an HTML form list representation of this L.O.V.
 * There's no default option defined.
 */
 public static void toHtmlString(JspWriter out, NodeList nL) throws Exception
 {
   for (int i=0; i<nL.getLength(); i++)
   {
     Node node = nL.item(i);
     if (!(node instanceof Element)) continue;
     NodeList nLchild = ((Element)node).getChildNodes();
     String rec = new String();
     for (int j=0; j<nLchild.getLength(); j++)
     {
       Node child = nLchild.item(j);
       if (child instanceof Element)
       {
         rec = rec.concat(((Element)child).getFirstChild().getNodeValue());
         rec = rec.concat(" ");
       }
       else continue;
     }
     if (!rec.equals(""))
     {
       out.println(" <option>"+rec);
     }
   }
 }

/**
 * Returns an HTML form list representation of this L.O.V.
 * The first value is defined as default option.
 * If this is a <i>key</i> L.O.V. the eventual <i>blank</i> values
 * are discarded.
 */
 public void toHtmlString(JspWriter out) throws Exception
 {
   if (v.size()>0)
   {
         String optionTag = new String(" <option>");
         String firstOptionTag = new String(" <option selected>");
         String tag = optionTag;
         for (int i = 0; i < v.size(); i++)
         {
           tag = ((i==0) ? firstOptionTag : optionTag);
           if ((!isKeyLst)||(!((String)v.elementAt(i)).equals(blankField)))
              out.println(optionTag + v.elementAt(i));
         }
   }
 }

/**
 * Defines an HTML form list and then calls the <i>toHtmlString</i> method
 * to fill it.
 */
 public void toHtml_defString(JspWriter out) throws Exception
 {
  String multStr  = new String();
        String multiDcl = new String();
  if (isMulti)
      multiDcl = new String("multiple");
        if (rowsN > 0)
        {
          multStr = new String(" size = \"" + rowsN + "\" " + multiDcl);
          out.println("<br>");
        }
        out.println(" <SELECT NAME=\"" + parName + "\" " + multStr + ">");
        toHtmlString(out);
        out.println(" </SELECT>");
 }

/**
 * Returns the attributes report in an XML format
 */
 public Document getReportAsXml(int maxColsN, boolean mode, boolean showAllBool, Vector keyVals) throws Exception
 {
   if (attrs==null)
   {
     Document doc=xmlRdr.getNewDoc("STDATA");
     Element dselEl=xmlRdr.addEl(doc.getDocumentElement(), "STDATASELECTION");
     dselEl.setAttribute("DATAID","datasel");
     dselEl.setAttribute("TYPE","ROW");
     //--- TITLE DEFINITION ---//
     String title=new String(getLabel().toUpperCase()+" DETAILED REPORT");
     xmlRdr.setVal(xmlRdr.addEl(dselEl, "TITLE"), title);
     //--- LABELS DEFINITION ---//
     Element labsEl=xmlRdr.addEl(dselEl, "STDATA_COLUMNS");
     Element labEl =xmlRdr.addEl(labsEl, "STDATA_COLUMN");
     labEl.setAttribute("name",  getName());
     labEl.setAttribute("label", getLabel());
     //--- ROWS INSERTION ---//
     for (int row=0; row<v.size(); row++)
     {
       Element rowEl=xmlRdr.addEl(dselEl, "STDATA_RECORD");
       Element colEl=xmlRdr.addEl(rowEl, "FIELD");
       xmlRdr.setVal(colEl, (String)v.elementAt(row));
     }
     return doc;
   }
   else
   {
     if ((!showAllBool)&&(keyVals==null))
     {
       keyVals=this.getVector();
     }
     return attrs.getReportAsXml(maxColsN, mode, showAllBool, keyVals);
   }
 }

/**
 * Prints a single line of the attributes report.
 * This report shows the detailed attributes too.
 * The caller of this method
 *
 * @see attrLst#printRow(boolean, JspWriter, String, int, boolean, String, String, String, String)
 */
 public boolean printAttrsRow(JspWriter out, String keyVal, int freeCols, boolean newLine, String bgCol, String fontFace, String fontSize, String textCol) throws Exception
 {
   if (attrs==null) return false;
   else return attrs.printRow(false, out, keyVal, freeCols, newLine, bgCol, fontFace, fontSize, textCol);
 }

/**
 * Prints the attributes report with these defaults: showAll=true
 *
 * @see calls printAttrsLabels and printAttrsRow
 */
 public void printAttrsReport(HttpServletRequest req, JspWriter out, String labBgCol, String bgCol, String fontFace, String fontSize, String textCol) throws Exception
 {
   this.printAttrsReport(req, out, labBgCol, bgCol, fontFace, fontSize, textCol);
 }

 /**
 * Prints the attributes report with these defaults: showAll=true
 *
 * @see calls printAttrsLabels and printAttrsRow
 */
 public void printAttrsReport(String firstCol, HttpServletRequest req, JspWriter out, String labBgCol, String bgCol, String fontFace, String fontSize, String textCol) throws Exception
 {
   this.printAttrsReport(true, firstCol, req, out, labBgCol, bgCol, fontFace, fontSize, textCol);
 }

 /**
 * Prints the attributes report with these defaults: showAll=true
 *
 * @see calls printAttrsLabels and printAttrsRow
 */
 public void printAttrsReport(boolean fetchBool, String firstCol, HttpServletRequest req, JspWriter out, String labBgCol, String bgCol, String fontFace, String fontSize, String textCol) throws Exception
 {
   if (attrs==null) return;
   if (fetchBool) fetchDependent(true, req);
   attrs.printLabels(true, out, (firstCol==null ? 0 : 1), labBgCol, "times", fontSize, textCol);
   attrs.printReportRows(firstCol, out, bgCol,  fontFace,  fontSize, textCol, true, null);
 }

/**
 * Prints the attributes report specifying an eventual attrs-report subset
 *
 * @param showAllBool if true the whole report is printed, whereas only a
 *   selection of it is printed (this subsetting depends on keyVals)
 * @param keyVals it specifies the key values whose report rows must be
 *   printed - if showAllBool is false and keyVals is null, the latter is then
 *   filled with the current L.O.V.
 * @see calls printAttrsLabels and printAttrsRow
 */
 public void printAttrsReport(HttpServletRequest req, JspWriter out, String labBgCol, String bgCol, String fontFace, String fontSize, String textCol, boolean showAllBool, Vector keyVals) throws Exception
 {
   this.printAttrsReport(true, req, out, labBgCol, bgCol, fontFace, fontSize, textCol, showAllBool, keyVals);
 }

 /**
 * Prints the attributes report specifying an eventual attrs-report subset
 *
 * @param showAllBool if true the whole report is printed, whereas only a
 *   selection of it is printed (this subsetting depends on keyVals)
 * @param keyVals it specifies the key values whose report rows must be
 *   printed - if showAllBool is false and keyVals is null, the latter is then
 *   filled with the current L.O.V.
 * @see calls printAttrsLabels and printAttrsRow
 */
 public void printAttrsReport(boolean fetchBool, HttpServletRequest req, JspWriter out, String labBgCol, String bgCol, String fontFace, String fontSize, String textCol, boolean showAllBool, Vector keyVals) throws Exception
 {
   if (attrs==null) return;
   if (fetchBool) fetchDependent(true, req);
   attrs.printLabels(true, out, 0, labBgCol, "times", fontSize, textCol);
   if ((!showAllBool)&&(keyVals==null))
   {
     keyVals=this.getVector();
   }
   attrs.printReportRows(out, bgCol,  fontFace,  fontSize, textCol, showAllBool, keyVals);
 }

/**
 * Prints the attributes labels row.
 *
 * @see attrLst#printLabels(boolean, JspWriter, int, String, String, String, String)
 */
 public void printAttrsLabels(JspWriter out, int freeCols, String bgCol, String fontFace, String fontSize, String textCol) throws Exception
 {
   if (attrs==null) return;
   else attrs.printLabels(false, out, freeCols, bgCol, fontFace, fontSize, textCol);
 }

/**
 * Given the name of a text file containing a Paremeters list, it returns
 * a list of strings properly formatted.
 * Note: these strings format is: <i>paramNo</i> <i>paramName</i>
 */
 public static Vector readTests(String fileName) throws Exception
 {
   flRdr f = new flRdr(fileName);
   Vector rows = new  Vector();
   Vector out = new Vector();
   f.getRows(rows);
   for (int i=0; i<rows.size(); i++)
   {
     String row = (String)rows.elementAt(i);
     StringTokenizer st = new StringTokenizer(row, "\n\r\t\f ", false);
     if (!st.hasMoreTokens()) continue;
     String fldType = st.nextToken().toUpperCase();
     if (!fldType.equals("S")) continue;
     out.addElement(st.nextToken());
     //--- desc field management (it can have inner blanks) ---//
     String str="";
     while (st.hasMoreTokens()) str = str.concat(st.nextToken());
     out.addElement(str);
   }
   return out;
 }

/**
 * Given the name of a text file containing a Bins list, it returns
 * a list of strings properly formatted.
 * Note: these strings format is: <i>binType</i> <i>binN</i> <i>binDesc</i>
 */
 public static Vector readBins(String fileName, String binType) throws Exception
 {
   flRdr f = new flRdr(fileName);
   Vector rows = new  Vector();
   Vector out = new Vector();
   f.getRows(rows);
   for (int i=0; i<rows.size(); i++)
   {
     String row = (String)rows.elementAt(i);
     StringTokenizer st = new StringTokenizer(row, "\n\r\t\f ", false);
     if (!st.hasMoreTokens()) continue;
     String fldType = st.nextToken().toUpperCase();
     if (binType.equals("HW"))
        {if (!fldType.equals("HB")) continue;}
     else if (!fldType.equals("SB")) continue;
     out.addElement(st.nextToken());
     out.addElement(st.nextToken());
     //--- desc field management (it can have inner blanks) ---//
     String str="";
     while (st.hasMoreTokens()) str = str.concat(st.nextToken()+" ");
     out.addElement(str);
   }
   return out;
 }

/**
 * Given a text file containing lines of such a type
 * "<i>field_1</i>, ..., <i>field_n</i>" returns an HTML table body
 * having a coloumn for each field.
 * <b>Remark:</b> fields could also feature blank spaces - it's not a problem
 */
 public static void readReport(JspWriter out, String fileName) throws Exception
 {
   flRdr f = new flRdr(fileName);
   Vector rows = new  Vector();
   f.getRows(rows);
   for (int i=0; i<rows.size(); i++)
   {
     String row = (String)rows.elementAt(i);
     if (row.trim().equals("")) continue;
     StringTokenizer st = new StringTokenizer(row, "\n,", false);
     out.println("<tr>");
     do
     {
       out.print("<td>");
       String str = st.nextToken().trim();
       out.print(str);
       out.println("</td>");
     }
     while (st.hasMoreTokens());
     out.println("</tr>");
   }
 }


 //---------------------------//
 //--- METHODS FOR NEW GUI ---//
 //---------------------------//

/**
 * Returns this L.O.V. <i>index</i>. This index is extracted from the <i>selBut</i>
 * name, which is of such a type: index<i>n</i>
 * (with <i>n</i> being the <i>index</i>).
 * <br>The index is a L.O.V. identifier which is unique within a give <i>dataSel</i>
 * object. Indexes and <i>selBut</i> names are obviously assigned by the
 * <i>dataSel</i> object itself.
 */
  public int getIndex()
  {
    if (selButName.length()<5) return -1;
    try  {return (Integer.valueOf(selButName.substring(5))).intValue();}
    catch(NumberFormatException e) {return -1;}
  }


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
       v.clear();
       v.addElement(blankField);
     }
     return;
   }

   if (req.getParameterValues("nVals")==null) return;
   //means I've asked for this field fetch
   String  nStr     = (req.getParameterValues("nVals"))[0];
   Integer nVals    = Integer.valueOf(nStr);

   v.clear();
   this.clearAttrs();

   if (isMulti)
   //checkboxes management
   {
     for (int i=0; i < nVals.intValue(); i++)
     {
       String iStr  = (new Integer(i)).toString();
       String check =  new String("chk"+iStr);
       String val   =  new String("val"+iStr);
       String value = "";
       boolean checkBool = (req.getParameterValues(check)!=null);
       if (checkBool)
       {
         value = ((req.getParameterValues(val))[0]).trim();
         v.addElement(value);
         if (attrs!=null) attrs.setValsFromReq(val, value, req);
       }
     }
   }
   else
   // only one selection is allowed -> radio button management
   {
     String opt[];
     if ((opt=req.getParameterValues("opt"))!=null)
     {
        String val = opt[0];
        String value = check(((req.getParameterValues(val))[0]).trim());
        v.addElement(value);
        if (attrs!=null) attrs.setValsFromReq(val, value, req);
     }
   }

   if (v.size()==0) v.addElement(blankField);
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

   for (int i=0; i < v.size(); i++)
   {
     String val = (String)v.elementAt(i);
     if (!val.equals(blankField))
     {
       out.print(val);
       out.println("<BR>");
     }
   }
 }

/**
 * Given an <i>stdf</i> name and its respective <i>lot</i> code, returns the
 * wafer number.
 * If it fails to extract the wafer number returns -1.
 */
 public static Integer getWafFromStdf(String stdf, String lot)
 {
  //returns null if it can't extract the wafer number
  Integer waf=null;
  StringTokenizer st = new StringTokenizer(stdf, "_", false);
  try
  {
    while (st.hasMoreTokens())
    {
      String tok = st.nextToken().toUpperCase();
      if (lot.toUpperCase().equals(tok))
          waf = Integer.valueOf(st.nextToken());
    }
  }
  catch(Exception e) {waf=new Integer(-1);}
  return (waf!=null ? waf : new Integer(-1));
 }

 public String toString()
 {
   return new String(this.name+"\n"+this.fieldName+"\n"+this.tableName+"\n"+this.selButName+"\n"+this.index);
 }

 public String getOperator()
 {
   if ((v.size()==0)||(isBlank())) return null;
   else
   {
     if (v.size()==1) return new String("=");
     else return new String("IN");
   }  
 }

/**
 * Gets an Element with primary attributes for the given
 * master-field value
 */
 public void getAttrsAsXML(Element targetEl, String value)
 {
   if (attrs==null) return;
   else
   {
     attrs.getAttrsAsXML(targetEl, value, false);
   }
 }

 public void setIsExpr(boolean b) {isExpr=b;}

 public void setFactTableName(String s) {this.factTableName=s;}
 public String getFactTableName() {return this.factTableName;}
}//class
