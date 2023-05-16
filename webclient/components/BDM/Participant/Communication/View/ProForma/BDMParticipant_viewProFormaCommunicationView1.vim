<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Views pro forma communication details.                                 -->
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
    NAME="DISPLAY"
    OPERATION="readProFormaAndCaseMember"
  />
  
  <SERVER_INTERFACE
    CLASS="BDMParticipant"
    NAME="DISPADDRESS"
    OPERATION="readMailingAddress"
  />


  <PAGE_PARAMETER NAME="communicationID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>
  <PAGE_PARAMETER NAME="correspondentConcernRoleID"/>


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
      PROPERTY="correspondentConcernRoleID"
    />
    <TARGET
      NAME="DISPADDRESS"
      PROPERTY="key$maintainAddressKey$concernRoleID"
    />
  </CONNECT>
  
  <CLUSTER
    LABEL_WIDTH="38"
    NUM_COLS="2"
  >
	  <FIELD LABEL="Field.Label.TemplateID">
	  	<CONNECT>
	    	<SOURCE
	     		NAME="DISPLAY"
	      		PROPERTY="templateID"
			/>
	  	</CONNECT>
	  </FIELD>
	  
	  <FIELD LABEL="Field.Label.Auther">
	  	<CONNECT>
	    	<SOURCE
	     		NAME="DISPLAY"
	      		PROPERTY="userFullName"
			/>
	  	</CONNECT>
	  </FIELD>
  </CLUSTER>

  <CLUSTER
    LABEL_WIDTH="38"
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >


    <FIELD LABEL="Field.Label.CaseMember">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="clientConcernRoleName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Method">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="methodTypeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD CONTROL="SKIP"/>


    <CONTAINER
      LABEL="Field.Label.CommunicationStatus"
      STYLE="action-link-button"
    >


      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="communicationStatus"
          />
        </CONNECT>
      </FIELD>


      <ACTION_CONTROL LABEL="ActionControl.PrintButton.label">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Participant_confirmPrintForProFormaCommunication"
          SAVE_LINK="true"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="readProFormaCommDetails$communicationID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="communicationID"
            />
          </CONNECT>


          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="localeIdentifier"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="localeIdentifier"
            />
          </CONNECT>
        </LINK>


      </ACTION_CONTROL>
    </CONTAINER>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="38"
    NUM_COLS="2"
    TITLE="Cluster.Title.Correspondent"
  >


    <FIELD LABEL="Field.Label.CorrespondentName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="correspondentName"
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


    <FIELD LABEL="Field.Label.CorrespondentType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="correspondentType"
        />
      </CONNECT>
    </FIELD>


    <FIELD CONTROL="SKIP"/>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="38"
    NUM_COLS="2"
    TITLE="Cluster.Title.CommunicationType"
  >


    <FIELD LABEL="Field.Label.FileLocation">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
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
    </FIELD>


    <FIELD LABEL="Field.Label.FileReference">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
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
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00416277 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>
  
  <INCLUDE FILE_NAME="BDMCommunication_viewCancelReason.vim" />
  
</VIEW>
