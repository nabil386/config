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
<!-- Description -->
<!-- =========== -->
<!-- This page is used to display the delivery method details of a          -->
<!-- financial instruction.                                                 -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
  >
  
  <CONTAINER LABEL="Field.Label.Amount">
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="liabilityHeaderDetails$currencyType"
        />
      </CONNECT>
    </FIELD>
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$totalOutstanding"
        />
      </CONNECT>
    </FIELD>
  </CONTAINER>
  
  <FIELD LABEL="Field.Label.CoversPeriod">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="result$coversPeriod"
      />
    </CONNECT>
  </FIELD>
  
  <FIELD LABEL="Field.Label.EffectiveDate">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="liabilityHeaderDetails$effectiveDate"
      />
    </CONNECT>
  </FIELD>
  
  <FIELD LABEL="Field.Label.Status">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="liabilityHeaderDetails$statusCode"
      />
    </CONNECT>
  </FIELD>
  
  <FIELD LABEL="Field.Label.NomineeName">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="nomineeName"
      />
    </CONNECT>
  </FIELD>
  
  <FIELD LABEL="Field.Label.NomineeAddress">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="nomineeAddress"
      />
    </CONNECT>
  </FIELD>
  
  <FIELD LABEL="Field.Label.PaymentMethod">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="deliveryMethod"
      />
    </CONNECT>
  </FIELD>
  
  <!--FIELD LABEL="Field.Label.VoucherReference">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="voucherNumber"
      />
    </CONNECT>
  </FIELD-->
  
</VIEW>
