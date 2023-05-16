<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
 
  Copyright IBM Corporation 2004, 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2004, 2009-2011 Curam Software Ltd.                          -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The page to modify the details of a draft email communication.         -->
<!-- BEGIN, CR00237256, NS -->
<?curam-deprecated Since Curam 6.0, replaced with Participant_modifyDraftEmailCommunicationForConcernOnly1.vim. See release note: CR00237256?>
<!-- END, CR00237256 -->
<VIEW
  PAGE_ID="Participant_modifyDraftEmailCommunicationForConcernOnly"
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
    OPERATION="readEmail"
  />


  <SERVER_INTERFACE
    CLASS="Communication"
    NAME="ACTION"
    OPERATION="modifyEmail"
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
      PROPERTY="key$communicationID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="communicationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$communicationID"
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
      PROPERTY="methodTypeCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="methodTypeCode"
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
      HEIGHT="3"
      LABEL="Field.Label.CommunicationText"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="emailText"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="emailText"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Correspondent"
  >


    <FIELD
      EDITABLE="NONEDITABLE"
      LABEL="Field.Label.CorrespondentName"
    >
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
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="correspondentType"
        />
      </CONNECT>
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
      <!-- BEGIN, CR00068110,GSP -->


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
      <!-- END CR00068110 -->
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
    NUM_COLS="2"
    TITLE="Cluster.Title.AssociatedFiles"
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


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >


    <!-- BEGIN, CR00406866, VT -->
    <FIELD
      HEIGHT="3"
      LABEL="Field.Label.Comments"
    >
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
