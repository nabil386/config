<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright (c) 2010 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Reads a Task's home page details without the task manage links.        -->
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
    CLASS="MaintainSupervisorTasks"
    NAME="DISPLAY"
    OPERATION="viewTaskDetails"
  />


  <PAGE_PARAMETER NAME="taskKey$taskID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="taskKey$taskID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="taskKey$taskID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Label.ReservedBy">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reservedByFullName"
        />
      </CONNECT>
      <LINK PAGE_ID="Supervisor_userWorkspace">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="reservedBy"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="CONSTANT"
            PROPERTY="supervisor.viewTaskOption"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="taskOptionCode"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <CONTAINER
      LABEL="Container.Label.TimeWorked"
      WIDTH="15"
    >
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="totalTimeWorked"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>
    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="status"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Deadline">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dueDateTime"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    SHOW_LABELS="false"
    STYLE="outer-cluster-borderless"
  >
    <LIST
      TITLE="List.Title.PrimaryAction"
      WIDTH="98"
    >
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$taskDetails$taskPrimaryActionDetailsList$taskActionDetailsList$subject"
          />
        </CONNECT>
        <LINK
          URI_SOURCE_NAME="DISPLAY"
          URI_SOURCE_PROPERTY="result$taskDetails$taskPrimaryActionDetailsList$taskActionDetailsList$actionURL"
        />
      </FIELD>
    </LIST>


    <LIST
      TITLE="List.Title.SupportingInformation"
      WIDTH="98"
    >
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$taskDetails$taskSupportingInformationDetailsList$taskActionDetailsList$subject"
          />
        </CONNECT>
        <LINK
          URI_SOURCE_NAME="DISPLAY"
          URI_SOURCE_PROPERTY="result$taskDetails$taskSupportingInformationDetailsList$taskActionDetailsList$actionURL"
        />
      </FIELD>
    </LIST>
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.History"
  >
    <FIELD HEIGHT="4">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="taskHistoryText"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
