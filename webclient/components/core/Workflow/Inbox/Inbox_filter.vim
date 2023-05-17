<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2009 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for modifying participant attachments.               -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.Filter"
  >
    <FIELD
      LABEL="Field.Label.Priority"
      USE_BLANK="true"
    >
      <CONNECT>
        <TARGET
          NAME="DISPLAY"
          PROPERTY="taskListKey$priority"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.TaskOption"
      USE_BLANK="true"
    >
      <CONNECT>
        <TARGET
          NAME="DISPLAY"
          PROPERTY="listTaskKey$taskListKey$taskOption"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Status"
      USE_BLANK="true"
    >
      <CONNECT>
        <TARGET
          NAME="DISPLAY"
          PROPERTY="taskListKey$status"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
