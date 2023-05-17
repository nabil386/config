<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2006-2007, 2010-2011 Curam Software Ltd.                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows the user to check if the person has already been -->
<!-- registered.                                                       -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <!-- BEGIN, CR00198144, SS -->
  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
  >
    <!-- BEGIN, CR00198224, SS -->
    <!-- BEGIN, CR00198884, SS -->
    <FIELD
      LABEL="Field.Label.QuestionnaireName"
      WIDTH="90"
    >
      <!-- END, CR00198884-->
      <!-- END, CR00198144 -->
      <!-- END, CR00198224 -->
      <CONNECT>
        <TARGET
          NAME="CREATE_QUESTIONNAIRE"
          PROPERTY="name"
        />
      </CONNECT>
      <!-- BEGIN, CR00198224, SS -->
    </FIELD>
    <!-- BEGIN, CR00198144, SS -->
    <!-- BEGIN, CR00198884, SS -->
    <!-- BEGIN, CR00237410, AK -->
    <FIELD
      LABEL="Field.Label.QuestionnaireType"
      USE_BLANK="false"
      USE_DEFAULT="true"
      WIDTH="72"
    >
      <!-- END, CR00237410 -->
      <!-- END, CR00198884-->
      <!-- END, CR00198144 -->
      <!-- END, CR00198224 -->
      <CONNECT>
        <TARGET
          NAME="CREATE_QUESTIONNAIRE"
          PROPERTY="type"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.QuestionnaireComments"
  >
    <!-- BEGIN, CR00198144, SS -->
    <FIELD HEIGHT="4">
      <!-- END, CR00198144 -->
      <CONNECT>
        <TARGET
          NAME="CREATE_QUESTIONNAIRE"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
