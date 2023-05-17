<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2002, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002-2010 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- Included view used to close an integrated case.                        -->
<VIEW WINDOW_OPTIONS="width=500" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE NAME="TEXT" PROPERTY="PageTitle.StaticText1"/>
    </CONNECT>
  </PAGE_TITLE>


  <!-- BEGIN, CR00163245, MC -->
  <SERVER_INTERFACE CLASS="IntegratedCase" NAME="ACTION" OPERATION="closeCase" PHASE="ACTION"/>


  <SERVER_INTERFACE CLASS="IntegratedCase" NAME="DISPLAY" OPERATION="countActiveTasks" PHASE="DISPLAY"/>


  <INFORMATIONAL>
    <CONNECT>
      <SOURCE NAME="DISPLAY" PROPERTY="informationMsgTxt"/>
    </CONNECT>
  </INFORMATIONAL>


  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="caseID"/>
    <TARGET NAME="DISPLAY" PROPERTY="caseID"/>
  </CONNECT>
  <!-- END, CR00163245 -->


  <ACTION_SET ALIGNMENT="CENTER">


    <ACTION_CONTROL IMAGE="SaveButton" LABEL="ActionControl.Label.Save" TYPE="SUBMIT"/>


    <ACTION_CONTROL IMAGE="CancelButton" LABEL="ActionControl.Label.Cancel"/>
  </ACTION_SET>


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>
  <PAGE_PARAMETER NAME="versionNo"/>


  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="caseID"/>
    <TARGET NAME="ACTION" PROPERTY="caseID"/>
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="versionNo"/>
    <TARGET NAME="ACTION" PROPERTY="versionNo"/>
  </CONNECT>


  <!-- BEGIN, CR00109753, SK -->
  <CLUSTER LABEL_WIDTH="25">
    <!-- END, CR00109753, SK -->


    <FIELD LABEL="Field.Label.Reason">
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="reasonCode"/>
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER SHOW_LABELS="false" TITLE="Cluster.Title.Comments">


    <!-- BEGIN, CR00406866, VT -->
    <FIELD HEIGHT="4" LABEL="Field.Label.Comments">
      <!-- END, CR00406866 -->
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="comments"/>
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>