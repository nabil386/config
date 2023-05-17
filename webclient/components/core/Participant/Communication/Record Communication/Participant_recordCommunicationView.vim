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
    CLASS="Communication"
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


    <CLUSTER
      DESCRIPTION="Cluster.Description.AddressDetails"
      LABEL_WIDTH="25"
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


    </CLUSTER>


    <CLUSTER
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


    <CLUSTER
      DESCRIPTION="Cluster.Description.EmailDetails"
      NUM_COLS="2"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        LABEL="Field.Label.SelectEmailAddress"
        USE_BLANK="true"
      >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="emailAddressID"
            NAME="DISPEMAIL"
            PROPERTY="emailAddress"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="correspondentEmailID"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.NewEmailAddress">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="newEmailAddress"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


    <CLUSTER
      DESCRIPTION="Cluster.Description.PhoneDetails"
      NUM_COLS="2"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        LABEL="Field.Label.SelectPhoneNumber"
        USE_BLANK="true"
      >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="phoneNumberID"
            NAME="DISPPHONE"
            PROPERTY="phoneNumberString"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="phoneNumberID"
          />
        </CONNECT>
      </FIELD>


      <CONTAINER
        ALIGNMENT="LEFT"
        LABEL="Field.Label.NewPhoneNumber"
        WIDTH="100"
      >
        <!-- BEGIN, CR00463142, EC -->
        <FIELD
          LABEL="Field.Label.PhoneCountryCode"
          WIDTH="3"
          WIDTH_UNITS="CHARS"
        >
          <!-- END, CR00463142 -->
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="phoneCountryCode"
            />
          </CONNECT>
        </FIELD>
        <!-- BEGIN, CR00463142, EC -->
        <FIELD
          LABEL="Field.Label.PhoneAreaCode"
          WIDTH="3"
          WIDTH_UNITS="CHARS"
        >
          <!-- END, CR00463142 -->
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="phoneAreaCode"
            />
          </CONNECT>
        </FIELD>
        <!-- BEGIN, CR00463142, EC -->
        <FIELD
          LABEL="Field.Label.PhoneNumber1"
          WIDTH="6"
          WIDTH_UNITS="CHARS"
        >
          <!-- END, CR00463142 -->
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="phoneNumber"
            />
          </CONNECT>
        </FIELD>
        <!-- BEGIN, CR00463142, EC -->
        <FIELD
          LABEL="Field.Label.PhoneExtension"
          WIDTH="2"
          WIDTH_UNITS="CHARS"
        >
          <!-- END, CR00463142 -->
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="phoneExtension"
            />
          </CONNECT>
        </FIELD>


      </CONTAINER>


    </CLUSTER>
  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.CommunicationType"
  >
    <WIDGET
      LABEL="Field.Label.File"
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
