<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright 2010-2011 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Infrastructure page containing common fields when capturing custom     -->
<!-- evidence                                                               -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

  
  <ACTION_CONTROL LABEL="ActionControl.Label.NewEvidence">
    <LINK
      OPEN_MODAL="true"
      PAGE_ID="Evidence_addNewEvidence"
    >
      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="caseID"
        />
        <TARGET
          NAME="PAGE"
          PROPERTY="caseID"
        />
      </CONNECT>
    </LINK>
  </ACTION_CONTROL>
  
  <SEPARATOR/>

  <ACTION_CONTROL LABEL="ActionControl.Label.OpenEvidenceTab">
    <LINK
      PAGE_ID="Evidence_workspaceInEditHighLevel"
    >
      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="caseID"
        />
        <TARGET
          NAME="PAGE"
          PROPERTY="caseID"
        />
      </CONNECT>
    </LINK>
  </ACTION_CONTROL>
  
  <SEPARATOR/>
  
  <ACTION_CONTROL LABEL="ActionControl.Label.ApplyChanges">
    <LINK
      OPEN_MODAL="true"
        PAGE_ID="Evidence_applyChanges1"
    >
      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="caseID"
        />
        <TARGET
          NAME="PAGE"
          PROPERTY="caseID"
        />
      </CONNECT>
    </LINK>
  </ACTION_CONTROL>
  
  <ACTION_CONTROL LABEL="ActionControl.Label.ValidateChanges">
    <LINK
      OPEN_MODAL="true"
        PAGE_ID="Evidence_validateChanges"
    >
      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="caseID"
        />
        <TARGET
          NAME="PAGE"
          PROPERTY="caseID"
        />
      </CONNECT>
    </LINK>
  </ACTION_CONTROL>

  <ACTION_CONTROL 
    IMAGE="TransferEvidence" 
    LABEL="ActionControl.Label.TransferEvidence"
  >
    <LINK OPEN_MODAL="true" PAGE_ID="Evidence_transferEvidence1">
      <CONNECT>
        <SOURCE NAME="PAGE" PROPERTY="caseID"/>
        <TARGET NAME="PAGE" PROPERTY="caseID"/>
      </CONNECT>
      <CONNECT>
        <SOURCE NAME="GETPARTICIPANT" PROPERTY="participantRoleID"/>
        <TARGET NAME="PAGE" PROPERTY="participantID"/>
      </CONNECT>
      <CONNECT>
        <SOURCE NAME="GETPARTICIPANT" PROPERTY="caseParticipantRoleID"/>
        <TARGET NAME="PAGE" PROPERTY="caseParticipantRoleID"/>
      </CONNECT>
    </LINK>
  </ACTION_CONTROL>
  
  <SEPARATOR/>
  
  <ACTION_CONTROL LABEL="ActionControl.Label.Approve">
    <LINK
      OPEN_MODAL="true"
      PAGE_ID="Evidence_approveApprovalRequest"
    >
      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="caseID"
        />
        <TARGET
          NAME="PAGE"
          PROPERTY="caseID"
        />
      </CONNECT>
    </LINK>
  </ACTION_CONTROL>

  <ACTION_CONTROL LABEL="ActionControl.Label.Reject">
    <LINK
      OPEN_MODAL="true"
      PAGE_ID="Evidence_rejectApprovalRequests"
    >
      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="caseID"
        />
        <TARGET
          NAME="PAGE"
          PROPERTY="caseID"
        />
      </CONNECT>
    </LINK>
  </ACTION_CONTROL>

  
  
</VIEW>
