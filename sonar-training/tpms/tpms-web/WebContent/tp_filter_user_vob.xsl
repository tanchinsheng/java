<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

<xsl:param name="user"></xsl:param>
<xsl:param name="status"></xsl:param>


<xsl:template match="/TPS">
  <xsl:copy>
  
  <xsl:if test="($status='Distributed')">
	 <xsl:apply-templates select="TP[VALID_LOGIN=$user or VALID_LOGIN='.']"/>
  </xsl:if>   
  <xsl:if test="($status='In_Validation')">
	 <xsl:apply-templates select="TP[VALID_LOGIN=$user or VALID_LOGIN='.' or PROD_LOGIN=$user or PROD_LOGIN='.']"/>
  </xsl:if>
<xsl:if test="($status='In_Production')">
	 <xsl:apply-templates select="TP[PROD_LOGIN=$user or PROD_LOGIN='.']"/>
  </xsl:if>
  
<xsl:if test="($status='')">
	 <xsl:apply-templates select="TP[PROD_LOGIN=$user or VALID_LOGIN=$user]"/>
  </xsl:if>     
  
  </xsl:copy>	
</xsl:template>

<!-- Identity Template -->  
<xsl:template match="@*|*|text()">
	<xsl:copy><xsl:apply-templates select="@*|*|text()"/></xsl:copy>
</xsl:template>

</xsl:stylesheet>
