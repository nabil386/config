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

<!-- Global Variables -->
<xsl:import href="UICommon.xslt"/>

<xsl:output method="xml" indent="yes"/>

<xsl:template name="ListRelatedEvidenceUI">

  <xsl:param name="prefix"/>
  <xsl:param name="path"/>
  <xsl:param name="UIName"/>
  <xsl:param name="relatedEvidenceName"/>    
  <xsl:param name="UISelectRelatedEvidence"/>
  <xsl:param name="cancelLink"/>

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


  <SERVER_INTERFACE>
    <xsl:choose >
      <xsl:when test="@concernRoleRelationship='Yes'">
          <xsl:attribute name="CLASS">{$prefix}EvidenceMaintenance</xsl:attribute>
          <xsl:attribute name="NAME">DISPLAY</xsl:attribute>
        <xsl:attribute name="OPERATION">listAll<xsl:value-of select="$relatedEvidenceName"/>AndDependentEvidenceByCaseID</xsl:attribute>
      </xsl:when> 
      <xsl:otherwise>
          <xsl:attribute name="CLASS">Evidence</xsl:attribute>
          <xsl:attribute name="NAME">DISPLAY</xsl:attribute>
          <xsl:attribute name="OPERATION">listEvidence</xsl:attribute>
      </xsl:otherwise>
    </xsl:choose>
  </SERVER_INTERFACE>

  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="contextDescription"/>
  <PAGE_PARAMETER NAME="evidenceType"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$caseID"
    />
  </CONNECT>
  
  <CONNECT>
    <SOURCE PROPERTY="{$relatedEvidenceName}.EvidenceType" NAME="CONSTANT"/>
    <TARGET PROPERTY="key$evidenceType" NAME="DISPLAY"/>
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

        <ACTION_CONTROL LABEL="ActionControl.Label.Select.For.RelatedEntity.Association">
          <LINK PAGE_ID="{$UISelectRelatedEvidence}">
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
                NAME="DISPLAY"
                PROPERTY="activeList$dtls$evidenceID"
              />
              <TARGET
                NAME="PAGE">
                <xsl:choose >
                  <xsl:when test="@concernRoleRelationship='Yes'">
                    <xsl:attribute name="PROPERTY">parEvID</xsl:attribute>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:attribute name="PROPERTY">relEvidenceID</xsl:attribute>
                  </xsl:otherwise>
                </xsl:choose>
              </TARGET>
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="activeList$dtls$evidenceType"
              />
              <TARGET
                NAME="PAGE">
                <xsl:choose >
                  <xsl:when test="@concernRoleRelationship='Yes'">
                    <xsl:attribute name="PROPERTY">parEvType</xsl:attribute>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:attribute name="PROPERTY">relEvidenceType</xsl:attribute>
                  </xsl:otherwise>
                </xsl:choose>
              </TARGET>
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="evidenceType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="evidenceType"
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

        <ACTION_CONTROL LABEL="ActionControl.Label.Select.For.RelatedEntity.Association">
          <LINK PAGE_ID="{$UISelectRelatedEvidence}">
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
                NAME="DISPLAY"
                PROPERTY="newAndUpdateList$dtls$evidenceID"
              />
              <TARGET
                NAME="PAGE">
                <xsl:choose >
                  <xsl:when test="@concernRoleRelationship='Yes'">
                    <xsl:attribute name="PROPERTY">parEvID</xsl:attribute>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:attribute name="PROPERTY">relEvidenceID</xsl:attribute>
                  </xsl:otherwise>
                </xsl:choose>
              </TARGET>
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="newAndUpdateList$dtls$evidenceType"
              />
              <TARGET
                NAME="PAGE">
                <xsl:choose >
                  <xsl:when test="@concernRoleRelationship='Yes'">
                    <xsl:attribute name="PROPERTY">parEvType</xsl:attribute>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:attribute name="PROPERTY">relEvidenceType</xsl:attribute>
                  </xsl:otherwise>
                </xsl:choose>
              </TARGET>
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="evidenceType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="evidenceType"
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
        LABEL="ActionControl.Label.Cancel"
      >
      <xsl:if test="$cancelLink='Evidence_resolveWorkspace'">
        <xsl:attribute name="IMAGE">CancelButton</xsl:attribute>
      </xsl:if>
        <LINK
          PAGE_ID="{$cancelLink}"
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
          <xsl:if test="$cancelLink='Evidence_resolveWorkspace'">
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="evidenceType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceType"
            />
          </CONNECT>
          </xsl:if>
        </LINK>
      </ACTION_CONTROL>

    </ACTION_SET>
  </CLUSTER>

</PAGE>
  
  </redirect:write>


<!-- BEGIN, PADDY -->
  <!-- View Content Vim Properties -->
  <xsl:call-template name="write-all-locales-list-related-properties">   
    <xsl:with-param name="locales" select="$localeList"/>
    <xsl:with-param name="filepath" select="$filepath"/>
  </xsl:call-template>
  <!-- END, PADDY -->

</xsl:template>

  
  <xsl:template name="ListRelatedEvidenceConstants">
  
    <xsl:param name="path"/>
    <xsl:param name="relatedEvidenceName"/>
    
    <redirect:open select="concat($path, $constantFileName)" method="text" append="true"/>
      <redirect:write select="concat($path, $constantFileName)">
      
      <!-- BEGIN, CR00100405, CD -->
      <xsl:variable name="evidenceType"> 
        <xsl:call-template name="JavaID2CodeValue">
          <xsl:with-param name="java_identifier" select="$relatedEvidenceName"/>  
          <xsl:with-param name="codetableName">EvidenceType</xsl:with-param>
          <xsl:with-param name="serverBuildDir" select="$serverBuildDir" />
        </xsl:call-template>  
      </xsl:variable>    
      <!-- END, CR00100405 -->
      <xsl:value-of select="$relatedEvidenceName"/>.EvidenceType=<xsl:value-of select="$evidenceType"/><xsl:text>&#xa;</xsl:text>
        
    
      </redirect:write>
    <redirect:close select="concat($path, $constantFileName)"/>

  </xsl:template>

<!-- BEGIN, PADDY -->
  
  <!--iterate through each token, generating each element-->
    <xsl:template name="write-all-locales-list-related-properties">
       
       <xsl:param name="locales"/>
       <xsl:param name="filepath"/>
       
       <!--tokens still exist-->
       <xsl:if test="$locales">               
         
         <xsl:choose>
         
           <!--more than one-->
           <xsl:when test="contains($locales,',')">
             
             <xsl:call-template name="write-list-related-properties">
               <xsl:with-param name="locale"
                              select="concat('_', substring-before($locales,','))"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
             </xsl:call-template>
             
             <!-- Recursively call self to process all locales -->
             <xsl:call-template name="write-all-locales-list-related-properties">   
               <xsl:with-param name="locales"
                               select="substring-after($locales,',')"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
             </xsl:call-template>
             
           </xsl:when>
           
           <!--only one token left-->
           <xsl:otherwise>
           
             <!-- Call for the final locale -->
             <xsl:call-template name="write-list-related-properties">
               <xsl:with-param name="locale" select="concat('_', $locales)"/>
               <xsl:with-param name="filepath"
                              select="$filepath"/>
             </xsl:call-template>
           
             <!-- Finally call for the default locale -->
             <xsl:call-template name="write-list-related-properties">
         <xsl:with-param name="locale"/>
         <xsl:with-param name="filepath" select="$filepath"/>
             </xsl:call-template>
           
           </xsl:otherwise>
         
         </xsl:choose>
       
       </xsl:if>
    
    </xsl:template>
    
    <xsl:template name="write-list-related-properties">
      
        <xsl:param name="locale"/>
        <xsl:param name="filepath"/>
        
    <xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/General)&gt;0">
<redirect:write select="concat($filepath, $locale, '.properties')">    
    <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/> 
    
    <xsl:if test="count($generalProperties/Help.List.RelatedEvidence.PageDescription)&gt;0">
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/Help.List.RelatedEvidence.PageDescription"/>
  <xsl:with-param name="evidenceNode" select="."/>
  <xsl:with-param name="altPropertyName">Help.PageDescription</xsl:with-param>
</xsl:call-template>  
</xsl:if>

<xsl:if test="count($generalProperties/Page.Title.ListRelatedEntity)&gt;0">
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/Page.Title.ListRelatedEntity"/>
  <xsl:with-param name="evidenceNode" select="."/>
  <xsl:with-param name="altPropertyName">Page.Title</xsl:with-param>
</xsl:call-template> 
<xsl:text>&#xa;</xsl:text> 
</xsl:if>

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/Cluster.Active.Title"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template> 

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ Cluster.Active.Description"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template>

Container.Separator=|

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ List.Title.Action"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template>   

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ ActionControl.Label.Cancel"/>
  <xsl:with-param name="evidenceNode" select="."/>
</xsl:call-template>  

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.Select.For.RelatedEntity.Association"/>
  <xsl:with-param name="evidenceNode" select="."/>
</xsl:call-template> 


<!-- DKenny - Why are these properties here with no values?? -->
<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.PendingIndication"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template>

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/List.Title.InEditIndication"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template>  
<!-- END DKenny -->

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ List.Title.ID"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template>  

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ List.Title.Name"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template>  

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ List.Title.EffectiveDate"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template>  

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ List.Title.Details"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template>  

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ Cluster.WIP.Title"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template>

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ Cluster.WIP.Description"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template>  

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ Cluster.WIP.Update.Description"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template>  

<xsl:call-template name="callGenerateProperties">
  <xsl:with-param name="propertyNode" select="$generalProperties/ List.Title.UpdatedBy"/>
  <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
</xsl:call-template>  
    
  </redirect:write>
    </xsl:if>
    </xsl:template>
    
</xsl:stylesheet>