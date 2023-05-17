<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2008, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view to display the details of a  financial               -->
<!-- instruction for an external party.                                     -->
<?curam-deprecated Since Curam 6.0, replaced by Participant_viewPaymentInstructionView.vim?>
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="ExternalParty"
    NAME="DISPLAY"
    OPERATION="readExternalPartyPaymentInstruction"
  />


  <ACTION_SET ALIGNMENT="CENTER">


    <ACTION_CONTROL
      IMAGE="ApprovePaymentButton"
      LABEL="ActionControl.Label.ApprovePayment"
    >
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="processedFlag"
        />
      </CONDITION>


      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Financial_approvePayment"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="finInstructionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="finInstructionID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="contextDescription"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="contextDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      IMAGE="RegeneratePaymentButton"
      LABEL="ActionControl.Label.RegeneratePayment"
    >
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="processedFlag"
        />
      </CONDITION>


      <LINK
        OPEN_MODAL="true"
        PAGE_ID="ExternalParty_regeneratePayment"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="finInstructionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="finInstructionID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="contextDescription"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="contextDescription"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      IMAGE="CancelPaymentButton"
      LABEL="ActionControl.Label.CancelPayment"
    >
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="processedFlag"
        />
      </CONDITION>


      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Financial_cancelPayment"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="finInstructionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="finInstructionID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="contextDescription"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="description"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


  </ACTION_SET>


  <PAGE_PARAMETER NAME="finInstructionID"/>
  <PAGE_PARAMETER NAME="contextName"/>
  <PAGE_PARAMETER NAME="caseDeductionItemID"/>


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


  <!-- BEGIN, CR00112389, ELG -->
  <CLUSTER
    SHOW_LABELS="false"
    STYLE="outer-cluster-borderless"
  >


    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="processedFlag"
      />
    </CONDITION>


    <CLUSTER
      NUM_COLS="2"
      TITLE="Cluster.Title.Details"
    >


      <CONDITION>
        <IS_FALSE
          NAME="DISPLAY"
          PROPERTY="canceledFlag"
        />
      </CONDITION>


      <FIELD LABEL="Field.Label.Name">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="paymentHeaderDetails$concernRoleName"
          />
        </CONNECT>
        <LINK PAGE_ID="Participant_resolveRoleHome">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="paymentHeaderDetails$concernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="paymentHeaderDetails$concernRoleType"
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
            PROPERTY="effectiveDate"
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
        <LINK PAGE_ID="ExternalParty_viewBankBranch">
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


      <FIELD LABEL="Field.Label.Invalidated">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="invalidatedInd"
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


      <CONTAINER LABEL="Field.Label.Amount">
        <FIELD>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="currencyType"
            />
          </CONNECT>
        </FIELD>
        <FIELD>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="amount"
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


    <CLUSTER
      NUM_COLS="2"
      TITLE="Cluster.Title.Details"
    >


      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="canceledFlag"
        />
      </CONDITION>


      <FIELD LABEL="Field.Label.Name">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="paymentHeaderDetails$concernRoleName"
          />
        </CONNECT>
        <LINK PAGE_ID="Participant_resolveRoleHome">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="paymentHeaderDetails$concernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="paymentHeaderDetails$concernRoleType"
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
            PROPERTY="effectiveDate"
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
        <LINK PAGE_ID="ExternalParty_viewBankBranch">
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


      <FIELD LABEL="Field.Label.Invalidated">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="invalidatedInd"
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


      <FIELD LABEL="Field.Label.CancelationReason">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="cancelationReasonCode"
          />
        </CONNECT>
      </FIELD>


      <CONTAINER LABEL="Field.Label.Amount">
        <FIELD>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="currencyType"
            />
          </CONNECT>
        </FIELD>
        <FIELD>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="amount"
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


  </CLUSTER>
  <!-- END, CR00112389 -->


  <CLUSTER
    SHOW_LABELS="false"
    STYLE="outer-cluster-borderless"
  >


    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY"
        PROPERTY="processedFlag"
      />
    </CONDITION>


    <FIELD>


      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="infoMessage"
        />
      </CONNECT>
    </FIELD>


    <ACTION_SET
      ALIGNMENT="CENTER"
      TOP="FALSE"
    >
      <ACTION_CONTROL
        IMAGE="CloseButton"
        LABEL="ActionControl.Label.Close"
      />
    </ACTION_SET>
  </CLUSTER>


  <LIST>


    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="processedFlag"
      />
    </CONDITION>


    <DETAILS_ROW>


      <INLINE_PAGE PAGE_ID="ExternalParty_viewLineItem">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="instructionLineItemID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="lineItemID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="contextName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="contextName"
          />
        </CONNECT>
      </INLINE_PAGE>


    </DETAILS_ROW>


    <!-- BEGIN, CR00119778, ELG -->
    <FIELD
      LABEL="Field.Label.CaseReference"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseReference"
        />
      </CONNECT>
      <LINK PAGE_ID="Case_resolveCaseHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ProductName"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$productName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$instructLineItemTypeDescription"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00119778 -->


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.Amount"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$debitAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.Credit"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$creditAmount"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
