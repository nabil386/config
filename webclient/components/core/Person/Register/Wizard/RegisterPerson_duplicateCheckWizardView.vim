<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2011-2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2011-2012 Curam Software Ltd                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to check if the person has already been      -->
<!-- registered.                                                            -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="Person"
    NAME="DISPLAYNICKNAMES"
    OPERATION="readSearchWithNicknamesIndicator"
  />


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
    TITLE="Cluster.Title.SearchCriteria"
  >


    <FIELD LABEL="Field.Label.ReferenceNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="referenceNumber"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="searchKey$searchKey$referenceNumber"
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


    <FIELD LABEL="Field.Label.FirstName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="forename"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="searchKey$searchKey$forename"
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
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="searchKey$searchKey$surname"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.DateOfBirth"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="searchKey$dateOfBirth"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="searchKey$searchKey$dateOfBirth"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AddressLineOne">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="addressLine1"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="searchKey$searchKey$addressDtls$addressLine1"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.City">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="city"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="searchKey$searchKey$addressDtls$city"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Nickname">
      <CONNECT>
        <SOURCE
          NAME="DISPLAYNICKNAMES"
          PROPERTY="searchWithNicknamesInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="searchKey$searchKey$nicknameInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DoubleMetaphone">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="soundsLikeInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="searchKey$searchKey$soundsLikeInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Gender"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="gender"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="searchKey$searchKey$gender"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AddressLineTwo">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="addressLine2"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="searchKey$searchKey$addressDtls$addressLine2"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.BirthLastName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="birthSurname"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="searchKey$searchKey$birthSurname"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER>
    <ACTION_SET
      ALIGNMENT="CENTER"
      BOTTOM="false"
    >
      <!-- BEGIN, CR00350228, AC-->
      <ACTION_CONTROL
        ACTION_ID="SEARCH"
        DEFAULT="true"
        IMAGE="SearchButton"
        LABEL="ActionControl.Label.Search"
        TYPE="SUBMIT"
      >
        <!-- END, CR00350228-->
        <LINK
          PAGE_ID="THIS"
          SAVE_LINK="false"
        />
      </ACTION_CONTROL>


      <!-- BEGIN, CR00304371, PS -->
      <ACTION_CONTROL
        ACTION_ID="RESETPAGE"
        IMAGE="ResetButton"
        LABEL="ActionControl.Label.Reset"
        TYPE="SUBMIT"
      >
        <!-- END, CR00304371 -->
        <LINK
          DISMISS_MODAL="false"
          PAGE_ID="RegisterPerson_duplicateCheckWizard"
          SAVE_LINK="false"
        >
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="wizardStateID$wizardStateID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="wizardStateID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>
  </CLUSTER>


  <LIST
    PAGINATED="false"
    
    TITLE="List.Title.SearchResults"
  >


    <!-- BEGIN, CR00341131, MR -->
    <FIELD
      LABEL="Field.Title.Name"
      WIDTH="40"
    >
      <!-- END, CR00341131 -->
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$searchResult$personSearchResult$dtlsList$concernRoleName"
        />
      </CONNECT>


    </FIELD>


    <FIELD
      LABEL="Field.Title.Address"
      WIDTH="44"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$searchResult$personSearchResult$dtlsList$formattedAddress"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00341131, MR -->
    <FIELD
      LABEL="Field.Title.DateOfBirth"
      WIDTH="16"
    >
      <!-- END, CR00341131 -->
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$searchResult$personSearchResult$dtlsList$dateOfBirth"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
