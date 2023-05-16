<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright (c) 2002-2003,2008, 2010 Curam Software Ltd.                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view for modifying a case member.                         -->
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
    CLASS="MaintainSupervisorCase"
    NAME="DISPLAY"
    OPERATION="readCaseUsersScheduleDetails"
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
        LABEL="Field.Title.WeekBegin"
      >
        <FIELD>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$detailsList$weekBeginDate"
            />
          </CONNECT>
        </FIELD>
      </CONTAINER>
    </CLUSTER>


    <CONTAINER>


      <ACTION_CONTROL LABEL="Action.Control.PreviousWeek">
        <LINK PAGE_ID="Supervisor_caseUsersScheduleWeekNav">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="key$caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$detailsList$previousDate"
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


      <ACTION_CONTROL LABEL="Action.Control.NextWeek">
        <LINK PAGE_ID="Supervisor_caseUsersScheduleWeekNav">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="key$caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$detailsList$nextDate"
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
      WIDTH="60"
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
            PROPERTY="result$detailsList$detailsList$dtls$userName"
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
      LABEL="Field.Label.ActivityCount"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$detailsList$detailsList$dtls$countActivity"
        />
      </CONNECT>
      <!-- BEGIN, CR00124350, SK -->
      <!-- BEGIN, CR00125046, SK -->
      <LINK PAGE_ID="Supervisor_resolveCaseUserCalendar">
        <!-- END, CR00125046 -->
        <!-- END, CR00124350 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$detailsList$detailsList$dtls$userName"
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
