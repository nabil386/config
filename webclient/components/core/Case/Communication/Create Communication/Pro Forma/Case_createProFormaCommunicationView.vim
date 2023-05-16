<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2009, 2013. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2003-2004, 2010-2011 Curam Software Ltd.                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for the record communication details.                -->
<!-- BEGIN, CR00236672, NS -->
<?curam-deprecated Since Curam 6.0, replaced with Case_createProFormaCommunicationView1.vim. See release note: CR00236672?>
<!-- END, CR00236672 -->
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
    <CONNECT>
      <SOURCE
        NAME="PAGE"
        PROPERTY="pageDescription"
      />
    </CONNECT>
  </PAGE_TITLE>
  <SERVER_INTERFACE
    CLASS="IntegratedCase"
    NAME="DISPLAY"
    OPERATION="listCaseParticipantsDetails"
  />
  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="ACTION"
    OPERATION="createTemplateCommunication"
    PHASE="ACTION"
  />
  <ACTION_SET ALIGNMENT="CENTER">
    <ACTION_CONTROL
      IMAGE="SaveButton"
      LABEL="ActionControl.Label.Save"
      TYPE="SUBMIT"
    />
    <ACTION_CONTROL
      IMAGE="SaveAndNewButton"
      LABEL="ActionControl.Label.SaveAndNew"
      TYPE="SUBMIT"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Case_createProFormaCommunication"
        SAVE_LINK="false"
      />
    </ACTION_CONTROL>
    <ACTION_CONTROL
      IMAGE="CancelButton"
      LABEL="ActionControl.Label.Cancel"
    />
  </ACTION_SET>
  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>
  <PAGE_PARAMETER NAME="communicationType"/>
  <PAGE_PARAMETER NAME="proFormaID"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$caseID"
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
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="communicationType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="typeCode"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="proFormaID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="proFormaID"
    />
  </CONNECT>
  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.CommunicationDetails"
  >
    <FIELD LABEL="Field.Label.PrintNow">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="printInd"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.CorrespondentType">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="correspondentTypeCode"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER
    DESCRIPTION="Cluster.ParticipantContact.Description"
    LABEL_WIDTH="25"
    NUM_COLS="1"
    TITLE="Cluster.Title.Correspondent"
  >
    <FIELD
      LABEL="Field.Label.CaseParticipant"
      USE_BLANK="TRUE"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="participantRoleID"
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="participantRoleID"
        />
      </CONNECT>
    </FIELD>
    <CONTAINER LABEL="Field.Label.CorrespondentSearch">
      <FIELD WIDTH="25">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="participantType"
          />
        </CONNECT>
      </FIELD>
      <FIELD WIDTH="74">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="participantID"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>
    <FIELD CONTROL="SKIP"/>
  </CLUSTER>
  <CLUSTER
    DESCRIPTION="Cluster.CorrespondentContact.Description"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Label.CorrespondentName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="correspondentName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.EmailAddress">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="emailAddress"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER
    NUM_COLS="2"
    TAB_ORDER="ROW"
  >
    <FIELD>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="addressData"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER NUM_COLS="2">
    <FIELD
      LABEL="Field.Label.CountryCode"
      WIDTH="5"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="phoneCountryCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Number"
      WIDTH="10"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="phoneNumber"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.AreaCode"
      WIDTH="5"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="phoneAreaCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Extension"
      WIDTH="5"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="phoneExtension"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00406866, VT -->
    <FIELD HEIGHT="3" LABEL="Field.Label.Comments">
      <!-- END, CR00406866 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
