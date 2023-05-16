<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2006 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for canceling a payment instrument.                  -->
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
    CLASS="Financial"
    NAME="ACTION"
    OPERATION="cancelPaymentInstrument"
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


  <PAGE_PARAMETER NAME="pmtInstrumentID"/>
  <PAGE_PARAMETER NAME="concernRoleName"/>
  <PAGE_PARAMETER NAME="effectiveDate"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="pmtInstrumentID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="pmtInstrumentID"
    />
  </CONNECT>


  <CLUSTER LABEL_WIDTH="30">


    <FIELD
      LABEL="Field.Label.Reason"
      WIDTH="65"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="reasonCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Regenerate">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="regenerateInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="reasonText"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
