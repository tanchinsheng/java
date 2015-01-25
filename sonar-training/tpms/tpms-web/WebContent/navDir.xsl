<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<xsl:param name="dirOnly">Y</xsl:param>
	<xsl:param name="enableSelection">Y</xsl:param> <!-- dir selection -->
	<xsl:param name="enableFileSelection">N</xsl:param> <!-- file selection -->
	<xsl:param name="fileExt"></xsl:param>
	

	<xsl:template match="/FILES">
  		<table cellspacing="1" cellpadding="4" border="0"> 
     	<tbody>
        <BR/>
        	<!-- HEADERS (class=testoH) -->
          <tr bgcolor="#c9e1fa">
             <xsl:if test="$enableSelection='Y'">
	          	<td bgcolor="#FFFFFF"><img src="img/blank_flat.gif"/></td> 
          	</xsl:if>
          	<xsl:if test="$enableFileSelection='Y'">
	          	<td bgcolor="#FFFFFF"><img src="img/blank_flat.gif"/></td>           	
          	</xsl:if>
            	<td class="testoH"><b>Name</b></td>
            	<td class="testoH"><b>Size [B]</b></td>
           	<td class="testoH"><b>Last modified</b></td>
         	</tr>
    	
				<xsl:choose>
					<xsl:when test="$dirOnly='N'">
					  <xsl:choose>
							<xsl:when test="$fileExt=''">
								<xsl:apply-templates select="FILE">
									<xsl:sort select="@dir" order="descending"/>
									<xsl:sort select="."/>								
								</xsl:apply-templates>		
							</xsl:when>
							<xsl:otherwise>
								<xsl:apply-templates select="FILE[@dir='Y' or @ext=$fileExt]">
									<xsl:sort select="@dir" order="descending"/>
									<xsl:sort select="."/>								
								</xsl:apply-templates>		
							</xsl:otherwise>
					   </xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="FILE[@dir='Y']">
							<xsl:sort select="."/>								
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
			
     	</tbody>
     </table>
   </xsl:template>

	<xsl:template match="FILE">
  		<tr>
       <xsl:if test="$enableSelection='Y'">
			<td>
				<xsl:choose>
	  				<xsl:when test="@dir='Y' and not(node()='..')">
						<xsl:element name="input">
							<xsl:attribute name="type">radio</xsl:attribute>
							<xsl:attribute name="name">selFile</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="../@path"/>/<xsl:value-of select="."/></xsl:attribute>
							<xsl:attribute name="onClick">javascript:checkDir('<xsl:value-of select="."/>')</xsl:attribute>
						</xsl:element>
  					</xsl:when>
  					<xsl:otherwise>
						<img src="img/blank_flat.gif"/>						
  					</xsl:otherwise> 
				</xsl:choose>
			</td>
       	</xsl:if>

       <xsl:if test="$enableFileSelection='Y'">
			<td>
				<xsl:choose>
	  				<xsl:when test="@dir='N'">
						<xsl:element name="input">
							<xsl:attribute name="type">radio</xsl:attribute>
							<xsl:attribute name="name">selFile</xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="../@path"/>/<xsl:value-of select="."/></xsl:attribute>
							<xsl:attribute name="onClick">javascript:checkFile('<xsl:value-of select="."/>')</xsl:attribute>
						</xsl:element>
  					</xsl:when>
  					<xsl:otherwise>
						<img src="img/blank_flat.gif"/>						
  					</xsl:otherwise> 
				</xsl:choose>
			</td>
       	</xsl:if>

     	<td class="testo" bgcolor="#f2f2f2">
     	<xsl:choose>
				<xsl:when test="@dir='Y'">
					<img src="img/folder_icon.gif" border="0"/><img src="img/blank_flat_small.gif"/>
     			<xsl:element name="A">
     				<xsl:attribute name="href">javascript:selDir('<xsl:value-of select="."/>')</xsl:attribute>
     	  			<xsl:value-of select="."/>
     	  		</xsl:element>
     		</xsl:when>  	
     		<xsl:otherwise>
     	  		<xsl:choose>
				<xsl:when test="@type='TEXT'">
		     		  <img src="img/text.gif" border="0"/>
     				  <img src="img/blank_flat_small.gif"/>
				  <xsl:element name="A">
				  	<xsl:attribute name="href">javascript:selFile('<xsl:value-of select="."/>')</xsl:attribute>
 					<xsl:value-of select="."/> 
			  	  </xsl:element>   	
				</xsl:when>
				<xsl:otherwise>
		     			<img src="img/folder_icon_blank.gif" border="0"/>
     					<img src="img/blank_flat_small.gif"/>
 					<xsl:value-of select="."/>    					
				</xsl:otherwise>
			</xsl:choose>	     			
     		</xsl:otherwise>  		
			</xsl:choose>
     	</td>
     	<td class="testo" bgcolor="#f2f2f2" align="right"><img src="img/blank_flat.gif"/>
     		<xsl:value-of select="@size"/>
     	</td>
     	<td class="testo" bgcolor="#f2f2f2"><img src="img/blank_flat.gif"/>
     		<xsl:value-of select="@dateModS"/>
     	</td>
		</tr>
	</xsl:template>


</xsl:stylesheet>
