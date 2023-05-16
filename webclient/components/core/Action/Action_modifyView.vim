<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2008, 2014. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008-2010 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                    -->
<!-- This software is the confidential and proprietary information of Curam  -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose     -->
<!-- such Confidential Information and shall use it only in accordance with  -->
<!-- the terms of the license agreement you entered into with Curam          -->
<!-- Software.                                                               -->
<!-- Description                                                             -->
<!-- ===========                                                             -->
<!-- The included view to modify an action details.                          -->
<?curam-deprecated Since Curam 6.0, replaced by Action_modifyActionView.vim?>
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
    CLASS="Action"
    NAME="DISPLAY"
    OPERATION="readAction"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="Action"
    NAME="ACTION"
    OPERATION="modifyAction"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="actionID"/>
  <PAGE_PARAMETER NAME="description"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="actionID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$actionID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$dtls$actionID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="actionID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="ownerConcernRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="ownerConcernRoleID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="ownerUserName"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="ownerUserName"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="ownerType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="ownerType"
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


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
    TITLE="Cluster.Label.Details"
  >


    <FIELD LABEL="Field.Label.SituationCategory">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="situationCategory"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="situationCategory"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.SituationRequiringAction">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="situationRequiringAction"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="situationRequiringAction"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Action">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="action"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="action"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Owner">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="ownerName"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Action_resolveOwnerHome"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="ownerConcernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="ownerConcernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="ownerName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="ownerName"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="ownerType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="ownerType"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD LABEL="Field.Label.ExpectedEndDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="expectedEndDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="expectedEndDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ActualEndDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="actualEndDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="actualEndDate"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00145651, GYH -->
    <FIELD
      LABEL="Field.Label.Outcome"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <!-- END, CR00145651 -->


      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="outcome"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="outcome"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="1"
    SHOW_LABELS="false"
    TITLE="Cluster.Label.Comments"
  >


    <!-- BEGIN, CR00417389, VT -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00417389 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
