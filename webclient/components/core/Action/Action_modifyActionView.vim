<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010 Curam Software Ltd.                                      -->
<!-- All rights reserved.                                                    -->
<!-- This software is the confidential and proprietary information of Curam  -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose     -->
<!-- such Confidential Information and shall use it only in accordance with  -->
<!-- the terms of the license agreement you entered into with Curam          -->
<!-- Software.                                                               -->
<!-- Description                                                             -->
<!-- ===========                                                             -->
<!-- The included view to modify an action details.                          -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <CLUSTER LABEL_WIDTH="17">
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Action"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="action"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="action"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER TITLE="Cluster.Label.OwnerDetails">
    <!-- BEGIN, CR00214099, AK -->
    <CLUSTER
      DESCRIPTION="Field.Statictext.ParticipantOwner"
      LABEL_WIDTH="25"
      STYLE="cluster-cpr-no-border"
    >
      <FIELD
        CONTROL="CHECKBOXED_LIST"
        HEIGHT="4"
        LABEL="Field.Label.CaseParticipant"
      >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="participantRoleID"
            NAME="DISPLAYCASEPARTICIPANT"
            PROPERTY="name"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseParticipantsTabList"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="caseParticipantsTabList"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
    <!-- END, CR00214099 -->


    <!-- BEGIN, CR00235006, AK -->
    <CLUSTER
      DESCRIPTION="Field.Label.UserOwner"
      LABEL_WIDTH="25"
      STYLE="cluster-cpr-no-border"
    >
      <FIELD
        LABEL="Field.Label.User"
        WIDTH="60"
      >
        <CONNECT>
          <INITIAL
            NAME="DISPLAY"
            PROPERTY="userOwnerFullName"
          />
        </CONNECT>
        <!-- END, CR00235006 -->
        <!-- BEGIN, CR00233907, AK -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="userOwner"
          />
        </CONNECT>
        <!-- END, CR00233907 -->
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="userOwner"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
  </CLUSTER>
</VIEW>
