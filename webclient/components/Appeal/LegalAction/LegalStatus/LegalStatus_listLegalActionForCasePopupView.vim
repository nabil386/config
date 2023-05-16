<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
    * Copyright 2010 Curam Software Ltd.
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
    * View for listing all the legal action configured for a case type
    *
    * 
-->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file:\\Curam\CuramUIMSchema"
>


  <SERVER_INTERFACE
    CLASS="LegalAction"
    NAME="DISPLAY_LEGALACTION"
    OPERATION="listLegalActionsByCaseID"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY_LEGALACTION"
      PROPERTY="caseID"
    />
  </CONNECT>


  <CLUSTER
    TITLE="Cluster.Title.LegalAction"
    WIDTH="100"
  >
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
                PROPERTY="name"
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
            PROPERTY="name"
          />
        </CONNECT>
      </FIELD>
    </LIST>
  </CLUSTER>
</VIEW>
