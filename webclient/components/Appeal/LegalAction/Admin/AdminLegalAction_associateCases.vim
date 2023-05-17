<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2011, 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
    * Copyright 2011 Curam Software Ltd.
    * All rights reserved.
    *
    * This software is the confidential and proprietary information of Curam
    * Software, Ltd. ("Confidential Information"). You shall not disclose
    * such Confidential Information and shall use it only in accordance with
    * the terms of the license agreement you entered into with Curam
    * Software.
    *
    * Description
    * ===========
    * This page is used to create an association between a legal action and
    * cases.
    *
-->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file:\\Curam\CuramUIMSchema"
>


  <PAGE_PARAMETER NAME="configLegalActionID"/>
  <PAGE_PARAMETER NAME="caseTypeCode"/>


  <SERVER_INTERFACE
    CLASS="AdminLegalAction"
    NAME="ACTION"
    OPERATION="associateCaseTypes"
    PHASE="ACTION"
  />


  <SERVER_INTERFACE
    CLASS="AdminLegalAction"
    NAME="DISPLAY"
    OPERATION="listCaseTypes"
    PHASE="DISPLAY"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="configLegalActionID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="configLegalActionID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseTypeCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="associatedCaseType"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="configLegalActionID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="configLegalActionID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseTypeCode"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="associatedCaseType"
    />
  </CONNECT>


  <ACTION_SET
    ALIGNMENT="CENTER"
    BOTTOM="true"
  >
    <ACTION_CONTROL
      LABEL="ActionControl.Label.Save"
      TYPE="SUBMIT"
    />
    <ACTION_CONTROL LABEL="ActionControl.Label.Cancel"/>
  </ACTION_SET>


  <LIST >
    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY"
        PROPERTY="dateInd"
      />
    </CONDITION>
    <CONTAINER
      ALIGNMENT="CENTER"
      WIDTH="10"
    >
      <WIDGET TYPE="MULTISELECT">
        <WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="relatedID"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
        <WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="relatedIDMultiselect"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
      </WIDGET>
    </CONTAINER>
    <FIELD
      LABEL="Field.Label.CaseTypes"
      WIDTH="100"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="value"
        />
      </CONNECT>
    </FIELD>
  </LIST>


  <LIST >
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="dateInd"
      />
    </CONDITION>
    <CONTAINER
      ALIGNMENT="CENTER"
      WIDTH="10"
    >
      <WIDGET TYPE="MULTISELECT">
        <WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="relatedID"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
        <WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="relatedIDMultiselect"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
      </WIDGET>
    </CONTAINER>


    <!-- BEGIN, CR00342018, PS -->
    <FIELD
      LABEL="Field.Label.CaseTypes"
      WIDTH="55"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="value"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.StartDate"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="confStartDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.EndDate"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="confEndDate"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00342018 -->
  </LIST>


</VIEW>
