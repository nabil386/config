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


  <!-- Display bean to list legal action case participants -->
  <SERVER_INTERFACE
    CLASS="LegalAction"
    NAME="DISPLAY"
    OPERATION="readByLegalActionCaseID"
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
      PROPERTY="key$caseID"
    />
  </CONNECT>


  <ACTION_SET BOTTOM="false">
    <ACTION_CONTROL LABEL="ActionControl.Label.Associate">
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="LegalAction_associateLegalAction"
      >
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
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="dtls$pageDescription"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>
  <!-- List to display the associated legal actions -->
  <LIST>

    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$name"
        />
      </CONNECT>
      <!-- BEGIN, CR00186980, SS -->
      <LINK PAGE_ID="Case_resolveCaseHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="legalActionCaseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
      <!-- END, CR00186980 -->
    </FIELD>
    <FIELD
      LABEL="Field.Label.Participants"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="participants"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$associatedLegalActions$dtls$dtls$statusCode"
        />
      </CONNECT>
    </FIELD>
  </LIST>
</VIEW>
