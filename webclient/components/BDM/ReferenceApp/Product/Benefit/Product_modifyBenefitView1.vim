<?xml version="1.0" encoding="UTF-8"?>
<!-- Description -->
<!-- =========== -->
<!-- This page is used to modify a benefit product. -->
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
    CLASS="BDMProduct"
    NAME="DISPLAY"
    OPERATION="readBDMBenefitProduct1"
  />


  <SERVER_INTERFACE
    CLASS="BDMProduct"
    NAME="ACTION"
    OPERATION="modifyBDMBenefitProduct1"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="productID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="productID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$productID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="productID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="key$productID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="benefitHomePageDetails$creationDate"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="creationDate"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="benefitHomePageDetails$versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="benefitHomePageDetails$statusCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="statusCode"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="benefitHomePageDetails$benefitInd"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="benefitInd"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="benefitHomePageDetails$dateListType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="dateListType"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="benefitHomePageDetails$coverPeriodType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="coverPeriodType"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$name"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>

   <FIELD LABEL="Field.Label.SpsProductCode">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="spsProductCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="spsProductCode"
        />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.Label.StartDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$startDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Language">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$languageCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="languageCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.MaxDeliveryPeriod"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$deliveryMaxPeriod"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="deliveryMaxPeriod"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.CaseHomePage">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$caseHomePageName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="caseHomePageName"
        />
      </CONNECT>
    </FIELD>


<FIELD CONTROL="SKIP"/>
<FIELD CONTROL="SKIP"/>

<FIELD LABEL="Field.Label.SpsCdoCode">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="spsCdoCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="spsCdoCode"
        />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.Label.EndDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$endDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ReviewFrequency">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$reviewFrequency"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="reviewFrequency"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.SupplierReturns">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$orderProductInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="orderProductInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Type">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$typeCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="typeCode"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00283741, KOH -->
    <FIELD LABEL="Field.Label.Appealable">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$appealableIndOpt"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="appealableIndOpt"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00283741 -->
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TITLE="Cluster.Label.EligibleParticipants"
  >
    <FIELD LABEL="Field.Label.Person">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$personInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personInd"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Employer">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$employerInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="employerInd"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Utility">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$utilityInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="utilityInd"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.InformationProvider">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$informationProviderInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="informationProviderInd"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ServiceSupplier">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$serviceSupplierInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="serviceSupplierInd"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ProductProvider">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$productProviderInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="productProviderInd"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TITLE="Cluster.Label.DisplayDetails"
  >
    <FIELD LABEL="Field.Label.MyCasesFilter">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$myCasesFilterInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="myCasesFilterInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.CaseSearchFilter">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$caseSearchFilterInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="caseSearchFilterInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ProgramList">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$programListInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="programListInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.CitizenSelfService">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$citizenWorkspaceInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="citizenWorkspaceInd"
        />
      </CONNECT>
    </FIELD>


    <!--BEGIN, CR00213628, PB-->
    <!--BEGIN, CR00205591, PB-->
    <FIELD LABEL="Field.Label.TranslationRequired">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$adminTranslationRequiredInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="adminTranslationRequiredInd"
        />
      </CONNECT>
    </FIELD>
    <!--END, CR00205591-->
    <!--END, CR00213628-->
    <FIELD LABEL="Field.Title.OwnershipStrategy">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="result$dtls$benefitHomePageDetails$ownershipStrategyName"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$benefitHomePageDetails$ownershipStrategyName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$dtls$ownershipStrategyName"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Label.Financial"
  >
    <FIELD LABEL="Field.Label.AdjustmentRequired">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$adjustmentInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="adjustmentInd"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.MaxDeductionRate"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$maxDeductionRate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="maxDeductionRate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.MinDeductionAmount"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$minDeductionAmount"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="minDeductionAmount"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.AdjustmentFrequency">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$adjustmentFrequency"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="adjustmentFrequency"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.MinPaymentAmount"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$minimumPmtAmt"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="minimumPmtAmt"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TITLE="Cluster.Label.Cost"
  >
    <FIELD
      LABEL="Field.Label.FixedCost"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$unitCost"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="unitCost"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Calculated">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$calculateCostInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="calculateCostInd"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TITLE="Cluster.Label.UnderOverCaseProcessing"
  >
    <FIELD LABEL="Field.Label.AutoUnderPaymentCase">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$autoUnderpaymentCaseInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="autoUnderpaymentCaseInd"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.OverPaymentProcessing">
      <!-- BEGIN, CR00230245, CW -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$reassessmentActionType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="reassessmentActionType"
        />
      </CONNECT>
      <!-- END, CR00230245 -->
    </FIELD>
    <FIELD LABEL="Field.Label.RolledUpReassessmentProducts">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$rolledUpReassessment"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="rolledUpReassessment"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <!-- BEGIN, CR00242359, KH -->
  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TITLE="Cluster.Title.Rules"
  >
    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY"
        PROPERTY="benefitHomePageDetails$cerProductInd"
      />
    </CONDITION>
    <FIELD
      LABEL="Field.Label.DateList"
      WIDTH="80"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$dateListType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dateListType"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.RerateFrequency">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$rerateFrequency"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="rerateFrequency"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <!-- END, CR00242359 -->


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TITLE="Cluster.Label.Certification"
  >
    <FIELD LABEL="Field.Label.CertificationRequired">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$certificationRequiredInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="certificationRequiredInd"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.CertificationFrequency">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$certificationFrequency"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="certificationFrequency"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.GracePeriod"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$certGracePeriod"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="certGracePeriod"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TITLE="Cluster.Label.SecurityRestrictions"
  >
    <FIELD LABEL="Field.Label.CreateRights">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$createSecurity"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$createSecurity"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="createSecurity"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ReadRights">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$readSecurity"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$readSecurity"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="readSecurity"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ApproveRights">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$approveSecurity"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$approveSecurity"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="approveSecurity"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.MaintainRights">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$maintainSecurity"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$maintainSecurity"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="maintainSecurity"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Label.Description"
  >
    <!-- BEGIN, CR00463142, EC -->
    <FIELD
      HEIGHT="4"
      LABEL="Cluster.Label.Description"
    >
      <!-- END, CR00463142 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="benefitHomePageDetails$description"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="description"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
