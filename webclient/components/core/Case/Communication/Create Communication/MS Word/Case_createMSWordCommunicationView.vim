<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012, 2013. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2004-2008, 2010 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for creating a new email communication.              -->
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
    CLASS="Communication"
    NAME="ACTION"
    OPERATION="createCaseMSWordCommunication1"
    PHASE="ACTION"
  />


  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="DISPADDRESS"
    OPERATION="listAddressString"
  />


  <SERVER_INTERFACE
    CLASS="System"
    NAME="DISPLAYTEM"
    OPERATION="listDocumentTemplatesByRelatedID"
  />


  <ACTION_SET ALIGNMENT="CENTER">
    <ACTION_CONTROL
      IMAGE="SaveButton"
      LABEL="ActionControl.SaveButton.label"
      TYPE="SUBMIT"
    >
      <LINK
        PAGE_ID="Case_controlMSWord"
        SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="result$dtls$communicationID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="communicationID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="details$dtls$templateID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="templateID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      IMAGE="CancelButton"
      LABEL="ActionControl.CancelButton.label"
    />
  </ACTION_SET>


  <PAGE_PARAMETER NAME="pageDescription"/>
  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="correspondentParticipantRoleID"/>
  <PAGE_PARAMETER NAME="correspondentName"/>
  <PAGE_PARAMETER NAME="correspondentParticipantRoleType"/>
  <PAGE_PARAMETER NAME="caseParticipantRoleID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="correspondentParticipantRoleID"
    />
    <TARGET
      NAME="DISPADDRESS"
      PROPERTY="concernRoleID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAYTEM"
      PROPERTY="caseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="correspondentParticipantRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="correspondentParticipantRoleID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseParticipantRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseParticipantRoleID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="correspondentName"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="correspondentName"
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


  <CLUSTER>


    <CLUSTER
      LABEL_WIDTH="30"
      NUM_COLS="1"
      STYLE="cluster-cpr-no-border"
    >
      <FIELD LABEL="Field.Label.Subject">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="subjectText"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.CorrespondentName">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="correspondentName"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.CorrespondentType"
        WIDTH="50"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="correspondentTypeCode"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


    <CLUSTER
      DESCRIPTION="Cluster.Description.AddressDetails"
      LABEL_WIDTH="30"
      NUM_COLS="1"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        LABEL="Field.Label.Address"
        USE_BLANK="true"
      >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="addressID"
            NAME="DISPADDRESS"
            PROPERTY="addressString"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="addressID"
          />
        </CONNECT>
      </FIELD>


      <CLUSTER
        LABEL_WIDTH="60"
        NUM_COLS="2"
        SHOW_LABELS="true"
        STYLE="cluster-cpr-no-border"
        TAB_ORDER="ROW"
      >
        <FIELD LABEL="Field.Label.Address">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="addressData"
            />
          </CONNECT>
        </FIELD>
      </CLUSTER>
    </CLUSTER>
  </CLUSTER>


  <CLUSTER
    DESCRIPTION="Cluster.Template.Description"
    LABEL_WIDTH="30"
  >
    <FIELD
      LABEL="Field.Label.TemplateName"
      WIDTH="50"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="documentTemplateID"
          NAME="DISPLAYTEM"
          PROPERTY="name"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$dtls$templateID"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER
    LABEL_WIDTH="60"
    NUM_COLS="2"
    TITLE="Cluster.Title.AssociatedFiles"
  >


    <FIELD LABEL="Field.Label.FileLocation">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$fileLocation"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DocumentLocation">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$documentLocation"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.FileReference">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$fileReferenceNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DocumentReference">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$documentRefNumber"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
