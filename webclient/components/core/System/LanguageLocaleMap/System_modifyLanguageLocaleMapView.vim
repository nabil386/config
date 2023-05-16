<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2009, 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2009, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows a user to create a new Microsoft Word template.              -->
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
    OPERATION="readLanguageLocaleDetails"
  />


  <SERVER_INTERFACE
    CLASS="System"
    NAME="ACTION"
    OPERATION="modifyLanguageLocaleMap"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="languageCode"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="languageCode"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$key$key$languageCode"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="languageCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="key$key$key$languageCode"
    />
  </CONNECT>


  <!-- BEGIN, CR00335371, MR -->
  <CLUSTER LABEL_WIDTH="41">
    <!-- END, CR00335371 -->


    <FIELD LABEL="Field.Label.languageDescription">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$details$dtls$languageCode"
        />
      </CONNECT>


    </FIELD>


    <FIELD
      LABEL="Field.Label.locale"
      WIDTH="45"
    >


      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="result$dtls$localeIdentifier"
          NAME="DISPLAY"
          PROPERTY="result$dtls$localeIdentifier"
        />
      </CONNECT>


      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$details$dtls$localeIdentifier"
        />
      </CONNECT>


      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="localeIdentifier"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
