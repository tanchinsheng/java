package tol;

import java.util.*;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.w3c.dom.*;

public class dbBinder
{
 public static final String _joinsFileName="joins.xml";
 public static final String _pathsFileName="paths.xml";

 NodeList joinsTree=null; //it is the JOINS SCHEMA
 NodeList pathsTree=null; //it is the PATHS SCHEMA
                          //it could remain set to null, as
                          //the database binder is suppose to work even
                          //if no path schema has been defined

 //--- JOIN SCHEMA CONSTANTS ---//
 static final String _tblaTag   = "DBTABLEA";
 static final String _tblbTag   = "DBTABLEB";
 static final String _fldaTag   = "JOINFLDA";
 static final String _fldbTag   = "JOINFLDB";
 static final String _outerFlag = "outer";
 static final String _joinIdTag = "JOIN_ID";

 //--- PATHS SCHEMA CONSTANTS ---//
 static final String _dsKeyTag    = "DSKEY_ID ";
 static final String _fromTblTag  = "FROM_TBL";
 static final String _toTblTag    = "TO_TBL";
 //join_id has already been defined
 static final String _pathTypeTag = "PATHTYPE";
 static final String _pathIdTag   = "PATH_ID";

 static final String _andStr      = " AND ";
 LogWriter log=null;

 public void setLogWriter(LogWriter log) {this.log=log;}
 public void debug(Object msg) {if (log!=null) log.p(msg);} public void debug(String msg) {if (log!=null) log.p(msg);}
 
  public dbBinder(String fileName) throws Exception
  //fileName is the name of the directory containing join and path
  //definition xml files
  {
   try
   {
     FileInputStream f=new FileInputStream(fileName.concat("/"+_joinsFileName));
     f.close();
     Element root = xmlRdr.getRoot(fileName.concat("/"+_joinsFileName), false);
     joinsTree = root.getChildNodes();
   }
   catch(FileNotFoundException e) {debug("NO JOINS FOUND>"); return;}
   catch(Exception e)             {debug("JOINS FILE PARSING ERROR>"); throw e;}
   try
   {
     FileInputStream f=new FileInputStream(fileName.concat("/"+_pathsFileName));
     f.close();
     Element root = xmlRdr.getRoot(fileName.concat("/"+_pathsFileName), false);
     pathsTree = root.getChildNodes();
   }
   catch(FileNotFoundException e) {debug("PATHS FILE NOT FOUND>"); return;}
   catch(Exception e)             {debug("PATHS FILE PARSING ERROR>"); throw e;}
  }

  private String getJoin(String tA, String tB, Vector joinsVect, Vector tbls)
  //It returns the where condition (ie: an expression
  //of type " AND ta1.flda1 = tb1.fldb1 ... AND ...") resulting from the
  //path connecting table tA to tB. This procedure scan the
  //JOINS SCHEMA (joinsTree) and eventually the PATHS SCHEMA (pathTree).
  //
  //Procedure:
  //- We look on the joins schema (joinsTree) for entries connecting
  //  tA to tB (for one direct link between tA and tB more entries may
  //  exist, as a join could be defined on a list of attributes, ie: MOV_N
  //  and MOV_DATE)
  //- If no matching entry exist, we look on the path schema (pathTree):
  //  if one matching path (not more than one could exist) exists, this
  //  one is translated into the corresponding joins list
  //- Now we have got a joins list which must then be tranlated in a
  //  list of sql conditions
  //
  //Remark: This method looks for a path from table 'tA' to table 'tB' only
  //        (doesn't look for joins in the reverse order)
  //Note:   if no path exists in order to connect table tA to tB, this method
  //        returns a null value;
  {
    Vector vL = new Vector(); Vector vR = new Vector();
    vL.addElement(_tblaTag); vL.addElement(_tblbTag); vL.addElement("@hidden");
    vR.addElement(tA); vR.addElement(tB); vR.addElement("");
    try
    {
      //we look on the joins schema
      Vector joinEls = xmlRdr.findEls(joinsTree, vL, vR);

      //if no direct join exist between tA and tB
      //we must check the paths schema
      if (joinEls==null) joinEls = getPath(tA, tB);

      //if neither a specific path exist, this method returns null
      if (joinEls==null) return null;

      //from here on at least one join have been detected in order
      //to connect the tables tA and tB
      String retStr="";
      for (int i=0; i<joinEls.size(); i++)
      {
        Element joinEl = (Element)joinEls.elementAt(i);

        if (!existJoin(joinsVect, joinEl, false))
        {
          String tblA = xmlRdr.getVal(joinEl,_tblaTag);
          String tblB = xmlRdr.getVal(joinEl,_tblbTag);

          //the tables list is checked in order to see if tables tblA
          //and tblB must be added to it
          popTable(tblA, tbls, tA, tB);
          popTable(tblB, tbls, tA, tB);

          //the where condition resulting from the current join (joinEl)
          //is then produced and returned to the calling method
          String fldA = xmlRdr.getVal(joinEl,_fldaTag);
          String fldB = xmlRdr.getVal(joinEl,_fldbTag);
          fldA = tblA.concat(".").concat(fldA);
          if (xmlRdr.getChild(joinEl,_joinIdTag).getAttribute(_outerFlag).equals("A"))
          {
            fldA=fldA.concat(" (+)");
          }
          fldB = tblB.concat(".").concat(fldB);
          if (xmlRdr.getChild(joinEl,_joinIdTag).getAttribute(_outerFlag).equals("B"))
          {
            fldB=fldB.concat(" (+)");
          }
          retStr = retStr.concat(_andStr+fldA+" = "+fldB);
        }
      }
      //updates the join vector
      for (int i=0; i<joinEls.size(); i++)
      {
        existJoin(joinsVect, (Element)joinEls.elementAt(i), true);
      }
      return retStr;
    }
    catch(Exception e) {return null;}
  }

  private Vector getPath(String tA, String tB) throws Exception
  //this method look on the PATHS SCHEMA for an entry connecting
  //table tA to tB - this entry (if any exists) is then translated into
  //the corresponding joins list (vector of 'JOIN' elements) which is
  //then returned to the calling method
  //Remark: if no matching entry exists a null value is returned.
  {
    Vector vL = new Vector();
    Vector vR = new Vector();
    vL.addElement(this._fromTblTag);
    vL.addElement(this._toTblTag);
    vR.addElement(tA);
    vR.addElement(tB);
    Vector pathEls = xmlRdr.findEls(pathsTree, vL, vR);
    if (pathEls.size()==0) return null;

    Vector retVect=new Vector();
    for (int i=0; i<pathEls.size(); i++)
    {
      String joinId=xmlRdr.getVal((Element)pathEls.elementAt(i),_joinIdTag);
      Vector pJoins=xmlRdr.findEls(joinsTree, _joinIdTag, joinId);
      for (int j=0; j<pJoins.size(); j++)
      {
        retVect.addElement(pJoins.elementAt(j));
      }
    }
    return retVect;
  }


 private boolean existJoin(Vector joinsVect, Element joinEl, boolean addBool) throws Exception
 //if the join id is not already included in the joinEl vector, this method
 //returns true and if addBool is set then adds the join id to the vector
 {
   String id=xmlRdr.getVal(joinEl,_joinIdTag);
   for (int i=0; i<joinsVect.size(); i++)
   {
     if (((String)joinsVect.elementAt(i)).equals(id)) return true;
   }
   if (addBool) joinsVect.addElement(id);
   return false;
 }

  public String getQuery(String tA, String fieldNames, String whrStr, String orderCls, Vector tbls, boolean distinctBool) throws SQLException
  {return getQuery(tA, null, fieldNames, whrStr, orderCls, tbls, distinctBool);}

  public String getQuery(String tA, String ftA, String fieldNames, String whrStr, String orderCls, Vector tbls, boolean distinctBool) throws SQLException
  //'f' is the field whose values must be fetched ('t' is the table of 'f')
  //'whrStr' is the where Clause created by the slctLst class
  //This methods appends the join conditions to 'whrStr', and then
  //returns the whole query.
  {
    Vector joinsVect = new Vector();
    String selectCls = new String("SELECT "+(distinctBool ? "DISTINCT " : "")+fieldNames);
    String whereCls  = new String(" "+whrStr);

    //I make a copy of the number of table, as invoking 'getJoin' method
    //results in adding elements to the tbls vector
    int oldTblSize=tbls.size();
    for (int i=0; i<oldTblSize; i++)
    {
     String tB = (String)tbls.elementAt(i);
     String join = getJoin(tA, tB, joinsVect, tbls);
     if (join==null) join = getJoin(tB, tA, joinsVect, tbls);
     if (join==null) throw new SQLException("CAN'T FIND JOIN FROM '"+tA+"' TO '"+tB+"'>");
     whereCls=whereCls.concat(whereCls.trim().equals("") ? substAndWhr(join) : join);
    }

    String formCls   = " FROM "+((ftA!=null)&&(isInTablesList(ftA,tbls)) ? ftA+", "+tA : tA);
    for (int i=0; i<tbls.size(); i++)
    {
      if ((ftA!=null)&&(((String)tbls.elementAt(i)).equals(ftA))) continue;
      formCls = formCls.concat(", "+(String)tbls.elementAt(i));
    }

    selectCls = selectCls.concat(formCls+whereCls+orderCls);
    return selectCls;
  }


 private void popTable(String tName, Vector tbls, String tA, String tB)
 //This method adds tName to the tbls vector provided
 //it is not already included and it is different from both tA and tB.
 //One of either tA or tB is necessarily the main field table (the main
 //field is that field for which the fetch is required by the user)
 //Remark: tbls must never include the main feature table
 {
   if (tName.equals(tA)) return;
   if (tName.equals(tB)) return;
   for (int i=0; i<tbls.size(); i++)
   {
     if (((String)tbls.elementAt(i)).equals(tName)) return;
   }
   tbls.addElement(tName);
 }

 boolean isInTablesList(String tblName, Vector tbls)
 {
   for (int i=0; i<tbls.size(); i++)
   {
       if (((String)tbls.elementAt(i)).equals(tblName)) return true;
   }
   return false;
 }

 private String substAndWhr(String s)
 //this method substitutes the _andStr with ' WHERE '
 //into the string s
 {
   return new String("WHERE "+s.substring(_andStr.length()));
 }

 public static void main(String[] args) throws Exception
 {
   dbBinder dbndr = new dbBinder("../scott-joins.xml");
   Vector tbls = new Vector();
   tbls.addElement(new String("CATMAN"));
   //debug(dbndr.getQuery("EMP","ENAME","WHERE ROLE = 'SALESMAN'","",tbls));
 }

}