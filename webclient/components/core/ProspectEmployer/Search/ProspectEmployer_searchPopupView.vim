<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2005, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This is a popup search page which allows the user to search -->
<!-- for a particular prospect employer.-->
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
    CLASS="ProspectEmployer"
    NAME="ACTION"
    OPERATION="searchProspectEmployers"
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
          PROPERTY="prospectEmployerSearchKey$referenceNumber"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.TradingName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="prospectEmployerSearchKey$tradingName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.BusinessAdd1">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="prospectEmployerSearchKey$businessAddress"
        />
      </CONNECT>
    </FIELD>
    <FIELD CONTROL="SKIP"/>
    <FIELD LABEL="Field.Label.RegName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="prospectEmployerSearchKey$registeredName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.City">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="prospectEmployerSearchKey$businessCity"
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
      <LINK PAGE_ID="ProspectEmployer_searchPopup"/>
    </ACTION_CONTROL>
    <ACTION_CONTROL
      IMAGE="CancelButton"
      LABEL="ActionControl.Label.Cancel"
      TYPE="DISMISS"
    />
  </ACTION_SET>


  <LIST TITLE="List.Title.SearchResults">
    <FIELD
      LABEL="Field.Title.Alt.Reg"
      WIDTH="8"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="dtls$altRegistrationIndicator"
        />
      </CONNECT>
    </FIELD>
    <CONTAINER
      LABEL="Container.Label.Action"
      WIDTH="10"
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
              PROPERTY="peDetails$dtls$concernRoleName"
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
      WIDTH="17"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="peDetails$dtls$referenceNumber"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.TradingName"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="peDetails$dtls$tradingName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.RegName"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="peDetails$dtls$registeredName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.BusinessAdd1"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="peDetails$dtls$businessAddress"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.City"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="peDetails$dtls$businessCity"
        />
      </CONNECT>
    </FIELD>
  </LIST>
</VIEW>
