<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2002, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002,2009, 2010 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page creates a new task concerning the participant.               -->
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


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Label.Subject">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="subject"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
    
  <CLUSTER>
   
     <FIELD LABEL="Field.Label.TaskType" USE_BLANK="true" USE_DEFAULT="false" WIDTH="90">
       <CONNECT>
         <TARGET NAME="ACTION" PROPERTY="taskType"/>
       </CONNECT>
     </FIELD>
     
  </CLUSTER>
  
  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
  >
    
    <FIELD LABEL="Field.Label.Priority">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="priority"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Deadline"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="deadlineTime"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER
    LABEL_WIDTH="22.5"
    TITLE="Cluster.Title.AssignmentDetails"
  >
    <FIELD LABEL="Field.Label.ReserveToMe">
      <CONNECT>
/        <TARGET
          NAME="ACTION"
          PROPERTY="reserveToMeInd"
        />
      </CONNECT>
    </FIELD>
    <CONTAINER LABEL="Container.Label.AssignedTo">
      <FIELD WIDTH="30">
        <SCRIPT
          ACTION="clearAssignedTo()"
          EVENT="ONCHANGE"
          SCRIPT_FILE="ClearAssignedTo.js"
        />
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="assignType"
          />
        </CONNECT>
      </FIELD>
      <FIELD WIDTH="65">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="assignmentID"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>
	
  </CLUSTER>
  
  <!-- #Task 12746 - Create task with skill type -->
  <CLUSTER
    LABEL_WIDTH="20"
    TITLE="Cluster.Title.Classification"
  >
	<FIELD LABEL="Field.Label.BDMTaskSKillType">
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="bdmTaskClassificationID"/>
      </CONNECT>
    </FIELD>
	
  </CLUSTER>
  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00406866, VT -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00406866 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
