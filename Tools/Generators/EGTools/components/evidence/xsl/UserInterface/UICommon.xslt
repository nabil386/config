<?xml version="1.0" encoding="UTF-8"?>
<!--
Licensed Materials - Property of IBM

PID 5725-H26

Copyright IBM Corporation 2006,2021. All Rights Reserved.

US Government Users Restricted Rights - Use, duplication or disclosure
restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<xsl:stylesheet
  extension-element-prefixes="redirect xalan"
  xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect"
  version="1.0"
  xmlns:xalan="http://xml.apache.org/xslt"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>
  <xsl:import href="../EvidenceCommon.xslt"/>
  <xsl:import href="CreateProperties.xslt"/>
  
  <!-- Open property mappings file once for use by all xslt that generate properties. -->
  <xsl:variable name="file">../propertyMappings.xml</xsl:variable>
  <xsl:variable name="propertyMappings" select="document($file)/mappings"/> 
  
  <xsl:variable name="facadeCaseIDAgg">caseIDKey</xsl:variable>
  <xsl:variable name="facadeEvidenceAgg">evidenceKey</xsl:variable>
  <xsl:variable name="facadeParentEvidenceAgg">parEvKey</xsl:variable>
  <xsl:variable name="facadeParentEvidenceKeyAgg">parentEvidenceCaseKey</xsl:variable>
  <xsl:variable name="facadeEvidenceCaseKeyAgg">evidenceCaseKey</xsl:variable>
  <xsl:variable name="facadePreAssociationAgg">preAssocKey</xsl:variable>

  <xsl:variable name="facadeViewKey">key</xsl:variable>
  <xsl:variable name="facadeDetails">dtls</xsl:variable>
  <xsl:variable name="facadeReturnDetails">result</xsl:variable>  
  
  <xsl:variable name="facadeCaseParticipantSearchMethod">listCaseParticipantWithoutDuplicates</xsl:variable>  
  <xsl:variable name="facadeCaseParticipantReadMethod">readCaseParticipant</xsl:variable>  
  
  <xsl:variable name="facadeConcernRoleRelationshipCaseParticipantReadMethod">readByEvidenceIDAttributeName</xsl:variable> 
  <xsl:variable name="employmentReadByCaseParticipantMethod">readEmploymentByCaseParticipant</xsl:variable> 
  <xsl:variable name="employmentListMethod">listEmploymentByEvidenceKey</xsl:variable>
  <!-- VM -->
  <xsl:variable name="employmentListByCPRMethod">listEmploymentsForCaseParticipant</xsl:variable>
  <!-- END, VM -->
  <xsl:variable name="relatedEntityReadMethod">RelatedEntityAttributes</xsl:variable>
  <xsl:variable name="typeEntityAttribute">relatedEntityAttribute</xsl:variable>
  <xsl:variable name="relatedEntityAttribute">RELATED_ENTITY_ATTRIBUTE</xsl:variable>
  <xsl:variable name="associatedLinkText">linkText</xsl:variable>  

  <!-- <xsl:variable name="evidenceTabUI">ISP_ResourcesEvidenceTab_content.vim</xsl:variable> -->
  
  <xsl:variable name="evidenceListEvidenceView">Evidence_listEvidenceView.vim</xsl:variable>
  <xsl:variable name="evidenceHeaderView">Evidence_viewHeaderForModal.vim</xsl:variable>
  <!-- BEGIN, CR00219910, CD -->
  <xsl:variable name="evidenceHeaderForObjectView">Evidence_viewHeaderForObject.vim</xsl:variable>
  <!-- END, CR00219910 -->
  <xsl:variable name="evidenceHeaderCreate">Evidence_createHeader.vim</xsl:variable>
  <xsl:variable name="evidenceHeaderModify">Evidence_modifyHeader1.vim</xsl:variable>

  <xsl:variable name="resolveParticipantHome">IntegratedCase_resolveParticipantHome</xsl:variable>
  <xsl:variable name="resolveEmployerHome">Person_resolveEmployerHome</xsl:variable>
  <xsl:variable name="createEmploymentPage">Person_createEmployment</xsl:variable>

  <xsl:variable name="hierarchyPath">Hierarchy</xsl:variable>
  
  <xsl:variable name="constantFileName">Constants.properties</xsl:variable>

  <xsl:variable name="typeComments">COMMENTS</xsl:variable>
  <xsl:variable name="fieldComments">Comments</xsl:variable>
  <xsl:variable name="evidenceID">evidenceID</xsl:variable>  
  <xsl:variable name="evidenceType">evType</xsl:variable>  
  <xsl:variable name="codeTableCode">CODETABLE_CODE</xsl:variable>
  <xsl:variable name="UITypeSummary">SUMMARY</xsl:variable>  
  
  <xsl:variable name="logfilename">Generator.log</xsl:variable>

  <xsl:variable name="searchPopupPath">SearchPopup</xsl:variable>
  <xsl:variable name="employmentPath">Employment</xsl:variable>

  <!-- TODO Put prefix in front of this and remove from references -->
  <xsl:variable name="facadeClass">EvidenceMaintenance</xsl:variable>
  <xsl:variable name="employmentClass">Person</xsl:variable>
  
  <!-- BEGIN, CR00118883, POB -->
  <xsl:variable name="fromActiveWorkspacePostfix">Active</xsl:variable>
  <xsl:variable name="fromInEditWorkspacePostfix">InEdit</xsl:variable>
  <!-- END, CR00118883 -->
  
  <!-- BEGIN, CR00219910, CD -->
  <xsl:variable name="businessObjectViewVersion">Object</xsl:variable>
  <!-- END, CR00219910 -->
  <xsl:variable name="historyRecordViewVersion">HistRec</xsl:variable>
  <xsl:variable name="snapshotViewVersion">Snapshot</xsl:variable>
  
  <xsl:template name="ModifyViewPage_ParentParticipantSearchInterface">

    <xsl:param name="prefix"/> 
    <xsl:param name="columnName"/>
    
  <SERVER_INTERFACE
    CLASS="{$prefix}{$facadeClass}"
    NAME="{$columnName}"
    OPERATION="{$facade_modifyViewPage_GetAssociatedCaseParticipantReadMethod}"
  />

  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="evidenceType"
    />
    <TARGET
      NAME="{$columnName}"
      PROPERTY="evidenceType"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="evidenceID"
    />
    <TARGET
      NAME="{$columnName}"
      PROPERTY="evidenceID"
    />
  </CONNECT>

  </xsl:template>
  <xsl:template name="CreatePage_ParentParticipantSearchInterface">
  
      <xsl:param name="prefix"/> 
      <xsl:param name="columnName"/>
      
    <SERVER_INTERFACE
      CLASS="{$prefix}{$facadeClass}"
      NAME="{$columnName}"
      OPERATION="{$facade_createPage_GetAssociatedCaseParticipantReadMethod}"
    />
  
    <CONNECT>
      <SOURCE
        NAME="PAGE"
        PROPERTY="parEvType"
      />
      <TARGET
        NAME="{$columnName}"
        PROPERTY="evidenceType"
      />
    </CONNECT>
    <CONNECT>
      <SOURCE
        NAME="PAGE"
        PROPERTY="parEvID"
      />
      <TARGET
        NAME="{$columnName}"
        PROPERTY="evidenceID"
      />
    </CONNECT>
  
  </xsl:template>

<xsl:template name="ParseParents">

  <xsl:param name="parentName"/>
  <xsl:param name="childLevel"/>

  <xsl:variable name="ParentEvidenceEntity" select="//EvidenceEntities/EvidenceEntity[@name=$parentName]"/>

  <xsl:if test="count($ParentEvidenceEntity/Relationships/Parent)>0">          
  
    <xsl:call-template name="ParseParents">
    
      <xsl:with-param name="parentName" select="$ParentEvidenceEntity/Relationships/Parent/@name"/>
      <xsl:with-param name="childLevel"><xsl:value-of select="number(number($childLevel)+1)"/></xsl:with-param>
  
    </xsl:call-template>

  </xsl:if>        

  <xsl:value-of select="number($childLevel)"/>    

</xsl:template>

<xsl:template name="GetChildLevel">

  <xsl:param name="capName"/>

  <xsl:variable name="EvidenceEntity" select="//EvidenceEntities/EvidenceEntity[@name=$capName]"/>

  <xsl:variable name="childLevel">
    <xsl:choose>
      <xsl:when test="count($EvidenceEntity/Relationships/Parent)=0 and count($EvidenceEntity/Relationships/MandatoryParents)=0"></xsl:when>
      <xsl:when test="count($EvidenceEntity/Relationships/Parent)>0">
        <xsl:call-template name="ParseParents">
      
          <xsl:with-param name="parentName" select="$EvidenceEntity/Relationships/Parent/@name"/>
          <xsl:with-param name="childLevel" select="number(1)"/>
    
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="count($EvidenceEntity/Relationships/MandatoryParents/Parent)>0">
        <xsl:call-template name="ParseParents">
          
          <xsl:with-param name="parentName" select="$EvidenceEntity/Relationships/Parent/@name"/>
          <xsl:with-param name="childLevel" select="number(1)"/>
          
        </xsl:call-template>
      </xsl:when>
    </xsl:choose>
  </xsl:variable>   

  <xsl:value-of select="string-length($childLevel)"/>
  
</xsl:template>
  
  <!-- Call to create properties template passing property mappings file plus other parameters -->
  
  <xsl:template name="callGenerateProperties">
    
    <xsl:param name="propertyNode"/>
    <xsl:param name="altPropertyName"/>
    <xsl:param name="evidenceNode"/>
    
    <xsl:if test="count($propertyNode)">
    <xsl:call-template name="generateProperty">
            
            <xsl:with-param name="propertyNode" select="$propertyNode"/>
            <xsl:with-param name="altPropertyName" select="$altPropertyName"/>
            <xsl:with-param name="evidenceNode" select="$evidenceNode"/>
            <xsl:with-param name="propertyMappings" select="$propertyMappings"/>
            
    </xsl:call-template>
    </xsl:if>
  </xsl:template>
  
    
  <!-- Call to create 3-way participant clusters -->      
    <xsl:template name="printFullCreateParticipantClusters">              
  
      <xsl:param name="capName"/>  
      <xsl:param name="currentField"/>
      <xsl:param name="nsStruct"/>
      <xsl:variable name="appendage">  
        <xsl:choose>  
          <xsl:when test="$currentField/@name and $currentField/@name!=''">.<xsl:value-of select="$currentField/@name"/></xsl:when>
          <xsl:otherwise></xsl:otherwise>
        </xsl:choose>     
      </xsl:variable>
      <xsl:variable name="caseParticipantRoleIDAggregation">  
        <xsl:choose>  
          <xsl:when test="$currentField/@name and $currentField/@name!=''"><xsl:value-of select="$currentField/@name"/>CaseParticipantDetails$</xsl:when>
          <xsl:otherwise>caseParticipantDetails$</xsl:otherwise>
        </xsl:choose>     
      </xsl:variable>

      <!-- BEGIN, CR00112535, CD -->
      <xsl:if test="count($currentField/ParticipantSearchType) &gt; 0">
      <!-- END, CR00112535 -->
      <CLUSTER 
        DESCRIPTION="Cluster.Description.CaseParticipant{$appendage}"
        STYLE="cluster-cpr-no-border"
        NUM_COLS="2"
        LABEL_WIDTH="40"
        >  
  
        <FIELD  
          LABEL="Field.Label.CaseParticipant{$appendage}"  
          USE_BLANK="true"  
          USE_DEFAULT="false"  
          >
  
          <CONNECT>  
            <INITIAL  
              HIDDEN_PROPERTY="searchCaseParticipantDetails$caseParticipantRoleID"  
              NAME="{$capName}{$currentField/@columnName}"  
              PROPERTY="nameAndAgeOpt"  
            />  
          </CONNECT>
  
          <CONNECT>  
            <TARGET  
              NAME="ACTION"  
              PROPERTY="{$caseParticipantRoleIDAggregation}caseParticipantRoleID"  
            />  
          </CONNECT>
  
        </FIELD>
        
        <FIELD CONTROL="SKIP"/>
  
     </CLUSTER>      
  </xsl:if>
  
      <CLUSTER  
        DESCRIPTION="Cluster.Description.RegisteredParticipant{$appendage}"  
        STYLE="cluster-cpr-no-border"
      >
        <xsl:choose>
          <xsl:when test="$nsStruct='Yes'">
            <xsl:attribute name="NUM_COLS">1</xsl:attribute>
            <xsl:attribute name="LABEL_WIDTH">20</xsl:attribute>
          
        <CONTAINER LABEL="Field.Label.Participant{$appendage}">
          <FIELD WIDTH="20">          
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="{$caseParticipantRoleIDAggregation}participantType"
            />
          </CONNECT>
        </FIELD>
          <FIELD WIDTH="70">  
          <CONNECT>  
            <TARGET  
              NAME="ACTION"  
              PROPERTY="{$caseParticipantRoleIDAggregation}participantRoleID"  
            />  
          </CONNECT>  
        </FIELD>
        </CONTAINER>
          </xsl:when>
          <xsl:otherwise>
              
    <xsl:attribute name="NUM_COLS">2</xsl:attribute>
          <xsl:attribute name="LABEL_WIDTH">40</xsl:attribute>
          
        <FIELD  
          LABEL="Field.Label.Participant{$appendage}">  
          <CONNECT>  
            <TARGET  
              NAME="ACTION"  
              PROPERTY="{$caseParticipantRoleIDAggregation}participantRoleID"  
            />  
          </CONNECT>  
        </FIELD>
          
          </xsl:otherwise>
        </xsl:choose>
        
        <FIELD CONTROL="SKIP"/>
  
      </CLUSTER> 
  
      <!-- BEGIN, CR00101382, POB --> 
     <CLUSTER  
        DESCRIPTION="Cluster.Description.UnRegisteredParticipant{$appendage}"
        NUM_COLS="2"
        STYLE="cluster-cpr-no-border"
        LABEL_WIDTH="40"
      > 
        <!-- END, CR00101382 -->
       
       <!-- BEGIN, CR00101878, POB -->
       <xsl:choose>
         <xsl:when test="$currentField/@singleNameField='No'">
        <FIELD LABEL="Field.Label.firstName">  
          <CONNECT>  
            <TARGET PROPERTY="{$caseParticipantRoleIDAggregation}participantName" NAME="ACTION"/>  
          </CONNECT>  
        </FIELD>
  
        <FIELD LABEL="Field.Label.secondName">  
          <CONNECT>  
            <TARGET PROPERTY="{$caseParticipantRoleIDAggregation}participantSecondName" NAME="ACTION"/>  
          </CONNECT>  
        </FIELD>
         </xsl:when>
         <xsl:otherwise>
           <FIELD LABEL="Field.Label.SingleName{$appendage}">  
          <CONNECT>  
            <TARGET PROPERTY="{$caseParticipantRoleIDAggregation}participantName" NAME="ACTION"/>  
          </CONNECT>  
        </FIELD>
        <FIELD CONTROL="SKIP"/>
         </xsl:otherwise>
       </xsl:choose>
       <!-- END, CR00101878, POB -->
   </CLUSTER> 
  
      <CLUSTER  
        NUM_COLS="2"  
        TAB_ORDER="ROW"  
        STYLE="cluster-cpr-no-border"
        LABEL_WIDTH="40"
        >
  
        <FIELD LABEL="Field.Label.address">  
          <CONNECT>  
            <TARGET  
              NAME="ACTION"  
              PROPERTY="{$caseParticipantRoleIDAggregation}address"  
            />  
          </CONNECT>  
        </FIELD>
  
      </CLUSTER> 
  
      <CLUSTER NUM_COLS="2" STYLE="cluster-cpr-no-border" LABEL_WIDTH="40"> 
        <FIELD LABEL="Field.Label.areaCode">  
          <CONNECT>  
            <TARGET  
              NAME="ACTION"  
              PROPERTY="{$caseParticipantRoleIDAggregation}phoneAreaCode"  
            />  
          </CONNECT>  
        </FIELD>
        
        <FIELD LABEL="Field.Label.phoneNumber">  
          <CONNECT>  
            <TARGET  
              NAME="ACTION"  
              PROPERTY="{$caseParticipantRoleIDAggregation}phoneNumber"  
            />
          </CONNECT>  
        </FIELD>
  
    </CLUSTER>
  
  </xsl:template>
  
  
  <!-- BEGIN, CR00100986, CD -->
  <!-- Call to create 3-way participant cluster static properties -->    
  <xsl:template name="printFullCreateParticipantClusterStaticProperties">      
  
    <xsl:param name="generalProperties"/>
    
    <!-- Static properties -->
    <xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Field.Label.firstName"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template> 
    <xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Field.Label.secondName"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template>
    <!-- BEGIN, CR00101878, POB -->
    <xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Field.Label.singleName"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template>
    <!-- END, CR00101878, POB -->
    <xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Field.Label.address"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template> 
    <xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Field.Label.areaCode"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template> 
    <xsl:call-template name="callGenerateProperties">
      <xsl:with-param name="propertyNode" select="$generalProperties/Field.Label.phoneNumber"/>
      <xsl:with-param name="evidenceNode" select="&apos;&apos;"/>
    </xsl:call-template> 
    
  </xsl:template>   
  
  <!-- Call to create 3-way participant cluster dynamic properties -->    
  <xsl:template name="printFullCreateParticipantClusterDynamicProperties">      

    <xsl:param name="dynamicProperties"/>
    <xsl:param name="locale"/>

    <xsl:for-each select="$dynamicProperties">
      <xsl:value-of select="@name"/>=<xsl:choose>
        <xsl:when test="./Label[@locale=$locale]/@value!=''"><xsl:value-of select="./Label[@locale=$locale]/@value"/></xsl:when>
        <xsl:otherwise><xsl:value-of select="@name"/><xsl:text>&#x9;</xsl:text>property must be set.</xsl:otherwise>
      </xsl:choose>
      <xsl:text>&#xa;</xsl:text>              
      <xsl:if test="count(./Help[@locale=$locale])&gt;0">
        <xsl:value-of select="@name"/>.Help=<xsl:value-of select="./Help[@locale=$locale]/@value"/>
        <xsl:text>&#xa;</xsl:text>
      </xsl:if>
    </xsl:for-each>
      
  </xsl:template> 
  <!-- END, CR00100986 -->
  
  
  <xsl:template name="CreateParticipantSearchInterface">
  
    <xsl:param name="capName"/>
    <xsl:param name="Field"/>
    <xsl:param name="baseAggregation"/>

    <xsl:variable name="facadeCaseParticipantMethod">
      <xsl:choose>
        <xsl:when test="$Field/@metatype=$metatypeParentCaseParticipant">
          <xsl:value-of select="$facade_createPage_GetAssociatedCaseParticipantReadMethod"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$facadeCaseParticipantSearchMethod"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>


    <SERVER_INTERFACE CLASS="{$prefix}{$facadeClass}" NAME="{$capName}{@columnName}"
      OPERATION="{$facadeCaseParticipantMethod}"/>

    <xsl:choose>
      <xsl:when test="$facadeCaseParticipantMethod!=$facade_createPage_GetAssociatedCaseParticipantReadMethod">

        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="{$capName}{@columnName}" PROPERTY="caseID"/>
        </CONNECT>

        <xsl:if test="$facadeCaseParticipantMethod=$facade_createPage_GetAssociatedCaseParticipantReadMethod">

          <CONNECT>
            <SOURCE NAME="{$capName}{@columnName}" PROPERTY="caseParticipantRoleID"/>
            <TARGET NAME="ACTION" PROPERTY="{$baseAggregation}dtls${@columnName}"/> </CONNECT>

        </xsl:if>

        <xsl:if test="count(ParticipantSearchType)>0 ">
          <!--and $Field/@readOnly!='Yes'-->
          <xsl:if
            test="$facadeCaseParticipantMethod=$facadeCaseParticipantReadMethod or $facadeCaseParticipantMethod=$facadeCaseParticipantSearchMethod">
            <CONNECT>
              <SOURCE NAME="CONSTANT" PROPERTY="{$capName}.{$Field/@columnName}.CaseParticipantType"/>
              <TARGET NAME="{$capName}{$Field/@columnName}" PROPERTY="caseParticipantTypeList"/>
            </CONNECT>
          </xsl:if>
        </xsl:if>

      </xsl:when>
      <xsl:otherwise>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="evidenceID"/>
          <TARGET NAME="{$capName}{@columnName}" PROPERTY="evidenceID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="evidenceType"/>
          <TARGET NAME="{$capName}{@columnName}" PROPERTY="evidenceType"/>
        </CONNECT>
      </xsl:otherwise>
    </xsl:choose>
  
  </xsl:template>
  
  
  <!-- BEGIN, CR00114711, CD -->
  <!-- BEGIN, CR00112474, DG -->
  <xsl:template name="GetChildButtons">

    <xsl:param name="capName"/>
    <xsl:param name="parents"/>
    <xsl:param name="fromChildCreate"/>  
    <xsl:param name="childLevelNo"/>  
    <xsl:param name="childUIInd"/>
    <xsl:param name="caseType" />
    <xsl:param name="modal"/>
    
      <xsl:variable name="EvidenceEntity" select="//EvidenceEntities/EvidenceEntity[@name=$capName]"/>
<!--
  displayName=<xsl:value-of select="$EvidenceEntity/@displayName"/>
  fromChildCreate=<xsl:value-of select="$fromChildCreate"/>
  number of child relationships = <xsl:value-of select="count($EvidenceEntity/Relationships/Child)"/>
  number of child createButtons = <xsl:value-of select="count($EvidenceEntity/Relationships/Child[@createButton='Yes'])"/>
  are there associations? = <xsl:value-of select="$EvidenceEntity/Relationships/@association"/>
  number of associations with createButtons = <xsl:value-of select="count($EvidenceEntity/Relationships/Association[@createButton='Yes'])"/>
  number of external links = <xsl:value-of select="count($EvidenceEntity/UserInterfaceLayer/SaveAddExternalLink)"/>
-->
      <!-- BEGIN, CR00113102, DG-->
      <!-- If parent or association set to YES then open new cluster and Action Set -->
      <!-- BEGIN, CR00221984, CD -->
      <xsl:if 
        test="($EvidenceEntity/Relationships/@association='Yes' and 
               count($EvidenceEntity/Relationships/Association[@createButton='Yes']) &gt; 0 and 
               $fromChildCreate=''
              ) or (
               count($EvidenceEntity/UserInterfaceLayer/SaveAddExternalLink) &gt; 0
              )">



        <CLUSTER STYLE="outer-cluster-borderless">

          <ACTION_SET ALIGNMENT="CENTER" BOTTOM="false">
      <!-- Removing save and add child action controls -->
      <!-- END, CR00221984, CD -->
      
        <xsl:if test="$EvidenceEntity/Relationships/@association='Yes'">

          <xsl:for-each select="$EvidenceEntity/Relationships/Association[@createButton='Yes']">

            <xsl:if test="@from!=''">

        <ACTION_CONTROL
          IMAGE="SaveAndAssociate{@from}Button"
          LABEL="ActionControl.Label.SaveAndAssociate{@from}Button"
          TYPE="SUBMIT"
        >
          <LINK 
            PAGE_ID="{$prefix}_list{@from}{$caseType}For{$capName}{$caseType}Association_fromCreate{$parents}"
            DISMISS_MODAL="false">
            <xsl:if test="$modal='Yes'">
              <xsl:attribute name="SAVE_LINK">false</xsl:attribute>
            </xsl:if>
            
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
                NAME="ACTION"
                PROPERTY="{$facadeReturnDetails}${$facadeEvidenceAgg}${$evidenceID}"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="linkedEvID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="ACTION"
                PROPERTY="{$facadeReturnDetails}${$facadeEvidenceAgg}${$evidenceType}"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="linkedEvType"
              />
            </CONNECT>
      <xsl:if test="$childLevelNo>=1 or $childUIInd='Yes'">
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

            </xsl:if>

            <xsl:if test="@to!='' and @displayInHierarchy='Yes'">

        <ACTION_CONTROL
          IMAGE="SaveAndAssociate{@to}Button"
          LABEL="ActionControl.Label.SaveAndAssociate{@to}Button"
          TYPE="SUBMIT"
        >
          <LINK 
            PAGE_ID="{$prefix}_list{@to}{$caseType}ForAssociation_fromCreateParent"
            DISMISS_MODAL="false">
            <xsl:if test="$modal='Yes'">
              <xsl:attribute name="SAVE_LINK">false</xsl:attribute>
            </xsl:if>
             
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
                NAME="ACTION"
                PROPERTY="{$facadeReturnDetails}${$facadeEvidenceAgg}${$evidenceID}"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="linkedEvID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="ACTION"
                PROPERTY="{$facadeReturnDetails}${$facadeEvidenceAgg}${$evidenceType}"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="linkedEvType"
              />
            </CONNECT>
      <xsl:if test="$childLevelNo>=1 or $childUIInd='Yes'">
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="parEvID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="grandParEvID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="parEvType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="grandParEvType"
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
                PROPERTY="greatGrandParEvID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="grandParEvType"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="greatGrandParEvType"
              />
            </CONNECT>
      </xsl:if>
          </LINK>
        </ACTION_CONTROL>

            </xsl:if>

          </xsl:for-each>

        </xsl:if>

        <!-- BEGIN, CR00113102, DG -->
        <xsl:for-each select="$EvidenceEntity/UserInterfaceLayer/SaveAddExternalLink">
          <xsl:variable name="linkName">
            <xsl:call-template name="replaceAll">
              <xsl:with-param name="stringToTransform" select="@displayName"/>
              <xsl:with-param name="target" select="' '"/>
              <xsl:with-param name="replacement" select="''"/>
            </xsl:call-template>
          </xsl:variable>
          <ACTION_CONTROL
            IMAGE="SaveAndAdd{$linkName}Button"
            LABEL="ActionControl.Label.Save"
            TYPE="SUBMIT"
          >
            <LINK SAVE_LINK="false" PAGE_ID="{@pageName}">
              <xsl:for-each select="Parameters/Parameter">
                <CONNECT>
                  <SOURCE NAME="{@source}" PROPERTY="{@from}" />
                  <TARGET NAME="PAGE" PROPERTY="{@to}" />
                </CONNECT>
              </xsl:for-each>
            </LINK>
          </ACTION_CONTROL>  
        </xsl:for-each>
        <!-- END, CR00113102 -->

        </ACTION_SET>


      </CLUSTER>
      </xsl:if>
      <!-- END, CR00113102 -->
  </xsl:template>
  <!-- END, CR00112474, DG -->
  <!-- END, CR00114711 -->
  
</xsl:stylesheet>
