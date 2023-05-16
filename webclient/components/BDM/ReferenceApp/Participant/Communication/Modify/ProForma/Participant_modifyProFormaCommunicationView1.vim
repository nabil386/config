<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The page to modify the details of a pro forma communication.           -->
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
    NAME="DISPLAY"
    OPERATION="readProForma1"
  />


  <SERVER_INTERFACE
    CLASS="Communication"
    NAME="ACTION"
    OPERATION="modifyProForma1"
    PHASE="ACTION"
  />


  <!--SERVER_INTERFACE
    CLASS="Participant"
    NAME="DISPADDRESS"
    OPERATION="listAddressString"
  /-->
  
  <SERVER_INTERFACE
    CLASS="BDMParticipant"
    NAME="DISPADDRESS"
    OPERATION="readMailingAddress"
  />


  <PAGE_PARAMETER NAME="communicationID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>
  <!-- BEGIN, CR00145755, SK -->
  <PAGE_PARAMETER NAME="localeIdentifier"/>
  <!-- END, CR00145755 -->
  <PAGE_PARAMETER NAME="correspondentConcernRoleID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="correspondentConcernRoleID"
    />
    <TARGET
      NAME="DISPADDRESS"
      PROPERTY="key$maintainAddressKey$concernRoleID"
    />
  </CONNECT>

  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="communicationID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="proFormaCommKey$communicationID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="communicationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="communicationID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="clientParticipantRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="clientParticipantRoleID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="methodTypeCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="methodTypeCode"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="correspondentParticipantRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="correspondentParticipantRoleID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="correspondentType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="correspondentType"
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
      PROPERTY="statusCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="statusCode"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="correspondentName"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="correspondentName"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="communicationFormat"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="communicationFormat"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="communicationTypeCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="communicationTypeCode"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="subject"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="subject"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="proFormaID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="proFormaID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="proFormaVersionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="proFormaVersionNo"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="proFormaInd"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="proFormaInd"
    />
  </CONNECT>


  <!-- BEGIN, CR00145770, SK -->
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="localeIdentifier"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="localeIdentifier"
    />
  </CONNECT>
  <!-- END, CR00145770 -->
  
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="communicationStatus"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="communicationStatus"
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


  <CLUSTER
    LABEL_WIDTH="25"
    NUM_COLS="1"
  >


    <CLUSTER
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD LABEL="Field.Label.CorrespondentName">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="correspondentName"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


    <!--FIELD
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
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="addressID"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="addressID"
        />
      </CONNECT>
	    <CONNECT>
	      <SOURCE
	        NAME="DISPADDRESS"
	        PROPERTY="formattedAddressData"
	      />
	    </CONNECT>
    </FIELD-->


    <CLUSTER
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD LABEL="Field.Label.CommunicationStatus">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="communicationStatus"
          />
        </CONNECT>
        <!--CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="communicationStatus"
          />
        </CONNECT-->
      </FIELD>
      <!--FIELD LABEL="Field.Label.Date">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="communicationDate"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="communicationDate"
          />
        </CONNECT>
      </FIELD-->
    </CLUSTER>


  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.AssociatedFiles"
  >


    <FIELD LABEL="Field.Label.FileLocation">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fileLocation"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fileLocation"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DocumentLocation">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="documentLocation"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="documentLocation"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.FileReference">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fileReference"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fileReference"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DocumentReference">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="documentReference"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="documentReference"
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
