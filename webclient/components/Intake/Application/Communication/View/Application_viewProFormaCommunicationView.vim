<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010-2011 Curam Software Ltd.                          -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Views pro forma communication details.                                 -->
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
    NAME="DISPLAY"
    OPERATION="readProFormaAndCaseMember"
  />


  <PAGE_PARAMETER NAME="communicationID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>
  <PAGE_PARAMETER NAME="correspondentConcernRoleID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="communicationID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="proFormaCommKey$communicationID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="38"
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >


    <FIELD LABEL="Field.Label.RegardingClient">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="clientConcernRoleName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Method">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="methodTypeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD CONTROL="SKIP"/>


    <CONTAINER
      LABEL="Field.Label.CommunicationStatus"
      STYLE="action-link-button"
    >


      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="communicationStatus"
          />
        </CONNECT>
      </FIELD>


      <ACTION_CONTROL LABEL="ActionControl.PrintButton.label">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Participant_confirmPrintForProFormaCommunication"
          SAVE_LINK="true"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="readProFormaCommDetails$communicationID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="communicationID"
            />
          </CONNECT>


          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="localeIdentifier"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="localeIdentifier"
            />
          </CONNECT>
        </LINK>


      </ACTION_CONTROL>
    </CONTAINER>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="38"
    NUM_COLS="2"
    TITLE="Cluster.Title.Correspondent"
  >


    <FIELD LABEL="Field.Label.CorrespondentName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="correspondentName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Address">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="formattedAddressData"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.CorrespondentType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="correspondentType"
        />
      </CONNECT>
    </FIELD>


    <FIELD CONTROL="SKIP"/>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="38"
    NUM_COLS="2"
    TITLE="Cluster.Title.CommunicationType"
  >


    <FIELD LABEL="Field.Label.FileLocation">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fileLocation"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DocumentLocation">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="documentLocation"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.FileReference">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fileReference"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DocumentReference">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="documentReference"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <FIELD HEIGHT="4">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>
</VIEW>
