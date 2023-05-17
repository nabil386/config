<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2002, 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002 - 2007, 2009, 2010 Curam Software Ltd.               -->
<!-- All rights reserved.                                                    -->
<!-- This software is the confidential and proprietary information of Curam  -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose     -->
<!-- such Confidential Information and shall use it only in accordance with  -->
<!-- the terms of the license agreement you entered into with Curam          -->
<!-- Software.                                                               -->
<!-- Description                                                             -->
<!-- ===========                                                             -->
<!-- This page allows a user to view details of a case client role.          -->
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
    CLASS="Case"
    NAME="DISPLAY"
    OPERATION="readClientRole1"
  />


  <PAGE_PARAMETER NAME="concernCaseRoleID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernCaseRoleID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="concernCaseRoleID"
    />
  </CONNECT>
  <CLUSTER NUM_COLS="2">
    <CLUSTER>
      <!--BEGIN, CR00226837, PB-->
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="translatorDisplayInd"
        />
      </CONDITION>
      <!--END, CR00226837-->
      <FIELD LABEL="Field.Label.TranslationRequired">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="translationRequiredInd"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
    <CLUSTER>
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="prefLanguageInd"
        />
      </CONDITION>
      <FIELD LABEL="Field.Label.PreferredLanguage">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="preferredLanguage"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00416277, VT -->
    <FIELD LABEL="Field.Label.Comments">
      <!-- END, CR00416277 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
