<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2010, 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2010 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for adding a report parameter.                       -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText1"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="ReportParameter"
    NAME="ACTION"
    OPERATION="addParameter"
    PHASE="ACTION"
  />


  <!-- Only one of these two parameters must be set -->
  <PAGE_PARAMETER NAME="biViewerConfigurationID"/>
  <PAGE_PARAMETER NAME="biReportConfigurationID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="biViewerConfigurationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="biViewerConfigurationID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="biReportConfigurationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="biReportConfigurationID"
    />
  </CONNECT>


  <!-- BEGIN, CR00335727, GA -->
  <CLUSTER
    LABEL_WIDTH="25"
    NUM_COLS="1"
  >
    <!-- END, CR00335727 -->


    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Value">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="value"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
