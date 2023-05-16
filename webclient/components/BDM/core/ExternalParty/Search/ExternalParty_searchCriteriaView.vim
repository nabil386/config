<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010-2011 Curam Software Ltd.                                -->
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


  <!-- BEGIN, CR00290965, IBM -->
  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="ACTION"
        PROPERTY="informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>

<!-- BEGIN BDM FEATURE 52455: Modified server interface class  -->
  <SERVER_INTERFACE
    CLASS="BDMExternalParty"
    NAME="ACTION"
    OPERATION="searchExternalPartyDetails"
    PHASE="ACTION"
  />
  <!-- END BDM FEATURE 52455:  -->
  <!-- END, CR00290965 -->


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
    TITLE="Cluster.Title.SearchCriteria"
  >


    <FIELD LABEL="Field.Label.ReferenceNumber">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$referenceNumber"
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
        <TARGET
          NAME="ACTION"
          PROPERTY="key$concernRoleName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AddressLineOne">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$addressDtls$addressLine1"
        />
      </CONNECT>
    </FIELD>

    <!-- BEGIN BDM FEATURE 52455: Manage SSA Countries -->    
	<FIELD LABEL="Field.Label.POBox">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="bdmKey$poBox"
        />
      </CONNECT>
    </FIELD>    

	<FIELD LABEL="Field.Label.Province">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="bdmKey$stateProvince"
        />
      </CONNECT>
    </FIELD>
	
	<FIELD LABEL="Field.Label.Country" USE_BLANK="TRUE"
        USE_DEFAULT="FALSE">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="bdmKey$countryCode"
        />
      </CONNECT>
    </FIELD>
	<!-- END BDM FEATURE 52455: Manage SSA Countries  -->

    <FIELD
      LABEL="Field.Label.Type"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$type"
        />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.Label.AddressLineTwo">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$addressDtls$addressLine2"
        />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.Label.City">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$addressDtls$city"
        />
      </CONNECT>
    </FIELD>

    <!-- BEGIN BDM FEATURE 52455: Manage SSA Countries  -->  
	<FIELD LABEL="Field.Label.Postal">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="bdmKey$postalCode"
        />
      </CONNECT>
    </FIELD>
    <!-- END BDM FEATURE 52455: Manage SSA Countries  -->  

    <FIELD CONTROL="SKIP"/>


  </CLUSTER>


</VIEW>
