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
<!-- The included view for modifying a report parameter.                    -->
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
    NAME="DISPLAY"
    OPERATION="readParameter"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="ReportParameter"
    NAME="ACTION"
    OPERATION="modifyParameter"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="biReportParameterID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="biReportParameterID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$biReportParameterID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="biReportParameterID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="biReportParameterID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="biReportConfigurationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="biReportConfigurationID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="biViewerConfigurationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="biViewerConfigurationID"
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
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Value">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="value"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="value"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
