<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- ====================================================================== -->
<!-- Copyright 2010 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ====================================================================== -->
<!-- This page allows to save the Application.                                -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="Application"
    NAME="ACTION"
    OPERATION="quitApplication"
    PHASE="ACTION"
  />


  <SERVER_INTERFACE
    CLASS="Application"
    NAME="DISPLAY"
    OPERATION="getOptionsForSaveApplication"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="executionID"/>
  <PAGE_PARAMETER NAME="concernRoleID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="executionID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="executionID"
    />
  </CONNECT>


  <LIST STYLE="screening-select-list">
    <CONTAINER WIDTH="5">
      <WIDGET
        ALIGNMENT="CENTER"
        TYPE="SINGLESELECT"
        WIDTH="100"
        WIDTH_UNITS="PERCENT"
      >
        <WIDGET_PARAMETER NAME="SELECT_TARGET">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="details$code"
            />
          </CONNECT>
        </WIDGET_PARAMETER>


        <WIDGET_PARAMETER NAME="SELECT_SOURCE">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$list$code"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
      </WIDGET>
    </CONTAINER>


    <FIELD LABEL="Description.Label">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$list$description"
        />
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>
