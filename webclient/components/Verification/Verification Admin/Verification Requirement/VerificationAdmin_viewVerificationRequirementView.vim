<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2011 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to view a Verification Requirement record.   -->
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
    CLASS="VerificationAdministration"
    NAME="DISPLAY"
    OPERATION="readVerificationRequirement"
  />


  <PAGE_PARAMETER NAME="verificationRequirementID"/>


  <!-- Map verificationRequirementID parameter to viewVerificationRequirement -->
  <!-- display-phase bean in order to retrieve details -->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="verificationRequirementID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$dtls$verificationRequirementID"
    />
  </CONNECT>
  
  
  <!-- BEGIN, CR00078989, AL  -->
  <CLUSTER
    DESCRIPTION="Cluster.Description.Details"
    LABEL_WIDTH="40"
    NUM_COLS="2"
  >
    <!-- END, CR00078989  -->
    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.FromDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fromDate"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.MinimumItems">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="minimumItems"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.ClientSupplied">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="clientSupplied"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.Level">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="verificationLevel"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.ToDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="toDate"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.ReverificationMode">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reverificationMode"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.Required">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="mandatory"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  
  
  <!-- BEGIN, CR00078989, AL  -->
  <CLUSTER
    DESCRIPTION="Cluster.Description.VerificationDueDetails"
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.VerificationDueDetails"
  >
    <!-- END, CR00078989  -->
    <FIELD LABEL="Field.Label.DueDays">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dueDays"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.DueDateFrom">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dueDateFrom"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.DueDateEvent">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dueDateEventType"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.WarningDays">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="warningDays"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.ModifyDueDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dueDateModifiableInd"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  
  
  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.EventDetails"
  >
    <FIELD LABEL="Field.Label.AddEvent">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="vrAddedEventType"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.ValueChangedEvent">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="valueChangedEventType"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.UpdateEvent">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="vrUpdatedEventType"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  
  
  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Description"
  >
    <FIELD
      HEIGHT="3"
      WIDTH="100"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  
</VIEW>