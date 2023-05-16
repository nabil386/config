<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view displaying a list of existing utility payments for a -->
<!-- client and is displayed before a new utility payment is created.       -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE DESCRIPTION="PageTitle.Description">
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText1"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="Person"
    NAME="DISPLAY"
    OPERATION="listUtilityPayment"
  />


  <PAGE_PARAMETER NAME="concernRoleID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="concernRoleID"
    />
  </CONNECT>


  <LIST TITLE="List.Title.ExistingUtilityPayments">
    <FIELD LABEL="Field.Label.UtilityName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="utilityName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.AccountName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="accountName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Amount">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="amount"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.CaseReference">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseReference"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>