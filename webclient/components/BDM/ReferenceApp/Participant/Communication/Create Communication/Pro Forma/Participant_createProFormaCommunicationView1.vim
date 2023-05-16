<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for the record communication details.                -->
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
    CLASS="BDMCommunication"
    NAME="ACTION"
    OPERATION="createProForma1"
    PHASE="ACTION"
  />


  <!--SERVER_INTERFACE
    CLASS="BDMParticipant"
    NAME="DISPADDRESS"
    OPERATION="readMailingAddress"
  /-->


  <ACTION_SET ALIGNMENT="CENTER">


    <ACTION_CONTROL LABEL="ActionControl.Label.Previous">
      <LINK
        DISMISS_MODAL="false"
        OPEN_MODAL="false"
        PAGE_ID="Participant_selectProFormaType"
        SAVE_LINK="false"
      >


        <!-- BEGIN, CR00264977, JAF -->
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="correspondentParticipantRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="correspondentParticipantRoleID"
          />
        </CONNECT>


        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="correspondentName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="correspondentName"
          />
        </CONNECT>


        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="correspondentParticipantRoleType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="correspondentParticipantRoleType"
          />
        </CONNECT>


        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseParticipantRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseParticipantRoleID"
          />
        </CONNECT>
        <!-- END, CR00264977, JAF -->


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


    <ACTION_CONTROL
      IMAGE="SaveButton"
      LABEL="ActionControl.Label.Save"
      TYPE="SUBMIT"
    />


    <ACTION_CONTROL
      IMAGE="CancelButton"
      LABEL="ActionControl.Label.Cancel"
    />


  </ACTION_SET>


  <PAGE_PARAMETER NAME="proFormaID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>
  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="concernRoleID"/>
  <PAGE_PARAMETER NAME="correspondentParticipantRoleID"/>
  <PAGE_PARAMETER NAME="correspondentName"/>
  <PAGE_PARAMETER NAME="templateName"/>
  <PAGE_PARAMETER NAME="proFormaVersionNo"/>
  <PAGE_PARAMETER NAME="correspondentParticipantRoleType"/>
  <PAGE_PARAMETER NAME="communicationType"/>
  <PAGE_PARAMETER NAME="caseParticipantRoleID"/>
  <!-- BEGIN, CR00145315, SK -->
  <PAGE_PARAMETER NAME="localeIdentifier"/>
  <!-- END, CR00145315 -->


  <!--CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="correspondentParticipantRoleID"
    />
    <TARGET
      NAME="DISPADDRESS"
      PROPERTY="key$maintainAddressKey$concernRoleID"
    />
  </CONNECT-->
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
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="clientParticipantRoleID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="correspondentParticipantRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="correspondentParticipantRoleID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="correspondentName"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="correspondentName"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="proFormaID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="proFormaID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="templateName"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="subject"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="proFormaVersionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="proFormaVersionNo"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="communicationType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="communicationTypeCode"
    />
  </CONNECT>


  <!-- BEGIN, CR00145315, SK -->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="localeIdentifier"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="localeIdentifier"
    />
  </CONNECT>
  
  <!--CONNECT>
    <SOURCE
      NAME="DISPADDRESS"
      PROPERTY="addressID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="addressID"
    />
  </CONNECT-->


  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="1"
  >


    <CLUSTER
      LABEL_WIDTH="30"
      NUM_COLS="1"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD LABEL="Field.Label.CorrespondentName">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="correspondentName"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.CorrespondentType"
        WIDTH="50"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="correspondentType"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


    <!--CLUSTER
      DESCRIPTION="Cluster.Description.AddressDetails"
      LABEL_WIDTH="30"
      NUM_COLS="1"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        LABEL="Field.Label.Address"
        USE_BLANK="true"
      >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="addressID"
            NAME="DISPADDRESS"
            PROPERTY="addressString"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="addressID"
          />
        </CONNECT>
      </FIELD>
      
      <CLUSTER
        LABEL_WIDTH="60"
        NUM_COLS="2"
        SHOW_LABELS="true"
        STYLE="cluster-cpr-no-border"
        TAB_ORDER="ROW"
      >
        <FIELD LABEL="Field.Label.Address">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="addressData"
            />
          </CONNECT>
        </FIELD>
      </CLUSTER>

	  <FIELD
        LABEL="Field.Label.Address"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPADDRESS"
            PROPERTY="formattedAddressData"
          />
        </CONNECT>
      </FIELD>

    </CLUSTER-->
  </CLUSTER>


  <!--CLUSTER
    LABEL_WIDTH="60"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Label.PrintNow">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="printInd"
        />
      </CONNECT>
    </FIELD>
    <FIELD CONTROL="SKIP"/>
  </CLUSTER-->


  <!-- BEGIN, CR00222172, MC -->
  <CLUSTER
    LABEL_WIDTH="60"
    NUM_COLS="2"
    TITLE="Cluster.Title.AssociatedFiles"
  >
    <FIELD LABEL="Field.Label.FileLocation">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fileLocation"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.DocumentLocation">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="documentLocation"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.FileReference">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fileReference"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.DocumentReference">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="documentReference"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <!-- END, CR00222172 -->


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
