package it.txt.tpms.tplib.xml;

import java.io.File;
import java.util.HashMap;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlReader extends DOMParser{
	private HashMap result;
	private String fileName;
	private String msg;

	public XmlReader(String fileName){
		this.fileName = fileName;
		result = new HashMap();
		msg = "Done";
	}

	public boolean parse(){
	    Document document = null;
		boolean parseResult = true;

		File f = new File(fileName);
		if(!f.exists()){
			msg = "The xml file " + fileName + " does not exists.";
			return false;
		}

		try{
			super.parse(fileName);
			document = super.getDocument();
		}catch(Exception e){
			document = null;
			parseResult = false;
			msg = e.getMessage();
		}

		if(document != null){
			parseDocument(document);
		}

		return parseResult;
	}

	public String getMessage(){
		return msg;
	}

	public HashMap getResult(){
		return result;
	}

	public String getValue(String tagName){
		return (String)result.get(tagName);
	}

	public File getSourceFile(){
		return new File(fileName);
	}

	public String getSourceFileName(){
		return fileName;
	}

	private void parseDocument(Node node){
		NodeList list = node.getChildNodes();
		int childCount = list.getLength();

		for(int i=0; i<childCount; i++){
			Node next = list.item(i);
			String name = next.getNodeName();
			String text = getNodeText(next);

			if(next.getNodeType() == Node.ELEMENT_NODE){
				result.put(name, text);
			}

			parseDocument(next);
		}
	}

	private String getNodeText(Node node){
		String text = "";

		Node child = node.getFirstChild();
		if(child != null) text = child.getNodeValue();

		return text;
	}
}
