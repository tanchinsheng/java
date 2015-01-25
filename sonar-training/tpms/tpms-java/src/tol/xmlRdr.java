package tol;

import java.util.*;
import java.io.*;
//DOM PARSERS
import javax.xml.parsers.*;
//import com.sun.xml.parser.*;
//import com.sun.xml.tree.*;
import org.w3c.dom.*;
import org.xml.sax.helpers.*;
import org.xml.sax.XMLReader;

import org.w3c.dom.traversal.NodeIterator;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.TextImpl;
//XSL TRANSFORMERS
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.sax.*;

//XPATH EXTRACTOR
import org.apache.xpath.XPathAPI;

/**
 * Manages XML file parsing and XSL transformations
 */
public class xmlRdr
{
  //----------------------------------------------------//
  //------ STATIC METHODS (XML PARSING UTILITIES) ------//
  //----------------------------------------------------//

/**
 * Given an XML filename returns its root element.
 *
 * @param filename the XML filename
 * @param validBool if true performs validation (not working at the moment)
 */
  public static Element getRoot(String flname, boolean validBool) throws Exception
  {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    //dbFactory.setNamespaceAware(true);
    //dbFactory.setValidating(validBool);
    //if validBool==true, a DTD must be provided, otherwise an exception is thrown
    DocumentBuilder db = dbFactory.newDocumentBuilder();
    Element root = db.parse(flname).getDocumentElement();
    return root;
  }

/**
 * Given an XML filename returns its root element.
 *
 * @param filename is the Input Stream to read the DOM object from
 * @param validBool if true performs validation (not working at the moment)
 */
  public static Element getRoot(InputStream iStr, boolean validBool) throws Exception
  {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    //dbFactory.setNamespaceAware(true);
    //dbFactory.setValidating(validBool);
    //if validBool==true, a DTD must be provided, otherwise an exception is thrown
    DocumentBuilder db = dbFactory.newDocumentBuilder();
    Element root = db.parse(iStr).getDocumentElement();
    return root;
  }

  public static Document getNewDoc(String rootTag) throws Exception
  {
   DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
   DocumentImpl doc=(DocumentImpl)db.newDocument();
   Element elRoot= new ElementImpl((CoreDocumentImpl)doc, rootTag);
   doc.appendChild(elRoot);
   return doc;
  }

/**
 * Adds a new element to the <i>elFather</i> element.
 */
  public static Element addEl(Element elFather, String elSonTag)
  {
   DocumentImpl doc=(DocumentImpl)elFather.getOwnerDocument();
   Element elSon= new ElementImpl((CoreDocumentImpl)doc, elSonTag);
   elFather.appendChild(elSon);
   return elSon;
  }

/**
 * Adds a new element to the <i>elFather</i>. This new element's
 * content is set to <i>elSonVal</i>.
 * A text node having <i>elSonVal</i> as its content
 * will be set up as the new element content.
 */
  public static Element addEl(Element elFather, String elSonTag, String elSonVal)
  {
   Element elSon=addEl(elFather, elSonTag);
   TextImpl txt=new TextImpl((DocumentImpl)elFather.getOwnerDocument(), elSonVal);
   elSon.appendChild(txt);
   return elSon;
  }

/**
 * the the value for a simple Element (ie: for an
 * elment which is either empty or contains a text node child only).
 *
 * @throws - Exception if the element is a complex one.
 */
 public static void setVal(Element el, String s) throws Exception
 {
   Node sonEl;
   if ((sonEl=el.getFirstChild())==null)
   {
     TextImpl txt=new TextImpl((DocumentImpl)el.getOwnerDocument(), s);
     el.appendChild(txt);
   }
   else
   {
     if (sonEl instanceof Text)
     {
       ((Text)sonEl).setData(s);
     }
     else throw new Exception("ATTEMPT TO SET THE VALUE IN A COMPLEX ELEMENT");
   }
 }

/**
 * the the value for the sonEl chile of the el Element (ie: for an
 * elment which is either empty or contains a text node child only).
 *
 * @throws - Exception if the element is a complex one.
 */
 public static void setVal(Element el, String elType, String s) throws Exception
 {
   el=xmlRdr.getChild(el, elType);
   Node sonEl;
   if ((sonEl=el.getFirstChild())==null)
   {
     TextImpl txt=new TextImpl((DocumentImpl)el.getOwnerDocument(), s);
     el.appendChild(txt);
   }
   else
   {
     if (sonEl instanceof Text)
     {
       ((Text)sonEl).setData(s);
     }
     else throw new Exception("ATTEMPT TO SET THE VALUE IN A COMPLEX ELEMENT");
   }
 }

/**
 * Returns the content of the Element (which is the text
 * contained between the element tags)
 *
 * @param el the element to be accessed
 */
  public static String getVal(Node el) throws Exception
  {
   return getVal((Element)el);
  }


/**
 * That's the element version of {@link #getVal(Node)}
 */
  public static String getVal(Element el) throws Exception
  {
   return (el!=null ? el.getFirstChild().getNodeValue().trim() : null);
  }

/**
 * returns the content of the child-element of type <i>elType</i> contained
 * into the element <i>el</i>.
 * If there are more than one matching child-node, it consider the first one
 * only.
 *
 * @param elType element type to retrieve
 * @param el     input element
 * @return null if no matching child-element exist
 */
  public static String getVal(Element el, String elType) throws Exception
  //returns null if there's no matching element
  {
   NodeList lst=el.getElementsByTagName(elType);
   if (lst.getLength()==0) return null;
   Element tNode = (Element)lst.item(0);
   if (tNode.hasChildNodes()) return tNode.getFirstChild().getNodeValue().trim();
   else return null;
  }


/**
 * It's the same as {@link #getVal(Element, String)} but it returns
 * the matching child-element itself.
 *
 * @return null if no matching child-element exists
 */
  public static Element getChild(Element el, String elType) throws Exception
  {
   NodeList lst=el.getElementsByTagName(elType);
   if (lst.getLength()==0) return null;
   return (Element)lst.item(0);
  }

/**
 * It's the same as {@link #getVal(Element, String)} but it
 * take into account only direct children of the input element
 *
 * @return null if no matching child-element exists
 */
  public static Element getDirChild(Element el, String elType) throws Exception
  {
   NodeList lst=el.getElementsByTagName(elType);
   if (lst.getLength()==0) return null;
   else
   {
     for (int i=0; i<lst.getLength(); i++)
     {
       if (((Element)lst.item(i)).getParentNode()==(Node)el) return (Element)lst.item(i);
     }
     return null;
   }
  }

/**
 * Returns the first element satisfying the following
 * condition: exist a son element whose name is <i>sL</i> and whose value is <i>sR.</i>.
 * Note: this method returns the first matching element
 *
 * @param sL left value string
 * @param sR right value string
 * @return null if no matching el is found
 */
  public static Element findEl(NodeList nL, String sL, String sR) throws Exception
  {
    Vector vL=new Vector(); vL.addElement(sL);
    Vector vR=new Vector(); vR.addElement(sR);
    return findEl(nL, vL, vR);
  }

/**
 * It's the same as {@link #findEl(NodeList, String, String)} but it can
 * get a multiple condition as input.
 *
 * @param sL the list of left values
 * @param sR the list of right values
 */
  public static Element findEl(NodeList nL, Vector vL, Vector vR) throws Exception
  {
   Element nFather=null;
   for (int i=0; i<nL.getLength(); i++)
   { Node node = nL.item(i);
     if (node instanceof Element) nFather = (Element)node;
     else continue;
     boolean matchBool = true;
     for (int k=0; k<vL.size(); k++)
     {
       NodeList nSons = nFather.getElementsByTagName((String)vL.elementAt(k));
       String rVal = nSons.item(0).getFirstChild().getNodeValue();
       if (!rVal.equals((String)vR.elementAt(k))) {matchBool = false; break; }
     }
     if (matchBool) return nFather;
   }
   //get here if no matches occur
   return null;
  }


/**
 * Returns the first element satisfying the following
 * condition: exist an attribute whose name is <i>sL</i> and whose value is <i>sR.</i>.
 * Note: this method returns the first matching element
 *
 * @param sL left value string
 * @param sR right value string
 * @return null if no matching el is found
 */
  public static Element findElwithAttr(NodeList nL, String sL, String sR) throws Exception
  {
    Vector vL=new Vector(); vL.addElement(sL);
    Vector vR=new Vector(); vR.addElement(sR);
    return findElwithAttr(nL, vL, vR);
  }


/**
 * It's the same as {@link #findEl(NodeList, String, String)} but it can
 * get a multiple condition as input.
 *
 * @param sL the list of left values
 * @param sR the list of right values
 */
  public static Element findElwithAttr(NodeList nL, Vector vL, Vector vR) throws Exception
  {
   for (int i=0; i<nL.getLength(); i++)
   {
     Element node=(nL.item(i) instanceof Element ? (Element)nL.item(i) : null);
     if  (node==null) continue;
     boolean matchBool = true;
     for (int k=0; k<vL.size(); k++)
     {
       String attrName=(String)vL.elementAt(k);
       if (!node.getAttribute(attrName).equals((String)vR.elementAt(k)))
       {
         matchBool = false; break;
       }
     }
     if (matchBool) return node;
   }
   //get here if no matches occur
   return null;
  }

/**
 * Returns the list of elements satisfying the following
 * condition: exist a son element whose name is <i>sL</i> and whose value is <i>sR.</i>.
 * Note: this method returns the first matching element
 *
 * @param sL left value string
 * @param sR right value string
 * @return null if no matching el is found
 */
  public static Vector findEls(NodeList nL, String sL, String sR) throws Exception
  {
    Vector vL=new Vector(); vL.addElement(sL);
    Vector vR=new Vector(); vR.addElement(sR);
    return findEls(nL, vL, vR);
  }

/**
 * It's the same as the previous method, bt it can get a multiple condition as
 * input.
 *
 * @param sL the list of left values
 * @param sR the list of right values
 */
  public static Vector findEls(NodeList nL, Vector vL, Vector vR) throws Exception
  //overloaded: see previous method
  {
   Vector vRet = null;
   Element nFather=null;
   Vector vTemp = new Vector();
   for (int i=0; i<nL.getLength(); i++)
   {
     Node node = nL.item(i);
     if (node instanceof Element) nFather = (Element)node;
     else continue;
     boolean matchBool = true;
     for (int k=0; k<vL.size(); k++)
     {
       if (!((String)vL.elementAt(k)).startsWith("@"))
       {
           NodeList nSons = nFather.getElementsByTagName((String)vL.elementAt(k));
           String rVal = nSons.item(0).getFirstChild().getNodeValue();
           if (!rVal.equals((String)vR.elementAt(k))) {matchBool = false; break;}
       }
       else
       {
           String attrName=((String)vL.elementAt(k)).substring(1);
           if (!(nFather.getAttribute(attrName)).equals((String)vR.elementAt(k))) {matchBool = false; break;}
       }
     }
     if (matchBool) (vRet=vTemp).addElement(nFather);
   }
   //get here if no matches occur
   return vRet;
  }


//----------------------------------//
//--- XPATH AND XSL QUERY ENGINE ---//
//----------------------------------//

/**
 * Scan the input files (gets an input files list in input) in order to find
 * elements matching a given xPath query string.
 * Note: it assumes the eventual matching elements are placed within a
 * single file only
 *
 * @param  fNames the list of fileNames
 * @param  xpath the XPath qyery string
 * @return the Document obtained applying the XPath string to the input files -
           null if it can't find any matching elements into any of the input
 *         files
 * @see #xPathExtract(Document, String)
 */
  public static Document xPathExtract(Vector fNames, String xpath) throws Exception
  {
   Document doc;
   for (int i=0; i<fNames.size(); i++)
   {
      try{doc=xPathExtract((String)fNames.elementAt(i), xpath);}
      catch(Exception e) {continue;}
      if (doc.getDocumentElement().hasChildNodes()) return doc;
   }
   return null;
  }

/**
 * It's the same as {@link #xPathExtract(Vector, String)} but it scan a given
 * file only.
 */
  public static Document xPathExtract(String filename, String xpath) throws Exception
  {
      Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream(filename));
      return xPathExtract(doc, xpath);
  }

/**
 * Scan a DOM object in order to find
 * elements matching a given xPath query string.
 * <br>Note: The query string must select first level elements, ie: elements
 *       which are direct sons of the root elements<br>
 * <code><pre>
 *       Ex: Look at this sample document:
 *       &lt;joins&gt;
 *         &lt;join&gt;&lt;id&gt;1&lt;/id&gt;&lt;tablea&gt;EMP&lt;/tablea&gt;&lt;/join&gt;
 *         &lt;join&gt;&lt;id&gt;2&lt;/id&gt;&lt;tablea&gt;DEP&lt;/tablea&gt;&lt;/join&gt;
 *         &lt;join&gt;&lt;id&gt;3&lt;/id&gt;&lt;tablea&gt;CASH&lt;/tablea&gt;&lt;/join&gt;
 *       &lt;/joins&gt;
 *       </code>
 *
 *      A valid sample query string would be:
 *      /joins/join[id='1']
 *
 *      The following query string on the other hand would be incorrect:
 *      /joins/join/tablea
 * </pre></code>
 *
 * @param  xpath the XPath qyery string
 * @return the input Document from which the non-matching elements
 *         have been deleted
 */
  public static Document xPathExtract(Document doc, String xpath) throws Exception
  {
      // Use the simple XPath API to select a nodeIterator.
      NodeIterator nl = XPathAPI.selectNodeIterator(doc, xpath);
      Node newRoot = doc.getDocumentElement().cloneNode(false);
      Node n;
      while ((n = nl.nextNode())!= null) newRoot.appendChild(n);
      doc.removeChild(doc.getDocumentElement());
      doc.appendChild(newRoot);
      return doc;
  }

/**
 * Applies the <i>xslName</i> stylesheet to the <i>xmlName</i> document, printing
 * the resulting document to the <i>out</i> Writer.
 *
 * @param xslName the XSL file name
 * @param xmlName the XML file name
 */
  public static void applyXSL(String xmlName, String xslName, Writer out) throws Exception
  {
    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlName);
    applyXSL(doc, xslName, out);
  }

/**
 * The same as {@link #applyXSL(String, String, Writer)} but it accepts a DOM object as
 * input instead of an XML file name
 *
 */
  public static void applyXSL(Document n, String xslName, Writer out) throws Exception
  {applyXSL(n, xslName, (Properties)null, out);}

  public static void applyXSL(Document n, String xslName, Properties props, Writer out) throws Exception
  {
    TransformerFactory tFactory = TransformerFactory.newInstance();
    Transformer transformer = tFactory.newTransformer(new StreamSource(new FileInputStream(xslName)));
    if (props!=null)
    {
      setParamFromProps(transformer, props);
    }
    transformer.transform(new DOMSource(n), new StreamResult(out));
  }

/**
 * The same as {@link #applyXSL(String, String, Writer)} but it accepts a DOM object as
 * input instead of an XML file name
 *
 */
  public static void applyXSL(Document n, String xslName, Document outXml) throws Exception
  {applyXSL(n, xslName, null, outXml);}

  public static void applyXSL(Document n, String xslName, Properties props, Document outXml) throws Exception
  {
    TransformerFactory tFactory = TransformerFactory.newInstance();
    Transformer transformer = tFactory.newTransformer(new StreamSource(new FileInputStream(xslName)));
    if (props!=null)
    {
      setParamFromProps(transformer, props);
    }
    transformer.transform(new DOMSource(n), new DOMResult(outXml));
  }

/**
 * The same as {@link #applyXSL(String, String, Writer)} but it accepts a DOM object as
 * input instead of an XML file name
 *
 */
  public static void applyXSL(Document n, Document xslDoc, Writer out) throws Exception
  {
    TransformerFactory tFactory = TransformerFactory.newInstance();
    Transformer transformer = tFactory.newTransformer(new DOMSource(xslDoc));
    transformer.transform(new DOMSource(n), new StreamResult(out));
  }

/**
 * The same as {@link #applyXSL(String, String, Writer)} but it accepts a DOM object as
 * input instead of an XML file name
 *
 */
  public static void applyXSL(Element n, Document xslDoc, Element outXml) throws Exception
  {
    TransformerFactory tFactory = TransformerFactory.newInstance();
    Transformer transformer = tFactory.newTransformer(new DOMSource(xslDoc));
    transformer.transform(new DOMSource(n), new DOMResult(outXml));
  }

/**
 * Applys an xsl to an XML doc and return the tranfsormed XML doc
 */
  public static Document transform(Document n, Document xslDoc) throws Exception {return transform(n, xslDoc, null);}

  public static Document transform(Document n, Document xslDoc, Properties props) throws Exception
  {
    TransformerFactory tFactory = TransformerFactory.newInstance();
    Transformer transformer = tFactory.newTransformer(new DOMSource(xslDoc));
    DOMResult outDoc=new DOMResult();
    setParamFromProps(transformer, props);
    transformer.transform(new DOMSource(n), outDoc);
    return (Document)outDoc.getNode();
  }


/**
 * Applys an xsl to an XML doc and return the tranfsormed XML doc
 */
  public static Document transform(Document n, String xslName) throws Exception {return transform(n, xslName, null);}

  public static Document transform(Document n, String xslName, Properties props) throws Exception
  {
    TransformerFactory tFactory = TransformerFactory.newInstance();
    Transformer transformer = tFactory.newTransformer(new StreamSource(new FileInputStream(xslName)));
    setParamFromProps(transformer, props);
    DOMResult outDoc=new DOMResult();
    transformer.transform(new DOMSource(n), outDoc);
    return (Document)outDoc.getNode();
  }


  public static Document transform(String xmlName, String xslName, Writer out) throws Exception {return transform(xmlName, xslName, null, out);}

  public static Document transform(String xmlName, String xslName, Properties props, Writer out) throws Exception
  {
    SAXTransformerFactory saxTFactory = ((SAXTransformerFactory) TransformerFactory.newInstance());
    TransformerHandler tHandler1 = saxTFactory.newTransformerHandler(new StreamSource(xslName));
    setParamFromProps(tHandler1.getTransformer(), props);
    tHandler1.setResult(new StreamResult(out));
    XMLReader reader = XMLReaderFactory.createXMLReader();
    reader.setContentHandler(tHandler1);
    reader.parse(xmlName);
    return null;
  }

  public static Document transform(Document n, String xslName, Properties props, Writer out) throws Exception
  {
    TransformerFactory tFactory = TransformerFactory.newInstance();
    Transformer transformer = tFactory.newTransformer(new StreamSource(new FileInputStream(xslName)));
    setParamFromProps(transformer, props);
    transformer.transform(new DOMSource(n), new StreamResult(out));
    return null;
  }


/**
 * Transforms the input XML file according to a pipe (vector) of XSL files. Remark that
 * the XSL files vector has got a properties vector associates. If no property setting is
 * necessary for a specific XSL, its associated elemenet within <i>propsVect</i> can be
 * <i>null</i>.
 */
  public static Document transform(String xmlName, Vector xslNmsVect, Vector propsVect, Writer out) throws Exception
  {
    DOMResult doc=null;
    SAXTransformerFactory saxTFactory = ((SAXTransformerFactory) TransformerFactory.newInstance());
    TransformerHandler oldHandler=null, firstHandler=null, tHandler=null;
    int i;
    for (i=0; i<xslNmsVect.size(); i++)
    {
      tHandler = saxTFactory.newTransformerHandler(new StreamSource((String)xslNmsVect.elementAt(i)));
      setParamFromProps(tHandler.getTransformer(), (Properties)propsVect.elementAt(i));
      if (i==0)
      {
        firstHandler=tHandler;
      }
      else
      {
        oldHandler.setResult(new SAXResult(tHandler));
      }
      oldHandler=tHandler;
    }
    if (tHandler!=null)
    {
      if (out!=null) tHandler.setResult(new StreamResult(out));
      else
      {
        doc=new DOMResult();
        tHandler.setResult(doc);
      }
      XMLReader reader = XMLReaderFactory.createXMLReader();
      reader.setContentHandler(firstHandler);
      reader.parse(xmlName);
    }
    return (doc!=null ? (Document)doc.getNode() : null);
  }

  protected static void setParamFromProps(Transformer transformer, Properties props)
  {
    if (props!=null)
    {
       for (Enumeration e = props.keys() ; e.hasMoreElements() ;)
       {
         String name=(String)e.nextElement();
         String val=(String)props.getProperty(name);
         transformer.setParameter(name, val);
       }
    }
  }

/**
 * The same as {@link #applyXSL(String, String, Writer)} but it accepts a DOM object as
 * input instead of an XML file name
 *
 */
  public static void applyXSL(Document n, Document xslDoc, Document outXml) throws Exception
  {
    TransformerFactory tFactory = TransformerFactory.newInstance();
    Transformer transformer = tFactory.newTransformer(new DOMSource(xslDoc));
    transformer.transform(new DOMSource(n), new DOMResult(outXml));
  }

/**
 * It's the same as {@link #applyXSL(String, String, String, Writer)} but
 * this is the multi-files version.
 * This method select the matching elements from the files listed into
 * the <i>fNames</i> vector.
 * Note: it assumes the eventual matching elements are placed within a
 * single file only
 *
 * @param fNames the list of the input file names
 * @see #xPathExtract(Document, String)
 * @return false if there are no matching elements into
 *         any of the input files
 */
  public static boolean applyXSL(Vector fNames, String xPathQry, String xslName, Writer out) throws Exception
  {
    Document doc=xPathExtract(fNames, xPathQry);
    if (doc==null) return false;
    else
    {
      applyXSL(doc, xPathQry, xslName, out);
      return true;
    }
  }

/**
 * It's the same as {@link #applyXSL(String, String, Writer)} but before
 * applying the stylesheet it filters the input XML document selecting
 * the elements matching the <i>xpath</i> query string.
 *
 * @see #xPathExtract(Document, String)
 * @return false if there are no matching elements into the input XML document
 */
  public static boolean applyXSL(String xmlName, String xPathQry, String xslName, Writer out) throws Exception
  {
    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlName);
    return applyXSL(doc, xPathQry, xslName, out);
  }

/**
 * The same as {@link #applyXSL(String, String, String, Writer)} but it gets
 * a DOM object as input (instead of an XML file name).
 *
 */
  public static boolean applyXSL(Document n, String xPathQry, String xslName, Writer out) throws Exception
  //returns true if it founds at least one matching record
  {
    Document outDoc=xPathExtract(n,xPathQry);
    if (!outDoc.getDocumentElement().hasChildNodes()) return false;
    TransformerFactory tFactory = TransformerFactory.newInstance();
    Transformer transformer = tFactory.newTransformer(new StreamSource(new FileInputStream(xslName)));
    transformer.transform(new DOMSource(n), new StreamResult(out));
    return true;
  }

 /**
  * Prints the content of a Dom object into an Output Stream.
  */
  public static void print(Document doc, OutputStream out, boolean omitDeclarBool) throws Exception
  {
   Transformer transformer = TransformerFactory.newInstance().newTransformer();
   transformer.setOutputProperty(OutputKeys.INDENT,"yes");
   transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,(omitDeclarBool ? "yes" : "no"));
   transformer.transform(new DOMSource(doc), new StreamResult(out));
  }

  public static void print(Document doc, OutputStream out) throws Exception
  {print(doc, out, true);}

 /**
  * Prints the content of a Dom object into an Output Stream.
  */
  public static void print(Document doc, Writer out, boolean omitDeclarBool) throws Exception
  {
   Transformer transformer = TransformerFactory.newInstance().newTransformer();
   transformer.setOutputProperty(OutputKeys.INDENT,"yes");
   transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,(omitDeclarBool ? "yes" : "no"));
   transformer.transform(new DOMSource(doc), new StreamResult(out));
  }

  public static void print(Document doc, Writer out) throws Exception
  {print(doc, out, true);}

  public static void echo(Node n, PrintStream out)
  {
    // Indent to the current level before printing anything
    //outputIndentation();
    int indent=0;
    int type = n.getNodeType();
    switch (type)
    {
        case Node.DOCUMENT_NODE:
            out.print("<?xml version=\"1.0\"?>");
            echo(((Document)n).getDocumentElement(), out) ;
            break;
        case Node.ELEMENT_NODE:
            out.print("\n");
            out.print("<"+n.getNodeName());

            // Print attributes if any.  Note: element attributes are not
            // children of ELEMENT_NODEs but are properties of their
            // associated ELEMENT_NODE.  For this reason, they are printed
            // with 2x the indent level to indicate this.
            NamedNodeMap atts = n.getAttributes();
            indent += 2;
            for (int i = 0; i < atts.getLength(); i++) {
                Node att = atts.item(i);
                out.print(getAttribute(att));
            }
            indent -= 2;
            Vector sons;
            if ((sons=hasNestedElements((Element)n))!=null)
            {
               out.print(">");
               for (int j=0; j<sons.size(); j++) echo((Node)sons.elementAt(j), out);
               if ((sons.size()==0)&&(((Node)sons.elementAt(0)).getNodeType()==Node.TEXT_NODE))
                  out.print("</"+n.getNodeName()+">");
               else
                  out.print("\n"+"</"+n.getNodeName()+">");
            }
            else
            {
              out.print("/>");
            }
            break;
        case Node.TEXT_NODE:
            if (!n.getNodeValue().trim().equals("")) out.print(n.getNodeValue());
            return;
    }
  }

  private static Vector hasNestedElements(Element el)
  {
      NodeList l=el.getChildNodes();
      Vector v=new Vector();
      for (int i=0; i<l.getLength(); i++)
      {
        if ((l.item(i).getNodeType()==Node.TEXT_NODE)||(l.item(i).getNodeType()==Node.ELEMENT_NODE))
        {
          v.addElement(l.item(i));
        }
      }
      return (v.size()==0 ? null : v);
  }

  private static String getAttribute(Node att)
  {
    return new String("\n "+att.getNodeName()+"="+"\""+att.getNodeValue()+"\"");
  }

  public static Vector getVectFromNodeList(NodeList l)
  {
    Vector v=new Vector();
    for (int i=0; i<l.getLength(); i++) v.addElement(l.item(i));
    return v;
  }

  /*
  public static Node getDom(InputStream iStr) throws Exception
  {
    DOMResult outDom=new DOMResult();
    Transformer transformer=TransformerFactory.newInstance().newTransformer();
    transformer.transform(new StreamSource(iStr), outDom);
    return outDom.getNode();
  }
  */

  public static void copy(Element src, Element dest) throws Exception
  {
    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.transform(new DOMSource(src), new DOMResult(dest));
  }

/**
 * Replaces special characters with proper entities reference.
 */
 public static String format(String s)
 {
   String ampStr  = "&amp;";
   String ltStr   = "&lt;";
   String gtStr   = "&gt;";
   String aposStr = "&apos;";
   String qtStr   = "&quot;";
   String etStr   = "&#064;";


   String out=new String();
   java.util.StringTokenizer stok=new java.util.StringTokenizer(s,"&<>'\"@",true);
   while (stok.hasMoreTokens())
   {
     String tok=stok.nextToken();
     if (tok.equals("&"))  tok=ampStr;
     if (tok.equals("<"))  tok=ltStr;
     if (tok.equals(">"))  tok=gtStr;
     if (tok.equals("'"))  tok=aposStr;
     if (tok.equals("\"")) tok=qtStr;
     if (tok.equals("@"))  tok=etStr;

     out=out.concat(tok);
   }
   return out;
 }

  //------------------//
  //------ MAIN ------//
  //------------------//

  public static void main(String[] args) throws Exception
  {
    Element joins=xmlRdr.getRoot("d:/jakarta-tomcat-3.2.3/webapps/eproway/cfg/db_int/TDB00/joins.xml", false);
    Vector vl=new Vector(); Vector vr=new Vector();
    vl.addElement("DBTABLEA"); vr.addElement("TDB_REGISTER_WL");
    vl.addElement("DBTABLEB"); vr.addElement("TDB_JOB_BIN");
    vl.addElement("@hidden"); vr.addElement("");
    Vector v=xmlRdr.findEls(joins.getElementsByTagName("JOIN"),vl,vr);
    if (v==null) return;
    for (int i=0; i<v.size(); i++)
    {
        System.out.println(xmlRdr.getVal((Element)v.elementAt(i),"JOIN_ID"));
    }
  }

  public static void main3(String[] args) throws Exception
  {
    //--- THIS SHOWS HOW TO PIPE XSL TRANSFORMATIONS RESORTING TO SAX APIS ---//
    SAXTransformerFactory saxTFactory = ((SAXTransformerFactory) TransformerFactory.newInstance());
    TransformerHandler tHandler1 = saxTFactory.newTransformerHandler(new StreamSource("Z:/Backups/CCAM-framework/templates/epwy/sort.xsl"));
    tHandler1.getTransformer().setParameter("sortFn1","YIELD");
    tHandler1.getTransformer().setParameter("sortord1","descending");
    tHandler1.getTransformer().setParameter("sortTp1","number");
    TransformerHandler tHandler2 = saxTFactory.newTransformerHandler(new StreamSource("Z:/Backups/CCAM-framework/templates/epwy/report-csv.xsl"));
    tHandler1.setResult(new SAXResult(tHandler2));
    tHandler2.setResult(new StreamResult(new FileOutputStream("Z:/Backups/CCAM-framework/data/out.txt")));
    XMLReader reader = XMLReaderFactory.createXMLReader();
    reader.setContentHandler(tHandler1);
    reader.parse("Z:/Backups/CCAM-framework/data/report.xml");
    /* InvokationTargetException is raised when the referenced XSL document doesn't exist */
  }

  public static void main2(String[] args) throws Exception
  {
    //--- THIS SHOWS HOW TO PIPE XSL TRANSFORMATIONS RESORTING TO SAX APIS ---//
    Vector xslVect=new Vector(), propsVect=new Vector();
    xslVect.addElement("Z:/Backups/CCAM-framework/templates/epwy/top.xsl");
    Properties props=new Properties();
    props.setProperty("lastRow","3");
    propsVect.addElement(props);
    xslVect.addElement("Z:/Backups/CCAM-framework/templates/epwy/projection.xsl");
    props=new Properties();
    props.setProperty("fld01","LOT");
    props.setProperty("fld02","PRODUCT");
    propsVect.addElement(props);
    xslVect.addElement("Z:/Backups/CCAM-framework/templates/epwy/report-csv.xsl");
    propsVect.addElement(null);
    //xmlRdr.transform("Z:/Backups/CCAM-framework/data/report.xml", xslVect, propsVect, new PrintWriter(System.out));
  }
}
