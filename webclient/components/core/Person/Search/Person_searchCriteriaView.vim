<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2010,2020. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- This page displays search criteria for a Person.         -->

<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="Person"
    NAME="DISPLAY"
    OPERATION="readSearchWithNicknamesIndicator"
  />


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
    TITLE="Cluster.Title.SearchCriteria"
  >


    <!-- BEGIN, CR00341856, PB -->
    <FIELD LABEL="Cluster.Field.Label.ReferenceNumber">
      <!-- END, CR00341856 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personSearchKey$referenceNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD CONTROL="SKIP"/>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
    TITLE="Cluster.Title.AdditionalSearchCriteria"
  >


    <!-- BEGIN, CR00341856, PB -->
    <FIELD LABEL="Cluster.Field.Label.FirstName">
      <!-- END, CR00341856 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personSearchKey$forename"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00341856, PB -->
    <FIELD LABEL="Cluster.Field.Label.LastName">
      <!-- END, CR00341856 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personSearchKey$surname"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.DateOfBirth"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personSearchKey$dateOfBirth"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00341856, PB -->
    <FIELD LABEL="Cluster.Field.Label.AddressLineOne">
      <!-- END, CR00341856 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personSearchKey$addressDtls$addressLine1"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00341856, PB -->
    <FIELD LABEL="Cluster.Field.Label.City">
      <!-- END, CR00341856 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personSearchKey$addressDtls$city"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Nickname">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$searchWithNicknamesInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personSearchKey$nicknameInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DoubleMetaphone">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personSearchKey$soundsLikeInd"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00341856, PB -->
    <FIELD
      LABEL="Cluster.Field.Label.Gender"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="50"
    >
      <!-- END, CR00341856 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personSearchKey$gender"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AddressLineTwo">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personSearchKey$addressDtls$addressLine2"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00341856, PB -->
    <FIELD LABEL="Cluster.Field.Label.BirthLastName">
      <!-- END, CR00341856 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personSearchKey$birthSurname"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
