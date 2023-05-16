<?xml version="1.0" encoding="UTF-8"?>
<!-- Description -->
<!-- =========== -->
<!-- The included view to display a list of financial instrcuctions for a   -->
<!-- product delivery on an integrated case.                                -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <!-- BEGIN, CR00443728, YF -->
  <LIST SUMMARY="FieldLabel.Transaction.Summary">
    <!-- END, CR00443728, YF -->
    <ACTION_SET TYPE="LIST_ROW_MENU">


      <ACTION_CONTROL
        IMAGE="ApproveButton"
        LABEL="ActionControl.Label.Approve"
      >


        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="approveInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Financial_approvePayment"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
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
              PROPERTY="description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="contextDescription"
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
            PROPERTY="cancelInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Financial_cancelPayment1"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="finInstructionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="finInstructionID"
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
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="effectiveDate"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="effectiveDate"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="ActionControl.Label.Invalidate">


        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="invalidateInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Financial_invalidatePayment"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="finInstructionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="finInstructionID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <!-- BEGIN, CR00369134, KRK -->
      <SEPARATOR/>
      <!-- END, CR00369134 -->


      <ACTION_CONTROL
        IMAGE="RegeneratePaymentButton"
        LABEL="ActionControl.Label.Reissue"
      >


        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="reissueInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Financial_regeneratePayment1"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="finInstructionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="finInstructionID"
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
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="effectiveDate"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="effectiveDate"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <!-- BEGIN, CR00369134, KRK -->
      <ACTION_CONTROL LABEL="ActionControl.Label.ReissueWithAppliedDeduction">


        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="reissueDeductionIndOpt"
          />
        </CONDITION>


        <LINK
          OPEN_MODAL="true"
          PAGE_ID="ProductDelivery_getParticipantLiabilityWizard"
        >
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
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
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
              PROPERTY="description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="pageDescription"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="ActionControl.Label.ReissueWithUnappliedDeduction">


        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="reissueDeductionIndOpt"
          />
        </CONDITION>


        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Financial_createUnappliedDeductionWizard"
        >
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
          <CONNECT>
            <SOURCE
              NAME="CONSTANT"
              PROPERTY="reissue.wizardStateID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="wizardStateID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
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
              PROPERTY="description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="pageDescription"
            />
          </CONNECT>


        </LINK>
      </ACTION_CONTROL>


      <SEPARATOR/>
      <!-- END, CR00369134 -->


      <!-- This action is called Reverse and is used to reverse a Liability. -->
      <ACTION_CONTROL
        IMAGE="ReverseButton"
        LABEL="ActionControl.Label.Reverse"
      >


        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="reverseInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Financial_createInstructionReversal"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="finInstructionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="finInstructionID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <!-- BEGIN, CR00328565, CSH -->
      <ACTION_CONTROL LABEL="ActionControl.Label.Allocate">
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="allocateInd"
          />
        </CONDITION>


        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Financial_allocatePaymentReceivedSelectLiability1"
          SAVE_LINK="true"
        >
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
              NAME="DISPLAY"
              PROPERTY="concernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="finInstructionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="finInstructionID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="ActionControl.Label.Refund">
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="refundInd"
          />
        </CONDITION>


        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Financial_createRefund"
          SAVE_LINK="true"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
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
              PROPERTY="unprocessedAmount"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="unallocatedAmount"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <!-- END, CR00328565 -->


    </ACTION_SET>


    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="26"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="typeStatus"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Nominee"
      WIDTH="26"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseNomineeName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Method"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="deliveryMethodType"
        />
      </CONNECT>
    </FIELD>

	<FIELD
      LABEL="Field.Label.ProcessedDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="effectiveDate"
        />
      </CONNECT>
    </FIELD>
	
 <FIELD
      LABEL="Field.Label.Status"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bdmStatusCode"
        />
      </CONNECT>
    </FIELD>
	<FIELD
      LABEL="Field.Label.DueDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dueDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.Amount"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="amount"
        />
      </CONNECT>
    </FIELD>


    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Financial_resolveInstructionView1">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="finInstructionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="instructionID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="typeCode"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="instructionType"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="instructionLineItemType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="instructionLineItemType"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="creditDebitType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="creditDebitType"
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


      </INLINE_PAGE>
    </DETAILS_ROW>


  </LIST>


</VIEW>
