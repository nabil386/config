<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012,2023. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2009-2011 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for the organization user search pages.              -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

  <!-- BEGIN, CR00282028, IBM -->
  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="ACTION"
        PROPERTY="informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>


  <SERVER_INTERFACE
    CLASS="Organization"
    NAME="ACTION"
    OPERATION="searchUserDetails"
    PHASE="ACTION"
  />
  <!-- END, CR00282028 -->


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
    TITLE="Cluster.Title.SearchCriteria"
  >


    <FIELD LABEL="Field.Label.FirstName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="criteria$firstname"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.OrganisationUnit">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="criteria$organisationUnitID"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.CancelledUsers">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="includeCancelledUserInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.LastName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="criteria$surname"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Job">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="jobID"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ClosedUsers">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="includeClosedUserInd"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER>
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
        <LINK PAGE_ID="Organization_adminUserSearch"/>
      </ACTION_CONTROL>
    </ACTION_SET>
  </CLUSTER>


  <LIST TITLE="List.Title.SearchResults">


    <FIELD
      LABEL="Field.Title.FirstName"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$dtls$dtls$userSearchDetailsReferenceList$fullName"
        >
        </SOURCE>
      </CONNECT>
      <LINK
        PAGE_ID="Organization_userHome"
        SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="username"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD
      LABEL="Field.Title.Position"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$dtls$dtls$userSearchDetailsReferenceList$positionName"
        />
      </CONNECT>
      <LINK PAGE_ID="Organization_listUsersForPosition">
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="result$dtls$dtls$userSearchDetailsReferenceList$positionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="positionID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="result$dtls$dtls$userSearchDetailsReferenceList$organisationStructureID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="organisationStructureID"
          />
        </CONNECT>


        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="result$dtls$dtls$userSearchDetailsReferenceList$organisationUnitID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="organisationUnitID"
          />
        </CONNECT>


      </LINK>
    </FIELD>


    <FIELD
      LABEL="Field.Title.OrganisationUnit"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$dtls$dtls$userSearchDetailsReferenceList$organisationUnitName"
        />
      </CONNECT>
      <LINK PAGE_ID="Organization_orgUnitHome">
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="result$dtls$dtls$userSearchDetailsReferenceList$organisationUnitID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="organisationUnitID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="result$dtls$dtls$userSearchDetailsReferenceList$organisationStructureID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="organisationStructureID"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD
      LABEL="Field.Title.StatusCode"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$dtls$dtls$userSearchDetailsReferenceList$statusCode"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
