<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2004-2007, 2010 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for the organization user search pages.              -->
<!-- BEGIN, CR00247294, PM  -->
<?curam-deprecated Since Curam 5.2 SP4, replaced with 
Organization_userSearchDetailsView.
As part of the change to this page, all pages relating to this have been 
replaced.See Release note:CR00215472?>
<!-- END, CR00247294 -->
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
    CLASS="Organization"
    NAME="ACTION"
    OPERATION="userSearch"
    PHASE="ACTION"
  />


  <CLUSTER
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
          PROPERTY="dtls$fullName"
        />
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
          PROPERTY="dtls$positionName"
        />
      </CONNECT>
      <LINK PAGE_ID="Organization_listUsersForPosition">
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="dtls$positionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="positionID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="dtls$organisationStructureID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="organisationStructureID"
          />
        </CONNECT>


        <!-- BEGIN CR00082256, MR-->
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="dtls$organisationUnitID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="organisationUnitID"
          />
        </CONNECT>
        <!--END CR00082256 -->


      </LINK>
    </FIELD>


    <FIELD
      LABEL="Field.Title.OrganisationUnit"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="dtls$organisationUnitName"
        />
      </CONNECT>
      <LINK PAGE_ID="Organization_orgUnitHome">
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="dtls$organisationUnitID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="organisationUnitID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="dtls$organisationStructureID"
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
          PROPERTY="dtls$statusCode"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
