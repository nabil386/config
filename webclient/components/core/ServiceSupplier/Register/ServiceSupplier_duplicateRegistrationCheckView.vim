<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- Check for duplication registration of a new service supplier -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <PAGE_TITLE DESCRIPTION="PageTitle.Description">
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText1"
      />
    </CONNECT>
  </PAGE_TITLE>


  <INCLUDE FILE_NAME="ServiceSupplier_searchCriteriaView.vim"/>


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
        <LINK PAGE_ID="ServiceSupplier_duplicateRegistrationCheck"/>
      </ACTION_CONTROL>
    </ACTION_SET>
  </CLUSTER>


  <LIST TITLE="List.Title.SearchResults">


    <DETAILS_ROW>


      <INLINE_PAGE PAGE_ID="ServiceSupplier_homeTabDetails">
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="dtlsList$concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
      </INLINE_PAGE>


    </DETAILS_ROW>


    <FIELD
      LABEL="Field.Title.Name"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="dtlsList$participantDetails"
        />
      </CONNECT>
      <LINK PAGE_ID="ServiceSupplier_homePage">
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="dtlsList$concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD
      LABEL="Field.Title.Address"
      WIDTH="60"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="dtlsList$formattedAddress"
        />
      </CONNECT>
    </FIELD>


  </LIST>
</VIEW>
