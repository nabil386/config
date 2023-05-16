<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2007-2010 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to view ICD code details.                    -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <SERVER_INTERFACE
    CLASS="ICDCodes"
    NAME="VIEW_ICD_CODE"
    OPERATION="viewICDCodeDetails"
    PHASE="DISPLAY"
  />
  <SERVER_INTERFACE
    CLASS="ICDCodes"
    NAME="SI_LIST_ICDTRANSLATIONS"
    OPERATION="readICDTextTranslations"
  />
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="ICDCodeID"
    />
    <TARGET
      NAME="VIEW_ICD_CODE"
      PROPERTY="key$keyDtls$ICDCodeID"
    />
  </CONNECT>
  <PAGE_PARAMETER NAME="ICDCodeID"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="ICDCodeID"
    />
    <TARGET
      NAME="SI_LIST_ICDTRANSLATIONS"
      PROPERTY="key$keyDtls$ICDCodeID"
    />
  </CONNECT>


  <!-- BEGIN, CR00226313, PS -->
  <CLUSTER NUM_COLS="2">
    <!-- END, CR00226313 -->
    <FIELD LABEL="Field.Label.CreatedDate">
      <CONNECT>
        <SOURCE
          NAME="VIEW_ICD_CODE"
          PROPERTY="result$viewCodeDtls$viewDtls$dateCreated"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.CreatedBy">
      <CONNECT>
        <SOURCE
          NAME="VIEW_ICD_CODE"
          PROPERTY="result$viewCodeDtls$createdUserFullName"
        />
      </CONNECT>
      <!-- BEGIN, CR00198297, SS -->
      <!-- BEGIN, CR00184681, AK -->
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Organization_viewUserDetails"
        WINDOW_OPTIONS="width=600"
      >
        <!-- END, CR00184681 -->
        <!-- END, CR00198297 -->
        <CONNECT>
          <SOURCE
            NAME="VIEW_ICD_CODE"
            PROPERTY="result$viewCodeDtls$viewDtls$createdBy"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
      </LINK>
    </FIELD>
  </CLUSTER>
  <!-- BEGIN, CR00226313, PS -->
  <CLUSTER NUM_COLS="2">
    <!-- END, CR00226313 -->
    <CLUSTER
      SHOW_LABELS="false"
      TITLE="Cluster.Title.ICDCodeTranslation"
    >
      <INCLUDE FILE_NAME="DecisionAssistAdmin_listICDCodeTranslationView.vim"/>
    </CLUSTER>
    <!-- BEGIN, CR00207167, SS -->
    <CLUSTER
      LABEL_WIDTH="25"
      TITLE="Cluster.Title.ICDText"
    >
      <FIELD LABEL="Field.Label.ICDText">
        <CONNECT>
          <SOURCE
            NAME="VIEW_ICD_CODE"
            PROPERTY="result$viewCodeDtls$viewDtls$ICDText"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
  </CLUSTER>
  <!-- END, CR00207167 -->
</VIEW>
