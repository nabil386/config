<?xml version="1.0" encoding="UTF-8"?>
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
    OPERATION="listMSWordDocumentType"
    PHASE="ACTION"
  />

<!-- TASK 8657 Select Language from Contcat Preference -->
<SERVER_INTERFACE CLASS="BDMCommunication"
		NAME="DISPLAYCONTACTPREF"
		OPERATION="getPreferredLanguageFromContactPreference" PHASE="DISPLAY" />
  
   <CONNECT>
	<SOURCE NAME="PAGE" PROPERTY="concernRoleID" />
	<TARGET NAME="DISPLAYCONTACTPREF" PROPERTY="concernRoleID$concernRoleID" />
</CONNECT>
  
<!-- TASK 8657 Select Language from Contcat Preference -->

  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="correspondentParticipantRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="concernRoleID"
    />
  </CONNECT>
  <!--CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="participantRoleID"
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

  <CLUSTER
    LABEL_WIDTH="25"
    NUM_COLS="3"
    TITLE="Cluster.Title.CommunicationType"
  >


    <FIELD
      LABEL="Field.Label.TemplateID"
      WIDTH="65"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$templateDetails$documentTemplateID"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD
      LABEL="Field.Label.TemplateName"
      WIDTH="65"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$templateDetails$name"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD
      LABEL="Field.Label.Language"
      WIDTH="65"
	  USE_BLANK="true"
    >

	<!-- TASK 8657 Select Language from Contcat Preference -->
	<CONNECT>
		<SOURCE NAME="DISPLAYCONTACTPREF"
			PROPERTY="result$preferredLanguage" />
		<!-- TASK 8657 -->
	</CONNECT>

	<!-- TASK 8657 Select Language from Contcat Preference -->	
    
    
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$language"
        />
      </CONNECT>
    </FIELD>
    
    <ACTION_SET
      TOP="false"
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
        <LINK PAGE_ID="BDMParticipant_selectMSWordType">
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
		  <CONNECT>
		    <SOURCE
		      NAME="PAGE"
		      PROPERTY="pageDescription"
		    />
		    <TARGET
		      NAME="PAGE"
		      PROPERTY="pageDescription"
		    />
		  </CONNECT>
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
		      PROPERTY="correspondentName"
		    />
		    <TARGET
		      NAME="PAGE"
		      PROPERTY="correspondentName"
		    />
		  </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>

  </CLUSTER>


  <LIST
    
  >
    <CONTAINER
      LABEL="Container.Label.Action"
      WIDTH="15"
    >
      <ACTION_CONTROL LABEL="ActionControl.Label.Select">
        <LINK
          DISMISS_MODAL="false"
          PAGE_ID="Participant_createMSWordCommunicationWithNoMandatoryAddress"
          SAVE_LINK="false"
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
              PROPERTY="correspondentName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="correspondentName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="result$dtls$list$dtls$documentTemplateID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="documentTemplateID"
            />
          </CONNECT>
		  <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="result$dtls$list$dtls$name"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="documentTemplateName"
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
	      <CONNECT>
	          <SOURCE
	            NAME="PAGE"
	            PROPERTY="pageDescription"
	          />
	          <TARGET
	            NAME="PAGE"
	            PROPERTY="pageDescription"
	          />
	      </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


    <FIELD
      LABEL="Field.Label.TemplateID"
      WIDTH="60"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$dtls$list$dtls$documentTemplateID"
        />
      </CONNECT>
    </FIELD>

	<FIELD
      LABEL="Field.Label.MSWordName"
      WIDTH="60"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$dtls$list$dtls$name"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD
      LABEL="Field.Label.Language"
      WIDTH="60"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$dtls$list$dtls$localeIdentifier"
        />
      </CONNECT>
    </FIELD>
    
  </LIST>


</VIEW>
