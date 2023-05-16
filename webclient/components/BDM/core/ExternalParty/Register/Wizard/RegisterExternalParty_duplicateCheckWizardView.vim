<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2011-2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2011-2012 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page displays search criteria for an External Party               -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


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
          PROPERTY="referenceNumber"
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


    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="concernRoleName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$concernRoleName"
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
          PROPERTY="addressLine1"
        />
      </CONNECT>
    </FIELD>

    <!-- BEGIN BDM FEATURE 52455 -->    
	<FIELD LABEL="Field.Label.POBox">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="searchKey$poBox"
        />
      </CONNECT>
    </FIELD>    

	<FIELD LABEL="Field.Label.Province">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="searchKey$stateProvince"
        />
      </CONNECT>
    </FIELD>
	
	<FIELD LABEL="Field.Label.Country" USE_BLANK="TRUE"
        USE_DEFAULT="FALSE">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="searchKey$countryCode"
        />
      </CONNECT>
    </FIELD>
    <!-- END BDM FEATURE 52455 -->  

    <FIELD
      LABEL="Field.Label.Type"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
        <CONNECT>
          <INITIAL HIDDEN_PROPERTY="code" NAME="DISPLAY_LISTTYPES" PROPERTY="description" />
        </CONNECT>    
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="type"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="type"
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
          PROPERTY="addressLine2"
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
          PROPERTY="city"
        />
      </CONNECT>
    </FIELD>

    <!-- BEGIN BDM FEATURE 52455 -->  
	<FIELD LABEL="Field.Label.Postal">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="searchKey$postalCode"
        />
      </CONNECT>
    </FIELD>
    <!-- END BDM FEATURE 52455 -->  

    <FIELD CONTROL="SKIP"/>

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
        <LINK
          DISMISS_MODAL="false"
          PAGE_ID="RegisterExternalParty_duplicateCheckWizard"
          SAVE_LINK="false"
        >
          <!-- END, CR00304371 -->
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


</VIEW>
