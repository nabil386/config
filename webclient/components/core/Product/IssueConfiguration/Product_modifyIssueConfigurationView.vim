<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2007, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2007, 2010-2011 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows a user to modify an existing  issue configuration record. -->
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
    CLASS="Product"
    NAME="DISPLAY"
    OPERATION="readIssueConfiguration"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="Product"
    NAME="ACTION"
    OPERATION="modifyIssueConfiguration"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="issueConfigurationID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="issueConfigurationID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$issueConfigurationID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="issueConfigurationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="issueConfigurationID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="type"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="type"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="creationDate"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="creationDate"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="recordStatus"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="recordStatus"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="47"
    NUM_COLS="2"
  >


    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="75"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="type"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.StartDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Priority"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="priority"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="priority"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.HomePageIdentifier">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="homePageIdentifier"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="homePageIdentifier"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EndDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00187598, SS -->
    <FIELD
      LABEL="Field.Title.OwnershipStrategy"
      WIDTH="98"
    >
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="result$dtls$dtls$ownershipStrategyName"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$dtls$ownershipStrategyName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$dtls$dtls$ownershipStrategyName"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00187598 -->
    <!-- BEGIN, CR00283741, KOH -->
    <FIELD LABEL="Field.Label.Appealable">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="notAppealableInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="notAppealableInd"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00283741 -->


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="23.5"
    TITLE="Cluster.Title.EventDetails"
  >


    <FIELD
      LABEL="Field.Label.CreateEvent"
      WIDTH="98"
    >
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="eventClassAndType"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="eventClassAndType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="eventClassAndType"
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
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
