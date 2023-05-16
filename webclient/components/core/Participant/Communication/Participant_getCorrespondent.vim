<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2004, 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2004, 2010, 2012 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to create a new communication.               -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <PAGE_PARAMETER NAME="concernRoleID"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="clientConcernRoleID"
    />
  </CONNECT>
  <SERVER_INTERFACE
    CLASS="Communication"
    NAME="ACTION"
    OPERATION="getCorrespondent"
    PHASE="ACTION"
  />
  <!-- BEGIN, CR00320919, DJ -->
  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="DISPLAY"
    OPERATION="listConcernContactsForCommunication"
    PHASE="DISPLAY"
  />
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="contactRMByConcernKey$concernRoleID"
    />
  </CONNECT>
  <!-- END, CR00320919 -->
  <CLUSTER
    LABEL_WIDTH="35"
    TITLE="Cluster.Title.Correspondent"
  >
    <FIELD LABEL="Field.Label.ClientIsCorrespondent">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="clientIsCorrespondent"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00320919, DJ -->
    <FIELD
      LABEL="Field.Label.ContactForClient"
      USE_BLANK="true"
      WIDTH="37"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="contactConRoleID"
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="correspondentDetails$details$contactConcernRoleIDOpt"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00320919 -->
    <CONTAINER LABEL="Field.Label.CorrespondentSearch">
      <FIELD WIDTH="37">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="correspondentDetails$details$correspondentType"
          />
        </CONNECT>
      </FIELD>
      <FIELD>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="correspondentDetails$details$correspondentConcernRoleID"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>
    <FIELD CONTROL="SKIP"/>
  </CLUSTER>
  <CLUSTER
    DESCRIPTION="Cluster.CorrespondentContact.Description"
    LABEL_WIDTH="35"
  >
    <FIELD
      LABEL="Field.Label.CorrespondentName"
      WIDTH="37"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="correspondentDetails$details$correspondentName"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
