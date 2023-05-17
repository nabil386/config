<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2005, 2013. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2005 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- Reads a Task's home page details. -->
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
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="description"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="MaintainSupervisorAppealCase"
    NAME="DISPLAY"
    OPERATION="readCaseAppeals"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="appealOption"/>
  <PAGE_PARAMETER NAME="taskOptionCode"/>
  <PAGE_PARAMETER NAME="caseIssuesViewOptionCode"/>
  <PAGE_PARAMETER NAME="appealType"/>
  <PAGE_PARAMETER NAME="appealTypeDesc"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$caseID"
    />
  </CONNECT>


  <!-- BEGIN, CR00405639, KRK -->
  <ACTION_SET>
    <ACTION_CONTROL LABEL="Action.Control.ReassignAppeals">
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="SupervisorAppeals_reassignCaseAppealsByDeadline"
        SAVE_LINK="true"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="taskOptionCode"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="taskOptionCode"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
    <ACTION_CONTROL LABEL="Action.Control.ViewByType">


      <CONDITION>
        <IS_FALSE
          NAME="DISPLAY"
          PROPERTY="appealTypeCodeInd"
        />
      </CONDITION>


      <LINK PAGE_ID="SupervisorAppeals_caseAppealsByTypeChart">
        <CONNECT>
          <SOURCE
            NAME="CONSTANT"
            PROPERTY="supervisor.type"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="appealOption"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="taskOptionCode"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="taskOptionCode"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="Action.Control.ViewByDeadline">


      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="appealTypeCodeInd"
        />
      </CONDITION>
      <LINK PAGE_ID="SupervisorAppeals_caseAppealsByTypeChart">
        <CONNECT>
          <SOURCE
            NAME="CONSTANT"
            PROPERTY="supervisor.deadline"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="appealOption"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="taskOptionCode"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="taskOptionCode"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>
  <!-- END, CR00405639 -->


</VIEW>
