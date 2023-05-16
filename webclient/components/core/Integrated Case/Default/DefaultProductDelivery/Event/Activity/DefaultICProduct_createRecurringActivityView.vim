<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
 
  Copyright IBM Corporation 2010, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2010 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Creates a new recurring activity for an DefaultICProduct               -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="IntegratedCase"
    NAME="DISPLAY"
    OPERATION="listCaseParticipantsDetails"
  />


  <SERVER_INTERFACE
    CLASS="Activity"
    NAME="ACTION"
    OPERATION="createRecurringUserActivity"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


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
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >


    <FIELD LABEL="Field.Label.Subject">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="subject"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Location">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="locationID"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Priority">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="priorityCode"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TITLE="Cluster.Title.Time"
  >


    <FIELD LABEL="Field.Label.Start">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDateTime"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.TimeStatus">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="timeStatusCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AllDay">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="allDayInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.End">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDateTime"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.IgnoreConflicts">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="ignoreConflictInd"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TITLE="Cluster.Title.ConcerningDetails"
  >
    <FIELD
      LABEL="Container.Label.Concerning"
      USE_BLANK="TRUE"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="participantRoleID"
          NAME="DISPLAY"
          PROPERTY="nameAndAgeOpt"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="concernRoleID"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="24.5"
    TITLE="Cluster.Title.ActivityRecurrence"
  >


    <FIELD LABEL="Field.Label.Frequency">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="frequencyPattern"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TITLE="Cluster.Title.RecurrenceDuration"
  >


    <FIELD
      LABEL="Field.Label.NumberOfOccurrences"
      WIDTH="3"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="numberOfOccurrences"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.RecurringEndDate"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="recurrenceEndDate"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >


    <!-- BEGIN, CR00406866, VT -->
    <FIELD
      HEIGHT="3"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00406866 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="notes"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
