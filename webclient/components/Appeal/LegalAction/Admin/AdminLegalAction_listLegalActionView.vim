<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
    * Copyright 2008, 2010-2011 Curam Software Ltd.
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
    * Lists all the legal action
    *
    *
-->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file:\\Curam\CuramUIMSchema"
>


  <!-- Declare the 'display' server bean -->
  <SERVER_INTERFACE
    CLASS="AdminLegalAction"
    NAME="DISPLAY_LEGALACTION"
    OPERATION="searchAdminLegalAction"
    PHASE="DISPLAY"
  />


  <!-- BEGIN, CR00169111, SS -->
  <CLUSTER>
    <!-- END, CR00169111 -->
    <!-- List fields on this page -->
    <LIST>
      <!-- BEGIN, CR00206667, GP -->
      <ACTION_SET TYPE="LIST_ROW_MENU">
        <!-- BEGIN, CR00169111, SS -->
        <ACTION_CONTROL LABEL="ActionControl.Label.AddIntegratedCases">
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="AdminLegalAction_resolveIntegratedCaseType"
          >
            <CONNECT>
              <SOURCE
                NAME="DISPLAY_LEGALACTION"
                PROPERTY="legalActionID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="legalActionID"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>
        <!-- BEGIN, CR00265048, DG -->
        <ACTION_CONTROL LABEL="ActionControl.Label.AddScreeningCases">
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="AdminLegalAction_resolveScreeningCaseType"
          >
            <CONNECT>
              <SOURCE
                NAME="DISPLAY_LEGALACTION"
                PROPERTY="legalActionID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="legalActionID"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>
        <!-- END, CR00265048 -->
        <ACTION_CONTROL LABEL="ActionControl.Label.AddProdDelCases">
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="AdminLegalAction_resolveProdDelCaseType"
          >
            <CONNECT>
              <SOURCE
                NAME="DISPLAY_LEGALACTION"
                PROPERTY="legalActionID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="legalActionID"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>
        <ACTION_CONTROL LABEL="ActionControl.Label.AddInvestigationCases">
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="AdminLegalAction_resolveInvestigationCaseType"
          >
            <CONNECT>
              <SOURCE
                NAME="DISPLAY_LEGALACTION"
                PROPERTY="legalActionID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="legalActionID"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>
        <!-- END, CR00169111 -->
        <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="AdminLegalAction_modifyLegalAction"
          >
            <CONNECT>
              <SOURCE
                NAME="DISPLAY_LEGALACTION"
                PROPERTY="legalActionID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="legalActionID"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>
        <ACTION_CONTROL
          IMAGE="DeleteButton"
          LABEL="ActionControl.Label.Remove"
        >
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="AdminLegalAction_cancelLegalAction"
          >
            <CONNECT>
              <SOURCE
                NAME="DISPLAY_LEGALACTION"
                PROPERTY="legalActionID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="configLegalActionID"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>
      </ACTION_SET>
      <!-- END, CR00206667 -->
      <FIELD LABEL="Field.Label.Name">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY_LEGALACTION"
            PROPERTY="legalActionNameCode"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.StartDate">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY_LEGALACTION"
            PROPERTY="dtls$startDate"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.EndDate">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY_LEGALACTION"
            PROPERTY="dtls$endDate"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.Status">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY_LEGALACTION"
            PROPERTY="dtls$recordStatusCode"
          />
        </CONNECT>
      </FIELD>
      <!-- BEGIN, CR00206667, GP -->
      <DETAILS_ROW>
        <INLINE_PAGE PAGE_ID="AdminLegalAction_viewLegalAction">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY_LEGALACTION"
              PROPERTY="legalActionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="legalActionID"
            />
          </CONNECT>
        </INLINE_PAGE>
      </DETAILS_ROW>
      <!-- END, CR00206667 -->
    </LIST>
  </CLUSTER>
</VIEW>
