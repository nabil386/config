<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012,2022. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- This page allows the user to create a contact log for a case.          -->

<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  
  <CLUSTER
    NUM_COLS="2"
  >
    <CLUSTER
      BEHAVIOR="NONE"
      SHOW_LABELS="false"
      TITLE="Cluster.Label.Concerning"
    >
      <FIELD
        HEIGHT="3"
        LABEL="Field.Label.Concerning"
        USE_BLANK="false"
      >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="caseParticipantRoleID"
            NAME="DISPLAY"
            PROPERTY="name"
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
    >
      <FIELD
        HEIGHT="3"
        LABEL="Field.Label.Purpose"
        USE_BLANK="false"
      >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="purposeCode"
            NAME="DISPLAYPURPOSE"
            PROPERTY="purposeName"
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
  	STYLE="outer-cluster-borderless"
    NUM_COLS="2"
  >

	<CLUSTER 
  		LABEL_WIDTH="30" 
  		BEHAVIOR="NONE"  
  	>

    <FIELD
      LABEL="Field.Label.Location"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="location"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.StartDate">
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
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="contactLogType"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Author">
      <CONNECT>
        <SOURCE
          NAME="DISPLAYCURRENTUSER"
          PROPERTY="userName"
        />
      </CONNECT>
      <CONNECT>
        <INITIAL
          NAME="DISPLAYCURRENTUSER"
          PROPERTY="fullName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="author"
        />
      </CONNECT>
    </FIELD>

	</CLUSTER>

	<CLUSTER 
  		LABEL_WIDTH="40" 
  		BEHAVIOR="NONE"  
  	>
	
    <FIELD LABEL="Field.Label.LocationDescription">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="locationDescription"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EndDate">
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
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="contactLogMethod"
        />
      </CONNECT>
    </FIELD>
    
    </CLUSTER>
  </CLUSTER>
</VIEW>
