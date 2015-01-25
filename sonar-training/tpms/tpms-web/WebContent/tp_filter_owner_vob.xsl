<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

<xsl:param name="user"></xsl:param>

<xsl:template match="/TPS">
  <xsl:copy>
    <xsl:apply-templates select="TP[OWNER_LOGIN=$user]"/>
  </xsl:copy>	
</xsl:template>

<!-- Identity Template -->  
<xsl:template match="@*|*|text()">
	<xsl:copy><xsl:apply-templates select="@*|*|text()"/></xsl:copy>
</xsl:template>

</xsl:stylesheet>
