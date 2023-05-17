<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2007, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2007,2009, 2010 Curam Software Ltd.                          -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows a user to modify an existing milestone configuration  -->
<!-- record. 																																-->
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


  <SERVER_INTERFACE
    CLASS="Product"
    NAME="DISPLAY"
    OPERATION="readMilestoneConfiguration"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="Product"
    NAME="ACTION"
    OPERATION="modifyMilestoneConfiguration"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="milestoneConfigurationID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="milestoneConfigurationID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$milestoneConfigurationID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="milestoneConfigurationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="milestoneConfigurationID"
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


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="name"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="name"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="recordStatus"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="recordStatus"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="creationDate"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="creationDate"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="55"
    NUM_COLS="2"
  >


    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EarliestStartDay">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="earliestStartDay"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="earliestStartDay"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.StartDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00138778, MC-->


    <FIELD
      LABEL="Field.Label.WaiverAllowed"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="waiverAllowed"
        />
      </CONNECT>


      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="waiverAllowed"
        />
      </CONNECT>
    </FIELD>


    <!--END, CR00138778-->


    <FIELD LABEL="Field.Label.Type">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="type"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="type"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Duration">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="duration"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="duration"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EndDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00138778, MC-->


    <FIELD LABEL="Field.Label.WaiverRequired">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="waiverRequired"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="waiverRequired"
        />
      </CONNECT>
    </FIELD>


    <!--END, CR00138778-->


  </CLUSTER>


  <CLUSTER LABEL_WIDTH="28">


    <FIELD
      LABEL="Field.Label.CreateEvent"
      WIDTH="80"
    >
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="addedEvent"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="addedEvent"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="addedEvent"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ExpctdEndDateEvent"
      WIDTH="80"
    >
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="expctdEndDateEvent"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="expctdEndDateEvent"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="expctdEndDateEvent"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ExpctdStDateEvent"
      WIDTH="80"
    >
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="expctdStDateEvent"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="expctdStDateEvent"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="expctdStDateEvent"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.CompleteEvent"
      WIDTH="80"
    >
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="completeEvent"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="completeEvent"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="completeEvent"
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
    >
      <!-- END, CR00406866 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
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
