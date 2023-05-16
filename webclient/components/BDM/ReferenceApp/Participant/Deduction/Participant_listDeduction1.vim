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
<!-- Description -->
<!-- =========== -->
<!-- This page allows the user to view a list of deductions for that user. -->
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
    CLASS="Participant"
    NAME="DISPLAY"
    OPERATION="listDeduction1"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="concernRoleID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="concernRoleID"
    />
  </CONNECT>


  <CLUSTER
    SHOW_LABELS="false"
    STYLE="tab-renderer"
  >


    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="statusInd"
      />
    </CONDITION>


    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="data"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <LIST>

<!-- Bug#28448. Deductions should only be editable from PDC Deduction, Manual Overpayment Flow, Outstanding Balance (CRA SetOff) flow or DOJ Interface.  -->
      <!-- 
    <ACTION_SET TYPE="LIST_ROW_MENU">

	<ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="ProductDelivery_resolveModifyDeductionView1"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
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
              PROPERTY="caseDeductionItemID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseDeductionItemID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="category"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="category"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="description"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>  -->


    <DETAILS_ROW>


      <INLINE_PAGE PAGE_ID="ProductDelivery_resolveDeductionDetailsView1">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseDeductionItemID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseDeductionItemID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
      </INLINE_PAGE>


    </DETAILS_ROW>


    <FIELD
      LABEL="Field.Label.Case"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseDescriptionText"
        />
      </CONNECT>
      <LINK PAGE_ID="Case_resolveCaseHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Name"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="deductionName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
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


    <FIELD
      LABEL="Field.Label.Percentage"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rate"
        />
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>
