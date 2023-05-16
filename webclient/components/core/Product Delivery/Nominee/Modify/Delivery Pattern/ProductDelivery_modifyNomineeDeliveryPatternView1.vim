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
<!-- The included view to allow the user to modify the delivery pattern for -->
<!-- a case nominee.                                                        -->
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
    OPERATION="modifyCaseNomineeProdDelPattern"
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
    >
      <LINK
        PAGE_ID="ProductDelivery_listNomineeCurrentDelPattern"
        SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseNomineeID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseNomineeID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      IMAGE="CancelButton"
      LABEL="ActionControl.Label.Cancel"
    >
      <LINK
        PAGE_ID="ProductDelivery_listNomineeCurrentDelPattern"
        SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseNomineeID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseNomineeID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


  </ACTION_SET>


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="caseNomineeID"/>
  <PAGE_PARAMETER NAME="versionNo"/>
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
      PROPERTY="caseNomineeID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseNomineeID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="30"
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


  </CLUSTER>


</VIEW>
