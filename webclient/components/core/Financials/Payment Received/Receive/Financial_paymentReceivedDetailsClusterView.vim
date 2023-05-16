<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view used to allow the user to enter the details of the   -->
<!-- the payment received.                                                  -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
  >


    <FIELD
      LABEL="Field.Label.Amount"
      USE_DEFAULT="false"
      WIDTH="10"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="amount"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.EffectiveDate"
      WIDTH="35"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="effectiveDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Currency">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="currencyTypeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.LedgerNumber">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="ledgerNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.DateReceived"
      WIDTH="35"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="receivedDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ReceiptMethod"
      WIDTH="35"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="receiptMethodCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.CheckNumber">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="chequeNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ReferenceNumber"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="liabilityExternalRefNo"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
