<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright (c) 2009, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- Pod Container -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <SERVER_INTERFACE
    CLASS="PodContainer1"
    NAME="ACTION"
    OPERATION="saveChanges"
    PHASE="ACTION"
  />


  <SERVER_INTERFACE
    CLASS="PodContainer1"
    NAME="DISPLAY"
    OPERATION="loadData"
    PHASE="DISPLAY"
  />


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="pageID$pageID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="pageID$pageID"
    />
  </CONNECT>


  <ACTION_SET
    ALIGNMENT="CENTER"
    BOTTOM="false"
  >
    <ACTION_CONTROL
      IMAGE="SaveButton"
      LABEL="ActionControl.Label.Save"
      STYLE="hidden"
      TYPE="SUBMIT"
    >
      <LINK PAGE_ID="PodContainer_redirect">
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="pageID$pageID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      LABEL="ActionControl.Label.Reset"
      STYLE="hidden"
      TYPE="SUBMIT"
    >
      <LINK PAGE_ID="PodContainer_redirect">
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="pageID$pageID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>


  <FIELD>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="result$XMLData"
      />
    </CONNECT>
    <CONNECT>
      <TARGET
        NAME="ACTION"
        PROPERTY="podContainerData$XMLData"
      />
    </CONNECT>
  </FIELD>


</VIEW>
