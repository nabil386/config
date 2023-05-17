<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2010, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010 Curam Software Ltd.                                         -->
<!-- All rights reserved.                                                       -->
<!-- This software is the confidential and proprietary information of Curam     -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose        -->
<!-- such Confidential Information and shall use it only in accordance with     -->
<!-- the terms of the license agreement you entered into with Curam             -->
<!-- Software.                                                                  -->
<!-- Description                                                                -->
<!-- ===========                                                                -->
<!-- The included view to modify a situation.                                   -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <!-- BEGIN, CR00216768, AK -->
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="recordStatus"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="recordStatus"
    />
  </CONNECT>
  <!-- END, CR00216768 -->


  <CLUSTER>
    <!-- BEGIN, CR00216915, AK -->
    <!-- BEGIN, CR00214099, AK -->
    <CLUSTER LABEL_WIDTH="27.5">
      <!-- END, CR00214099 -->
      <FIELD
        LABEL="Field.Label.SituationCategory"
        USE_BLANK="false"
        USE_DEFAULT="true"
        WIDTH="40"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="situationCategory"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="situationCategory"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        ALIGNMENT="LEFT"
        HEIGHT="4"
        LABEL="Field.Label.SituationRequiringAction"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="situationRequiringAction"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="situationRequiringAction"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        ALIGNMENT="LEFT"
        LABEL="Field.Label.Outcome"
        USE_BLANK="true"
        USE_DEFAULT="false"
        WIDTH="40"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="outcome"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="outcome"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


    <!-- BEGIN, CR00229423, AK -->
    <CLUSTER
      LABEL_WIDTH="50"
      NUM_COLS="2"
    >
      <!-- END, CR00229423 -->
      <FIELD
        LABEL="Field.Label.ExpectedEndDate"
        USE_BLANK="true"
        USE_DEFAULT="false"
        WIDTH="10"
        WIDTH_UNITS="CHARS"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="expectedEndDate"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="expectedEndDate"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.ActualEndDate"
        USE_BLANK="true"
        USE_DEFAULT="false"
        WIDTH="10"
        WIDTH_UNITS="CHARS"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="actualEndDate"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="actualEndDate"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


    <!-- BEGIN, CR00214099, AK -->
    <CLUSTER
      LABEL_WIDTH="35"
      NUM_COLS="2"
      TITLE="Cluster.Title.SituationConcerns"
    >
      <!-- BEGIN, CR00229423, AK -->
      <FIELD
        CONTROL="CHECKBOXED_LIST"
        HEIGHT="4"
        LABEL="Field.Label.ConcerningParticipants"
      >
        <!-- END, CR00229423 -->
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="participantRoleID"
            NAME="DISPLAYCASEPARTICIPANT"
            PROPERTY="name"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAYCONCERNCASEPARTICIPANT"
            PROPERTY="caseParticipantTabList"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="concernCaseparticipantTabList"
          />
        </CONNECT>
      </FIELD>


      <!-- BEGIN, CR00229423, AK -->
      <FIELD
        CONTROL="CHECKBOXED_LIST"
        HEIGHT="4"
        LABEL="Field.Label.Allegations"
      >
        <!-- END, CR00229423 -->
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="allegationID"
            NAME="DISPLAYALLEGATIONS"
            PROPERTY="type"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="allegationIDTabList"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="allegationIDTabList"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
    <!-- END, CR00214099 -->
    <!-- END, CR00216915 -->


    <CLUSTER
      SHOW_LABELS="false"
      TITLE="Cluster.Label.Comments"
    >
      <!-- BEGIN, CR00417389, VT -->
      <FIELD
        HEIGHT="4"
        LABEL="Field.Label.Comments"
      >
        <!-- END, CR00417389 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="comments"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="comments"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
  </CLUSTER>


</VIEW>
