<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2002, 2014. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2002, 2007, 2009, 2010 Curam Software Ltd.                   -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This is the included view that allows the user to change the case   -->
<!-- owner.                                                              -->
<VIEW WINDOW_OPTIONS="width=500" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE NAME="TEXT" PROPERTY="PageTitle.StaticText1"/>
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE CLASS="Case" NAME="ACTION" OPERATION="createCaseOwner" PHASE="ACTION"/>


  <ACTION_SET ALIGNMENT="CENTER" TOP="false">


    <ACTION_CONTROL IMAGE="SaveButton" LABEL="ActionControl.Label.Save" TYPE="SUBMIT"/>


    <ACTION_CONTROL IMAGE="CancelButton" LABEL="ActionControl.Label.Cancel"/>


  </ACTION_SET>


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="caseID"/>
    <TARGET NAME="ACTION" PROPERTY="caseID"/>
  </CONNECT>


  <CLUSTER LABEL_WIDTH="28">


    <CONTAINER LABEL="Container.Label.NewOwner">
      <!-- BEGIN, CR00358285, PB -->
      <FIELD LABEL="Field.Label.NewOwner" WIDTH="43">
        <!-- END, CR00358285 -->
        <CONNECT>
          <TARGET NAME="ACTION" PROPERTY="orgObjectType"/>
        </CONNECT>
      </FIELD>


      <FIELD>
        <CONNECT>
          <TARGET NAME="ACTION" PROPERTY="caseOwnerName"/>
        </CONNECT>
      </FIELD>
    </CONTAINER>


    <FIELD LABEL="Field.Label.Reason" USE_BLANK="true" WIDTH="42">
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