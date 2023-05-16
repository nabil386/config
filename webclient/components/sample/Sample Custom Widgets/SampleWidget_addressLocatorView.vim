<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- Included view used to locate a person's address on a map               -->
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
    CLASS="DisplayWidget"
    NAME="ACTION"
    OPERATION="getGoogleMapData"
    PHASE="ACTION"
  />


  <CLUSTER
    NUM_COLS="2"
    STYLE="outer-cluster-borderless"
  >
    <FIELD LABEL="Field.Label.SelectaPerson">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personID"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <ACTION_SET
    ALIGNMENT="CENTER"
    BOTTOM="false"
  >
    <ACTION_CONTROL
      DEFAULT="true"
      IMAGE="SearchButton"
      LABEL="ActionControl.Label.FindOnMap"
      TYPE="SUBMIT"
    >
      <LINK PAGE_ID="THIS"/>
    </ACTION_CONTROL>
  </ACTION_SET>


  <CLUSTER
    SHOW_LABELS="false"
    STYLE="outer-cluster-borderless"
  >
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$googleMapXML"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
