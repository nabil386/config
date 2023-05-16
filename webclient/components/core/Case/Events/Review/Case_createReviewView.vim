<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2003, 2013. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003, 2009, 2010 Curam Software Ltd.                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This is the included view to create a new review event.                -->
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
    CLASS="Case"
    NAME="ACTION"
    OPERATION="createReview1"
    PHASE="ACTION"
  />


  <SERVER_INTERFACE
    CLASS="Case"
    NAME="DISPLAY"
    OPERATION="listAllCaseUsers"
  />


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


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


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
  </CONNECT>


  <CLUSTER LABEL_WIDTH="26.5">
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="result"
      />
    </CONDITION>
    <FIELD
      LABEL="Field.Label.Reviewer"
      USE_BLANK="true"
      WIDTH="43"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="currentReviewer$userName"
        />
      </CONNECT>
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="dtls$userName"
          NAME="DISPLAY"
          PROPERTY="dtls$fullName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="previousUserID"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.SearchForReviewer"
      WIDTH="55"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="reviewerID"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
  >
    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY"
        PROPERTY="result"
      />
    </CONDITION>
    <FIELD LABEL="Field.Label.User">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="reviewerID"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TITLE="Cluster.Title.ScheduleReview"
  >
    <FIELD
      LABEL="Field.Label.ScheduledStart"
      WIDTH="65"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="scheduledStartDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Reason">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="reasonCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.ExpectedEndDate"
      USE_DEFAULT="false"
      WIDTH="65"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="expectCompleteDate"
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
      HEIGHT="190"
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
