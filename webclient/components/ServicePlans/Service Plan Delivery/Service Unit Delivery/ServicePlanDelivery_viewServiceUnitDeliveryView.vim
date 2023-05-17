<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2008, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008, 2011 Curam Software Ltd.                                                 -->
<!-- All rights reserved.                                                                     -->
<!-- This software is the confidential and proprietary information of Curam                   -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose                      -->
<!-- such Confidential Information and shall use it only in accordance with                   -->
<!-- the terms of the license agreement you entered into with Curam                           -->
<!-- Software.                                                                                -->
<!-- Description                                                                              -->
<!-- ===========                                                                              -->
<!-- This page allows the user to view the details of a service unit delivery.                -->
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
    CLASS="ServicePlanDelivery"
    NAME="DISPLAY"
    OPERATION="readServiceUnitDeliveryDetails"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="plannedItemID"/>
  <PAGE_PARAMETER NAME="serviceUnitDeliveryID"/>
  <PAGE_PARAMETER NAME="description"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="serviceUnitDeliveryID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="readServiceUnitDeliveryKey$key$serviceUnitDeliveryKey$serviceUnitDeliveryID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="plannedItemID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="readServiceUnitDeliveryKey$key$plannedItemKey$plannedItemIDKey$plannedItemID"
    />
  </CONNECT>


  <CLUSTER NUM_COLS="2">


    <FIELD LABEL="Field.Label.DateRecorded">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="recordedDate"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Label.Comments"
  >
    <!-- BEGIN, CR00416277, VT -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00416277 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
