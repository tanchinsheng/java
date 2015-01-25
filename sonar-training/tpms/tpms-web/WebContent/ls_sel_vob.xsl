<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"/>
	<xsl:param name="lineset_name">netline</xsl:param>
	
	<xsl:template match="/LineSET_LIST">
	   <LS_DETAILS>
  	      <xsl:apply-templates select="LS[LS_NAME=$lineset_name]"/>
  	   </LS_DETAILS> 	
	</xsl:template>

	<!-- Identity Template -->
	<xsl:template match="@*|*|text()">
		<xsl:copy>
			<xsl:apply-templates select="@*|*|text()"/>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
