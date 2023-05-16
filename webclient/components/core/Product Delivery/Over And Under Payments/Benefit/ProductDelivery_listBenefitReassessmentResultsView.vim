<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012, 2015. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2010 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Included view used to list the results of a reassessment of a case.    -->
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
    OPERATION="listReassessmentResults"
  />


  <!-- BEGIN, CR00202877, CW -->
  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>
  <!-- END, CR00202877 -->


  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
  </CONNECT>


  <!-- BEGIN, CR00463142, EC -->
  <LIST SUMMARY="Summary.OverUnderPayment.Details">
    <!-- END, CR00463142 -->


    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="ProductDelivery_listBenefitReassessmentResultsResolveDetailsRow">
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
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="nomineeOverUnderPaymentID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="nomineeOverUnderPaymentID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="multipleObjectivesInd"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="multipleObjectivesInd"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reassessmentResultType"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00239173, CW -->
    <FIELD
      LABEL="Field.Label.OverUnderPaymentCaseReference"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="overUnderPaymentCaseReference"
        />
      </CONNECT>
      <LINK PAGE_ID="Case_resolveCaseHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="overUnderPaymentCaseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <!-- END, CR00239173 -->


    <FIELD
      LABEL="Field.Label.CoversPeriod"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="coversPeriod"
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
          PROPERTY="nomineeName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ReassessmentDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reassessmentDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.Amount"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="amount"
        />
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>
