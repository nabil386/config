<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM

  PID 5725-H26
 
  Copyright IBM Corporation 2009, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2009, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for the modify organization unit milestone waiver    -->
<!-- approval pages.                                                        -->
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
    CLASS="MaintainMilestoneWaiverApproval"
    NAME="DISPLAY"
    OPERATION="viewOrgUnitMWApproval"
  />


  <SERVER_INTERFACE
    CLASS="MaintainMilestoneWaiverApproval"
    NAME="ACTION"
    OPERATION="modifyOrgUnitMWApproval"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="milestoneApprovalCheckID"/>
  <PAGE_PARAMETER NAME="organisationUnitID"/>
  <PAGE_PARAMETER NAME="description"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="milestoneApprovalCheckID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$dtls$dtls$milestoneWaiverApprovalCheckID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="organisationUnitID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="organisationUnitID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
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
      PROPERTY="status"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="status"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="milestoneApprovalCheckID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$dtls$dtls$milestoneWaiverApprovalCheckID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="organisationUnitID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$dtls$dtls$organisationUnitID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="1"
  >


    <FIELD
      LABEL="Field.Label.Percentage"
      WIDTH="4"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="percentage"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$dtls$dtls$percentage"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Milestone"
      WIDTH="50"
    >


      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="milestoneName"
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
          PROPERTY="details$dtls$dtls$comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
