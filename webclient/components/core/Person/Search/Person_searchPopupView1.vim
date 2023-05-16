<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2010, 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010-2011 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- Popup person search page -->
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
    CLASS="Person"
    NAME="ACTION"
    OPERATION="searchPersonForPopup"
    PHASE="ACTION"
  />
  <!-- END, CR00282028 -->


  <ACTION_SET>
    <ACTION_CONTROL
      IMAGE="CancelButton"
      LABEL="ActionControl.Label.Cancel"
      TYPE="DISMISS"
    />
  </ACTION_SET>


  <INCLUDE FILE_NAME="Person_searchCriteriaView.vim"/>


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
        <!-- BEGIN, CR00236989, AK -->
        <LINK
          DISMISS_MODAL="false"
          PAGE_ID="Person_searchPopup1"
        />
      </ACTION_CONTROL>
      <!-- END, CR00236989 -->
    </ACTION_SET>
  </CLUSTER>


  <LIST
    
    TITLE="List.Title.SearchResults"
  >
    <!-- BEGIN, CR00332583, PB -->
    <CONTAINER
      LABEL="Container.Label.Action"
      WIDTH="15"
    >
      <!-- END, CR00332583 -->
      <ACTION_CONTROL
        LABEL="ActionControl.Label.Select"
        TYPE="DISMISS"
      >
        <CONDITION>
          <IS_FALSE
            NAME="ACTION"
            PROPERTY="result$personSearchResult$dtlsList$restrictedIndOpt"
          />
        </CONDITION>
        <LINK>
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="dtlsList$concernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="value"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="dtlsList$concernRoleName"
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
      LABEL="Field.Title.Name"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="dtlsList$xmlPersonData"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.Address"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="dtlsList$formattedAddress"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.DateOfBirth"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="dtlsList$dateOfBirth"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
