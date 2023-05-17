<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
    * Copyright 2008, 2011 Curam Software Ltd.
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
    * Lists all the EXISTING legal action TO add/assign for a particular category
    *
    *
-->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file:\\Curam\CuramUIMSchema"
>


  <!-- Page parameter required for this page -->
  <PAGE_PARAMETER NAME="legalCategoryNameCode"/>
  <PAGE_PARAMETER NAME="legalCategoryId"/>


  <!-- Declare the 'display' server bean -->
  <SERVER_INTERFACE
    CLASS="AdminLegalAction"
    NAME="DISPLAY"
    OPERATION="searchExistingLegalActionByCategoryID"
    PHASE="DISPLAY"
  />


  <!-- Declare the 'action' server bean -->
  <SERVER_INTERFACE
    CLASS="AdminLegalAction"
    NAME="ACTION"
    OPERATION="createLegalActionLink"
    PHASE="ACTION"
  />


  <!-- connection parameters required for action and display bean -->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="legalCategoryId"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$legalActionCategoryID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="legalCategoryNameCode"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$legalStatusDtls$dtls$code"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="legalCategoryId"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="legalActionCategoryID"
    />
  </CONNECT>


  <!-- BEGIN, CR00169111, SS -->
  <!-- Actionset contains Save and Cancel link -->
  <ACTION_SET
    ALIGNMENT="CENTER"
    BOTTOM="true"
  >
    <ACTION_CONTROL
      LABEL="ActionControl.Label.Save"
      TYPE="SUBMIT"
    >
      
    </ACTION_CONTROL>
    <ACTION_CONTROL LABEL="ActionControl.Label.Cancel">
      
    </ACTION_CONTROL>
  </ACTION_SET>


  <CLUSTER DESCRIPTION="Cluster.Description.LegalAction">
    <!-- List fields on this page -->
    <LIST >
      <!-- END, CR00169111 -->
      <CONTAINER
        ALIGNMENT="CENTER"
        WIDTH="5"
      >
        <WIDGET TYPE="MULTISELECT">
          <WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="legalActionID"
              />
            </CONNECT>
          </WIDGET_PARAMETER>
          <WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
            <CONNECT>
              <TARGET
                NAME="ACTION"
                PROPERTY="multiselectString"
              />
            </CONNECT>
          </WIDGET_PARAMETER>
        </WIDGET>
      </CONTAINER>
      <FIELD LABEL="Field.Label.Name">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="legalActionNameCode"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.StartDate">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="dtls$startDate"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.EndDate">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="dtls$endDate"
          />
        </CONNECT>
      </FIELD>
    </LIST>
  </CLUSTER>
</VIEW>
