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
    * Lists all the legal action for a particular category
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
    NAME="DISPLAY"
    OPERATION="searchAdminLegalActionByCategoryID"
    PHASE="DISPLAY"
  />


  <!-- connection parameters required for display bean -->
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


  <!-- BEGIN, CR00169111, SS -->
  <CLUSTER TITLE="Cluster.Title.LegalAction">
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
                NAME="DISPLAY"
                PROPERTY="legalActionID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="legalActionID"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>
        <ACTION_CONTROL LABEL="ActionControl.Label.AddProdDelCases">
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="AdminLegalAction_resolveProdDelCaseType"
          >
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
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
                NAME="DISPLAY"
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
                NAME="DISPLAY"
                PROPERTY="legalActionID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="legalActionID"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>
        <ACTION_CONTROL LABEL="ActionControl.Label.Remove">
          <!-- BEGIN, CR00169111, SS -->
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="AdminLegalAction_removeLegalActionForCategory"
          >
            <!-- END, CR00169111 -->
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="legalCategoryNameCode"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="legalCategoryNameCode"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="legalActionID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="legalActionID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="legalCategoryId"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="legalCategoryId"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>
      </ACTION_SET>
      <!-- END, CR00206667 -->
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
      <!-- BEGIN, CR00206667, GP -->
      <DETAILS_ROW>
        <INLINE_PAGE PAGE_ID="AdminLegalAction_viewLegalAction">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="legalActionNameCode"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="legalActionNameCode"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
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
