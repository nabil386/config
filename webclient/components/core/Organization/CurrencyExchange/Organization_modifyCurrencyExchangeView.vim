<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003, 2010 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This is the included view for the currency exchange modify pages. -->
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
    NAME="DISPLAY"
    OPERATION="readCurrencyExchange"
  />


  <SERVER_INTERFACE
    CLASS="Organization"
    NAME="ACTION"
    OPERATION="modifyCurrencyExchange"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="currencyExchangeID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="currencyExchangeID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="orgCurrencyExchangeKeyStruct$currencyExchangeID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="orgCurrencyExchangeKeyStruct$currencyExchangeID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="currencyExchangeID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="organisationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="organisationID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="currencyTypeCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="currencyTypeCode"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="statusCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="statusCode"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Label.Currency">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="currencyTypeCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.RateFromDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rateFromDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="rateFromDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Rate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="rate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.RateToDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rateToDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="rateToDate"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
