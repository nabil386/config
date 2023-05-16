<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2008, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008, 2011 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Views pro forma communication details.               		            -->
<!-- BEGIN, CR00236672, NS -->
<?curam-deprecated Since Curam 6.0, replaced with Participant_viewProFormaCommunicationView1.vim. See release note: CR00236672?>
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
    CLASS="Communication"
    NAME="DISPLAY"
    OPERATION="readProForma"
  />


  <PAGE_PARAMETER NAME="communicationID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


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
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >


    <FIELD LABEL="Field.Label.Subject">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="subject"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.DateReceived">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="communicationDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="statusCode"
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
    <FIELD LABEL="Field.Label.CommunicationType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="communicationTypeCode"
        />
      </CONNECT>
    </FIELD>


    <CONTAINER LABEL="Field.Label.CommunicationStatus">
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="communicationStatus"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>


  </CLUSTER>


  <CLUSTER
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
      <LINK
        OPEN_NEW="TRUE"
        PAGE_ID="Participant_resolveHome"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseParticipantRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseParticipantRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="correspondentParticipantRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="correspondentConcernRoleType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="participantType"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD LABEL="Field.Label.CorrespondentType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="correspondentType"
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


  </CLUSTER>


  <CLUSTER
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
    <!-- BEGIN, CR00416277, VT -->
    <FIELD LABEL="Field.Label.Comments">
      <!-- END, CR00416277 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>
</VIEW>
