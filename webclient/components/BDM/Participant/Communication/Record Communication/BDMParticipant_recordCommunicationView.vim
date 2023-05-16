<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2004, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2004-2011 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for the record communication details.                -->
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


  <!--BEGIN, CR00237168, PB-->
  <SERVER_INTERFACE
    CLASS="BDMCommunication"
    NAME="ACTION"
    OPERATION="createRecordedComm1"
    PHASE="ACTION"
  />
  <!--END, CR00237168-->


  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="DISPADDRESS"
    OPERATION="listAddressString"
  />


  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="DISPEMAIL"
    OPERATION="listEmailAddress"
  />


  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="DISPPHONE"
    OPERATION="listPhoneNumber"
  />


  <!--BEGIN, CR00441479, SP-->
  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="DISPEXCEPTION"
    OPERATION="listExceptions"
  />
  <!--END, CR00441479-->


  <PAGE_PARAMETER NAME="pageDescription"/>
  <PAGE_PARAMETER NAME="concernRoleID"/>
  <PAGE_PARAMETER NAME="correspondentParticipantRoleID"/>
  <PAGE_PARAMETER NAME="correspondentConcernRoleID"/>
  <PAGE_PARAMETER NAME="correspondentParticipantRoleType"/>
  <PAGE_PARAMETER NAME="correspondentName"/>
  <PAGE_PARAMETER NAME="caseID"/>
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
      PROPERTY="correspondentParticipantRoleID"
    />
    <TARGET
      NAME="DISPEMAIL"
      PROPERTY="concernRoleID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="correspondentParticipantRoleID"
    />
    <TARGET
      NAME="DISPPHONE"
      PROPERTY="concernRoleID"
    />
  </CONNECT>
  <!--BEGIN, CR00441479, SP-->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="correspondentParticipantRoleID"
    />
    <TARGET
      NAME="DISPEXCEPTION"
      PROPERTY="correspondentConcernRoleID"
    />
  </CONNECT>
  <!--END, CR00441479-->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="clientParticipantRoleID"
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
      PROPERTY="correspondentConcernRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="correspondentConcernRoleID"
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


  <!--BEGIN, CR00441479, SP-->
  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="DISPEXCEPTION"
        PROPERTY="informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>
  <!--END, CR00441479-->


  <CLUSTER LABEL_WIDTH="25">
    <FIELD LABEL="Field.Label.Subject">
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
        <TARGET
          NAME="ACTION"
          PROPERTY="communicationText"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER NUM_COLS="2">


    <FIELD LABEL="Field.Label.Direction">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="communicationDirection"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Method">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="methodTypeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DateReceived">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="communicationDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.CommunicationType"
      USE_BLANK="true"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="communicationTypeCode"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER NUM_COLS="1">


    <CLUSTER
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD LABEL="Field.Label.CorrespondentName">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="correspondentName"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.CorrespondentType">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="correspondentType"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>

  </CLUSTER>


  <CLUSTER NUM_COLS="2" TITLE="Cluster.Title.CommunicationType">
  
  <!-- TASK 91944 93726 Remove File upload widget -->
  <FIELD CONTROL="SKIP"/>

  <FIELD LABEL="Field.Label.FileLocation">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fileLocation"
        />
      </CONNECT>
  </FIELD>


    <FIELD LABEL="Field.Label.DocumentLocation">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="documentLocation"
        />
      </CONNECT>
    </FIELD>


    <FIELD CONTROL="SKIP"/>


    <FIELD LABEL="Field.Label.FileReference">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fileReference"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DocumentReference">
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
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
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
