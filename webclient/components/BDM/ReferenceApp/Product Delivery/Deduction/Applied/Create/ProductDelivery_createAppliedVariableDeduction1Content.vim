<?xml version="1.0" encoding="UTF-8"?>
<!-- Description -->
<!-- =========== -->
<!-- Create a variable applied deduction for a product delivery.            -->
<!-- Task 2396 and 5865 - Calculate Deduction Priorities and remove Deduction Priority Fields -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    ACTION_ID_PROPERTY="details$actionIDProperty"
    CLASS="BDMProductDelivery"
    NAME="ACTION"
    OPERATION="userActionVariableDeduction"
    PHASE="ACTION"
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
    CLASS="ProductDelivery"
    NAME="DEDUCTIONHEADER"
    OPERATION="readDeductionLiabilityDetails"
  />


  <SERVER_INTERFACE
    CLASS="BDMProductDelivery"
    NAME="DISPLAYDN"
    OPERATION="listAppliedDeductionName"
  />


  <SERVER_INTERFACE
    CLASS="ProductDelivery"
    NAME="LASTPAYMENTDATE"
    OPERATION="readLastPaymentDate"
  />


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


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>
  <PAGE_PARAMETER NAME="liabilityConcernRoleID"/>
  <PAGE_PARAMETER NAME="relatedCaseID"/>


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
      NAME="DISPLAYCOMPONENTS"
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
      PROPERTY="relatedCaseID"
    />
    <TARGET
      NAME="DEDUCTIONHEADER"
      PROPERTY="caseID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAYDN"
      PROPERTY="caseID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="deductionDtls$caseID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="relatedCaseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="relatedCaseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="liabilityConcernRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="liabilityConcernRoleID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Label.LiabilityParticipant">
      <CONNECT>
        <SOURCE
          NAME="DEDUCTIONHEADER"
          PROPERTY="liabilityParticipantName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.OriginalLiability">
      <CONNECT>
        <SOURCE
          NAME="DEDUCTIONHEADER"
          PROPERTY="originalLiabilityAmt"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Liability">
      <CONNECT>
        <SOURCE
          NAME="DEDUCTIONHEADER"
          PROPERTY="liabilityNameReference"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.OutstandingLiability">
      <CONNECT>
        <SOURCE
          NAME="DEDUCTIONHEADER"
          PROPERTY="outstandingLiabilityAmt"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    DESCRIPTION="Cluster.Title.Description"
    LABEL_WIDTH="22.5"
  >
    <FIELD
      LABEL="Field.Label.Nominee"
      USE_BLANK="true"
      WIDTH="81"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="caseNomineeID"
          NAME="DISPLAYNOMINEES"
          PROPERTY="nameAndDeliveryPattern"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="nomineeID"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Component"
      USE_BLANK="true"
      WIDTH="35"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="rulesObjectiveIDPopup"
          NAME="DISPLAYCOMPONENTS"
          PROPERTY="objectiveCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="rulesObjectiveID"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
  >
    <FIELD
      LABEL="Field.Label.DeductionName"
      USE_BLANK="true"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="deductionName"
          NAME="DISPLAYDN"
          PROPERTY="dtls$deductionName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="deductionName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.StartDate"
      USE_DEFAULT="false"
      WIDTH="50"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>



    <FIELD
      LABEL="Field.Label.Percentage"
      USE_DEFAULT="false"
      WIDTH="35"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="rate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.EndDate"
      USE_DEFAULT="false"
      WIDTH="50"
    >
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
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
