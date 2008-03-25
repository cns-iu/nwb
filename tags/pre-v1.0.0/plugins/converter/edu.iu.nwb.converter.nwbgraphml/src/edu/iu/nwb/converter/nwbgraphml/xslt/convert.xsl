<?xml version='1.0' encoding='utf-8'?>
<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform' xmlns:g="http://graphml.graphdrawing.org/xmlns">

<xsl:output method='text' encoding='utf-8' indent='no'/>
<xsl:variable name="labelKey" select="//g:key[@attr.name='label' and @for='node']/@id"/>
<xsl:variable name="labelDefault" select="//g:key[@attr.name='label' and @for='node']/g:default"/>
<xsl:variable name="weightKey" select="//g:key[@attr.name='weight' and @for='edge']/@id"/>
<xsl:variable name="weightDefault" select="//g:key[@attr.name='weight' and @for='edge']/g:default"/>
<xsl:template match="/">
	<xsl:text>*Nodes </xsl:text><xsl:value-of select="count(//g:node)"/>
	<xsl:text>
</xsl:text>
	<xsl:choose>
		<xsl:when test="count($labelKey) > 0">
			<xsl:apply-templates select="//g:node" mode="labels"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:apply-templates select="//g:node" mode="noLabels"/>
		</xsl:otherwise>
	</xsl:choose>
	<xsl:choose>
		<xsl:when test="/g:graphml/g:graph[@edgedefault='directed']">
			<xsl:text>*DirectedEdges</xsl:text>
		</xsl:when>
		<xsl:when test="/g:graphml/g:graph[@edgedefault='undirected']">
			<xsl:text>*UndirectedEdges</xsl:text>
		</xsl:when>
		<xsl:otherwise>
			<xsl:text>*UndirectedEdges</xsl:text>
		</xsl:otherwise>
	</xsl:choose>
	<xsl:text>
</xsl:text>
	<xsl:choose>
		<xsl:when test="count($weightKey) > 0">
			<xsl:apply-templates select="//g:edge" mode="weighted"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:apply-templates select="//g:edge" mode="unweighted"/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template match="g:node" mode="labels">
	<xsl:value-of select="@id"/>
	<xsl:text> "</xsl:text>
	<xsl:choose>
		<xsl:when test="g:data[@key=$labelKey]">
			<xsl:value-of select="g:data[@key=$labelKey]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="$labelDefault"/>
		</xsl:otherwise>
	</xsl:choose>
	<xsl:text>"</xsl:text>
	<xsl:text>
</xsl:text>
</xsl:template>

<xsl:template match="g:node" mode="noLabels">
	<xsl:value-of select="@id"/>
	<xsl:text> "</xsl:text>
	<xsl:value-of select="@id"/>
	<xsl:text>"</xsl:text>
	<xsl:text>
</xsl:text>
</xsl:template>

<xsl:template match="g:edge" mode="weighted">
	<xsl:value-of select="@source"/>
	<xsl:text> </xsl:text>
	<xsl:value-of select="@target"/>
	<xsl:text> </xsl:text>
	<xsl:choose>
		<xsl:when test="g:data[@key=$weightKey]">
			<xsl:value-of select="g:data[@key=$weightKey]"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="$weightDefault"/>
		</xsl:otherwise>
	</xsl:choose>
	<xsl:text>
</xsl:text>
</xsl:template>

<xsl:template match="g:edge" mode="unweighted">
	<xsl:value-of select="@source"/>
	<xsl:text> </xsl:text>
	<xsl:value-of select="@target"/>
	<xsl:text>
</xsl:text>
</xsl:template>


</xsl:stylesheet>