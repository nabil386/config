<?xml version="1.0" encoding="UTF-8"?>
<!--
    Licensed Materials - Property of IBM
    
    Copyright IBM Corporation 2013. All Rights Reserved.
    
    US Government Users Restricted Rights - Use, duplication or disclosure 
    restricted by GSA ADP Schedule Contract with IBM Corp.
-->
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
  <PAGE_PARAMETER NAME="finInstructionID"/>


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


  </CLUSTER>


  <CLUSTER>
    <ACTION_SET
      ALIGNMENT="CENTER"
      BOTTOM="false"
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
          PAGE_ID="ProductDelivery_getParticipantLiabilityWizard"
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
              NAME="PAGE"
              PROPERTY="finInstructionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="finInstructionID"
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
      WIDTH="10"
    >
      <ACTION_CONTROL LABEL="ActionControl.Label.Select">
        <LINK
          DISMISS_MODAL="false"
          PAGE_ID="Financial_createAppliedFixedDeductionWizard"
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
          <CONNECT>
            <SOURCE
              NAME="CONSTANT"
              PROPERTY="reissue.wizardStateID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="wizardStateID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="finInstructionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="finInstructionID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


    <FIELD
      LABEL="Field.Label.CaseReference"
      WIDTH="15"
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
      WIDTH="22"
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
