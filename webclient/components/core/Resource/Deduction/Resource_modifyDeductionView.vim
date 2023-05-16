<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2006, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2006, 2010, 2011 Curam Software Ltd.                         -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view for creating a deduction type.                       -->
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
    CLASS="Resource"
    NAME="DISPLAY"
    OPERATION="readDeduction"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="Resource"
    NAME="ACTION"
    OPERATION="modifyDeduction"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="deductionID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="deductionID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$deductionID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="deductionID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="key$deductionID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="deductionID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="dtls$deductionID"
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
      PROPERTY="dateCreated"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="dateCreated"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="deductionName"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="deductionName"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="47"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="deductionName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.MaxDeductionAmount">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="maxAmount"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="maxAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.MaxDeductionPercentage">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="maxPercentage"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="maxPercentage"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.MinDeductionAmount">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="minAmount"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="minAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Priority">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="priority"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="priority"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.PreventOverlappingInd">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="preventOverlappingInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="preventOverlappingInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Category">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="category"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="category"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DefaultAmount">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="defaultAmount"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="defaultAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DefaultPercentage">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="defaultPercentage"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="defaultPercentage"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ActionType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="actionType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="actionType"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AssignNextPriorityInd">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="applyNextPriorityInd"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER LABEL_WIDTH="47.5">
    <FIELD LABEL="Field.Label.ApplyToAllProductsInd">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="changeAllIndicator"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ApplyOverlappingToAllProductsInd">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="changeAllOverlappingIndOpt"
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
