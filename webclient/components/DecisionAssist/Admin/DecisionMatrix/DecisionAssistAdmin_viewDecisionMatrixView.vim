<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2007, 2014. All Rights Reserved.
  
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
<!-- This page allows the user to view decision matrix details.             -->
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
    <CONNECT>
      <SOURCE
        NAME="VIEW_DECISION_MATRIX"
        PROPERTY="result$currentVersion$dtls$name"
      />
    </CONNECT>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText2"
      />
    </CONNECT>
    <CONNECT>
      <SOURCE
        NAME="VIEW_DECISION_MATRIX"
        PROPERTY="result$currentVersion$dtls$decisionMatrixVersionNumber"
      />
    </CONNECT>
  </PAGE_TITLE>
  <PAGE_PARAMETER NAME="determinationConfigID"/>
  <PAGE_PARAMETER NAME="decisionMatrixID"/>
  <PAGE_PARAMETER NAME="decisionMatrixVersionID"/>
  <PAGE_PARAMETER NAME="determinationPackageVersionID"/>
  <SERVER_INTERFACE
    CLASS="DecisionMatrix"
    NAME="VIEW_DECISION_MATRIX"
    OPERATION="readDecisionMatrixVersion"
    PHASE="DISPLAY"
  />
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="decisionMatrixVersionID"
    />
    <TARGET
      NAME="VIEW_DECISION_MATRIX"
      PROPERTY="key$key$key$decisionMatrixVersionID"
    />
  </CONNECT>
  <!-- BEGIN, CR00215172, PS -->
  <CLUSTER NUM_COLS="2">
    <!-- END, CR00215172 -->
    <!-- BEGIN, CR00226313, PS -->
    <FIELD LABEL="Field.Label.MultipleOutcomes">
      <CONNECT>
        <SOURCE
          NAME="VIEW_DECISION_MATRIX"
          PROPERTY="result$currentVersion$dtls$multipleOutcomes"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ScoringByOutcomes">
      <CONNECT>
        <SOURCE
          NAME="VIEW_DECISION_MATRIX"
          PROPERTY="result$currentVersion$dtls$scoringByOutcomes"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.InitiallyCreatedBy">
      <CONNECT>
        <SOURCE
          NAME="VIEW_DECISION_MATRIX"
          PROPERTY="result$initialVersion$createdUserFullName"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Organization_viewUserDetails"
      >
        <CONNECT>
          <SOURCE
            NAME="VIEW_DECISION_MATRIX"
            PROPERTY="result$initialVersion$dtls$createdBy"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <FIELD LABEL="Field.Label.CumulativeScore">
      <CONNECT>
        <SOURCE
          NAME="VIEW_DECISION_MATRIX"
          PROPERTY="result$currentVersion$dtls$cumulativeScore"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.InitialReleaseDate">
      <CONNECT>
        <SOURCE
          NAME="VIEW_DECISION_MATRIX"
          PROPERTY="result$initialVersion$dtls$toDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.InitialCreationDate">
      <CONNECT>
        <SOURCE
          NAME="VIEW_DECISION_MATRIX"
          PROPERTY="result$initialVersion$dtls$dateCreated"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00226313 -->
  </CLUSTER>
  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.VersionDetails"
  >
    <FIELD LABEL="Field.Label.DateCreated">
      <CONNECT>
        <SOURCE
          NAME="VIEW_DECISION_MATRIX"
          PROPERTY="result$currentVersion$dtls$dateCreated"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.CreatedBy">
      <CONNECT>


        <SOURCE
          NAME="VIEW_DECISION_MATRIX"
          PROPERTY="currentVersion$createdUserFullName"
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
            NAME="VIEW_DECISION_MATRIX"
            PROPERTY="result$currentVersion$dtls$createdBy"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
      </LINK>
    </FIELD>
  </CLUSTER>
  <LIST TITLE="Cluster.Title.Outcomes">
    <CONDITION>
      <IS_TRUE
        NAME="VIEW_DECISION_MATRIX"
        PROPERTY="result$currentVersion$dtls$cumulativeScore"
      />
    </CONDITION>
    <!-- BEGIN, CR00214609, PS -->
    <ACTION_SET TYPE="LIST_ROW_MENU">
      <!-- END, CR00214609 -->
      <ACTION_CONTROL
        LABEL="ActionControl.Label.Edit"
        TYPE="ACTION"
      >
        <CONDITION>
          <IS_TRUE
            NAME="VIEW_DECISION_MATRIX"
            PROPERTY="result$inEditStatusInd"
          />
        </CONDITION>
        <!-- BEGIN, CR00198144, SS -->
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="DecisionAssistAdmin_modifyAssociateOutcomes"
          WINDOW_OPTIONS="width=625"
        >
          <!-- END, CR00198144 -->
          <CONNECT>
            <SOURCE
              NAME="VIEW_DECISION_MATRIX"
              PROPERTY="result$outcomeScoreList$dtls$outcomeScoreAssociationID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="outcomeScoreAssociationID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="VIEW_DECISION_MATRIX"
              PROPERTY="key$key$key$decisionMatrixVersionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="decisionMatrixVersionID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="VIEW_DECISION_MATRIX"
              PROPERTY="result$currentVersion$dtls$name"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="decisionMatrixName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="VIEW_DECISION_MATRIX"
              PROPERTY="result$currentVersion$dtls$decisionMatrixVersionNumber"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="versionNumber"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="VIEW_DECISION_MATRIX"
              PROPERTY="result$outcomeScoreList$dtls$outcomeID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="outcomeID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="VIEW_DECISION_MATRIX"
              PROPERTY="result$outcomeScoreList$dtls$outcomeName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="outcomeName"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL
        LABEL="ActionControl.Label.Delete"
        TYPE="ACTION"
      >
        <CONDITION>
          <IS_TRUE
            NAME="VIEW_DECISION_MATRIX"
            PROPERTY="result$inEditStatusInd"
          />
        </CONDITION>
        <!-- BEGIN, CR00198297, SS -->
        <!-- BEGIN, CR00198144, SS -->
        <!-- BEGIN, CR00187459, SS -->
        <!-- BEGIN, CR00237410, AK -->
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="DecisionAssistAdmin_deleteAssociateOutcomes"
          WINDOW_OPTIONS="width=500"
        >
          <!-- END, CR00237410 -->
          <!-- END, CR00187459 -->
          <!-- END, CR00198144 -->
          <!-- END, CR00198297 -->
          <CONNECT>
            <SOURCE
              NAME="VIEW_DECISION_MATRIX"
              PROPERTY="result$outcomeScoreList$dtls$outcomeScoreAssociationID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="outcomeScoreAssociationID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="VIEW_DECISION_MATRIX"
              PROPERTY="key$key$key$decisionMatrixVersionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="decisionMatrixVersionID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="VIEW_DECISION_MATRIX"
              PROPERTY="result$currentVersion$dtls$name"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="decisionMatrixName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="VIEW_DECISION_MATRIX"
              PROPERTY="result$currentVersion$dtls$decisionMatrixVersionNumber"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="versionNumber"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <!-- BEGIN, CR00214609, PS -->
    </ACTION_SET>
    <!-- END, CR00214609 -->
    <FIELD
      LABEL="Field.Label.Outcomes"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="VIEW_DECISION_MATRIX"
          PROPERTY="result$outcomeScoreList$dtls$outcomeName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.MinimumScore"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="VIEW_DECISION_MATRIX"
          PROPERTY="result$outcomeScoreList$dtls$minimumScore"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.MaximumScore"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="VIEW_DECISION_MATRIX"
          PROPERTY="result$outcomeScoreList$dtls$maximumScore"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Value"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="VIEW_DECISION_MATRIX"
          PROPERTY="result$outcomeScoreList$dtls$value"
        />
      </CONNECT>
    </FIELD>
  </LIST>
  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00417165, GK -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00417165 -->
      <CONNECT>
        <SOURCE
          NAME="VIEW_DECISION_MATRIX"
          PROPERTY="result$currentVersion$dtls$comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
