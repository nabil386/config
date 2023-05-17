<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2010-2011 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page lists the product periods in a sandbox.                      -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <LIST>


    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="actionsDisabledInd"
      />
    </CONDITION>


    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="CREOLEProductAdmin_listInEditProductPeriodDispCat1">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="productID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="productID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="productStructureRuleClassNameOpt"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="productStructureClassSummary"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="productStructureRuleSetNameOpt"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="productStructureRuleSetName"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="otherKeyDataRuleClassNameOpt"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="otherKeyDataClassSummary"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="otherKeyDataRuleSetNameOpt"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="otherKeyDataRuleSetName"
          />
        </CONNECT>


        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="creoleProductPeriodID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="creoleProductPeriodID"
          />
        </CONNECT>


      </INLINE_PAGE>
    </DETAILS_ROW>


    <ACTION_SET TYPE="LIST_ROW_MENU">


      <ACTION_CONTROL LABEL="ActionControl.Label.AssociateDecisionDetailsRule">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="CREOLEProductAdmin_createSandboxProdPeriodDispCat1"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="sandboxID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="sandboxID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$details$sandboxVersionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="sandboxVersionNo"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="creoleProductPeriodID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="creoleProductPeriodID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="key$productID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="productID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="ActionControl.Label.Modify">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="CREOLEProductAdmin_modifyProductPeriod"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="creoleProductPeriodID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="creoleProductPeriodID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="sandboxID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="sandboxID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="ActionControl.Label.Rename">


        <LINK
          OPEN_MODAL="true"
          PAGE_ID="CREOLEProductAdmin_confirmRenameProductPeriod"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="nameID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="nameID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="sandboxID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="sandboxID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$details$sandboxVersionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="sandboxVersionNo"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="ActionControl.Label.Remove">


        <LINK
          OPEN_MODAL="true"
          PAGE_ID="CREOLEProductAdmin_confirmRemoveProductPeriod"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="creoleProductPeriodID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="creoleProductPeriodID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="sandboxID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="sandboxID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$details$sandboxVersionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="sandboxVersionNo"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


    </ACTION_SET>


    <FIELD
      LABEL="field.label.result$dtls$name"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="field.label.dateRange"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateRange"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="field.label.decisionRuleSet"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="decisionRuleSetNameOpt"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="field.label.decisionClassSummary"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="decisionRuleClassNameOpt"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
