<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2005-2006, 2008, 2010 Curam Software Ltd.                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- Popup person search page -->
<?curam-deprecated Since Curam 6.0, replaced by Person_searchPopupView1.vim?>
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
    OPERATION="search"
    PHASE="ACTION"
  />
  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.SearchCriteria"
  >
    <FIELD LABEL="Field.Label.ReferenceNumber">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personSearchKey$referenceNumber"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00050298, MR -->
    <!-- BEGIN, HARP 64908, SP -->
    <CONTAINER LABEL="Container.Label.LastName">
      <FIELD
        LABEL="Field.Label.LastName"
        WIDTH="85"
      >
        <!-- END, HARP 64908 -->
        <!-- END, CR00050298 -->


        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="personSearchKey$surname"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.Soundex"
        WIDTH="15"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="personSearchKey$soundsLikeInd"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>
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
    <FIELD LABEL="Field.Label.AddressLineOne">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personSearchKey$address"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.BirthLastName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personSearchKey$birthSurname"
        />
      </CONNECT>
    </FIELD>
    <FIELD CONTROL="SKIP"/>
    <FIELD LABEL="Field.Label.FirstName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personSearchKey$forename"
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
          PROPERTY="personSearchKey$sex"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.City">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personSearchKey$city"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.MotherBirthName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personSearchKey$motherSurname"
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
      <LINK PAGE_ID="Person_searchPopup"/>
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
      <!-- BEGIN, CR00100763, PDN -->
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Person_duplicateHomePage"
      >
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="originalConcernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
      </LINK>
      <!-- END, CR00100763 -->
    </FIELD>
    <CONTAINER
      LABEL="Container.Label.Action"
      WIDTH="7"
    >
      <ACTION_CONTROL
        LABEL="ActionControl.Label.Select"
        TYPE="DISMISS"
      >
        <LINK>
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="dtls$concernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="value"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="dtls$personFullName"
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
      LABEL="Field.Title.ReferenceNumber"
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
      LABEL="Field.Title.AddressLineOne"
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
      WIDTH="12"
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
      WIDTH="13"
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
