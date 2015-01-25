package tol;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import org.w3c.dom.*;

/**
 * Manages data selection. It is basicly a collection of {@link slctLst} instances
 * and exposes methods to:
 * <ul>
 * <li>configure data selection by means of an XML configuration file</li>
 * <li>easily access <i>slctLst</i> objects and their respective <i>attrLst</i> objects
 *     by index or name</li>
 * </lu>
 * <br>
 * <h3><a name="composedNames">Composed Names<a/></h3>
 * Composed names are used to address <i>attributes</i> (ie: <i>attrLst</i>
 * instances) owned by <i>slctLst</i> instances.<br>
 * A composed names of type <i>list_name</i>/<i>attribute_name</i> references
 * the attribute named as <i>attribute_name</i> belonging to the <i>slctLst</i>
 * instance whose name is <i>list_name</i>.
 */
public class dataSel
{
 dbRdr dbr;
 String _xmlFname="datasel.xml";
 static final String _fieldSep="/";
 LogWriter log=null;
 boolean ignoreCaseBool;

 public void setLogWriter(LogWriter log) {this.log=log;}
 public void debug(Object msg) {if (log!=null) log.p(msg);} public void debug(String msg) {if (log!=null) log.p(msg);}

/**
 * List of the <i>slctLst</i> instances
 */
 Vector v; //select lists vector
 slctLst keyLst=null; //references the key list within the vector v
 int nOflds=0;

 public dataSel() {}

/**
 * Initializes the list of <i>slctLst</i> instances by means of the
 * XML file (whose relative name is defined by the static member
 * <i>_xmlFname</i>) located in the <i>fileName</i> directory.
 *
 * @param dbr is the database reader which the <i>slctLst</i> instances will be
 *        initialized with
 */
  public dataSel(String fileDir, dbRdr dbr, LogWriter log) throws Exception {this.ignoreCaseBool=false; init(fileDir, dbr, log, _xmlFname);}

  public dataSel(String fileDir, dbRdr dbr, LogWriter log, String xmlFname) throws Exception {this.ignoreCaseBool=false; init(fileDir, dbr, log, xmlFname);}

  public void init(String fileName, dbRdr dbr, LogWriter log, String xmlFname) throws Exception
  {
    this.dbr = dbr;
    this.log = log;
    v=new Vector();
    Element root=xmlRdr.getRoot(fileName.concat("/"+xmlFname),false);
    String keyLstName=root.getAttribute("keyField");
    NodeList l=root.getChildNodes();
    int lstIndex=0; nOflds=0;
    for (int i=0; i<l.getLength(); i++)
    {
      slctLst lst=null;
      if (!(l.item(i) instanceof Element)) continue;
      Element el=(Element)l.item(i);
      String name=el.getAttribute("name");
      boolean hidden=(el.getAttribute("hidden").equals("Y") ? true : false);
      boolean auto=(el.getAttribute("auto").equals("Y") ? true : false);
      boolean isMulti=(el.getAttribute("isMulti").equals("Y") ? true : false);
      boolean isRange=(el.getAttribute("isRange").equals("Y") ? true : false);
      boolean isMand =(el.getAttribute("isMand").equals("Y")  ? true : false);
      String initVal=el.getAttribute("initVal");
      String initVal2=el.getAttribute("initVal2");
      String label=xmlRdr.getVal(el,"label");
      String dataType=el.getAttribute("datatype");
      String fieldName=null;
      String tableName=xmlRdr.getVal(el,"tableName");
      String factTableName=(xmlRdr.getChild(el,"factTableName")!=null ? xmlRdr.getVal(el,"factTableName") : null);
      boolean distinctBool=(el.getAttribute("distinct").equals("N") ? false : true);
      String butName=new String("index"+lstIndex);
      String parName=new String("param"+lstIndex);
      String maxNofSelKeys = el.getAttribute("maxNofSelKeys");

      //debug("EL-TYPE: "+el.getNodeName()); //debug
      //FIELD
      if (el.getNodeName().equals("field"))
      {
        boolean isExprBool=(xmlRdr.getDirChild(el,"expr")!=null);
        fieldName=(!isExprBool ? xmlRdr.getVal(el,"fieldName") : xmlRdr.getVal(el,"expr"));
        if (dataType.charAt(0)!=slctLst._DATE)
        {
          if (!isRange)
          {
            lst=new slctLst(name,
                          (label!=null ? label : name),
                          dataType.charAt(0),
                          "",
                          dbr,
                          tableName,
                          fieldName,
                          "",
                          butName,
                          3,
                          isMulti
                         );
          }
          else
          {
            lst=new rngTxtLst(name,
                              (label!=null ? label : name),
                              dataType.charAt(0),
                              "", "",
                              dbr,
                              tableName,
                              fieldName,
                              "", "",
                              butName,
                              3,
                              3
                             );
          }
        }
        else //Mandatory Date Range Type
        {
          lst=new dRngTxtLst(name,
                             (label!=null ? label : name),
                             "",
                             "",
                             dbr,
                             tableName,
                             fieldName,
                             butName,
                             isMand //is set the list will be always active
                            );
        }
        lst.setIsExpr(isExprBool);
        nOflds++;
      }//FIELD ELEMENT CASE
      //FIELD-FROM FILE
      if (el.getNodeName().equals("fieldFromFile"))
      {
        fieldName=xmlRdr.getVal(el,"fieldName");
        String baseDir=el.getAttribute("baseDir");
        lst=new slctLstFromFile(name,
                                (label!=null ? label : name),
                                dataType.charAt(0),
                                parName,
                                dbr,
                                tableName,
                                fieldName,
                                "",
                                butName,
                                3,
                                isMulti,
                                dbr.getCfgProp("CONN_STR")
                       );
        nOflds++;
      }//FIELD FROM FILE CASE
      //SECTION
      if ((el.getNodeName().equals("section"))||
          (el.getNodeName().equals("dbimage")))
      {
        lst=new repSect(
                        name,
                        (label!=null ? label : name),
                        dbr,
                        tableName,
                        butName
                       );
        if (!el.getAttribute("outName").equals(""))
        {
            ((repSect)lst).setOutputTableName(el.getAttribute("outName"));
        }
      }//SECTION CASE
      //CHART
      if (el.getNodeName().equals("chart"))
      {
        lst=new chartSect(
                           name,
                           (label!=null ? label : name),
                           dbr,
                           tableName,
                           butName,
                           xmlRdr.getChild(el,"STCHART")
                       );
        //--- BOUND FIELDS MANAGEMENT ---//
        Element bndFldEl=xmlRdr.getChild(el,"boundFields");
        if (bndFldEl!=null)
        {
           StringTokenizer st = new StringTokenizer(bndFldEl.getAttribute("names")," ",false);
           while (st.hasMoreTokens())
           {
             String lstName = st.nextToken().trim();
             get(lstName).addChart((chartSect)lst);
           }
        }
      }//CHART CASE
      lst.setLogWriter(log);
      if (factTableName!=null) lst.setFactTableName(factTableName);
      lst.setDistinctBool(distinctBool);
      if (hidden) lst.setAsHidden(true);
      if (auto) lst.setAuto(true);
      if (isMand) lst.setMand();
      if (!initVal.equals(""))  lst.setInitVal(initVal);
      if ((!initVal2.equals(""))&&(lst instanceof rngTxtLst)) ((rngTxtLst)lst).setInitVal2(initVal2);
      if (el.getAttribute("editable").toUpperCase().equals("Y")) lst.setEditable();
      if (!maxNofSelKeys.equals(""))
      {
        lst.setMaxNofSelKeysAllowed(Integer.valueOf(maxNofSelKeys).intValue());
      }
      //--- ALTERNATIVE WHERE CONDITIONS ---//
      lst.setDefaultWhrTable(lst.getTableName());
      NodeList whrCndLsts=el.getElementsByTagName("whrCond");
      for (int k=0; k<whrCndLsts.getLength(); k++)
      {
        //debug("whr-cond>");//debug
        Element whrCndLstEl=(Element)whrCndLsts.item(k);
        lst.setAlternativeWhrCond(whrCndLstEl.getAttribute("tableName"), xmlRdr.getVal(whrCndLstEl));
        if (whrCndLstEl.getAttribute("default").equals("Y"))
        {
            lst.setDefaultWhrTable(whrCndLstEl.getAttribute("tableName"));
        }
      }

      //--- GROUP BY LIST ---//
      NodeList grpLsts=el.getElementsByTagName("grpByList");
      for (int k=0; k<grpLsts.getLength(); k++)
      {
        //debug("group-by>");//debug
        Element grpLstEl=(Element)grpLsts.item(k);
        lst.setGrpByLst(xmlRdr.getVal(grpLstEl), (grpLstEl.getAttribute("level").equals("2") ? 2 : 1));
      }

      //--- ORDER LIST ---//
      NodeList ordLsts=el.getElementsByTagName("orderList");
      for (int k=0; k<ordLsts.getLength(); k++)
      {
        //debug("order-by");//debug
        Element ordLstEl=(Element)ordLsts.item(k);
        lst.setOrderLst(xmlRdr.getVal(ordLstEl), (ordLstEl.getAttribute("level").equals("2") ? 2 : 1));
      }
      v.addElement(lst);
      lstIndex++;

      //--- MASTER FIELDS MANAGEMENT ---//
      //debug("master-fields>");//debug
      Element mstrFlds=xmlRdr.getChild(el,"masterFields");
      if (mstrFlds!=null)
      {
         StringTokenizer st = new StringTokenizer(mstrFlds.getAttribute("names")," ",false);
         while (st.hasMoreTokens())
         {
           String lstName = st.nextToken().trim();
           lst.addDep(get(lstName));
         }
      }

      //--- ATTRIBUTES FIELDS MANAGEMENT ---//
      //debug("attrs>");//debug
      NodeList attrs=el.getElementsByTagName("attr");
      for (int j=0; j<attrs.getLength(); j++)
      {
        Element attr=(Element)attrs.item(j);
        String attrName=xmlRdr.getVal(attr,"name");
        String attrLabel=xmlRdr.getVal(attr,"label");
        String attrDataType=attr.getAttribute("datatype");
        String attrFieldName=xmlRdr.getVal(attr,"fieldName");
        String attrTableName=xmlRdr.getVal(attr,"tableName");
        if (attrTableName==null) attrTableName=tableName;
        String attrFormat=(attr.getAttribute("format").equals("") ? null : attr.getAttribute("format"));
        String attrExpr=xmlRdr.getVal(attr,"expr");      //eventually null
        Element attrFunz=xmlRdr.getChild(attr,"funz"); //eventually null
        String attrSql="";
        String fldName="substr(TP_PLANT.TESTER_INFO,1,instr(TP_PLANT.TESTER_INFO,' ',1,2))";
        if ((attrExpr==null)&&(attrFunz==null))
        {
            // add code for remove the tablename (attrTableName+"."+attrFieldName)
        	if (attrFieldName.equals(fldName)){
        	attrSql=((attrDataType.charAt(0)==slctLst._DATE) ? dbr.applyTO_CHAR(attrTableName+"."+attrFieldName,false,attrFormat) : attrFieldName);
        	}
        	else{
          attrSql=((attrDataType.charAt(0)==slctLst._DATE) ? dbr.applyTO_CHAR(attrTableName+"."+attrFieldName,false,attrFormat) : attrTableName+"."+attrFieldName);
        }
        }	
        if (attrExpr!=null) attrSql=attrExpr;
        if (attrFunz!=null) attrSql=null;
        String correctAttrName=(ignoreCaseBool ? attrName : attrName.toLowerCase()).replace(' ','_');
        correctAttrName=correctAttrName.replace('(','_').replace(')','_');
        correctAttrName=correctAttrName.replace('[','_').replace(']','_');
        correctAttrName=correctAttrName.replace('%','_');

        if (attr.getAttribute("level").equals("1"))
           lst.addAttr  (correctAttrName,   (attrLabel!=null ? attrLabel : attrName), attrDataType.charAt(0), attrFieldName, attrTableName, attrSql, attrFormat, attrFunz);
        else
           lst.addDetail(correctAttrName, (attrLabel!=null ? attrLabel : attrName), attrDataType.charAt(0), attrFieldName, attrTableName, attrSql, attrFormat, attrFunz);
      }
    }
    keyLst=get(keyLstName);
    if (keyLst!=null) keyLst.setKey();
  }

/**
 * Returns the number of the <i>slctLst</i> instances contained by this object.
 */
  public int size() {return v.size();}

/**
 * Returns the number of the <i>field</i> elements contained by this object.
 */
  public int getNoFlds() {return nOflds;}

//-------------------//
//----- GETTERS -----//
//-------------------//

/**
 * Returns the <i>slctLst</i> instance whose name is <i>name</i>
 *
 * @return null - if the referenced L.O.V. doesn't exist
 */
  public slctLst get(String name)
  {
    for (int i=0; i<v.size(); i++)
    {
      slctLst tLst=(slctLst)v.elementAt(i);
      if (tLst.getName().equals(name))
      {
        return tLst;
      }
    }
    return null;
  }

/**
 * Returns the <i>slctLst</i> instance whose position within the list is <i>i</i>.
 */
  public slctLst get(int i) {return (slctLst)v.elementAt(i);}

/**
 * It's the same as {@link #get(int)}, but the position <i>i</i> of the
 * required <i>slctLst</i> is taken from the 'index' parameter of an
 * HTTP request.
 */
  public slctLst get(HttpServletRequest req)
  {
    int i = (Integer.valueOf((req.getParameterValues("index"))[0])).intValue();
    return (slctLst)v.elementAt(i);
  }

/**
 * Gets the i-th L.O.V. label.
 */
 public String getLabel(int i) {return this.get(i).getLabel();}

/**
 * Gets the label of that L.O.V. whose name is <i>name</i>.
 */
 public String getLabel(String name) {return this.get(name).getLabel();}

/**
 * Returns the value of the <i>slctLst</i> instance which is the
 * data selection key
 */
  public slctLst getKey()
  {
    return keyLst;
  }

/**
 * Returns the value of the <i>slctLst</i> instance whose position within
 * the list is <i>i</i>.
 *
 * @see slctLst#getVal()
 */
  public String getVal(int i)
  {
    return get(i).getVal();
  }

/**
 * Returns the value of the <i>slctLst</i> instance whose name
 * is <i>name</i>. If <i>name</i> is <a href="#composedNames">composed</a> this
 * method returns the value of the appropriate attribute of the addressed
 * <i>slctLst</i> instance.
 *
 * @see slctLst#getVal()
 * @see slctLst#getAttrVal(String)
 */
  public String getVal(String name)
  {
    boolean isComposedBool;
    StringBuffer lstNameBuff=new StringBuffer();
    StringBuffer attrNameBuff=new StringBuffer();
    if (isComposedBool=isComposed(name, lstNameBuff, attrNameBuff))
    {
      name=lstNameBuff.toString();
    }
    slctLst lst=get(name);
    if (isComposedBool) return(lst.getAttrVal(attrNameBuff.toString()));
    else return lst.getVal();
  }

/**
 * Returns a string in the form label=val for the specific L.O.V. referenced
 * by the 'name' parameter.
 * If attrName==null, the L.O.V. value is considered, otherwise
 * the value of that attribute referenced by attrName is considered.
 * if the referenced L.O.V. doesn't exist, null is returned.
 */
  public String getBindString(String name, String label, boolean labelBool)
  {
    boolean isComposedBool;
    StringBuffer lstNameBuff=new StringBuffer();
    StringBuffer attrNameBuff=new StringBuffer();
    if (isComposedBool=isComposed(name, lstNameBuff, attrNameBuff))
    {
      name=lstNameBuff.toString();
    }
    slctLst lst=get(name);
    if (lst==null) return null;
    else
    {
      String attrName=null;
      if (isComposedBool) attrName=attrNameBuff.toString();
      return lst.getBindString(attrName, label, labelBool);
    }
  }

  public String getAllBindString()
  {
    String s="";
    for (int i=0; i<nOflds; i++)
    {
      slctLst l=(slctLst)v.elementAt(i);
      String bind=l.getBindString(null,l.getLabel(), true);
      s=s.concat(bind.equals("") ? "" : bind+" ");
    }
    return s;
  }

  public static boolean isComposed(String fieldName, StringBuffer lstName, StringBuffer attrName)
  {
    StringTokenizer st = new StringTokenizer(fieldName, _fieldSep, false);
    String s1=st.nextToken();
    String s2;
    if (st.hasMoreTokens())
    {
      s2=st.nextToken();
      lstName.append(s1);
      attrName.append(s2);
      return true;
    }
    else return false;
  }

 /**
  * Returns the list of <i>slctLst</i> instances which this object consists of.
  */
  public Vector getVector() {return v;}

 /**
  * Checks if the <i>slctLst</i> instances have been properly set as a result
  * of the user data selection process.
  * It roughly means that this method checks each mandatory <i>slctLst</i>
  * instance: if any of theese contains no values an exception is thrown.<br>
  * <b>Remark:</b> if the <u>key</u> <i>slctLst</i> instance contains no values,
  * values for it are fetched from the data base according to the conditions
  * defined by its <u>master fields</u>
  */
  public void check(HttpServletRequest request) throws Exception
  {
    for (int i=0; i<v.size(); i++)
    {
      slctLst l = get(i);
      if (l==keyLst) continue;
      else if ((l.isBlank())&&(l.isMand()))
           throw new NumberFormatException("'"+l.getLabel()+"' IS A MANDATORY FIELD");
    }
    //If the key list hasn't been explicitly specified
    //I fetch values for It (according to the master fields values)
    if ((keyLst!=null)&&(keyLst.isBlank()))
    {
      keyLst.fetchDependent(request);
    }
    //Now I must check that the key list contains at least one value
    if ((keyLst!=null)&&(keyLst.isBlank()))
    {
      throw new NumberFormatException("NO VALUE SELECTED FOR '"+ keyLst.getLabel() +"' FIELD");
    }
    //Now I must check that the number of fetched keys doesn't exceed
    //the specified threshold (if defined)
    if ((keyLst!=null)&&(!keyLst.checkNofSelKeys()))
    {
      String nVals=new Integer(keyLst.getMaxNofSelKeysAllowed()).toString();
      throw new NumberFormatException("NO MORE THAN "+nVals+" VALUES CAN BE SELECTED FOR '"+ keyLst.getLabel() +"' FIELD");
    }
    //debug(keyLst.getVector());
  }


/**
 * @return the index of the last set up L.O.V.
 */
  public int setCritFromXML(String fileName) throws Exception
  {
    int lastInd=0;
    selCrit crit=new selCrit(fileName);
    for (int i=0; i<nOflds; i++)
    {
      slctLst lst=get(i);
      lst.setBlank();
      Element fldEl=crit.get(lst.getName());
      if (fldEl!=null)
      {
        lastInd=i;
        //debug(">"+lst.getName()); //debug
        lst.setRows(fldEl);
      }
    }
    return lastInd;
  }


/**
 * Gets the current state of the key L.O.V.
 * in a DOM object form. This dom object has got
 * one element for each value selected which in turn has got
 * one vhild element for each attribute
 */
public Document getCritAsXmlRecordset(boolean mode, Properties fldsLst) throws Exception
{
 slctLst keyLst=this.getKey();
 Document doc=xmlRdr.getNewDoc("SELCRIT");
 keyLst.getAttrLst().getAttrsAsXMLRecordset(doc.getDocumentElement(), mode, fldsLst);
 return doc;
}


/**
 * Gets the current state of the data selection instance
 * in a DOM object form.
 *
 * @param attrsMode if true includes the attributes too
 * @param detailMode is considered only if attrsMode=true - this case, if set
 *    to true makes detailed attributes to be included into the document too.
 */
  public Document getCritAsXML(boolean attrsMode) throws Exception
  {
    Document doc=xmlRdr.getNewDoc("SELCRIT");
    Element  conditionsEl=xmlRdr.addEl(doc.getDocumentElement(), "CONDITIONS");
    for (int i=0; i<nOflds; i++)
    {
      slctLst list=this.get(i);
      if (list.getWhrCond(list.getTableName())!=null)
      {
        Element condEl = xmlRdr.addEl(conditionsEl, "CONDITION");
        Element fldEl  = xmlRdr.addEl(condEl, "FIELD");
	      fldEl.setAttribute("name",     list.getName());
	      fldEl.setAttribute("label",    list.getLabel());
	      fldEl.setAttribute("fieldName",list.getFieldName());
	      fldEl.setAttribute("tableName",list.getTableName());
	      fldEl.setAttribute("datatype", new Character(list.getType()).toString());
        condEl.setAttribute("operator",list.getOperator());
        if ((list.getOperator().equals("IN"))||(list.getOperator().equals("=")))
        {
          Vector values=list.getVector();
          for (int j=0; j<values.size(); j++)
          {
            Element itemEl=xmlRdr.addEl(condEl,"ITEM");
            Element valEl=xmlRdr.addEl(itemEl, "VAL", (String)values.elementAt(j));
            if (attrsMode) list.getAttrsAsXML(valEl, (String)values.elementAt(j));
          }
        }
        else if (list.getOperator().equals("BETWEEN"))
        {
          Element itemEl1=xmlRdr.addEl(condEl,"ITEM");
	        xmlRdr.addEl(itemEl1, "VAL", list.getVal());
          Element itemEl2=xmlRdr.addEl(condEl,"ITEM");
	        xmlRdr.addEl(itemEl2, "VAL", ((rngTxtLst)list).getVal2());
	      }
        else if (list.getOperator().equals("<="))
        {
          Element itemEl=xmlRdr.addEl(condEl,"ITEM");
	        xmlRdr.addEl(itemEl, "VAL", ((rngTxtLst)list).getVal2());
        }
        else //operator='>='
        {
          Element itemEl=xmlRdr.addEl(condEl,"ITEM");
	        xmlRdr.addEl(itemEl, "VAL", list.getVal());
        }
      }
    }
    return doc;
  }

  public String getAllocDataCells()
  {
    String s="";
    for (int i=0; i<nOflds; i++)
    {
      slctLst list=this.get(i);
      s=s.concat(list.getName()+" "+new Integer(list.getVector().size()).toString()+"\n");
      if (list.hasAttrs())
      {
         attrLst attrs=list.getAttrLst();
         for (int j=0; j<attrs.size(); j++)
         {
           s=s.concat("  "+attrs.getName(j)+" "+new Integer(attrs.getVals(j).size()).toString());
         }
      }
    }
    return s;
  }

  public void setIgnoreCaseBool(boolean b) {this.ignoreCaseBool=b;}

  public dbRdr getDbRdr() {return dbr;}
  public static void main(String[] args) throws Exception
  {
   dataSel ds = new dataSel("c:/Temp", null, null);
  }
}