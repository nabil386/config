<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
 
  Copyright IBM Corporation 2003, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view for the modify location holiday pages.               -->
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
    CLASS="Organization"
    NAME="DISPLAY"
    OPERATION="readLocationHoliday"
  />


  <SERVER_INTERFACE
    CLASS="Organization"
    NAME="ACTION"
    OPERATION="modifyLocationHoliday"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="locationHolidayID"/>
  <PAGE_PARAMETER NAME="locationID"/>
  <PAGE_PARAMETER NAME="locationStructureID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="locationHolidayID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$maintainLocationHolidayKey$locationHolidayID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="locationHolidayID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="locationHolidayID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="locationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="locationID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="locationID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$maintainLocationHolidayKey$locationID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="statusCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="statusCode"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>


  <CLUSTER NUM_COLS="1">


    <FIELD LABEL="Field.Label.Holiday">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="holidayName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="holidayName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Date">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="holidayDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="holidayDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Apply">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="applyIndicator"
        />
      </CONNECT>


      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$maintainLocationHolidayDetails$applyIndicator"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00406866, VT -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
      WIDTH="100"
    >
      <!-- END, CR00406866 -->


      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$maintainLocationHolidayDetails$comments"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
