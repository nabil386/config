<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2009-2011 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page displays the Integrated Service Plan Home.                   -->
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
    CLASS="IntegratedServicePlan"
    NAME="DISPLAY"
    OPERATION="readHomePageDetails"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="integratedServicePlanHomePageKey$integratedCaseID"
    />
  </CONNECT>

  <LIST>
    <!-- BEGIN, CR00246725, MR -->
    <FIELD LABEL="Field.Label.Reference" WIDTH="10">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="summaryReference"
        />
      </CONNECT>
      <!-- BEGIN, CR00234425, SS -->
      <LINK PAGE_ID="IntegratedServicePlan_resolveDelivery">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="summaryID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="summaryID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="summaryType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="summaryType"
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
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="description"
          />
        </CONNECT>
      </LINK>
      <!-- END, CR00234425 -->
    </FIELD>
    <FIELD LABEL="Field.Label.Name" WIDTH="15">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="summaryName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Type" WIDTH="8">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="summaryType"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Goal" WIDTH="20">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="summaryGoal"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Outcome" WIDTH="10">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="summaryOutcome"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.TotalEstimatedCost" WIDTH="14">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="summaryEstimatedCost"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.TotalActualCost" WIDTH="14">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="summaryActualCost"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Status" WIDTH="9">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="summaryStatus"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00246725 -->
  </LIST>


</VIEW>
