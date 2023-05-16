<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2003, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page modifies a rule set assignment for an assessment.            -->
<VIEW WINDOW_OPTIONS="width=500" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE NAME="TEXT" PROPERTY="PageTitle.StaticText1"/>
    </CONNECT>


  </PAGE_TITLE>


  <SERVER_INTERFACE CLASS="Resource" NAME="DISPLAY" OPERATION="readAssessmentRulesLink"/>


  <SERVER_INTERFACE CLASS="Resource" NAME="ACTION" OPERATION="modifyAssessmentRulesLink" PHASE="ACTION"/>


  <PAGE_PARAMETER NAME="assessmentRulesLinkID"/>


  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="assessmentRulesLinkID"/>
    <TARGET NAME="DISPLAY" PROPERTY="viewAssessmentRuleLinkKey$assessmentRulesLinkID"/>
  </CONNECT>


  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="viewAssessmentRuleLinkKey$assessmentRulesLinkID"/>
    <TARGET NAME="ACTION" PROPERTY="assessmentRulesLinkID"/>
  </CONNECT>


  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="versionNo"/>
    <TARGET NAME="ACTION" PROPERTY="versionNo"/>
  </CONNECT>


  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="rulesName"/>
    <TARGET NAME="ACTION" PROPERTY="rulesName"/>
  </CONNECT>


  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="assessmentConfigurationID"/>
    <TARGET NAME="ACTION" PROPERTY="assessmentConfigurationID"/>
  </CONNECT>


  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="assessmentName"/>
    <TARGET NAME="ACTION" PROPERTY="assessmentName"/>
  </CONNECT>


  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="ruleSetID"/>
    <TARGET NAME="ACTION" PROPERTY="ruleSetID"/>
  </CONNECT>


  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="statusCode"/>
    <TARGET NAME="ACTION" PROPERTY="statusCode"/>
  </CONNECT>


  <CLUSTER LABEL_WIDTH="35" NUM_COLS="1">


    <FIELD LABEL="Field.Label.RuleSet">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="rulesName"/>
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.StartDate">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="startDate"/>
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="startDate"/>
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.EndDate">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="endDate"/>
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="endDate"/>
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER SHOW_LABELS="false" TITLE="Cluster.Title.Comments">


    <!-- BEGIN, CR00406866, VT -->
    <FIELD HEIGHT="4" LABEL="Field.Label.Comments">
      <!-- END, CR00406866 -->
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="comments"/>
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="comments"/>
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>