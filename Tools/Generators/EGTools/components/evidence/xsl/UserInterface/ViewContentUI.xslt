<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2006,2019. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright (c) 2006-2008,2010 Curam Software Ltd.  All rights reserved.

This software is the confidential and proprietary information of Curam
Software, Ltd. ("Confidential Information").  You shall not
disclose such Confidential Information and shall use it only in accordance
with the terms of the license agreement you entered into with Curam Software.
-->
<xsl:stylesheet
  extension-element-prefixes="redirect xalan"
  xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect"
  version="1.0"
  xmlns:xalan="http://xml.apache.org/xslt"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>

<!-- Global Variables -->
<xsl:import href="UICommon.xslt"/>

<xsl:output method="xml" indent="yes"/>

<xsl:template name="ViewContentUI">

  <xsl:param name="path" />
  <xsl:param name="UIName" />

  <!-- BEGIN, CR00219910, CD -->
  <xsl:param name="viewVersion" />

  <!-- Evidence Entity Specific Variables -->
  <xsl:variable name="capName"><xsl:value-of select="@name"/></xsl:variable>
  <xsl:variable name="capNameCaseType"><xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/></xsl:variable>
  <xsl:variable name="EntityName"><xsl:value-of select="$capName"/></xsl:variable>

  <xsl:variable name="filepath"><xsl:value-of select="$path"/><xsl:value-of select="$UIName"/></xsl:variable>

  <xsl:variable name="facadeViewMethod">
    <xsl:choose>
      <xsl:when test="$viewVersion=$businessObjectViewVersion">read<xsl:value-of select="$EntityName"/>Object</xsl:when>
      <xsl:otherwise>read<xsl:value-of select="$EntityName"/>Evidence</xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <xsl:variable name="headerView">
    <xsl:choose>
      <xsl:when test="$viewVersion=$businessObjectViewVersion or $viewVersion=$snapshotViewVersion"><xsl:value-of select="$evidenceHeaderForObjectView" /></xsl:when>
      <xsl:otherwise><xsl:value-of select="$evidenceHeaderView" /></xsl:otherwise>
    </xsl:choose>
  </xsl:variable>
  <!-- END, CR00219910 -->
  <xsl:variable name="childLevelNo">
    <xsl:call-template name="GetChildLevel">
      <xsl:with-param name="capName" select="$capName"/>
    </xsl:call-template>
  </xsl:variable>

  <redirect:write select="concat($filepath, '.vim')">

  <xsl:call-template name="printXMLCopyright">
    <xsl:with-param name="date" select="$date"/>
  </xsl:call-template>

  <VIEW
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
  >
  <xsl:choose>

    <xsl:when test="$viewVersion=$businessObjectViewVersion">
      <PAGE_PARAMETER NAME="successionID"/>
    </xsl:when>

    <xsl:otherwise>
      <PAGE_PARAMETER NAME="caseID"/>
      <PAGE_PARAMETER NAME="evidenceID"/>
      <PAGE_PARAMETER NAME="evidenceType"/>

    <!-- For each optional Page Parameter create link -->
    <xsl:for-each select="UserInterfaceLayer/PageParams/PageParam">
      <PAGE_PARAMETER NAME="{To/@property}"/>
    </xsl:for-each>

    <xsl:for-each select="UserInterfaceLayer/Cluster[not(@view) or @view!='No']">
      <xsl:for-each select="Field[@metatype=$metatypeParentCaseParticipant]">
        <xsl:call-template name="ModifyViewPage_ParentParticipantSearchInterface">
          <xsl:with-param name="prefix" select="$prefix" />
          <xsl:with-param name="columnName" select="@columnName" />
        </xsl:call-template>
      </xsl:for-each>
    </xsl:for-each>
    </xsl:otherwise>
  </xsl:choose>

  <SERVER_INTERFACE
    CLASS="{$prefix}{$facadeClass}"
    NAME="DISPLAY"
    OPERATION="{$facadeViewMethod}"
  />

  <xsl:choose>
    <xsl:when test="$viewVersion=$businessObjectViewVersion">
      <CONNECT>
        <SOURCE PROPERTY="successionID" NAME="PAGE"/>
        <TARGET PROPERTY="key$successionID" NAME="DISPLAY"/>
      </CONNECT>
    </xsl:when>
    <xsl:otherwise>
    <CONNECT>
      <SOURCE
        NAME="PAGE"
        PROPERTY="evidenceID"
      />
      <TARGET
        NAME="DISPLAY"
        PROPERTY="{$facadeViewKey}${$facadeEvidenceAgg}${$evidenceID}"
      />
    </CONNECT>
    <CONNECT>
      <SOURCE
        NAME="PAGE"
        PROPERTY="evidenceType"
      />
      <TARGET
        NAME="DISPLAY"
        PROPERTY="{$facadeViewKey}${$facadeEvidenceAgg}${$evidenceType}"
      />
    </CONNECT>
    <CONNECT>
      <SOURCE
        NAME="PAGE"
        PROPERTY="caseID"
      />
      <TARGET
        NAME="DISPLAY"
        PROPERTY="{$facadeViewKey}${$facadeCaseIDAgg}$caseID"
      />
    </CONNECT>
    </xsl:otherwise>
  </xsl:choose>

  <!-- BEGIN, 25/02/2008, CD -->
  <xsl:variable name="AllClusters"
    select="UserInterfaceLayer/Cluster[(not(@view) or @view!='No')]"/>

  <!-- BEGIN, CR00101071, POB -->
  <xsl:if test="count($AllClusters/EvidenceHeader)=0">
    <CLUSTER
      LABEL_WIDTH="40"
      NUM_COLS="2"
      BEHAVIOR="NONE"
    >
      <INCLUDE FILE_NAME="{$headerView}"/>
    </CLUSTER>
  </xsl:if>
  <!-- END, CR00101071, POB -->

  <xsl:call-template name="ViewContentUI-WriteClusters">
    <xsl:with-param name="Clusters" select="$AllClusters"/>
    <xsl:with-param name="capName" select="$capName"/>
    <xsl:with-param name="childLevelNo" select="$childLevelNo"/>
    <xsl:with-param name="headerView" select="$headerView"/>
    <xsl:with-param name="viewVersion" select="$viewVersion"/>
  </xsl:call-template>

  </VIEW>

  </redirect:write>

  <!-- BEGIN, PADDY -->
  <xsl:call-template name="write-all-locales-view-content-properties">
    <xsl:with-param name="locales" select="$localeList"/>
    <xsl:with-param name="filepath" select="$filepath"/>
  </xsl:call-template>
  <!-- END, PADDY -->

</xsl:template>

<!-- BEGIN, PADDY -->
<!--iterate through each token, generating each element-->
<xsl:template name="write-all-locales-view-content-properties">

  <xsl:param name="locales"/>
  <xsl:param name="filepath"/>

  <!--tokens still exist-->
  <xsl:if test="$locales">
    <xsl:choose>

    <!--more than one-->
    <xsl:when test="contains($locales,',')">

      <xsl:call-template name="write-view-content-properties">
      <xsl:with-param name="locale"
        select="concat('_', substring-before($locales,','))"/>
      <xsl:with-param name="filepath"
        select="$filepath"/>
      </xsl:call-template>

     <!-- Recursively call self to process all locales -->
     <xsl:call-template name="write-all-locales-view-content-properties">
     <xsl:with-param name="locales"
       select="substring-after($locales,',')"/>
     <xsl:with-param name="filepath"
       select="$filepath"/>
     </xsl:call-template>

   </xsl:when>

    <!--only one token left-->
    <xsl:otherwise>

      <!-- Call for the final locale -->
      <xsl:call-template name="write-view-content-properties">
        <xsl:with-param name="locale" select="concat('_', $locales)"/>
        <xsl:with-param name="filepath" select="$filepath"/>
      </xsl:call-template>

      <!-- Finally call for the default locale -->
      <xsl:call-template name="write-view-content-properties">
        <xsl:with-param name="locale"/>
        <xsl:with-param name="filepath" select="$filepath"/>
      </xsl:call-template>

    </xsl:otherwise>

    </xsl:choose>

  </xsl:if>

</xsl:template>

<xsl:template name="write-view-content-properties">
  <xsl:param name="locale"/>
  <xsl:param name="filepath"/>
<xsl:if test="count(.//*[@locale=$locale])&gt;0">

<redirect:write select="concat($filepath, $locale, '.properties')">

<!-- BEGIN, 25/02/2008, CD -->
<xsl:for-each select="UserInterfaceLayer/Cluster[not(@view) or @view!='No']">
<!-- END, 25/02/2008, CD -->

<xsl:if test="@label!=''">
<xsl:if test="count(./Label[@locale=$locale])&gt;0">
<xsl:value-of select="@label"/>=<xsl:value-of select="./Label[@locale=$locale]/@value"/><xsl:text>&#xa;</xsl:text>
    </xsl:if>
      <!--
        If help is present on this Cluster title it is a comments Cluster.
      -->
      <xsl:if test="@help!=&apos;&apos;">
        <xsl:if test="count(./Help[@locale=$locale])&gt;0">
          <xsl:value-of select="@help"/>=<xsl:value-of select="./Help[@locale=$locale]/@value"/><xsl:text>&#xa;</xsl:text>
        </xsl:if>
      </xsl:if>
    </xsl:if>
    <xsl:if test="@description!=''">
      <xsl:if test="count(./Description[@locale=$locale])&gt;0">
        <xsl:value-of select="@description"/>=<xsl:value-of select="./Description[@locale=$locale]/@value"/>
        <xsl:text>&#xa;</xsl:text>
      </xsl:if>
    </xsl:if>

    <xsl:for-each select="Field">
      <xsl:if test="@label!=''">
        <xsl:if test="count(./Label[@locale=$locale])&gt;0">
          <xsl:value-of select="@label"/>=<xsl:value-of select="./Label[@locale=$locale]/@value"/><xsl:text>&#xa;</xsl:text>
        </xsl:if>
        <xsl:if test="@help!=&apos;&apos;">
          <xsl:if test="count(./Help[@locale=$locale])&gt;0">
            <xsl:value-of select="@help"/>=<xsl:value-of select="./Help[@locale=$locale]/@value"/><xsl:text>&#xa;</xsl:text>
          </xsl:if>
        </xsl:if>
      </xsl:if>
    </xsl:for-each>

    <!-- BEGIN, CR00113054, DG -->
    <xsl:for-each select="LinkField">
      <xsl:variable name="fCN" select="@columnName"/>
      <xsl:if test="@label!=''">
        <xsl:if test="count(./Label[@locale=$locale])&gt;0">
          <xsl:value-of select="@label"/>=<xsl:value-of select="./Label[@locale=$locale]/@value"/><xsl:text>&#xa;</xsl:text>
        </xsl:if>
        <xsl:if test="@help!=&apos;&apos;">
          <xsl:if test="count(./Help[@locale=$locale])&gt;0">
            <xsl:value-of select="@help"/>=<xsl:value-of select="./Help[@locale=$locale]/@value"/><xsl:text>&#xa;</xsl:text>
          </xsl:if>
        </xsl:if>
      </xsl:if>
    </xsl:for-each>
    <!--END, CR00113054 -->

    <!-- Not sure what the below is for -->
    <xsl:for-each select="association">
      <xsl:if test="@label!=''">
        Field.Label.Associated<xsl:value-of select="@from"/>=<xsl:value-of select="@label"/><xsl:text>&#xa;</xsl:text>
      </xsl:if>
      <xsl:if test="@comments!=''">
        Field.Label.Associated<xsl:value-of select="@from"/>.Help=<xsl:value-of select="@comments"/><xsl:text>&#xa;</xsl:text>
      </xsl:if>
    </xsl:for-each>

  </xsl:for-each>

  </redirect:write>

  </xsl:if>
  </xsl:template>

  <!-- BEGIN, CR00100369, POB -->
  <xsl:template name="ViewContentUI-WriteClusters">

    <xsl:param name="Clusters"/>
    <xsl:param name="capName"/>
    <xsl:param name="childLevelNo"/>
    <xsl:param name="headerView"/>
    <xsl:param name="viewVersion" />

    <xsl:for-each select="$Clusters">

      <xsl:sort select="@order" data-type="number"/>

      <xsl:if test="(($viewVersion=$businessObjectViewVersion and count(Field[not(@metatype)
        or @metatype!=$metatypeParentCaseParticipant])>0) or ($viewVersion!=$businessObjectViewVersion and count(Field)>0)
        or count(SkipField)&gt;0 or count(EvidenceHeader)&gt;0 or count(LinkField)&gt;0) or count(association)>0">

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

      <CLUSTER
        LABEL_WIDTH="{$labelWidth}"
        NUM_COLS="{$numCols}"
        SHOW_LABELS="{$showLabelsInd}"
        BEHAVIOR="NONE"
      >

      <xsl:if test="@label!=''">
        <xsl:attribute name="TITLE"><xsl:value-of select="@label"/></xsl:attribute>
      </xsl:if>

      <xsl:variable name="clusterChildNodes" select="child::node()"/>

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
            <INCLUDE FILE_NAME="{$headerView}"/>
          </xsl:when>
          <!-- END, CR00101071, POB -->
          <xsl:when test="local-name()='Field' and ($viewVersion!=$businessObjectViewVersion or (not(@metatype) or @metatype!=$metatypeParentCaseParticipant))">

        <FIELD>

        <xsl:if test="@label!=''">
          <xsl:attribute name="LABEL"><xsl:value-of select="@label"/></xsl:attribute>
        </xsl:if>

        <xsl:choose>

        <xsl:when test="@metatype=$metatypeRelatedEntityAttribute">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$relatedEntityAttributes${@columnName}"
          />
        </CONNECT>
    </xsl:when>

    <xsl:when test="@metatype=$metatypeCaseParticipantSearch or @metatype=$metatypeEmployerCaseParticipant
      or @metatype=$metatypeDisplayCaseParticipant or (@createCaseParticipant='Yes' and @name!='')">

    <xsl:variable name="readParticipantNameDetails">
      <xsl:choose>
        <xsl:when test="@name!=''"><xsl:value-of select="@name"/>CaseParticipantDetails</xsl:when>
        <xsl:otherwise>caseParticipantDetails</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <xsl:variable name="fieldColumnName" select="@columnName"/>

    <xsl:variable name="attributeName">
      <xsl:choose>
        <xsl:when test="../../../ServiceLayer/RelatedParticipantDetails[@columnName=$fieldColumnName and @createParticipantType=$ParticipantTypeEmployer]">employerName</xsl:when>
        <xsl:otherwise>caseParticipantName</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="{$readParticipantNameDetails}${$attributeName}"
    />
    </CONNECT>
    <xsl:if test="$viewVersion=$businessObjectViewVersion">
      <LINK
         OPEN_NEW="true"
         PAGE_ID="{$resolveParticipantHome}"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="{@columnName}"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseParticipantRoleID"
          />
        </CONNECT>
      </LINK>
    </xsl:if>
  </xsl:when>
  <xsl:when test="@metatype=$metatypeParentCaseParticipant">
    <CONNECT>
      <SOURCE
        NAME="{@columnName}"
        PROPERTY="nameAndAgeOpt"
      />
    </CONNECT>
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
               <!-- <xsl:if test="$linkAttribute!=''">
                  <LINK PAGE_ID="Representative_home" OPEN_NEW="true">
                    <CONNECT>
                      <SOURCE PROPERTY="{$linkAttribute}" NAME="DISPLAY"/>
                      <TARGET PROPERTY="concernRoleID" NAME="PAGE"/>
                    </CONNECT>
                  </LINK>
                </xsl:if> -->
      </xsl:if>
    </FIELD>
  </xsl:when>

  <xsl:when test="@metatype=$metatypeAssociationID">

    <xsl:if test="$viewVersion!=$businessObjectViewVersion">
      <xsl:variable name="toEntity"><xsl:for-each select="../../../Relationships/Association[@to!='']"><xsl:value-of select="@to"/></xsl:for-each></xsl:variable>

        <xsl:if test="@label!=''">
          <xsl:attribute name="LABEL"><xsl:value-of select="@label"/></xsl:attribute>
        </xsl:if>

        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="{$associatedLinkText}"
          />
        </CONNECT>
            <!-- <LINK PAGE_ID="{$prefix}_resolve{$toEntity}{$caseType}Association">
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="caseID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="caseID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="contextDescription"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="contextDescription"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="evidenceID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="linkedEvID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="evidenceType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="linkedEvType"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="associatedEvidenceDetails$evidenceKey$evidenceID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="evidenceID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="associatedEvidenceDetails$evidenceKey$evType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="evidenceType"
              />
            </CONNECT>
          </LINK> -->
        </xsl:if> <!-- test="$viewVersion!=$businessObjectViewVersion"-->
      </xsl:when>

      <xsl:otherwise>

        <xsl:variable name="columnName" select="@columnName"/>

        <xsl:variable name="fieldHeight">
          <xsl:choose>
            <xsl:when test="@metatype=$metatypeComments">5</xsl:when>
            <xsl:otherwise>1</xsl:otherwise>
          </xsl:choose>
        </xsl:variable>

        <xsl:attribute name="HEIGHT"><xsl:value-of select="$fieldHeight"/></xsl:attribute>
        <!--
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="{@columnName}"
          />
        </CONNECT>

        -->
        <xsl:choose>
          <xsl:when test="@metatype=$metatypeRelatedEntityAttribute">
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="result$relatedEntityAttributes${@columnName}"
              />
            </CONNECT>
          </xsl:when>
          <xsl:when test="not(@metatype) or (@metatype!=$metatypeRelatedEntityAttribute and @metatype!=$metatypeParentCaseParticipant)">
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
            </CONNECT>
          </xsl:when>
        </xsl:choose>
      </xsl:otherwise>
    </xsl:choose>

    </FIELD>
          </xsl:when>
          </xsl:choose>
    </xsl:for-each>

  </CLUSTER>
  </xsl:if>
    </xsl:for-each>

</xsl:template>
</xsl:stylesheet>
