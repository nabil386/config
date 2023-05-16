<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page contains details of the Action menu for a product sandbox.   -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <ACTION_SET
    ALIGNMENT="CENTER"
    BOTTOM="FALSE"
  >
    <ACTION_CONTROL LABEL="ActionControl.Label.Publish">
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="actionsDisabledInd"
        />
      </CONDITION>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="CREOLEProductAdmin_selectForPublish"
      >
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
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.Discard">
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="actionsDisabledInd"
        />
      </CONDITION>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="CREOLEProductAdmin_confirmRemoveSandbox"
      >
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
      </LINK>
    </ACTION_CONTROL>


    <SEPARATOR/>


    <ACTION_CONTROL LABEL="ActionControl.Label.EditDeterminationDetails">
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="actionsDisabledInd"
        />
      </CONDITION>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="CREOLEProductAdmin_modifyInEditEligibilityDeterminationDetails"
      >
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
      </LINK>
    </ACTION_CONTROL>


    <SEPARATOR/>


    <ACTION_CONTROL LABEL="ActionControl.Label.AddDisplayCategory">
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="actionsDisabledInd"
        />
      </CONDITION>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="CREOLEProductAdmin_createSandboxDecisionDisplayCategory"
      >
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
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.AddProductPeriod">
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="actionsDisabledInd"
        />
      </CONDITION>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="CREOLEProductAdmin_createSandboxProductPeriod"
      >
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
      </LINK>
    </ACTION_CONTROL>


  </ACTION_SET>


</VIEW>
