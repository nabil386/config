<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2010, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                              -->
<!-- ===========                                              -->
<!-- This included page to create new situations.                           -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CLUSTER>
    <!-- BEGIN, CR00200707, AK -->
    <!-- BEGIN, CR00213311, AK -->
    <CLUSTER LABEL_WIDTH="27.5">
      <!-- END, CR00213311 -->
      <!-- BEGIN, CR00216008, AK -->
      <FIELD
        LABEL="Field.Label.SituationCategory"
        USE_BLANK="true"
        USE_DEFAULT="true"
        WIDTH="40"
      >
        <!-- END, CR00216008 -->
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="situationCategory"
          />
        </CONNECT>
      </FIELD>


      <!-- END, CR00213311 -->
      <!-- END, CR00200707 -->
      <FIELD
        HEIGHT="4"
        LABEL="Field.Label.SituationRequiringAction"
      >
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
          <TARGET
            NAME="ACTION"
            PROPERTY="outcome"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


    <!-- BEGIN, CR00200707, AK -->
    <!-- BEGIN, CR00213311, AK -->
    <CLUSTER
      LABEL_WIDTH="50"
      NUM_COLS="2"
    >
      <!-- END, CR00213311 -->
      <FIELD
        LABEL="Field.Label.ExpectedEndDate"
        USE_BLANK="true"
        USE_DEFAULT="false"
        WIDTH="10"
        WIDTH_UNITS="CHARS"
      >
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
        <!-- END, CR00200707 -->
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="actualEndDate"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


    <!-- BEGIN, CR00213311, AK -->
    <CLUSTER
      NUM_COLS="2"
      TITLE="Cluster.Title.SituationConcerns"
    >
      <CLUSTER LABEL_WIDTH="32.5">
        <FIELD
          CONTROL="CHECKBOXED_LIST"
          HEIGHT="4"
          LABEL="Field.Label.ConcerningParticipants"
        >
          <CONNECT>
            <INITIAL
              HIDDEN_PROPERTY="participantRoleID"
              NAME="DISPLAYCASEPARTICIPANT"
              PROPERTY="name"
            />
          </CONNECT>
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="concernCaseparticipantTabList"
            />
          </CONNECT>
        </FIELD>
      </CLUSTER>
      <CLUSTER LABEL_WIDTH="32.5">
        <FIELD
          CONTROL="CHECKBOXED_LIST"
          HEIGHT="4"
          LABEL="Field.Label.Allegations"
        >
          <CONNECT>
            <INITIAL
              HIDDEN_PROPERTY="allegationID"
              NAME="DISPLAYALLEGATIONS"
              PROPERTY="type"
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
    </CLUSTER>


    <CLUSTER
      SHOW_LABELS="false"
      TITLE="Cluster.Label.Comments"
    >
      <!-- END, CR00213311 -->
      <!-- BEGIN, CR00417389, VT -->
      <FIELD
        HEIGHT="4"
        LABEL="Field.Label.Comments"
      >
        <!-- END, CR00417389 -->
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="comments"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
    <!-- BEGIN, CR00208312, AK -->
  </CLUSTER>
  <!-- END, CR00208312 -->
</VIEW>
