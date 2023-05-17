<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2005, 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2005 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- Popup representative search page -->
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
    CLASS="Representative"
    NAME="ACTION"
    OPERATION="search"
    PHASE="ACTION"
  />
  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.SearchCriteria"
  >
    <FIELD LABEL="Field.Label.AlternateID">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$dtls$dtls$alternateID"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <ACTION_SET
    ALIGNMENT="CENTER"
    BOTTOM="false"
    TOP="true"
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
      <LINK PAGE_ID="Representative_searchPopup"/>
    </ACTION_CONTROL>
    <ACTION_CONTROL
      IMAGE="CancelButton"
      LABEL="ActionControl.Label.Cancel"
      TYPE="DISMISS"
    />
  </ACTION_SET>
  <LIST TITLE="List.Title.SearchResults">
    <!-- BEGIN, CR00335290, GA -->
    <CONTAINER
      LABEL="Container.Label.Action"
      WIDTH="15"
    >
      <!-- END, CR00335290 -->
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
              PROPERTY="representativeName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="description"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>
    <!-- BEGIN, CR00335290, GA -->
    <FIELD
      LABEL="Field.Title.ReferenceNumber"
      WIDTH="20"
    >
      <!-- END, CR00335290 -->
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="concernRoleID"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00335290, GA -->
    <FIELD
      LABEL="Field.Title.RepresentativeName"
      WIDTH="40"
    >
      <!-- END, CR00335290 -->
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="representativeName"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00335290, GA -->
    <FIELD
      LABEL="Field.Title.AlternateID"
      WIDTH="25"
    >
      <!-- END, CR00335290 -->
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$dtls$dtls$alternateID"
        />
      </CONNECT>
    </FIELD>
  </LIST>
</VIEW>
