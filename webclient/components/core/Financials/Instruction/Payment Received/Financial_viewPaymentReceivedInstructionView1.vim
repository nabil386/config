<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2010, 2012 Curam Software Ltd.                           -->
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


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
  >


    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="foreignCurrencyInd"
      />
    </CONDITION>


    <FIELD LABEL="Field.Label.UnallocatedAmount">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentReceivedHeaderDetails$unAllocatedAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Processed">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentReceivedHeaderDetails$effectiveDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ForeignCurrency">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentReceivedHeaderDetails$foreignCurrency"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentReceivedHeaderDetails$statusCode"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
  >


    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY"
        PROPERTY="foreignCurrencyInd"
      />
    </CONDITION>


    <FIELD LABEL="Field.Label.UnallocatedAmount">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentReceivedHeaderDetails$unAllocatedAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentReceivedHeaderDetails$statusCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Processed"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentReceivedHeaderDetails$effectiveDate"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <!-- Conditionally Display the appropriate delivery method details. -->
  <CLUSTER
    LABEL_WIDTH="35"
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
    LABEL_WIDTH="35"
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


  <!-- Conditionally Display the reversal details. -->
  <CLUSTER
    LABEL_WIDTH="17"
    NUM_COLS="1"
    TITLE="Cluster.Title.CancellationDetails"
  >
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="reversalInd"
      />
    </CONDITION>


    <INCLUDE FILE_NAME="Financial_reversalDetailsView.vim"/>
  </CLUSTER>


  <LIST TITLE="List.Title.Allocations">
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="allocationFlag"
      />
    </CONDITION>


    <FIELD
      LABEL="Field.Title.AllocationDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$paymentReceivedDetailsList$dtls$allocationDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.AllocatedTo"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$paymentReceivedDetailsList$dtls$allocatedTo"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.Component"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$paymentReceivedDetailsList$dtls$type"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.CoversPeriod"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$paymentReceivedDetailsList$dtls$coversPeriod"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Title.AllocatedAmount"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$paymentReceivedDetailsList$dtls$allocationAmount"
        />
      </CONNECT>
    </FIELD>

  </LIST>

  <!-- BEGIN, CR00324347, KH -->  
  <LIST TITLE="List.Title.Refunds">
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="refundFlag"
      />
    </CONDITION>

    <FIELD
      LABEL="Field.Title.RefundDate"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$refundList$dtls$effectiveDate"
        />
      </CONNECT>
    </FIELD>

    <FIELD
      LABEL="Field.Title.RefundStatus"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$refundList$dtls$statusCode"
        />
      </CONNECT>
    </FIELD>
        
    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Title.RefundAmount"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$refundList$dtls$amount"
        />
      </CONNECT>
    </FIELD>
    
  </LIST>
  <!-- END, CR00324347 -->


</VIEW>
