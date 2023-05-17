<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2008, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008-2010 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view to display an Investigation Delivery Case home page  -->
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
  <SHORTCUT_TITLE ICON="investigationcase.icon">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="contextDtls$description"
      />
    </CONNECT>
  </SHORTCUT_TITLE>
  <!-- END, CR00113328 -->


  <SERVER_INTERFACE
    CLASS="InvestigationDelivery"
    NAME="DISPLAY"
    OPERATION="readHomePageDetails1"
  />


  <!-- BEGIN, CR00234436, PM -->
  <SERVER_INTERFACE
    CLASS="CaseTransactionLog"
    NAME="DISPLAYTRANS"
    OPERATION="listAllChanges"
  />
  <!-- END, CR00234436 -->


  <PAGE_PARAMETER NAME="caseID"/>


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


  <!-- BEGIN, CR00234436, PM -->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAYTRANS"
      PROPERTY="key$dtls$dtls$caseID"
    />
  </CONNECT>
  <!-- END, CR00234436 -->


  <ACTION_SET>


    <ACTION_CONTROL LABEL="ActionControl.Label.AddResolution">
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="displayAddInd"
        />
      </CONDITION>
      <LINK
        OPEN_MODAL="TRUE"
        PAGE_ID="InvestigationDelivery_createResolution"
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


    <ACTION_CONTROL LABEL="ActionControl.Label.EditResolution">
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="displayEditInd"
        />
      </CONDITION>
      <LINK
        OPEN_MODAL="TRUE"
        PAGE_ID="InvestigationDelivery_modifyResolution"
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
      </LINK>
    </ACTION_CONTROL>
    <ACTION_CONTROL LABEL="ActionControl.Label.ViewResolutionHistory">
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="displayHistoryInd"
        />
      </CONDITION>
      <LINK
        OPEN_MODAL="TRUE"
        PAGE_ID="InvestigationDelivery_listResolutionChangeHistory"
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
    <ACTION_CONTROL LABEL="ActionControl.Label.RemoveResolution">
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="displayCancelInd"
        />
      </CONDITION>
      <LINK
        OPEN_MODAL="TRUE"
        PAGE_ID="InvestigationDelivery_cancelInvestigationResolution"
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
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>


  <!-- Case Details cluster -->
  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Label.ReferenceNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseDtls$caseReference"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.StartDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseStartDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.InvestigationType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="investigationType"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.InvestigationSubtype">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="investigationSubtype"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Priority">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="priorityCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.RegistrationDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="registrationDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseStatus"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.EndDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseEndDate"
        />
      </CONNECT>
    </FIELD>


    <CONTAINER LABEL="Field.Label.PrimaryClient">
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="concernRoleName"
          />
        </CONNECT>
        <LINK PAGE_ID="Participant_resolveConcernRoleTypeHome">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="concernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleID"
            />
          </CONNECT>
        </LINK>
      </FIELD>
    </CONTAINER>


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
              PROPERTY="contextDtls$description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="pageDescription"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>
  </CLUSTER>


  <CLUSTER
    NUM_COLS="1"
    TITLE="Cluster.Title.ClosureDetails"
  >
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="isCaseCloseInd"
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
            PROPERTY="closureDtls$comments"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00416277, VT -->
    <FIELD LABEL="Field.Label.Comments">
      <!-- END, CR00416277 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseDtls$comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <!-- BEGIN, CR00105577, MC -->
  <!-- Add Resolutions to the home page -->
  <INCLUDE FILE_NAME="InvestigationDelivery_homeResolutionsView.vim"/>
  <!-- END, CR00105577 -->


  <!-- BEGIN, CR00234436, PM -->
  <!-- Add Recent Changes section to the home page -->
  <INCLUDE FILE_NAME="InvestigationDelivery_recentChangesView.vim"/>
  <!-- END, CR00234436 -->


</VIEW>
