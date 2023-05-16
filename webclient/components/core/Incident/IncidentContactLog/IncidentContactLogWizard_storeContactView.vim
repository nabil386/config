<?xml version="1.0" encoding="UTF-8"?>
<!--
  IBM Confidential
  
  OCO Source Materials
  
  PID 5725-H26
  
  Copyright IBM Corporation 2011, 2014
  
  The source code for this program is not published or otherwise divested
  of its trade secrets, irrespective of what has been deposited with the US Copyright Office
-->
<!-- Copyright 2011 Curam Software Ltd..                                    -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to create a contact log for a case.          -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
  >
    <CLUSTER
      BEHAVIOR="NONE"
      SHOW_LABELS="false"
      TITLE="Cluster.Label.Purpose"
      WIDTH="96"
    >
      <FIELD
        HEIGHT="4"
        LABEL="Field.Label.Purpose"
        USE_BLANK="false"
        WIDTH="99"
      >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="purposeCode"
            NAME="DISPLAYPURPOSE"
            PROPERTY="purposeName"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="purpose"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="purpose"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
  </CLUSTER>


  <CLUSTER
    BEHAVIOR="NONE"
    LABEL_WIDTH="36"
    NUM_COLS="2"
    TITLE="Cluster.Label.Details"
  >
    <FIELD
      LABEL="Field.Label.Location"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="97"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="location"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="location"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.StartDate"
      WIDTH="42"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDateTime"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDateTime"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Type"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="97"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="contactLogType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="contactLogType"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00426798, AC -->
    <FIELD LABEL="Field.Label.Author">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$incidentWizardDetails$wizardDetails$contactDetails$author"
        />
      </CONNECT>
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="result$incidentWizardDetails$wizardDetails$contactDetails$author"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="author"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00426798-->
    <FIELD
      LABEL="Field.Label.LocationDescription"
      WIDTH="94"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="locationDescription"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="locationDescription"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.EndDate"
      WIDTH="41"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDateTime"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDateTime"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Method"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="94"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="contactLogMethod"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="contactLogMethod"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
