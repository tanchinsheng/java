<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" indent="yes"/>

<xsl:param name="jobname">2691EBA4</xsl:param>
<xsl:param name="job_rel">01</xsl:param>
<xsl:param name="job_rev">01</xsl:param>
<xsl:param name="job_ver">01</xsl:param>


<xsl:template match="STDATASELECTION">
 <xsl:copy>
  	<xsl:apply-templates select="STDATA_COLUMNS"/>
	<xsl:apply-templates select="STDATA_RECORD[JOBNAME=$jobname][JOB_REL=$job_rel][JOB_REV=$job_rev][JOB_VER=$job_ver][position()=1]"/>
 </xsl:copy>
</xsl:template>

<!-- Identity Template -->  
<xsl:template match="@*|*|text()">
	<xsl:copy><xsl:apply-templates select="@*|*|text()"/></xsl:copy>
</xsl:template>


</xsl:stylesheet>
