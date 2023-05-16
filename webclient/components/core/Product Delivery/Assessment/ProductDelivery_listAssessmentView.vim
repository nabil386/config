<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002-2008, 2010 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This is the included view used to display a list of assessments for an-->
<!-- integrated case.  -->
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
    CLASS="ProductDelivery"
    NAME="DISPLAY"
    OPERATION="listAssessment"
  />


  <PAGE_PARAMETER NAME="caseID"/>


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
    <ACTION_CONTROL
      IMAGE="NewButton"
      LABEL="ActionControl.Label.New"
    >
      <LINK PAGE_ID="ProductDelivery_createAssessment">
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
  <LIST>
    <FIELD
      LABEL="Field.Label.AssessmentID"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="TEXT"
          PROPERTY="Field.Label.View"
        />
      </CONNECT>
      <LINK PAGE_ID="Assessment_resolveView">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$dtls$dtls$caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>


      </LINK>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ClientName"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$dtls$dtls$concernRoleName"
        />
      </CONNECT>


    </FIELD>


    <FIELD
      LABEL="Field.Label.Assessment"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$dtls$dtls$name"
        />
      </CONNECT>
      <!--<LINK PAGE_ID="Assessment_home">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="assessmentConfigurationID" />
          <TARGET NAME="PAGE" PROPERTY="assessmentConfigurationID" />
        </CONNECT>
     </LINK>-->
    </FIELD>


    <FIELD
      LABEL="Field.Label.StartDate"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$dtls$dtls$startDate"
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
          PROPERTY="result$dtls$dtls$dtls$statusCode"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
