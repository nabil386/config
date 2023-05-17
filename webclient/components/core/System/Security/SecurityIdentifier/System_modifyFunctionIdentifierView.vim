<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2004, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The page allows the user to modify function identifier pages.          -->
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
    OPERATION="readSecurityIdentifier"
  />


  <SERVER_INTERFACE
    CLASS="System"
    NAME="ACTION"
    OPERATION="modifySecurityIdentifier"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="securityIdentifierID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="securityIdentifierID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$securityIdentifierID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="sidType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="sidType"
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


  <CLUSTER LABEL_WIDTH="25">


    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="sidName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="sidName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Description">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="sidDescription"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="sidDescription"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Function">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="functionIdentifier"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="functionIdentifier"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="securityIdentifierID"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
