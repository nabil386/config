<?xml version="1.0" encoding="UTF-8"?>
<!--
    Licensed Materials - Property of IBM
    
    Copyright IBM Corporation 2014. All Rights Reserved.
    
    US Government Users Restricted Rights - Use, duplication or disclosure 
    restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to select the appellant and respondent for   -->
<!-- the appeal                                                             -->
<VIEW>


  <MENU MODE="WIZARD_PROGRESS_BAR">
    <CONNECT>
      <SOURCE
        NAME="DISPLAYWIZARD"
        PROPERTY="wizardMenu"
      />
    </CONNECT>
  </MENU>


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="wizardStateID"/>


  <SERVER_INTERFACE
    CLASS="AppealWizard"
    NAME="DISPLAY"
    OPERATION="getWizRecordOptions"
  />


  <SERVER_INTERFACE
    CLASS="AppealWizard"
    NAME="DISPLAYWIZARD"
    OPERATION="getWizRecordDetails"
  />


  <SERVER_INTERFACE
    CLASS="CaseAppealOnlineRequests"
    NAME="DISPLAY1"
    OPERATION="getOnlineAppealRequestsForDetAppealCreation"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    ACTION_ID_PROPERTY="actionIDProperty"
    CLASS="AppealWizard"
    NAME="ACTION"
    OPERATION="setWizRecordDetails"
    PHASE="ACTION"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="wizardStateID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="wizardStateID$wizardStateID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAYWIZARD"
      PROPERTY="wizardStateID$caseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="wizardStateID"
    />
    <TARGET
      NAME="DISPLAYWIZARD"
      PROPERTY="wizardStateID$wizardStateID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAYWIZARD"
      PROPERTY="stateID$wizardStateID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="wizardStateID$wizardStateID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="wizardStateID"
    />
    <TARGET
      NAME="DISPLAY1"
      PROPERTY="wizardStateID$wizardStateID"
    />
  </CONNECT>


  <CLUSTER TITLE="Cluster.Title.AppealedCaseDetails">
    <CLUSTER
      LABEL_WIDTH="50"
      NUM_COLS="2"
    >
      <FIELD LABEL="Field.Label.DateReceived">
        <CONNECT>
          <SOURCE
            NAME="DISPLAYWIZARD"
            PROPERTY="dateReceived"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="dateReceived"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.Reason">
        <CONNECT>
          <SOURCE
            NAME="DISPLAYWIZARD"
            PROPERTY="reasonCode"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="reasonCode"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.EffectiveDate">
        <CONNECT>
          <SOURCE
            NAME="DISPLAYWIZARD"
            PROPERTY="effectiveDate"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="effectiveDate"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.ReceiptNotice">
        <CONNECT>
          <SOURCE
            NAME="DISPLAYWIZARD"
            PROPERTY="receiptNoticeIndicator"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="receiptNoticeIndicator"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.ReceiptMethod">
        <CONNECT>
          <SOURCE
            NAME="DISPLAYWIZARD"
            PROPERTY="receiptMethod"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="receiptMethod"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.Emergency">
        <CONNECT>
          <SOURCE
            NAME="DISPLAYWIZARD"
            PROPERTY="emergencyCode"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="emergencyCode"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.Difficulty">
        <CONNECT>
          <SOURCE
            NAME="DISPLAYWIZARD"
            PROPERTY="difficultyTypeCode"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="difficultyTypeCode"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
    <CLUSTER
      LABEL_WIDTH="50"
      NUM_COLS="2"
    >
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="benefitsIndicator"
        />
      </CONDITION>
      <FIELD LABEL="Field.Label.ContinueBenefits">
        <CONNECT>
          <SOURCE
            NAME="DISPLAYWIZARD"
            PROPERTY="continueBenefitsIndicator"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="continueBenefitsIndicator"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
  </CLUSTER>


  <INCLUDE FILE_NAME="CaseOnlineAppealRequests_addView.vim"/>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAYWIZARD"
          PROPERTY="comments"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
