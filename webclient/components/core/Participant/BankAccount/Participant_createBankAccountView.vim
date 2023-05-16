<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2002, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002-2004, 2011 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Included view to create a new bank account.                            -->
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
    CLASS="Participant"
    NAME="ACTION"
    OPERATION="createBankAccountWithTextSortCode"
    PHASE="ACTION"
  />
  <!-- BEGIN, CR00371769, VT -->
  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="DISPLAY"
    OPERATION="readIban"
  />
  <!-- END, CR00371769 -->


  <PAGE_PARAMETER NAME="concernRoleID"/>
  <PAGE_PARAMETER NAME="description"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="concernRoleID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    SHOW_LABELS="true"
  >
    <!-- BEGIN, CR00371769, VT -->
    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY"
        PROPERTY="ibanInd"
      />
    </CONDITION>
    <!-- END, CR00371769 -->


    <FIELD LABEL="Field.Label.AccountName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AccountNumber">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="accountNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.From">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Primary">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="primaryBankInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AccountType">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="typeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.BankBranchName"
      WIDTH="50"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$bankSortCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.To"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Joint">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="jointAccountInd"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>
  <!-- BEGIN, CR00371769, VT-->
  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    SHOW_LABELS="true"
  >


    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="ibanInd"
      />
    </CONDITION>


    <FIELD LABEL="Field.Label.AccountName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Iban">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$participantBankAccountDetails$ibanOpt"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AccountNumber">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="accountNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.From">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Primary">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="primaryBankInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AccountType">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="typeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Bic">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$bicOpt"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.BankBranchName"
      WIDTH="50"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$bankSortCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.To"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Joint">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="jointAccountInd"
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
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
