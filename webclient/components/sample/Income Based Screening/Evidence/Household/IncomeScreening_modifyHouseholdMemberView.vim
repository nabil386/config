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
<!-- This page allows the user to view a list of household members -->
<!-- for an Income Screening case. -->
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
    OPERATION="viewHouseholdEvidence"
  />


  <SERVER_INTERFACE
    CLASS="IncomeScreening"
    NAME="ACTION"
    OPERATION="modifyHouseholdEvidence"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="householdEvidenceID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="householdEvidenceID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="fsHouseholdEvidenceID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="householdEvidenceID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="fsHouseholdEvidenceID"
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
    LABEL_WIDTH="55"
    NUM_COLS="2"
    TITLE="Cluster.Tilte.Details"
  >


    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="concernRoleName"
        />
      </CONNECT>
      <LINK
        PAGE_ID="Participant_resolveRoleHome"
        SAVE_LINK="false"
      >
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


    <FIELD LABEL="Field.Label.PrepareMeals">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="prepareMealsInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="prepareMealsInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Disabled">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="disabledInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="disabledInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Relationship">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rshipToHOH"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="rshipToHOH"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Citizen">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="citizenshipCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="citizenshipCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Striker">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="strikerInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="strikerInd"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>