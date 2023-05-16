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
<!-- This page allows the user to enter person registration details.        -->
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
    CLASS="Person"
    NAME="ACTION"
    OPERATION="register"
    PHASE="ACTION"
  />


  <!-- BEGIN, CR00108252 PA -->
  <SERVER_INTERFACE
    CLASS="Person"
    NAME="DISPLAYRACE"
    OPERATION="listRace"
    PHASE="DISPLAY"
  />
  <!-- END, CR00108252 - PA -->


  <!-- BEGIN, CR00078486, POH -->
  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="ACTION"
        PROPERTY="msg"
      />
    </CONNECT>
  </INFORMATIONAL>
  <!-- END, CR00078486 -->


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


    <FIELD LABEL="Field.Label.RefernceNumber">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="socialSecurityNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.FirstName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="firstForename"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.LastName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="surname"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Initials"
      WIDTH="5"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="initials"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.MothersLastName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="motherBirthSurname"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Title"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="50"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="title"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.MiddleName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="otherForename"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Suffix"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="50"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="nameSuffix"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.BirthLastName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="birthName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Gender"
      WIDTH="50"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="sex"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >


    <!-- BEGIN, CR00050298, MR -->
    <!-- BEGIN, HARP 64908, SP -->
    <CONTAINER LABEL="Container.Label.DateofBirth">


      <FIELD
        LABEL="Field.Label.DateofBirth"
        USE_DEFAULT="false"
        WIDTH="50"
      >
        <!-- END, HARP 64908 -->
        <!-- END, CR00050298 -->


        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="dateOfBirth"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.Verified">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="dateOfBirthVerified"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>


    <FIELD LABEL="Field.Label.RegistrationDate">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="registrationDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.SpecialInterest"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="50"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="specialInterest"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.BirthPlace">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="birthPlace"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.EthnicOrigin"
      USE_BLANK="true"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="ethnicOriginCode"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00108252 PA -->
    <FIELD LABEL="Field.Label.IndigenousPerson">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$personRegistrationDetails$indigenousPersonInd"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00108252 - PA -->


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


    <FIELD LABEL="Field.Label.PreferredPublicOffice">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="preferredPublicOfficeContact"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00050298, MR -->
    <!-- BEGIN, HARP 64908, SP -->
    <CONTAINER LABEL="Container.Label.DateofDeath">
      <FIELD
        LABEL="Field.Label.DateofDeath"
        USE_DEFAULT="false"
        WIDTH="50"
      >
        <!-- END, HARP 64908 -->
        <!-- END, CR00050298 -->


        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="dateOfDeath"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.Verified">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="dateOfDeathVerified"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>


    <FIELD
      LABEL="Field.Label.MaritalStatus"
      WIDTH="50"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="currentMaritalStatus"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Nationality"
      WIDTH="50"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="nationality"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.CountryOfBirth">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="birthCountry"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00108252 PA -->
    <FIELD
      HEIGHT="3"
      LABEL="Field.Label.Race"
      USE_BLANK="true"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="raceCode"
          NAME="DISPLAYRACE"
          PROPERTY="raceName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$personRegistrationDetails$race"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.IndigenousGroup">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$personRegistrationDetails$indigenousGroupCode"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00108252 - PA -->


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


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TAB_ORDER="ROW"
    TITLE="Cluster.Title.PrimaryAddress"
  >


    <!-- BEGIN, CR00050298, MR -->
    <!-- BEGIN, HARP 64908, SP -->
    <FIELD LABEL="Field.Label.PrimaryAddressData">
      <!-- END, HARP 64908 -->
      <!-- END, CR00050298 -->


      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="addressData"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    DESCRIPTION="Cluster.Description.MailingAddress"
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TAB_ORDER="ROW"
    TITLE="Cluster.Title.MailingAddress"
  >


    <!-- BEGIN, CR00050298, MR -->
    <!-- BEGIN, HARP 64908, SP -->
    <FIELD LABEL="Field.Label.MailingAddressData">
      <!-- END, HARP 64908 -->
      <!-- END, CR00050298 -->


      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="mailingAddressData"
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
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.Citizenship"
  >


    <FIELD
      LABEL="Field.Label.Country"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="citizenshipCountryCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.From"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="citizenshipFromDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Reason"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="citizenshipReasonCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.To"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="citizenshipToDate"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.ForeignResidency"
  >


    <FIELD
      LABEL="Field.Label.Country"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="foreignResidencyCountryCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.From"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="foreignResidencyFromDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Reason"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="foreignResidencyReasonCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.To"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="foreignResidencyToDate"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.PaymentDetails"
  >


    <FIELD
      LABEL="Field.Label.MethodOfPayment"
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
      LABEL="Field.Label.PaymentFrequency"
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
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.CommunicationException"
  >


    <FIELD
      LABEL="Field.Label.Method"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="commExceptionMethodCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.From"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="commExceptionFromDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Reason"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="commExceptionReasonCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.To"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="commExceptionToDate"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
