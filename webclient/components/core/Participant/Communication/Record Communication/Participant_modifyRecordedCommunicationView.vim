<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
 
  Copyright IBM Corporation 2008, 2013. All Rights Reserved.

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
<!-- View for modifying recorded communication details.                     -->
<!-- BEGIN, CR00237138, NS -->
<?curam-deprecated Since Curam 6.0, replaced with Participant_modifyRecordedCommunicationView1.vim. See release note: CR00237138?>
<!-- END, CR00237138 -->
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
    OPERATION="readRecordedCommunication"
  />


  <SERVER_INTERFACE
    CLASS="Communication"
    NAME="ACTION"
    OPERATION="modifyRecordedCommunication"
    PHASE="ACTION"
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
      PROPERTY="recordedCommKey$communicationID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="communicationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="recordedCommDetails$communicationID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="clientParticipantRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="clientParticipantRoleID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="communicationDate"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="communicationDate"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="communicationStatus"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="communicationStatus"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="statusCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="statusCode"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="correspondentParticipantRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="correspondentParticipantRoleID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="correspondentName"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="correspondentName"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="attachmentID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="attachmentID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="attachmentInd"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="attachmentInd"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="communicationFormat"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="communicationFormat"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseID"
    />
  </CONNECT>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >


    <FIELD LABEL="Field.Label.Direction">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="communicationDirection"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="communicationDirection"
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
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="methodTypeCode"
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


    <FIELD LABEL="Field.Label.CommunicationType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="communicationTypeCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="communicationTypeCode"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="25"
    TITLE="Cluster.Title.CommunicationText"
  >
    <FIELD LABEL="Field.Label.Subject">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="subject"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="subject"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      HEIGHT="5"
      LABEL="Field.Label.CommunicationText"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="communicationText"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="communicationText"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.CommunicationContactDetails"
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
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="addressLine1"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="addressID"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="addressID"
        />
      </CONNECT>
      <LINK>
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
      </LINK>
    </FIELD>


    <FIELD LABEL="Field.Label.EmailAddress">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="emailAddress"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="correspondentEmailID"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="correspondentEmailID"
        />
      </CONNECT>
      <LINK>
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
      </LINK>
    </FIELD>


    <FIELD LABEL="Field.Label.CorrespondentType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="correspondentType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="correspondentType"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.PhoneNumber">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="phoneNumberString"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="phoneNumberID"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="phoneNumberID"
        />
      </CONNECT>
      <LINK>
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
      </LINK>
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
      <CONNECT>
        <TARGET
          NAME="ACTION"
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
      <CONNECT>
        <TARGET
          NAME="ACTION"
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
      <CONNECT>
        <TARGET
          NAME="ACTION"
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
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="documentReference"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER NUM_COLS="2">


    <!-- BEGIN, CR00090361, MR -->
    <CONTAINER LABEL="Field.Label.File">
      <WIDGET TYPE="FILE_DOWNLOAD">
        <WIDGET_PARAMETER NAME="LINK_TEXT">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="newAttachmentName"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
        <WIDGET_PARAMETER NAME="PARAMS">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="attachmentID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="attachmentID"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
      </WIDGET>
    </CONTAINER>
    <!-- END, CR00090361  -->


    <WIDGET
      LABEL="Field.Label.ChangeFile"
      TYPE="FILE_UPLOAD"
    >
      <WIDGET_PARAMETER NAME="CONTENT">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="newAttachmentContents"
          />
        </CONNECT>
      </WIDGET_PARAMETER>
      <WIDGET_PARAMETER NAME="FILE_NAME">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="newAttachmentName"
          />
        </CONNECT>
      </WIDGET_PARAMETER>
    </WIDGET>
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >


    <!-- BEGIN, CR00406866, VT -->
    <FIELD HEIGHT="3" LABEL="Field.Label.Comments">
      <!-- END, CR00406866 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
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
