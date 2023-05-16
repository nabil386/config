<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002-2008, 2010 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This is the included view for a list of tasks associated with a        -->
<!-- an integrated case participant.                                        -->
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
    CLASS="IntegratedCase"
    NAME="DISPLAY"
    OPERATION="listMemberTasks"
  />


  <MENU>
    <ACTION_CONTROL LABEL="ActionControl.Label.NewActivity">
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="ICMember_createMemberActivity"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseParticipantRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseParticipantRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </MENU>
  <PAGE_PARAMETER NAME="caseParticipantRoleID"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseParticipantRoleID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseParticipantRoleID"
    />
  </CONNECT>
  <LIST>
    <ACTION_SET BOTTOM="false">
      <ACTION_CONTROL
        IMAGE="NewButton"
        LABEL="ActionControl.NewButton.label"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="ICMember_createTask"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="caseParticipantRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseParticipantRoleID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="pageDescription"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>
    <FIELD WIDTH="3">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reservedByExistsInd"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.TaskID"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="taskID"
        />
      </CONNECT>
      <LINK PAGE_ID="TaskManagement_taskHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="taskID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="taskID"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <FIELD
      LABEL="Field.Title.Subject"
      WIDTH="37"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="subject"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.Priority"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="priority"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.Status"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="status"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.DueDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dueDateTime"
        />
      </CONNECT>
    </FIELD>
  </LIST>
</VIEW>
