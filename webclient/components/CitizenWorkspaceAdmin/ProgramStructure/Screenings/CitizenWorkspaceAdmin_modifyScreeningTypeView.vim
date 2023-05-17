<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->  

<!-- ====================================================================== -->
<!-- Copyright 2008, 2012 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- ====================================================================== -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">
  <SERVER_INTERFACE CLASS="CitizenWorkspaceAdmin" NAME="DISPLAY" OPERATION="viewScreeningType" />
  <SERVER_INTERFACE CLASS="CitizenWorkspaceAdmin" NAME="ACTION" OPERATION="modifyScreeningType" PHASE="ACTION" />
  <PAGE_PARAMETER NAME="screeningTypeID" />
  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="screeningTypeID" />
    <TARGET NAME="DISPLAY" PROPERTY="key$screeningTypeID" />
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="screeningTypeID" />
    <TARGET NAME="ACTION" PROPERTY="screeningTypeID" />
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="details$dtls$versionNo" />
    <TARGET NAME="ACTION" PROPERTY="versionNo" />
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="details$dtls$nameTextID" />
    <TARGET NAME="ACTION" PROPERTY="nameTextID" />
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="details$dtls$descriptionTextID" />
    <TARGET NAME="ACTION" PROPERTY="descriptionTextID" />
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="summaryTextID" />
    <TARGET NAME="ACTION" PROPERTY="summaryTextID" />
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="amIEligibleTextID" />
    <TARGET NAME="ACTION" PROPERTY="amIEligibleTextID" />
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="howToApplyTextID" />
    <TARGET NAME="ACTION" PROPERTY="howToApplyTextID" />
  </CONNECT>
  <CLUSTER NUM_COLS="2" LABEL_WIDTH="50">
    <FIELD LABEL="Field.Title.Name">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="details$name" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="name" />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Title.ProgramSelection">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="programSelectionInd" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="programSelectionInd" />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Title.Icon">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="icon" />
      </CONNECT>      
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="icon" />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Title.URL">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="url" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="url" />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Title.AllowRescreening">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="details$allowRescreeningInd" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="details$allowRescreeningInd" />
      </CONNECT>
    </FIELD>    
  </CLUSTER>
  <CLUSTER NUM_COLS="2" TITLE="Cluster.Title.EligibilityDetails" LABEL_WIDTH="50">
    <FIELD LABEL="Field.Title.EligibilityScript" >
      <CONNECT>
        <INITIAL NAME="DISPLAY" HIDDEN_PROPERTY="scriptID" PROPERTY="result$initialDtls$scriptList$listDtls$name" />
      </CONNECT>
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="screeningScriptID" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="screeningScriptID" />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Title.EligibilityRules" >
      <CONNECT> 
        <INITIAL NAME="DISPLAY" HIDDEN_PROPERTY="result$initialDtls$rulesList$dtls$ruleSetID" PROPERTY="result$initialDtls$rulesList$dtls$name" />
      </CONNECT>
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="details$ruleSetID" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="ruleSetID" />
      </CONNECT>      
    </FIELD>
    <FIELD LABEL="Field.Title.EligibilitySchema" >
      <CONNECT>
        <INITIAL NAME="DISPLAY" PROPERTY="schemaName" />
      </CONNECT>
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="screeningSchemaName" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="screeningSchemaName" />
      </CONNECT>       
    </FIELD>   
  </CLUSTER>  
  <CLUSTER NUM_COLS="2" TITLE="Cluster.Title.FilterDetails" LABEL_WIDTH="50">
    <FIELD LABEL="Field.Title.FilterScript"  USE_BLANK="true">
      <CONNECT>
        <INITIAL NAME="DISPLAY" HIDDEN_PROPERTY="scriptID" PROPERTY="result$initialDtls$scriptList$listDtls$name" />
      </CONNECT>
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="filterScreeningScriptID" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="filterScreeningScriptID" />
      </CONNECT>       
    </FIELD>
    
    <FIELD LABEL="Field.Title.FilterRules"  USE_BLANK="true">
      <CONNECT>
        <INITIAL NAME="DISPLAY" HIDDEN_PROPERTY="result$initialDtls$rulesList$dtls$ruleSetID" PROPERTY="result$initialDtls$rulesList$dtls$name" />
      </CONNECT>
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="filterRuleSetID" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="filterRuleSetID" />
      </CONNECT>        
    </FIELD>
    
    <FIELD LABEL="Field.Title.FilterSchema"  USE_BLANK="true">
      <CONNECT>
        <INITIAL NAME="DISPLAY" PROPERTY="schemaName" />
      </CONNECT>
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="filterScreeningSchemaName" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="filterScreeningSchemaName" />
      </CONNECT>       
    </FIELD>  
  </CLUSTER>   
  <CLUSTER TITLE="Cluster.Title.Summary" SHOW_LABELS="false">
    <FIELD HEIGHT="150">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="summary" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="summary" />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER TITLE="Cluster.Title.Description" SHOW_LABELS="false">
    <FIELD HEIGHT="150">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="details$description" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="description" />
      </CONNECT>
    </FIELD>
  </CLUSTER>  
  <CLUSTER TITLE="Cluster.Title.AmIEligible" SHOW_LABELS="false">
    <FIELD HEIGHT="150">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="amIEligible" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="amIEligible" />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER TITLE="Cluster.Title.HowToApply" SHOW_LABELS="false">
    <FIELD HEIGHT="150">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="howToApply" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="howToApply" />
      </CONNECT>
    </FIELD>
  </CLUSTER> 
</VIEW>
