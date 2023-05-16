<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2005-2009 Curam Software Ltd.                            -->
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
<?curam-deprecated Since Curam 6.0, replaced by Incident_createIncident_content1.vim?>
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CLUSTER
    NUM_COLS="1"
    TITLE="Cluster.Details.Title"
  >
    <CLUSTER
      LABEL_WIDTH="35"
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        LABEL="Field.Label.Type"
        USE_BLANK="TRUE"
        USE_DEFAULT="FALSE"
        WIDTH="75"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="type"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.Sensitivity"
        USE_DEFAULT="TRUE"
        WIDTH="25"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="sensitivityCode"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.TimeOfDay"
        USE_DEFAULT="true"
        WIDTH="75"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="timeOfDay"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.Severity"
        USE_DEFAULT="true"
        WIDTH="50"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="severity"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.IncidentDateTime"
        USE_DEFAULT="TRUE"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="incidentDateTime"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.Location">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="location"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


    <CLUSTER
      LABEL_WIDTH="15"
      NUM_COLS="1"
      SHOW_LABELS="FALSE"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        HEIGHT="5"
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
    NUM_COLS="1"
    TITLE="Cluster.ReporterDetails.Title"
  >
    <CLUSTER
      LABEL_WIDTH="35"
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        LABEL="Field.Label.ReportMethod"
        WIDTH="50"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="reportMethod"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.AnonymousReport">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="anonymousReportInd"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


    <INCLUDE FILE_NAME="IncidentParticipant_incidentReporterParticipant_content.vim"/>


  </CLUSTER>


</VIEW>
