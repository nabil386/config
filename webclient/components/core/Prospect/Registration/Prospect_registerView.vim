<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2004, 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2004-2007 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to enter prospect person registration        -->
<!-- details.                                                               -->
<!-- BEGIN, CR00348318, GA -->
<?curam-deprecated Since Curam 6.0.5.0 replaced with AddProspectPerson_registerWizardView.vim. This vim is deprecated as the Prospect participant type is no longer used. See release note: CR00348318?>
<!-- END, CR00348318 -->
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
    CLASS="Prospect"
    NAME="ACTION"
    OPERATION="registerProspectPerson"
    PHASE="ACTION"
  />


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


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
    TITLE="Cluster.Title.Name"
  >


    <FIELD
      LABEL="Field.Label.Title"
      USE_BLANK="true"
      USE_DEFAULT="false"
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
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="sex"
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


  </CLUSTER>


  <!-- Cluster containing personal details for the prospect -->
  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >


    <FIELD
      LABEL="Field.Label.DateofBirth"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dateOfBirth"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.RegistrationDate"
      USE_DEFAULT="false"
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
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="specialInterest"
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


    <FIELD
      LABEL="Field.Label.BirthPlace"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="birthPlace"
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
          PROPERTY="publicOfficeID"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.DateofDeath"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dateOfDeath"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.MaritalStatus"
      USE_BLANK="true"
      USE_DEFAULT="false"
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
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="nationality"
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
          PROPERTY="birthCountry"
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


  </CLUSTER>


  <!-- Cluster containing contact address details. -->
  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
    TAB_ORDER="ROW"
    TITLE="Cluster.Title.ContactAddress"
  >


    <FIELD>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="contactAddressData"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <!-- Cluster containing phone details. -->
  <CLUSTER
    LABEL_WIDTH="45"
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


  <!-- Cluster containing citizenship details. -->
  <CLUSTER
    LABEL_WIDTH="45"
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


  <!-- Cluster containing foreign residency details. -->
  <CLUSTER
    LABEL_WIDTH="45"
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


  <!-- Cluster containing communication exception details. -->
  <CLUSTER
    LABEL_WIDTH="45"
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
