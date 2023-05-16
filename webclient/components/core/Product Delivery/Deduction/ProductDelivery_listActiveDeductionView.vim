<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2006,2009, 2010-2011 Curam Software Ltd.                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view to display a list of active deductions for a         -->
<!-- product delivery.                                                      -->
<?curam-deprecated Since Curam 6.0, replaced by ICProduct_listCurrentDeductionView.vim.?>
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
    OPERATION="listActiveDeduction"
  />


  <PAGE_PARAMETER NAME="caseID"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$caseID"
    />
  </CONNECT>
  <ACTION_SET BOTTOM="false">
    <ACTION_CONTROL
      IMAGE="NewButton"
      LABEL="ActionControl.Label.New"
    >
      <LINK PAGE_ID="ProductDelivery_selectDeductionCategory">
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
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>


  <LIST DESCRIPTION="Cluster.Description.Tax">


    <ACTION_SET TYPE="LIST_ROW_MENU">


      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <!-- BEGIN, CR00238896, GD -->
        <LINK PAGE_ID="ProductDelivery_resolveModifyDeductionView1">
        <!-- END, CR00238896 -->
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
              PROPERTY="description"
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
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>
    <FIELD
      LABEL="Field.Label.Category"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="deductionType"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Name"
      WIDTH="12"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="deductionName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Priority"
      WIDTH="12"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="priority"
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
          NAME="DISPLAY"
          PROPERTY="amount"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Percentage"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rate"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00128858, MC -->
    <FIELD
      LABEL="Field.Label.StartDate"
      WIDTH="12"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.EndDate"
      WIDTH="12"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00128858 -->
    <FIELD
      LABEL="Field.Label.BusinessStatus"
      WIDTH="12"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="businessStatus"
        />
      </CONNECT>
    </FIELD>
    <DETAILS_ROW>
      <!-- BEGIN, CR00238896, GD -->
      <INLINE_PAGE PAGE_ID="ProductDelivery_resolveDeductionDetailsView1">
      <!-- END, CR00238896 -->
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


  </LIST>
</VIEW>
