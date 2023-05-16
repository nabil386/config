<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2007, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of       -->
<!-- Curam Software, Ltd. ("Confidential Information").  You shall not      -->
<!-- disclose such Confidential Information and shall use it only in        -->
<!-- accordance with the terms of the license agreement you entered into    -->
<!-- with Curam Software.                                                   -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to modify time constraint details when       -->
<!-- opened from the time constraint view page.                             -->
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
    CLASS="Product"
    NAME="DISPLAY"
    OPERATION="readIssueTimeConstraint"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="Product"
    NAME="ACTION"
    OPERATION="modifyIssueTimeConstraint"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="issueTimeConstraintID"/>
  <PAGE_PARAMETER NAME="description"/>


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


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="issueTimeConstraintID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="issueTimeConstraintID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="issueConfigurationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="issueConfigurationID"
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


  <CLUSTER LABEL_WIDTH="40">


    <FIELD
      LABEL="Field.Label.NumberOfDays"
      WIDTH="45"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="numberOfDays"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="numberOfDays"
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
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fromDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ConstraintType"
      WIDTH="90"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="constraintType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="constraintType"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
