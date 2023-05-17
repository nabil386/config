<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003 ,2010 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of       -->
<!-- Curam Software, Ltd. ("Confidential Information"). You                 -->
<!-- shall not disclose such Confidential Information and shall use it only -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- with Curam Software.                                                   -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Page displaying the details of a rules data object.                    -->
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
    CLASS="RulesEditor"
    NAME="DISPLAY"
    OPERATION="viewRulesDataObject"
  />
  
  
  <PAGE_PARAMETER NAME="rulesDataObjectID"/>
  
  
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="rulesDataObjectID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="rulesDataObjectID"
    />
  </CONNECT>
  
  
  <CLUSTER LABEL_WIDTH="18">
    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rulesDataObjectName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Type">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rulesDataObjectType"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  
  
  <LIST TITLE="List.Title.Attributes">
    <FIELD
      LABEL="Field.Title.Name"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="attributeName"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD
      LABEL="Field.Title.Type"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="attributeType"
        />
      </CONNECT>
    </FIELD>
  </LIST>
  
</VIEW>
