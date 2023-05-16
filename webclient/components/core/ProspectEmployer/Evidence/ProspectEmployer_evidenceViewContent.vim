<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Reads a Prospect Employer's home page details and further details      -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="ProspectEmployer"
    NAME="DISPLAY"
    OPERATION="readHomePageDetails"
  />


  <PAGE_PARAMETER NAME="concernRoleID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="maintainConcernRoleKey$concernRoleID"
    />
  </CONNECT>


  <CLUSTER NUM_COLS="2">
    <FIELD LABEL="Field.Label.RegisteredName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="registeredName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Permanent">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="numberPermanentStaff"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.BusinessDescription">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="businessDesc"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EmployerType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="companyType"
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


    <FIELD LABEL="Field.Label.TradingName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="tradingName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Casual">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="numberCasualStaff"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.SpecialInterest">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="specialInterestCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.IndustryType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="industryType"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
