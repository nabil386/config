<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2011, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2011 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- View for modifying recorded communication details.                     -->
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
    OPERATION="readRecordedCommunication1"
  />


  <SERVER_INTERFACE
    CLASS="Communication"
    NAME="ACTION"
    OPERATION="modifyRecordedCommunication1"
    PHASE="ACTION"
  />


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


  <!--BEGIN, CR00442055, SP-->
  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="DISPEXCEPTION"
    OPERATION="listExceptions"
  />
  <!--END, CR00442055-->


  <PAGE_PARAMETER NAME="communicationID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>
  <PAGE_PARAMETER NAME="concernRoleID"/>
  <PAGE_PARAMETER NAME="correspondentConcernRoleID"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="correspondentConcernRoleID"
    />
    <TARGET
      NAME="DISPADDRESS"
      PROPERTY="concernRoleID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="correspondentConcernRoleID"
    />
    <TARGET
      NAME="DISPEMAIL"
      PROPERTY="concernRoleID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="correspondentConcernRoleID"
    />
    <TARGET
      NAME="DISPPHONE"
      PROPERTY="concernRoleID"
    />
  </CONNECT>


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


  <!--BEGIN, CR00442055, SP-->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="correspondentConcernRoleID"
    />
    <TARGET
      NAME="DISPEXCEPTION"
      PROPERTY="correspondentID$correspondentConcernRoleID"
    />
  </CONNECT>
  <!--END, CR00442055-->


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


  <!--BEGIN, CR00442055, SP-->
  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="DISPEXCEPTION"
        PROPERTY="informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>
  <!--END, CR00442055-->


  <CLUSTER LABEL_WIDTH="25">
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


  <CLUSTER NUM_COLS="2">


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


  <CLUSTER NUM_COLS="1">


    <CLUSTER
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD LABEL="Field.Label.CorrespondentName">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="correspondentName"
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
      </FIELD>


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
          LABEL="Field.Label.PhoneNumber"
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
    TITLE="Cluster.Title.AssociatedFiles"
  >


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
      HEIGHT="4"
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
