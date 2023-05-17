<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003-2008 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view to display the details of a  financial               -->
<!-- instruction.                                                           -->
<?curam-deprecated Since Curam 6.0, replaced by Financial_viewPaymentInstructionView1.vim?>
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="Financial"
    NAME="DISPLAY"
    OPERATION="readPaymentInstruction"
  />


  <ACTION_SET ALIGNMENT="CENTER">


    <ACTION_CONTROL
      IMAGE="ApprovePaymentButton"
      LABEL="ActionControl.Label.ApprovePayment"
    >
      <!-- BEGIN CR00096336 , DJ -->
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="processedFlag"
        />
      </CONDITION>
      <!-- END CR00096336 -->


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
      <!-- BEGIN CR00096336 , DJ -->
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="processedFlag"
        />
      </CONDITION>
      <!-- END CR00096336 -->


      <!-- BEGIN, CR00096349, DJ -->
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Financial_regeneratePayment"
        WINDOW_OPTIONS="width=500"
      >
        <!-- END, CR00096349 -->
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
            PROPERTY="result$caseID"
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
      <!-- BEGIN CR00096336 , DJ -->
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="processedFlag"
        />
      </CONDITION>
      <!-- END CR00096336 -->


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
  <!-- BEGIN CR00096336 , DJ -->
  <PAGE_PARAMETER NAME="caseDeductionItemID"/>
  <!-- END CR00096336 -->


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
    <!-- BEGIN CR00096336 , DJ -->
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="processedFlag"
      />
    </CONDITION>
    <!-- END CR00096336 -->


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
        <LINK
          OPEN_NEW="true"
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
        <LINK
          OPEN_NEW="true"
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


  <!-- BEGIN CR00103944 , DJ -->
  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="result$informationalMsgDtlsList$informationalMsgDtlsList$dtls$informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>


  <!-- END CR00103944 -->
  <!-- BEGIN CR00096336 , DJ -->
  <CLUSTER
    SHOW_LABELS="false"
    STYLE="outer-cluster-borderless"
  >


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
  <!-- END CR00096336 -->


  <LIST>
    <!-- BEGIN CR00096336 , DJ -->


    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="processedFlag"
      />
    </CONDITION>
    <!-- END CR00096336 -->


    <CONTAINER
      LABEL="Container.Label.Action"
      WIDTH="5"
    >
      <ACTION_CONTROL LABEL="ActionControl.Label.View">
        <LINK PAGE_ID="Financial_viewLineItem">
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
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


    <!-- BEGIN, CR00119778, ELG -->
    <FIELD
      LABEL="Field.Label.CaseReference"
      WIDTH="15"
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
            PROPERTY="dtls$caseID"
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
