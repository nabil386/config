<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright (c) 2003, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view used to display Heat map of Cases with Issues        -->
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
    CLASS="SupervisorWorkspace"
    NAME="DISPLAY"
    OPERATION="readSupervisorHomePageDetails"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="SupervisorWorkspace"
    NAME="DISPLAY1"
    OPERATION="readSupervisorUICWorkSpaceDetails"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="SupervisorWorkspace"
    NAME="ACTION"
    OPERATION="viewUICfilter"
    PHASE="ACTION"
  />
  <PAGE_PARAMETER NAME="week"/>


  <CLUSTER
    NUM_COLS="4"
    SHOW_LABELS="false"
  >
    <CONTAINER ALIGNMENT="LEFT">
      <ACTION_CONTROL LABEL="Action.Control.ReassignCases">
        <LINK
          PAGE_ID="Supervisor_reassignPriorityWeekCaseIssues"
          SAVE_LINK="true"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$pageContextDescription$description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="description"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY1"
              PROPERTY="priorityDates$startDate"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="regFromDate"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY1"
              PROPERTY="endDate"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="regToDate"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>
    <CONTAINER>
      <FIELD CONTROL="SKIP"/>
    </CONTAINER>
    <CONTAINER>
      <FIELD CONTROL="SKIP"/>
    </CONTAINER>
    <CONTAINER ALIGNMENT="RIGHT">
      <ACTION_CONTROL LABEL="Action.Control.ViewList">
        <LINK
          PAGE_ID="Supervisor_workspaceUICList"
          SAVE_LINK="true"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY1"
              PROPERTY="priorityDates$startDate"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="priorityWeekBegin"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>
  </CLUSTER>


  <CLUSTER
    NUM_COLS="3"
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Name"
  >
    <CLUSTER
      LABEL_WIDTH="45"
      NUM_COLS="1"
      SHOW_LABELS="true"
    >
      <CONTAINER
        ALIGNMENT="LEFT"
        LABEL="Field.Label.PeriorityWeek"
      >
        <FIELD
          LABEL="Field.Label.PeriorityWeek"
          WIDTH="50"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY1"
              PROPERTY="priorityDates$startDate"
            />
          </CONNECT>
        </FIELD>
      </CONTAINER>
    </CLUSTER>
    <CLUSTER
      LABEL_WIDTH="27"
      NUM_COLS="1"
      SHOW_LABELS="true"
    >
      <CONTAINER
        ALIGNMENT="LEFT"
        LABEL="Field.Title.ChooseDay"
      >
        <FIELD LABEL="Field.Title.ChooseDay">
          <CONNECT>
            <INITIAL
              HIDDEN_PROPERTY="chooseDayList$date"
              NAME="DISPLAY1"
              PROPERTY="dateDesc"
            />
          </CONNECT>
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="key$date$date"
            />
          </CONNECT>
        </FIELD>
        <ACTION_CONTROL
          IMAGE="UpdateButton"
          LABEL="Action.Control.Update"
          TYPE="SUBMIT"
        >
          <LINK PAGE_ID="Supervisor_casesWithIssuesByDateHeatMap">
            <CONNECT>
              <SOURCE
                NAME="ACTION"
                PROPERTY="key$date$date"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="recievedOnDate"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="week"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="week"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>
      </CONTAINER>
    </CLUSTER>
    <CONTAINER ALIGNMENT="RIGHT">
      <ACTION_CONTROL LABEL="Action.Control.PreviousWeek">
        <LINK PAGE_ID="Supervisor_workspaceUICByWeek">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY1"
              PROPERTY="previousDate"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="weekDate"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="Action.Control.NextWeek">


        <LINK PAGE_ID="Supervisor_workspaceUICByWeek">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY1"
              PROPERTY="nextDate"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="weekDate"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>
  </CLUSTER>
  <CLUSTER
    NUM_COLS="1"
    SHOW_LABELS="false"
  >
    <FIELD CONFIG="HeatMapUIC">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY1"
          PROPERTY="heatmapXMLString"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
