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
<!-- This page displays search criteria for a Service Supplier               -->
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


  <SERVER_INTERFACE
    CLASS="BDMOASServiceSupplier"
    NAME="ACTION"
    OPERATION="searchServiceSupplier"
    PHASE="ACTION"
  />
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

    <!-- BEGIN TASK 67230 -->    
	<FIELD LABEL="Field.Label.POBox">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$poBox"
        />
      </CONNECT>
    </FIELD>    

	<FIELD LABEL="Field.Label.Province">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$stateProvince"
        />
      </CONNECT>
    </FIELD>
	
	<FIELD LABEL="Field.Label.Country" USE_BLANK="TRUE"
        USE_DEFAULT="FALSE">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$countryCode"
        />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.Label.Type" USE_BLANK="TRUE"
        USE_DEFAULT="FALSE">
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="key$typeCode"/>
      </CONNECT>
    </FIELD>    
    <!-- END TASK 67230 -->  

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

    <!-- BEGIN TASK 67230 -->  
	<FIELD LABEL="Field.Label.Postal">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$postalCode"
        />
      </CONNECT>
    </FIELD>
    <!-- END TASK 67230 -->  
    <FIELD CONTROL="SKIP"/>

  </CLUSTER>

</VIEW>
