<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010 Curam Software Ltd.                            					                 -->
<!-- All rights reserved.                                                                                   -->
<!-- This software is the confidential and proprietary information of Curam  -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose         -->
<!-- such Confidential Information and shall use it only in accordance with   -->
<!-- the terms of the license agreement you entered into with Curam          -->
<!-- Software.                                                                                                 -->
<!-- Description                                                                                               -->
<!-- ===========                                                                                   -->
<!-- This page allows a user to create criteria                                                -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.Title"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="SelectionQuery"
    NAME="ACTION"
    OPERATION="createCriteria"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="selectionQueryID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="selectionQueryID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="selectionQueryID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
  >


    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Value">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="value"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DisplayName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="displayName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DisplayValue">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="displayValue"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
