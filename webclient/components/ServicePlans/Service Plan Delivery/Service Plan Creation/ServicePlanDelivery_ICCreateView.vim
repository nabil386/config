<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2004, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2004-2010, 2012 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to create a service plan delivery.           -->
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
    CLASS="ServicePlanDelivery"
    NAME="DISPLAY"
    OPERATION="getAvailableICMembersAndSPTypes"
  />
  <!--BEGIN CR00109001, GBA-->
  <SERVER_INTERFACE
    CLASS="IntegratedCase"
    NAME="DISPLAY_PARTICIPANTS"
    OPERATION="listActiveCaseMembers"
  />
  <!--END CR00109001-->


  <SERVER_INTERFACE
    CLASS="ServicePlanDelivery"
    NAME="ACTION"
    OPERATION="ICCreate"
    PHASE="ACTION"
  />


  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="description"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$servicePlanIntegratedCaseKey$servicePlanIntegratedCaseKey$caseID"
    />
  </CONNECT>


  <!--BEGIN CR00109001, GBA-->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY_PARTICIPANTS"
      PROPERTY="key$caseID"
    />
  </CONNECT>
  <!--END CR00109001-->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="key$servicePlanIntegratedCaseKey$servicePlanIntegratedCaseKey$caseID"
    />
  </CONNECT>


  <ACTION_SET
    ALIGNMENT="CENTER"
    TOP="false"
  >


    <ACTION_CONTROL
      IMAGE="SaveButton"
      LABEL="ActionControl.Label.Save"
      TYPE="SUBMIT"
    >


      <!-- BEGIN, CR00146960, DJ-->
      <LINK PAGE_ID="ServicePlanDelivery_home1">
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="result$servicePlanDeliveryKey$key$caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
      <!-- END,CR00146960-->
    </ACTION_CONTROL>


    <ACTION_CONTROL
      IMAGE="SaveAndUseTemplate"
      LABEL="ActionControl.Label.SaveAndUseTemplate"
      TYPE="SUBMIT"
    >
      <LINK
        DISMISS_MODAL="false"
        OPEN_MODAL="false"
        PAGE_ID="ServicePlanDelivery_useTemplate"
        SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="result$servicePlanDeliveryKey$key$caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="description"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      IMAGE="CancelButton"
      LABEL="ActionControl.Label.Cancel"
    />


  </ACTION_SET>


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
  >


    <!--BEGIN CR00109001, GBA-->
    <FIELD LABEL="Field.Label.PrimaryPlanParticipant">
      <!--END CR00109001-->
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="participantRoleID"
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="concernRoleID"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Type">
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="servicePlanID"
          NAME="DISPLAY"
          PROPERTY="servicePlanType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="servicePlanID"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <!--BEGIN CR00109001, GBA-->
  <!-- BEGIN, CR00235147, SS -->
  <LIST
    
    TITLE="List.Title.CaseParticipantList"
  >
    <!-- END, CR00235147 -->
    <CONTAINER
      ALIGNMENT="CENTER"
      WIDTH="10"
    >
      <WIDGET TYPE="MULTISELECT">
        <WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY_PARTICIPANTS"
              PROPERTY="participantRoleID"
            />
          </CONNECT>
        </WIDGET_PARAMETER>


        <WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="planParticipants"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
      </WIDGET>
    </CONTAINER>
    <FIELD
      LABEL="Field.Label.Name"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_PARTICIPANTS"
          PROPERTY="participantList$name"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00306738, VT -->
    <FIELD
      LABEL="Field.Label.ParticipantType"
      WIDTH="20"
    >
      <!-- END, CR00306738 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_PARTICIPANTS"
          PROPERTY="typeCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.StartDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_PARTICIPANTS"
          PROPERTY="fromDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.EndDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_PARTICIPANTS"
          PROPERTY="toDate"
        />
      </CONNECT>
    </FIELD>
  </LIST>
  <!--END CR00109001-->


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00406866, VT -->
    <FIELD HEIGHT="4" LABEL="Field.Label.Comments">
      <!-- END, CR00406866 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
