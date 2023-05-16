<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012,2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2012 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Evidence infrastructure view page to be included in the client page    -->
<!-- for listing in edit and active case evidence                  -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

  <PAGE_PARAMETER NAME="caseID"/>

  <SERVER_INTERFACE
    CLASS="BDMEvidence"
    NAME="DISPLAY"
    OPERATION="listAllEvidence"
  />

  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$caseID"
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

  <LIST PAGINATED="FALSE">
  <!-- BEGIN CR00424202, JD -->
  <CONDITION>
	<IS_TRUE
	  NAME="DISPLAY"
	  PROPERTY="caseUpdatesAllowed"
	/>
  </CONDITION>
  <!-- END CR00424202, JD -->
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


    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <CONDITION>
          <IS_FALSE
            NAME="DISPLAY"
            PROPERTY="readOnlyInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="TRUE"
          PAGE_ID="Evidence_resolveModifyEvidencePage"
          SAVE_LINK="TRUE"
        >
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
              PROPERTY="evidenceDescriptorID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceDescriptorID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="evidenceID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceID"
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
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.discard">
      <CONDITION>
          <IS_FALSE
            NAME="DISPLAY"
            PROPERTY="evidenceParticipantDtlsList$dtls$activeEvidence"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Evidence_deleteInEdit"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="evidenceParticipantDtlsList$dtls$evidenceDescriptorID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceDescriptorID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
       <ACTION_CONTROL LABEL="ActionControl.Label.delete">
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="evidenceParticipantDtlsList$dtls$activeWithSingleInstanceInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="TRUE"
          PAGE_ID="Evidence_removeActive"
        >
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
              PROPERTY="evidenceParticipantDtlsList$dtls$evidenceDescriptorID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceDescriptorID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="evidenceParticipantDtlsList$dtls$evidenceID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>
    <DETAILS_ROW>
       <INLINE_PAGE URI_SOURCE_NAME="DISPLAY" URI_SOURCE_PROPERTY="evidenceDetailsPanelURL"/>
    </DETAILS_ROW> 
  </LIST>
  
  <!-- BEGIN CR00424202, JD -->
  <LIST PAGINATED="FALSE">
  <CONDITION>
	<IS_FALSE
	  NAME="DISPLAY"
	  PROPERTY="caseUpdatesAllowed"
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


    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <CONDITION>
          <IS_FALSE
            NAME="DISPLAY"
            PROPERTY="readOnlyInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="TRUE"
          PAGE_ID="Evidence_resolveModifyEvidencePage"
          SAVE_LINK="TRUE"
        >
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
              PROPERTY="evidenceDescriptorID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceDescriptorID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="evidenceID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceID"
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
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.discard">
      <CONDITION>
          <IS_FALSE
            NAME="DISPLAY"
            PROPERTY="readOnlyInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Evidence_deleteInEdit"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="evidenceParticipantDtlsList$dtls$evidenceDescriptorID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceDescriptorID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
       <ACTION_CONTROL LABEL="ActionControl.Label.delete">
        <CONDITION>
          <IS_FALSE
            NAME="DISPLAY"
            PROPERTY="readOnlyInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="TRUE"
          PAGE_ID="Evidence_removeActive"
        >
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
              PROPERTY="evidenceParticipantDtlsList$dtls$evidenceDescriptorID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceDescriptorID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="evidenceParticipantDtlsList$dtls$evidenceID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>
    <DETAILS_ROW>
       <INLINE_PAGE URI_SOURCE_NAME="DISPLAY" URI_SOURCE_PROPERTY="evidenceDetailsPanelURL"/>
    </DETAILS_ROW> 
  </LIST>
  <!-- END CR00424202, JD -->
</VIEW>
