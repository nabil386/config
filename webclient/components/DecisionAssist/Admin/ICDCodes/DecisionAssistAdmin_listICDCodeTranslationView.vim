<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012, 2022. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2007-2008, 2010-2011 Curam Software Ltd.                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page is allows the user to view list of ICD code translations.    -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">
  <LIST>
    <!-- BEGIN, CR00207167, SS -->
    <ACTION_SET TYPE="LIST_ROW_MENU">
      <!-- END, CR00207167 -->
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit" TYPE="ACTION">
        <!-- BEGIN, CR00207167, SS -->
        <!-- BEGIN, CR00198884, SS -->
        <!-- BEGIN, CR00198297, SS -->
        <LINK OPEN_MODAL="true" PAGE_ID="DecisionAssistAdmin_modifyICDCodeTranslation" WINDOW_OPTIONS="width=500">
          <!-- END, CR00198297 -->
          <!-- END, CR00198884 -->
          <!-- END, CR00207167 -->
          <CONNECT>
            <SOURCE NAME="SI_LIST_ICDTRANSLATIONS" PROPERTY="translationID"/>
            <TARGET NAME="PAGE" PROPERTY="translationID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="VIEW_ICD_CODE" PROPERTY="ICDCode"/>
            <TARGET NAME="PAGE" PROPERTY="ICDCode"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Delete" TYPE="ACTION">
        <!-- BEGIN, CR00207167, SS -->
        <!-- BEGIN, CR00198297, SS -->
        <!-- BEGIN, CR00187459, SS -->
        <!-- BEGIN, CR00237410, AK -->
        <LINK OPEN_MODAL="true" PAGE_ID="DecisionAssistAdmin_deleteICDCodeTranslation" WINDOW_OPTIONS="width=500">
          <!-- END, CR00237410 -->
          <!-- END, CR00187459 -->
          <!-- END, CR00198297 -->
          <!-- END, CR00207167 -->
          <CONNECT>
            <SOURCE NAME="SI_LIST_ICDTRANSLATIONS" PROPERTY="translationID"/>
            <TARGET NAME="PAGE" PROPERTY="translationID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="VIEW_ICD_CODE" PROPERTY="result$viewCodeDtls$viewDtls$ICDCodeID"/>
            <TARGET NAME="PAGE" PROPERTY="ICDCodeID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="VIEW_ICD_CODE" PROPERTY="ICDCode"/>
            <TARGET NAME="PAGE" PROPERTY="ICDCode"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="SI_LIST_ICDTRANSLATIONS" PROPERTY="versionNo"/>
            <TARGET NAME="PAGE" PROPERTY="versionNo"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <!-- BEGIN, CR00207167, SS -->
    </ACTION_SET>
    <!-- END, CR00207167 -->
    <FIELD LABEL="Field.Label.Language">
      <CONNECT>
        <SOURCE NAME="SI_LIST_ICDTRANSLATIONS" PROPERTY="languageID"/>
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.TranslationText">
      <CONNECT>
        <SOURCE NAME="SI_LIST_ICDTRANSLATIONS" PROPERTY="translationText"/>
      </CONNECT>
    </FIELD>
  </LIST>
</VIEW>