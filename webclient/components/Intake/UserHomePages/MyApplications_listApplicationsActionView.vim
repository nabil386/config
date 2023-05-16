<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2011 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <!--BEGIN,CR00277123,ZT-->
  <LIST
    DEFAULT_PAGE_SIZE="15"
    PAGINATION_THRESHOLD="15"
  >
    <!--END,CR00277123-->
    <TITLE>
      <CONNECT>
        <SOURCE
          NAME="TEXT"
          PROPERTY="List.Title.Applications"
        />
      </CONNECT>
    </TITLE>


    <DETAILS_ROW>
      <INLINE_PAGE
        URI_SOURCE_NAME="ACTION"
        URI_SOURCE_PROPERTY="applicationContextPanelPage"
      />
    </DETAILS_ROW>


    <FIELD LABEL="Container.Label.Application">
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="dtls$reference"
        />
      </CONNECT>
      <LINK
        URI_SOURCE_NAME="ACTION"
        URI_SOURCE_PROPERTY="result$dtlsList$applicationSummaryPage"
      >
        <CONDITION>
          <IS_FALSE
            NAME="ACTION"
            PROPERTY="result$dtlsList$isInProgressApplication"
          />
        </CONDITION>
      </LINK>
    </FIELD>


    <FIELD LABEL="Field.Lable.DaysForDeadline">
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="daysForDeadline"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ApplicantName">
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="applicantName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.ProgramsApplied"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="programsApplied"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ApplicationDate">
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$dtlsList$dtls$filingDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$dtlsList$applicationStatus"
        />
      </CONNECT>
    </FIELD>
  </LIST>
</VIEW>
