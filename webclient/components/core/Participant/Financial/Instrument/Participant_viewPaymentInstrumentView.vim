<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2009 - 2010 Curam Software Ltd.                          -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view to display the details of a financial instrument.    -->
<?curam-deprecated Since Curam 6.0, replaced by Financial_viewPaymentInstrumentView1.vim?>
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
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="result$result$paymentDetails$paymentHeader$concernRoleName"
      />
    </CONNECT>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.Separator"
      />
    </CONNECT>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="result$result$paymentDetails$paymentHeader$effectiveDate"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="Financial"
    NAME="DISPLAY"
    OPERATION="readPaymentDetails"
  />


  <PAGE_PARAMETER NAME="pmtInstrumentID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="pmtInstrumentID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$pmtInstrumentID"
    />
  </CONNECT>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >


    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$result$paymentDetails$paymentHeader$concernRoleName"
        />
      </CONNECT>
      <LINK PAGE_ID="Participant_resolveRoleHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$result$paymentDetails$paymentHeader$concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$result$paymentDetails$paymentHeader$concernRoleType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="participantType"
          />
        </CONNECT>
      </LINK>
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


    <FIELD LABEL="Field.Label.CreationDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="datePosted"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EffectiveDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$result$paymentDetails$paymentHeader$effectiveDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.BankAccountNo">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bankAccountNumber"
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


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$result$paymentDetails$paymentHeader$statusCode"
        />
      </CONNECT>
    </FIELD>


    <CONTAINER LABEL="Field.Label.Amount">
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$result$paymentDetails$paymentHeader$currencyType"
          />
        </CONNECT>
      </FIELD>
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$result$paymentDetails$paymentHeader$amount"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>


    <FIELD LABEL="Field.Label.ForeignCurrency">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="foreignCurrency"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.PaymentMethod">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="methodOfDelivery"
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


    <FIELD LABEL="Field.Label.VoucherReference">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="voucherNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.LedgerReference">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="ledgerNumber"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <LIST>


    <DETAILS_ROW>


      <INLINE_PAGE PAGE_ID="Participant_viewNonClientPaymentInstruction">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$result$instructionList$dtlsList$dtls$finInstructionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="finInstructionID"
          />
        </CONNECT>
      </INLINE_PAGE>


    </DETAILS_ROW>


    <FIELD
      ALIGNMENT="LEFT"
      LABEL="Field.Label.Status"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$result$instructionList$dtlsList$dtls$statusCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.EffectiveDate"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$result$instructionList$dtlsList$dtls$effectiveDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Currency"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$result$instructionList$dtlsList$dtls$currencyTypeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.Debit"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$result$instructionList$dtlsList$dtls$amount"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
