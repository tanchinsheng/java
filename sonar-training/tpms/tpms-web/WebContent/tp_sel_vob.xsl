<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"/>
	<xsl:param name="jobname"/>
	<xsl:param name="job_rel"/>
	<xsl:param name="job_rev"/>
	<xsl:param name="job_ver"/>
	<xsl:param name="to_plant"/>
	<xsl:param name="jobLabel"/>
	
	<xsl:template match="/TPS">
		<xsl:choose>
			<xsl:when test="not($jobLabel='')">
				<xsl:apply-templates select="TP[TP_LABEL=$jobLabel][position()=1]"/>
			</xsl:when>
			<xsl:otherwise>
			  <xsl:choose>
			    <xsl:when test="$to_plant=''">
  				   <xsl:apply-templates 
				    select="TP[JOBNAME=$jobname][JOB_REL=$job_rel][JOB_REV=$job_rev][JOB_VER=$job_ver][position()=1]"/>
			    </xsl:when>
			    <xsl:otherwise>
  				   <xsl:apply-templates 
				    select="TP[JOBNAME=$jobname][JOB_REL=$job_rel][JOB_REV=$job_rev][JOB_VER=$job_ver][position()=1][TO_PLANT=$to_plant]"/>
			    </xsl:otherwise>
			  </xsl:choose>				  
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="/LS_DETAILS">
		<xsl:choose>
			<xsl:when test="not($jobLabel='')">
				<xsl:apply-templates select="TP[TP_LABEL=$jobLabel][position()=1]"/>
			</xsl:when>
			<xsl:otherwise>
			  <xsl:choose>
			    <!-- FP 3-06-2005 Rev5-->
			    <xsl:when test="$job_rev='' and $job_ver='' ">
			        <xsl:element name = "TPS">
			        		<xsl:apply-templates 
				    		select="TP[JOBNAME=$jobname][JOB_REL=$job_rel]"/>
			        </xsl:element>
			    </xsl:when>
			    <xsl:when test="$to_plant=''">
  				   <xsl:apply-templates 
				    select="TP[JOBNAME=$jobname][JOB_REL=$job_rel][JOB_REV=$job_rev][JOB_VER=$job_ver][position()=1]"/>
			    </xsl:when>			    
			    <xsl:otherwise>
  				   <xsl:apply-templates 
				    select="TP[JOBNAME=$jobname][JOB_REL=$job_rel][JOB_REV=$job_rev][JOB_VER=$job_ver][position()=1][TO_PLANT=$to_plant]"/>
			    </xsl:otherwise>
			  </xsl:choose>				  
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Identity Template -->
	<xsl:template match="@*|*|text()">
		<xsl:copy>
			<xsl:apply-templates select="@*|*|text()"/>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
