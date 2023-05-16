<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
 
  Copyright IBM Corporation 2008, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2008 Curam Software Ltd.                                   -->
<!-- All rights reserved.                                                     -->
<!-- This software is the confidential and proprietary information of Curam   -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose      -->
<!-- such Confidential Information and shall use it only in accordance with   -->
<!-- the terms of the license agreement you entered into with Curam           -->
<!-- Software.                                                                -->
<!-- Description -->
<!-- =========== -->
<!-- The included view for the modify org unit approval pages.                -->
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
    CLASS="Organization"
    NAME="DISPLAY"
    OPERATION="readInvestigationApprovalCheckForOrgUnit"
  />


  <SERVER_INTERFACE
    CLASS="Organization"
    NAME="ACTION"
    OPERATION="modifyInvestigationApprovalCheckForOrgUnit"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="investigationApprovalCheckID"/>
  <PAGE_PARAMETER NAME="description"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="investigationApprovalCheckID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$investigationApprovalCheckID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="investigationApprovalCheckID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="investigationApprovalCheckID"
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
      PROPERTY="organisationUnitID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="organisationUnitID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="investigationConfigID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="investigationConfigID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="recordStatus"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="recordStatus"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="typeCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="typeCode"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="applyToAllInvestigationTypes"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="applyToAllInvestigationTypes"
    />
  </CONNECT>


  <CLUSTER NUM_COLS="1">
    <FIELD
      LABEL="Field.Label.Percentage"
      WIDTH="4"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="percentage"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="percentage"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.InvestigationType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="type"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00406866, VT -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
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
