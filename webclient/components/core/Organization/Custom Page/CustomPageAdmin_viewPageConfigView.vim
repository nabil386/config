<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2009 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- View Custom Page Configuration - view -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="CustomPageAdmin"
    NAME="DISPLAY"
    OPERATION="readCustomPage"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="pageConfigID"/>
  <PAGE_PARAMETER NAME="pageID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="pageConfigID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$pageConfigID"
    />
  </CONNECT>


  <LIST TITLE="List.Title.Contexts">
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="hasContexts"
      />
    </CONDITION>
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$contextList$items$context"
        />
      </CONNECT>
    </FIELD>
  </LIST>


  <CLUSTER
    TITLE="Cluster.Title.PageLayout"
    LABEL_WIDTH="20"
  >
    <FIELD LABEL="Field.Label.NumberOfColumns">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="numColumns"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <LIST TITLE="List.Title.Pods">
    <FIELD
      LABEL="List.Column.Title.AvailablePods"
      WIDTH="70"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$availablePodsList$items$podCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Column.Title.SelectedPodYN"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$availablePodsList$items$selectedInd"
        />
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>
