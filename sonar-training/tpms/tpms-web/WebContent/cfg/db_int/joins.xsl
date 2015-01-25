<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:template match="/">
<center>
<font face="arial">
   <H3>JOINS SCHEMA</H3> 
   <table border="1" BGCOLOR="FFFFFF" CELLSPACING="2" CELLPADDING="4">
     <tr BGCOLOR="C0C0C0">
       <th>ID=<xsl:value-of select="/JOINS/JOIN[1]/JOIN_ID"/></th>
       <th><xsl:value-of select="/JOINS/JOIN[1]/DBTABLEA"/></th>
       <th><xsl:value-of select="/JOINS/JOIN[1]/DBTABLEB"/></th>			  
     </tr>     
     <xsl:for-each select="JOINS/JOIN">          
      <tr BGCOLOR="E0E0E0">
        <td BGCOLOR="FFFFFF"></td>
        <td><xsl:value-of select="JOINFLDA"/></td>
	<td><xsl:value-of select="JOINFLDB"/></td>
      </tr>   
      <xsl:if test="following-sibling::JOIN[position()=1]/JOIN_ID!=./JOIN_ID">
       <tr BGCOLOR="C0C0C0">
         <th>ID=<xsl:value-of select="following-sibling::JOIN[position()=1]/JOIN_ID"/></th>
         <th><xsl:value-of select="following-sibling::JOIN[position()=1]/DBTABLEA"/></th>
         <th><xsl:value-of select="following-sibling::JOIN[position()=1]/DBTABLEB"/></th>			  
       </tr>
      </xsl:if>   
   </xsl:for-each>
   </table>   
   </font>
   </center>
</xsl:template>
</xsl:stylesheet>


