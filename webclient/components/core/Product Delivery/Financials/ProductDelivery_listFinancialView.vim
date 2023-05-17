<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003-2011 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. (&quot;Confidential Information&quot;). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view to display a list of financials for a                -->
<!-- product delivery.                                                      -->
<?curam-deprecated Since Curam 6.0. Replaced by Financial_listFinInstructionView1.vim. ?>
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
    OPERATION="listCaseFinancials"
  />


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


  <LIST>


    <ACTION_SET TYPE="LIST_ROW_MENU">


      <ACTION_CONTROL LABEL="ActionControl.Label.ViewInstruction">
        <LINK PAGE_ID="Financial_resolveInstructionView">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtls$financialInstructionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="instructionID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtls$instructionLineItemCategory"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="instructionType"
            />
          </CONNECT>
          <!-- BEGIN, CR00096570, DJ -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtls$creditDebitType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="creditDebitType"
            />
          </CONNECT>
          <!-- END, CR00096570 -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="instructionLineItemType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="instructionLineItemType"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="iliTypeDescription"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$statusCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.EffectiveDate"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="effectiveDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.Amount"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="amount"
        />
      </CONNECT>
    </FIELD>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Financial_viewLineItem">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="instructionLineItemID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="lineItemID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


  </LIST>


</VIEW>
