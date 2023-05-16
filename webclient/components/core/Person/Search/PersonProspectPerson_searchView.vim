<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2008, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This is a search page which allows the user to search                  -->
<!-- for prospect persons and persons.                                      -->
<!-- BEGIN, CR00232757, GD -->
<?curam-deprecated Since Curam 6.0, replaced by Person_search1.uim?>
<!-- END, CR00232757 -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <!-- BEGIN, CR00100777, BD -->
  <SERVER_INTERFACE
    CLASS="Person"
    NAME="DISPLAY"
    OPERATION="readSearchWithNicknamesIndicator"
  />
  <!-- END, CR00100777 -->
  <SERVER_INTERFACE
    CLASS="ProspectPerson"
    NAME="ACTION"
    OPERATION="searchPersonsAndProspectPersons"
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
          PROPERTY="prospectPersonSearchKey$referenceNumber"
        />
      </CONNECT>
    </FIELD>


    <CONTAINER LABEL="Container.Label.LastName">
      <FIELD
        LABEL="Field.Label.LastName"
        WIDTH="85"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="prospectPersonSearchKey$surname"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.DoubleMetaphone"
        WIDTH="15"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="prospectPersonSearchKey$soundsLikeInd"
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
          PROPERTY="prospectPersonSearchKey$dateOfBirth"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AddressLineOne">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="prospectPersonSearchKey$address"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.BirthLastName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="prospectPersonSearchKey$birthSurname"
        />
      </CONNECT>
    </FIELD>


    <FIELD CONTROL="SKIP"/>


    <!-- BEGIN, CR00100777, BD -->
    <CONTAINER LABEL="Field.Label.FirstName">
      <!-- BEGIN, CR00463142, EC -->
      <FIELD LABEL="Field.Label.FirstName">
        <!-- END, CR00463142 -->
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="prospectPersonSearchKey$forename"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.Nickname"
        WIDTH="15"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$searchWithNicknamesInd"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="prospectPersonSearchKey$nicknameInd"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>
    <!-- END, CR00100777 -->
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


    <FIELD LABEL="Field.Label.MotherBirthName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="prospectPersonSearchKey$motherSurname"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER>
    <ACTION_SET
      ALIGNMENT="LEFT"
      BOTTOM="false"
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
        <LINK PAGE_ID="PersonProspectPerson_search"/>
      </ACTION_CONTROL>
    </ACTION_SET>
  </CLUSTER>


  <!-- List to display Person records-->
  <LIST>
    <TITLE>
      <CONNECT>
        <SOURCE
          NAME="TEXT"
          PROPERTY="List.Title.Persons"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$personIndicator"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="TEXT"
          PROPERTY="Field.Label.SearchResultsClosingParanthesis"
        />
      </CONNECT>
    </TITLE>
    <FIELD WIDTH="2">
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="pDetails$dtls$duplicateInd"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Person_duplicateHomePage"
      >
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="pDetails$dtls$originalConcernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <CONTAINER
      LABEL="Container.Label.ReferenceNumber"
      WIDTH="5"
    >
      <FIELD WIDTH="8">
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="pDetails$dtls$referenceNumber"
          />
        </CONNECT>
        <LINK PAGE_ID="Person_resolveHomePage">
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="pDetails$dtls$concernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleID"
            />
          </CONNECT>


        </LINK>
      </FIELD>
    </CONTAINER>


    <FIELD
      LABEL="Field.Label.FirstName"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="pDetails$dtls$forename"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.LastName"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="pDetails$dtls$surname"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.AddressLineOne"
      WIDTH="12"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="pDetails$dtls$address"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.City"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="pDetails$dtls$city"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.DateOfBirth"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="pDetails$dtls$dateOfBirth"
        />
      </CONNECT>
    </FIELD>


  </LIST>


  <!-- List to display Prospect Person records-->
  <LIST>
    <TITLE>
      <CONNECT>
        <SOURCE
          NAME="TEXT"
          PROPERTY="List.Title.ProspectPersons"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$prospectPersonIndicator"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="TEXT"
          PROPERTY="Field.Label.SearchResultsClosingParanthesis"
        />
      </CONNECT>
    </TITLE>


    <FIELD WIDTH="2">
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="ppDetails$dtls$duplicateInd"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Person_duplicateHomePage"
      >
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="ppDetails$dtls$originalConcernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <CONTAINER
      LABEL="Container.Label.AlternativeRegistration"
      WIDTH="6"
    >
      <FIELD WIDTH="6">
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="ppDetails$dtls$altRegistrationIndicator"
          />
        </CONNECT>
        <LINK PAGE_ID="Person_resolveHomePage">
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="ppDetails$dtls$personConcernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleID"
            />
          </CONNECT>
        </LINK>
      </FIELD>
    </CONTAINER>


    <CONTAINER
      LABEL="Container.Label.ReferenceNumber"
      WIDTH="5"
    >
      <FIELD WIDTH="8">
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="ppDetails$dtls$referenceNumber"
          />
        </CONNECT>
        <!-- BEGIN CR00113312 SAI -->
        <LINK PAGE_ID="ProspectPerson_resolveViewHome">
          <!-- END CR00113312 -->
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="ppDetails$dtls$concernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="ppDetails$dtls$concernRoleType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleType"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="ppDetails$dtls$altRegistrationIndicator"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="altRegistrationIndicator"
            />
          </CONNECT>
        </LINK>
      </FIELD>
    </CONTAINER>


    <FIELD
      LABEL="Field.Label.FirstName"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="ppDetails$dtls$forename"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.LastName"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="ppDetails$dtls$surname"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.AddressLineOne"
      WIDTH="12"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="ppDetails$dtls$address"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.City"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="ppDetails$dtls$city"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.DateOfBirth"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="ppDetails$dtls$dateOfBirth"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
