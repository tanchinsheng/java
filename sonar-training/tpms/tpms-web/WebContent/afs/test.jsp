<%@ page import="it.txt.afs.packet.Packet" %>
<%@ page import="it.txt.afs.packet.utils.PacketConstants" %>
<%@ page import="it.txt.general.utils.XmlUtils" %>

<%@ page import="org.w3c.dom.Element" %>

<%@ page import="tpms.utils.UserUtils"%>

<%@ page import="it.txt.afs.clearcase.utils.ClearcaseInterfaceCostants"%>

<%@ page import="it.txt.afs.servlets.master.AfsServletUtils"%>

<%--
  Created by IntelliJ IDEA.
  User: furgiuele
  Date: 25-gen-2006
  Time: 16.44.52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Simple jsp page</title></head>

<body>
<%
    Packet packet = (Packet) session.getAttribute(PacketConstants.PACKET_ATTRIBUTE_SESSION_NAME);
    if (packet != null) {
%>
<br>protected String id = *<%=packet.getId()%>*
<br>protected String name = *<%=packet.getName()%>*
<br>protected String tpRelease = *<%=packet.getTpRelease()%>*
<br>protected String tpRevision = *<%=packet.getTpRevision()%>*
<br>protected String destinationPlant = *<%=packet.getDestinationPlant()%>*
<br>protected String fromPlant = *<%=packet.getFromPlant()%>*
<br>protected String senderLogin = *<%=packet.getSenderLogin()%>*
<br>protected String senderEmail = *<%=packet.getSenderEmail()%>*
<br>protected String firstRecieveLogin = *<%=packet.getFirstRecieveLogin()%>*
<br>protected String firstRecieveEmail = *<%=packet.getFirstRecieveEmail()%>*
<br>protected String secondRecieveLogin = *<%=packet.getSecondRecieveLogin()%>*
<br>protected String secondRecieveEmail = *<%=packet.getSecondRecieveEmail()%>*
<br>protected String ccEmail = *<%=packet.getCcEmail()%>*
<br>protected String status = *<%=packet.getStatus()%>*
<br>protected String vobStatus = *<%=packet.getVobStatus()%>*
<br>protected String extractionLogin = *<%=packet.getExtractionLogin()%>*
<br>protected Date extractionDate = *<%=packet.getExtractionDate()%>*
<br>protected String recieverComments = *<%=packet.getRecieverComments()%>*
<br>protected String senderComments = *<%=packet.getSenderComments()%>*
<br>protected String xferPath = *<%=packet.getXferPath()%>*
<br>protected Date lastActionDate = *<%=packet.getLastActionDate()%>*
<br>protected Date sentDate = *<%=packet.getSentDate()%>*
<%}%>
<%--


    String xmlOutputPath = "D:\\bat\\ccSimulator\\out_test.xml";
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
    Document doc = documentBuilder.parse(new File(xmlOutputPath));
    Element pkgElemets = (Element) doc.getDocumentElement().getElementsByTagName("REP").item(0);
    Element pkgElement = (Element) pkgElemets.getFirstChild();
%>


<br>"LABEL" = <%=XmlUtils.getTextValue(pkgElement, "LABEL")%>
<br>"NAME" = <%=XmlUtils.getTextValue(pkgElement, "NAME")%>
<br>"ID" = <%=XmlUtils.getTextValue(pkgElement, "ID")%>
<br>"DATE" = <%=XmlUtils.getTextValue(pkgElement, "DATE")%>
<hr>
--%>
<%
    String vobDescriptionWithLabel = "<B>Vob:</B>" + session.getAttribute("vobDesc");
    String vobTypeWithLabel = "<B>Type:</B>" + (String) session.getAttribute("vobType");
    if (UserUtils.hasAfsRole((String) session.getAttribute("user"))) {
        //Vob RVob = (Vob) session.getAttribute(AfsServletUtils.SELECTED_R_VOB_SESSION_ATTRIBUTE_NAME);
        Vobob(Vobession.getAttribute(AfsServletUtils.SELECTED_T_VOB_SESSION_ATTRIBUTE_NAME);
        vobTypeWithLabel = "";
        if (TVob null)
            vobDescriptionWithLabel = "<B>Default destination Plant:</B>&nbsp:" + TVobtPlant();
        else
            vobDescriptionWithLabel = "<B>No default destination plant:</B>";
    }

%>


<%

            Element sentPacketListRoot = XmlUtils.getRoot("/export/home/vobadm/jakarta-tomcat-3.2.3/webapps/tpms/images/afs_5iag2zx741_1139239039679_rep.xml");
            Element el = (Element) sentPacketListRoot.getElementsByTagName(ClearcaseInterfaceCostants.SENT_XMLOUT_PACKET_ELEMENT).item(0);
    %>
            <br>SENT_XMLOUT_ID_ELEMENT = <%=XmlUtils.getTextValue(el, ClearcaseInterfaceCostants.SENT_XMLOUT_ID_ELEMENT).trim()%>
            <br>SENT_XMLOUT_LABEL_ELEMENT<%=XmlUtils.getTextValue(el, ClearcaseInterfaceCostants.SENT_XMLOUT_LABEL_ELEMENT).trim()%>



<br>vobDescriptionWithLabel = <%=vobDescriptionWithLabel%>
<br>vobTypeWithLabel = <%=vobTypeWithLabel%>
</body>
</html>