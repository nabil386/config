<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2008, 2010 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view to list client participation for the particular      -->
<!-- PlanItem of the service plan                                           -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.Title"
      />
    </CONNECT>
  </PAGE_TITLE>
  <MENU MODE="INTEGRATED_CASE">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="menuData"
      />
    </CONNECT>
  </MENU>
  <SERVER_INTERFACE
    CLASS="ServicePlanDelivery"
    NAME="DISPLAY"
    OPERATION="listClienParticipationForPlanItem"
  />
  <PAGE_PARAMETER NAME="plannedItemID"/>
  <PAGE_PARAMETER NAME="description"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="plannedItemID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="plannedItemIDKey$plannedItemID"
    />
  </CONNECT>
  <CLUSTER
    LABEL_WIDTH="15"
    NUM_COLS="1"
  >
    <FIELD LABEL="Field.Label.PlanItem">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="piDtlsList$name"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <ACTION_SET
    BOTTOM="false"
    TOP="true"
  >
    <ACTION_CONTROL
      IMAGE="NewButton"
      LABEL="ActionControl.Label.New"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="ServicePlanDelivery_addClientParticipationDefaultPlanItem"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="plannedItemID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="plannedItemID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="piDtlsList$name"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="name"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="description"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>
  <LIST>
    <!--BEGIN CR00128267, GBA-->
    <ACTION_SET TYPE="LIST_ROW_MENU">
      <!--END CR00128267-->


      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="ServicePlanDelivery_modifyClientParticipation"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="piDtlsList$dtls$dailyAttendanceID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="dailyAttendanceID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>
    <FIELD
      LABEL="Field.Label.Date"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="piDtlsList$dtls$serviceDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.ParticipationType"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="piDtlsList$dtls$attendance"
        />
      </CONNECT>
    </FIELD>


    <CONTAINER
      LABEL="Container.Label.totalTime"
      STYLE="date-time"
    >


      <FIELD ALIGNMENT="RIGHT">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="piDtlsList$piDtlsList$dtls$totalHours"
          />
        </CONNECT>
      </FIELD>


      <FIELD ALIGNMENT="CENTER">
        <CONNECT>
          <SOURCE
            NAME="TEXT"
            PROPERTY="Container.TotalTime.Separator"
          />
        </CONNECT>
      </FIELD>


      <FIELD ALIGNMENT="LEFT">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="piDtlsList$piDtlsList$dtls$totalMinutes"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>
    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="piDtlsList$dtls$recordStatus"
        />
      </CONNECT>
    </FIELD>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="ServicePlanDelivery_addClientParticipationDefaultPlanItem">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="plannedItemID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="plannedItemID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


  </LIST>
</VIEW>
