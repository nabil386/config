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
    
    <xsl:template name="ListCoreEmployments">
        
    <xsl:param name="prefix"/>
    <xsl:param name="path"/>
    <xsl:param name="UIName"/>
    <xsl:param name="relatedEvidenceName"/>
    <xsl:param name="employmentResolveScript" />    
    <xsl:param name="cancelLink" />    
    

    <xsl:variable name="filepath"><xsl:value-of select="$path"/><xsl:value-of select="$UIName"/></xsl:variable>
        
    <redirect:write select="concat($filepath, '.uim')">
            
<xsl:call-template name="printXMLCopyright">
  <xsl:with-param name="date" select="$date"/>
</xsl:call-template>
<xsl:comment> Reads a list of existing employments for a claim participant.          </xsl:comment>
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
      CLASS="{$prefix}{$facadeClass}"
    NAME="DISPLAY"
      OPERATION="{$employmentListByCPRMethod}"
  />


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="relEvidenceID"/>
  <PAGE_PARAMETER NAME="relEvidenceType"/>
  <PAGE_PARAMETER NAME="evidenceType"/>
  <PAGE_PARAMETER NAME="contextDescription"/>
  <!-- VM -->
  <PAGE_PARAMETER NAME="caseParticipantRoleID"/>
  <!-- END, VM -->  


  <!--<CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
  </CONNECT>-->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseParticipantRoleID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$caseParticipantRoleID"
    />
  </CONNECT>
  

  <LIST>


    <CONTAINER
      LABEL="Container.Label.Action"
      SEPARATOR="Container.Separator"
      WIDTH="15"
    >

      <ACTION_CONTROL LABEL="ActionControl.Label.View">
        <LINK PAGE_ID="{$prefix}Evidence_viewEmployment" DISMISS_MODAL="false">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="employmentID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="employmentID"
            />
          </CONNECT>
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
              PROPERTY="relEvidenceID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="relEvidenceID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="relEvidenceType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="relEvidenceType"
            />
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
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="caseParticipantRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseParticipantRoleID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
     

      <ACTION_CONTROL LABEL="ActionControl.Label.Employment">
        <LINK PAGE_ID="{$employmentResolveScript}" DISMISS_MODAL="false">
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="caseParticipantRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseParticipantRoleID"
            />
          </CONNECT>        
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
              PROPERTY="employmentID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="employmentID"
            />
          </CONNECT>
          
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="employerConcernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="employerConcernRoleID"
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
              PROPERTY="relEvidenceID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="relEvidenceID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="relEvidenceType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="relEvidenceType"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


    </CONTAINER>


    <FIELD
      LABEL="Field.Label.Primary"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="primaryCurrentInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Employer"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="employerName"
        />
      </CONNECT>
     
      <LINK
        OPEN_NEW="true"
        PAGE_ID="{$resolveEmployerHome}"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="employerConcernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="employerType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="employerType"
          />
        </CONNECT>
      </LINK>
     
    </FIELD>


    <FIELD
      LABEL="Field.Label.Occupation"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="occupationType"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.From"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fromDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.To"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="toDate"
        />
      </CONNECT>
    </FIELD>


  </LIST>


  <CLUSTER>


    <ACTION_SET
      ALIGNMENT="CENTER"
      BOTTOM="true"
      TOP="false"
    >

     <xsl:if test="$createEmploymentLink='true'">
      
      <ACTION_CONTROL
        IMAGE="NewButton"
        LABEL="ActionControl.Label.New"
      >
        <LINK PAGE_ID="{$prefix}_createEmployment" OPEN_MODAL="true">
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
              PROPERTY="caseParticipantRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseParticipantRoleID"
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
     </xsl:if>

     
      <ACTION_CONTROL
        LABEL="ActionControl.Label.Cancel"
      >
      <xsl:if test="$cancelLink='Evidence_resolveWorkspace'">
        <xsl:attribute name="IMAGE">CancelButton</xsl:attribute>
      </xsl:if>
      <!--
        <LINK
          PAGE_ID="{$prefix}_resolveClaimParticipantRelated{$caseType}"
          SAVE_LINK="false"
        >
        -->
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
        </LINK>
      </ACTION_CONTROL>


    </ACTION_SET>


  </CLUSTER>


</PAGE>

    </redirect:write>
        
    
        
        <!-- BEGIN, PADDY -->
        <xsl:call-template name="write-all-locales-list-core-employments-properties">
          <xsl:with-param name="locales" select="$localeList"/>
          <xsl:with-param name="filepath" select="$filepath"/>
        </xsl:call-template>
        <!-- END, PADDY -->
      
    </xsl:template>
    
    <!-- BEGIN, PADDY -->
      
      <!--iterate through each token, generating each element-->
        <xsl:template name="write-all-locales-list-core-employments-properties">
           
           <xsl:param name="locales"/>
           <xsl:param name="filepath"/>
           
           <!--tokens still exist-->
           <xsl:if test="$locales">               
             
             <xsl:choose>
             
               <!--more than one-->
               <xsl:when test="contains($locales,',')">
                 
                 <xsl:call-template name="write-list-core-employments-properties">
                   <xsl:with-param name="locale"
                                  select="concat('_', substring-before($locales,','))"/>
                   <xsl:with-param name="filepath"
                                  select="$filepath"/>
                 </xsl:call-template>
                 
                 <!-- Recursively call self to process all locales -->
                 <xsl:call-template name="write-all-locales-list-core-employments-properties">   
                   <xsl:with-param name="locales"
                                   select="substring-after($locales,',')"/>
                   <xsl:with-param name="filepath"
                                  select="$filepath"/>
                 </xsl:call-template>
                 
               </xsl:when>
               
               <!--only one token left-->
               <xsl:otherwise>
               
                 <!-- Call for the final locale -->
                 <xsl:call-template name="write-list-core-employments-properties">
                   <xsl:with-param name="locale" select="concat('_', $locales)"/>
                   <xsl:with-param name="filepath"
                                  select="$filepath"/>
                 </xsl:call-template>
               
                 <!-- Finally call for the default locale -->
                 <xsl:call-template name="write-list-core-employments-properties">
             <xsl:with-param name="locale"/>
             <xsl:with-param name="filepath" select="$filepath"/>
                 </xsl:call-template>
               
               </xsl:otherwise>
             
             </xsl:choose>
           
           </xsl:if>
        
        </xsl:template>
        
        <xsl:template name="write-list-core-employments-properties">
          
            <xsl:param name="locale"/>
            <xsl:param name="filepath"/>
            
        <xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/General) + count(//EvidenceEntities/Properties[@locale=$locale]/Employment)&gt;0">
        
          <redirect:write select="concat($filepath, $locale, '.properties')">
          
      <xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/Employment)&gt;0">
      
      <xsl:variable name="employmentProperties" select="//EvidenceEntities/Properties[@locale=$locale]/Employment"/>
    
    <xsl:if test="count($employmentProperties/Help.PageDescription.List.CoreEmployments)&gt;0">
<xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$employmentProperties/Help.PageDescription.List.CoreEmployments"/>
          <xsl:with-param name="evidenceNode" select="."/>
	      <xsl:with-param name="altPropertyName">Help.PageDescription</xsl:with-param>
    </xsl:call-template> 
    </xsl:if>
    
    <xsl:if test="count($employmentProperties/Page.Title.Employment)&gt;0">
<xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$employmentProperties/Page.Title.Employment"/>
           <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
	      <xsl:with-param name="altPropertyName">Page.Title</xsl:with-param>
    </xsl:call-template>
    <xsl:text>&#xa;</xsl:text>                       
    </xsl:if>
    
<xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$employmentProperties/ Container.Label.Action"/>
          <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template>       
    
<xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$employmentProperties/  ActionControl.Label.Employment"/>
          <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template>
    
<xsl:call-template name="callGenerateProperties">
          <xsl:with-param name="propertyNode" select="$employmentProperties/ Field.Label.Primary"/>
          <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
        </xsl:call-template>
    
    
    
<xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$employmentProperties/ Field.Label.Employer"/>
      <xsl:with-param name="evidenceNode" select="."/>
    </xsl:call-template>
    
    
    
<xsl:call-template name="callGenerateProperties">
       <xsl:with-param name="propertyNode" select="$employmentProperties/ Field.Label.Occupation"/>
       <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template>
    
    
    
<xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$employmentProperties/ Field.Label.From"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template>
    
    
    
<xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$employmentProperties/ Field.Label.To"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template>
    
      </xsl:if>
      
      <xsl:if test="count(//EvidenceEntities/Properties[@locale=$locale]/General)&gt;0">
      
      <xsl:variable name="generalProperties" select="//EvidenceEntities/Properties[@locale=$locale]/General"/>
    
    
    <xsl:call-template name="callGenerateProperties">
        <xsl:with-param name="propertyNode" select="$generalProperties/ActionControl.Label.New"/>
        <xsl:with-param name="evidenceNode" select="."/>
    </xsl:call-template> 
    
    
    
Container.Separator=\ |\ 
<xsl:call-template name="callGenerateProperties">
       <xsl:with-param name="propertyNode" select="$generalProperties/ ActionControl.Label.View"/>
       <xsl:with-param name="evidenceNode" select="."/>
      </xsl:call-template>
    
        
    
<xsl:call-template name="callGenerateProperties">
        <xsl:with-param name="propertyNode" select="$generalProperties/  ActionControl.Label.Cancel"/>
        <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template>
    
      </xsl:if>
    
        </redirect:write>
        
        </xsl:if>
    </xsl:template>
    
</xsl:stylesheet>