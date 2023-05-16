<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for modifying a third party case deduction record.   -->
<!-- Task 2396 and 5865 - Calculate Deduction Priorities and remove Deduction Priority Fields -->
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
    OPERATION="readDeduction1"
  />


  <SERVER_INTERFACE
    CLASS="ProductDelivery"
    NAME="LASTPAYMENTDATE"
    OPERATION="readLastPaymentDate"
  />


  <SERVER_INTERFACE
    CLASS="Case"
    NAME="DISPLAYNOMINEES"
    OPERATION="listOperationalCaseNomineeDetails"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="Case"
    NAME="DISPLAYCOMPONENTS"
    OPERATION="listCaseObjectiveDetails"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="BDMProductDelivery"
    NAME="ACTION"
    OPERATION="modifyThirdPartyDeductionItem"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="caseID"/>


  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="LASTPAYMENTDATE"
        PROPERTY="informationMsgTxt"
      />
    </CONNECT>
    <CONNECT>
      <SOURCE
        NAME="ACTION"
        PROPERTY="informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="LASTPAYMENTDATE"
      PROPERTY="caseID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAYNOMINEES"
      PROPERTY="caseID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAYCOMPONENTS"
      PROPERTY="caseID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseDeductionItemID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$caseDeductionItemID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseDeductionItemID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseDeductionItemID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="relatedCaseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="relatedCaseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="statusCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="statusCode"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="thirdPartyConcernRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="thirdPartyConcernRoleID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="concernRoleID"
    />
  </CONNECT>
  <!-- BEGIN - CK - Task 9830 - Autoset Third Party Details -->
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="customerAccNumber"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="customerAccNumber"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="customerAccName"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="customerAccName"
    />
  </CONNECT>
  <!-- END - CK - Task 9830 - Autoset Third Party Details -->

  <CLUSTER NUM_COLS="2">
    <FIELD
      LABEL="Field.Label.Name"
      USE_BLANK="true"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="deductionName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ThirdParty">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="thirdPartyConcernRoleName"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER LABEL_WIDTH="25">
    <FIELD
      LABEL="Field.Label.Nominee"
      USE_BLANK="true"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="caseNomineeID"
          NAME="DISPLAYNOMINEES"
          PROPERTY="nameAndDeliveryPattern"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="nomineeIDPopup"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="nomineeIDPopup"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Component"
      USE_BLANK="true"
      WIDTH="33"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="rulesObjectiveIDPopup"
          NAME="DISPLAYCOMPONENTS"
          PROPERTY="objectiveCode"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rulesObjectiveIDPopup"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="rulesObjectiveIDPopup"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER NUM_COLS="2">
    <FIELD
      LABEL="Field.Label.Amount"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="amount"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="amount"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.StartDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Percentage"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="rate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.EndDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00406866, VT -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00406866 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
