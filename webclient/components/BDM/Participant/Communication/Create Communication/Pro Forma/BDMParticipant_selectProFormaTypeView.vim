<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for the create template communication.               -->
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

<!-- START :TASK 8657 Select Written Language from Contcat Preference --> 
	<SERVER_INTERFACE CLASS="BDMCommunication"
		NAME="DISPLAYCONTACTPREF"
		OPERATION="getPreferredLanguageFromContactPreference" PHASE="DISPLAY" />
  
<!-- END :TASK 8657 Select Written Language from Contcat Preference --> 
  <SERVER_INTERFACE
    CLASS="BDMCommunication"
    NAME="ACTION"
    OPERATION="listTemplateByTypeAndParticipant"
    PHASE="ACTION"
  />

  <PAGE_PARAMETER NAME="pageDescription"/>
  <PAGE_PARAMETER NAME="concernRoleID"/>
  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="correspondentParticipantRoleID"/>
  <PAGE_PARAMETER NAME="correspondentName"/>
  <PAGE_PARAMETER NAME="correspondentParticipantRoleType"/>
  <PAGE_PARAMETER NAME="caseParticipantRoleID"/>
  
  
  <!-- START :TASK 8657 Select Written Language from Contcat Preference --> 
  <CONNECT>
	<SOURCE NAME="PAGE" PROPERTY="concernRoleID" />
	<TARGET NAME="DISPLAYCONTACTPREF" PROPERTY="concernRoleID$concernRoleID" />
</CONNECT>
<!-- END :TASK 8657 Select Written Language from Contcat Preference --> 

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
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseID"
    />
  </CONNECT>


  <ACTION_SET
    ALIGNMENT="CENTER"
    TOP="false"
  >


    <ACTION_CONTROL LABEL="ActionControl.PrevButton.label">
      <LINK
        DISMISS_MODAL="false"
        OPEN_MODAL="false"
        PAGE_ID="Participant_getProFormaCorrespondent"
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
            PROPERTY="pageDescription"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
	
    <ACTION_CONTROL
      IMAGE="CancelButton"
      LABEL="ActionControl.Label.Cancel"
    />
  </ACTION_SET>


  <CLUSTER>
  
    <INCLUDE FILE_NAME="BDMCommunication_selectProFormaTypeFieldView.vim"/>
 
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
        <LINK PAGE_ID="Participant_selectProFormaType" SAVE_LINK="false" >
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
          PAGE_ID="Participant_createProFormaCommunication1"
          SAVE_LINK="false"
        >
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="result$dtls$dtls$templateID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="proFormaID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="localeIdentifier"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="localeIdentifier"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="templateType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="communicationType"
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
              PROPERTY="result$dtls$dtls$templateName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="templateName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="latestVersion"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="proFormaVersionNo"
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
          PROPERTY="result$dtls$dtls$templateIDCode"
        />
      </CONNECT>
    </FIELD>

	<FIELD
      LABEL="Field.Label.ProFormaName"
      WIDTH="60"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$dtls$dtls$templateName"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD
      LABEL="Field.Label.Language"
      WIDTH="30"
    >
      <CONNECT>
        <!--SOURCE
          NAME="ACTION"
          PROPERTY="result$searchTemplatesByConcernAndTypeResult$xslTemplateDetailsListOut$dtls$localeIdentifier"
        /-->
		<SOURCE
          NAME="ACTION"
          PROPERTY="result$dtls$dtls$localeIdentifier"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="60"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$dtls$dtls$type"
        />
      </CONNECT>
    </FIELD>
    
  </LIST>

</VIEW>
