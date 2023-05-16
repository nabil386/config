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
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="BDMAppealWizard"
    NAME="DISPLAYWIZARD"
    OPERATION="getWizParticipantDetails"
  />


  <SERVER_INTERFACE
    ACTION_ID_PROPERTY="actionIDProperty"
    CLASS="BDMAppealWizard"
    NAME="ACTION"
    OPERATION="setWizParticipantDetails"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="caseID"/>


  <PAGE_PARAMETER NAME="wizardStateID"/>


  <PAGE_PARAMETER NAME="appealObjectsDelimitedList"/>


  <MENU MODE="WIZARD_PROGRESS_BAR">
    <CONNECT>
      <SOURCE
        NAME="DISPLAYWIZARD"
        PROPERTY="wizardMenu"
      />
    </CONNECT>
  </MENU>


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
      NAME="DISPLAY"
      PROPERTY="appealType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="appealType"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="appealObjectsDelimitedList"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$participantDetails$appealObjectsDelimitedList"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
  >
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


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
  >
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


  <CLUSTER TITLE="Cluster.Title.AppellantRespondent">
    <CLUSTER
      DESCRIPTION="Cluster.Appellant.Description"
      LABEL_WIDTH="50"
      NUM_COLS="2"
    >
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
          <SOURCE
            NAME="DISPLAYWIZARD"
            PROPERTY="appellantCaseParticipantRoleID"
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
          <SOURCE
            NAME="DISPLAYWIZARD"
            PROPERTY="appellantOrganizationInd"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="appellantOrganizationInd"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
    <CLUSTER
      DESCRIPTION="Cluster.Respondent.Description"
      LABEL_WIDTH="50"
      NUM_COLS="2"
    >
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
          <SOURCE
            NAME="DISPLAYWIZARD"
            PROPERTY="respondentCaseParticipantRoleID"
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
          <SOURCE
            NAME="DISPLAYWIZARD"
            PROPERTY="respondentOrganizationInd"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="respondentOrganizationInd"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
  </CLUSTER>


</VIEW>
