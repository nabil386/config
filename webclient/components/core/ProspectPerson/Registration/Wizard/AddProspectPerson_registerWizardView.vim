<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
 
  Copyright IBM Corporation 2012, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2011-2012 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to enter prospect person registration details.        -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CLUSTER
    LABEL_WIDTH="40"
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


    <FIELD LABEL="Field.Label.FirstName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_SEARCH_CRITERIA"
          PROPERTY="forename"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="firstForename"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.LastName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_SEARCH_CRITERIA"
          PROPERTY="surname"
        />
      </CONNECT>
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


    <!--BEGIN, CR00236822, PB-->
    <FIELD LABEL="Field.Label.BirthLastName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_SEARCH_CRITERIA"
          PROPERTY="birthSurname"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personBirthName"
        />
      </CONNECT>
    </FIELD>
    <!--END, CR00236822-->


    <FIELD
      LABEL="Field.Label.Gender"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_SEARCH_CRITERIA"
          PROPERTY="gender"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="gender"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
  >


    <FIELD
      LABEL="Field.Label.DateofBirth"
      USE_DEFAULT="false"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_SEARCH_CRITERIA"
          PROPERTY="dateOfBirth"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dateOfBirth"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.FromAge"
      WIDTH="3"
      WIDTH_UNITS="CHARS"
    >
      <!-- BEGIN, CR00386007, VT -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="estimatedFromAgeOpt"
        />
      </CONNECT>
      <!-- END, CR00386007 -->
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


    <FIELD
      LABEL="Field.Label.SpecialInterest"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="50"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="specialInterestCode"
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


    <FIELD
      LABEL="Field.Label.CountryOfBirth"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="countryOfBirth"
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


    <FIELD LABEL="Field.Label.PreferredPublicOffice">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="preferredPublicOfficeContact"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00306060, PS -->
    <FIELD
      LABEL="Field.Label.MaritalStatus"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="50"
    >
      <!-- END, CR00306060 -->


      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="maritalStatusCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.DateofDeath"
      USE_DEFAULT="false"
      WIDTH="50"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dateOfDeath"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ToAge"
      WIDTH="3"
      WIDTH_UNITS="CHARS"
    >
      <!-- BEGIN, CR00386007, VT -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="estimatedToAgeOpt"
        />
      </CONNECT>
      <!-- END, CR00386007 -->
    </FIELD>


    <FIELD
      LABEL="Field.Label.Nationality"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="nationalityCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.BirthPlace">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="placeOfBirth"
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


    <FIELD
      HEIGHT="3"
      LABEL="Field.Label.Race"
      USE_BLANK="true"
      WIDTH="100"
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
          PROPERTY="race"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.IndigenousPerson">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="indigenousPersonInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.IndigenousGroup">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="indigenousGroupCode"
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
    BEHAVIOR="COLLAPSED"
    DESCRIPTION="Cluster.Description.MailingAddress"
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TAB_ORDER="ROW"
    TITLE="Cluster.Title.MailingAddress"
  >


    <FIELD>
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


</VIEW>
