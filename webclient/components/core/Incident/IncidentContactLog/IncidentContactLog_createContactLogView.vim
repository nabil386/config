<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008-2009 Curam Software Ltd.                                   -->
<!-- All rights reserved.                                                      -->
<!-- This software is the confidential and proprietary information of Curam    -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose       -->
<!-- such Confidential Information and shall use it only in accordance with    -->
<!-- the terms of the license agreement you entered into with Curam            -->
<!-- Software.                                                                 -->
<!-- Description                                                               -->
<!-- ===========                                                               -->
<!-- This page allows the user to create a contact log for an Incident         -->
<?curam-deprecated Since Curam 6.0, replaced by IncidentContactLog_createContactLogView1.vim?>
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.Title"
      />
    </CONNECT>


  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="Incidents"
    NAME="ACTION"
    OPERATION="createIncidentContactLog"
    PHASE="ACTION"
  />


  <!-- BEGIN, CR00140531, CL -->
  <SERVER_INTERFACE
    CLASS="ContactLog"
    NAME="DISPLAYPURPOSE"
    OPERATION="listPurpose"
    PHASE="DISPLAY"
  />
  <!-- END, CR00140531 -->


  <PAGE_PARAMETER NAME="incidentID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="incidentID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="linkID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="2"
    TITLE="Cluster.Label.Details"
  >


    <!-- BEGIN, CR00140531, CL -->
    <FIELD
      HEIGHT="3"
      LABEL="Field.Label.Purpose"
      USE_BLANK="true"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="purposeCode"
          NAME="DISPLAYPURPOSE"
          PROPERTY="purposeName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="purpose"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00143927, SK  -->
    <FIELD
      LABEL="Field.Label.Location"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="location"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00143927  -->
    <!-- END, CR00140531 -->


    <FIELD LABEL="Field.Label.StartDate">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDateTime"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00143927, SK  -->
    <FIELD
      LABEL="Field.Label.Type"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="contactLogType"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00143927  -->


    <!--BEGIN, CR00140531, CL-->
    <FIELD CONTROL="SKIP">
    </FIELD>


    <FIELD LABEL="Field.Label.LocationDescription">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="locationDescription"
        />
      </CONNECT>
    </FIELD>


    <!--END, CR00140531 -->
    <FIELD LABEL="Field.Label.EndDate">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDateTime"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00143927, SK  -->
    <FIELD
      LABEL="Field.Label.Method"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="method"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00143927  -->


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="20"
    NUM_COLS="1"
    SHOW_LABELS="true"
    TITLE="Cluster.Label.ContactDetails"
  >


    <CLUSTER
      DESCRIPTION="Cluster.Description.RegisteredParticipant"
      LABEL_WIDTH="20"
      NUM_COLS="1"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <CONTAINER LABEL="Field.Label.Participant">
        <FIELD WIDTH="25">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="concernRoleType"
            />
          </CONNECT>
        </FIELD>
        <FIELD WIDTH="30">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="attendeeDetails$concernRoleID"
            />
          </CONNECT>
        </FIELD>


      </CONTAINER>
      <FIELD CONTROL="SKIP"/>
    </CLUSTER>


    <CLUSTER
      DESCRIPTION="Cluster.Description.UnRegisteredParticipant"
      LABEL_WIDTH="40"
      NUM_COLS="2"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD LABEL="Field.Label.ParticipantName">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="attendeeDetails$concernRoleName"
          />
        </CONNECT>
      </FIELD>


      <FIELD CONTROL="SKIP"/>


    </CLUSTER>


    <CLUSTER
      DESCRIPTION="Cluster.Description.User"
      LABEL_WIDTH="40"
      NUM_COLS="2"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        HEIGHT="1"
        LABEL="Field.Label.User"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="attendeeDetails$userName"
          />
        </CONNECT>
      </FIELD>


      <FIELD CONTROL="SKIP"/>


    </CLUSTER>
  </CLUSTER>


  <!-- BEGIN, CR00129475, RPB -->
  <CLUSTER
    LABEL_WIDTH="15"
    NUM_COLS="1"
    TITLE="Cluster.Label.NarrativeDetails"
  >
    <FIELD
      HEIGHT="5"
      LABEL="Field.Label.NarrativeDetails"
      USE_DEFAULT="true"
    >
      <!-- END, CR00129475 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="notesText"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Label.AttachmentDetails"
  >


    <WIDGET
      LABEL="Field.Label.File"
      TYPE="FILE_UPLOAD"
    >
      <WIDGET_PARAMETER NAME="CONTENT">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="attachmentContents"
          />
        </CONNECT>
      </WIDGET_PARAMETER>


      <WIDGET_PARAMETER NAME="FILE_NAME">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="attachmentName"
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
    <FIELD LABEL="Field.Label.DocumentType">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="documentType"
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
    <FIELD LABEL="Field.Label.ReceiptDate">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="receiptDate"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="15"
    NUM_COLS="1"
    TITLE="Cluster.Label.AttachmentDescriptionDetails"
  >


    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.AttachmentDescription"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="attachmentDetails$attachmentLinkDtls$description"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
