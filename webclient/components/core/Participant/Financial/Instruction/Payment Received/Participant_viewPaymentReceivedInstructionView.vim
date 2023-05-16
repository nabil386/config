<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

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
<!-- The included view to display the details of a payment received         -->
<!-- instruction.                                                           -->
<?curam-deprecated Since Curam 6.0, replaced with Financial_viewPaymentReceivedInstructionView1.vim?>
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
    NAME="DISPLAY"
    OPERATION="readPaymentReceivedInstruction"
  />


  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>


  <PAGE_PARAMETER NAME="finInstructionID"/>
  <PAGE_PARAMETER NAME="instructionLineItemType"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="finInstructionID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$finInstructionID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="instructionLineItemType"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="instructionLineItemType"
    />
  </CONNECT>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >


    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="processedFlag"
      />
    </CONDITION>


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentReceivedHeaderDetails$statusCode"
        />
      </CONNECT>
    </FIELD>


    <CONTAINER LABEL="Field.Label.Amount">
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="paymentReceivedHeaderDetails$currencyType"
          />
        </CONNECT>
      </FIELD>
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="paymentReceivedHeaderDetails$amount"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>


    <FIELD LABEL="Field.Label.ForeignCurrency">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentReceivedHeaderDetails$foreignCurrency"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DateReceived">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentReceivedHeaderDetails$receivedDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.CheckNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentReceivedHeaderDetails$chequeNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.BankAccountNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentReceivedHeaderDetails$bankAccountNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AccountName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentReceivedHeaderDetails$concernRoleName"
        />
      </CONNECT>
      <LINK
        PAGE_ID="Participant_resolveRoleHome"
        SAVE_LINK="true"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="paymentReceivedHeaderDetails$concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="paymentReceivedHeaderDetails$concernRoleType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="participantType"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <CONTAINER LABEL="Field.Label.UnallocatedAmount">
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="paymentReceivedHeaderDetails$currencyType"
          />
        </CONNECT>
      </FIELD>
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="paymentReceivedHeaderDetails$unAllocatedAmount"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>


    <FIELD LABEL="Field.Label.PaymentMethod">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentReceivedHeaderDetails$methodOfDelivery"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EffectiveDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentReceivedHeaderDetails$effectiveDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.LedgerReference">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentReceivedHeaderDetails$ledgerNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.BankSortCode">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentReceivedHeaderDetails$bankAccountSortCode"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.IssuerDetails"
  >
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="processedFlag"
      />
    </CONDITION>


    <FIELD LABEL="Field.Label.IssuerName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentReceivedHeaderDetails$issuerName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.IssuerAddress">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentReceivedHeaderDetails$issuerAddress"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <LIST>
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="processedFlag"
      />
    </CONDITION>


    <DETAILS_ROW>


      <INLINE_PAGE PAGE_ID="Participant_resolveInstructionView">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="dtls$finInstructionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="instructionID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="dtls$category"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="instructionType"
          />
        </CONNECT>
      </INLINE_PAGE>


    </DETAILS_ROW>


    <FIELD
      LABEL="Field.Title.Type"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$category"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.EffectiveDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$effectiveDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.Status"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$statusCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.AllocationDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$allocationDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Currency"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$currencyType"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Title.AllocatedAmount"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$allocationAmount"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
