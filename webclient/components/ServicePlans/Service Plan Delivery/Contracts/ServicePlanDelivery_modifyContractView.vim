<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2005, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2005, 2008, 2010 Curam Software Ltd.                         -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of       -->
<!-- Curam Software, Ltd. ("Confidential Information"). You                 -->
<!-- shall not disclose such Confidential Information and shall use it only -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- with Curam Software.                                                   -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This process allows the user to modify service plan contract           -->
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
    CLASS="ServicePlanDelivery"
    NAME="DISPLAY"
    OPERATION="viewContractHome"
  />


  <!--BEGIN, CR00123092, MC -->


  <SERVER_INTERFACE
    CLASS="ServicePlanDelivery"
    NAME="DISPLAY_PLANNED_PARTICIPANTS"
    OPERATION="listPlanParticipantsForContract"
    PHASE="DISPLAY"
  />
  <!--END, CR00123092 -->


  <SERVER_INTERFACE
    CLASS="ServicePlanDelivery"
    NAME="ACTION"
    OPERATION="modifyContract"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="servicePlanContractID"/>
  <PAGE_PARAMETER NAME="description"/>
  <!--BEGIN, CR00123092, MC -->
  <PAGE_PARAMETER NAME="caseID"/>
  <!--END, CR00123092 -->


  <!--BEGIN, CR00124499, MC -->
  <PAGE_PARAMETER NAME="issuedToParticipantRole"/>
  <!--END, CR00124499 -->


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="servicePlanContractID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="servPlanContractKey$servicePlanContractID"
    />
  </CONNECT>


  <!--BEGIN, CR00123092, MC -->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY_PLANNED_PARTICIPANTS"
      PROPERTY="caseID"
    />
  </CONNECT>
  <!--END, CR00123092 -->


  <!--BEGIN, CR00124499, MC -->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="issuedToParticipantRole"
    />
    <TARGET
      NAME="DISPLAY_PLANNED_PARTICIPANTS"
      PROPERTY="issueToID"
    />
  </CONNECT>
  <!--END, CR00124499 -->


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
      NAME="DISPLAY"
      PROPERTY="creationDate"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="creationDate"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="issuedToParticipantRole"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="issuedToParticipantRole"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="status"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="status"
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
      PROPERTY="attachedFileInd"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="attachedFileInd"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="attachmentDetails$attachmentID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="attachmentDetails$attachmentID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="contractDetails$dtls$servicePlanContractID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="servicePlanContractID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="sensitivityCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="sensitivityCode"
    />
  </CONNECT>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >
    <FIELD LABEL="Field.Label.IssueTo">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="clientName"
        />
      </CONNECT>


    </FIELD>


    <FIELD LABEL="Field.Label.Reason">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="issueReason"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="issueReason"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Address" WIDTH="12" WIDTH_UNITS="CHARS">
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
            PROPERTY="issuedToParticipantRole"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD LABEL="Field.Label.IssueDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateIssued"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dateIssued"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>
  <!-- BEGIN, CR00123092, MC -->
  <!-- List of Planned participants for the selected service plan -->
  <LIST TITLE="List.Title.SignedBy">
    <!-- BEGIN, CR00125260, ANK -->
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY_PLANNED_PARTICIPANTS"
        PROPERTY="havingMultiplePlanParticipants"
      />
    </CONDITION>
    <!-- END, CR00125260 -->
    <CONTAINER
      ALIGNMENT="CENTER"
      WIDTH="10"
    >
      <WIDGET TYPE="MULTISELECT">
        <WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY_PLANNED_PARTICIPANTS"
              PROPERTY="participantRoleID"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
        <WIDGET_PARAMETER NAME="MULTI_SELECT_INITIAL">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="signedBy"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
        <WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="signedBy"
            />
          </CONNECT>
        </WIDGET_PARAMETER>


      </WIDGET>
    </CONTAINER>
    <FIELD LABEL="Field.Label.PlannedParticipantName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_PLANNED_PARTICIPANTS"
          PROPERTY="fullName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Type">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_PLANNED_PARTICIPANTS"
          PROPERTY="typeCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.FromDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_PLANNED_PARTICIPANTS"
          PROPERTY="fromDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ToDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_PLANNED_PARTICIPANTS"
          PROPERTY="toDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_PLANNED_PARTICIPANTS"
          PROPERTY="recordStatus"
        />
      </CONNECT>
    </FIELD>
  </LIST>
  <!-- END, CR00123092-->


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TITLE="Cluster.Title.Acceptance"
  >
    <FIELD LABEL="Field.Label.DateAccepted">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateAccepted"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dateAccepted"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Label.Location">
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
    <FIELD LABEL="Field.Label.Reference">
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
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
  >

    <CONTAINER LABEL="Field.Label.AttachedFile">
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="attachmentName"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>

    <WIDGET
      LABEL="Field.Label.NewFile"
      TYPE="FILE_UPLOAD"
      WIDTH="18"
      WIDTH_UNITS="CHARS"
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
  </CLUSTER>

  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Rejection"
  >
    <FIELD LABEL="Field.Label.DateRejected">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateRejected"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dateRejected"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.RejectionReason"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rejectReason"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="rejectReason"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER LABEL_WIDTH="25">
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.RejectionComments"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rejectionComments"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="rejectionComments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Cancellation"
  >
    <FIELD LABEL="Field.Label.DateCancelled">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateCanceled"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dateCanceled"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.CancellationReason"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="cancelReason"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="cancelReason"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER LABEL_WIDTH="25">
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.CancellationComments"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="cancelComments"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="cancelComments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00406866, VT -->
    <FIELD HEIGHT="4" LABEL="Field.Label.Comments">
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
