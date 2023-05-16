<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2008, 2014. All Rights Reserved.

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
<!-- Views an ConcernRole Bank Account Snapshot.                            -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="DISPLAY"
    OPERATION="readBankAccount"
  />


  <PAGE_PARAMETER NAME="concernRoleBankAccountID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleBankAccountID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="readConcernRoleBankAcKey$concernRoleBankAccountID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="36"
    NUM_COLS="2"
    TAB_ORDER="ROW"
    TITLE="Cluster.Label.Details"
  >


    <FIELD LABEL="Field.Label.AccountName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
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
    </FIELD>


    <FIELD LABEL="Field.Label.AccountNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="accountNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.BankBranchName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bankBranchName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.BankName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bankName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Joint">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="jointAccountInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.From">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.To">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Primary">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="primaryBankInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AccountStatus">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bankAccountStatus"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="statusCode"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Label.Comments"
  >
    <!-- BEGIN, CR00416277, VT -->
    <FIELD LABEL="Field.Label.Comments">
      <!-- END, CR00416277 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>