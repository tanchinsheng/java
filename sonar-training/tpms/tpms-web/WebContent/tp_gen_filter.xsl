<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

<xsl:template match="/TPS">
  <xsl:copy>
	 <xsl:apply-templates select="TP[translate(substring(DATE,1,10),'.','') &gt; 20030317 or translate(substring(DATE,1,10),'.','') = 20030317][translate(substring(DATE,1,10),'.','') &lt; 20030619]"/>
  </xsl:copy>	
</xsl:template>

<!-- Identity Template -->  
<xsl:template match="@*|*|text()">
	<xsl:copy><xsl:apply-templates select="@*|*|text()"/></xsl:copy>
</xsl:template>

</xsl:stylesheet>
