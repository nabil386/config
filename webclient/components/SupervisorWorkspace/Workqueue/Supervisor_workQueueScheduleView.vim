<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright (c) 2003 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view used to allow the user to enter the details of the   -->
<!-- the payment received.                                                  -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="MaintainSupervisorWorkQueues"
    NAME="DISPLAY"
    OPERATION="readWorkQueueUsersScheduleDetails"
    PHASE="DISPLAY"
  />


  <CLUSTER
    NUM_COLS="3"
    SHOW_LABELS="false"
  >
    <CLUSTER
      LABEL_WIDTH="40"
      SHOW_LABELS="true"
    >
      <CONTAINER
        ALIGNMENT="LEFT"
        LABEL="Field.Title.WeekBeginning"
      >
        <FIELD>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="activityCountDetailsList$weekBeginDate"
            />
          </CONNECT>
        </FIELD>
      </CONTAINER>
    </CLUSTER>


    <CONTAINER>
      <ACTION_CONTROL LABEL="Action.Control.PreviousWeek">
        <LINK PAGE_ID="Supervisor_workQueueScheduleByWeek">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="workQueueID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="workQueueID"
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
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="previousDate"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="weekBeginDate"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="Action.Control.NextWeek">
        <LINK PAGE_ID="Supervisor_workQueueScheduleByWeek">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="workQueueID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="workQueueID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="nextDate"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="weekBeginDate"
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
    </CONTAINER>
  </CLUSTER>


  <LIST>
    <FIELD
      LABEL="Field.Label.Name"
      WIDTH="80"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fullName"
        />
      </CONNECT>
      <LINK PAGE_ID="Supervisor_userWorkspace">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="userName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
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
    </FIELD>


    <FIELD
      LABEL="Field.Label.ActivitiesInWeek"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="countActivity"
        />
      </CONNECT>
      <LINK PAGE_ID="Supervisor_calendar">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="userName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
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
    </FIELD>


  </LIST>
</VIEW>
