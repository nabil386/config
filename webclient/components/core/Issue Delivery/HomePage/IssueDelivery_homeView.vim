<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2007, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2007-2010, 2012 Curam Software Ltd.                          -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view to display an Issue Delivery Case's home page        -->
<!-- details.                                                               -->
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


  <!-- BEGIN, CR00113328, ELG -->
  <SHORTCUT_TITLE ICON="issuecase.icon">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="result$description$description"
      />
    </CONNECT>
  </SHORTCUT_TITLE>
  <!-- END, CR00113328 -->


  <SERVER_INTERFACE
    CLASS="IssueDelivery"
    NAME="DISPLAY"
    OPERATION="readHomePageDetails1"
  />


  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseHeaderKey$caseID"
    />
  </CONNECT>


  <ACTION_SET>
    <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="IssueDelivery_modifyIssueFromView"
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


    <!-- BEGIN, CR00241280, MC -->
    <ACTION_CONTROL LABEL="ActionControl.Label.AddResolution">
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="result$indicators$noResolutionInd"
        />
      </CONDITION>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="IssueDelivery_createResolution"
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
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="issueConfigurationID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="issueConfigurationID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$description$description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="description"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.EditResolution">
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="result$indicators$inEditResolutionInd"
        />
      </CONDITION>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="IssueDelivery_modifyResolution"
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
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="issueConfigurationID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="issueConfigurationID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="resolutionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="resolutionID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$description$description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="description"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.SubmitResolution">
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="result$indicators$inEditResolutionInd"
        />
      </CONDITION>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="IssueDelivery_submitResolutionForApproval"
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
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="issueConfigurationID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="issueConfigurationID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="resolutionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="resolutionID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$description$description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="description"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
    <ACTION_CONTROL
      APPEND_ELLIPSIS="false"
      LABEL="ActionControl.Label.ResolutionStatusHistory"
    >
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="result$indicators$inEditResolutionInd"
        />
      </CONDITION>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="IssueDelivery_listResolutionStatusHistory"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$resolutionDetails$resolutionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="resolutionID"
          />
        </CONNECT>
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
      APPEND_ELLIPSIS="false"
      LABEL="ActionControl.Label.ResolutionStatusHistory"
    >
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="result$indicators$approvedResolutionInd"
        />
      </CONDITION>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="IssueDelivery_listResolutionStatusHistory"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$resolutionDetails$resolutionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="resolutionID"
          />
        </CONNECT>
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
    <ACTION_CONTROL LABEL="ActionControl.Label.RejectResolution">
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="result$indicators$approvedResolutionInd"
        />
      </CONDITION>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="IssueDelivery_rejectResolution"
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
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="resolutionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="resolutionID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$description$description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="description"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <SEPARATOR/>


    <ACTION_CONTROL LABEL="ActionControl.Label.AddEvidence">
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="IssueDelivery_resolveSelectEvidenceType"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$adminIntegratedCaseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="adminIntegratedCaseID"
          />
        </CONNECT>
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
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$relatedCaseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="relatedCaseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$description$description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="description"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
    <!-- END, CR00241280 -->


    <SEPARATOR/>


    <ACTION_CONTROL LABEL="ActionControl.Label.ChangeClose">
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="result$indicators$caseClosedInd"
        />
      </CONDITION>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="IssueDelivery_modifyClosureDetails"
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
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$description$description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.Close">
      <CONDITION>
        <IS_FALSE
          NAME="DISPLAY"
          PROPERTY="result$indicators$caseClosedInd"
        />
      </CONDITION>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="IssueDelivery_close"
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
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$description$description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>


  <!-- Case Details cluster -->
  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="2"
  >


    <CONTAINER LABEL="Field.Label.Owner">
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="orgObjectReferenceName"
          />
        </CONNECT>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Case_resolveOrgObjectTypeHome"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="userName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="userName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="orgObjectReference"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="orgObjectReference"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="orgObjectType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="orgObjectType"
            />
          </CONNECT>
        </LINK>
      </FIELD>
      <ACTION_CONTROL
        APPEND_ELLIPSIS="false"
        LABEL="ActionControl.Label.Change"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Case_modifyCaseOwner"
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
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$description$description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="pageDescription"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


    <FIELD LABEL="Field.Label.OwnerType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="orgObjectType"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <!-- Conditional Closure Details Cluster -->
  <CLUSTER
    NUM_COLS="1"
    TITLE="Cluster.Title.ClosureDetails"
  >
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="caseClosedInd"
      />
    </CONDITION>


    <CLUSTER
      LABEL_WIDTH="30"
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
    >
      <FIELD LABEL="Field.Label.Closed">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="closureDateAndUserFullName"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.Reason">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="reasonCode"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


    <CLUSTER
      LABEL_WIDTH="15"
      STYLE="cluster-cpr-no-border"
    >
      <FIELD LABEL="Field.Label.Comments">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="dtls$comments"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


  </CLUSTER>


  <!-- Resolution Details cluster -->
  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="2"
    TITLE="Cluster.Title.Resolution"
  >


    <FIELD LABEL="Field.Label.Resolution">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$resolutionDetails$resolution"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.CreatedBy">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$resolutionDetails$createdByFullName"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Organization_viewUserDetails"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$resolutionDetails$createdBy"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <FIELD LABEL="Field.Label.ResolutionStatus">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$resolutionDetails$resolutionStatus"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.CreationDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$resolutionDetails$creationDate"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <!-- Current Evidence Details list -->
  <LIST
    DESCRIPTION="Cluster.CurrentEvidence.Description"
    TITLE="Cluster.Title.CurrentEvidence"
  >


    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Evidence_resolveViewSnapshotEvidencePage">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$currentEvidence$dtlsList$evidenceType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceType"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$currentEvidence$dtlsList$evidenceID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$currentEvidence$dtlsList$evidenceDescriptorID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceDescriptorID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$caseDetails$caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$description$description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="contextDescription"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


    <FIELD
      LABEL="Field.Label.CurrentName"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$currentEvidence$dtlsList$concernRoleName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.CurrentEffectiveDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$currentEvidence$dtlsList$effectiveDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.CurrentDetails"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$currentEvidence$dtlsList$evidenceSummary"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.CurrentStatus"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$currentEvidence$dtlsList$evidenceStatus"
        />
      </CONNECT>
    </FIELD>
  </LIST>
  <!-- End Current Evidence Details list -->
  <!-- Snapshot Evidence Details list -->
  <CLUSTER
    DESCRIPTION="Cluster.SnapshotEvidence.Description"
    SHOW_LABELS="false"
    TITLE="Cluster.Title.SnapshotEvidence"
  >
    <FIELD CONTROL="SKIP"/>
  </CLUSTER>


  <LIST>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Evidence_resolveViewSnapshotEvidencePage">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$snapshotEvidence$dtlsList$evidenceType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceType"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$snapshotEvidence$dtlsList$evidenceID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$snapshotEvidence$dtlsList$evidenceDescriptorID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceDescriptorID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$caseDetails$caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$description$description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="contextDescription"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


    <FIELD
      LABEL="Field.Label.SnapshotName"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$snapshotEvidence$dtlsList$concernRoleName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.SnapshotEffectiveDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$snapshotEvidence$dtlsList$effectiveDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.SnapshotDetails"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$snapshotEvidence$dtlsList$evidenceSummary"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.SnapshotStatus"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$snapshotEvidence$dtlsList$evidenceStatus"
        />
      </CONNECT>
    </FIELD>
  </LIST>
  <!-- End Snapshot Evidence List -->
  <!-- Case Comments cluster -->
  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00416277, VT -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00416277 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$caseDetails$comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <!-- BEGIN, CR00022647, AK -->
  <LIST TITLE="List.Title.RecentChanges">


    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Case_resolveViewTransactionLogRecord">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="transactionDtls$transactionDetailsList$dtlsList$relatedID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="relatedID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="transactionDtls$transactionDetailsList$dtlsList$eventTypeDesc"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="transactionDescription"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="transactionDtls$transactionDetailsList$dtlsList$transactionDateTime"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="transactionDateTime"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="transactionDtls$transactionDetailsList$dtlsList$userFullName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userFullName"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="transactionDtls$transactionDetailsList$dtlsList$eventTypeDesc"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="eventType"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="transactionDtls$transactionDetailsList$dtlsList$pageID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


    <FIELD
      LABEL="Field.Label.EventType"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$transactionDtls$transactionDetailsList$dtlsList$eventTypeDesc"
        />
      </CONNECT>
      <!--BEGIN CR00108593, GBA-->


    </FIELD>
    <FIELD
      LABEL="Field.Label.Description"
      WIDTH="45"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="transactionDtls$transactionDetailsList$dtlsList$transactionDescription"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.TransactionDateTime"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$transactionDtls$transactionDetailsList$dtlsList$transactionDateTime"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.UserFullname"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$transactionDtls$transactionDetailsList$dtlsList$userFullName"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Organization_viewUserDetails"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="transactionDtls$transactionDetailsList$dtlsList$username"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
      </LINK>
      <!--END CR00108593-->
    </FIELD>
  </LIST>
  <!-- END, CR00022647 -->
</VIEW>
