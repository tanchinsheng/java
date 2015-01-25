<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

<xsl:param name="status1"></xsl:param>
<xsl:param name="status2"></xsl:param>
<xsl:param name="status3"></xsl:param>
<xsl:param name="status4"></xsl:param>
<xsl:param name="status5"></xsl:param>

<xsl:template match="/TPS">
  <xsl:copy>
  <xsl:if test="not($status1='')">
	 <xsl:apply-templates select="TP[STATUS=$status1]"/>
  </xsl:if>   
  <xsl:if test="not($status2='')">
	 <xsl:apply-templates select="TP[STATUS=$status2]"/>
  </xsl:if>   
  <xsl:if test="not($status3='')">
	 <xsl:apply-templates select="TP[STATUS=$status3]"/>
  </xsl:if>   
  <xsl:if test="not($status4='')">
	 <xsl:apply-templates select="TP[STATUS=$status4]"/>
  </xsl:if>   
  <xsl:if test="not($status5='')">
	 <xsl:apply-templates select="TP[STATUS=$status5]"/>
  </xsl:if>   		 	
  </xsl:copy>	
</xsl:template>

<!-- Identity Template -->  
<xsl:template match="@*|*|text()">
	<xsl:copy><xsl:apply-templates select="@*|*|text()"/></xsl:copy>
</xsl:template>

</xsl:stylesheet>
