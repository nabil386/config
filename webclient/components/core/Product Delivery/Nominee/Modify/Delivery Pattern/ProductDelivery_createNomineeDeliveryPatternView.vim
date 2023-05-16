<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2010 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view to allow the user to add a delivery pattern for a    -->
<!-- case nominee.                                                          -->
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
    CLASS="Case"
    NAME="DISPLAY"
    OPERATION="listDeliveryPatternForCase"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="Case"
    NAME="ACTION"
    OPERATION="createCaseNomineeProdDelPattern"
    PHASE="ACTION"
  />


  <ACTION_SET
    ALIGNMENT="CENTER"
    TOP="false"
  >
    <ACTION_CONTROL
      IMAGE="SaveButton"
      LABEL="ActionControl.Label.Save"
      TYPE="SUBMIT"
    />
    
    <ACTION_CONTROL
      IMAGE="CancelButton"
      LABEL="ActionControl.Label.Cancel"
    />
  </ACTION_SET>


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="caseNomineeID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseNomineeID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseNomineeID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="1"
  >


    <FIELD
      LABEL="Field.Label.ProductDeliveryPatternName"
      USE_DEFAULT="false"
      WIDTH="25"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="productDeliveryPatternID"
          NAME="DISPLAY"
          PROPERTY="productDeliveryPatternName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="productDeliveryPatternID"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.FromDate"
      USE_DEFAULT="false"
      WIDTH="8"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fromDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.FromStartDateInd">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fromCaseStartDateInd"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
