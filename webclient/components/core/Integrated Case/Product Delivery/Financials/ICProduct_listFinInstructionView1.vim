<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2009-2010 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view to display a list of financial instrcuctions for a   -->
<!-- product delivery on an integrated case.                                -->
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
    CLASS="ProductDelivery"
    NAME="DISPLAY"
    OPERATION="listCaseFinancialInstruction"
  />


  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$caseID"
    />
  </CONNECT>


  <LIST>
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
              NAME="DISPLAY"
              PROPERTY="result$caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="caseNomineeID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseNomineeID"
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
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="caseNomineeName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="nomineeName"
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
              NAME="DISPLAY"
              PROPERTY="result$caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="caseNomineeID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseNomineeID"
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
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="caseNomineeName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="nomineeName"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


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
    </ACTION_SET>


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


    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="35"
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
      WIDTH="20"
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
