<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012, 2019. All Rights Reserved.

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
<!-- View a location holiday details.                                       -->
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


  <PAGE_PARAMETER NAME="locationHolidayID"/>
  <!-- <PAGE_PARAMETER NAME="locationID"/> -->


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
      PROPERTY="locationID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$maintainLocationHolidayKey$locationID"
    />
  </CONNECT>


  <CLUSTER
    NUM_COLS="1"
  >


    <FIELD LABEL="Field.Label.Holiday">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="holidayName"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <FIELD LABEL="Field.Label.Description">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <MENU MODE="NAVIGATION">
    <ACTION_CONTROL LABEL="Field.Label.Browse">
      <LINK PAGE_ID="Organization_resolveLocationTree">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="locationStructureID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="locationStructureID"
          />
        </CONNECT>


        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="locationID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="locationID"
          />
        </CONNECT>


      </LINK>
    </ACTION_CONTROL>
  </MENU>


</VIEW>
