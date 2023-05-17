<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002-2003 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows the user to view evidence.                            -->
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
    CLASS="SampleBenefit"
    NAME="DISPLAY"
    OPERATION="getEvidence"
  />


  <PAGE_PARAMETER NAME="evidenceID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="evidenceID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$evidenceID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >


    <FIELD LABEL="Field.Label.EffectiveFrom">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$effectiveFrom"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Eligible">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="eligibleInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="statusCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DailyRate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dailyRate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EligibleForChildBenefit">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="childIndicator"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>