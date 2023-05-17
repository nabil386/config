<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2008, 2014. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008-2011 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- This page allows the user to view the fund fiscal year.                -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <!-- BEGIN, CR00186204, GP -->
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText1"
      />
    </CONNECT>
  </PAGE_TITLE>
  <!-- END, CR00186204 -->


  <SERVER_INTERFACE
    CLASS="MaintainFundFiscalYear"
    NAME="DISPLAY"
    OPERATION="readFundHomeForFiscalYear"
  />


  <SERVER_INTERFACE
    CLASS="MaintainFundFiscalYearLineItem"
    NAME="DISPLAY1"
    OPERATION="listLineItems"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="fiscalYearFundID"/>
  <PAGE_PARAMETER NAME="fundName"/>
  <PAGE_PARAMETER NAME="fiscalYearName"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="fiscalYearFundID"
    />
    <TARGET
      NAME="DISPLAY1"
      PROPERTY="key$fundFiscalYearID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="fiscalYearFundID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$fundFiscalYearID"
    />
  </CONNECT>


  <!-- BEGIN, CR00227014, PS -->
  <CLUSTER NUM_COLS="2">
    <!-- END, CR00227014 -->
    <!-- BEGIN, CR00199417, RD -->
    <FIELD LABEL="Field.Label.FundName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fundName"
        />
      </CONNECT>
      <!-- BEGIN, CR00227014, PS -->
      <LINK PAGE_ID="FundPM_viewFundHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="programFundID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="fundIDParam"
          />
        </CONNECT>
      </LINK>
      <!-- END, CR00227014 -->
    </FIELD>
    <FIELD LABEL="Field.Label.BudgetedTotal">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="allocatedTotal"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.RemainingBudgetedBalance">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="remBudgetedBalance"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.RemainingActualBalance">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="remainingActualBalance"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.FiscalYearName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="yearName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ObligatedTotal">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="totalObligatedAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.PaymentTotal">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="paymentTotal"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.FundFiscalYearStatus">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="status"
        />
      </CONNECT>
    </FIELD>
    <!--  END, CR00199417 -->
  </CLUSTER>


  <!-- BEGIN, CR00261860, GP -->
  <LIST TITLE="Cluster.Title.FundFiscalYearLineItems">
    <!-- END, CR00261860 -->


    <!-- BEGIN, CR00207190, PS -->
    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <!-- BEGIN, CR00198414, AS -->
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="FundPM_modifyFundFiscalYearLineItem"
          WINDOW_OPTIONS="width=625"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="fiscalYearFundID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="fiscalYearFundID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY1"
              PROPERTY="fundFclYrLineItemID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="fundFiscalYearLineItemID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY1"
              PROPERTY="versionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="versionNo"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="fundName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="fundName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="fiscalYearName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="fiscalYearName"
            />
          </CONNECT>
        </LINK>
        <!-- END, CR00198414 -->
      </ACTION_CONTROL>
      <!-- BEGIN, CR00207915, PS -->
      <ACTION_CONTROL LABEL="ActionControl.Label.NewBudgetAdjustment">
        <!-- BEGIN, CR00227014, PS -->
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="FundPM_createBalanceLineItemDetails"
          WINDOW_OPTIONS="width=625"
        >
          <!-- END, CR00227014 -->
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="fiscalYearFundID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="fiscalYearFundID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="fundName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="fundName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="fiscalYearName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="fiscalYearName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY1"
              PROPERTY="fundFclYrLineItemID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="fundFiscalYearLineItemID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <!-- END, CR00207915 -->
      <ACTION_CONTROL
        LABEL="ActionControl.Label.Delete"
        TYPE="ACTION"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="FundPM_deleteFundFiscalYearLineItem"
          WINDOW_OPTIONS="width=450"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="fiscalYearFundID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="fiscalYearFundID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY1"
              PROPERTY="fundFclYrLineItemID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="fundFiscalYearLineItemID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY1"
              PROPERTY="versionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="versionNo"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="fundName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="fundName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="fiscalYearName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="fiscalYearName"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>
    <!-- END, CR00207190 -->
    <!-- BEGIN, CR00261860, GP -->
    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY1"
          PROPERTY="type"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.BudgetedTotal"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY1"
          PROPERTY="budgetAllocatedTotal"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.PaymentTotal"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY1"
          PROPERTY="paymentTotal"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00261860 -->
    <!-- BEGIN, CR00207190, PS -->
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="FundPM_viewFundFiscalYearLineItem">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY1"
            PROPERTY="fundFclYrLineItemID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="fundFiscalYearLineItemID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="fiscalYearFundID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="fiscalYearFundID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="fundName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="fundName"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="fiscalYearName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="fiscalYearName"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>
    <!-- END, CR00207190 -->


    <!-- BEGIN, CR00261860, GP -->
  </LIST>
  <!-- END, CR00261860 -->


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
     BEGIN, CR00426143, GK --&gt;
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
      WIDTH="100"
      WIDTH_UNITS="PERCENT"
    >
      <!-- END, CR00426143 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
