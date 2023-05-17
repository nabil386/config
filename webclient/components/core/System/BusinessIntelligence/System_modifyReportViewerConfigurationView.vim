<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

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
<!-- Description -->
<!-- =========== -->
<!-- The included view for editing a Report Viewer configuration details.   -->
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
    CLASS="ReportViewerConfiguration"
    NAME="DISPLAY"
    OPERATION="readReportViewerConfigurationDetails"
  />


  <SERVER_INTERFACE
    CLASS="ReportViewerConfiguration"
    NAME="ACTION"
    OPERATION="modifyReportViewerConfigurationDetails"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="biViewerConfigurationID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="biViewerConfigurationID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="biViewerConfigurationID"
    />
  </CONNECT>


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
      PROPERTY="recordStatus"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="recordStatus"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="58"
    NUM_COLS="2"
  >


    <FIELD LABEL="Field.Label.ReportViewerName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="viewerName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="viewerName"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="58"
    NUM_COLS="2"
  >


    <FIELD LABEL="Field.Label.ReportRoot">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reportRoot"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="reportRoot"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ReportContext">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reportContext"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="reportContext"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ReportServlet">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reportServlet"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="reportServlet"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ReportNameParameter">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reportNameParameter"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="reportNameParameter"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Description"
  >
    <!-- BEGIN, CR00463142, EC -->
    <FIELD
      HEIGHT="4"
      LABEL="Cluster.Title.Description"
    >
      <!-- END, CR00463142 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="description"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="description"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
