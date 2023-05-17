<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  OCO Source Materials
  
  PID 5725-H26
  
  Copyright IBM Corporation 2010, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2010 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to view service plan delivery closure        -->
<!-- details.                                                               -->
<VIEW WINDOW_OPTIONS="width=500" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE NAME="TEXT" PROPERTY="PageTitle.Title"/>
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE CLASS="ServicePlanDelivery" NAME="DISPLAY" OPERATION="viewClosureDetails" PHASE="DISPLAY"/>


  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="caseID"/>
    <TARGET NAME="DISPLAY" PROPERTY="key$servicePlanDeliveryKey$key$caseID"/>
  </CONNECT>


  <CLUSTER LABEL_WIDTH="45" NUM_COLS="1">


    <FIELD LABEL="Field.Label.Reason">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="reasonCode"/>
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Date">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="closureDate"/>
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER SHOW_LABELS="false" TITLE="Cluster.Title.Comments">


    <!-- BEGIN, CR00406866, VT -->
    <FIELD HEIGHT="4" LABEL="Field.Label.Comments">
      <!-- END, CR00406866 -->
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="notesText"/>
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>