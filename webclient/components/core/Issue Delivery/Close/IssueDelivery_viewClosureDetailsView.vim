<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  OCO Source Materials
 
  PID 5725-H26
 
  Copyright IBM Corporation 2007, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2007, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Included view used to view closure details for a product delivery      -->
<!-- closure event.                                                         -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE NAME="TEXT" PROPERTY="PageTitle.StaticText1"/>
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE CLASS="IssueDelivery" NAME="DISPLAY" OPERATION="readClosureDetails1"/>


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="caseID"/>
    <TARGET NAME="DISPLAY" PROPERTY="details$caseID"/>
  </CONNECT>


  <CLUSTER NUM_COLS="2">


    <FIELD LABEL="Field.Label.Reason">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$reasonCode"/>
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ClosureDate">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$closureDate"/>
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER SHOW_LABELS="false" TITLE="Cluster.Title.Comments">


    <!-- BEGIN, CR00406866, VT -->
    <FIELD LABEL="Field.Label.Comments">
      <!-- END, CR00406866 -->
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="noteText"/>
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>