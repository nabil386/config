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
<!-- This page displays search criteria for a Participant Search            -->
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
  <!-- END, CR00290965 -->


  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="DISPLAY"
    OPERATION="getAddressRegionList"
    PHASE="DISPLAY"
  />


  <!-- BEGIN, CR00233791, DJ -->
  <!-- BEGIN, CR00290965, IBM -->
  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="ACTION"
    OPERATION="searchParticipantDetails"
    PHASE="ACTION"
  />
  <!-- END, CR00290965 -->
  <!-- END, CR00233791 -->


  <CLUSTER
    LABEL_WIDTH="40"
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
    LABEL_WIDTH="40"
    NUM_COLS="2"
    STYLE="cluster-cpr-no-internal-padding"
    TITLE="Cluster.Title.AdditionalSearchCriteria"
  >


    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="displayDistrictInd"
      />
    </CONDITION>


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


    <FIELD LABEL="Field.Label.City">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$addressDtls$city"
        />
      </CONNECT>
    </FIELD>


    <FIELD CONTROL="SKIP"/>


    <FIELD LABEL="Field.Label.AddressLineTwo">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$addressDtls$addressLine2"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.District">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$addressDtls$district"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    STYLE="cluster-cpr-no-internal-padding"
    TITLE="Cluster.Title.AdditionalSearchCriteria"
  >


    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY"
        PROPERTY="displayDistrictInd"
      />
    </CONDITION>


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


    <FIELD LABEL="Field.Label.City">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$addressDtls$city"
        />
      </CONNECT>
    </FIELD>


    <FIELD CONTROL="SKIP"/>


    <FIELD LABEL="Field.Label.AddressLineTwo">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$addressDtls$addressLine2"
        />
      </CONNECT>
    </FIELD>


    <CLUSTER
      LABEL_WIDTH="40"
      NUM_COLS="1"
      STYLE="cluster-cpr-no-border"
    >
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="displayRegionListInd"
        />
      </CONDITION>


      <FIELD
        LABEL="Field.Label.State"
        USE_BLANK="true"
        USE_DEFAULT="false"
        WIDTH="100"
      >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="code"
            NAME="DISPLAY"
            PROPERTY="description"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="key$addressDtls$regionOpt"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


  </CLUSTER>


</VIEW>
