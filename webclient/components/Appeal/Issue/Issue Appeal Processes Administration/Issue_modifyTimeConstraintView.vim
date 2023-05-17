<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2004-2006, 2010 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page is used to modify a benefit product. -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText"
      />
    </CONNECT>
  </PAGE_TITLE>

  <SERVER_INTERFACE
    CLASS="Product"
    NAME="DISPLAY"
    OPERATION="readIssueTimeConstraint"
  />
  
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="issueTimeConstraintID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$issueTimeConstraintID"
    />
  </CONNECT>   

  <SERVER_INTERFACE
    CLASS="Product"
    NAME="ACTION"
    OPERATION="modifyIssueTimeConstraint"
    PHASE="ACTION"
  />
  
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$issueConfigurationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="issueConfigurationID"
    />
  </CONNECT>
  
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$versionNo"
    />
  </CONNECT>
  
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$recordStatus"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$recordStatus"
    />
  </CONNECT>
  
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$issueTimeConstraintID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$issueTimeConstraintID"
    />
  </CONNECT>
  
  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Label.Details"
    >
    
    <FIELD LABEL="Field.Label.NumberOfDays">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$numberOfDays"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$numberOfDays"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.ConstraintType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$constraintType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$constraintType"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.FromDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$fromDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$fromDate"
        />
      </CONNECT>
    </FIELD>

  </CLUSTER>


</VIEW>