<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008, 2011 Curam Software Ltd.                           -->
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
        PROPERTY="PageTitle.Hearing"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="Hearing"
    NAME="DISPLAY"
    OPERATION="listForHearingCase"
    PHASE="DISPLAY"
  />
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
  <!-- Page Parameter required for this page  -->
  <PAGE_PARAMETER NAME="caseID"/>


  <LIST >


    <FIELD LABEL="Field.Label.ReferenceNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="HearingCaseHearingDetails$referenceNumber"
        />
      </CONNECT>


      <LINK PAGE_ID="Appeal_hearingCaseHearingHomeForIC">


        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="hearingID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="hearingID"
          />
        </CONNECT>
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
            PROPERTY="pageDescription"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD LABEL="Field.Label.HearingOfficial">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="userFullname"
        />
      </CONNECT>


    </FIELD>


    <FIELD LABEL="Field.Label.Date">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="hearingDateTime"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="HearingCaseHearingDetails$statusCode"
        />
      </CONNECT>
    </FIELD>


  </LIST>
</VIEW>
