<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM

  PID 5725-H26

  Copyright IBM Corporation 2017,2019. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<xsl:stylesheet extension-element-prefixes="redirect xalan"
  xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect" version="1.0"
  xmlns:xalan="http://xml.apache.org/xslt" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!-- Global Variables -->
  <xsl:import href="../UICommon.xslt"/>
  <xsl:import href="IncomingAddToCaseUtilityTemplates.xslt"/>

  <xsl:output method="xml" indent="yes"/>


  <!--
    Template to write the content vim and associated properties files for the entities incomingAddToCase page

    @param entityElement XML Element for the entity
    @param baseDirectory Base Directory to write the files into
    @param fileName Name of the file to incomingAddToCase (with no file extension)
    @param baseAggregation The base for all dtls connect statements (e.g. dtls$)
  -->
  <xsl:template name="IncomingAddToCaseContentVIM">

    <xsl:param name="entityElement"/>
    <xsl:param name="baseDirectory"/>
    <xsl:param name="fileName"/>
    <xsl:param name="baseAggregation"/>
    <xsl:param name="endDateWizard"/>

    <xsl:variable name="capName"><xsl:value-of select="@name"/></xsl:variable>
    <xsl:variable name="EntityName"><xsl:value-of select="$capName"/></xsl:variable>
    <xsl:variable name="EntityVariable"><xsl:value-of select="$lcPrefix"/><xsl:value-of select="$capName"/></xsl:variable>

    <!-- Name of the entity -->
    <xsl:variable name="entityName"><xsl:value-of select="$entityElement/@name"/></xsl:variable>

    <!-- facade incomingAddToCase method -->
    <xsl:variable name="facadeIncomingAddToCaseMethod">
    		<xsl:text>incomingAddToCase</xsl:text><xsl:value-of select="$entityName"/><xsl:text>Evidence</xsl:text>
    </xsl:variable>
    <xsl:variable name="facadeIncomingAddToCaseDisplayMethod">
    		<xsl:text>read</xsl:text><xsl:value-of select="$entityName"/><xsl:text>Evidence</xsl:text>
    </xsl:variable>

    <!-- Full name and directory of the file to incomingAddToCase, minus the file extension -->
    <xsl:variable name="fullFileName"><xsl:value-of select="$baseDirectory"/><xsl:value-of select="$fileName"/></xsl:variable>

    <xsl:variable name="modifiable" select="@modify"/>
    <xsl:variable name="evidenceHeaderVim"><xsl:choose>
      <xsl:when test="$modifiable='Yes'"><xsl:value-of select="$evidenceHeaderModify"/></xsl:when>
      <xsl:otherwise><xsl:value-of select="$evidenceHeaderView"/></xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

    <!-- Write the main VIM file -->
    <redirect:write select="concat($fullFileName, '.vim')">

      <!-- add copyright notice -->
      <xsl:call-template name="printXMLCopyright">
        <xsl:with-param name="date" select="$date"/>
      </xsl:call-template>

      <VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">

        <!-- Page Parameters and their standard connections -->

        <!-- CaseID -->
        <PAGE_PARAMETER NAME="caseID"/>
        <PAGE_PARAMETER NAME="evidenceID"/>
        <PAGE_PARAMETER NAME="evidenceType"/>

        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="ACTION" PROPERTY="dtls${$facadeCaseIDAgg}$caseID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE  NAME="PAGE" PROPERTY="evidenceID" />
          <TARGET NAME="ACTION" PROPERTY="{$baseAggregation}dtls${$evidenceID}" />
        </CONNECT>

        <xsl:if test="$entityElement/Relationships/@exposeOperation='Yes'">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="RELATEDATTRIBUTES" PROPERTY="caseID"/>
          </CONNECT>
        </xsl:if>

        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="evidenceID" />
          <TARGET NAME="DISPLAY" PROPERTY="{$facadeViewKey}${$facadeEvidenceAgg}${$evidenceID}" />
        </CONNECT>

        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="evidenceType" />
          <TARGET NAME="DISPLAY" PROPERTY="{$facadeViewKey}${$facadeEvidenceAgg}${$evidenceType}" />
        </CONNECT>

        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID" />
          <TARGET NAME="DISPLAY" PROPERTY="{$facadeViewKey}${$facadeCaseIDAgg}$caseID" />
        </CONNECT>

        <!-- Parent -->
        <xsl:if test="count($entityElement/Relationships/Parent) &gt; 0">
          <xsl:variable name="idParamName"><xsl:call-template name="Utilities-getIncomingAddToCasePage-EvidenceIDParamName">
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="fromRelated" select="$entityElement/Relationships/Parent[1]/@name"/>
          </xsl:call-template></xsl:variable>
          <xsl:variable name="typeParamName"><xsl:call-template name="Utilities-getIncomingAddToCasePage-EvidenceTypeParamName">
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="fromRelated" select="$entityElement/Relationships/Parent[1]/@name"/>
          </xsl:call-template></xsl:variable>
          <PAGE_PARAMETER NAME="{$idParamName}"/>
          <PAGE_PARAMETER NAME="{$typeParamName}"/>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="{$idParamName}"/>
            <TARGET NAME="ACTION" PROPERTY="{$facadeParentEvidenceAgg}${$evidenceID}"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="{$typeParamName}"/>
            <TARGET NAME="ACTION" PROPERTY="{$facadeParentEvidenceAgg}${$evidenceType}"/>
          </CONNECT>
          <xsl:if test="$entityElement/Relationships/@exposeOperation='Yes'">
              <CONNECT>
                <SOURCE NAME="PAGE" PROPERTY="{$idParamName}"/>
                <TARGET NAME="RELATEDATTRIBUTES" PROPERTY="evidenceKey$evidenceID"/>
              </CONNECT>
              <CONNECT>
                <SOURCE NAME="PAGE" PROPERTY="{$typeParamName}"/>
                <TARGET NAME="RELATEDATTRIBUTES" PROPERTY="evidenceKey$evType"/>
              </CONNECT>
            </xsl:if>
        </xsl:if>

        <xsl:for-each select="UserInterfaceLayer/Cluster[@modify='Yes']/Field[@modify='No' and (not(@metatype) or (@metatype!=$metatypeRelatedEntityAttribute and @metatype!=$metatypeParentCaseParticipant))]">
    <xsl:variable name="nonEvidenceDtls">
      <xsl:choose>
        <xsl:when test="@notOnEntity='Yes'">nonEvidenceDetails$</xsl:when>
        <xsl:otherwise>dtls$</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result${$nonEvidenceDtls}{@columnName}"
          />
          <TARGET
            NAME="ACTION"
            PROPERTY="{$baseAggregation}{$nonEvidenceDtls}{@columnName}"
          />
        </CONNECT>
  </xsl:for-each>
  <!-- END, CR00099971 -->

    <xsl:for-each select="UserInterfaceLayer/Cluster[@modify='No']/Field[not(@metatype) or (@metatype!=$metatypeRelatedEntityAttribute and metatype!=$metatypeParentCaseParticipant)]">
       <xsl:variable name="nonEvidenceDtls">
         <xsl:choose>
           <xsl:when test="@notOnEntity='Yes'">nonEvidenceDetails$</xsl:when>
           <xsl:otherwise>dtls$</xsl:otherwise>
         </xsl:choose>
       </xsl:variable>
       <xsl:variable name="nonDisplayFieldName" select="@columnName"/>
    <xsl:choose>
      <!-- ignore fields that appear in any modify-screen cluster -->
      <xsl:when test="count(../../Cluster[@modify='Yes']/Field[@columnName=$nonDisplayFieldName])&gt;0"/>
      <xsl:otherwise>
    <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result${$nonEvidenceDtls}{@columnName}"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="{$baseAggregation}{$nonEvidenceDtls}{@columnName}"
    />
    </CONNECT>
      </xsl:otherwise>
    </xsl:choose>
     </xsl:for-each>

     <xsl:for-each select="UserInterfaceLayer/Cluster[not(@modify) or @modify!='No']">

      <xsl:for-each select="Field[not(@modify) or @modify!='No']">
       <xsl:variable name="nonEvidenceDtls">
         <xsl:choose>
           <xsl:when test="@notOnEntity='Yes'">nonEvidenceDetails$</xsl:when>
           <xsl:otherwise>dtls$</xsl:otherwise>
         </xsl:choose>
       </xsl:variable>

      <xsl:if test="@metatype=$metatypeCaseParticipantSearch or @metatype=$metatypeDisplayCaseParticipant">

      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result${$nonEvidenceDtls}{@columnName}"
        />
        <TARGET
          NAME="ACTION"
          PROPERTY="{$baseAggregation}{$nonEvidenceDtls}{@columnName}"
        />
      </CONNECT>
          </xsl:if>

          </xsl:for-each>

        </xsl:for-each>

        <xsl:for-each select="Relationships/RelatedEntityAttribute/Link">
          <xsl:if test="@allowModify!='Yes'">
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="dtls${@columnName}"
              />
              <TARGET
                NAME="ACTION"
                PROPERTY="{$baseAggregation}dtls${@columnName}"
              />
            </CONNECT>
          </xsl:if>
        </xsl:for-each>

      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$versionNo"
        />
        <TARGET
          NAME="ACTION"
          PROPERTY="{$baseAggregation}dtls$versionNo"
        />
      </CONNECT>


        <!-- Multiple Mandatory Parents -->
        <xsl:for-each select="$entityElement/Relationships/MandatoryParents/Parent">
          <xsl:variable name="aggregationName"><xsl:value-of select="translate(substring(@name, 0, 2), $ucletters, $lcletters)"/><xsl:value-of select="substring(@name, 2)"/></xsl:variable>
          <xsl:variable name="idParamName"><xsl:call-template name="Utilities-getIncomingAddToCasePage-EvidenceIDParamName">
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="fromRelated" select="@name"/>
          </xsl:call-template></xsl:variable>
          <xsl:variable name="typeParamName"><xsl:call-template name="Utilities-getIncomingAddToCasePage-EvidenceTypeParamName">
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="fromRelated" select="@name"/>
          </xsl:call-template></xsl:variable>
          <PAGE_PARAMETER NAME="{$idParamName}"/>
          <PAGE_PARAMETER NAME="{$typeParamName}"/>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="{$idParamName}"/>
            <TARGET NAME="ACTION" PROPERTY="{$aggregationName}ParEvKey$evidenceID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="{$typeParamName}"/>
            <TARGET NAME="ACTION" PROPERTY="{$aggregationName}ParEvKey$evType"/>
          </CONNECT>
        </xsl:for-each>

        <!-- PreAssociations -->
        <xsl:if test="$entityElement/Relationships/@preAssociation='Yes'">
          <xsl:variable name="idParamName"><xsl:call-template name="Utilities-getIncomingAddToCasePage-EvidenceIDParamName">
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="fromRelated" select="$entityElement/Relationships/PreAssociation[1]/@to"/>
          </xsl:call-template></xsl:variable>
          <xsl:variable name="typeParamName"><xsl:call-template name="Utilities-getIncomingAddToCasePage-EvidenceTypeParamName">
            <xsl:with-param name="entityElement" select="$entityElement"/>
            <xsl:with-param name="fromRelated" select="$entityElement/Relationships/PreAssociation[1]/@to"/>
          </xsl:call-template></xsl:variable>
          <PAGE_PARAMETER NAME="{$idParamName}"/>
          <PAGE_PARAMETER NAME="{$typeParamName}"/>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="{$idParamName}"/>
            <TARGET NAME="ACTION" PROPERTY="{$facadePreAssociationAgg}$evidenceID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="{$typeParamName}"/>
            <TARGET NAME="ACTION" PROPERTY="{$facadePreAssociationAgg}$evType"/>
          </CONNECT>
          <CONNECT>
      	    <SOURCE PROPERTY="evidenceID" NAME="PAGE"/>
      	    <TARGET PROPERTY="dtls$dtls$evidenceID" NAME="ACTION"/>
        	</CONNECT>
          <xsl:if test="$entityElement/Relationships/@exposeOperation='Yes'">
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="{$idParamName}"/>
              <TARGET NAME="RELATEDATTRIBUTES" PROPERTY="preAssocKey$evidenceID"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="PAGE" PROPERTY="{$typeParamName}"/>
              <TARGET NAME="RELATEDATTRIBUTES" PROPERTY="preAssocKey$evType"/>
            </CONNECT>
          </xsl:if>
        </xsl:if>

        <!-- Related Employment -->
        <xsl:if test="$entityElement/Relationships/Related/@to='Employment'">
          <PAGE_PARAMETER NAME="employmentID"/>
          <PAGE_PARAMETER NAME="employerConcernRoleID"/>
          <PAGE_PARAMETER NAME="caseParticipantRoleID"/>
        </xsl:if>

        <!-- Server Interfaces -->
        <SERVER_INTERFACE CLASS="{$prefix}{$facadeClass}" NAME="DISPLAY"
          OPERATION="{$facadeIncomingAddToCaseDisplayMethod}" PHASE="DISPLAY"/>
        <SERVER_INTERFACE CLASS="{$prefix}{$facadeClass}" NAME="ACTION"
          OPERATION="{$facadeIncomingAddToCaseMethod}" PHASE="ACTION"/>
        <xsl:if test="$entityElement/Relationships/Related/@to='Employment'">
          <SERVER_INTERFACE CLASS="{$prefix}{$facadeClass}"
          OPERATION="readNameByCaseParticipantRole" NAME="PARTICIPANTDETAILS"/>
        </xsl:if>
        <xsl:if test="$entityElement/Relationships/@exposeOperation='Yes'">
          <SERVER_INTERFACE CLASS="{$prefix}{$facadeClass}"
          OPERATION="get{$entityName}RelatedEntityAttributes" NAME="RELATEDATTRIBUTES"/>
        </xsl:if>

        <!-- Standard connections -->
        <xsl:if test="$entityElement/Relationships/Related/@to='Employment'">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PARTICIPANTDETAILS" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseParticipantRoleID"/>
            <TARGET NAME="PARTICIPANTDETAILS" PROPERTY="key$caseParticipantRoleID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE PROPERTY="employerConcernRoleID" NAME="PAGE"/>
            <TARGET PROPERTY="empCaseParticipantDetails$participantRoleID" NAME="ACTION"/>
          </CONNECT>
        </xsl:if>

        <xsl:if test="$entityElement/Relationships/Related/@to='Employment'">
          <CONNECT>
            <SOURCE PROPERTY="employmentID" NAME="PAGE"/>
            <TARGET PROPERTY="employmentID" NAME="ACTION"/>
          </CONNECT>
          <CONNECT>
            <SOURCE PROPERTY="empCaseParticipantRoleID" NAME="DISPLAY" />
            <TARGET PROPERTY="empCaseParticipantRoleID" NAME="ACTION" />
          </CONNECT>
        </xsl:if>

        <xsl:for-each select="$entityElement/UserInterfaceLayer/Cluster[not(@create) or @create!='No']">
          <xsl:for-each select="Field[@metatype=$metatypeCaseParticipantSearch or @fullCreateParticipant='Yes']">
            <xsl:call-template name="CreateParticipantSearchInterface">
              <xsl:with-param name="capName" select="$entityName"/>
              <xsl:with-param name="Field" select="."/>
              <xsl:with-param name="baseAggregation" select="$baseAggregation"/>
            </xsl:call-template>
          </xsl:for-each>

          <xsl:for-each select="Field[@metatype=$metatypeParentCaseParticipant]">
            <xsl:call-template name="ModifyViewPage_ParentParticipantSearchInterface">
              <xsl:with-param name="prefix" select="$prefix" />
              <xsl:with-param name="columnName" select="@columnName" />
            </xsl:call-template>
          </xsl:for-each>

          <xsl:for-each select="Field[@metatype=$metatypeEmployerCaseParticipant]">
            <xsl:call-template name="CreateContentVIM-EmployerSearchInterface">
              <xsl:with-param name="Field" select="."/>
              <xsl:with-param name="employmentClass" select="$employmentClass"/>
              <xsl:with-param name="entityName" select="$entityName"/>
              <xsl:with-param name="baseAggregation" select="$baseAggregation"/>
            </xsl:call-template>
          </xsl:for-each>
        </xsl:for-each>

        <INFORMATIONAL>
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="msg"/>
          </CONNECT>
        </INFORMATIONAL>

        <xsl:variable name="AllClusters"
          select="$entityElement/UserInterfaceLayer/Cluster[(not(@create) or @create!='No')]"/>

        <xsl:if test="count($entityElement/UserInterfaceLayer/Cluster[(not(@create) or @create!='No')]/EvidenceHeader)=0">
        <CLUSTER LABEL_WIDTH="40" NUM_COLS="2" BEHAVIOR="NONE">
          <INCLUDE FILE_NAME="{$evidenceHeaderModify}"/>
        </CLUSTER>
        </xsl:if>

        <xsl:call-template name="ModifyContentUIIncoming-WriteClusters">
          <xsl:with-param name="Clusters" select="$AllClusters"/>
          <xsl:with-param name="baseAggregation" select="$baseAggregation"/>
          <xsl:with-param name="capName" select="$entityName"/>
          <xsl:with-param name="modifiable" select="$modifiable"/>
        </xsl:call-template>
      </VIEW>

    </redirect:write>

    <!-- IncomingAddToCase Content Vim Properties -->
    <xsl:call-template name="write-all-locales-incomingAddToCasecontentvim-properties">
      <xsl:with-param name="locales" select="$localeList"/>
      <xsl:with-param name="fullFileName" select="$fullFileName"/>
      <xsl:with-param name="entityElement" select="$entityElement"/>
    </xsl:call-template>

  </xsl:template>

<xsl:template name="ModifyContentUIIncoming-WriteClusters">

  <xsl:param name="baseAggregation"/>
  <xsl:param name="Clusters"/>
  <xsl:param name="capName"/>
  <xsl:param name="modifiable"/>

  <xsl:variable name="evidenceHeaderVim"><xsl:choose>
    <xsl:when test="$modifiable='Yes'"><xsl:value-of select="$evidenceHeaderModify"/></xsl:when>
    <xsl:otherwise><xsl:value-of select="$evidenceHeaderView"/></xsl:otherwise>
  </xsl:choose>
  </xsl:variable>

  <xsl:for-each select="$Clusters">

    <xsl:sort select="@order" data-type="number"/>

    <xsl:variable name="clusterChildNodes" select="child::node()"/>

    <xsl:if test="count(Field)&gt;0 or count(SkipField)&gt;0 or count(EvidenceHeader)&gt;0 or count(LinkField)&gt;0">

      <xsl:variable name="firstFieldName" select="Field/@columnName"/>

      <xsl:variable name="firstAttributeType" select="Field/@metatype"/>

      <xsl:variable name="numCols">
        <xsl:choose>
          <xsl:when test="@numCols!=''"><xsl:value-of select="@numCols"/></xsl:when>
          <xsl:otherwise>
            <xsl:choose>
              <xsl:when test="$firstAttributeType=$metatypeComments">1</xsl:when>
              <xsl:otherwise>2</xsl:otherwise>
            </xsl:choose>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:variable>

      <xsl:variable name="labelWidth">
        <xsl:choose>
          <xsl:when test="@labelWidth!=''"><xsl:value-of select="@labelWidth"/></xsl:when>
          <xsl:otherwise>
            <xsl:choose>
              <xsl:when test="$numCols=1">20</xsl:when>
              <xsl:otherwise>40</xsl:otherwise>
            </xsl:choose>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:variable>

      <xsl:variable name="showLabelsInd">
        <xsl:choose>
          <xsl:when test="$firstAttributeType=$metatypeComments">false</xsl:when>
          <xsl:otherwise>true</xsl:otherwise>
        </xsl:choose>
      </xsl:variable>

      <!-- BEGIN CONOR  -->
      <!-- This choose was added originally for the optional participant pattern. i.e. CGISS have
           the Pregnancy Father example whereby a Pregnancy can be created without Father data.
           The Father data can be added on the modify screen if it was not added during the create. -->
      <xsl:choose>

          <!-- Optional participant fields should be in their own cluster -->
          <!-- Optional/Modifiable participant fields should be in their own cluster -->
          <!-- BEGIN, CR00101034, CD  -->
          <xsl:when
            test="count(Field)=1 and
                  (count(../../ServiceLayer/RelatedParticipantDetails[@columnName=$firstFieldName and @modifyParticipant='Yes']) &gt; 0 or
                   count(../../ServiceLayer/RelatedParticipantDetails[@columnName=$firstFieldName and @modifyParticipant='Many']) &gt; 0)">
          <!-- END, CR00101034  -->

            <!-- BEGIN, CR00101786, CD  -->
            <xsl:variable name="currentName">
              <xsl:choose>
                <xsl:when test="../../ServiceLayer/RelatedParticipantDetails[@columnName=$firstFieldName and @modifyParticipant='Yes'][1]/@name">
                  <xsl:value-of  select="../../ServiceLayer/RelatedParticipantDetails[@columnName=$firstFieldName and @modifyParticipant='Yes'][1]/@name"/>
                </xsl:when>
                <xsl:when test="../../ServiceLayer/RelatedParticipantDetails[@columnName=$firstFieldName and @modifyParticipant='Many'][1]/@name">
                  <xsl:value-of  select="../../ServiceLayer/RelatedParticipantDetails[@columnName=$firstFieldName and @modifyParticipant='Many'][1]/@name"/>
                </xsl:when>
                <xsl:otherwise/>
              </xsl:choose>
            </xsl:variable>

            <xsl:variable name="shortName">
              <xsl:call-template name="capitalize">
                <xsl:with-param name="convertThis" select="$currentName"/>
              </xsl:call-template>
            </xsl:variable>

            <xsl:variable name="caseParticipantDetailsAggregation">
              <xsl:choose>
                <xsl:when test="not($currentName) or $currentName=''">result$caseParticipantDetails$</xsl:when>
                <xsl:otherwise>result$<xsl:value-of select="$currentName"/>CaseParticipantDetails$</xsl:otherwise>
              </xsl:choose>
            </xsl:variable>
            <!-- BEGIN, CR00112535, CD -->
            <xsl:variable name="currentFieldMandatory">
              <xsl:choose>
                <xsl:when test="count(../Cluster/Field[@columnName=$firstFieldName and @mandatory='Yes']) &gt; 0">Yes</xsl:when>
                <xsl:otherwise>No</xsl:otherwise>
              </xsl:choose>
            </xsl:variable>
            <!-- END, CR00112535 -->

      <!-- BEGIN, CR00101034, CD  -->
      <xsl:if test="count(../../ServiceLayer/RelatedParticipantDetails[@columnName=$firstFieldName and @modifyParticipant='Yes']) &gt; 0">

      <SERVER_INTERFACE CLASS="{$prefix}{$facadeClass}" NAME="{$shortName}OptionalParticipant" OPERATION="get{$capName}{$shortName}OptionalParticipantDetails" PHASE="DISPLAY" />

      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="caseID"
        />
        <TARGET
          NAME="{$shortName}OptionalParticipant"
          PROPERTY="caseID"
        />
      </CONNECT>

      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="evidenceID"
        />
        <TARGET
          NAME="{$shortName}OptionalParticipant"
          PROPERTY="evidenceID"
        />
      </CONNECT>

      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="evidenceType"
        />
        <TARGET
          NAME="{$shortName}OptionalParticipant"
          PROPERTY="evType"
        />
      </CONNECT>

      </xsl:if>
      <!-- END, CR00101034  -->

      <xsl:variable name="caseParticipantNameField">
        <!-- We've already checked there's only one field, so we now can refer directly to it. -->
        <FIELD>
          <xsl:if test="Field[1]/@label!=''">
            <xsl:attribute name="LABEL"><xsl:value-of select="Field[1]/@label"/></xsl:attribute>
          </xsl:if>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="{$caseParticipantDetailsAggregation}caseParticipantName"
            />
          </CONNECT>
        </FIELD>
      </xsl:variable>

      <!-- BEGIN, CR00113300, CD -->
      <xsl:variable name="caseParticipantUpdateFields">
        <xsl:choose>
          <xsl:when test="Field[1]/@fullCreateParticipant='Yes'">

            <xsl:call-template name="printFullCreateParticipantClusters">
              <xsl:with-param name="capName" select="$capName"/>
              <xsl:with-param name="currentField" select="Field[1]"/>
              <!-- BEGIN, CR00112513, POB -->
              <xsl:with-param name="nsStruct" select="Field[1]/@nsStruct"/>
              <!-- END, CR00112513 -->
            </xsl:call-template>

          </xsl:when>

          <!-- Case Participant Search -->
          <xsl:when test="Field[1]/@metatype=$metatypeCaseParticipantSearch">

            <FIELD>
            <xsl:if test="count(../../ServiceLayer/RelatedParticipantDetails[@columnName=$firstFieldName and @modifyParticipant='Yes']) &gt; 0">
               <xsl:if test="Field[1]/@label!=''">
                <xsl:attribute name="LABEL">
                  <xsl:value-of select="Field[1]/@label"/>
                </xsl:attribute>
              </xsl:if>
            </xsl:if>
            <xsl:attribute name="USE_BLANK"><xsl:choose><xsl:when test="$currentFieldMandatory='Yes'">false</xsl:when><xsl:otherwise>true</xsl:otherwise></xsl:choose></xsl:attribute>
            <xsl:attribute name="USE_DEFAULT">false</xsl:attribute>


              <CONNECT>
                <INITIAL HIDDEN_PROPERTY="caseParticipantRoleID" NAME="{$capName}{$firstFieldName}"
                  PROPERTY="nameAndAgeOpt"/>
              </CONNECT>
              <!-- Removing this for the moment. We can't be sure that the
                   CPRID we may have created on the create screen is going to
                   match any of the ones returned in the drop-down. This is
                   because we may have set a CreateCaseParticipant roleType
                   that is not included in the SearchType list.
              <xsl:if test="count(../../ServiceLayer/RelatedParticipantDetails[@columnName=$firstFieldName and @modifyParticipant='Yes']) &gt; 0">
              <CONNECT>
                <SOURCE PROPERTY="{$baseAggregation}{$firstFieldName}" NAME="DISPLAY"/>
              </CONNECT>
              </xsl:if>
              -->
              <xsl:variable
                name="name"
                select="../../ServiceLayer/RelatedParticipantDetails[@columnName=$firstFieldName]/@name"/>
              <xsl:variable name="actionColumnName">
                <xsl:choose>
                  <xsl:when test="$currentName!=''"><xsl:value-of select="$baseAggregation"/><xsl:value-of select="$currentName"/>CaseParticipantDetails$caseParticipantRoleID</xsl:when>
                  <xsl:otherwise><xsl:value-of select="$baseAggregation"/>CaseParticipantDetails$caseParticipantRoleID</xsl:otherwise>
                </xsl:choose>
              </xsl:variable>
              <CONNECT>
                <TARGET NAME="ACTION" PROPERTY="{$actionColumnName}"/>
              </CONNECT>

            </FIELD>

          </xsl:when>
          <xsl:otherwise/>
        </xsl:choose>
      </xsl:variable>

      <xsl:choose>
        <xsl:when test="count(../../ServiceLayer/RelatedParticipantDetails[@columnName=$firstFieldName and @modifyParticipant='Yes']) &gt; 0">
          <!-- write 2 conditional clusters here -->
          <!-- this is for the Optional Participant Pattern -->
          <CLUSTER NUM_COLS="1" LABEL_WIDTH="20" BEHAVIOR="NONE">

            <xsl:if test="@label!=''">
              <xsl:attribute name="TITLE"><xsl:value-of select="@label"/></xsl:attribute>
            </xsl:if>

            <CONDITION>
              <IS_TRUE
                NAME="{$shortName}OptionalParticipant"
                PROPERTY="displayParticipantLink"
              />
            </CONDITION>

            <xsl:copy-of select="$caseParticipantNameField"/>

          </CLUSTER>

          <CLUSTER NUM_COLS="1" BEHAVIOR="NONE" STYLE="cluster-cpr-no-internal-padding">

            <xsl:choose>
              <xsl:when test="@metatype!=$metatypeCaseParticipantSearch">
                <xsl:attribute name="SHOW_LABELS">false</xsl:attribute>
              </xsl:when>
              <xsl:otherwise>
                <xsl:attribute name="LABEL_WIDTH">20</xsl:attribute>
              </xsl:otherwise>
            </xsl:choose>

            <xsl:if test="@label!=''">
              <xsl:attribute name="TITLE">
                <xsl:value-of select="@label"/>
              </xsl:attribute>
            </xsl:if>

            <xsl:if test="@description!=''">
              <xsl:attribute name="DESCRIPTION">
                <xsl:value-of select="@description"/>
              </xsl:attribute>
            </xsl:if>

            <CONDITION>
              <IS_TRUE
                NAME="{$shortName}OptionalParticipant"
                PROPERTY="displayAddParticipantCluster"
              />
            </CONDITION>

            <xsl:copy-of select="$caseParticipantUpdateFields"/>

          </CLUSTER>
        </xsl:when>
        <xsl:when test="count(../../ServiceLayer/RelatedParticipantDetails[@columnName=$firstFieldName and @modifyParticipant='Many']) &gt; 0">
        <!-- write 1 cluster here -->
        <!-- BEGIN, CR00112535, CD -->
        <!-- this is for the Modifiable Participant Pattern -->
          <CLUSTER NUM_COLS="1" BEHAVIOR="NONE" LABEL_WIDTH="20" STYLE="cluster-cpr-no-internal-padding">

            <xsl:if test="@label!=''">
              <xsl:attribute name="TITLE"><xsl:value-of select="@label"/></xsl:attribute>
            </xsl:if>

            <xsl:if test="@description!=''">
              <xsl:attribute name="DESCRIPTION">
                <xsl:value-of select="@description"/>
              </xsl:attribute>
            </xsl:if>

            <xsl:copy-of select="$caseParticipantNameField"/>

            <xsl:copy-of select="$caseParticipantUpdateFields"/>

            </CLUSTER>
            <!-- END, CR00112535 -->
        </xsl:when>
        <xsl:otherwise/>
        <!-- END, CR00113300 -->
      </xsl:choose>
      <!-- END, CR00101786 -->

          </xsl:when>


          <xsl:otherwise>

      <CLUSTER
        LABEL_WIDTH="{$labelWidth}"
        NUM_COLS="{$numCols}"
        SHOW_LABELS="{$showLabelsInd}"
        BEHAVIOR="NONE"
      >
        <xsl:if test="@label!=''">
          <xsl:attribute name="TITLE"><xsl:value-of select="@label"/></xsl:attribute>
        </xsl:if>

        <!-- BEGIN, CR00100369, POB -->
        <xsl:for-each select="$clusterChildNodes">
          <xsl:sort select="@order" data-type="number"/>

          <xsl:choose>
            <xsl:when test="local-name()='SkipField'">
              <FIELD CONTROL="SKIP"/>
            </xsl:when>

            <!-- BEGIN, CR00113054, DG -->
            <xsl:when test="local-name()='LinkField'">
              <FIELD>
              <xsl:if test="@label!=''">
                <xsl:attribute name="LABEL"><xsl:value-of select="@label"/></xsl:attribute>
              </xsl:if>
              <CONNECT>
                <SOURCE
                  NAME="DISPLAY"
                  PROPERTY="{@linkText}"
                />
              </CONNECT>
                <LINK PAGE_ID="{@pageName}">
                <xsl:for-each select="Parameters/Parameter">
                  <CONNECT>
                    <SOURCE NAME="{@source}" PROPERTY="{@from}" />
                    <TARGET NAME="PAGE" PROPERTY="{@to}" />
                  </CONNECT>
                </xsl:for-each>
                </LINK>
              </FIELD>
            </xsl:when>
            <!-- END, CR00113054 -->

            <!-- BEGIN, CR00101071, POB -->
            <xsl:when test="local-name()='EvidenceHeader'">
              <!-- BEGIN, CR00100657, POB -->
              <INCLUDE FILE_NAME="{$evidenceHeaderVim}"/>
              <!-- END, CR00100657, POB -->
            </xsl:when>
            <!-- END, CR00101071 -->
            <xsl:when test="local-name()='Field'">
              <!-- END, CR00100369, POB -->

              <xsl:variable name="currentFieldColumnName" select="@columnName"/>

              <!-- To figure out if a field is mandatory we must check whether any
                   of the instances of the field defined in the EUIM have mandatory='Yes' -->
              <xsl:variable name="currentFieldMandatory">
                <xsl:choose>
                  <xsl:when test="count(../../Cluster/Field[@columnName=$currentFieldColumnName and @mandatory='Yes'])&gt;0">Yes</xsl:when>
                  <xsl:otherwise>No</xsl:otherwise>
                </xsl:choose>
              </xsl:variable>

          <xsl:choose>

            <!-- BEGIN, CR00098722, CD -->
            <xsl:when test="@metatype=$metatypeRelatedEntityAttribute">
            <FIELD>
              <!-- BEGIN, CR00100993, CD -->
              <xsl:if test="@label!=''">
                <xsl:attribute name="LABEL"><xsl:value-of select="@label"/></xsl:attribute>
              </xsl:if>
              <!-- END, CR00100993 -->
              <!-- Copy in the USE_DEFAULT and USE_BLANK options specified in the EUIM file -->
              <xsl:if test="$currentFieldMandatory='No'">
                <xsl:if test="@use_blank!=''">
                  <xsl:attribute name="USE_BLANK"><xsl:value-of select="@use_blank"/></xsl:attribute>
                </xsl:if>
                <xsl:if test="@use_default!=''">
                  <xsl:attribute name="USE_DEFAULT"><xsl:value-of select="@use_default"/></xsl:attribute>
                </xsl:if>
              </xsl:if>
              <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="result$relatedEntityAttributes${@columnName}"/>
              </CONNECT>
            </FIELD>
            </xsl:when>
            <!-- END, CR00098722 -->

            <xsl:when test="@metatype=$metatypeCaseParticipantSearch or @metatype=$metatypeDisplayCaseParticipant">

              <xsl:choose>

                <xsl:when test="@createParticipantOnModify='Yes'">
                  <CONTAINER>

                    <FIELD>
                      <xsl:if test="@label!=''">
                        <!-- BEGIN, PADDY -->
                        <xsl:attribute name="LABEL"><xsl:value-of select="@label"/></xsl:attribute>
                        <!-- END, PADDY -->
                      </xsl:if>
                      <!-- Copy in the USE_DEFAULT and USE_BLANK options specified in the EUIM file -->
                      <xsl:if test="$currentFieldMandatory='No'">
                        <xsl:if test="@use_blank!=''">
                          <xsl:attribute name="USE_BLANK"><xsl:value-of select="@use_blank"/></xsl:attribute>
                        </xsl:if>
                        <xsl:if test="@use_default!=''">
                          <xsl:attribute name="USE_DEFAULT"><xsl:value-of select="@use_default"/></xsl:attribute>
                        </xsl:if>
                      </xsl:if>

                      <!-- This conditional is used where there is more than one aggregated association between
                        the caseParticipantDetails struct and the read<EntityName>Details struct. We need
                        to distingush between the 2 so the metadata EvidenceEntity/Cluster/Field/@name
                        attribute is populated.
                        NOTE: the value given here has to be exactly the same as that given to the start of
                        the additional aggregated association in the model.
                        The association modelled must have the the following naming convention :

                        <value equal to that in metadata><ProductName>ReadCaseParticipantDetails

                        A business scenario where we might need to do this would be where say the Case Participant's
                        details as well as that of the Case Participants employer are displayed upon the same page
                      -->
                      <xsl:variable name="fieldColumnName" select="@columnName"/>

                      <xsl:variable name="attributeName">
                        <xsl:choose>
                          <xsl:when test="../../../ServiceLayer/RelatedParticipantDetails[@columnName=$fieldColumnName and @createParticipantType=$ParticipantTypeEmployer]">employerName</xsl:when>
                          <xsl:otherwise>caseParticipantName</xsl:otherwise>
                        </xsl:choose>
                      </xsl:variable>

                      <xsl:variable name="readParticipantNameDetails">
                        <xsl:choose>
                          <xsl:when test="@name!=''"><xsl:value-of select="@name"/>CaseParticipantDetails</xsl:when>
                          <xsl:otherwise>caseParticipantDetails</xsl:otherwise>
                        </xsl:choose>
                      </xsl:variable>

                      <CONNECT>
                        <SOURCE
                          NAME="DISPLAY"
                          PROPERTY="{$readParticipantNameDetails}${$attributeName}"
                        />
                      </CONNECT>
                    </FIELD>
                    <FIELD>
                      <CONNECT>
                        <SOURCE
                          NAME="DISPLAY"
                          PROPERTY="modify{@relatedCaseParticipantName}"
                        />
                      </CONNECT>
                    </FIELD>


                  </CONTAINER>

                </xsl:when>
                <xsl:otherwise>
                  <FIELD>
                    <xsl:if test="@label!=''">
                      <xsl:attribute name="LABEL"><xsl:value-of select="@label"/></xsl:attribute>
                    </xsl:if>
                    <!-- Copy in the USE_DEFAULT and USE_BLANK options specified in the EUIM file -->
                    <xsl:if test="$currentFieldMandatory='No'">
                      <xsl:if test="@use_blank!=''">
                        <xsl:attribute name="USE_BLANK"><xsl:value-of select="@use_blank"/></xsl:attribute>
                      </xsl:if>
                      <xsl:if test="@use_default!=''">
                        <xsl:attribute name="USE_DEFAULT"><xsl:value-of select="@use_default"/></xsl:attribute>
                      </xsl:if>
                    </xsl:if>

                    <xsl:variable name="fieldColumnName" select="@columnName"/>

                    <xsl:variable name="attributeName">
                      <xsl:choose>
                        <xsl:when test="../../../ServiceLayer/RelatedParticipantDetails[@columnName=$fieldColumnName and @createParticipantType=$ParticipantTypeEmployer]">employerName</xsl:when>
                        <xsl:otherwise>caseParticipantName</xsl:otherwise>
                      </xsl:choose>
                    </xsl:variable>

                    <xsl:variable name="readParticipantNameDetails">
                      <xsl:choose>
                        <xsl:when test="@name!=''"><xsl:value-of select="@name"/>CaseParticipantDetails</xsl:when>
                        <xsl:otherwise>caseParticipantDetails</xsl:otherwise>
                      </xsl:choose>
                    </xsl:variable>

                    <CONNECT>
                      <SOURCE
                        NAME="DISPLAY"
                        PROPERTY="{$readParticipantNameDetails}${$attributeName}"
                      />
                    </CONNECT>
                  </FIELD>

                </xsl:otherwise>

              </xsl:choose>

            </xsl:when>

            <!-- Employer Case Participants -->

            <xsl:when test="@metatype=$metatypeEmployerCaseParticipant">

              <xsl:variable name="fieldColumnName" select="@columnName"/>

              <xsl:variable name="attributeName">
                <xsl:choose>
                  <xsl:when test="../../../ServiceLayer/RelatedParticipantDetails[@columnName=$fieldColumnName and @createParticipantType=$ParticipantTypeEmployer]">employerName</xsl:when>
                  <xsl:otherwise>caseParticipantName</xsl:otherwise>
                </xsl:choose>
              </xsl:variable>

              <FIELD>
                <xsl:if test="@label!=''">
                  <xsl:attribute name="LABEL"><xsl:value-of select="@label"/></xsl:attribute>
                </xsl:if>

                <CONNECT>
                   <SOURCE NAME="{$capName}{@columnName}" PROPERTY="employerName"/>
                 </CONNECT>
              </FIELD>

            </xsl:when>

            <xsl:when test="@metatype=$metatypeParentCaseParticipant and
            not(count(../Field)=1 and count(../../../ServiceLayer/RelatedParticipantDetails[@columnName=../Field[1]/@columnName and @modifyParticipant='Yes']) >
            0)">

              <FIELD>
                <xsl:if test="@label!=''">
                  <xsl:attribute name="LABEL"><xsl:value-of select="@label"/></xsl:attribute>
                </xsl:if>

                <CONNECT>
                  <SOURCE
                    NAME="{@columnName}"
                    PROPERTY="nameAndAgeOpt"
                  />
                </CONNECT>

              </FIELD>

            </xsl:when>

            <xsl:when test="@createCaseParticipant='Yes' and @name!=''">

              <xsl:variable name="fieldColumnName" select="@columnName"/>

              <xsl:variable name="attributeName">
                <xsl:choose>
                  <xsl:when test="../../../ServiceLayer/RelatedParticipantDetails[@columnName=$fieldColumnName and @createParticipantType=$ParticipantTypeEmployer]">employerName</xsl:when>
                  <xsl:otherwise>caseParticipantName</xsl:otherwise>
                </xsl:choose>
              </xsl:variable>

              <FIELD>
                <xsl:if test="@label!=''">
                  <xsl:attribute name="LABEL"><xsl:value-of select="@label"/></xsl:attribute>
                </xsl:if>

                <CONNECT>
                  <SOURCE
                    NAME="DISPLAY"
                    PROPERTY="{@name}CaseParticipantDetails${$attributeName}"
                  />
                </CONNECT>
              </FIELD>

            </xsl:when>

            <xsl:when test="@metatype=$metatypeAlternateIDSearch">
              <FIELD>
                <xsl:if test="@label!=''">
                  <xsl:attribute name="LABEL"><xsl:value-of select="@label"/></xsl:attribute>
                </xsl:if>
                <CONNECT>
                  <INITIAL
                    NAME="DISPLAY"
                    PROPERTY="{@columnName}"
                  />
                </CONNECT>
                <CONNECT>
                  <SOURCE
                    NAME="DISPLAY"
                    PROPERTY="{@columnName}"
                  />
                </CONNECT>
                <!-- BEGIN, CR00100657, POB -->
                <xsl:if test="$modifiable='Yes'">
                <CONNECT>
                  <TARGET
                    NAME="ACTION"
                    PROPERTY="{@columnName}"
                  />
                </CONNECT>
                  </xsl:if>
                  <!-- END, CR00100657, POB -->
              </FIELD>
            </xsl:when>

            <xsl:when test="@metatype=$metatypeRepresentativeLink">
              <FIELD>
                <xsl:if test="@label!=''">
                  <xsl:attribute name="LABEL"><xsl:value-of select="@label"/></xsl:attribute>
                </xsl:if>

                <xsl:if test="RelatedAttribute[@displayAttribute!='']">

                  <xsl:variable name="displayAttribute"><xsl:value-of select="./RelatedAttribute/@displayAttribute"/></xsl:variable>
                  <xsl:variable name="linkAttribute"><xsl:value-of select="./RelatedAttribute/@linkAttribute"/></xsl:variable>

                  <CONNECT>
                    <SOURCE NAME="DISPLAY" PROPERTY="{$displayAttribute}" />
                  </CONNECT>

                </xsl:if>

              </FIELD>
            </xsl:when>
            <xsl:otherwise>

              <xsl:variable name="columnName" select="@columnName"/>

              <xsl:variable name="fieldHeight">
                <xsl:choose>
                  <xsl:when test="@metatype=$metatypeComments">5</xsl:when>
                  <xsl:otherwise>1</xsl:otherwise>
                </xsl:choose>
              </xsl:variable>

              <xsl:variable name="nonEvidenceDtls">
                <xsl:choose>
                  <xsl:when test="@notOnEntity='Yes'">nonEvidenceDetails$</xsl:when>
                  <xsl:otherwise>dtls$</xsl:otherwise>
                </xsl:choose>
              </xsl:variable>

              <xsl:variable name="currentFieldColumnName" select="@columnName"/>

              <!-- To figure out if a field is mandatory we must check whether any
                   of the instances of the field defined in the EUIM have mandatory='Yes' -->
              <xsl:variable name="currentFieldMandatory">
                <xsl:choose>
                  <xsl:when test="count(../../Cluster/Field[@columnName=$currentFieldColumnName and @mandatory='Yes'])&gt;0">Yes</xsl:when>
                  <xsl:otherwise>No</xsl:otherwise>
                </xsl:choose>
              </xsl:variable>

              <FIELD
                HEIGHT="{$fieldHeight}"
                >
                <xsl:if test="@label!=''">
                  <!-- BEGIN, PADDY -->
                  <xsl:attribute name="LABEL"><xsl:value-of select="@label"/></xsl:attribute>
                  <!-- END, PADDY -->
                </xsl:if>
                <!-- Copy in the USE_DEFAULT and USE_BLANK options specified in the EUIM file -->
                <xsl:if test="$currentFieldMandatory='No'">
                  <xsl:if test="@use_blank!=''">
                    <xsl:attribute name="USE_BLANK"><xsl:value-of select="@use_blank"/></xsl:attribute>
                  </xsl:if>
                  <xsl:if test="@use_default!=''">
                    <xsl:attribute name="USE_DEFAULT"><xsl:value-of select="@use_default"/></xsl:attribute>
                  </xsl:if>
                </xsl:if>

                <xsl:choose>
                  <xsl:when test="@phase!=''">
                    <CONNECT>
                      <SOURCE
                        NAME="{@phase}"
                        PROPERTY="{@columnName}"
                      />
                    </CONNECT>
                  </xsl:when>
                  <xsl:otherwise>
                    <CONNECT>
                      <SOURCE
                        NAME="DISPLAY"
                        PROPERTY="result${$nonEvidenceDtls}{@columnName}"
                      />
                    </CONNECT>
                  </xsl:otherwise>
                </xsl:choose>

                <xsl:choose>
                  <!-- BEGIN, CR00099871, POB -->
                  <xsl:when test="@readOnly='Yes' or @modify='No' or @metatype=$metatypeAssociationID"/>
                  <!-- END, CR00099871 -->
                  <xsl:otherwise>
                    <!-- BEGIN, CR00100657, POB -->
                    <xsl:if test="$modifiable='Yes'">
                    <CONNECT>
                      <TARGET
                        NAME="ACTION"
                        PROPERTY="{$baseAggregation}{$nonEvidenceDtls}{@columnName}"
                      />
                    </CONNECT>
                      </xsl:if>
                      <!-- END, CR00100657, POB -->
                  </xsl:otherwise>
                </xsl:choose>

              </FIELD>
            </xsl:otherwise>
          </xsl:choose>
            </xsl:when>
            </xsl:choose>
        </xsl:for-each>
      </CLUSTER>

      </xsl:otherwise>
    </xsl:choose>
    <!-- END CONOR -->

    </xsl:if>
  </xsl:for-each>
</xsl:template>


  <xsl:template name="IncomingAddToCaseContentVIM-EmployerSearchInterface">

    <xsl:param name="Field"/>
    <xsl:param name="employmentClass"/>
    <xsl:param name="baseAggregation"/>
    <xsl:param name="entityName"/>

    <SERVER_INTERFACE CLASS="{$prefix}{$facadeClass}" NAME="{$entityName}{@columnName}"
      OPERATION="{$employmentReadByCaseParticipantMethod}"/>

    <CONNECT>
      <SOURCE NAME="PAGE" PROPERTY="employerConcernRoleID"/>
      <TARGET NAME="{$entityName}{@columnName}" PROPERTY="concernRoleID"/>
    </CONNECT>

  </xsl:template>

  <!--iterate through each token, generating each element-->
  <xsl:template name="write-all-locales-incomingAddToCasecontentvim-properties">

    <xsl:param name="locales"/>
    <xsl:param name="fullFileName"/>
    <xsl:param name="entityElement"/>

    <!--tokens still exist-->
    <xsl:if test="$locales">

      <xsl:choose>

        <!--more than one-->
        <xsl:when test="contains($locales,',')">

          <xsl:call-template name="write-incomingAddToCasecontentvim-properties">
            <xsl:with-param name="locale" select="concat('_', substring-before($locales,','))"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
          </xsl:call-template>

          <!-- Recursively call self to process all locales -->
          <xsl:call-template name="write-all-locales-incomingAddToCasecontentvim-properties">
            <xsl:with-param name="locales" select="substring-after($locales,',')"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
          </xsl:call-template>

        </xsl:when>

        <!--only one token left-->
        <xsl:otherwise>

          <!-- Call for the final locale -->
          <xsl:call-template name="write-incomingAddToCasecontentvim-properties">
            <xsl:with-param name="locale" select="concat('_', $locales)"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
          </xsl:call-template>

          <!-- Finally call for the default locale -->
          <xsl:call-template name="write-incomingAddToCasecontentvim-properties">
            <xsl:with-param name="locale"/>
            <xsl:with-param name="fullFileName" select="$fullFileName"/>
            <xsl:with-param name="entityElement" select="$entityElement"/>
          </xsl:call-template>

        </xsl:otherwise>

      </xsl:choose>

    </xsl:if>

  </xsl:template>

  <xsl:template name="write-incomingAddToCasecontentvim-properties">

    <xsl:param name="locale"/>
    <xsl:param name="fullFileName"/>
    <xsl:param name="entityElement"/>

    <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>

    <xsl:if test="count($entityElement//*[@locale=$locale])&gt;0">
      <redirect:write select="concat($fullFileName, $locale, '.properties')">

        <xsl:for-each select="$entityElement/UserInterfaceLayer/Cluster[not(@create) or @create!='No']">

          <xsl:if test="@label!=''">
            <xsl:if test="count(./Label[@locale=$locale])&gt;0">
              <xsl:value-of select="@label"/>=<xsl:value-of select="./Label[@locale=$locale]/@value"/>
              <xsl:text>&#xa;</xsl:text>
            </xsl:if>
            <!--
            If help is present on this Cluster title it is a comments Cluster.
          -->
            <xsl:if test="@help!=&apos;&apos;">
              <xsl:if test="count(./Help[@locale=$locale])&gt;0">
                <xsl:value-of select="@help"/>=<xsl:value-of select="./Help[@locale=$locale]/@value"/>
                <xsl:text>&#xa;</xsl:text>
              </xsl:if>
            </xsl:if>
          </xsl:if>
          <xsl:if test="@description!=''">
            <xsl:if test="count(./Description[@locale=$locale])&gt;0">
              <xsl:value-of select="@description"/>=<xsl:value-of
                select="./Description[@locale=$locale]/@value"/>
              <xsl:text>&#xa;</xsl:text>
            </xsl:if>
          </xsl:if>

          <xsl:for-each select="Field">
            <xsl:if test="@label!=''">
              <xsl:if test="count(./Label[@locale=$locale])&gt;0">
                <xsl:value-of select="@label"/>=<xsl:value-of
                  select="./Label[@locale=$locale]/@value"/>
                <xsl:text>&#xa;</xsl:text>
              </xsl:if>
              <xsl:if test="@help!=&apos;&apos;">
                <xsl:if test="count(./Help[@locale=$locale])&gt;0">
                  <xsl:value-of select="@help"/>=<xsl:value-of
                    select="./Help[@locale=$locale]/@value"/>
                  <xsl:text>&#xa;</xsl:text>
                </xsl:if>
              </xsl:if>
            </xsl:if>

            <xsl:if test="@fullCreateParticipant='Yes'">
              <xsl:call-template name="printFullCreateParticipantClusterDynamicProperties">
                <xsl:with-param name="dynamicProperties" select="Property"/>
                <xsl:with-param name="locale" select="$locale"/>
              </xsl:call-template>
            </xsl:if>
          </xsl:for-each>

        </xsl:for-each>


        <xsl:if test="count($entityElement/UserInterfaceLayer/Cluster[not(@create) or @create!='No']/Field[@fullCreateParticipant='Yes']) &gt; 0">
          <xsl:call-template name="printFullCreateParticipantClusterStaticProperties">
            <xsl:with-param name="generalProperties" select="$generalProperties"/>
          </xsl:call-template>
        </xsl:if>
      </redirect:write>
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>
