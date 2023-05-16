<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008, 2010-2011 Curam Software Ltd.                          -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to view an list participants.                -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">


  <!-- Page Title -->
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.Statement"
      />
    </CONNECT>
  </PAGE_TITLE>


  <!-- Page Parameter required for this page  -->
  <PAGE_PARAMETER NAME="caseID"/>


  <!-- Contains new link to add more participants -->
  <ACTION_SET
    ALIGNMENT="LEFT"
    BOTTOM="false"
  >
    <ACTION_CONTROL
      IMAGE="NewButton"
      LABEL="ActionControl.Label.New"
    >
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="multiParticipantInd"
        />
      </CONDITION>
      <!-- BEGIN, CR00186980, SS -->
      <LINK OPEN_MODAL="true"
        PAGE_ID="LegalAction_addParticipant">
        <!-- END, CR00186980 -->
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>


  <!-- Display bean to list legal action case participants -->
  <SERVER_INTERFACE
    CLASS="LegalAction"
    NAME="DISPLAY"
    OPERATION="listLegalActionCaseParticipantsDetails"
    PHASE="DISPLAY"
  />


  <!-- Connection parameters required for this page -->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
  </CONNECT>


  <!-- List contains participants details -->
  <LIST>
    <FIELD
      LABEL="Field.Label.Name"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Age"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="age"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Gender"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="gender"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Role"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="list$typeCode"
        />
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>
