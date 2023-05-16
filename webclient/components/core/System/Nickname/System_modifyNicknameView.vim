<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2008, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This action allows the user to modify details for a nickname           -->
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
    OPERATION="readNickname"
  />


  <SERVER_INTERFACE
    CLASS="System"
    NAME="ACTION"
    OPERATION="modifyNickname"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="nickname"/>
  <PAGE_PARAMETER NAME="properName"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="nickname"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$nickname"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="properName"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$properName"
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
      NAME="PAGE"
      PROPERTY="nickname"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="key$nickname"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="properName"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="key$properName"
    />
  </CONNECT>


  <CLUSTER LABEL_WIDTH="33">


    <FIELD LABEL="Field.Label.ProperName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$properName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$properName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Nickname">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$nickname"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$nickname"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
