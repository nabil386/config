<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2010, 2014. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright  2010 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view to display the details of a financial instruction.   -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="Financial"
    NAME="DISPLAY"
    OPERATION="readPaymentInstruction1"
  />


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


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >


    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="foreignCurrencyInd"
      />
    </CONDITION>


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


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentStatus"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Processed">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="datePosted"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ForeignCurrency">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="foreignCurrency"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.CoversPeriod">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$coverPeriod"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.PaymentDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentDate"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >


    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY"
        PROPERTY="foreignCurrencyInd"
      />
    </CONDITION>


    <CONTAINER LABEL="Field.Label.Amount">


      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="amount"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>


    <FIELD LABEL="Field.Label.Processed">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="datePosted"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentStatus"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.CoversPeriod">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$coverPeriod"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.PaymentDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentDate"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <!-- Conditionally Display the appropriate delivery method details. -->
  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.DeliveryDetails"
  >
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="chequeInd"
      />
    </CONDITION>


    <INCLUDE FILE_NAME="Financial_deliveryMethodChequeView.vim"/>
  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.DeliveryDetails"
  >
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="cashInd"
      />
    </CONDITION>


    <INCLUDE FILE_NAME="Financial_deliveryMethodCashView.vim"/>
  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.DeliveryDetails"
  >
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="voucherInd"
      />
    </CONDITION>


    <INCLUDE FILE_NAME="Financial_deliveryMethodVoucherView.vim"/>
  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.DeliveryDetails"
  >
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="EFTInd"
      />
    </CONDITION>


    <INCLUDE FILE_NAME="Financial_deliveryMethodEFTView.vim"/>
  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.DeliveryDetails"
  >
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="otherDeliveryMethodInd"
      />
    </CONDITION>


    <INCLUDE FILE_NAME="Financial_deliveryMethodAllView.vim"/>
  </CLUSTER>


  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>


  <CLUSTER
    NUM_COLS="1"
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >


    <!-- BEGIN, CR00416277, VT -->
    <FIELD LABEL="Field.Label.Comments">
      <!-- END, CR00416277 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$paymentHeaderDetails$comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <LIST TITLE="Cluster.Title.PaymentItems">


    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="processedFlag"
      />
    </CONDITION>


    <FIELD
      LABEL="Field.Label.CaseReference"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseDescription"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00385373, MR -->
    <FIELD
      LABEL="Field.Label.Component"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="instructLineItemTypeDescription"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00385373 -->


    <FIELD
      LABEL="Field.Label.CoversPeriod"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$paymentILIList$dtls$coverPeriod"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.Credit"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="debitAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.Debit"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="creditAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.Status"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$paymentILIList$dtls$statusCode"
        />
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>
