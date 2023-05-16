<?xml version="1.0" encoding="UTF-8"?>
<!-- Description -->
<!-- =========== -->
<!-- The included view to display the reversal details of a financial       -->
<!-- instruction.                                                           -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CLUSTER
    LABEL_WIDTH="36"
    NUM_COLS="2"
    STYLE="cluster-cpr-no-border"
  >


    <FIELD LABEL="Field.Label.Reason">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reasonDescription"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.CancelationDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="closureEffectiveDate"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>

  <!-- BEGIN - Task 9889 - CK - List Historical Transactions View-->
  <CLUSTER
    NUM_COLS="1"
    STYLE="cluster-cpr-no-border"
  >
    <FIELD
      HEIGHT="5"
      LABEL="Field.Label.Comments"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reversalDetails$comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <!-- END - Task 9889 - CK - List Historical Transactions View-->

</VIEW>
