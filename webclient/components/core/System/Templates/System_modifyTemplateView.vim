<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008, 2010-2011 Curam Software Ltd.                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view for the modify template pages. -->
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
  <!-- BEGIN, CR00279987, KRK -->
  <SERVER_INTERFACE
    CLASS="System"
    NAME="DISPLAY"
    OPERATION="readXSLTemplate"
  />
  <SERVER_INTERFACE
    CLASS="System"
    NAME="ACTION"
    OPERATION="modifyXSLTemplate"
    PHASE="ACTION"
  />
  <!-- END, CR00279987 -->
  <PAGE_PARAMETER NAME="templateID"/>
  <PAGE_PARAMETER NAME="localeIdentifier"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="templateID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="templateID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="localeIdentifier"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$localeIdentifier"
    />
  </CONNECT>
  <!-- BEGIN, CR00096332, MR -->
  <!-- BEGIN, CR00279987, KRK -->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="localeIdentifier"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="localeIdentifier"
    />
  </CONNECT>
  <!-- END CR00279987 -->
  <!-- END CR00096332 -->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="templateID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="templateID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Label.Description">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="templateName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="templateName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="85"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="templateType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="templateType"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00279987, KRK -->
    <FIELD LABEL="Field.Label.TemplateID">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="templateIDCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="templateIDCode"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00279987 -->


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Category"
  >
    <!-- BEGIN, CR00237191, AK -->
    <FIELD
      CONTROL="CT_HIERARCHY_HORIZONTAL"
      LABEL="Field.Label.RelatesTo"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <!-- END, CR00237191 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="relatesTo"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="relatesTo"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
