<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
 
  Copyright IBM Corporation 2012, 2016. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2009-2011 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page is used to modify a liability product. -->
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
    CLASS="Product"
    NAME="DISPLAY"
    OPERATION="readLiabilityProduct1"
  />


  <SERVER_INTERFACE
    CLASS="Product"
    NAME="ACTION"
    OPERATION="modifyLiabilityProduct1"
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
      PROPERTY="creationDate"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="creationDate"
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
      PROPERTY="benefitInd"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="benefitInd"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="dateListType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="dateListType"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="coverPeriodType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="coverPeriodType"
    />
  </CONNECT>


  <CLUSTER NUM_COLS="2">


    <!-- BEGIN, CR00283741, KOH -->
    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="name"
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
    <FIELD LABEL="Field.Label.Language">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="languageCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="languageCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.CaseHomePage">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseHomePageName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="caseHomePageName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Type">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="typeCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="typeCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ProgramList">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="programListInd"
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
          PROPERTY="citizenWorkspaceInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="citizenWorkspaceInd"
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
    <FIELD LABEL="Field.Title.OwnershipStrategy">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="result$liabilityProductDetails$ownershipStrategyName"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$liabilityProductDetails$ownershipStrategyName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$ownershipStrategyName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ReviewFrequency">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reviewFrequency"
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
          PROPERTY="orderProductInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="orderProductInd"
        />
      </CONNECT>
    </FIELD>
    <!--BEGIN, CR00205591, PB-->
    <FIELD LABEL="Field.Label.TranslationRequired">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="adminTranslationRequiredInd"
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
    <FIELD LABEL="Field.Label.MyCasesFilter">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="myCasesFilterInd"
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
          PROPERTY="caseSearchFilterInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="caseSearchFilterInd"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Appealable">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="appealableIndOpt"
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
          PROPERTY="liabilityProductDetails$description"
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


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Label.Financial"
  >
    <FIELD LABEL="Field.Label.OverPaymentProcessing">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="overUnderPaymentAutoProcessInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="overUnderPaymentAutoProcessInd"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.AdjustmentRequired">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="adjustmentInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="adjustmentInd"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.OverAllocation">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="overAllocationInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="overAllocationInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Normal">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="liabilityReassessmentNormalInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="liabilityReassessmentNormalInd"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.AdjustmentFrequency">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="adjustmentFrequency"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="adjustmentFrequency"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    STYLE="outer-cluster-borderless"
  >
    <FIELD
      LABEL="Field.Label.FixedCost"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="unitCost"
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
          PROPERTY="calculateCostInd"
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


  <!-- BEGIN, CR00242359, KH -->
  <CLUSTER
    NUM_COLS="2"
    STYLE="outer-cluster-borderless"
  >


    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY"
        PROPERTY="cerProductInd"
      />
    </CONDITION>


    <FIELD
      LABEL="Field.Label.DateList"
      WIDTH="80"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateListType"
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
          PROPERTY="rerateFrequency"
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
    LABEL_WIDTH="45"
    NUM_COLS="2"
    TITLE="Cluster.Label.Certification"
  >
    <FIELD LABEL="Field.Label.CertificationRequired">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="certificationRequiredInd"
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
          PROPERTY="certificationFrequency"
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
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="certGracePeriod"
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
    LABEL_WIDTH="55"
    NUM_COLS="3"
    TITLE="Cluster.Label.EligibleParticipants"
  >
    <FIELD LABEL="Field.Label.Person">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="personInd"
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
          PROPERTY="employerInd"
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
          PROPERTY="utilityInd"
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
          PROPERTY="informationProviderInd"
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
          PROPERTY="serviceSupplierInd"
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
          PROPERTY="productProviderInd"
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
    NUM_COLS="2"
    TITLE="Cluster.Label.SecurityRestrictions"
  >
    <FIELD
      LABEL="Field.Label.LocationSecurityLevel"
      USE_BLANK="true"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="locationSecurityLevel"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="locationSecurityLevel"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.CreateRights"
      WIDTH="75"
    >
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="createSecurity"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="createSecurity"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="createSecurity"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.ReadRights"
      WIDTH="75"
    >
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="readSecurity"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="readSecurity"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="readSecurity"
        />
      </CONNECT>
    </FIELD>


    <FIELD CONTROL="SKIP"/>
    <FIELD
      LABEL="Field.Label.ApproveRights"
      WIDTH="75"
    >
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="approveSecurity"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="approveSecurity"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="approveSecurity"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.MaintainRights"
      WIDTH="75"
    >
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="maintainSecurity"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="maintainSecurity"
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


</VIEW>
