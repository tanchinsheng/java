package tol.ws;

import org.w3c.dom.Document;

import javax.xml.rpc.Call;
import javax.xml.rpc.Service;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceFactory;
import javax.xml.rpc.ParameterMode;
import javax.xml.soap.*;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.net.URL;
import java.util.Iterator;
import java.io.ByteArrayInputStream;

public class WsClient
{
/*
    //--- callByRpc() on Win  ---//
    private static String _endPoint = "http://127.0.0.1:8080/hello-jaxrpc/hello";
    private static String _qnameService = "MyHelloService";
    private static String _qnamePort = "HelloIF";
    private static String _operationName = "sayHello";
    private static String _param = "Daniele";
*/

/*
    //--- callByDocument() on Win ---//
    private static String _endPoint = "http://172.16.17.175/mytestwebservice/service1.asmx";
    private static String _urn = "http://www.st.com";
    private static String _qnameService = "Service1";
    private static String _qnamePort = "Service1Soap";
    private static String _operationName = "FormatHello";
    private static String _param = "Daniele";

*/

    //--- callEpwyOlapService() and callOlapServiceFromXmlDoc() on Win ---//
    private static String _endPoint = "http://172.16.17.53/MsEpwyOlapService/MsEpwyOlapService.asmx";
    private static String _urn = "http://www.st.com";
    private static String _qnameService = "MsEpwyOlapService";
    private static String _qnamePort = "MsEpwyOlapServiceSoap";
    private static String _operationName = "processCube";
    private static String _param = "";
    private static String _index = "0";


/*
    //--- callEpwyOlapService() and callOlapServiceFromXmlDoc() on Unix ---//
    private static String _endPoint = "http://localhost:8080/eproway/servlet/wsServlet";
    private static String _urn = "http://www.st.com";
    private static String _qnameService = "";
    private static String _qnamePort = "";
    private static String _operationName = "processCube";
    private static String _param = "";
*/

    String endPoint;
    String urn;
    String qnameService;
    String qnamePort;
    String operationName;
    String index;

    private static String BODY_NAMESPACE_VALUE ="urn:Foo";
    private static String ENCODING_STYLE_PROPERTY ="javax.xml.rpc.encodingstyle.namespace.uri";
    private static String NS_XSD ="http://www.w3.org/2001/XMLSchema";
    private static String URI_ENCODING ="http://schemas.xmlsoap.org/soap/encoding/";

    public WsClient(
            String endPoint,
            String urn,
            String qnameService,
            String qnamePort,
            String operationName,
            String index
            )
    {
      this.endPoint=endPoint;
      this.urn=urn;
      this.qnameService=qnameService;
      this.qnamePort=qnamePort;
      this.operationName=operationName;
      this.index=index;
    }

    public static void main(String[] args) throws Exception
    {
      WsClient wsc=new WsClient(_endPoint, _urn, _qnameService, _qnamePort, _operationName, _index);
      //System.out.println(wsc.callByRPC());
      //System.out.println(wsc.callByDocument());
      //System.out.println(wsc.callEpwyOlapServiceFromXmlDoc());
      String[] cubeIds = { "binAnalysis" };
      System.out.println(wsc.callEpwyOlapService(0, "", "", "TXTSID074", cubeIds, "BBC"));
    }

    public String call() { return callByRPC(); }

    public String callByRPC()
    {
      System.out.println("Endpoint address = " + endPoint);
      try
      {
        ServiceFactory factory = ServiceFactory.newInstance();
        Service service = factory.createService(new QName(qnameService));
        QName port = new QName(qnamePort);

        Call call = service.createCall(port);
        call.setTargetEndpointAddress(endPoint);

        call.setProperty(Call.SOAPACTION_USE_PROPERTY, new Boolean(true));
        call.setProperty(Call.SOAPACTION_URI_PROPERTY, "http://tempuri.org/FormatHello"); //"http://tempuri.org/FormatHello"
        call.setProperty(ENCODING_STYLE_PROPERTY, URI_ENCODING);
        QName QNAME_TYPE_STRING = new QName(NS_XSD, "string");

        call.setOperationName(new QName(BODY_NAMESPACE_VALUE,operationName));

        call.setReturnType(QNAME_TYPE_STRING);

        call.addParameter("String_1", QNAME_TYPE_STRING, ParameterMode.IN);

        String[] params = { index };
        String result = (String)call.invoke(params);

        System.out.println(result);
        return result;
      }
      catch (Exception ex)
      {
            ex.printStackTrace();
      }
      return null;
    }

    public String callByDocument()
    {
      System.out.println("Endpoint address = " + endPoint);
      try
      {
        Transformer transformer=TransformerFactory.newInstance().newTransformer();

        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();
        MimeHeaders headers = msg.getMimeHeaders();
        headers.addHeader("SOAPAction", "\""+urn+"/"+operationName+"\"");
        SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
        SOAPBody body = env.getBody();
        SOAPBodyElement bodyEl=body.addBodyElement(env.createName(operationName,"",urn+"/"));
        SOAPElement index=bodyEl.addChildElement(env.createName("HelloName"));
        index.addTextNode(this.index);
        msg.saveChanges();

          //--- REQUEST DEBUG PRINT ---//
          System.out.println("\nREQUEST:\n");
          transformer.transform(msg.getSOAPPart().getContent(), new StreamResult(System.out));

        SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
        SOAPConnection conn = scf.createConnection();
        SOAPMessage response = conn.call(msg,new URL(this.endPoint));

          //--- RESPONSE DEBUG PRINT ---//
          System.out.println("\nRESPONSE:\n");
          transformer.transform(response.getSOAPPart().getContent(), new StreamResult(System.out));

        conn.close();
        return "OK";
      }
      catch (Exception ex)
      {
            ex.printStackTrace();
      }
      return null;
    }


    public String callEpwyOlapService(  int connIndex,
                                        String userName,
                                        String userEmail,
                                        String sID,
                                        String[] cubeIDs,
                                        String appID
                                     ) throws Exception
    {
      //System.out.println("Endpoint address = " + endPoint);
      try
      {
        Transformer transformer=TransformerFactory.newInstance().newTransformer();
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();
        MimeHeaders headers = msg.getMimeHeaders();
        headers.addHeader("SOAPAction", "\""+urn+"/"+operationName+"\"");
        SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
        SOAPBody body = env.getBody();
        SOAPBodyElement bodyEl=body.addBodyElement(env.createName(operationName,"",urn+"/"));
        SOAPElement el=bodyEl.addChildElement(env.createName("sessionID"));
        el.addTextNode(sID);
        el=bodyEl.addChildElement(env.createName("cubeIDs"));
        for (int i=0; i<cubeIDs.length; i++)
        {
            el.addChildElement("string").addTextNode(cubeIDs[i]);
        }
        el=bodyEl.addChildElement(env.createName("appID"));
        el.addTextNode(appID);
        el=bodyEl.addChildElement(env.createName("nIndex"));
        el.addTextNode(new Integer(connIndex).toString());
        el=bodyEl.addChildElement(env.createName("sUser"));
        el.addTextNode(userName != null ? userName : "");
        el=bodyEl.addChildElement(env.createName("sEmail"));
        el.addTextNode(userEmail != null ? userEmail : "");

        msg.saveChanges();

          //--- REQUEST DEBUG PRINT ---//
          //System.out.println("\nREQUEST:\n");
          //transformer.transform(msg.getSOAPPart().getContent(), new StreamResult(System.out));

        SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
        SOAPConnection conn = scf.createConnection();
        SOAPMessage response = conn.call(msg,new URL(this.endPoint));

          //--- RESPONSE DEBUG PRINT ---//
          //System.out.println("\nRESPONSE:\n");
          //transformer.transform(response.getSOAPPart().getContent(), new StreamResult(System.out));

        bodyEl=(SOAPBodyElement)response.getSOAPPart().getEnvelope().getBody().getChildElements().next();
        Iterator it=bodyEl.getChildElements();
        String ret=(it.hasNext() ? ((SOAPElement)it.next()).getValue() : null);
        conn.close();
        return ret;
      }
      catch (Exception ex)
      {
            throw ex;
      }
    }


    public String callEpwyOlapServiceFromXmlDoc()
    {
      System.out.println("Endpoint address = " + endPoint);
      Document document=null;
      String s="<processCube xmlns=\"http://www.st.com/\">"+
               "<sessionID>TXTSID074-0A1</sessionID>" +
               "<cubeIDs><string>binAnalysis</string></cubeIDs>" +
               "<appID>BBC</appID>" +
               "</processCube>";
      try
      {
          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
          factory.setNamespaceAware(true);
          DocumentBuilder builder = factory.newDocumentBuilder();
          document = builder.parse(new ByteArrayInputStream(s.getBytes()));
      }
      catch(Exception e)
      {
          System.out.println("ERROR PARSING XML DOCUMENT");
      }
      try
      {
        Transformer transformer=TransformerFactory.newInstance().newTransformer();
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage msg = mf.createMessage();
        MimeHeaders headers = msg.getMimeHeaders();
        headers.addHeader("SOAPAction", "\""+urn+"/"+operationName+"\"");
        SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
        SOAPBody body = env.getBody();
        body.addDocument(document);

        msg.saveChanges();

          //--- REQUEST DEBUG PRINT ---//
          System.out.println("\nREQUEST:\n");
          transformer.transform(msg.getSOAPPart().getContent(), new StreamResult(System.out));

        SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
        SOAPConnection conn = scf.createConnection();
        SOAPMessage response = conn.call(msg,new URL(this.endPoint));

          //--- RESPONSE DEBUG PRINT ---//
          System.out.println("\nRESPONSE:\n");
          //transformer.transform(response.getSOAPPart().getContent(), new StreamResult(System.out));

        SOAPBodyElement bodyEl=(SOAPBodyElement)response.getSOAPPart().getEnvelope().getBody().getChildElements().next();
        Iterator it=bodyEl.getChildElements();
        String ret=(it.hasNext() ? ((SOAPElement)it.next()).getValue() : null);
        conn.close();
        return ret;
      }
      catch (Exception ex)
      {
            ex.printStackTrace();
      }
      return null;
    }


}

