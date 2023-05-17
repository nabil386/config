<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010-2011 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Evidence dashboard details.                                            -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_PARAMETER NAME="applicationID"/>


  <SERVER_INTERFACE
    CLASS="ApplicationEvidence"
    NAME="DISPLAY"
    OPERATION="getEvidenceDashboardDataWithRelationshipAndFilter"
  />


  <SERVER_INTERFACE
    CLASS="ApplicationEvidence"
    NAME="ACTION"
    OPERATION="getEvidenceDashboardDataWithRelationshipAndFilter"
    PHASE="ACTION"
  />


  <SERVER_INTERFACE
    CLASS="ApplicationEvidence"
    NAME="DISPLAYPARTICIPANTS"
    OPERATION="getEvidenceParticipantListForCase"
  />


  <SERVER_INTERFACE
    CLASS="ApplicationEvidence"
    NAME="GETPARTICIPANT"
    OPERATION="getPrimaryCaseParticipantDetails"
    PHASE="DISPLAY"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="applicationID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="applicationID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="applicationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="applicationID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="applicationID"
    />
    <TARGET
      NAME="DISPLAYPARTICIPANTS"
      PROPERTY="applicationID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="applicationID"
    />
    <TARGET
      NAME="GETPARTICIPANT"
      PROPERTY="applicationID"
    />
  </CONNECT>


  <INCLUDE FILE_NAME="ApplicationEvidence_IncomingEvidenceMsgView.vim"/>
  <FIELD>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="evidenceTypes"
      />
    </CONNECT>
  </FIELD>


  <CLUSTER STYLE="evidence-dashboard-cluster">
    <CONTAINER STYLE="inner-span-action-control-visibility-hidden">
      <ACTION_CONTROL
        IMAGE="SearchButton"
        LABEL="ActionControl.Label.List"
        TYPE="SUBMIT"
      >
        <LINK PAGE_ID="THIS"/>
      </ACTION_CONTROL>
    </CONTAINER>
  </CLUSTER>
</VIEW>
