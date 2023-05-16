<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2005-2010 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This is a popup search page which allows the user to search -->
<!-- for a particular prospect person.-->
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
    NAME="ACTION"
    OPERATION="searchProspectPersons"
    PHASE="ACTION"
  />
  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TITLE="Cluster.Title.SearchCriteria"
  >
    <FIELD LABEL="Field.Label.RefNumber">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="prospectPersonSearchKey$referenceNumber"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.LastName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="prospectPersonSearchKey$surname"
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
          PROPERTY="prospectPersonSearchKey$dateOfBirth"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.AddressLine1">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$prospectPersonSearchKey$address"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.BirthLastName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="birthSurname"
        />
      </CONNECT>
    </FIELD>
    <FIELD CONTROL="SKIP"/>
    <FIELD LABEL="Field.Label.FirstName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="prospectPersonSearchKey$forename"
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
          PROPERTY="prospectPersonSearchKey$sex"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.City">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="prospectPersonSearchKey$city"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.MotherLastName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="motherSurname"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <ACTION_SET
    ALIGNMENT="CENTER"
    TOP="false"
  >
    <ACTION_CONTROL
      DEFAULT="true"
      IMAGE="SearchButton"
      LABEL="ActionControl.Label.Search"
      TYPE="SUBMIT"
    >
      <LINK PAGE_ID="THIS"/>
    </ACTION_CONTROL>
    <ACTION_CONTROL
      IMAGE="ResetButton"
      LABEL="ActionControl.Label.Reset"
    >
      <LINK PAGE_ID="ProspectPerson_searchPopup"/>
    </ACTION_CONTROL>
    <ACTION_CONTROL
      IMAGE="CancelButton"
      LABEL="ActionControl.Label.Cancel"
      TYPE="DISMISS"
    />
  </ACTION_SET>


  <LIST TITLE="List.Title.SearchResults">
    <FIELD WIDTH="1">
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="duplicateInd"
        />
      </CONNECT>
      <!-- BEGIN, CR00151260, PDN -->
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Person_duplicateHomePage"
      >
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="dtls$originalConcernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
      </LINK>
      <!-- END, CR00151260 -->
    </FIELD>
    <FIELD
      LABEL="Field.Title.Alt.Reg"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="altRegistrationIndicator"
        />
      </CONNECT>
    </FIELD>
    <CONTAINER
      LABEL="Container.Label.Action"
      WIDTH="8"
    >
      <ACTION_CONTROL
        LABEL="ActionControl.Label.Select"
        TYPE="DISMISS"
      >
        <LINK>
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="concernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="value"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="result$prospectPersonSearchResult$ppDetails$dtls$personFullName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="description"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>
    <FIELD
      LABEL="Field.Title.RefNumber"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="dtls$referenceNumber"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.FirstName"
      WIDTH="14"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="dtls$forename"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.LastName"
      WIDTH="14"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="dtls$surname"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.AddressLine1"
      WIDTH="14"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="dtls$address"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.City"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="dtls$city"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.DateOfBirth"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="dtls$dateOfBirth"
        />
      </CONNECT>
    </FIELD>
  </LIST>
</VIEW>
