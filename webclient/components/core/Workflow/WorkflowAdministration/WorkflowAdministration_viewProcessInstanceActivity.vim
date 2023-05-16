<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2007-2008 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This process allows the user to displays details of a particular       -->
<!-- Process Instance                                                       -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CLUSTER NUM_COLS="2">
    <FIELD LABEL="Field.Label.ActivityName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$activityName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$status"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.StartDateTime">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$startDateTime"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ActivityType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$activityType"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.TaskID">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$taskID"
        />
      </CONNECT>
      <LINK PAGE_ID="TaskManagement_taskHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$taskID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="taskID"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <FIELD LABEL="Field.Label.EndDateTime">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$endDateTime"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
