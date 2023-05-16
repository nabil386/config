<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2009-2011 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Page to view the planned group details                                 -->
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
    CLASS="ServicePlanDelivery"
    NAME="DISPLAY"
    OPERATION="viewPlanGroupDetails"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="description"/>
  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="plannedGroupID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="plannedGroupID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$plannedGroupID"
    />
  </CONNECT>

  <!-- BEGIN, CR00236498, MR -->
  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments">
  <!-- END, CR00236498 -->
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
