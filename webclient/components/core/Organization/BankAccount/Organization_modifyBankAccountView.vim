<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2003, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003-2004, 2010 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- View for the modify bank account pages. -->
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
    OPERATION="readBankAccount"
  />


  <SERVER_INTERFACE
    CLASS="Organization"
    NAME="ACTION"
    OPERATION="modifyBankAccountWithTextSortCode"
    PHASE="ACTION"
  />
  <!-- BEGIN, CR00371769, VT -->
  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="IBANINDICATOR"
    OPERATION="readIban"
  />
  <!-- END, CR00371769 -->


  <PAGE_PARAMETER NAME="bankAccountID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="bankAccountID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$readBankAccountKey$bankAccountID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="bankAccountID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="bankAccountID"
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
    TAB_ORDER="ROW"
  >
    <!-- BEGIN, CR00371769, VT -->
    <CONDITION>
      <IS_FALSE
        NAME="IBANINDICATOR"
        PROPERTY="ibanInd"
      />
    </CONDITION>
    <!-- END, CR00371769 -->


    <FIELD LABEL="Field.Label.AccountName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AccountType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="typeCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="typeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AccountNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="accountNumber"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="accountNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.BankBranchName">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="bankBranchName"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bankSortCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$bankSortCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.FromDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ToDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDate"
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


  <!-- BEGIN, CR00371769, VT -->
  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
    TAB_ORDER="ROW"
  >


    <CONDITION>
      <IS_TRUE
        NAME="IBANINDICATOR"
        PROPERTY="ibanInd"
      />
    </CONDITION>


    <FIELD LABEL="Field.Label.AccountName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AccountType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="typeCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="typeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Iban">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$orgBankAccountDetails$ibanOpt"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$maintainBankAccountDetails$ibanOpt"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Bic">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$orgBankAccountDetails$bicOpt"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$bicOpt"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AccountNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="accountNumber"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="accountNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.BankBranchName">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="bankBranchName"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bankSortCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$bankSortCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.FromDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ToDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDate"
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
  <!-- END, CR00371769 -->


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >


    <!-- BEGIN, CR00406866, VT -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00406866 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
