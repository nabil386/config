<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2010, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2007, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- Views a Prospect Employer Snapshot.                                    -->
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
    CLASS="ProspectEmployer"
    NAME="DISPLAY"
    OPERATION="readProspectEmployerDetails"
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


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >


    <FIELD LABEL="Field.Label.TradingName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="tradingName"
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


    <FIELD LABEL="Field.Label.BusinessDesc">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="businessDesc"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.PreferredLanguage">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="preferredLanguage"
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


    <FIELD LABEL="Field.Label.PermStaff">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="numberPermanentStaff"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.PaymentFrequency">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentFrequency"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.NextPaymentDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="nextPaymentDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.StatusCode">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="statusCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.RegName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="registeredName"
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


    <FIELD LABEL="Field.Label.PublicOfficeName">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="publicOfficeName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.PrefCommMethod">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="prefCommMethod"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.CasualStaff">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="numberCasualStaff"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.RegistrationDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="registrationDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Sensitivity">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="sensitivity"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Currency">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="currencyType"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.MethodOfPayment">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="methodOfPmtCode"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >


    <!-- BEGIN, CR00416277, VT -->
    <FIELD LABEL="Field.Label.Comments">
      <!-- END, CR00416277 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
