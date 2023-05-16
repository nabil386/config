<?xml version="1.0" encoding="UTF-8"?>
<!-- Licensed Materials - Property of IBM PID 5725-H26 Copyright IBM Corporation 
	2010, 2014. All Rights Reserved. US Government Users Restricted Rights - 
	Use, duplication or disclosure restricted by GSA ADP Schedule Contract with 
	IBM Corp. -->
<!-- BEGIN, 191675, JAY -->
<xsl:stylesheet extension-element-prefixes="redirect xalan"
	xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect" version="1.0"
	xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<!-- Global Variables -->
	<xsl:import href="../UICommon.xslt" />

	<!-- Create specific templates -->
	<xsl:import href="CreateUtilityTemplates.xslt" />

	<xsl:output method="xml" indent="yes" />

	<!-- Template to write the content vim and associated properties files for 
		the entities end date page @param entityElement XML Element for the entity 
		@param baseDirectory Base Directory to write the files into @param fileName 
		Name of the file to create (with no file extension) @param baseAggregation 
		The base for all dtls connect statements (e.g. dtls$) -->
	<xsl:template name="WizardEndDateContentVIM">

		<xsl:param name="entityElement" />
		<xsl:param name="baseDirectory" />
		<xsl:param name="fileName" />
		<xsl:param name="baseAggregation" />

		<!-- Name of the entity -->
		<xsl:variable name="entityName">
			<xsl:value-of select="$entityElement/@name" />
		</xsl:variable>

		<!-- facade end date method -->
		<xsl:variable name="facadeListEndDateMethod">listEvidenceForAutoEndDating</xsl:variable>

		<xsl:variable name="facadeEndDateMethod">autoEndDate<xsl:value-of select="$entityName"/>Evidence</xsl:variable>

		<!-- Full name and directory of the file to create, minus the file extension -->
		<xsl:variable name="fullFileName">
			<xsl:value-of select="$baseDirectory" />
			<xsl:value-of select="$fileName" />
		</xsl:variable>

		<!-- Write the main VIM file -->
		<redirect:write select="concat($fullFileName, '.vim')">

			<!-- add copyright notice -->
			<xsl:call-template name="printXMLCopyright">
				<xsl:with-param name="date" select="$date" />
			</xsl:call-template>

			<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">

				<!-- Page Parameters and their standard connections -->

				<!-- CaseID -->
				<PAGE_PARAMETER NAME="caseID" />
				<CONNECT>
					<SOURCE NAME="PAGE" PROPERTY="caseID" />
					<TARGET NAME="DISPLAYAUTOENDDATELIST" PROPERTY="key$caseID" />
				</CONNECT>
				<CONNECT>
					<SOURCE NAME="PAGE" PROPERTY="caseID" />
					<TARGET NAME="ACTION" PROPERTY="caseID" />
				</CONNECT>

				<PAGE_PARAMETER NAME="evidenceType" />
				<CONNECT>
					<SOURCE NAME="PAGE" PROPERTY="evidenceType" />
					<TARGET NAME="DISPLAYAUTOENDDATELIST" PROPERTY="key$evidenceType" />
				</CONNECT>
				
				<PAGE_PARAMETER NAME="startDateStr"/>
				<CONNECT>
					<SOURCE NAME="PAGE" PROPERTY="startDateStr"/>
					<TARGET NAME="DISPLAYAUTOENDDATELIST" PROPERTY="key$startDateStr"/>
				</CONNECT>
				<!-- Evidence ID from create page used to ignore the evidence created 
					in the create step -->
				<PAGE_PARAMETER NAME="evidenceID" />
				<CONNECT>
					<SOURCE NAME="PAGE" PROPERTY="evidenceID" />
					<TARGET NAME="DISPLAYAUTOENDDATELIST" PROPERTY="key$evidenceID" />
				</CONNECT>
				<xsl:if test="$entityElement/@relateEvidenceParticipantID !=''">
				<!-- Participant ID from create page used to list evidence for the 
					participant selected in the create step -->
					<PAGE_PARAMETER NAME="participantID" />	
					<CONNECT> 
						<SOURCE NAME="PAGE" PROPERTY="participantID"/> 
						<TARGET NAME="DISPLAYAUTOENDDATELIST" PROPERTY="key$participantID"/> 
					</CONNECT>
				</xsl:if>
				<xsl:if test="$entityElement/UserInterfaceLayer/@saveAndNewButton='Yes'">
					<PAGE_PARAMETER NAME="evidenceIDTabList"/>
					<CONNECT> 
						<SOURCE NAME="PAGE" PROPERTY="evidenceIDTabList"/> 
						<TARGET NAME="DISPLAYAUTOENDDATELIST" PROPERTY="key$evidenceIDList"/> 
					</CONNECT>
					<xsl:if test="$entityElement/@relateEvidenceParticipantID !=''">
					<PAGE_PARAMETER NAME="participantIDTabList"/>
					<CONNECT> 
						<SOURCE NAME="PAGE" PROPERTY="participantIDTabList"/> 
						<TARGET NAME="DISPLAYAUTOENDDATELIST" PROPERTY="key$participantIDList"/> 
					</CONNECT>
					</xsl:if>
				</xsl:if>

				<!-- Server Interfaces -->
				<SERVER_INTERFACE CLASS="{$prefix}{$facadeClass}"
					NAME="ACTION" OPERATION="{$facadeEndDateMethod}" PHASE="ACTION" />
				<SERVER_INTERFACE CLASS="Evidence" NAME="DISPLAYAUTOENDDATELIST"
					OPERATION="{$facadeListEndDateMethod}" PHASE="DISPLAY" />

				<INFORMATIONAL>
					<CONNECT>
						<SOURCE NAME="ACTION" PROPERTY="msg" />
					</CONNECT>
				</INFORMATIONAL>

				<CLUSTER LABEL_WIDTH="0" NUM_COLS="2" SHOW_LABELS="true">
					<CONDITION>
						<IS_FALSE NAME="DISPLAYAUTOENDDATELIST" PROPERTY="result$showListInd" />
					</CONDITION>
					<FIELD HEIGHT="1" USE_DEFAULT="false">
						<CONNECT>
							<SOURCE NAME="TEXT" PROPERTY="EndDateEvidence.Text.NoEvidence" />
						</CONNECT>
					</FIELD>
				</CLUSTER>

				<CLUSTER LABEL_WIDTH="30" NUM_COLS="1" SHOW_LABELS="true"
					STYLE="remove-child-indent">
					<CONDITION>
						<IS_TRUE NAME="DISPLAYAUTOENDDATELIST" PROPERTY="result$showListInd" />
					</CONDITION>
					<FIELD HEIGHT="1" LABEL="Field.Label.ChangeReason" 
						WIDTH="40">
						<CONNECT>
							<TARGET NAME="ACTION" PROPERTY="changeReason" />
						</CONNECT>
					</FIELD>
				</CLUSTER>

				<CLUSTER LABEL_WIDTH="30" NUM_COLS="1" SHOW_LABELS="true"
					STYLE="remove-child-indent">
					<CONDITION>
						<IS_TRUE NAME="DISPLAYAUTOENDDATELIST" PROPERTY="result$showListInd" />
					</CONDITION>
					<FIELD HEIGHT="1" LABEL="Field.Label.EndDate"
						WIDTH="40">
						<CONNECT>
							<SOURCE NAME="DISPLAYAUTOENDDATELIST" PROPERTY="result$endDate" />
						</CONNECT>
						<CONNECT>
							<TARGET NAME="ACTION" PROPERTY="endDate" />
						</CONNECT>
					</FIELD>
				</CLUSTER>

				<LIST DESCRIPTION="List.Description.SelectEndDateEvidence"
					SCROLL_HEIGHT="150" TITLE="List.Title.SelectEndDateEvidence">
					<CONTAINER WIDTH="5">
						<WIDGET TYPE="MULTISELECT">
							<WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
								<CONNECT>
									<SOURCE NAME="DISPLAYAUTOENDDATELIST" PROPERTY="result$list$evidenceDescriptorID" />
								</CONNECT>
								<CONNECT>
									<SOURCE NAME="DISPLAYAUTOENDDATELIST" PROPERTY="result$list$versionNo" />
								</CONNECT>
							</WIDGET_PARAMETER>
							<WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
								<CONNECT>
									<TARGET NAME="ACTION" PROPERTY="evidenceList$multiSelectStr" />
								</CONNECT>
							</WIDGET_PARAMETER>
						</WIDGET>
					</CONTAINER>
					<CONDITION>
						<IS_TRUE NAME="DISPLAYAUTOENDDATELIST" PROPERTY="result$showListInd" />
					</CONDITION>
					<FIELD DOMAIN="SVR_STRING" HEIGHT="1" LABEL="List.Title.Name"
						USE_DEFAULT="false" WIDTH="30">
						<CONNECT>
							<SOURCE NAME="DISPLAYAUTOENDDATELIST" PROPERTY="result$list$participantDetails" />
						</CONNECT>
					</FIELD>
					<FIELD DOMAIN="SVR_STRING" HEIGHT="1" LABEL="List.Title.EvidenceDescription"
						USE_DEFAULT="false" WIDTH="45">
						<CONNECT>
							<SOURCE NAME="DISPLAYAUTOENDDATELIST" PROPERTY="result$list$summary" />
						</CONNECT>
					</FIELD>
					<FIELD DOMAIN="SVR_STRING" HEIGHT="1" LABEL="List.Title.Period"
						USE_DEFAULT="false" WIDTH="25">
						<CONNECT>
							<SOURCE NAME="DISPLAYAUTOENDDATELIST" PROPERTY="result$list$period" />
						</CONNECT>
					</FIELD>
				</LIST>

			</VIEW>

		</redirect:write>
		<xsl:call-template
			name="write-all-locales-wizardenddatecontentvim-properties">
			<xsl:with-param name="locales" select="$localeList" />
			<xsl:with-param name="fullFileName" select="$fullFileName" />
			<xsl:with-param name="entityElement" select="$entityElement" />
		</xsl:call-template>

	</xsl:template>


	<xsl:template name="write-all-locales-wizardenddatecontentvim-properties">

		<xsl:param name="locales" />
		<xsl:param name="fullFileName" />
		<xsl:param name="entityElement" />

		<!-- tokens still exist -->
		<xsl:if test="$locales">

			<xsl:choose>

				<!--more than one -->
				<xsl:when test="contains($locales,',')">

					<xsl:call-template name="write-wizardenddatecontentvim-properties">
						<xsl:with-param name="locale"
							select="concat('_', substring-before($locales,','))" />
						<xsl:with-param name="fullFileName" select="$fullFileName" />
						<xsl:with-param name="entityElement" select="$entityElement" />
					</xsl:call-template>

					<!-- Recursively call self to process all locales -->
					<xsl:call-template
						name="write-all-locales-wizardenddatecontentvim-properties">
						<xsl:with-param name="locales"
							select="substring-after($locales,',')" />
						<xsl:with-param name="fullFileName" select="$fullFileName" />
						<xsl:with-param name="entityElement" select="$entityElement" />
					</xsl:call-template>

				</xsl:when>

				<!--only one token left -->
				<xsl:otherwise>

					<!-- Call for the final locale -->
					<xsl:call-template name="write-wizardenddatecontentvim-properties">
						<xsl:with-param name="locale" select="concat('_', $locales)" />
						<xsl:with-param name="fullFileName" select="$fullFileName" />
						<xsl:with-param name="entityElement" select="$entityElement" />
					</xsl:call-template>

					<!-- Finally call for the default locale -->
					<xsl:call-template name="write-wizardenddatecontentvim-properties">
						<xsl:with-param name="locale" />
						<xsl:with-param name="fullFileName" select="$fullFileName" />
						<xsl:with-param name="entityElement" select="$entityElement" />
					</xsl:call-template>

				</xsl:otherwise>

			</xsl:choose>

		</xsl:if>

	</xsl:template>

	<xsl:template name="write-wizardenddatecontentvim-properties">

		<xsl:param name="locale" />
		<xsl:param name="fullFileName" />
		<xsl:param name="entityElement" />

		<xsl:if
			test="count(//EvidenceEntities/Properties[@locale=$locale]/General)&gt;0">

			<xsl:variable name="generalProperties"
				select="//EvidenceEntities/Properties[@locale=$locale]/General" />

			<redirect:write select="concat($fullFileName, $locale, '.properties')">
				<xsl:call-template name="callGenerateProperties">
					<xsl:with-param name="propertyNode"
						select="$generalProperties/List.Title.Name" />
					<xsl:with-param name="evidenceNode" select="&apos;&apos;" />
				</xsl:call-template>

				<xsl:call-template name="callGenerateProperties">
					<xsl:with-param name="propertyNode"
						select="$generalProperties/List.Title.EvidenceDescription" />
					<xsl:with-param name="evidenceNode" select="&apos;&apos;" />
				</xsl:call-template>

				<xsl:call-template name="callGenerateProperties">
					<xsl:with-param name="propertyNode"
						select="$generalProperties/List.Title.Period" />
					<xsl:with-param name="evidenceNode" select="&apos;&apos;" />
				</xsl:call-template>

				<xsl:call-template name="callGenerateProperties">
					<xsl:with-param name="propertyNode"
						select="$generalProperties/List.Description.SelectEndDateEvidence" />
					<xsl:with-param name="evidenceNode" select="&apos;&apos;" />
				</xsl:call-template>

				<xsl:call-template name="callGenerateProperties">
					<xsl:with-param name="propertyNode"
						select="$generalProperties/List.Title.SelectEndDateEvidence" />
					<xsl:with-param name="evidenceNode" select="$entityElement" />
				</xsl:call-template>

				<xsl:call-template name="callGenerateProperties">
					<xsl:with-param name="propertyNode"
						select="$generalProperties/Field.Label.ChangeReason" />
					<xsl:with-param name="evidenceNode" select="&apos;&apos;" />
				</xsl:call-template>

				<xsl:call-template name="callGenerateProperties">
					<xsl:with-param name="propertyNode"
						select="$generalProperties/Field.Label.EndDate" />
					<xsl:with-param name="evidenceNode" select="&apos;&apos;" />
				</xsl:call-template>

				<xsl:call-template name="callGenerateProperties">
					<xsl:with-param name="propertyNode"
						select="$generalProperties/EndDateEvidence.Text.NoEvidence" />
					<xsl:with-param name="evidenceNode" select="&apos;&apos;" />
				</xsl:call-template>

			</redirect:write>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
<!-- END, 191675, JAY -->    