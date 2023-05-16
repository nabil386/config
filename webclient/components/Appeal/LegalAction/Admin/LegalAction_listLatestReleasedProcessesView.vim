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
    * This page lists all the latest released workflow
    *
-->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <!-- The page title for this page -->
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText1"
      />
    </CONNECT>
  </PAGE_TITLE>


  <!-- Declare the 'display' server bean -->
  <SERVER_INTERFACE
    CLASS="ProcessAdmin"
    NAME="DISPLAY"
    OPERATION="listLatestReleasedProcesses"
    PHASE="DISPLAY"
  />


  <!-- Lists entire workflow -->
  <LIST>
    <CONTAINER
      LABEL="Container.Label.Action"
      WIDTH="10"
    >
      <ACTION_CONTROL
        LABEL="ActionControl.Label.Select"
        TYPE="DISMISS"
      >
        <LINK>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="name"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="value"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
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
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
      <LINK PAGE_ID="ProcessDefinitionTool_viewProcessRedirect">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="processID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="processID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="processVersionNo"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="processVersionNo"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="name"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="processName"
          />
        </CONNECT>
      </LINK>
    </FIELD>
  </LIST>
</VIEW>
