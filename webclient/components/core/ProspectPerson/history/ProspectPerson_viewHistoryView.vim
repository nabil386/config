<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2007, 2014. All Rights Reserved.

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
<!-- Views a Prospect Person Snapshot.                                      -->
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
    CLASS="ProspectPerson"
    NAME="DISPLAY"
    OPERATION="readProspectPerson"
  />


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


  <PAGE_PARAMETER NAME="concernRoleID"/>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Name"
  >


    <FIELD LABEL="Field.Label.Title">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="title"
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


    <FIELD LABEL="Field.Label.BirthName">
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


    <FIELD LABEL="Field.Label.FirstName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="firstForename"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Surname">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="surname"
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


    <FIELD LABEL="Field.Label.MotherBirthSurname">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="motherBirthSurname"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >


    <FIELD LABEL="Field.Label.DateOfBirth">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateOfBirth"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DateOfDeath">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateOfDeath"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00052548, "PA" -->
    <FIELD LABEL="Field.Label.FromAge">
      <!-- BEGIN, CR00386007, VT -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="estimatedFromAgeOpt"
        />
      </CONNECT>
      <!-- END, CR00386007 -->
    </FIELD>
    <!-- END, CR00052548 -->


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


    <FIELD LABEL="Field.Label.PublicOffice">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="preferredPublicOfficeName"
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


    <FIELD LABEL="Field.Label.DateOfBirthVerification">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateOfBirthVerInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DateOfDeathVerification">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateOfDeathVerInd"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00052548, "PA" -->
    <FIELD LABEL="Field.Label.ToAge">
      <!-- BEGIN, CR00386007, VT -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="estimatedToAgeOpt"
        />
      </CONNECT>
      <!-- END, CR00386007 -->
    </FIELD>
    <!-- END, CR00052548 -->


    <FIELD LABEL="Field.Label.maritalStatus">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="maritalStatusCode"
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


    <FIELD LABEL="Field.Label.prefCommMethod">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="prefCommMethod"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Country"
  >


    <FIELD LABEL="Field.Label.nationality">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="nationalityCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.BirthCountry">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="countryOfBirth"
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


    <FIELD LABEL="Field.Label.BirthPlace">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="placeOfBirth"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EthnicOriginCode">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="ethnicOriginCode"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >


    <!-- BEGIN, CR00416277, VT -->
    <FIELD
      HEIGHT="3"
      LABEL="Field.Label.Comments"
    >
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
