<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
    * Copyright (c) 2008 Curam Software Ltd.
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
    CLASS="LegalAction"
    NAME="DISPLAY_LEGALACTION"
    OPERATION="listLegalActionsForCaseParticipant"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="LegalStatus"
    NAME="DISPLAYCONTEXT"
    OPERATION="getContextDescription"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="caseParticipantRoleID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseParticipantRoleID"
    />
    <TARGET
      NAME="DISPLAY_LEGALACTION"
      PROPERTY="key$key$caseParticipantRoleID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseParticipantRoleID"
    />
    <TARGET
      NAME="DISPLAYCONTEXT"
      PROPERTY="caseParticipantRoleID"
    />
  </CONNECT>


  <CLUSTER
    TITLE="Cluster.Title.LegalAction"
    WIDTH="100"
  >
    <!-- List fields on this page -->
    <LIST>
      <CONTAINER
        LABEL="Field.Label.Action"
        WIDTH="10"
      >
        <ACTION_CONTROL
          LABEL="ActionControl.Label.Select"
          TYPE="DISMISS"
        >
          <LINK>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY_LEGALACTION"
                PROPERTY="legalActionID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="value"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY_LEGALACTION"
                PROPERTY="legalActionName"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="description"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>
      </CONTAINER>
      <FIELD LABEL="Field.Label.Name">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY_LEGALACTION"
            PROPERTY="legalActionName"
          />
        </CONNECT>
      </FIELD>
    </LIST>
  </CLUSTER>
</VIEW>
