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
<!-- Reads Client Details                                                   -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="Person"
    NAME="DISPLAY"
    OPERATION="readClientEvidence"
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
    LABEL_WIDTH="45"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="concernRoleName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.StartDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.PrefLanguage">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="preferredLanguage"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.PrefCommunication">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="prefCommMethod"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.PrefCommunicationFromDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="prefCommFromDate"
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


    <FIELD LABEL="Field.Label.EndDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.PrefPublicOffice">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="prefPublicOfficeName"
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


    <FIELD LABEL="Field.Label.PrefCommunicationToDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="prefCommToDate"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
    TITLE="Cluster.Label.Contact"
  >


    <FIELD LABEL="Field.Label.Address">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="addressData"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
    STYLE="outer-cluster-borderless-nospace"
  >


    <FIELD LABEL="Field.Label.EmailAddress">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="primaryEmailAddress"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.WebAddress">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="primaryWebAddress"
        />
      </CONNECT>
    </FIELD>


    <CONTAINER LABEL="Container.Label.Telephone">
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="phoneCountryCode"
          />
        </CONNECT>
      </FIELD>
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="phoneAreaCode"
          />
        </CONNECT>
      </FIELD>
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="phoneNumber"
          />
        </CONNECT>
      </FIELD>
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="phoneExtension"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
    TITLE="Cluster.Title.BankAccountDetails"
  >


    <FIELD LABEL="Field.Label.AccountName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bankAccountName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AccountType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bankAccountType"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AccountNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bankAccountNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.SortCode">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bankSortCode"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
