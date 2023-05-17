<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
 PID 5725-H26
 
  Copyright IBM Corporation 2012, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2011 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page finds the client to associate with an applied deduction      -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="IntegratedCase"
    NAME="DISPLAY"
    OPERATION="listDeductionCaseParticipantWithoutProspect"
  />


  <SERVER_INTERFACE
    CLASS="ProductDelivery"
    NAME="DISPLAYPARTICIPANTS"
    OPERATION="readAppliedDeductionParticipantsWithoutProspect"
  />


  <SERVER_INTERFACE
    CLASS="ProductDelivery"
    NAME="ACTION"
    OPERATION="listActiveLiabilitiesForConcernRole"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="23"
    NUM_COLS="1"
  >
    <FIELD
      LABEL="Field.Label.CaseMember"
      USE_BLANK="TRUE"
      WIDTH="30"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="participantRoleID"
          NAME="DISPLAY"
          PROPERTY="nameAndAgeOpt"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="caseParticipantConcernRoleID"
        />
      </CONNECT>
    </FIELD>
    <CONTAINER LABEL="Field.Label.SelectOther">
      <FIELD WIDTH="20">
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="participantType"
            NAME="DISPLAYPARTICIPANTS"
            PROPERTY="participantDescription"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="concernRoleType"
          />
        </CONNECT>
      </FIELD>
      <FIELD WIDTH="50">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>


  </CLUSTER>
  <CLUSTER>
    <ACTION_SET
      ALIGNMENT="CENTER"
      TOP="FALSE"
    >
      <ACTION_CONTROL
        DEFAULT="true"
        IMAGE="SearchButton"
        LABEL="ActionControl.Label.Search"
        TYPE="SUBMIT"
      >
        <LINK PAGE_ID="THIS"/>
      </ACTION_CONTROL>


      <ACTION_CONTROL
        IMAGE="ResetButton"
        LABEL="ActionControl.Label.Reset"
      >
        <LINK
          DISMISS_MODAL="false"
          PAGE_ID="ProductDelivery_getParticipantAndLiabilityForVariableWizard"
          SAVE_LINK="false"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>
  </CLUSTER>


  <LIST
    PAGINATED="False"
    
    TITLE="Cluster.Title.ExistingLiabilities"
  >


    <CONTAINER
      LABEL="Container.Label.Action"
      WIDTH="8"
    >
      <ACTION_CONTROL LABEL="ActionControl.Label.Select">
        <LINK
          DISMISS_MODAL="false"
          PAGE_ID="ProductDelivery_createAppliedVariableDeductionWizard"
          SAVE_LINK="false"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="relatedCaseID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


    <FIELD
      LABEL="Field.Label.CaseReference"
      WIDTH="14"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="caseReference"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="productType"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.StartDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.OriginalAmount"
      WIDTH="18"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="originalAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.BalOutstanding"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="outstandingAmount"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
