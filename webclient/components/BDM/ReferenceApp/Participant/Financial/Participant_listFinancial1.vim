<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This Page lists all financials for a particular participant            -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="BDMParticipant"
    NAME="DISPLAY"
    OPERATION="listParticipantFinancial1"
  />
  <!-- BEGIN, CR00321600, AC-->
  <SERVER_INTERFACE
    CLASS="Case"
    NAME="DISPLAY1"
    OPERATION="getCaseIDByConcernRoleID"
  />
  <!-- END, CR00321600 -->
  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="concernRoleID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="concernRoleID"
    />
  </CONNECT>
  <!-- BEGIN, CR00321600, AC-->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="DISPLAY1"
      PROPERTY="concernRoleID"
    />
  </CONNECT>
  <!-- END, CR00321600 -->
  <CLUSTER
    SHOW_LABELS="false"
    STYLE="tab-renderer"
  >


    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="statusInd"
      />
    </CONDITION>


    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="data"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <ACTION_SET BOTTOM="false">


    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY"
        PROPERTY="statusInd"
      />
    </CONDITION>


    <ACTION_CONTROL
      IMAGE="NewAccountAdjustmentButton"
      LABEL="ActionControl.Label.NewAccountAdjustment"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Participant_newAccountAdjustment"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="contextDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
    
    
      <ACTION_CONTROL
      LABEL="ActionControl.Label.AddManualOverPayment"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="BDMManualOverPaymentWizard_enterBalance"
      >
        <CONNECT>
          <SOURCE
           NAME="PAGE"
            PROPERTY="concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
      
        <CONNECT>
          <SOURCE
           NAME="CONSTANT" PROPERTY="ConstantwizardStateID" />
          <TARGET
            NAME="PAGE"
            PROPERTY="wizardStateID"
          />
        </CONNECT>
      
      </LINK>
    </ACTION_CONTROL>


  </ACTION_SET>


  <!-- BEGIN, CR00321600, AC -->
  <INCLUDE FILE_NAME="Financial_listParticipantFinInstructionView.vim"/>
  <!-- END, CR00321600 -->


</VIEW>
