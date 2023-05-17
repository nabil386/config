<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2006,2014. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
Copyright (c) 2006-2008 Curam Software Ltd.  All rights reserved.

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

<xsl:output method="xml" indent="yes"/>

<xsl:template name="ListEvidenceForAssociation2UI">

  <xsl:param name="prefix"/>
  <xsl:param name="path"/>
  <xsl:param name="UIName"/>
  <xsl:param name="capName"/>    
  <xsl:param name="childEvidenceName"/>    
  <xsl:param name="fromCreateParentInd"/>

  <xsl:param name="evidenceList"/>
  <xsl:param name="firstEvidenceName" />

  <xsl:variable name="childLevelNo">
    <xsl:call-template name="GetChildLevel">
      <xsl:with-param name="capName" select="$childEvidenceName"/>
    </xsl:call-template>
  </xsl:variable>   

  <xsl:variable name="EvidenceEntity" select="//EvidenceEntities/EvidenceEntity[@name=$capName]"/>  

  <xsl:variable name="listOrViewUIName">
    <xsl:choose>
      <xsl:when test="$fromCreateParentInd='Yes'"><xsl:value-of select="$prefix"/>_list<xsl:value-of select="$firstEvidenceName"/><xsl:value-of select="$caseType"/></xsl:when>
      <xsl:otherwise><xsl:value-of select="$prefix"/>_view<xsl:value-of select="$firstEvidenceName"/><xsl:value-of select="$caseType"/></xsl:otherwise>
    </xsl:choose>    
  </xsl:variable>

  <xsl:variable name="viewAssociationEvidenceUIName">
      <xsl:choose>
      <xsl:when test="$fromCreateParentInd='Yes'"><xsl:value-of select="$prefix"/>_view<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>FromAssociationList_fromCreate</xsl:when>  
      <xsl:otherwise><xsl:value-of select="$prefix"/>_view<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>FromAssociationList</xsl:otherwise>
    </xsl:choose>
  </xsl:variable>
  
  <xsl:variable name="createAssociationEvidenceUIName">
    <xsl:choose>
      <xsl:when test="$fromCreateParentInd='Yes'"><xsl:value-of select="$prefix"/>_create<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>FromAssociationList_fromCreate</xsl:when>  
      <xsl:otherwise><xsl:value-of select="$prefix"/>_create<xsl:value-of select="$capName"/><xsl:value-of select="$caseType"/>FromAssociationList</xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <xsl:variable name="confirmCreateAssociationUIName">
    <xsl:choose>
      <xsl:when test="$fromCreateParentInd='Yes'"><xsl:value-of select="$prefix"/>_confirmCreate<xsl:value-of select="$childEvidenceName"/><xsl:value-of select="$caseType"/>Association_fromCreate</xsl:when>  
      <xsl:otherwise><xsl:value-of select="$prefix"/>_confirmCreate<xsl:value-of select="$childEvidenceName"/><xsl:value-of select="$caseType"/>Association</xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <xsl:variable name="filepath"><xsl:value-of select="$path"/><xsl:value-of select="$UIName"/></xsl:variable>

  <redirect:write select="concat($filepath, '.uim')">
  
<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>

<PAGE
  PAGE_ID="{$UIName}"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="Page.Title"
      />
    </CONNECT>
    <CONNECT>
      <SOURCE
        NAME="PAGE"
        PROPERTY="contextDescription"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="Evidence"
    NAME="DISPLAY"
    OPERATION="listEvidence"
  />

  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="contextDescription"/>
  <PAGE_PARAMETER NAME="linkedEvID"/>
  <PAGE_PARAMETER NAME="linkedEvType"/>
  <xsl:if test="$childLevelNo>=1">
  <PAGE_PARAMETER NAME="parEvID"/>
  <PAGE_PARAMETER NAME="parEvType"/>
  </xsl:if>  
  <xsl:if test="$childLevelNo>=2">
  <PAGE_PARAMETER NAME="grandParEvID"/>
  <PAGE_PARAMETER NAME="grandParEvType"/>
  </xsl:if> 
  <xsl:if test="$childLevelNo=3">
  <PAGE_PARAMETER NAME="greatGrandParEvID"/>
  <PAGE_PARAMETER NAME="greatGrandParEvType"/>
  </xsl:if>  


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
  </CONNECT>

  <CLUSTER
    DESCRIPTION="Cluster.Active.Description"
    SHOW_LABELS="false"
    STYLE="evidencecluster"
    TITLE="Cluster.Active.Title"
  >


    <LIST>

      <CONTAINER
        LABEL="List.Title.Action"
        SEPARATOR="Container.Separator"
        WIDTH="10"
      >

        <ACTION_CONTROL LABEL="ActionControl.Label.Select.For.Association">
          <LINK PAGE_ID="{$confirmCreateAssociationUIName}">
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
                NAME="DISPLAY"
                PROPERTY="activeList$dtls$evidenceID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="linkedEvID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="activeList$dtls$evidenceType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="linkedEvType"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="linkedEvID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="evidenceID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="linkedEvType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="evidenceType"
              />
            </CONNECT>
  <xsl:if test="$childLevelNo>=1">
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="parEvID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="parEvID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="parEvType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="parEvType"
              />
            </CONNECT>
  </xsl:if>  
  <xsl:if test="$childLevelNo>=2">
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="grandParEvID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="grandParEvID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="grandParEvType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="grandParEvType"
              />
            </CONNECT>
  </xsl:if> 
  <xsl:if test="$childLevelNo=3">
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="greatGrandParEvID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="greatGrandParEvID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="greatGrandParEvType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="greatGrandParEvType"
              />
            </CONNECT>
  </xsl:if>  
          </LINK>
        </ACTION_CONTROL>

        <ACTION_CONTROL LABEL="ActionControl.Label.View">
          <LINK PAGE_ID="{$viewAssociationEvidenceUIName}">
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
                NAME="DISPLAY"
                PROPERTY="activeList$dtls$evidenceID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="evidenceID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="activeList$dtls$evidenceType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="evidenceType"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="linkedEvID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="linkedEvID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="linkedEvType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="linkedEvType"
              />
            </CONNECT>
  <xsl:if test="$childLevelNo>=1">
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="parEvID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="parEvID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="parEvType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="parEvType"
              />
            </CONNECT>
  </xsl:if>  
  <xsl:if test="$childLevelNo>=2">
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="grandParEvID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="grandParEvID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="grandParEvType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="grandParEvType"
              />
            </CONNECT>
  </xsl:if> 
  <xsl:if test="$childLevelNo=3">
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="greatGrandParEvID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="greatGrandParEvID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="greatGrandParEvType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="greatGrandParEvType"
              />
            </CONNECT>
  </xsl:if>  
          </LINK>
        </ACTION_CONTROL>

      </CONTAINER>


      <FIELD
        LABEL="List.Title.PendingIndication"
        WIDTH="3"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="activeList$dtls$wipPendingCode"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="List.Title.ID"
        WIDTH="10"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="activeList$dtls$correctionSetID"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="List.Title.Name"
        WIDTH="15"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="activeList$dtls$concernRoleName"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="List.Title.EffectiveDate"
        WIDTH="11"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="activeList$dtls$effectiveFrom"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="List.Title.Details"
        WIDTH="18"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="activeList$dtls$summary"
          />
        </CONNECT>
      </FIELD>
    </LIST>


  </CLUSTER>


  <CLUSTER
    DESCRIPTION="Cluster.WIP.Description"
    SHOW_LABELS="false"
    STYLE="evidencecluster"
    TITLE="Cluster.WIP.Title"
  >
    <LIST>

      <CONTAINER
        LABEL="List.Title.Action"
        SEPARATOR="Container.Separator"
        WIDTH="10"
      >

        <ACTION_CONTROL LABEL="ActionControl.Label.Select.For.Association">
          <LINK PAGE_ID="{$confirmCreateAssociationUIName}">
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
                NAME="DISPLAY"
                PROPERTY="newAndUpdateList$dtls$evidenceID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="linkedEvID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="newAndUpdateList$dtls$evidenceType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="linkedEvType"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="linkedEvID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="evidenceID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="linkedEvType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="evidenceType"
              />
            </CONNECT>
  <xsl:if test="$childLevelNo>=1">
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="parEvID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="parEvID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="parEvType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="parEvType"
              />
            </CONNECT>
  </xsl:if>  
  <xsl:if test="$childLevelNo>=2">
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="grandParEvID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="grandParEvID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="grandParEvType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="grandParEvType"
              />
            </CONNECT>
  </xsl:if> 
  <xsl:if test="$childLevelNo=3">
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="greatGrandParEvID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="greatGrandParEvID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="greatGrandParEvType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="greatGrandParEvType"
              />
            </CONNECT>
  </xsl:if>  
          </LINK>
        </ACTION_CONTROL>

        <ACTION_CONTROL LABEL="ActionControl.Label.View">
          <LINK PAGE_ID="{$viewAssociationEvidenceUIName}">
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
                NAME="DISPLAY"
                PROPERTY="newAndUpdateList$dtls$evidenceID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="evidenceID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="newAndUpdateList$dtls$evidenceType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="evidenceType"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="linkedEvID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="linkedEvID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="linkedEvType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="linkedEvType"
              />
            </CONNECT>
  <xsl:if test="$childLevelNo>=1">
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="parEvID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="parEvID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="parEvType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="parEvType"
              />
            </CONNECT>
  </xsl:if>  
  <xsl:if test="$childLevelNo>=2">
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="grandParEvID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="grandParEvID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="grandParEvType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="grandParEvType"
              />
            </CONNECT>
  </xsl:if> 
  <xsl:if test="$childLevelNo=3">
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="greatGrandParEvID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="greatGrandParEvID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="greatGrandParEvType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="greatGrandParEvType"
              />
            </CONNECT>
  </xsl:if>  
          </LINK>
        </ACTION_CONTROL>

      </CONTAINER>


        <FIELD
          LABEL="List.Title.InEditIndication"
          WIDTH="3"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="newAndUpdateList$dtls$wipPendingCode"
            />
          </CONNECT>
        </FIELD>
        <FIELD
          LABEL="List.Title.ID"
          WIDTH="10"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="newAndUpdateList$dtls$correctionSetID"
            />
          </CONNECT>
        </FIELD>
        <FIELD
          LABEL="List.Title.Name"
          WIDTH="15"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="newAndUpdateList$dtls$concernRoleName"
            />
          </CONNECT>
        </FIELD>
        <FIELD
          LABEL="List.Title.EffectiveDate"
          WIDTH="11"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="newAndUpdateList$dtls$effectiveFrom"
            />
          </CONNECT>
        </FIELD>


        <FIELD
          LABEL="List.Title.Details"
          WIDTH="22"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="newAndUpdateList$dtls$summary"
            />
          </CONNECT>
        </FIELD>
        <FIELD
          LABEL="List.Title.UpdatedBy"
          WIDTH="10"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="newAndUpdateList$dtls$updatedBy"
            />
          </CONNECT>
        </FIELD>
      </LIST>


  </CLUSTER>


  <CLUSTER>
    <ACTION_SET
      ALIGNMENT="CENTER"
      TOP="false"
    >
      <ACTION_CONTROL
        IMAGE="NewButton"
        LABEL="ActionControl.Label.New"
      >
        <LINK 
          PAGE_ID="{$createAssociationEvidenceUIName}"
          SAVE_LINK="false"
        >
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
              PROPERTY="linkedEvID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="linkedEvID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="linkedEvType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="linkedEvType"
            />
          </CONNECT>
  <xsl:if test="$childLevelNo>=1">
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="parEvID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="parEvID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="parEvType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="parEvType"
            />
          </CONNECT>
  </xsl:if>  
  <xsl:if test="$childLevelNo>=2">
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="grandParEvID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="grandParEvID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="grandParEvType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="grandParEvType"
            />
          </CONNECT>
  </xsl:if> 
  <xsl:if test="$childLevelNo=3">
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="greatGrandParEvID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="greatGrandParEvID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="greatGrandParEvType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="greatGrandParEvType"
            />
          </CONNECT>
  </xsl:if>  
        </LINK>
      </ACTION_CONTROL>

      <ACTION_CONTROL
        IMAGE="CancelButton"
        LABEL="ActionControl.Label.Cancel"
      >
        <LINK
          PAGE_ID="{$listOrViewUIName}"
          SAVE_LINK="false"
        >
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
        <xsl:choose>
          <xsl:when test="$fromCreateParentInd='Yes'">
  <xsl:if test="$childLevelNo>=1">
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="parEvID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="parEvType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceType"
            />
          </CONNECT>
  </xsl:if>  
  <xsl:if test="$childLevelNo>=2">
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="grandParEvID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="parEvID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="grandParEvType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="parEvType"
            />
          </CONNECT>
  </xsl:if> 
  <xsl:if test="$childLevelNo=3">
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="greatGrandParEvID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="grandParEvID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="greatGrandParEvType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="grandParEvType"
            />
          </CONNECT>
  </xsl:if>  
          </xsl:when>  
          <xsl:otherwise>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="linkedEvID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="linkedEvType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceType"
            />
          </CONNECT>
  <xsl:if test="$childLevelNo>=1">
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="parEvID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="parEvID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="parEvType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="parEvType"
            />
          </CONNECT>
  </xsl:if>  
  <xsl:if test="$childLevelNo>=2">
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="grandParEvID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="grandParEvID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="grandParEvType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="grandParEvType"
            />
          </CONNECT>
  </xsl:if> 
  <xsl:if test="$childLevelNo=3">
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="greatGrandParEvID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="greatGrandParEvID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="greatGrandParEvType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="greatGrandParEvType"
            />
          </CONNECT>
  </xsl:if>  
          </xsl:otherwise>
        </xsl:choose>
        </LINK>
      </ACTION_CONTROL>

    </ACTION_SET>
  </CLUSTER>

</PAGE>
  
  </redirect:write>

  <!-- BEGIN, PADDY -->
    <xsl:call-template name="write-all-locales-list-association-to-properties">   
      <xsl:with-param name="locales" select="$localeList"/>
      <xsl:with-param name="filepath" select="$filepath"/>
      <xsl:with-param name="EvidenceEntity" select="$EvidenceEntity"/>
    </xsl:call-template>
  <!-- END, PADDY -->
  
</xsl:template>



<!-- BEGIN, PADDY -->
  
  <!--iterate through each token, generating each element-->
    <xsl:template name="write-all-locales-list-association-to-properties">
       
       <xsl:param name="locales"/>
       <xsl:param name="filepath"/>
       <xsl:param name="EvidenceEntity"/>
       
       <!--tokens still exist-->
       <xsl:if test="$locales">               
         
         <xsl:choose>
         
           <!--more than one-->
           <xsl:when test="contains($locales,',')">
             
             <xsl:call-template name="write-list-association-to-properties">
               <xsl:with-param name="locale"
                              select="concat('_', substring-before($locales,','))"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
               <xsl:with-param name="EvidenceEntity" select="$EvidenceEntity"/>
             </xsl:call-template>
             
             <!-- Recursively call self to process all locales -->
             <xsl:call-template name="write-all-locales-list-association-to-properties">   
               <xsl:with-param name="locales"
                               select="substring-after($locales,',')"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
               <xsl:with-param name="EvidenceEntity" select="$EvidenceEntity"/>
             </xsl:call-template>
             
           </xsl:when>
           
           <!--only one token left-->
           <xsl:otherwise>
           
             <!-- Call for the final locale -->
             <xsl:call-template name="write-list-association-to-properties">
               <xsl:with-param name="locale" select="concat('_', $locales)"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
               <xsl:with-param name="EvidenceEntity" select="$EvidenceEntity"/>
             </xsl:call-template>
           
             <!-- Finally call for the default locale -->
             <xsl:call-template name="write-list-association-to-properties">
         <xsl:with-param name="locale"/>
         <xsl:with-param name="filepath" select="$filepath"/>
         <xsl:with-param name="EvidenceEntity" select="$EvidenceEntity"/>
             </xsl:call-template>
           
           </xsl:otherwise>
         
         </xsl:choose>
       
       </xsl:if>
    
    </xsl:template>
    
    <xsl:template name="write-list-association-to-properties">
      
        <xsl:param name="locale"/>
        <xsl:param name="filepath"/>
        <xsl:param name="EvidenceEntity"/>
        
    <xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/General)&gt;0">
    
    <redirect:write select="concat($filepath, $locale, '.properties')">
       <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>
       <xsl:if test="count($generalProperties/Help.PageDescription.List.AssociatedEvidence)&gt;0">
<xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Help.PageDescription.List.AssociatedEvidence"/>
      <xsl:with-param name="evidenceNode" select="$EvidenceEntity"/>
	  <xsl:with-param name="altPropertyName">Help.PageDescription</xsl:with-param>
    </xsl:call-template>     
    </xsl:if>
    
    <xsl:if test="count($generalProperties/Page.Title.ListEntity)&gt;0">
<xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Page.Title.ListEntity"/>
      <xsl:with-param name="evidenceNode" select="$EvidenceEntity"/>
	  <xsl:with-param name="altPropertyName">Page.Title</xsl:with-param>
    </xsl:call-template>   
    <xsl:text>&#xa;</xsl:text>                   
    </xsl:if>
    
<xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Cluster.Active.Title"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template> 
    
<xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Cluster.Active.Description"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template>
        
Container.Separator=\ |\ 
Container.Separator=|
    
<xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.Action"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template>   
    
<xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Select.For.Association"/>
      <xsl:with-param name="evidenceNode" select="$EvidenceEntity"/>
    </xsl:call-template>   
        
<xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.View"/>
      <xsl:with-param name="evidenceNode" select="$EvidenceEntity"/>
    </xsl:call-template>     
    
<xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.New"/>
      <xsl:with-param name="evidenceNode" select="$EvidenceEntity"/>
    </xsl:call-template>  
    
<xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Cancel"/>
      <xsl:with-param name="evidenceNode" select="$EvidenceEntity"/>
    </xsl:call-template>  
    
    <!-- Why are these properties here with no values??
    List.Title.PendingIndication=
    List.Title.InEditIndication= -->
    
<xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.ID"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template>  
    
<xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.Name"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template>  
    
<xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.EffectiveDate"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template>  
    
<xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.Details"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template>  
    
<xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Cluster.WIP.Title"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template>  
    
<xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Cluster.WIP.Description"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template>  
    
<xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Cluster.WIP.Update.Description"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template>  
    
<xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.UpdatedBy"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template>  

<xsl:call-template name="callGenerateProperties">    
      <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.PendingIndication"/>    
      <xsl:with-param name="evidenceNode" select="$EvidenceEntity"/>    
    </xsl:call-template> 
        
<xsl:call-template name="callGenerateProperties">    
      <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.InEditIndication"/>    
      <xsl:with-param name="evidenceNode" select="$EvidenceEntity"/>    
    </xsl:call-template> 


    </redirect:write>
    </xsl:if>
    </xsl:template>
    
</xsl:stylesheet>