<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2004 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page, when included in another modify uim file, will allow the    -->
<!-- user to modify an existing Product Provider location.                  -->
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
    CLASS="ProductProvider"
    NAME="DISPLAY"
    OPERATION="readProductProviderLocationDetails"
    PHASE="DISPLAY"
  />
  <SERVER_INTERFACE
    CLASS="ProductProvider"
    NAME="ACTION"
    OPERATION="modifyProductProviderLocation"
    PHASE="ACTION"
  />
  <PAGE_PARAMETER NAME="providerLocationID"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="providerLocationID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="providerLocationKeyStruct$providerLocationID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="dtls$productProviderID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="productProviderID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="providerLocationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="providerLocationDetails$providerLocationID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="dtls$statusCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="statusCode"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="dtls$versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="dtls$addressID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="addressID"
    />
  </CONNECT>
  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
  >
    <FIELD
      LABEL="Field.Title.Name"
      WIDTH="100"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$name"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Title.Start">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$startDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD CONTROL="SKIP"/>
    <FIELD LABEL="Field.Title.End">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$endDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TAB_ORDER="ROW"
    TITLE="Cluster.Title.Address"
  >
    <FIELD
      LABEL="Field.Title.Address"
      WIDTH="90"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$addressData"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="addressData"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
