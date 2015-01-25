package it.txt.general.utils;

import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.dom.TextImpl;
import org.w3c.dom.*;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;
import tpms.TpmsException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;


/**
 * Created by IntelliJ IDEA.
 * User: furgiuele
 * Date: 28-set-2005
 * Time: 16.19.18
 * This class contains general xml utilities.
 */
public class XmlUtils {
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat();



    public static String getLeafElementAsString (String tagName, NamedNodeMap namedNodeMap, String nodeText){
        String result = getOpenTagString( tagName, namedNodeMap );
        result = result + nodeText;
        result = result + getCloseTagString( tagName );
        return result;
    }


    /**
     * @param tagName
     * @return a String representing the open tag called tagName
     */
    public static String getOpenTagString(String tagName) {
        return getOpenTagString(tagName, null);
    }

    /**
     * @param tagName
     * @param namedNodeMap
     * @return a String representing the open tag called tagName with the attributes contained in namedNodeMap
     */
    public static String getOpenTagString(String tagName, NamedNodeMap namedNodeMap) {
        String result = "";
        if (!GeneralStringUtils.isEmptyString(tagName)) {
            String attributes = " " + getNodeAttributesAsString(namedNodeMap) + "";
            result = "<" + tagName + attributes + ">";
        }
        return result;
    }


    /**
     * @param tagName
     * @return a String representing the closing tag called tagName
     */
    public static String getCloseTagString(String tagName) {
        return "</" + tagName + ">";
    }

    /**
     * @param namedNodeMap
     * @return a String repesnting the attributes contained in namedNodeMap
     */
    public static String getNodeAttributesAsString(NamedNodeMap namedNodeMap) {
        StringBuffer result = new StringBuffer();
        if (namedNodeMap != null) {
            int attributesCount = namedNodeMap.getLength();
            for (int i = 0; i < attributesCount; i++) {
                result.append(namedNodeMap.item(i));
            }
        }
        return result.toString().trim();
    }

    /**
     *
     * @param node
     * @return a String repesnting the attributes of node.
     */
    public static String getNodeAttributesAsString(Node node) {
        String result = "";
        if (node != null) {
            result = getNodeAttributesAsString(node.getAttributes());
        }
        return result;
    }

    /**
     * @param nodeName
     * @return return true if and only if the given nodeName is equal to a fixed values that you can find in org.w3c.dom.Node documentation
     */
    private static boolean skipNodeStructure(String nodeName) {
        return nodeName.equals("#text") || nodeName.equals("#cdata-section") || nodeName.equals("#comment") || nodeName.equals("#document") || nodeName.equals("#document-fragment");
    }


    /**
     * @param node
     * @return a String that represent the structure of the given Node
     */
    public static String getNodeStructureAsString(Node node) {
        String result = "";
        if (node != null) {
            String nodeName = node.getNodeName();
            if (!skipNodeStructure(nodeName)) {
                String nodeOpenTag = getOpenTagString(nodeName, node.getAttributes());
                String textValue = GeneralStringUtils.isEmptyString(node.getNodeValue()) ? "" : node.getNodeValue();

                String childNodesString = "";
                if (node.hasChildNodes()) {
                    NodeList childNodes = node.getChildNodes();
                    int childCount = childNodes.getLength();
                    for (int i = 0; i < childCount; i++) {
                        childNodesString += getNodeStructureAsString(childNodes.item(i));
                    }
                }
                String nodeCloseTag = getCloseTagString(nodeName);
                result = nodeOpenTag + textValue + childNodesString + nodeCloseTag;
            } else {
                result = GeneralStringUtils.isEmptyString(node.getNodeValue()) ? "" : node.getNodeValue();
            }
        }
        return result;
    }

    /**
     * @param element
     * @param toBeDeleteTagName
     * @return remove the child identified by toBeDeleteTagName tag from the given Element
     */
    public static Element removeChilds(Element element, String toBeDeleteTagName) {
        if (element != null && !GeneralStringUtils.isEmptyString(toBeDeleteTagName)) {
            NodeList toBeDeletedNodeList = element.getElementsByTagName(toBeDeleteTagName);
            if (toBeDeletedNodeList != null && toBeDeletedNodeList.getLength() > 0) {
                int toBeDeletedElementsCount = toBeDeletedNodeList.getLength();
                Node currentNode;
                for (int i = (toBeDeletedElementsCount - 1); i >= 0; i--) {
                    currentNode = toBeDeletedNodeList.item(i);
                    if (currentNode != null) {
                        element.removeChild(currentNode);
                    }
                }
            }
        }
        return element;
    }


    /**
     * take a xml element and the tag name, look for the tag and get
     * the text content
     *
     * @param ele     the element
     * @param tagName is the tag name contained in ele wich we want to retrieve the value
     * @return the value of the element identified by tagName inside of ele
     */

    public static String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            Element el = (Element) nl.item(0);
            if (el != null) {
                Node n = el.getFirstChild();
                if (n != null) {
                    textVal = n.getNodeValue();
                }
            }
        }

        return textVal;
    }

    /**
     * take a xml element and the tag name, look for the tag and get
     * the text content
     *
     * @param e       the element
     * @param tagName is the tag name contained in ele wich we want to retrieve the value
     * @return the value of the element in a Date object identified by tagName inside of e. If the value is null or empty return null
     * @throws ParseException if an error occours during value parsing
     */
    public static Date getDateValue(Element e, String tagName, String dateFormat) throws ParseException {
        String stringDate = getTextValue(e, tagName);
        if (GeneralStringUtils.isEmptyString(stringDate)) {
            return null;
        } else {
            dateFormatter.applyPattern(dateFormat);
            return dateFormatter.parse(stringDate);
        }
    }

    /**
     * take a xml element and the tag name, look for the tag and get
     * the text content returning it in int format
     *
     * @param e       the element
     * @param tagName is the tag name contained in ele wich we want to retrieve the value
     * @return the value of the element in int format identified by tagName inside of e. If the value is null or empty return 0
     * @throws NumberFormatException if the streing content of the given tagName does not contain a parsable integer.
     */

    public static int getIntValue(Element e, String tagName) throws NumberFormatException {
        String stringInt = getTextValue(e, tagName);
        if (GeneralStringUtils.isEmptyString(stringInt)) {
            return 0;
        } else {
            return Integer.parseInt(stringInt);
        }
    }

    /**
     * take a xml element and the tag name, look for the tag and get
     * the text content returning it in long format
     *
     * @param e       the element
     * @param tagName is the tag name contained in ele wich we want to retrieve the value
     * @return the value of the element in int format identified by tagName inside of e. If the value is null or empty return 0
     * @throws NumberFormatException if the streing content of the given tagName does not contain a parsable long.
     */

    public static long getLongValue(Element e, String tagName) throws NumberFormatException {
        String stringInt = getTextValue(e, tagName);
        if (GeneralStringUtils.isEmptyString(stringInt)) {
            return 0;
        } else {
            return Long.parseLong(stringInt);
        }
    }

       /**
     * take a xml element and the tag name, look for the tag and get
     * the text content returning it in float format
     *
     * @param e       the element
     * @param tagName is the tag name contained in ele wich we want to retrieve the value
     * @return the value of the element in float format identified by tagName inside of e. If the value is null or empty return 0
     * @throws NumberFormatException if the streing content of the given tagName does not contain a parsable float.
     */
   public static float getFloatValue(Element e, String tagName) throws NumberFormatException {
        String stringInt = getTextValue(e, tagName);
        if (GeneralStringUtils.isEmptyString(stringInt)) {
            return 0;
        } else {
            return Float.parseFloat(stringInt);
        }
    }

    public static Element getRoot(String fileName) throws ParserConfigurationException, SAXException, IOException {

        File f = new File(fileName);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(f).getDocumentElement();

    }

    public static Element getRoot(File f) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(f).getDocumentElement();
    }

    public static Element getRoot(InputStream inputStream)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentbuilder = documentbuilderfactory.newDocumentBuilder();
        return documentbuilder.parse(inputStream).getDocumentElement();
    }

    public static Document getNewDoc(String s) throws ParserConfigurationException {
        DocumentBuilder documentbuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        DocumentImpl documentimpl = (DocumentImpl) documentbuilder.newDocument();
        ElementImpl elementimpl = new ElementImpl(documentimpl, s);
        documentimpl.appendChild(elementimpl);
        return documentimpl;
    }

    public static Element addEl(Element element, String s) {
        DocumentImpl documentimpl = (DocumentImpl) element.getOwnerDocument();
        ElementImpl elementimpl = new ElementImpl(documentimpl, s);
        element.appendChild(elementimpl);
        return elementimpl;
    }

    public static Element addEl(Element element, String s, String s1) {
        Element element1 = addEl(element, s);
        org.apache.xerces.dom.TextImpl textimpl = new TextImpl((DocumentImpl) element.getOwnerDocument(), s1);
        element1.appendChild(textimpl);
        return element1;
    }

    public static void setVal(Element element, String s) throws TpmsException {
        org.w3c.dom.Node node;
        if ((node = element.getFirstChild()) == null) {
            org.apache.xerces.dom.TextImpl textimpl = new TextImpl((DocumentImpl) element.getOwnerDocument(), s);
            element.appendChild(textimpl);
        } else if (node instanceof org.w3c.dom.Text)
            ((org.w3c.dom.Text) node).setData(s);
        else
            throw new TpmsException("ATTEMPT TO SET THE VALUE IN A COMPLEX ELEMENT");
    }

    public static void setVal(Element element, String s, String s1)
            throws TpmsException {
        element = getChild(element, s);
        org.w3c.dom.Node node;
        if ((node = element.getFirstChild()) == null) {
            org.apache.xerces.dom.TextImpl textimpl = new TextImpl((DocumentImpl) element.getOwnerDocument(), s1);
            element.appendChild(textimpl);
        } else if (node instanceof org.w3c.dom.Text)
            ((org.w3c.dom.Text) node).setData(s1);
        else
            throw new TpmsException("ATTEMPT TO SET THE VALUE IN A COMPLEX ELEMENT");
    }

    public static String getVal(org.w3c.dom.Node node) {
        return getVal((Element) node);
    }

    public static String getVal(Element element) {
        return element == null ? null : element.getFirstChild().getNodeValue().trim();
    }

    public static String getVal(Element element, String s) {
        NodeList nodelist = element.getElementsByTagName(s);
        if (nodelist.getLength() == 0)
            return null;
        Element element1 = (Element) nodelist.item(0);
        if (element1.hasChildNodes())
            return element1.getFirstChild().getNodeValue().trim();
        else
            return null;
    }

    public static Element getChild(Element element, String s) {
        NodeList nodelist = element.getElementsByTagName(s);
        if (nodelist.getLength() == 0)
            return null;
        else
            return (Element) nodelist.item(0);
    }

    public static Element getDirChild(Element element, String s) {
        NodeList nodelist = element.getElementsByTagName(s);
        if (nodelist.getLength() == 0)
            return null;
        for (int i = 0; i < nodelist.getLength(); i++)
            if (nodelist.item(i).getParentNode() == element)
                return (Element) nodelist.item(i);

        return null;
    }

    public static Element findEl(NodeList nodelist, String s, String s1) {
        Vector vector = new Vector();
        vector.addElement(s);
        Vector vector1 = new Vector();
        vector1.addElement(s1);
        return findEl(nodelist, vector, vector1);
    }

    public static Element findEl(NodeList nodelist, Vector vector, Vector vector1) {
        for (int i = 0; i < nodelist.getLength(); i++) {
            org.w3c.dom.Node node = nodelist.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                boolean flag = true;
                for (int j = 0; j < vector.size(); j++) {
                    NodeList nodelist1 = element.getElementsByTagName((String) vector.elementAt(j));
                    String s = nodelist1.item(0).getFirstChild().getNodeValue();
                    if (s.equals(vector1.elementAt(j)))
                        continue;
                    flag = false;
                    break;
                }

                if (flag)
                    return element;
            }
        }

        return null;
    }

    public static Element findElwithAttr(NodeList nodelist, String s, String s1) {
        Vector vector = new Vector();
        vector.addElement(s);
        Vector vector1 = new Vector();
        vector1.addElement(s1);
        return findElementWithAttr(nodelist, vector, vector1);
    }

    public static Element findElementWithAttr(NodeList nodelist, Vector vector, Vector vector1) {
        for (int i = 0; i < nodelist.getLength(); i++) {
            Element element = (nodelist.item(i) instanceof Element) ? (Element) nodelist.item(i) : null;
            if (element != null) {
                boolean flag = true;
                for (int j = 0; j < vector.size(); j++) {
                    String s = (String) vector.elementAt(j);
                    if (element.getAttribute(s).equals(vector1.elementAt(j)))
                        continue;
                    flag = false;
                    break;
                }

                if (flag)
                    return element;
            }
        }

        return null;
    }

    public static Vector findEls(NodeList nodelist, String s, String s1) {
        Vector vector = new Vector();
        vector.addElement(s);
        Vector vector1 = new Vector();
        vector1.addElement(s1);
        return findElements(nodelist, vector, vector1);
    }

    public static Vector findElements(NodeList nodelist, Vector vector, Vector vector1) {
        Vector vector2 = null;
        Vector vector3 = new Vector();
        for (int i = 0; i < nodelist.getLength(); i++) {
            org.w3c.dom.Node node = nodelist.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                boolean flag = true;
                for (int j = 0; j < vector.size(); j++) {
                    if (!((String) vector.elementAt(j)).startsWith("@")) {
                        NodeList nodelist1 = element.getElementsByTagName((String) vector.elementAt(j));
                        String s1 = nodelist1.item(0).getFirstChild().getNodeValue();
                        if (s1.equals(vector1.elementAt(j)))
                            continue;
                        flag = false;
                        break;
                    }
                    String s = ((String) vector.elementAt(j)).substring(1);
                    if (element.getAttribute(s).equals(vector1.elementAt(j)))
                        continue;
                    flag = false;
                    break;
                }

                if (flag)
                    (vector2 = vector3).addElement(element);
            }
        }

        return vector2;
    }

    public static Document xPathExtract(Vector vector, String s) {
        for (int i = 0; i < vector.size(); i++) {
            Document document;
            try {
                document = xPathExtract((String) vector.elementAt(i), s);
            }
            catch (Exception exception) {
                continue;
            }
            if (document.getDocumentElement().hasChildNodes())
                return document;
        }

        return null;
    }

    public static Document xPathExtract(String s, String s1) throws SAXException, IOException, ParserConfigurationException, TransformerException {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream(s));

        return xPathExtract(document, s1);
    }

    public static Document xPathExtract(Document document, String s) throws TransformerException {
        NodeIterator nodeiterator = org.apache.xpath.XPathAPI.selectNodeIterator(document, s);

        org.w3c.dom.Node node = document.getDocumentElement().cloneNode(false);
        org.w3c.dom.Node node1;
        while ((node1 = nodeiterator.nextNode()) != null) node.appendChild(node1);
        document.removeChild(document.getDocumentElement());
        document.appendChild(node);
        return document;
    }

    public static void applyXSL(String s, String s1, Writer writer) throws SAXException, IOException, ParserConfigurationException, TransformerException {

        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(s);
        applyXSL(document, s1, writer);

    }

    public static void applyXSL(Document document, String s, Writer writer)
            throws TransformerConfigurationException, TransformerException, IOException {
        applyXSL(document, s, (java.util.Properties) null, writer);
    }

    public static void applyXSL(Document document, String s, java.util.Properties properties, Writer writer)
            throws TransformerException, FileNotFoundException {
        javax.xml.transform.TransformerFactory transformerfactory = javax.xml.transform.TransformerFactory.newInstance();
        javax.xml.transform.Transformer transformer = transformerfactory.newTransformer(new StreamSource(new FileInputStream(s)));
        if (properties != null)
            setParamFromProps(transformer, properties);
        transformer.transform(new DOMSource(document), new StreamResult(writer));
    }

    public static void applyXSL(Document document, String s, Document document1) throws TransformerConfigurationException, TransformerException, IOException {
        applyXSL(document, s, null, document1);
    }

    public static void applyXSL(Document document, String s, java.util.Properties properties, Document document1) throws TransformerConfigurationException, TransformerException, IOException {
        javax.xml.transform.TransformerFactory transformerfactory = javax.xml.transform.TransformerFactory.newInstance();
        javax.xml.transform.Transformer transformer = transformerfactory.newTransformer(new StreamSource(new FileInputStream(s)));
        if (properties != null)
            setParamFromProps(transformer, properties);
        transformer.transform(new DOMSource(document), new DOMResult(document1));
    }

    public static void applyXSL(Document document, Document document1, Writer writer) throws TransformerConfigurationException, TransformerException {
        javax.xml.transform.TransformerFactory transformerfactory = javax.xml.transform.TransformerFactory.newInstance();
        javax.xml.transform.Transformer transformer = transformerfactory.newTransformer(new DOMSource(document1));
        transformer.transform(new DOMSource(document), new StreamResult(writer));
    }

    public static void applyXSL(Element element, Document document, Element element1) throws TransformerConfigurationException, TransformerException {
        javax.xml.transform.TransformerFactory transformerfactory = javax.xml.transform.TransformerFactory.newInstance();
        javax.xml.transform.Transformer transformer = transformerfactory.newTransformer(new DOMSource(document));
        transformer.transform(new DOMSource(element), new DOMResult(element1));
    }

    public static Document transform(Document document, Document document1) throws TransformerConfigurationException, TransformerException {
        return transform(document, document1, null);
    }

    public static Document transform(Document document, Document document1, java.util.Properties properties) throws TransformerException {
        javax.xml.transform.TransformerFactory transformerfactory = javax.xml.transform.TransformerFactory.newInstance();
        javax.xml.transform.Transformer transformer = transformerfactory.newTransformer(new DOMSource(document1));
        javax.xml.transform.dom.DOMResult domresult = new DOMResult();
        setParamFromProps(transformer, properties);
        transformer.transform(new DOMSource(document), domresult);
        return (Document) domresult.getNode();
    }

    public static Document transform(Document document, String s) throws TransformerConfigurationException, TransformerException, IOException {
        return transform(document, s, null);
    }

    public static Document transform(Document document, String s, java.util.Properties properties) throws TransformerConfigurationException, TransformerException, IOException {
        javax.xml.transform.TransformerFactory transformerfactory = javax.xml.transform.TransformerFactory.newInstance();
        javax.xml.transform.Transformer transformer = transformerfactory.newTransformer(new StreamSource(new FileInputStream(s)));
        setParamFromProps(transformer, properties);
        javax.xml.transform.dom.DOMResult domresult = new DOMResult();
        transformer.transform(new DOMSource(document), domresult);
        return (Document) domresult.getNode();
    }


    public static Document transform(Document document, String s, java.util.Properties properties, Writer writer) throws TransformerConfigurationException, TransformerException, IOException {
        javax.xml.transform.TransformerFactory transformerfactory = javax.xml.transform.TransformerFactory.newInstance();
        javax.xml.transform.Transformer transformer = transformerfactory.newTransformer(new StreamSource(new FileInputStream(s)));
        setParamFromProps(transformer, properties);
        transformer.transform(new DOMSource(document), new StreamResult(writer));
        return null;
    }


    protected static void setParamFromProps(javax.xml.transform.Transformer transformer, java.util.Properties properties) {
        if (properties != null) {
            String s;
            String s1;
            for (java.util.Enumeration enumeration = properties.keys(); enumeration.hasMoreElements(); transformer.setParameter(s, s1))
            {
                s = (String) enumeration.nextElement();
                s1 = properties.getProperty(s);
            }

        }
    }

    public static void applyXSL(Document document, Document document1, Document document2) throws TransformerConfigurationException, TransformerException {
        javax.xml.transform.TransformerFactory transformerfactory = javax.xml.transform.TransformerFactory.newInstance();
        javax.xml.transform.Transformer transformer = transformerfactory.newTransformer(new DOMSource(document1));
        transformer.transform(new DOMSource(document), new DOMResult(document2));
    }

    public static boolean applyXSL(Vector vector, String s, String s1, Writer writer) throws TransformerConfigurationException, TransformerException, IOException {
        Document document = xPathExtract(vector, s);
        if (document == null) {
            return false;
        } else {
            applyXSL(document, s, s1, writer);
            return true;
        }
    }

    public static boolean applyXSL(String s, String s1, String s2, Writer writer) throws IOException, SAXException, ParserConfigurationException, TransformerException, FileNotFoundException {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(s);

        return applyXSL(document, s1, s2, writer);
    }

    public static boolean applyXSL(Document document, String s, String s1, Writer writer) throws TransformerException, FileNotFoundException {
        Document document1 = xPathExtract(document, s);
        if (!document1.getDocumentElement().hasChildNodes()) {
            return false;
        } else {
            javax.xml.transform.TransformerFactory transformerfactory = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = transformerfactory.newTransformer(new StreamSource(new FileInputStream(s1)));
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            return true;
        }
    }

    public static void print(Document document, java.io.OutputStream outputstream, boolean flag) throws TransformerException {

        javax.xml.transform.Transformer transformer = javax.xml.transform.TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty("indent", "yes");
        transformer.setOutputProperty("omit-xml-declaration", flag ? "yes" : "no");
        transformer.transform(new DOMSource(document), new StreamResult(outputstream));

    }

    public static void print(Document document, java.io.OutputStream outputstream) throws TransformerException {
        print(document, outputstream, true);
    }

    public static void print(Document document, Writer writer, boolean flag) throws TransformerException {
        javax.xml.transform.Transformer transformer = javax.xml.transform.TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty("indent", "yes");
        transformer.setOutputProperty("omit-xml-declaration", flag ? "yes" : "no");
        transformer.transform(new DOMSource(document), new StreamResult(writer));
    }

    public static void print(Document document, Writer writer) throws TransformerException {
        print(document, writer, true);
    }

    public static void echo(org.w3c.dom.Node node, java.io.PrintStream printstream) {

        short word0 = node.getNodeType();
        switch (word0) {
            default:
                break;

            case 9: // '\t'
                printstream.print("<?xml version=\"1.0\"?>");
                echo(((((Document) node).getDocumentElement())), printstream);
                break;

            case 1: // '\001'
                printstream.print("\n");
                printstream.print("<" + node.getNodeName());
                org.w3c.dom.NamedNodeMap namednodemap = node.getAttributes();

                for (int j = 0; j < namednodemap.getLength(); j++) {
                    org.w3c.dom.Node node1 = namednodemap.item(j);
                    printstream.print(getAttribute(node1));
                }


                Vector vector;
                if ((vector = hasNestedElements((Element) node)) != null) {
                    printstream.print(">");
                    for (int k = 0; k < vector.size(); k++)
                        echo((org.w3c.dom.Node) vector.elementAt(k), printstream);

                    if (vector.size() == 0 && ((org.w3c.dom.Node) vector.elementAt(0)).getNodeType() == 3)
                        printstream.print("</" + node.getNodeName() + ">");
                    else
                        printstream.print("\n</" + node.getNodeName() + ">");
                } else {
                    printstream.print("/>");
                }
                break;

            case 3: // '\003'
                if (!node.getNodeValue().trim().equals(""))
                    printstream.print(node.getNodeValue());
        }
    }

    private static Vector hasNestedElements(Element element) {
        NodeList nodelist = element.getChildNodes();
        Vector vector = new Vector();
        for (int i = 0; i < nodelist.getLength(); i++)
            if (nodelist.item(i).getNodeType() == 3 || nodelist.item(i).getNodeType() == 1)
                vector.addElement(nodelist.item(i));

        return vector.size() != 0 ? vector : null;
    }

    private static String getAttribute(org.w3c.dom.Node node) {
        return "\n " + node.getNodeName() + "=" + "\"" + node.getNodeValue() + "\"";
    }

    public static Vector getVectFromNodeList(NodeList nodelist) {
        Vector vector = new Vector();
        for (int i = 0; i < nodelist.getLength(); i++)
            vector.addElement(nodelist.item(i));

        return vector;
    }

    public static void copy(Element element, Element element1)
            throws Exception {
        javax.xml.transform.Transformer transformer = javax.xml.transform.TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(element), new DOMResult(element1));
    }

    public static String format(String s) {
        String s1 = "&amp;";
        String s2 = "&lt;";
        String s3 = "&gt;";
        String s4 = "&apos;";
        String s5 = "&quot;";
        String s6 = "&#064;";
        String s7 = "";
        for (java.util.StringTokenizer stringtokenizer = new StringTokenizer(s, "&<>'\"@", true); stringtokenizer.hasMoreTokens();)
        {
            String s8 = stringtokenizer.nextToken();
            if (s8.equals("&"))
                s8 = s1;
            if (s8.equals("<"))
                s8 = s2;
            if (s8.equals(">"))
                s8 = s3;
            if (s8.equals("'"))
                s8 = s4;
            if (s8.equals("\""))
                s8 = s5;
            if (s8.equals("@"))
                s8 = s6;
            s7 = s7.concat(s8);
        }

        return s7;
    }


}
