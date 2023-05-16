<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012,2020. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
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
      TITLE="Cluster.Label.Concerning"
      WIDTH="97"
    >
      <FIELD
        HEIGHT="3"
        LABEL="Field.Label.Concerning"
        USE_BLANK="false"
        WIDTH="99"
      >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="caseParticipantRoleID"
            NAME="DISPLAYPARTICIPANTLIST"
            PROPERTY="name"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="contactDetails$concerningCaseParticipantTabList"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="concerningCaseParticipantTabList"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


    <CLUSTER
      BEHAVIOR="NONE"
      SHOW_LABELS="false"
      TITLE="Cluster.Label.Purpose"
      WIDTH="99"
    >
      <FIELD
        HEIGHT="3"
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
            PROPERTY="contactDetails$purpose"
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
    LABEL_WIDTH="30"
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
          PROPERTY="contactDetails$location"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="location"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.StartDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="contactDetails$startDateTime"
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
          PROPERTY="contactDetails$contactLogType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="contactLogType"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Author"
      WIDTH="93"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="contactDetails$author"
        />
      </CONNECT>
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="contactDetails$author"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="author"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.LocationDescription"
      WIDTH="94"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="contactDetails$locationDescription"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="locationDescription"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EndDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="contactDetails$endDateTime"
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
          PROPERTY="contactDetails$contactLogMethod"
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
