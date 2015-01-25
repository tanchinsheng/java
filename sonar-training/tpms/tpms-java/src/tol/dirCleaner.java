package tol;

import java.io.*;
import java.util.*;

public class dirCleaner
{
 File dir;
 Vector files;
 long safeTime;  //expressed in milliseconds
 LogWriter log;

 public void setLogWriter(LogWriter log) {this.log=log;}
 public void debug(Object msg) {if (log!=null) log.p(msg);} public void debug(String msg) {if (log!=null) log.p(msg);}

 public dirCleaner(String dirName, int min) throws IOException
 {
  safeTime = min*60000;
  dir = new File(dirName);
  if (!dir.isDirectory()) throw new IOException("'"+dirName+"' DIRECTORY DOESN'T EXIST");
 }

 protected void refreshList()
 {
  String[] fileArr = dir.list();
  files = new Vector();
  try
  {
    for (int i=0; true; i++) files.addElement(fileArr[i]);
  }
  catch(ArrayIndexOutOfBoundsException e) {}
 }


 public void delFilesIfOld() throws Exception
 {
   refreshList();
   for (int i=0; i<files.size(); i++)
                delIfOld((String)files.elementAt(i));
 }

 public void delFiles(Vector files) throws Exception
 {
   for (int i=0; i<files.size(); i++)
   {
     File f = new File(dir, (String)files.elementAt(i));
     f.delete();
   }
 }

 protected boolean delIfOld(String fname) throws Exception
 {
  File f = new File(dir, fname);
  /*debug(f.getName()+" - delay[min]: " +
                    ((System.currentTimeMillis()-f.lastModified())/60000));*/
  if ((System.currentTimeMillis()-f.lastModified())>safeTime)
  {
    debug(f.getName()+"> DELETED.");
    f.delete();
    return true;
  }
  else return false;
 }

 public static void main(String args[]) throws Exception
 {
  dirCleaner d = new dirCleaner(args[0],2);
  d.delFilesIfOld();
 }
}