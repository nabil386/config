<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2009, 2013. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2009 Curam Software Ltd.                                 -->
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


  <FIELD LABEL="Field.Label.NomineeName">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="nomineeName"
      />
    </CONNECT>
  </FIELD>


  <FIELD LABEL="Field.Label.Method">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="methodOfDelivery"
      />
    </CONNECT>
  </FIELD>


  <FIELD LABEL="Field.Label.BankSortCode">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="bankAccountSortCode"
      />
    </CONNECT>
    <LINK
      OPEN_MODAL="true"
      PAGE_ID="Organization_viewBankBranch"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bankBranchID"
        />
        <TARGET
          NAME="PAGE"
          PROPERTY="bankBranchID"
        />
      </CONNECT>
    </LINK>
  </FIELD>


  <FIELD LABEL="Field.Label.LedgerReference">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="ledgerNumber"
      />
    </CONNECT>
  </FIELD>


  <FIELD LABEL="Field.Label.NomineeAddress">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="formattedNomineeAddress"
      />
    </CONNECT>
  </FIELD>


  <FIELD LABEL="Field.Label.CheckNumber">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="chequeNumber"
      />
    </CONNECT>
  </FIELD>


  <!-- BEGIN, CR00378225, VT -->
  <FIELD LABEL="Field.Label.BankAccountRef">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="bankAccountNumberOpt"
      />
    </CONNECT>
  </FIELD>
  <!-- END, CR00378225 -->


  <FIELD LABEL="Field.Label.VoucherReference">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="voucherNumber"
      />
    </CONNECT>
  </FIELD>


</VIEW>
