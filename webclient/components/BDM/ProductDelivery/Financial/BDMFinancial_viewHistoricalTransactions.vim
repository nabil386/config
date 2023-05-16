<?xml version="1.0" encoding="UTF-8"?>
<!-- Description -->
<!-- =========== -->
<!-- Task 9888 - CK - List Historical Transactions View-->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

  
  
  <LIST 
    PAGINATED="false"
    TITLE="Cluster.Title.HistoricalTransactions">
    
    
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="displayHistoricalTransactionsInd"
      />
    </CONDITION>
    
    <!--TODO: add status and processed date fields once Task 9888 is complete-->
    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="26"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAYHISTORICALTRANSACTIONS"
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
          NAME="DISPLAYHISTORICALTRANSACTIONS"
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
          NAME="DISPLAYHISTORICALTRANSACTIONS"
          PROPERTY="deliveryMethodType"
        />
      </CONNECT>
    </FIELD>

    <!-- BEGIN - CK - Task 10214 - 10215 - View Payments -->
    <FIELD
      LABEL="Field.Label.ProcessedDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAYHISTORICALTRANSACTIONS"
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
          NAME="DISPLAYHISTORICALTRANSACTIONS"
          PROPERTY="bdmStatusCode"
        />
      </CONNECT>
    </FIELD>
    <!-- END - CK - Task 10214 - 10215 - View Payments -->

    <FIELD
      LABEL="Field.Label.DueDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAYHISTORICALTRANSACTIONS"
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
          NAME="DISPLAYHISTORICALTRANSACTIONS"
          PROPERTY="amount"
        />
      </CONNECT>
    </FIELD>


    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Financial_resolveInstructionView1">
        <CONNECT>
          <SOURCE
            NAME="DISPLAYHISTORICALTRANSACTIONS"
            PROPERTY="finInstructDtls$finInstructionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="instructionID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAYHISTORICALTRANSACTIONS"
            PROPERTY="typeCode"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="instructionType"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAYHISTORICALTRANSACTIONS"
            PROPERTY="instructionLineItemType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="instructionLineItemType"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAYHISTORICALTRANSACTIONS"
            PROPERTY="creditDebitType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="creditDebitType"
          />
        </CONNECT>
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


      </INLINE_PAGE>
    </DETAILS_ROW>


  </LIST>


</VIEW>
