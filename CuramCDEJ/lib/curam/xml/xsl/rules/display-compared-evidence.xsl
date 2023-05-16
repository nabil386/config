<?xml version="1.0"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2012,2022. All rights reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:domain="http://xml.apache.org/xalan/java/curam.omega3.taglib.widget.EvidenceTag"
	xmlns:xalan="http://xml.apache.org/xalan"
	xmlns:request-utils="http://xml.apache.org/xalan/java/curam.omega3.request.RequestUtils"
	extension-element-prefixes="xalan" exclude-result-prefixes="domain request-utils">
	<xsl:import href="../common/ui.xsl" />
	<xsl:import href="../common/string-utils.xsl" />
	<xsl:import href="ui-evidence-styles.xsl" />

	<xsl:output method="xml" indent="no" omit-xml-declaration="yes" />
	<xsl:strip-space elements="*" />

	<xsl:param name="locale" />
	<xsl:param name="Evidence.Label.MODIFIED" />
	<xsl:param name="Evidence.Label.NEW" />
	<xsl:param name="Evidence.Label.REMOVED" />

	<xsl:param name="Description.Column.Visible" />
	<xsl:param name="Group.Column.Visible" />
	<xsl:param name="Oldval.Column.Visible" />
	<xsl:param name="Value.Column.Visible" />

	<xsl:param name="Description.Column.Header" />
	<xsl:param name="Group.Column.Header" />
	<xsl:param name="Oldval.Column.Header" />
	<xsl:param name="Value.Column.Header" />
	<xsl:param name="Description.Column.Value" />

	<xsl:param name="Description.Column.Link" />
	<xsl:param name="Group.Column.Link" />
	<!-- The username variable is supplied by the Transformer object in the jsp tag. -->
	<xsl:param name="username" />

	<xsl:variable name="structure">
		<COLUMN name="Description" />
		<COLUMN name="Group" />
		<COLUMN name="Oldval" />
		<COLUMN name="Value" />
	</xsl:variable>

	<!-- List title -->
	<xsl:template match="EVIDENCE">

		<xsl:if test="@TYPE = 'MODIFIED'">
			<xsl:call-template name="gen-list">
				<xsl:with-param name="title">
					<xsl:value-of select="$Evidence.Label.MODIFIED" />
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		<xsl:if test="@TYPE = 'NEW'">
			<xsl:call-template name="gen-list">
				<xsl:with-param name="title">
					<xsl:value-of select="$Evidence.Label.NEW" />
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		<xsl:if test="@TYPE = 'REMOVED'">
			<xsl:call-template name="gen-list">
				<xsl:with-param name="title">
					<xsl:value-of select="$Evidence.Label.REMOVED" />
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<!-- Column definitions -->

	<xsl:template name="gen-list-column-defs">
		<xsl:variable name="denominator">
			<xsl:call-template name="num-visible">
				<xsl:with-param name="current" select="4" />
				<xsl:with-param name="shown" select="4" />
			</xsl:call-template>
		</xsl:variable>
		<xsl:call-template name="gen-list-column-def">
			<xsl:with-param name="all-cols" select="$denominator" />
			<xsl:with-param name="curr-col" select="1" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="gen-list-column-def">
		<xsl:param name="all-cols" />
		<xsl:param name="curr-col" />
		<xsl:param name="percents" select="round(100 div $all-cols)" />
		<col width="{$percents}%"
			xsl:use-attribute-sets="list-column-attributes" />
		<xsl:if test="$curr-col &lt; $all-cols">
			<xsl:call-template name="gen-list-column-def">
				<xsl:with-param name="curr-col" select="$curr-col + 1" />
				<xsl:with-param name="all-cols" select="$all-cols" />
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<!-- List header fields -->

	<xsl:template name="gen-list-header-fields">
		<xsl:variable name="exclude"
			select="3*number(@TYPE = 'NEW') + 4*number(@TYPE = 'REMOVED')" />
		<xsl:call-template name="gen-list-header-field">
			<xsl:with-param name="which-column" select="1" />
			<xsl:with-param name="exclude" select="$exclude" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="gen-list-header-field">
		<xsl:param name="which-column" />
		<xsl:param name="exclude" />
		<xsl:if test="$which-column &lt;= 4">
			<xsl:variable name="prefix"
				select="xalan:nodeset($structure)/COLUMN[position() = $which-column]/@name" />

			<xsl:if test="$prefix = 'Description'">
				<xsl:if
					test="$Description.Column.Visible = 'true' and ($which-column != $exclude)">
					<th>
						<span>
							<xsl:value-of select="$Description.Column.Header" />
						</span>
					</th>
				</xsl:if>
			</xsl:if>
			<xsl:if test="$prefix = 'Group'">
				<xsl:if
					test="$Group.Column.Visible = 'true' and ($which-column != $exclude)">
					<th>
						<span>
							<xsl:value-of select="$Group.Column.Header" />
						</span>
					</th>
				</xsl:if>
			</xsl:if>
			<xsl:if test="$prefix = 'Oldval'">
				<xsl:if
					test="$Oldval.Column.Visible = 'true' and ($which-column != $exclude)">
					<th>
						<span>
							<xsl:value-of select="$Oldval.Column.Header" />
						</span>
					</th>
				</xsl:if>
			</xsl:if>
			<xsl:if test="$prefix = 'Value'">
				<xsl:if
					test="$Value.Column.Visible = 'true' and ($which-column != $exclude)">
					<th>
						<span>
							<xsl:value-of select="$Value.Column.Header" />
						</span>
					</th>
				</xsl:if>
			</xsl:if>
			<xsl:call-template name="gen-list-header-field">
				<xsl:with-param name="which-column"
					select="$which-column + 1" />
				<xsl:with-param name="exclude" select="$exclude" />
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<!-- List body-->

	<xsl:template name="gen-list-body-rows">
		<xsl:for-each
			select="GROUP/EVIDENCE_ITEM[(not(@VISIBLE) or @VISIBLE='true')]">
			<xsl:call-template name="gen-list-body-row" />
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="gen-list-body-fields">
		<xsl:call-template name="gen-list-body-field">
			<xsl:with-param name="which-column" select="1" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="gen-list-body-field">
		<xsl:param name="which-column" />
		<xsl:variable name="exclude"
			select="3*number(../../@TYPE = 'NEW') + 4*number(../../@TYPE = 'REMOVED')" />
		<xsl:if test="$which-column &lt;= 4">
			<xsl:variable name="prefix"
				select="xalan:nodeset($structure)/COLUMN[position() = $which-column]/@name" />
			<xsl:if test="$prefix = 'Description'">
				<xsl:if
					test="$Description.Column.Visible = 'true' and ($which-column != $exclude)">
					<td class="{$list-body-field-class} widget-border">
						<xsl:call-template
							name="gen-list-body-field-content">
							<xsl:with-param name="col-num"
								select="$which-column" />
						</xsl:call-template>
					</td>
				</xsl:if>
			</xsl:if>
			<xsl:if test="$prefix = 'Group'">
				<xsl:if
					test="$Group.Column.Visible = 'true' and ($which-column != $exclude)">
					<td class="{$list-body-field-class} widget-border">
						<xsl:call-template
							name="gen-list-body-field-content">
							<xsl:with-param name="col-num"
								select="$which-column" />
						</xsl:call-template>
					</td>
				</xsl:if>
			</xsl:if>
			<xsl:if test="$prefix = 'Oldval'">
				<xsl:if
					test="$Oldval.Column.Visible = 'true' and ($which-column != $exclude)">
					<td class="{$list-body-field-class} widget-border">
						<xsl:call-template
							name="gen-list-body-field-content">
							<xsl:with-param name="col-num"
								select="$which-column" />
						</xsl:call-template>
					</td>
				</xsl:if>
			</xsl:if>
			<xsl:if test="$prefix = 'Value'">
				<xsl:if
					test="$Value.Column.Visible = 'true' and ($which-column != $exclude)">
					<td class="{$list-body-field-class} widget-border">
						<xsl:call-template
							name="gen-list-body-field-content">
							<xsl:with-param name="col-num"
								select="$which-column" />
						</xsl:call-template>
					</td>
				</xsl:if>
			</xsl:if>
			<xsl:call-template name="gen-list-body-field">
				<xsl:with-param name="which-column"
					select="$which-column + 1" />
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<xsl:template name="gen-list-body-field-content">
		<xsl:param name="col-num" />
		<xsl:variable name="prefix"
			select="xalan:nodeset($structure)/COLUMN[position() = $col-num]/@name" />
		<xsl:choose>
			<xsl:when test="$col-num = 1">
				<xsl:choose>
					<xsl:when test="$prefix = 'Group'">
						<xsl:call-template
							name="probably-linked-value">
							<xsl:with-param name="textual-contents"
								select="@DESCRIPTION" />
							<xsl:with-param name="a-link">
								<xsl:value-of
									select="$Group.Column.Link" />
							</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:when test="$prefix = 'Description'">
						<xsl:call-template
							name="probably-linked-value">
							<xsl:with-param name="textual-contents"
								select="@DESCRIPTION" />
							<xsl:with-param name="a-link">
								<xsl:value-of
									select="$Group.Column.Link" />
							</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:message terminate="yes">
							Error in stylesheet. Parameter missing
						</xsl:message>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$col-num = 2">
				<xsl:choose>
					<xsl:when test="$prefix = 'Group'">
						<xsl:call-template
							name="probably-linked-value">
							<xsl:with-param name="textual-contents"
								select="../@DESCRIPTION" />
							<xsl:with-param name="a-link"
								select="$Group.Column.Link" />
						</xsl:call-template>
					</xsl:when>
					<xsl:when test="$prefix = 'Description'">
						<xsl:call-template
							name="probably-linked-value">
							<xsl:with-param name="textual-contents"
								select="../@DESCRIPTION" />
							<xsl:with-param name="a-link"
								select="$Description.Column.Link" />
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:message terminate="yes">
							Error in stylesheet. Parameter missing
						</xsl:message>
					</xsl:otherwise>
				</xsl:choose>


			</xsl:when>
			<xsl:when
				test="($col-num = 3) and (../../@TYPE != 'NEW')">
				<xsl:value-of
					select="domain:getFormatedValue(@DOMAIN, @OLDVAL, $locale)" />
			</xsl:when>
			<xsl:when
				test="$col-num &gt;= 3 and ../../@TYPE != 'REMOVED'">
				<xsl:value-of
					select="domain:getFormatedValue(@DOMAIN, @VALUE, $locale)" />
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<!-- auxillary-->
	<xsl:template name="probably-linked-value">
		<xsl:param name="textual-contents" />
		<xsl:param name="a-link" />
		<xsl:variable name="value-buffer">
			<xsl:call-template name="get-localized-message">
				<xsl:with-param name="messages"
					select="$textual-contents" />
				<xsl:with-param name="locale-code" select="$locale" />
			</xsl:call-template>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$a-link != ''">
			
			<xsl:variable name="url-no-hash">
		      <xsl:value-of select="concat($a-link, 'Page.do?', 'itemID=',@ID, '&amp;groupID=', ../@ID)"/>
		    </xsl:variable>
		    <xsl:variable name="url-with-hash">
		      <xsl:value-of select="request-utils:replaceOrAddURLHashToken($url-no-hash, $username)"/>
		    </xsl:variable>
			
				<a
					href="{$url-with-hash}">
					<xsl:value-of select="$value-buffer" />
				</a>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$value-buffer" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="num-visible">
		<xsl:param name="current" />
		<xsl:param name="shown" />
		<xsl:param name="not-shown"
			select="3*number(@TYPE = 'NEW') + 4*number(@TYPE = 'REMOVED')" />
		<xsl:choose>
			<xsl:when test="$current &gt;= 1">
				<xsl:variable name="prefix"
					select="xalan:nodeset($structure)/COLUMN[$current]/@name" />
				<xsl:variable name="invisible">
					<xsl:choose>
						<xsl:when test="$prefix = 'Description'">
							<xsl:value-of
								select="number(($Description.Column.Visible = 'false') or ($current = $not-shown))" />
						</xsl:when>
						<xsl:when test="$prefix = 'Group'">
							<xsl:value-of
								select="number(($Group.Column.Visible = 'false') or ($current = $not-shown))" />
						</xsl:when>
						<xsl:when test="$prefix = 'Oldval'">
							<xsl:value-of
								select="number(($Oldval.Column.Visible = 'false') or ($current = $not-shown))" />
						</xsl:when>
						<xsl:when test="$prefix = 'Value'">
							<xsl:value-of
								select="number(($Value.Column.Visible = 'false') or ($current = $not-shown))" />
						</xsl:when>
					</xsl:choose>
				</xsl:variable>
				<xsl:call-template name="num-visible">
					<xsl:with-param name="current"
						select="$current - 1" />
					<xsl:with-param name="shown"
						select="$shown - $invisible" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$shown" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- PLACEHOLDERS -->

	<xsl:template name="gen-action-set">
		<xsl:param name="position" />
		<xsl:param name="context" />
	</xsl:template>

	<xsl:template name="gen-action-control">
		<xsl:param name="context" />
	</xsl:template>

</xsl:stylesheet>
