<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2005, 2008 Curam Software Ltd.                           -->
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


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
    STYLE="cluster-cpr-no-border"
  >
    <FIELD
      LABEL="Field.Label.RoleType"
      USE_BLANK="TRUE"
      USE_DEFAULT="FALSE"
      WIDTH="50"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="incidentParticipantRoleType"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    DESCRIPTION="Cluster.UserName.Description"
    LABEL_WIDTH="22.5"
    NUM_COLS="1"
    STYLE="cluster-cpr-no-border"
  >
    <FIELD
      LABEL="Field.Label.UserName"
      WIDTH="40"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="userName"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    DESCRIPTION="Cluster.Participant.Description"
    LABEL_WIDTH="22.5"
    STYLE="cluster-cpr-no-border"
  >
    <CONTAINER LABEL="Field.Label.Participant">
      <FIELD WIDTH="25">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="concernRoleType"
          />
        </CONNECT>
      </FIELD>
      <FIELD WIDTH="50">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="participantRoleID"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>
  </CLUSTER>


  <CLUSTER
    DESCRIPTION="Cluster.NewParticipant.Description"
    LABEL_WIDTH="45"
    NUM_COLS="2"
    STYLE="cluster-cpr-no-border"
  >


    <FIELD LABEL="Field.Label.ParticipantName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="participantName"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
    STYLE="cluster-cpr-no-border"
  >


    <FIELD LABEL="Field.Label.Email">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="email"
        />
      </CONNECT>
    </FIELD>


    <CONTAINER LABEL="Field.Label.PhoneNumber">
      <FIELD WIDTH="25">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="phoneAreaCode"
          />
        </CONNECT>
      </FIELD>
      <FIELD WIDTH="50">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="phoneNumber"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>


  </CLUSTER>
  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
    STYLE="cluster-cpr-no-border"
  >
    <FIELD LABEL="Field.Label.Address">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="address"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
