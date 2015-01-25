package tol.util;

import tol.*;
import java.util.*;
import org.w3c.dom.*;
import java.io.*;

public class RecordsetCritBuilder extends CritBuilder
{
 public RecordsetCritBuilder(String _initDir, String analisys_id, String model, String plant) throws Exception
 {
  super(_initDir, analisys_id, model, plant);
 }

 public RecordsetCritBuilder(CritBuilder crit) throws Exception
 {
  dbmodel=crit.getDbModelRoot();
  images=crit.getImagesDefRoot();
  model_id=crit.getModelID();
  plant=crit.getPlant();
  analisys_id=crit.getAnalysisID();
  _initDir=crit.getInitDir();
 }

/**
 * create an XML file named as 'fileName', into the 'dir' directory, containing
 * the cryterion defined by the current status of the
 * data selection object 'dsel'.
 */
 public void getCrit(String app_id, dataSel data, String dir, String fileName, boolean detailedCritBool, Properties fldsLst) throws Exception
 {
  Document doc=data.getCritAsXmlRecordset(detailedCritBool, fldsLst);
  this.writeDbAccessInfo(doc.getDocumentElement());
  FileOutputStream output = new FileOutputStream(dir.concat("/"+fileName), false);
  xmlRdr.echo(doc, new PrintStream(output));
  output.close();
 }
}