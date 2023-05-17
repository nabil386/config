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
<!-- Reads a Prospect Person' home page details and further details         -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="ProspectPerson"
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
      PROPERTY="concernRoleHomePageKey$concernRoleID"
    />
  </CONNECT>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Label.Name"
  >


    <FIELD LABEL="Field.Label.Title">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="title"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.FirstName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="firstForename"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.LastName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="surname"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.MiddleName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="otherForename"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Suffix">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="nameSuffix"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Initials">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="initials"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Label.Details"
  >


    <FIELD LABEL="Field.Label.MaritalStatus">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="maritalStatusCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.BirthLastName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="personBirthName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Gender">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="gender"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DOB">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateOfBirth"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DOD">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateOfDeath"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.FromAge">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fromAge"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.PlaceOfBirth">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="placeOfBirth"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Race">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$prospectPersonHomeDetails$race"
        />
      </CONNECT>
    </FIELD>


    <!-- TODO REPLACE SKIP FIELD WITH IndigenousPerson FIELD -->
    <FIELD CONTROL="SKIP"/>


    <!--
    <FIELD LABEL="Field.Label.IndigenousPerson">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$prospectPersonHomeDetails$indigenousInd"
        />
      </CONNECT>
    </FIELD>
-->


    <!-- TODO REPLACE SKIP FIELD WITH RESIDENCY ABROAD FIELD -->
    <FIELD CONTROL="SKIP"/>


    <!--
    <FIELD LABEL="Field.Label.ResidencyAbroad">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$personHomeDetails$residencyAbroadInd"
        />
      </CONNECT>
    </FIELD>
-->


    <FIELD LABEL="Field.Label.Nationality">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="nationalityCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.MothersBirthLastName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="motherBirthSurname"
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


    <FIELD LABEL="Field.Label.DOBVerified">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateOfBirthVerInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DODVerified">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateOfDeathVerInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ToAge">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="toAge"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.CountryOfBirth">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="countryOfBirth"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EthnicOrigin">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="ethnicOriginCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.IndigenousGroup">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$prospectPersonHomeDetails$indigenousGroupCode"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
