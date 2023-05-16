<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002-2006, 2010 Curam Software Ltd.                      -->
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
<?curam-deprecated Since Curam 6.0, replaced with RegisterProductProviderWizard_registerView.vim?>
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
    CLASS="ProductProvider"
    NAME="ACTION"
    OPERATION="registerProductProviderWithTextBankAccountSortCode"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="relatedConcernID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="relatedConcernID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="relatedConcernRoleID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.Name"
  >
    <FIELD LABEL="Field.Label.ReferenceNumber">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="socialSecurityNumber"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.RegisteredName">
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
      WIDTH="50"
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
      WIDTH="50"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="prefCommMethod"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.RegistrationDate">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="registrationDate"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TAB_ORDER="ROW"
    TITLE="Cluster.Title.Address"
  >
    <FIELD>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="addressData"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="40"
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
    <FIELD CONTROL="SKIP"/>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.PaymentDetails"
  >
    <FIELD
      LABEL="Field.Label.PaymentMethod"
      USE_BLANK="true"
      WIDTH="50"
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
      WIDTH="50"
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
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.BankDetails"
  >


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
          PROPERTY="key$bankSortCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.AccountType"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="50"
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


  <CLUSTER
    LABEL_WIDTH="40"
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
    <FIELD
      LABEL="Field.Label.CountryCode"
      WIDTH="5"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="contactPhoneCountryCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Phone">
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
    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="contactName"
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
          PROPERTY="contactPhoneAreaCode"
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
          PROPERTY="contactPhoneExtension"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
