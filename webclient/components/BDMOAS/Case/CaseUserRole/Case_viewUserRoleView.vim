<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2002, 2014. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002, 2007, 2009, 2010 Curam Software Ltd.               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                  -->
<!-- ===========                        -->
<!-- This is the included view displaying details of a user case role.      -->
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
    CLASS="BDMOASCase"
    NAME="DISPLAY"
    OPERATION="readAdminCaseRole"
  />


  <PAGE_PARAMETER NAME="userRoleID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="userRoleID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="maintainAdminCaseRoleIDKey$administrationCaseRoleID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="25"
    NUM_COLS="2"
  >


    <FIELD LABEL="Field.Label.Reason">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$reasonCode"
        />
      </CONNECT>
    </FIELD>
	
	<FIELD LABEL="Field.Label.IsManuallyChanged">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="isManuallyChanged"
        />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$recordStatus"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00408407, VT -->
    <FIELD
      HEIGHT="3"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00408407 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
