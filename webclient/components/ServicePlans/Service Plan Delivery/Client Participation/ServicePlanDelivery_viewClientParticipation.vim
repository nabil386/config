<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2004-2005, 2008-2010 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to view Client Participation details for     -->
<!-- particular plan item of service plan.                                  -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="ServicePlanDelivery"
    NAME="DISPLAY"
    OPERATION="readClientParticipationDetails"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="dailyAttendanceID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="dailyAttendanceID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$dailyAttendanceID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="41"
    NUM_COLS="2"
  >


    <FIELD LABEL="Field.Label.AbsenceReason">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dailyAttendanceDtls$dtls$dtls$absenceReason"
        />
      </CONNECT>
    </FIELD>


    <!--BEGIN, CR00168425, GBA-->
    <CONTAINER LABEL="Container.Label.TimeAbsent">


      <FIELD ALIGNMENT="RIGHT">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="dailyAttendanceDtls$dtls$dtls$hoursAbsent"
          />
        </CONNECT>
      </FIELD>


      <FIELD ALIGNMENT="CENTER">
        <CONNECT>
          <SOURCE
            NAME="TEXT"
            PROPERTY="Container.Separator"
          />
        </CONNECT>
      </FIELD>


      <FIELD ALIGNMENT="LEFT">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="dailyAttendanceDtls$dtls$dtls$minutesAbsent"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>
    <!--END, CR00168425-->


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="20.5"
    NUM_COLS="1"
    TITLE="Cluster.Title.Progress"
  >
    <FIELD LABEL="Field.Label.Progress">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dailyAttendanceDtls$dtls$dtls$progress"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ProgressReason">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dailyAttendanceDtls$dtls$dtls$progressReason"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <FIELD HEIGHT="4">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dailyAttendanceDtls$dtls$dtls$comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
