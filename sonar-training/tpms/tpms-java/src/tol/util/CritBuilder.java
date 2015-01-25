package tol.util;

import tol.*;
import java.util.*;
import org.w3c.dom.*;
import java.io.*;

public class CritBuilder
{
  protected Element dbmodel;
  protected Element images;
  protected String model_id;
  protected String plant;
  protected String analisys_id;
  protected String _initDir;
  protected String dskey;

  public CritBuilder() {}

  public CritBuilder(String _initDir, String analisys_id, String model, String plant) throws Exception
  {
    String dbModelInitDir=_initDir.concat("/db_int");
    String dbModelXml=dbModelInitDir.concat("/dbmodel.xml");
    String imagesXml=dbModelInitDir.concat("/").concat(model).concat("/images.xml");

    dbmodel=xmlRdr.getRoot(dbModelXml,false);
    images=xmlRdr.getRoot(imagesXml,false);
    model_id=model;
    this.analisys_id=analisys_id;
    this.plant=plant;
    this._initDir=_initDir;
  }

  public Element getImagesDefRoot() {return images;}

  public Element getDbModelRoot() {return dbmodel;}

  public String getAnalysisID() {return analisys_id;};

  public String getModelID() {return model_id;};

  public String getPlant() {return plant;}

  public String getInitDir() {return _initDir;}

  public void getCrit(String app_id, dataSel data, String dir, String critFileName, boolean detailedCritBool, Properties fldsLst) throws Exception
  {
    //Scrivo il criterio di selezione.....------------------------------------------------------
    getCrit(data, dir, critFileName, detailedCritBool, null);
  }

protected void writeDbAccessInfo(Element root) throws Exception
{
    String datatype = " ";
    String family    = " ";

    //--- DBMODEL PARAMETERS FETCH ---//
    NodeList rows=dbmodel.getChildNodes();
    for (int i=0; i<rows.getLength(); i++)
    {
      Node n=rows.item(i);
      if (!(n instanceof Element)) continue;
      String cur_model=xmlRdr.getVal((Element)n,"MODEL_ID");
      if (cur_model.equals(model_id))
      {
        datatype  = xmlRdr.getVal((Element)n,"DATATYPE");
        dskey     = xmlRdr.getVal((Element)n,"DSKEY_ID");
        family    = xmlRdr.getVal((Element)n,"DBFAMILY");
      }
    }

    //--- SEL CRIT ROOT ELEMENT UPDATE ---//
    root.setAttribute("DATATYPE",  datatype);
    root.setAttribute("FAMILY",    family);
    root.setAttribute("PLANT",     plant);
    root.setAttribute("DSKEY",     dskey);
    root.setAttribute("MODEL_ID",  (model_id.length()<=4 ? model_id : model_id.substring(0,4)));

    dbRdrMgr dbmgr=new dbRdrMgr();
    dbRdr dbr=dbmgr.getDbRdr(this._initDir, this.plant, this.model_id, "0");
    root.setAttribute("DBS_TYPE",dbr.getCfgProp("DBS_TYPE"));
    root.setAttribute("DBS_ATTR",dbr.getCfgProp("DBS_ATTR"));
    root.setAttribute("NETNODE",dbr.getCfgProp("NETNODE"));
    root.setAttribute("DB_NAME",dbr.getCfgProp("DB_NAME"));
    root.setAttribute("USERNAME",dbr.getCfgProp("USERNAME"));
    root.setAttribute("PASSWORD",dbr.getCfgProp("PASSWORD"));
    root.setAttribute("ALIAS",dbr.getCfgProp("ALIAS"));
    root.setAttribute("STDFPATH",dbr.getCfgProp("STDFPATH"));
    root.setAttribute("STDFHOME",dbr.getCfgProp("STDFHOME"));
    root.setAttribute("STDFHOST",dbr.getCfgProp("STDFHOST"));
    root.setAttribute("STDFREAD",dbr.getCfgProp("STDFREAD"));
    root.setAttribute("READ_CMD",dbr.getCfgProp("READ_CMD"));
    root.setAttribute("READER",dbr.getCfgProp("READER"));
}


/**
 * create an XML file named as 'fileName', into the 'dir' directory, containing
 * the cryterion defined by the current status of the
 * data selection object 'dsel'.
 */
  public void getCrit(dataSel data, String dir, String fileName, boolean detailedCritBool, Properties fldsLst) throws Exception
  {
    //----- definizione parametri generali SELCRIT ---------------------------------
    String prefix      = "I_";
    String att_prefix  = "name";
    String selcrit     = "SELCRIT";
    String block_l     = "BLOCK_L";
    String condition_l = "CONDITION_L";
    String vlist       = "VLIST";

    //----- definizione parametri COSTANTI CABLATI SELCRIT -------------------------
    String cross_flg = "N";

    //----- definizione parametri COSTANTI CABLATI BLOCK ---------------------------
    String nblock = "1";
    String cross  = "N";
    String status = "A";

    //----- definizione parametri COSTANTI CABLATI CONDITION -----------------------
    String ncondition = "";
    String ctype      = "S";
    String def        = "N";//(..DEFAULT..)
    String origin     = "DB";
    String timing     = "I";
    String oper       = "";
    String dtable     = "";
    String dfield     = "";
    String dftype     = "";
    String pwyid      = "";
    int j;

    Document doc = xmlRdr.getNewDoc(prefix.concat(selcrit));
    Element root = doc.getDocumentElement();
    this.writeDbAccessInfo(root);
    root.setAttribute("CROSS_FLG", cross_flg);

    // ----- INIZIALIZZAZIONE BLOCCO -----------------------------------------------
    Element blocks = xmlRdr.addEl(root, prefix.concat(block_l));
    Element block = xmlRdr.addEl(blocks, prefix.concat(nblock));
    block.setAttribute("CROSS",    cross);
    block.setAttribute("STATUS",   status);
    block.setAttribute("PLANT",    plant);
    block.setAttribute("MODEL_ID", model_id);
    block.setAttribute("DSKEY",    dskey);

    // ----- INIZIALIZZAZIONE CONDIZIONI -------------------------------------------
    Element conditions = xmlRdr.addEl(block, prefix.concat(condition_l));
    String index;
    slctLst lista;
    String table;
    String field;
    String label;
    String name;
    String fullname;
    String val="";
    String val1="";
    String val2="";

    for (int i=0, i2=0; i<data.size(); i++)
    {
      lista=data.get(i);
      if (lista.getWhrCond(lista.getDefaultWhrTable())!= null)
      {
        i2++;
        ncondition=new Integer(i2).toString();
        table=lista.getDefaultWhrTable();
        field=lista.getDefaultWhrField();
        label=lista.getLabel();
        name=lista.getName();
        fullname=table.concat(".").concat(field);
        oper = lista.getOperator();
        dftype=getType(lista.getType());

        // ----- INIZIALIZZAZIONE CONDIZIONE -------------------------------------
        Element condition = xmlRdr.addEl(conditions, prefix.concat(ncondition));
        condition.setAttribute("CTYPE", ctype);
        condition.setAttribute("OPERATOR", oper);
        condition.setAttribute("DTABLE", table);
        condition.setAttribute("DFIELD", field);
        condition.setAttribute("PWYID", fullname);
        condition.setAttribute("DFTYPE",  dftype);
        condition.setAttribute("DEFAULT", def);
        condition.setAttribute("ORIGIN", origin);
        condition.setAttribute("TIMING", timing);
        condition.setAttribute("LABEL", label);
        condition.setAttribute("NAME", label);

        Element vlist_tag = xmlRdr.addEl(condition, prefix.concat(vlist));
        //------------------------------------------------------------------------"IN"----------
        if (oper.equals("IN"))
        {
          Vector values=lista.getVector();
          for (j=0; j<values.size(); j++)
          {
            index=new Integer(j+1).toString();
            val=(String)values.elementAt(j);
            vlist_tag.setAttribute(att_prefix.concat(index), formatValue(val, lista.getType()));
          }
        }
        //------------------------------------------------------------------------"BETWEEN"-----
        else if (oper.equals("BETWEEN"))
        {
          val1=lista.getVal();
          val2=((rngTxtLst)lista).getVal2();
          vlist_tag.setAttribute(att_prefix.concat("1"), formatValue(val1, lista.getType()));
          vlist_tag.setAttribute(att_prefix.concat("2"), formatValue(val2, lista.getType(), false));
        }
        //------------------------------------------------------------------------"<="----------
        else if (oper.equals("<="))
        {
          val2=((rngTxtLst)lista).getVal2();
          vlist_tag.setAttribute(att_prefix.concat("1"), formatValue(val2, lista.getType(), false));
        }
        //--------------------------------------------------------------------------------------
        else
        {
          val=lista.getVal();
          vlist_tag.setAttribute(att_prefix.concat("1"), formatValue(val, lista.getType()));
        }
      }
    }
    //----- STAMPO SU FILE IL CRITERIO DI SELEZINE IN FORMATO XML..... -------------------------
    FileOutputStream output = new FileOutputStream(dir.concat("/"+fileName), false);
    xmlRdr.echo(doc, new PrintStream(output));
    output.close();
  }


  private String applyTO_DATE(String f, boolean lowBndBool)
 //lowBndBool=true means that I request a lower bound
 //(otherwise i've requested an upper bound
  {
    String lowBndStr="0:00:00";
    String upBndStr ="23:59:59";
    String timeStr  =(lowBndBool ? lowBndStr : upBndStr);
    String s="";
    StringTokenizer st=new StringTokenizer(f,dateRd._separators,false);
    s = s.concat(st.nextToken());
    s = s.concat(dateRd.getMonthLabel(st.nextToken()).toUpperCase());
    s = s.concat(st.nextToken()+"-");
    s = s.concat(timeStr);
    return(s);
  }

  private String formatValue(String value, char type)
  {
    boolean b=true;
    String s=formatValue(value, type, b);
    return(s);
  }


  private String formatValue(String value, char type, boolean b)
  {
    String s=value;
    if (type==slctLst._DATE)
    {
      s=applyTO_DATE(value, b);
    }
    else if (type==slctLst._CHAR)
    {
      try
      {
        Double.valueOf(value);
      }
      catch(NumberFormatException e)
      {
        return(s);
      }
      String str="'";
      s=str.concat(value).concat(str);
    }
    return(s);
  }

  private String getType(char type)
  {
    String type2="C";
    if (type==slctLst._DATE)
      type2="D";
    else if (type==slctLst._NUM)
      type2="N";
    return(type2);
  }

  public static void main(String[] args) throws Exception
  {
    System.out.println(new String("TDB00").substring(0,4));
  }
}


