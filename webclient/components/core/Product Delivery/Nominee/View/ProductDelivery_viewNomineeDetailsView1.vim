<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2010, 2013. All Rights Reserved.

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
<!-- The included view to display the details of a case nominee.            -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="Case"
    NAME="DISPLAY"
    OPERATION="viewCaseNomineeDetails1"
  />

  <PAGE_PARAMETER NAME="caseNomineeID"/>
  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseNomineeID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseNomineeViewKey$caseNomineeID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
  >


    <FIELD LABEL="Field.Label.PreferredOffice">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="publicOfficeName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Currency">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="currencyType"
        />
      </CONNECT>
    </FIELD>


    <CONTAINER LABEL="Field.Label.DefaultNominee">
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="defaultNomInd"
          />
        </CONNECT>
      </FIELD>
      <ACTION_CONTROL 
        LABEL="ActionControl.Label.Set"
        APPEND_ELLIPSIS="false"
      >


        <CONDITION>
          <IS_FALSE
            NAME="DISPLAY"
            PROPERTY="defaultNomInd"
          />
        </CONDITION>


        <LINK
          OPEN_MODAL="true"
          PAGE_ID="ProductDelivery_resolveSetDefaultNominee"
          SAVE_LINK="true"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="caseNomineeDetails$caseNomineeID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseNomineeID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="caseNomineeDetails$caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="caseNomineeDetails$versionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="versionNo"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$caseNomineeContextDescription$description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="pageDescription"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    STYLE="nested-cluster-no-border"
  >
     <!-- BEGIN, CR00372248, GA -->
      <CONDITION>
      <IS_FALSE
        NAME="DISPLAY"
        PROPERTY="ibanIndOpt"
      />
      </CONDITION>
      <!-- ENd, CR00372248 -->
    <CLUSTER
      LABEL_WIDTH="40"
      NUM_COLS="1"
      STYLE="nested-cluster-left"
      TITLE="Cluster.Title.BankAccountDetails"
    >
      <FIELD LABEL="Field.Label.AccountName">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="bankAccountName"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.AccountType">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="bankAccountType"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.AccountNumber">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="bankAccountNumber"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.SortCode">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="bankSortCode"
          />
        </CONNECT>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="ProductDelivery_viewBankBranch"
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
      </FIELD>
    </CLUSTER>
    
    <CLUSTER
      SHOW_LABELS="false"
      STYLE="nested-cluster-right"
      TITLE="Cluster.Title.Address"
    >
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="addressData"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
  </CLUSTER>
 
 <!-- BEGIN, CR00372248, GA -->
 <CLUSTER
    NUM_COLS="2"
    STYLE="nested-cluster-no-border"
   >
     <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="ibanIndOpt"
      />
    </CONDITION>
    <CLUSTER
      LABEL_WIDTH="40"
      NUM_COLS="1"
      STYLE="nested-cluster-left"
      TITLE="Cluster.Title.BankAccountDetails"
    >
        
      <FIELD LABEL="Field.Label.AccountName">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="bankAccountName"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.AccountType">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="bankAccountType"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.AccountNumber">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="ibanOpt"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.SortCode">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="bankSortCode"
          />
        </CONNECT>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="ProductDelivery_viewBankBranch"
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
      </FIELD>
      <FIELD LABEL="Field.Label.Bic">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="bicOpt"
          />
        </CONNECT>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="ProductDelivery_viewBankBranch"
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
        
      </FIELD>
    </CLUSTER>
     
    <CLUSTER
      SHOW_LABELS="false"
      STYLE="nested-cluster-right"
      TITLE="Cluster.Title.Address"
    >
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="addressData"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
  </CLUSTER>
  <!-- END, CR00372248 -->


</VIEW>
