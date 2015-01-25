package tol;

import java.util.*;
import java.io.*;
import java.sql.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import org.w3c.dom.*;
import oracle.jdbc.driver.OracleConnection;

public class repSect extends slctLst
{
 public repSect() {}
 LogWriter log = null;
 String outputTableName;

 public void setLogWriter(LogWriter log) {this.log=log;}
 public void debug(Object msg) {if (log!=null) log.p(msg);} public void debug(String msg) {if (log!=null) log.p(msg);}

 public repSect(String name, String label, dbRdr d, String t, String bt)
 {
  dbr = d;
  this.name      = name;
  this.label     = label;
  this.tableName = t;
  this.outputTableName = this.tableName;
  selButName = bt;
  index = getIndex(); //called after having set selButName
  v=new Vector();
 }

 public boolean isBlank() {return (v.size()==0);}

 public void addAttr(String name, String label, char type, String fieldName, String tableName, String sqlexpr, String format, Element funzEl)
 {
  if (attrs==null)
  {
    attrs=new attrLst();
  }
  attrs.add(name, label, type, fieldName, tableName, sqlexpr, format, funzEl);
 }

 public void addDetail(String name, String label, char type, String fieldName, String tableName, String sqlexpr, String format, Element funzEl)
 {
  if (attrs==null)
  {
    attrs=new attrLst();
  }
  attrs.addDetail(name, label, type, fieldName, tableName, sqlexpr, format, funzEl);
 }

 protected String getFldLst(boolean mode)
 {
  String fldStr="";
  for (int i=0; i<attrs.size(); i++)
  {
    if ((!mode)&&(attrs.isDetailed(i))) continue;
    if (attrs.isComputed(i)) continue;
    if (i>0) fldStr=fldStr.concat(", ");
    fldStr=fldStr.concat(attrs.getSqlExpr(i));
  }
  return fldStr;
 }


 public Properties setFilter(HttpServletRequest req) throws Exception
 {return this.setFilter(req, true);}

 public Properties setFilter(HttpServletRequest req, boolean resetBool) throws Exception
 {
  Properties props=new Properties();
  for (int i = 0; i<singleDep.size(); i++)
  {
    slctLst mLst = (slctLst)singleDep.elementAt(i);
    String parName="field."+mLst.getName();
    if (resetBool) mLst.setBlank();
    if (!(mLst instanceof rngTxtLst))
    {
      if ((req.getParameterValues(parName)!=null)&&(!req.getParameterValues(parName)[0].trim().equals("")))
      {
        mLst.setVal(req.getParameterValues(parName)[0]);
        props.setProperty(parName, req.getParameterValues(parName)[0]);
      }
    }
    else
    {
      if (req.getParameterValues(parName+".min")!=null)
      {
        ((rngTxtLst)mLst).setVal1(req.getParameterValues(parName+".min")[0]);
        props.setProperty(parName+".min", req.getParameterValues(parName+".min")[0]);
      }
      if (req.getParameterValues(parName+".max")!=null)
      {
        ((rngTxtLst)mLst).setVal2(req.getParameterValues(parName+".max")[0]);
        props.setProperty(parName+".max", req.getParameterValues(parName+".max")[0]);
      }
    }
  }
  return props;
 }

 public Properties setFilter(Properties props) throws Exception
 {return this.setFilter(props, true);}

 public Properties setFilter(Properties props, boolean resetBool) throws Exception
 {
  for (int i = 0; i<singleDep.size(); i++)
  {
    slctLst mLst = (slctLst)singleDep.elementAt(i);
    String parName="field."+mLst.getName();
    if (resetBool) mLst.setBlank();
    if (!(mLst instanceof rngTxtLst))
    {
      if (props.containsKey(parName))
      {
        mLst.setVal(props.getProperty(parName));
      }
    }
    else
    {
      if (props.containsKey(parName+".min"))
      {
        ((rngTxtLst)mLst).setVal1(props.getProperty(parName+".min"));
      }
      if (props.containsKey(parName+".max"))
      {
        ((rngTxtLst)mLst).setVal2(props.getProperty(parName+".max"));
      }
    }
  }
  return props;
 }

 public void setOutputTableName(String s) { this.outputTableName=s; }
 public String getOutputTableName() { return this.outputTableName; }


 public String getInsertSqlSttmt(ResultSet rs, String partitionKey) throws SQLException
 {
    String sttmt=" INSERT INTO "+getOutputTableName()+ "(";
    for (int i=0; i<getAttrsN(); i++)
    {
        sttmt+=( i>0 ? ", " : "" )+getAttrName(i);
    }
    if (partitionKey != null)
    {
        sttmt+=", PKEY";
    }
    sttmt+=")";
    sttmt+=" VALUES(";
    for( int j=1; j<=rs.getMetaData().getColumnCount(); j++ )
    {
        if ( rs.getObject(j) instanceof String)
        {
            sttmt+=( j>1 ? ", " : "" )+"'"+rs.getObject(j)+"'";
        }
        else
        {
            sttmt+=( j>1 ? ", " : "" )+rs.getObject(j);
        }
    }
     if (partitionKey != null)
     {
         sttmt+=", '"+partitionKey+"'";
     }
    sttmt+=")";
    return sttmt;
 }

 public String toString()
 {
   return new String(this.name+"\n"+this.fieldName+"\n"+this.tableName+"\n"+this.selButName+"\n"+this.index);
 }

public void createImage (
                            Connection srcConn,
                            Connection outConn,
                            String partitionKey ) throws Exception
{
    Statement srcStmt=null;
    ResultSet rs=null;
    Statement sqlSttmt=null;

    try
    {
        String srcQry = this.getQuery(false);
        debug("DB-IMAGE-QUERY>"+srcQry);
        srcStmt=srcConn.createStatement();
        rs = srcStmt.executeQuery (srcQry);
    }
    catch(Exception e)
    {
        debug("ERROR>DB IMAGE DATA FETCH FAILURE: "+e.toString());
        rs.close();
        srcStmt.close();
        throw e;
    }

    try
    {
        sqlSttmt = outConn.createStatement();
        if (partitionKey==null)
        {
            sqlSttmt.execute("TRUNCATE TABLE "+this.getOutputTableName());
        }
    }
    catch(Exception e)
    {
        debug("ERROR>DB IMAGE TABLES INITIALIZATION FAILURE: "+e.toString());
        rs.close();
        srcStmt.close();
        sqlSttmt.close();
        throw e;
    }

    for  (int i=0; rs.next(); i++)
    {
        if (i==0) debug(this.getInsertSqlSttmt(rs, partitionKey)); //debug
        try
        {
            sqlSttmt.execute(this.getInsertSqlSttmt(rs, partitionKey));
        }
        catch(Exception e)
        {
            debug(this.getInsertSqlSttmt(rs, partitionKey));
            debug("ERROR>DB IMAGE DATA INSERTION FAILURE: "+(e.getMessage()!=null ? e.getMessage() : ""));
        }
    }
    rs.close();
    srcStmt.close();
    sqlSttmt.close();
 }

 /*
 public ResultSet getRecordSet(boolean mode) throws Exception
 {
     return dbr.getRecordset(this.getQuery(mode));
 }
 */
}
