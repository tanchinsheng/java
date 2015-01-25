package tol;

import java.util.Vector;
import org.w3c.dom.*;

public class selCrit
{
  Element  critRootEl;
  LogWriter log = null;

  public void setLogWriter(LogWriter log) {this.log=log;}
  public void debug(Object msg) {if (log!=null) log.p(msg);} public void debug(String msg) {if (log!=null) log.p(msg);}

  public selCrit(dataSel d) throws Exception
  {critRootEl=d.getCritAsXML(true).getDocumentElement();}

  public selCrit(String fileName) throws Exception
  {
   critRootEl=xmlRdr.getRoot(fileName ,false);
  }

  public org.w3c.dom.Document getCritAsXML(boolean attrsMode) throws java.lang.Exception
  {return critRootEl.getOwnerDocument();}

  public Element get(String name)
  {
    try
    {
      return xmlRdr.findElwithAttr(critRootEl.getElementsByTagName("FIELD"), "name",  name);
    }
    catch(Exception e) {return null;}
  }

  public String getLabel(String name)
  {
    Element fldEl=get(name);
    return fldEl.getAttribute("label");
  }

  public String getVal(String name) throws Exception
  {
    boolean isComposedBool;
    StringBuffer lstNameBuff=new StringBuffer();
    StringBuffer attrNameBuff=new StringBuffer();
    if (isComposedBool=dataSel.isComposed(name, lstNameBuff, attrNameBuff))
    {
      name=lstNameBuff.toString();
    }
    Element fldEl=get(name);
    if (fldEl==null) return null;
    if (isComposedBool) return getAttrVal(fldEl, attrNameBuff.toString());
    else return getVal(fldEl);
  }

  private String getAttrVal(Element fldEl, String attrName)
  {
    try
    {
      Element valEl=xmlRdr.getChild(xmlRdr.getChild((Element)fldEl.getParentNode(), "ITEM"), "VAL");
      return xmlRdr.getVal(xmlRdr.getChild(valEl, "ATTROW"),attrName);
    }
    catch(Exception e) {return null;}
  }

  private String getVal(Element fldEl)
  {return getVal(fldEl, true, true);}

  private String getVal(Element fldEl, boolean lowBool, boolean rangeBool)
  {
    try
    {
     Element condEl=(Element)fldEl.getParentNode();
     if (condEl.getAttribute("operator").equals("BETWEEN"))
     {
      NodeList els=condEl.getElementsByTagName("ITEM");
      Element lowEl=(Element)els.item(0);
      Element uppEl=(Element)els.item(1);
      if (rangeBool) return new String("("+xmlRdr.getVal(lowEl, "VAL")+"-"+xmlRdr.getVal(uppEl, "VAL")+")");
      else
        if (lowBool) return xmlRdr.getVal(lowEl, "VAL");
        else return xmlRdr.getVal(uppEl, "VAL");
     }
     else
     {
      return xmlRdr.getVal(xmlRdr.getChild(condEl, "ITEM"), "VAL");
     }
    }
    catch(Exception e) {return null;}
  }

  public String getBindString(java.lang.String name, java.lang.String label, boolean labelBool)
  {
    boolean isComposedBool;
    StringBuffer lstNameBuff=new StringBuffer();
    StringBuffer attrNameBuff=new StringBuffer();
    if (isComposedBool=dataSel.isComposed(name, lstNameBuff, attrNameBuff))
    {
      name=lstNameBuff.toString();
    }
    Element fldEl=get(name);
    if (fldEl==null) return null;
    else
    {
      String labelStr=(label.equals("") ? "" : label.concat("="));
      String val=null;
      if (!isComposedBool)
      {
        val=getVal(fldEl);
      }
      else
      {
        String attrName=attrNameBuff.toString();
        val=getAttrVal(fldEl, attrName);
      }
      if (val==null) return null;
      else return (!val.equals("") ? (labelBool ? labelStr+val : val) : null);
    }
  }

  public static void main(String[] args) throws Exception
  {
    selCrit s=new selCrit("D:/Temp/crit2.xml");
    //debug(s.getVal("lot/product"));
    //debug(s.getBindString("lot/producto", "Prod", true));
  }
}