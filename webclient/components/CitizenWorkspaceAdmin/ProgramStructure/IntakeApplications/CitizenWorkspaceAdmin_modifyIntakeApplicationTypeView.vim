<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012,2013. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->  

<!-- ====================================================================== -->
<!-- Copyright (c) 2008-2012 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- ====================================================================== -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">
  <SERVER_INTERFACE CLASS="CitizenWorkspaceAdmin" NAME="DISPLAY" OPERATION="viewIntakeApplicationType" />
  <SERVER_INTERFACE CLASS="CitizenWorkspaceAdmin" NAME="ACTION" OPERATION="modifyIntakeApplicationType" PHASE="ACTION" />
  <PAGE_PARAMETER NAME="intakeApplicationTypeID" />
  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="intakeApplicationTypeID" />
    <TARGET NAME="DISPLAY" PROPERTY="key$intakeApplicationTypeID" />
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="intakeApplicationTypeID" />
    <TARGET NAME="ACTION" PROPERTY="intakeApplicationTypeID" />
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="details$versionNo" />
    <TARGET NAME="ACTION" PROPERTY="versionNo" />
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="details$nameTextID" />
    <TARGET NAME="ACTION" PROPERTY="nameTextID" />
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="channel" />
    <TARGET NAME="ACTION" PROPERTY="channel" />
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="details$descriptionTextID" />
    <TARGET NAME="ACTION" PROPERTY="descriptionTextID" />
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="summaryTextID" />
    <TARGET NAME="ACTION" PROPERTY="summaryTextID" />
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="submissionConfirmPageTitleID" />
    <TARGET NAME="ACTION" PROPERTY="submissionConfirmPageTitleID" />
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="submissionConfirmPageTextID" />
    <TARGET NAME="ACTION" PROPERTY="submissionConfirmPageTextID" />
  </CONNECT>
  
  <CLUSTER LABEL_WIDTH="60" NUM_COLS="2" >
    <FIELD LABEL="Field.Title.Name">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$name" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="name" />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Title.Schema"  USE_BLANK="true">
      <CONNECT>
        <INITIAL NAME="DISPLAY" PROPERTY="schemaName" />
      </CONNECT>    
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="intakeSchemaName" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="intakeSchemaName" />
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
    <FIELD LABEL="Field.Title.ProgramSelection">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="programSelectionInd" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="programSelectionInd" />
      </CONNECT>
    </FIELD>     
    <FIELD LABEL="Field.Title.ClientRegistration" USE_BLANK="true">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$clientRegistration"/>
      </CONNECT>   
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="details$clientRegistration"/>
      </CONNECT>         
    </FIELD>      
    
    <FIELD LABEL="Field.Title.ApplicationScript" WIDTH="100" WIDTH_UNITS="PERCENT" USE_BLANK="true">
      <CONNECT>
        <INITIAL NAME="DISPLAY" HIDDEN_PROPERTY="scriptID" PROPERTY="listDtls$name" />
      </CONNECT>
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="intakeScriptID" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="intakeScriptID" />
      </CONNECT>
    </FIELD>    
    <FIELD USE_BLANK="true" LABEL="Field.Title.SubmissionScript"  >
      <CONNECT>
        <INITIAL NAME="DISPLAY" HIDDEN_PROPERTY="scriptID" PROPERTY="listDtls$name" />
      </CONNECT>
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="submissionScriptID" />
      </CONNECT>       
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="submissionScriptID" />
      </CONNECT>      
    </FIELD>      
    <FIELD USE_BLANK="true" LABEL="Field.Title.PDFForm" >
      <CONNECT>
        <INITIAL NAME="DISPLAY" HIDDEN_PROPERTY="dtls$pdfFormID" PROPERTY="pdfDetails$dtls$name" />
      </CONNECT>    
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="details$pdfFormID" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="pdfFormID" />
      </CONNECT>       
    </FIELD>    
    <FIELD LABEL="Field.Title.SubmitOnCompletion">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="submitOnCompletionInd" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="submitOnCompletionInd" />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Title.Icon">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$details$icon" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="details$details$icon" />
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
        <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$description" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="description" />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER LABEL_WIDTH="10" TITLE="Cluster.Title.SubmissionConfirmationDetails">
    <FIELD LABEL="Field.Title.ClusterTitle" WIDTH="100">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="submissionConfirmPageTitle" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="submissionConfirmPageTitle" />
      </CONNECT>
    </FIELD>
    
      <FIELD LABEL="Field.Title.Text" HEIGHT="150">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="submissionConfirmPageText" />
        </CONNECT>
        <CONNECT>
          <TARGET NAME="ACTION" PROPERTY="submissionConfirmPageText" />
        </CONNECT>
      </FIELD>
    
  </CLUSTER>
</VIEW>
