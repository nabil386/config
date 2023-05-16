<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003-2004, 2009, 2010 Curam Software Ltd.                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This is the included view used to display a list of assessments for an -->
<!-- integrated case member.                                                -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText1"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="IntegratedCase"
    NAME="DISPLAY"
    OPERATION="listMemberAssessment"
  />


  <PAGE_PARAMETER NAME="caseParticipantRoleID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseParticipantRoleID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseParticipantRoleID"
    />
  </CONNECT>


  <LIST>
    <FIELD
      LABEL="Field.Label.AssessmentID"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$list$list$dtls$caseID"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ClientName"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$list$list$dtls$concernRoleName"
        />
      </CONNECT>
      <LINK PAGE_ID="Participant_resolveRoleHome">
        <!-- BEGIN, CR00130954, CW -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$list$list$dtls$concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <!-- END, CR00130954 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$list$list$dtls$concernRoleName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="result$list$list$dtls$concernRoleName"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$list$list$dtls$concernRoleType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="participantType"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD
      LABEL="Field.Label.CaseTypeCode"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$list$list$dtls$caseTypeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.StartDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$list$list$dtls$startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.StatusCode"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$list$list$dtls$statusCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ClientType"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$list$list$dtls$concernRoleType"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
