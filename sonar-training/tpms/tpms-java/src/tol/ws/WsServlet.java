package tol.ws;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.rpc.Call;
import javax.xml.rpc.Service;
import javax.xml.rpc.JAXRPCException;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceFactory;
import javax.xml.rpc.ParameterMode;
import javax.xml.soap.*;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import java.net.URL;
import java.util.*;
import java.io.*;


/**
 * Created by IntelliJ IDEA.
 * User: Colecchia
 * Date: Dec 11, 2003
 * Time: 5:15:32 PM
 * To change this template use Options | File Templates.
 */
public class WsServlet extends HttpServlet
{
    MessageFactory fac = null;

    public void init(ServletConfig servletConfig) throws ServletException
    {
        super.init(servletConfig);
        try
        {
            fac = MessageFactory.newInstance();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        System.out.println("DO-POST>"+(fac!=null));
        try {
            // Get all the headers from the HTTP request
            MimeHeaders headers = getHeaders(req);

            // Get the body of the HTTP request
            InputStream is = req.getInputStream();

            // Now internalize the contents of the HTTP request
            // and create a SOAPMessage
            SOAPMessage msg = fac.createMessage(headers, is);

            SOAPMessage reply = null;
            reply = onMessage(msg);

            if (reply != null) {

                /*
                 * Need to call saveChanges because we're
                 * going to use the MimeHeaders to set HTTP
                 * response information. These MimeHeaders
                 * are generated as part of the save.
                 */
                if (reply.saveRequired()) {
                    reply.saveChanges();
                }

                resp.setStatus(HttpServletResponse.SC_OK);
                putHeaders(reply.getMimeHeaders(), resp);

                // Write out the message on the response stream
                OutputStream os = resp.getOutputStream();
                reply.writeTo(os);
                os.flush();
            }
            else
            {
                resp.setStatus(
                HttpServletResponse.SC_NO_CONTENT);
            }
        }
        catch (Exception ex)
        {
            throw new ServletException("SAAJ POST failed: " +
            ex.getMessage());
        }
    }

    static MimeHeaders getHeaders(HttpServletRequest req)
    {
        Enumeration Enum = req.getHeaderNames();
        MimeHeaders headers = new MimeHeaders();

        while (Enum.hasMoreElements()) {
            String headerName = (String)Enum.nextElement();
            String headerValue = req.getHeader(headerName);

            StringTokenizer values =
                new StringTokenizer(headerValue, ",");
            while (values.hasMoreTokens()) {
                headers.addHeader(headerName,
                    values.nextToken().trim());
            }
        }
        return headers;
    }

    static void putHeaders(MimeHeaders headers, HttpServletResponse res)
    {

        Iterator it = headers.getAllHeaders();
        while (it.hasNext()) {
            MimeHeader header = (MimeHeader)it.next();

            String[] values = headers.getHeader(header.getName());
            if (values.length == 1) {
                res.setHeader(header.getName(), header.getValue());
            } else {
                StringBuffer concat = new StringBuffer();
                int i = 0;
                while (i < values.length) {
                    if (i != 0) {
                        concat.append(',');
                    }
                    concat.append(values[i++]);
                }
                res.setHeader(header.getName(), concat.toString());
            }
        }
    }

    // This is the application code for responding to the message.

    public SOAPMessage onMessage(SOAPMessage msg)
    {
        String sID = "";
        try
        {
          SOAPBodyElement bodyEl=(SOAPBodyElement)msg.getSOAPPart().getEnvelope().getBody().getChildElements().next();
          Iterator it=bodyEl.getChildElements();
          sID=(it.hasNext() ? ((SOAPElement)it.next()).getValue() : null);
        }
        catch(Exception e)
        {}

        SOAPMessage message = null;

        try
        {
            // create price list message
            message = fac.createMessage();

            // Access the SOAPBody object
            SOAPPart part = message.getSOAPPart();
            SOAPEnvelope envelope = part.getEnvelope();
            SOAPBody body = envelope.getBody();

            // Create the appropriate elements and add them
            Name bodyName = envelope.createName("processCubeTestResponse","", "http://www.st.com");
            SOAPBodyElement bodyEl = body.addBodyElement(bodyName);
            SOAPElement el=bodyEl.addChildElement(envelope.createName("appID"));
            el.addTextNode("Received request from session "+(sID==null ? "UNKNOWN" : sID));

            message.saveChanges();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return message;
    }

}
