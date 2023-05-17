<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- Modify the details for a batch process include page -->
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
    CLASS="System"
    NAME="DISPLAY"
    OPERATION="readBatchErrorCode"
  />


  <SERVER_INTERFACE
    CLASS="System"
    NAME="ACTION"
    OPERATION="modifyBatchErrorCode"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="batchErrorCodeID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="batchErrorCodeID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="readBatchErrorCodeKey$batchErrorCodeID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="batchErrorCodeID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="batchErrorCodeID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>


  <CLUSTER LABEL_WIDTH="50">


    <FIELD LABEL="Field.Label.BatchErrorCodeID">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="readBatchErrorCodeKey$batchErrorCodeID"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.BatchErrorCode">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="batchErrorCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="batchErrorCode"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
