<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page displays the list of household resources. -->
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
    <CONNECT>
      <SOURCE
        NAME="PAGE"
        PROPERTY="pageDescription"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="IncomeScreening"
    NAME="DISPLAY"
    OPERATION="viewResourceEvidence"
  />


  <SERVER_INTERFACE
    CLASS="IncomeScreening"
    NAME="ACTION"
    OPERATION="modifyResourceEvidence"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="householdResourcesID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>
  <PAGE_PARAMETER NAME="caseEvidenceTypeID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="householdResourcesID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="fsHouseholdResourcesID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseEvidenceTypeID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseEvidenceTypeID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="householdResourcesID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="fsHouseholdResourceID"
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
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="concernRoleID"
    />
  </CONNECT>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >


    <FIELD LABEL="Field.Label.HouseholdMember">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="concernRoleName"
        />
      </CONNECT>
      <LINK PAGE_ID="Participant_resolveRoleHome">
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
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="concernRoleType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="participantType"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD LABEL="Field.Label.TotalValue">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="totalValue"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="totalValue"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Usage">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="assetUsage"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="assetUsage"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DateDisposed">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateDisposed"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dateDisposed"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AmountReceived">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="amountReceived"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="amountReceived"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Type">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="assetTypeCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="assetTypeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.TotalOwed">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="totalOwed"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="totalOwed"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DateAquired">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateAquired"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dateAquired"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.DisposalReason"
      USE_BLANK="true"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="disposalReasonCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="disposalReasonCode"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
