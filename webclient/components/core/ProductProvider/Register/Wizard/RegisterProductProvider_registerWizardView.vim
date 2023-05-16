<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2011, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2011-2012 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page presents the fields that must be completed by the user       -->
<!-- in order to register.                                                  -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Label.ReferenceNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_SEARCH_CRITERIA"
          PROPERTY="referenceNumber"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="socialSecurityNumber"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.RegisteredName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_SEARCH_CRITERIA"
          PROPERTY="registeredName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="registeredName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.PreferredLanguage"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="preferredLanguage"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_SEARCH_CRITERIA"
          PROPERTY="concernRoleName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.PreferredCommunication"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="prefCommMethod"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.RegistrationDate"
      WIDTH="50"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="registrationDate"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
    TAB_ORDER="ROW"
    TITLE="Cluster.Title.Address"
  >
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_SEARCH_CRITERIA"
          PROPERTY="addressData"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="addressData"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
    TITLE="Cluster.Title.PhoneNumber"
  >
    <FIELD
      LABEL="Field.Label.PhoneType"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="50"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="phoneType"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.AreaCode"
      WIDTH="5"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="phoneAreaCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Extension"
      WIDTH="5"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="phoneExtension"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.CountryCode"
      WIDTH="5"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="phoneCountryCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Phone">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="phoneNumber"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
    TITLE="Cluster.Title.PaymentDetails"
  >
    <FIELD
      LABEL="Field.Label.PaymentMethod"
      USE_BLANK="true"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="methodOfPmtCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Frequency"
      USE_BLANK="true"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="paymentFrequency"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Currency"
      USE_BLANK="true"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="currencyType"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
    TITLE="Cluster.Title.BankDetails"
  >
    <!-- BEGIN, CR00371769, VT -->
    <CONDITION>
      <IS_FALSE
        NAME="IBANINDICATOR"
        PROPERTY="ibanInd"
      />
    </CONDITION>
    <!-- END, CR00371769 -->


    <FIELD LABEL="Field.Label.AccountName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="bankAccountName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.BankBranchName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="registrationDtls$bankSortCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.AccountType"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="accountType"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AccountNumber">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="accountNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Joint">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="jointAccountInd"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <!-- BEGIN, CR00371769, VT -->
  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
    TITLE="Cluster.Title.BankDetails"
  >


    <CONDITION>
      <IS_TRUE
        NAME="IBANINDICATOR"
        PROPERTY="ibanInd"
      />
    </CONDITION>


    <FIELD LABEL="Field.Label.Iban">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="ibanOpt"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AccountName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="bankAccountName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.BankBranchName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="registrationDtls$bankSortCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.AccountType"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="accountType"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Bic">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="registrationDtls$bicOpt"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AccountNumber">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="accountNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Joint">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="jointAccountInd"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>
  <!-- END, CR00371769 -->


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
    TITLE="Cluster.Title.ContactDetails"
  >
    <FIELD
      LABEL="Field.Label.Type"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="50"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="contactTitle"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00306738, VT -->
    <FIELD
      LABEL="Field.Label.ContactPhoneCountryCode"
      WIDTH="5"
      WIDTH_UNITS="CHARS"
    >
      <!-- END, CR00306738 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="contactPhoneCountryCode"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00306738, VT -->
    <FIELD LABEL="Field.Label.ContactPhoneNumber">
      <!-- END, CR00306738 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="contactPhoneNumber"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.PublicOffice">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="prefPublicOfficeID"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00306738, VT -->
    <FIELD LABEL="Field.Label.ContactName">
      <!-- END, CR00306738 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="contactName"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00306738, VT -->
    <FIELD
      LABEL="Field.Label.ContactPhoneAreaCode"
      WIDTH="5"
      WIDTH_UNITS="CHARS"
    >
      <!-- END, CR00306738 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="contactPhoneAreaCode"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00306738, VT -->
    <FIELD
      LABEL="Field.Label.ContactPhoneExtension"
      WIDTH="5"
      WIDTH_UNITS="CHARS"
    >
      <!-- END, CR00306738 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="contactPhoneExtension"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
