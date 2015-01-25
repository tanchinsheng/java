package it.txt.tpms.tplib.xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class XmlValidator {
	private Vector fieldNames;
	private HashMap fieldsToValidate;
	private StringBuffer report;

	public XmlValidator(){
		fieldNames = new Vector();
		report = new StringBuffer();
	}

	public void addFieldName(String name){
		fieldNames.add(name);
	}

	public void addFieldNames(String[] names){
		for(int i=0; i<names.length; i++){
			addFieldName(names[i]);
		}
	}

	public void setFieldsToValidate(HashMap parsedFields){
		fieldsToValidate = parsedFields;
	}

	public boolean validate(){
		Iterator it = fieldNames.iterator();
		boolean esit = true;

		while(it.hasNext()){
			String fieldName = (String)it.next();
			String fieldValue = (String)fieldsToValidate.get(fieldName);

			if(fieldValue == null){
				report.append("The field " + fieldName + " is required. - ");
				esit = false;
			}else if(fieldValue.equals("")){
				report.append("The field " + fieldName + " could not be empty. - ");
				esit = false;
			}
		}

		return esit;
	}

	public String getReport(){
		return report.toString();
	}
}
