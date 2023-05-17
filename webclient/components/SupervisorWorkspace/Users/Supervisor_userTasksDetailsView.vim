<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright 2010-2011 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view for user assigned tasks.                             -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


  <!-- BEGIN, CR00291093, DJ -->
  <SERVER_INTERFACE CLASS="MaintainSupervisorUsers" NAME="DISPLAYCHART" OPERATION="readSupervisorWorkspaceContentDetails" PHASE="DISPLAY"/>


  <SERVER_INTERFACE CLASS="MaintainSupervisorUsers" NAME="ACTION" OPERATION="readSupervisorWorkspaceContentDetails" PHASE="ACTION"/>
  <!-- END, CR00291093 -->


  <SERVER_INTERFACE CLASS="MaintainSupervisorUsers" NAME="DISPLAYWQ" OPERATION="listAllWorkQueuesForUser"/>
  <!-- BEGIN, CR00291093, DJ -->
  <INFORMATIONAL>
    <CONNECT>
      <SOURCE NAME="DISPLAYCHART" PROPERTY="informationMsgTxt"/>
    </CONNECT>
  </INFORMATIONAL>
  <!-- END, CR00291093 -->
  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="userName"/>
    <TARGET NAME="DISPLAYCHART" PROPERTY="key$userName"/>
  </CONNECT>


  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="taskOptionCode"/>
    <TARGET NAME="DISPLAYCHART" PROPERTY="key$taskOption"/>
  </CONNECT>


  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="userName"/>
    <TARGET NAME="DISPLAYWQ" PROPERTY="key$userName"/>
  </CONNECT>


  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="userName"/>
    <TARGET NAME="ACTION" PROPERTY="key$userName"/>
  </CONNECT>


  <ACTION_SET>
    <ACTION_CONTROL LABEL="ActionControl.Label.SubscribeToWorkQueue">
      <LINK OPEN_MODAL="true" PAGE_ID="Supervisor_subscribeUserToWorkQueue" SAVE_LINK="true" WINDOW_OPTIONS="width=700">
        <CONNECT>
          <SOURCE NAME="DISPLAYCHART" PROPERTY="key$userName"/>
          <TARGET NAME="PAGE" PROPERTY="userName"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAYCHART" PROPERTY="userFullName"/>
          <TARGET NAME="PAGE" PROPERTY="userFullName"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.ReserveAssignedTasks">
      <LINK OPEN_MODAL="true" PAGE_ID="Supervisor_reserveAssignedtasksForUser" SAVE_LINK="true" WINDOW_OPTIONS="width=700">
        <CONNECT>
          <SOURCE NAME="DISPLAYCHART" PROPERTY="key$userName"/>
          <TARGET NAME="PAGE" PROPERTY="userName"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAYCHART" PROPERTY="userFullName"/>
          <TARGET NAME="PAGE" PROPERTY="userFullName"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.TaskRedirection">
      <LINK OPEN_MODAL="true" PAGE_ID="Supervisor_redirectTasksToUsers" SAVE_LINK="true" WINDOW_OPTIONS="width=1000">
        <CONNECT>
          <SOURCE NAME="DISPLAYCHART" PROPERTY="key$userName"/>
          <TARGET NAME="PAGE" PROPERTY="userName"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAYCHART" PROPERTY="userFullName"/>
          <TARGET NAME="PAGE" PROPERTY="userFullName"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.ReserveTasksFromWorkQueue">
      <LINK OPEN_MODAL="true" PAGE_ID="Supervisor_reserveWorkQueueTasksForUser" SAVE_LINK="true" WINDOW_OPTIONS="width=700">
        <CONNECT>
          <SOURCE NAME="DISPLAYCHART" PROPERTY="key$userName"/>
          <TARGET NAME="PAGE" PROPERTY="userName"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAYCHART" PROPERTY="userFullName"/>
          <TARGET NAME="PAGE" PROPERTY="userFullName"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.TaskAllocationBlocking">
      <LINK OPEN_MODAL="true" PAGE_ID="Supervisor_taskAllocationBlocking" SAVE_LINK="true" WINDOW_OPTIONS="width=725">
        <CONNECT>
          <SOURCE NAME="DISPLAYCHART" PROPERTY="key$userName"/>
          <TARGET NAME="PAGE" PROPERTY="userName"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAYCHART" PROPERTY="userFullName"/>
          <TARGET NAME="PAGE" PROPERTY="userFullName"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.TasksDueNextWeek" TYPE="SUBMIT">
      <LINK PAGE_ID="Supervisor_resolverUserWorkspace">
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="userName"/>
          <TARGET NAME="PAGE" PROPERTY="userName"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="CONSTANT" PROPERTY="supervisor.viewTaskOption"/>
          <TARGET NAME="PAGE" PROPERTY="taskOptionCode"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.TasksDueNextMonth" TYPE="SUBMIT">
      <LINK PAGE_ID="Supervisor_resolverUserWorkspace">
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="userName"/>
          <TARGET NAME="PAGE" PROPERTY="userName"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="CONSTANT" PROPERTY="supervisor.viewTaskOption2"/>
          <TARGET NAME="PAGE" PROPERTY="taskOptionCode"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


  </ACTION_SET>


  <CLUSTER NUM_COLS="2" SHOW_LABELS="false" STYLE="outer-cluster-borderless">
    <LIST STYLE="cluster-margin-right" TITLE="Cluster.Title.WorkQueueSubscriptions">
      <ACTION_SET TYPE="LIST_ROW_MENU">
        <ACTION_CONTROL LABEL="ActionControl.Label.Unsubscribe">
          <LINK OPEN_MODAL="true" PAGE_ID="Supervisor_unsubscribeOrgObjectFromWorkQueue" WINDOW_OPTIONS="width=400">
            <CONNECT>
              <SOURCE NAME="DISPLAYWQ" PROPERTY="result$dtls$subscriberID"/>
              <TARGET NAME="PAGE" PROPERTY="subscriberID"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="DISPLAYWQ" PROPERTY="result$dtls$subscriberType"/>
              <TARGET NAME="PAGE" PROPERTY="subscriberType"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="DISPLAYWQ" PROPERTY="result$dtls$subscriberName"/>
              <TARGET NAME="PAGE" PROPERTY="userName"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="DISPLAYWQ" PROPERTY="result$dtls$workQueueID"/>
              <TARGET NAME="PAGE" PROPERTY="workQueueID"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="DISPLAYWQ" PROPERTY="result$dtls$pageTitle"/>
              <TARGET NAME="PAGE" PROPERTY="pageTitle"/>
            </CONNECT>
            <CONNECT>
              <SOURCE NAME="DISPLAYWQ" PROPERTY="result$dtls$unsubscribePageText"/>
              <TARGET NAME="PAGE" PROPERTY="unsubscribePageText"/>
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>
      </ACTION_SET>
      <FIELD LABEL="Field.Label.WorkQueueName" WIDTH="50">
        <CONNECT>
          <SOURCE NAME="DISPLAYWQ" PROPERTY="result$dtls$workQueueName"/>
        </CONNECT>
        <LINK PAGE_ID="Supervisor_workQueueWorkspace">
          <CONNECT>
            <SOURCE NAME="DISPLAYWQ" PROPERTY="result$dtls$workQueueID"/>
            <TARGET NAME="PAGE" PROPERTY="workQueueID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="CONSTANT" PROPERTY="supervisor.viewTaskOption"/>
            <TARGET NAME="PAGE" PROPERTY="taskOptionCode"/>
          </CONNECT>
        </LINK>
      </FIELD>
      <FIELD LABEL="Field.Label.SubscriberType" WIDTH="50">
        <CONNECT>
          <SOURCE NAME="DISPLAYWQ" PROPERTY="result$dtls$subscriberType"/>
        </CONNECT>
      </FIELD>
    </LIST>


    <!-- BEGIN, CR00291093, DJ -->
    <LIST STYLE="cluster-margin-left" TITLE="Cluster.Title.OrganizationUnitMembership">
      <FIELD LABEL="Field.Label.OrgName" WIDTH="50">
        <CONNECT>
          <SOURCE NAME="DISPLAYCHART" PROPERTY="result$contentDetails$orgDetailsList$dtls$orgUnitName"/>
        </CONNECT>
        <LINK PAGE_ID="Supervisor_orgUnitWorkspace">
          <CONNECT>
            <SOURCE NAME="DISPLAYCHART" PROPERTY="result$contentDetails$orgDetailsList$dtls$orgUnitID"/>
            <TARGET NAME="PAGE" PROPERTY="orgUnitID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="CONSTANT" PROPERTY="supervisor.viewTaskOption"/>
            <TARGET NAME="PAGE" PROPERTY="taskOptionCode"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAYCHART" PROPERTY="result$contentDetails$orgDetailsList$dtls$orgStructureID"/>
            <TARGET NAME="PAGE" PROPERTY="orgStructureID"/>
          </CONNECT>
        </LINK>
      </FIELD>
      <FIELD LABEL="Field.Label.Position" WIDTH="50">
        <CONNECT>
          <SOURCE NAME="DISPLAYCHART" PROPERTY="result$contentDetails$orgDetailsList$dtls$positionName"/>
        </CONNECT>
      </FIELD>
    </LIST>
    <!-- END, CR00291093 -->
  </CLUSTER>


</VIEW>