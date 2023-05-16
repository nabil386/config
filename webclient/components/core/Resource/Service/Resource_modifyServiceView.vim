<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2003, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003-2008, 2010 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to modify an existing service.               -->
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
    <!--BEGIN CR00085579, SK-->
    <!--END CR00085579-->
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="Resource"
    NAME="DISPLAY"
    OPERATION="readService"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="Resource"
    NAME="ACTION"
    OPERATION="modifyService"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="serviceID"/>
  <!--BEGIN CR00085579, SK-->
  <PAGE_PARAMETER NAME="typeName"/>
  <!--END CR00085579-->


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="serviceID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="serviceDetailsKey$serviceID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="serviceID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="serviceDetails$serviceID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="statusCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="statusCode"
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


  <CLUSTER
    LABEL_WIDTH="37"
    NUM_COLS="2"
    TAB_ORDER="ROW"
  >


    <FIELD LABEL="Field.Title.Type">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="typeCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="typeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Title.Description">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$readServiceResult$dtls$description"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="description"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Title.Start">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Title.End">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="18.5"
    TITLE="Cluster.Title.Cessation"
  >


    <FIELD
      LABEL="Field.Title.Reason"
      USE_BLANK="true"
      WIDTH="32"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="cessationReasonCde"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="cessationReasonCde"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      HEIGHT="4"
      LABEL="Field.Title.Comments"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="cessationReasonTxt"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="cessationReasonTxt"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >


    <!-- BEGIN, CR00406866, VT -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00406866 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
