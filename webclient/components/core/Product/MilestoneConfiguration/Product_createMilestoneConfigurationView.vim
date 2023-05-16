<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2007, 2014. All Rights Reserved.

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
<!-- The included view for creating a milestone     .                       -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CLUSTER
    LABEL_WIDTH="55"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EarliestStartDay">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="earliestStartDay"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.StartDate">
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
        <TARGET
          NAME="ACTION"
          PROPERTY="waiverAllowed"
        />
      </CONNECT>
    </FIELD>


    <!--END, CR00138778-->


    <FIELD LABEL="Field.Label.type">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="type"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Duration">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="duration"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.EndDate"
      USE_DEFAULT="false"
    >
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
        <TARGET
          NAME="ACTION"
          PROPERTY="waiverRequired"
        />
      </CONNECT>
    </FIELD>


    <!--END, CR00138778-->


  </CLUSTER>


  <CLUSTER LABEL_WIDTH="30.5">


    <FIELD
      LABEL="Field.Label.AddedEvent"
      WIDTH="80"
    >
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
        <TARGET
          NAME="ACTION"
          PROPERTY="completeEvent"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Label.Comments"
  >


    <!-- BEGIN, CR00417389, VT -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00417389 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
