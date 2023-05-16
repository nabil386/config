<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010-2011 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Evidence infrastructure view page to be included in the client page    -->
<!-- for listing in edit and pending removal case evidence                  -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_PARAMETER NAME="applicationID"/>


  <SERVER_INTERFACE
    CLASS="ApplicationEvidence"
    NAME="DISPLAY"
    OPERATION="listAllInEdit"
  />
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="applicationID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="applicationID"
    />
  </CONNECT>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="Page.Title"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="ApplicationEvidence"
    NAME="GETPARTICIPANT"
    OPERATION="getPrimaryCaseParticipantDetails"
    PHASE="DISPLAY"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="applicationID"
    />
    <TARGET
      NAME="GETPARTICIPANT"
      PROPERTY="applicationID"
    />
  </CONNECT>


  <!-- Commenting out for CEF-016 as it does not work
  <SERVER_INTERFACE
    CLASS="Evidence"
    NAME="ACTION"
    OPERATION="getEvidenceTypeAndParticipantID"
    PHASE="ACTION"
  />
  
  <CLUSTER
    NUM_COLS="3"
    SHOW_LABELS="false"
    STYLE="cluster-cpr-no-border"
    >
    <CLUSTER
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
      >
      <FIELD
        LABEL="Field.Label.Participant"
        USE_BLANK="false"
        USE_DEFAULT="false"
        >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="participantIDNameDetailsList$dtls$participantID"
            NAME="DISPLAY"
            PROPERTY="participantIDNameDetailsList$dtls$participantName"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$filteredParticipantID"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="key$participantID"
          />
        </CONNECT>
        <SCRIPT
          ACTION="enactHiddenActionControl_onChange()"
          EVENT="ONCHANGE"
          SCRIPT_FILE="evidenceTypeWorkspace.js"
        />
      </FIELD>
    </CLUSTER>
    <CONTAINER
      ALIGNMENT="LEFT"
      STYLE="inner-span-action-control-visibility-hidden"
      >
      <ACTION_CONTROL
        IMAGE="ListButton"
        LABEL="ActionControl.Label.List"
        TYPE="SUBMIT"
        >
        <LINK PAGE_ID="this"/>
      </ACTION_CONTROL>
    </CONTAINER>
  </CLUSTER>
  -->
  <LIST PAGINATED="FALSE">


    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="hasVerificationsOrIssues"
      />
    </CONDITION>


    <FIELD WIDTH="5">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="verificationOrIssuesExist"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.EvidenceType"
      WIDTH="17"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="evidenceParticipantDtlsList$dtls$evidenceType"
        />
      </CONNECT>


      <LINK PAGE_ID="Evidence_workspaceTypeList">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="evidenceParticipantDtlsList$dtls$evidenceType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceType"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>


    </FIELD>
    <FIELD
      LABEL="List.Title.Name"
      WIDTH="17"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="evidenceParticipantDtlsList$dtls$participantName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.Description"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="evidenceParticipantDtlsList$dtls$summary"
        />
      </CONNECT>
      <LINK PAGE_ID="Evidence_resolveObjectFromMetaData">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="evidenceParticipantDtlsList$dtls$successionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="successionID"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <FIELD
      LABEL="List.Title.Period"
      WIDTH="18"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="period"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.LatestActivity"
      WIDTH="23"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="evidenceParticipantDtlsList$dtls$latestActivity"
        />
      </CONNECT>
    </FIELD>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Evidence_listInEditEvdInstanceChanges">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="evidenceParticipantDtlsList$dtls$successionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="successionID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>
  </LIST>


  <LIST PAGINATED="FALSE">


    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY"
        PROPERTY="hasVerificationsOrIssues"
      />
    </CONDITION>


    <FIELD
      LABEL="List.Title.EvidenceType"
      WIDTH="17"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="evidenceParticipantDtlsList$dtls$evidenceType"
        />
      </CONNECT>


      <LINK PAGE_ID="Evidence_workspaceTypeList">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="evidenceParticipantDtlsList$dtls$evidenceType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceType"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>


    </FIELD>
    <FIELD
      LABEL="List.Title.Name"
      WIDTH="17"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="evidenceParticipantDtlsList$dtls$participantName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.Description"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="evidenceParticipantDtlsList$dtls$summary"
        />
      </CONNECT>
      <LINK PAGE_ID="Evidence_resolveObjectFromMetaData">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="evidenceParticipantDtlsList$dtls$successionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="successionID"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <FIELD
      LABEL="List.Title.Period"
      WIDTH="18"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="period"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.LatestActivity"
      WIDTH="23"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="evidenceParticipantDtlsList$dtls$latestActivity"
        />
      </CONNECT>
    </FIELD>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Evidence_listInEditEvdInstanceChanges">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="evidenceParticipantDtlsList$dtls$successionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="successionID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>
  </LIST>
</VIEW>
