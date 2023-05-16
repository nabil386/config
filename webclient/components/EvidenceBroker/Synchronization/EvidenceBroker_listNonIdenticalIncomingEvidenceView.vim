<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright 2011 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- List of non-identical incoming evidence to be synchronized.            -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="Page.Title"
      />
    </CONNECT>
  </PAGE_TITLE>


  <PAGE_PARAMETER NAME="caseID"/>


  <SERVER_INTERFACE
    CLASS="EvidenceBroker"
    NAME="DISPLAY"
    OPERATION="listNonIdenticalIncomingEvidenceWithVerifications"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
  </CONNECT>


  <LIST>
    <!-- BEGIN CR00424202, JD -->
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="caseUpdatesAllowed"
      />
    </CONDITION>
    <!-- END CR00424202, JD -->
    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Compare">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="EvidenceBroker_compareNonIdenticalEvidence"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="affectedEvidence"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="targetEvidenceType"
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
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Dismiss">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="EvidenceBroker_confirmResolvedNonIdenticalEvidence"
        >
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
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="Action.Control.ViewVerifications">
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="isVerificationShared"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="EvidenceBroker_listVerificationsForIncomingEvidence"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="sourceCaseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="evidenceType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceType"
            />
          </CONNECT>


          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="affectedEvidence"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="targetEvidenceType"
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
              PROPERTY="caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="targetCaseID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <FIELD
      LABEL="List.Title.EvidenceType"
      WIDTH="11"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="evidenceType"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.TargetEvidenceType"
      WIDTH="11"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="affectedEvidence"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="List.Title.Participant"
      WIDTH="11"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="participantName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.Description"
      WIDTH="23"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="summary"
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
      LABEL="List.Title.Event"
      WIDTH="9"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="eventType"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.SourceCase"
      WIDTH="17"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="sourceCase"
        />
      </CONNECT>
    </FIELD>
  </LIST>


  <!-- BEGIN CR00424202, JD -->
  <LIST>
    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY"
        PROPERTY="caseUpdatesAllowed"
      />
    </CONDITION>
    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Compare">
        <CONDITION>
          <IS_FALSE
            NAME="DISPLAY"
            PROPERTY="readOnlyInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="EvidenceBroker_compareNonIdenticalEvidence"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="affectedEvidence"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="targetEvidenceType"
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
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Dismiss">
        <CONDITION>
          <IS_FALSE
            NAME="DISPLAY"
            PROPERTY="readOnlyInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="EvidenceBroker_confirmResolvedNonIdenticalEvidence"
        >
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
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="Action.Control.ViewVerifications">
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="isVerificationShared"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="EvidenceBroker_listVerificationsForIncomingEvidence"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="sourceCaseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="evidenceType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceType"
            />
          </CONNECT>


          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="affectedEvidence"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="targetEvidenceType"
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
              PROPERTY="caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="targetCaseID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <FIELD
      LABEL="List.Title.EvidenceType"
      WIDTH="11"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="evidenceType"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.TargetEvidenceType"
      WIDTH="11"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="affectedEvidence"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="List.Title.Participant"
      WIDTH="11"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="participantName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.Description"
      WIDTH="23"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="summary"
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
      LABEL="List.Title.Event"
      WIDTH="9"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="eventType"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.SourceCase"
      WIDTH="17"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="sourceCase"
        />
      </CONNECT>
    </FIELD>
  </LIST>
  <!-- END CR00424202, JD -->
</VIEW>
