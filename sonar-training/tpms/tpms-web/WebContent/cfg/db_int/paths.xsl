<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"> 
<xsl:template match="/">
   <center>
   <font face="arial">
   <H4>PATHS SCHEMA</H4>
   <table border="0" BGCOLOR="FFFFFF" CELLSPACING="3" CELLPADDING="4">
     <tr BGCOLOR="C0C0C0">	  
       <th><xsl:value-of select="PATHS/PATH[1]/FROM_TBL"/></th>
       <th><xsl:value-of select="PATHS/PATH[1]/TO_TBL"/></th>
       <th>Join ID</th>
     </tr>
     <xsl:for-each select="PATHS/PATH">
       <xsl:variable name="join" select="."/>
       <xsl:for-each select="document('joins.xml',/)/JOINS/JOIN[JOIN_ID=$join/JOIN_ID][1]">
         <tr BGCOLOR="E0E0E0">
          <td><xsl:value-of select="DBTABLEA"/></td>
          <td><xsl:value-of select="DBTABLEB"/></td>
          <td><xsl:value-of select="JOIN_ID"/></td>
         </tr>
       </xsl:for-each>
       <xsl:if test="(following-sibling::PATH[1]/FROM_TBL!=./FROM_TBL) or (following-sibling::PATH[1]/TO_TBL!=./TO_TBL)">
         <tr BGCOLOR="C0C0C0">
           <th><xsl:value-of select="following-sibling::PATH[1]/FROM_TBL"/></th>
           <th><xsl:value-of select="following-sibling::PATH[1]/TO_TBL"/></th>			  
	   <th>Join ID</th>
         </tr>
       </xsl:if>        
     </xsl:for-each>
   </table>
   </font>
   </center>
</xsl:template>
</xsl:stylesheet>
