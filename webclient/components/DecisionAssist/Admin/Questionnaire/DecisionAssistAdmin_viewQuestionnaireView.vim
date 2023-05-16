<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2007-2011 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows the user to view the details of a questionnaire.      -->
<VIEW
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
  </PAGE_TITLE>
  <SERVER_INTERFACE
    CLASS="Questionnaire"
    NAME="VIEW_QUESTIONNAIREVERSION"
    OPERATION="viewQuestionnaireDetails"
    PHASE="DISPLAY"
  />
  <SERVER_INTERFACE
    CLASS="Questionnaire"
    NAME="READ_IEGSCRIPTKEY_INFO"
    OPERATION="readIEGScriptKey"
    PHASE="DISPLAY"
  />
  <PAGE_PARAMETER NAME="questionnaireVersionID"/>
  <PAGE_PARAMETER NAME="questionnaireID"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="questionnaireVersionID"
    />
    <TARGET
      NAME="VIEW_QUESTIONNAIREVERSION"
      PROPERTY="key$dtls$questionnaireVersionID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="questionnaireVersionID"
    />
    <TARGET
      NAME="READ_IEGSCRIPTKEY_INFO"
      PROPERTY="key$dtls$questionnaireVersionID"
    />
  </CONNECT>


  <!-- BEGIN, CR00215172, PS -->
  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="2"
  >
    <!-- END, CR00215172 -->
    <FIELD LABEL="Field.Label.QuestionnaireInitialCreationDate">
      <CONNECT>
        <SOURCE
          NAME="VIEW_QUESTIONNAIREVERSION"
          PROPERTY="result$initialVersionDtls$dtls$dateCreated"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.QuestionnaireInitiallyReleasedOn">
      <CONNECT>
        <SOURCE
          NAME="VIEW_QUESTIONNAIREVERSION"
          PROPERTY="result$initialVersionDtls$dtls$fromDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.QuestionnaireInitiallyCreatedBy">
      <CONNECT>
        <SOURCE
          NAME="VIEW_QUESTIONNAIREVERSION"
          PROPERTY="result$initialVersionDtls$createdUserFullName"
        />
      </CONNECT>
      <!-- BEGIN, CR00184681, AK -->
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Organization_viewUserDetails"
      >
        <!-- END, CR00184681 -->
        <CONNECT>
          <SOURCE
            NAME="VIEW_QUESTIONNAIREVERSION"
            PROPERTY="result$initialVersionDtls$dtls$createdBy"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
      </LINK>
    </FIELD>
  </CLUSTER>
  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="2"
    TITLE="Cluster.Title.QuestionnaireVersionDetails"
  >
    <FIELD LABEL="Field.Label.QuestionnaireCreationDate">
      <CONNECT>
        <SOURCE
          NAME="VIEW_QUESTIONNAIREVERSION"
          PROPERTY="result$specifiedVersionDtls$dtls$dateCreated"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.QuestionnaireCreatedBy">
      <CONNECT>
        <SOURCE
          NAME="VIEW_QUESTIONNAIREVERSION"
          PROPERTY="result$specifiedVersionDtls$createdUserFullName"
        />
      </CONNECT>
      <!-- BEGIN, CR00198297, SS -->
      <!-- BEGIN, CR00184681, AK -->
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Organization_viewUserDetails"
        WINDOW_OPTIONS="width=625"
      >
        <!-- END, CR00184681 -->
        <!-- END, CR00198297 -->
        <CONNECT>
          <SOURCE
            NAME="VIEW_QUESTIONNAIREVERSION"
            PROPERTY="result$specifiedVersionDtls$dtls$createdBy"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
      </LINK>
    </FIELD>
  </CLUSTER>
  <LIST TITLE="Cluster.Title.QuestionDetails">
    <!-- BEGIN, CR00226313, PS -->
    <FIELD LABEL="Field.Label.Questions">
      <!-- END, CR00226313 -->
      <CONNECT>
        <SOURCE
          NAME="VIEW_QUESTIONNAIREVERSION"
          PROPERTY="result$questionsList$dtls$questionText"
        />
      </CONNECT>
    </FIELD>
  </LIST>
  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.QuestionnaireComments"
  >
    <FIELD HEIGHT="4">
      <CONNECT>
        <SOURCE
          NAME="VIEW_QUESTIONNAIREVERSION"
          PROPERTY="result$specifiedVersionDtls$dtls$comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
