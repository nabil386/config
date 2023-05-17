<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2008, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of       -->
<!-- Curam Software. ("Confidential Information"). You                      -->
<!-- shall not disclose such Confidential Information and shall use it only -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- with Curam Software.                                                   -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page is the included view for enter appellant and respondent      -->
<!-- details for hearing reviews and judicial reviews                       -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText1"
      />
    </CONNECT>
  </PAGE_TITLE>
  <SERVER_INTERFACE
    CLASS="Appeal"
    NAME="DISPLAY"
    OPERATION="listCaseParticipantsForAppellantAndRespondent"
    PHASE="DISPLAY"
  />
  <PAGE_PARAMETER NAME="priorAppealCaseID"/>
  <PAGE_PARAMETER NAME="implCaseID"/>
  <PAGE_PARAMETER NAME="contextDescription"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="implCaseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="implCaseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="priorAppealCaseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="priorAppealCaseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="implCaseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="priorAppealCaseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="priorCaseID"
    />
  </CONNECT>
  <CLUSTER
    NUM_COLS="2"
    STYLE="outer-cluster-borderless"
  >
    <CLUSTER
      DESCRIPTION="Cluster.Appellant.Description"
      TITLE="Cluster.Title.Appellant"
    >
      <FIELD LABEL="Field.Label.Organization">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="appellantOrganizationInd"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.Appellant"
        USE_BLANK="true"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="appellantList$appealParticipantDetails$caseParticipantRoleID"
            NAME="DISPLAY"
            PROPERTY="appellantList$appealParticipantDetails$name"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="appellantCaseParticipantRoleID"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
    <CLUSTER
      DESCRIPTION="Cluster.Respondent.Description"
      TITLE="Cluster.Title.Respondent"
    >
      <FIELD LABEL="Field.Label.Organization">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="respondentOrganizationInd"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.Respondent"
        USE_BLANK="true"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="result$respondentList$appealParticipantDetails$caseParticipantRoleID"
            NAME="DISPLAY"
            PROPERTY="respondentList$appealParticipantDetails$name"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="respondentCaseParticipantRoleID"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
  </CLUSTER>
</VIEW>
