<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
 
  Copyright IBM Corporation 2009, 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2009-2011 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Infrastructure page containing common fields when capturing custom     -->
<!-- evidence                                                               -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CLUSTER>
    <!-- BEGIN, CR00335793, SSK -->
    <CLUSTER
      LABEL_WIDTH="30"
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
    >
      <!-- END, CR00335793 -->


      <FIELD
        LABEL="Field.Label.Type"
        USE_BLANK="TRUE"
        USE_DEFAULT="FALSE"
      
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="type"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.ParticipantRole"
        USE_BLANK="FALSE"
        USE_DEFAULT="TRUE"
    
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="details$incidentParticipantRoleType"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.Sensitivity"
        USE_DEFAULT="TRUE"
       
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="sensitivityCode"
          />
        </CONNECT>
      </FIELD>


      <!-- BEGIN, CR00386722, MP -->
      <FIELD
        LABEL="Field.Label.Category">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="category"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.Location">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="location"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.Severity"
        USE_DEFAULT="true"> 
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="severity"
          />
        </CONNECT>
      </FIELD>
      <!-- END, CR00386722 -->


    </CLUSTER>


    <CLUSTER
      LABEL_WIDTH="15"
      NUM_COLS="1"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        LABEL="Field.Label.Description"
        USE_DEFAULT="true"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="description"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="3"
    TITLE="Cluster.Occurrence.Title"
  >


    <FIELD
      LABEL="Field.Label.IncidentDate"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="incidentDate"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00349838,AC-->
    <FIELD
      LABEL="Field.Label.IncidentTime"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="50"
    >
      <!-- END, CR00349838-->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="incidentTime"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.TimeOfDay"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="timeOfDay"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER TITLE="Cluster.ReporterDetails.Title">
    <CLUSTER
      LABEL_WIDTH="45" 
      NUM_COLS="3"
      STYLE="cluster-cpr-no-border"
    >


      <CLUSTER
        LABEL_WIDTH="45"
        STYLE="cluster-cpr-no-border"
      >


        <!-- BEGIN, CR00280394, DJ -->
        <FIELD
          LABEL="Field.Label.ReportMethod"
          USE_BLANK="true"
          WIDTH="80"
        >
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="reportMethod"
            />
          </CONNECT>
        </FIELD>
        <!-- END, CR00280394 -->


      </CLUSTER>


      <FIELD LABEL="Field.Label.AnonymousReport">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="anonymousReportInd"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.ReportedByMe">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="reportedByMeInd"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


    <INCLUDE FILE_NAME="IncidentParticipant_incidentReporterParticipant_content.vim"/>


  </CLUSTER>


</VIEW>
