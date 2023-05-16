<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2009-2011 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to select focus areas to add or remove from  -->
<!-- the audit plan.                                                        -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="CaseAuditFindings"
    NAME="DISPLAY"
    OPERATION="getSelectedConfiguredFocusAreas"
  />


  <SERVER_INTERFACE
    CLASS="CaseAuditFindings"
    NAME="ACTION"
    OPERATION="modifyAuditPlanFocusAreas"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="auditPlanID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="auditPlanID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="auditPlanID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="auditPlanID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="auditPlanID"
    />
  </CONNECT>


  <LIST
    PAGINATED="false"
    
  >
    <CONTAINER
      ALIGNMENT="CENTER"
      WIDTH="5"
    >
      <WIDGET TYPE="MULTISELECT">
        <WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="focusAreaCode"
            />
          </CONNECT>
        </WIDGET_PARAMETER>


        <WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="selectedFocusAreas"
            />
          </CONNECT>
        </WIDGET_PARAMETER>


        <WIDGET_PARAMETER NAME="MULTI_SELECT_INITIAL">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="selectedFocusAreas"
            />
          </CONNECT>
        </WIDGET_PARAMETER>


      </WIDGET>
    </CONTAINER>
    <FIELD LABEL="Field.Label.FocusArea">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="focusAreaCode"
        />
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>
