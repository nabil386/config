<?xml version="1.0" encoding="UTF-8"?>
<!--
    Licensed Materials - Property of IBM
    
    PID 5725-H26
    
    Copyright IBM Corporation 2011, 2014. All Rights Reserved.
    
    US Government Users Restricted Rights - Use, duplication or disclosure 
    restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2011 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to select the appellant and respondent for   -->
<!-- the appeal                                                             -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="Appeal"
    NAME="DISPLAY"
    OPERATION="readForCreateNewAppeal"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="Appeal"
    NAME="ACTION"
    OPERATION="createAppeal"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="appealObjectsDelimitedList"/>


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
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="implCaseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="appealObjectsDelimitedList"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="appealObjectsDelimitedList"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="dtls$priorAppealID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="appAndResDetails$priorAppealCaseID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="dtls$appealType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="appealTypeCode"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="dtls$appealType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="appealType"
    />
  </CONNECT>


  <ACTION_SET ALIGNMENT="CENTER">
    <ACTION_CONTROL
      IMAGE="SaveButton"
      LABEL="ActionControl.Label.Save"
      TYPE="SUBMIT"
    >
      <LINK PAGE_ID="Appeal_resolveHearingCaseHome">
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="appealCaseKey$caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="appealType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="appealTypeCode"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      IMAGE="CancelButton"
      LABEL="ActionControl.Label.Cancel"
    />
  </ACTION_SET>


  <!-- BEGIN, CR00369210, PS -->
  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
  >
    <!-- END, CR00369210 -->


    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY"
        PROPERTY="anyIndicator"
      />
    </CONDITION>
    <FIELD LABEL="Field.Label.AppealType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="appealStageNumber"
        />
      </CONNECT>


    </FIELD>
  </CLUSTER>


  <!-- BEGIN, CR00369210, PS -->
  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
  >
    <!-- END, CR00369210 -->


    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="anyIndicator"
      />
    </CONDITION>
    <FIELD
      LABEL="Field.Label.AppealType"
      WIDTH="100"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="appealType"
        />
      </CONNECT>


    </FIELD>
  </CLUSTER>


  <!-- BEGIN, CR00369210, PS -->
  <CLUSTER
    DESCRIPTION="Cluster.Appellant.Description"
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TITLE="Cluster.Title.Appellant"
  >
    <!-- END, CR00369210 -->


    <FIELD
      LABEL="Field.Label.Appellant"
      USE_BLANK="TRUE"
      USE_DEFAULT="TRUE"
      WIDTH="100"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="appellantList$appealParticipantDetails$caseParticipantRoleID"
          NAME="DISPLAY"
          PROPERTY="appellantList$appealParticipantDetails$nameAndAgeOpt"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="appellantCaseParticipantRoleID"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Organization">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="appellantOrganizationInd"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <!-- BEGIN, CR00369210, PS -->
  <CLUSTER
    DESCRIPTION="Cluster.Respondent.Description"
    LABEL_WIDTH="50"
    NUM_COLS="2"
  >
    <!-- END, CR00369210 -->


    <FIELD
      LABEL="Field.Label.Respondent"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="100"
    >


      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="respondentList$appealParticipantDetails$caseParticipantRoleID"
          NAME="DISPLAY"
          PROPERTY="respondentList$appealParticipantDetails$nameAndAgeOpt"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="respondentCaseParticipantRoleID"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Organization">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="respondentOrganizationInd"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <!-- BEGIN, CR00369210, PS -->
  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TITLE="Cluster.Title.AppealedCaseDetails"
  >
    <!-- END, CR00369210 -->


    <FIELD LABEL="Field.Label.DateReceived">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dateReceived"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Reason">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="reasonCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EffectiveDate">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="effectiveDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ReceiptNotice">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="receiptNoticeIndicator"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ReceiptMethod">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="receiptMethod"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Emergency">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="emergencyCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Difficulty">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="difficultyTypeCode"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <!-- BEGIN, CR00369210, PS -->
  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
  >
    <!-- END, CR00369210 -->


    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="benefitsIndicator"
      />
    </CONDITION>


    <FIELD LABEL="Field.Label.ContinueBenefits">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="continueBenefitsIndicator"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00407812, RB -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00407812 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
