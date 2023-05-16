<?xml version="1.0" encoding="UTF-8"?>
<!-- Licensed Materials - Property of IBM Copyright IBM Corporation 2012. All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. -->
<!-- Copyright (c) 2003, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows the user to change a code table name.                 -->
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
    NAME="ACTION"
    OPERATION="modifyCodeTableName"
    PHASE="ACTION"
  />
  <SERVER_INTERFACE
    CLASS="System"
    NAME="DISPLAY"
    OPERATION="readCodeTable"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="System"
    NAME="DISPLAY_ITEM_LIST"
    OPERATION="filterListItemsInCodeTable"
  />


  <SERVER_INTERFACE
    CLASS="System"
    NAME="ACTION_DEFAULT_ITEM"
    OPERATION="modifyCodeTableDefault"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="codetableName"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="codetableName"
    />
    <TARGET
      NAME="ACTION_DEFAULT_ITEM"
      PROPERTY="details$name"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$versionNo"
    />
    <TARGET
      NAME="ACTION_DEFAULT_ITEM"
      PROPERTY="details$versionNo"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="codetableName"
    />
    <TARGET
      NAME="DISPLAY_ITEM_LIST"
      PROPERTY="key$codeTableName"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="codetableName"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="codeTableName$name"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$displayNameId"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="displayNameId"
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
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="codetableName"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="oldName"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$functionalName"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="oldDisplayName"
    />
  </CONNECT>
  <CLUSTER LABEL_WIDTH="50">
    <FIELD LABEL="Field.Label.Table.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$functionalName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="newDisplayName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Default.Item"
      USE_BLANK="true"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="result$codeTableItemDetails$code"
          NAME="DISPLAY_ITEM_LIST"
          PROPERTY="result$codeTableItemDetails$description"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$defaultCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION_DEFAULT_ITEM"
          PROPERTY="details$defaultCode"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
