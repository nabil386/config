<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for the create Microsoft Word communication details.        -->
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
    CLASS="Communication"
    NAME="ACTION"
    OPERATION="createMSWordCommunicationWithNoMandatoryAddress"
    PHASE="ACTION"
  />


  <SERVER_INTERFACE
    CLASS="BDMParticipant"
    NAME="DISPADDRESS"
    OPERATION="readMailingAddress"
  />


  <!--SERVER_INTERFACE
    CLASS="System"
    NAME="DISPLAYTEM"
    OPERATION="listDocumentTemplatesByRelatedID"
  /-->


  <PAGE_PARAMETER NAME="pageDescription"/>
  <PAGE_PARAMETER NAME="concernRoleID"/>
  <PAGE_PARAMETER NAME="correspondentParticipantRoleID"/>
  <PAGE_PARAMETER NAME="correspondentName"/>
  <PAGE_PARAMETER NAME="correspondentParticipantRoleType"/>
  <PAGE_PARAMETER NAME="documentTemplateID"/>
  <PAGE_PARAMETER NAME="documentTemplateName"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="correspondentParticipantRoleID"
    />
    <TARGET
      NAME="DISPADDRESS"
      PROPERTY="key$maintainAddressKey$concernRoleID"
    />
  </CONNECT>


  <!--CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="correspondentParticipantRoleID"
    />
    <TARGET
      NAME="DISPLAYTEM"
      PROPERTY="concernRoleID"
    />
  </CONNECT-->


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="participantRoleID"
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
      PROPERTY="documentTemplateID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="dtls$dtls$templateID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPADDRESS"
      PROPERTY="addressID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="addressID"
    />
  </CONNECT>


  <ACTION_SET ALIGNMENT="CENTER">
    <ACTION_CONTROL
      IMAGE="SaveButton"
      LABEL="ActionControl.Label.Save"
      TYPE="SUBMIT"
    >
      <LINK
        PAGE_ID="Participant_controlMSWord"
        SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="result$dtls$communicationID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="communicationID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="dtls$dtls$templateID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="templateID"
          />
        </CONNECT>
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
      </LINK>
    </ACTION_CONTROL>
    <ACTION_CONTROL
      IMAGE="CancelButton"
      LABEL="ActionControl.Label.Cancel"
    >
    </ACTION_CONTROL>
  </ACTION_SET>


  <CLUSTER>


    <CLUSTER
      LABEL_WIDTH="30"
      NUM_COLS="1"
      STYLE="cluster-cpr-no-border"
    >
      <FIELD LABEL="Field.Label.Subject">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="subjectText"
          />
        </CONNECT>
      </FIELD>


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
            PROPERTY="correspondentTypeCode"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.Address">
	    <CONNECT>
          <SOURCE
            NAME="DISPADDRESS"
            PROPERTY="formattedAddressData"
          />
        </CONNECT>
	  </FIELD>
    </CLUSTER>


    <!--CLUSTER
      DESCRIPTION="Cluster.Description.AddressDetails"
      LABEL_WIDTH="30"
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


    </CLUSTER-->
  </CLUSTER>


  <CLUSTER
    DESCRIPTION="Cluster.Template.Description"
    LABEL_WIDTH="30"
  >


    <FIELD
      LABEL="Field.Label.TemplateName"
      WIDTH="50"
    >


      <!--CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="documentTemplateID"
          NAME="DISPLAYTEM"
          PROPERTY="name"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dtls$dtls$templateID"
        />
      </CONNECT-->
      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="documentTemplateName"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


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
          PROPERTY="dtls$fileLocation"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DocumentLocation">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dtls$documentLocation"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.FileReference">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dtls$fileReferenceNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DocumentReference">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dtls$documentRefNumber"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <!-- END, CR00222172 -->


</VIEW>
