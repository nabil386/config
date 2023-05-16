<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2010, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for the modify alternate name pages.                 -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="Page.Title"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="SpecialCaution"
    NAME="ACTION"
    OPERATION="createSpecialCautionFromCase"
    PHASE="ACTION"
  />


  <SERVER_INTERFACE
    CLASS="IntegratedCase"
    NAME="DISPLAY"
    OPERATION="listCaseMembers1"
  />


  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$caseID"
    />
  </CONNECT>


  <!-- BEGIN, CR00407868, SG -->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseIDOpt"
    />
  </CONNECT>
  <!-- END, CR00407868 -->


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="1"
  >
    <FIELD
      LABEL="Field.Label.CaseMember"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="100"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="participantRoleID"
          NAME="DISPLAY"
          PROPERTY="result$participantList$nameAndAgeOpt"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$dtls$concernRoleID"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      CONTROL="CT_HIERARCHY_HORIZONTAL"
      LABEL="Cluster.Title.CategoryAndType"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="90"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$dtls$typeCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.StartDate"
      USE_BLANK="true"
      USE_DEFAULT="true"
      WIDTH="35"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$dtls$startDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.EndDate"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="35"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$dtls$endDate"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="FALSE"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00406866, VT -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00406866 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$dtls$comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
