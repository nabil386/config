<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

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
<!-- Included view used to view the statement details of a reassessed       -->
<!--  payment on a benefit case.                                            -->
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


  <!-- BEGIN, CR00202877, CW -->
  <LIST PAGINATED="false">
  <!-- END, CR00202877 -->
  
    <DETAILS_ROW>


      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="multipleDecisionPeriodsInd"
        />
      </CONDITION>


      <INLINE_PAGE PAGE_ID="ProductDelivery_listFilteredPaymentPeriodDecisionList">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="reassessmentInfoID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="reassessmentInfoID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


    <ACTION_SET TYPE="LIST_ROW_MENU">

      <ACTION_CONTROL LABEL="ActionControl.Label.ShowDifferences">

        <CONDITION>
          <IS_FALSE
            NAME="DISPLAY"
            PROPERTY="multipleDecisionPeriodsInd"
          />
        </CONDITION>

        <LINK
          PAGE_ID="ProductDelivery_resolvePaymentPeriodDecisionView"
          SAVE_LINK="false"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="reassessmentInfoID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="reassessmentInfoID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      
    </ACTION_SET>


    <FIELD
      LABEL="Field.Label.CoversPeriod"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="coversPeriod"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Component"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rulesComponentTypeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.ActualAmount"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="actualAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.ReassessedAmount"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reassessedAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.DifferenceAmount"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="difference"
        />
      </CONNECT>
    </FIELD>


    <FOOTER_ROW>
      <FIELD CONTROL="SKIP"/>
      <FIELD CONTROL="SKIP"/>
      <FIELD CONTROL="SKIP"/>


      <FIELD LABEL="Field.Label.CorrectionAmount">
        <CONNECT>
          <SOURCE
            NAME="TEXT"
            PROPERTY="Field.Label.CorrectionAmount"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        ALIGNMENT="RIGHT"
        LABEL="Field.Label.CorrectionAmount"
        WIDTH="20"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$totalDifference"
          />
        </CONNECT>
      </FIELD>
    </FOOTER_ROW>


  </LIST>


</VIEW>
