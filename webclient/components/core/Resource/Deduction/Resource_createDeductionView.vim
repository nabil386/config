<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2006, 2013, 2015. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2006, 2011 Curam Software Ltd.                               -->
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


  <!-- BEGIN, CR00463142, EC -->
  <CLUSTER
    LABEL_WIDTH="47"
    NUM_COLS="2"
    SUMMARY="Summary.NewDeductions.Details"
  >
    <!-- END, CR00463142 -->
    <FIELD
      LABEL="Field.Label.Name"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="deductionName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.MaxDeductionAmount">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="maxAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.MaxDeductionPercentage">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="maxPercentage"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.MinDeductionAmount">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="minAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Priority">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="priority"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.PreventOverlappingInd">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="preventOverlappingInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Category"
      USE_DEFAULT="true"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="category"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DefaultAmount">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="defaultAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DefaultPercentage">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="defaultPercentage"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ActionType"
      USE_DEFAULT="true"
    >
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
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
