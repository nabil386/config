<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view to display the details of a financial instruction.   -->
<!-- Task 9889 - CK - List Historical Transactions View-->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

  <!-- BEGIN - Task 9889 - CK - List Historical Transactions View-->
  <SERVER_INTERFACE
    CLASS="BDMFinancial"
    NAME="DISPLAY"
    OPERATION="readPaymentInstruction1"
  />


  <PAGE_PARAMETER NAME="finInstructionID"/>
  <PAGE_PARAMETER NAME="contextName"/>
  <PAGE_PARAMETER NAME="caseDeductionItemID"/>

  <SERVER_INTERFACE
    CLASS="BDMFinancial"  
    NAME="DISPLAYHISTORICALTRANSACTIONS"
    OPERATION="readLinkedCancelledReversedPayments"
    PHASE="DISPLAY"
  />
  

  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="finInstructionID"
    />
    <TARGET
      NAME="DISPLAYHISTORICALTRANSACTIONS"
      PROPERTY="caseIDFinInstrIDKey$finInstructionID"
    />
  </CONNECT>    

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
  <!-- END - Task 9889 - CK - List Historical Transactions View-->

  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="2"
  >
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="foreignCurrencyInd"
      />
    </CONDITION>

    <!-- BEGIN - Task 9889 - CK - List Historical Transactions View-->
    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bdmFinInstrStatus"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Processed">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="effectiveDate"
        />
      </CONNECT>
    </FIELD>
    <!-- END - Task 9889 - CK - List Historical Transactions View-->

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
          PROPERTY="result$dtls$coverPeriod"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="2"
  >


    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY"
        PROPERTY="foreignCurrencyInd"
      />
    </CONDITION>


    <FIELD LABEL="Field.Label.Processed">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="effectiveDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Amount">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="amount"
        />
      </CONNECT>
    </FIELD>
    


    <FIELD LABEL="Field.Label.CoversPeriod">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$coverPeriod"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bdmFinInstrStatus"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <!-- Conditionally Display the appropriate delivery method details. -->
  <CLUSTER
    LABEL_WIDTH="30"
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
    
    <FIELD LABEL="Field.Label.ReferenceNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="referenceNumber"
        />
      </CONNECT>
    </FIELD>    
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="30"
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
    
    <FIELD LABEL="Field.Label.ReferenceNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="referenceNumber"
        />
      </CONNECT>
    </FIELD> 
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="30"
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
    
    <FIELD LABEL="Field.Label.ReferenceNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="referenceNumber"
        />
      </CONNECT>
    </FIELD>     
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="30"
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
    
    <FIELD LABEL="Field.Label.ReferenceNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="referenceNumber"
        />
      </CONNECT>
    </FIELD>     
  </CLUSTER>


  <!-- BEGIN, CR00372248, GA -->
  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="2"
    TITLE="Cluster.Title.DeliveryDetails"
  >
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="EFTWithIBANIndOpt"
      />
    </CONDITION>


    <INCLUDE FILE_NAME="Financial_deliveryMethodEFTWithIBANView.vim"/>
    
    <FIELD LABEL="Field.Label.ReferenceNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="referenceNumber"
        />
      </CONNECT>
    </FIELD>     
  </CLUSTER>
  <!-- END, CR00372248 -->


  <CLUSTER
    LABEL_WIDTH="30"
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
    
    <FIELD LABEL="Field.Label.ReferenceNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="referenceNumber"
        />
      </CONNECT>
    </FIELD>     
  </CLUSTER>


  <!-- Conditionally Display the reversal details. -->
  <CLUSTER
    LABEL_WIDTH="30"
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


  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>


  <LIST
    PAGINATED="false"
    TITLE="Cluster.Title.PaymentItems"
  >


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


    <FIELD
      LABEL="Field.Label.CoversPeriod"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentILIList$dtls$coverPeriod"
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
      LABEL="Field.Label.Status"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$statusCode"
        />
      </CONNECT>
    </FIELD>


  </LIST>

  <!-- BEGIN - Task 9889 - CK - List Historical Transactions View-->
  <INCLUDE FILE_NAME="BDMFinancial_viewHistoricalTransactions.vim"/>
  <!-- END - Task 9889 - CK - List Historical Transactions View-->  
</VIEW>
