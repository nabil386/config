<?xml version="1.0" encoding="UTF-8"?>
<!-- Licensed Materials - Property of IBM Copyright IBM Corporation 2013. All Rights Reserved. US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. -->
<!-- Copyright (c) 2003, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows the user to view code tables within a hierarchy.      -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <SERVER_INTERFACE
    CLASS="System"
    NAME="DISPLAY"
    OPERATION="filterListItemsInCodeTable"
  />


  <PAGE_PARAMETER NAME="codetableName"/>
  <PAGE_PARAMETER NAME="displayName"/>
  <PAGE_PARAMETER NAME="itemFilter"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="codetableName"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$codeTableName"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="itemFilter"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$itemFilter"
    />
  </CONNECT>


  <LIST>
    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="System_modifyCodetableItemFromList"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="codetableName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="codetableName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="code"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="code"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="languageCode"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="languageCode"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Show">
        <CONDITION>
          <IS_FALSE
            NAME="DISPLAY"
            PROPERTY="selectableInd"
          />
        </CONDITION>
        <LINK PAGE_ID="System_resolveHideOrShowCodetableItem">
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="codetableName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="codetableName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="code"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="code"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="languageCode"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="languageCode"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="CONSTANT"
              PROPERTY="CodeTableItem.isHidden.TRUE"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="enabled"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Hide">
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="selectableInd"
          />
        </CONDITION>
        <LINK
          PAGE_ID="System_resolveHideOrShowCodetableItem"
          SAVE_LINK="false"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="codetableName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="codetableName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="code"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="code"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="languageCode"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="languageCode"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="CONSTANT"
              PROPERTY="CodeTableItem.isHidden.FALSE"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="enabled"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Translate">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="System_listCTItemFunctionalNames"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="codetableName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="codetableName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$codeTableItemDetails$code"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="itemCode"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="itemFilter"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="itemFilter"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL
        IMAGE="DeleteButton"
        LABEL="ActionControl.Label.Delete"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="System_cancelCodetableItem"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="codetableName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="codetableName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="code"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="code"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="languageCode"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="languageCode"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <FIELD
      LABEL="Field.Label.Item"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="description"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Code"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$codeTableItemDetails$code"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Selectable"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="selectableInd"
        />
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>
