<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for a list of communications for a product delivery  -->
<!-- on an integrated case.                                                 -->
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


  <!-- BEGIN, CR00222438, MC -->
  <SERVER_INTERFACE
    CLASS="BDMCase"
    NAME="DISPLAY"
    OPERATION="listCaseCommunication"
  />
  <!-- END, CR00222438 -->


  <ACTION_SET BOTTOM="false">
    <ACTION_CONTROL
      IMAGE="RecordCommunication"
      LABEL="ActionControl.Label.RecordCommunication"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Case_getRecordCommCorrespondent"
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
    <!-- BUG-119565, Start -->
    
    <ACTION_CONTROL IMAGE="CreateCorrespondence" LABEL="ActionControl.Label.CorrespondenceCommunication">
         <LINK OPEN_MODAL="true" PAGE_ID="BDMParticipant_createCorrespondenceCase">
            <CONNECT>
               <SOURCE NAME="PAGE" PROPERTY="caseID" />
               <TARGET NAME="PAGE" PROPERTY="caseID" />
            </CONNECT>
         </LINK>
      </ACTION_CONTROL>
	<!-- BUG-119565, End -->
    <ACTION_CONTROL
      IMAGE="CreateEmail"
      LABEL="ActionControl.Label.EmailCommunication"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Case_getEmailCorrespondent"
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


    <!--BEGIN, CR00050150, MR-->
    <!--BEGIN, HARP 68190,PN-->
    <ACTION_CONTROL
      IMAGE="CreateProforma"
      LABEL="ActionControl.Label.CreateProforma"
    >
      <!--END HARP, 68190-->
      <!--END, CR00050150-->


      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Case_getProFormaCorrespondent"
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


    <!--BEGIN, CR00050150, MR-->
    <!--BEGIN, HARP 68190,PN-->
    <ACTION_CONTROL
      IMAGE="CreateMSWord"
      LABEL="ActionControl.Label.CreateMSWord"
    >
      <!--END HARP, 68190-->
      <!--END, CR00050150-->


      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Case_getMSWordCorrespondentCaseMember"
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


  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$caseID"
    />
  </CONNECT>

  <LIST>
	
	<!-- BEGIN, CR00222438, MC -->
    <INCLUDE FILE_NAME="BDMParticipant_viewListRowMenuActionsView.vim"/>
    <!-- END, CR00222438 -->

    <FIELD
      LABEL="Field.Label.Subject"
      WIDTH="21"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="subjectText"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="12"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="communicationFormat"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Name"
      WIDTH="21"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="correspondentName"
        />
      </CONNECT>
      <LINK PAGE_ID="Participant_resolveRoleHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="correspondentConcernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="correspondentConcernRoleType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="participantType"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    
    <FIELD LABEL="Field.Label.TrackingNumber" WIDTH="12">
         <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="trackingNumber" />
         </CONNECT> 
         <LINK PAGE_ID="BDMParticipant_resolveViewCommunicationExternal" OPEN_MODAL="true" WINDOW_OPTIONS="width=1000">
           <CONDITION>
            	<IS_TRUE NAME="DISPLAY" PROPERTY="result$communicationDtls$enableTrackingInd" />
            </CONDITION> 
            <CONNECT>
               <SOURCE NAME="DISPLAY" PROPERTY="workItemID" />
               <TARGET NAME="PAGE" PROPERTY="workItemID" />
            </CONNECT>
            <CONNECT>
               <SOURCE NAME="DISPLAY" PROPERTY="editActionInd" />
               <TARGET NAME="PAGE" PROPERTY="editActionInd" />
            </CONNECT>
            <CONNECT>
               <SOURCE NAME="DISPLAY" PROPERTY="communicationID" />
               <TARGET NAME="PAGE" PROPERTY="communicationID" />
            </CONNECT>
         </LINK>
      </FIELD>
    <FIELD
      LABEL="Field.Label.CommunicationStatus"
      WIDTH="21"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="communicationStatus"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Date"
      WIDTH="13"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="communicationDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="12"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="statusCode"
        />
      </CONNECT>
    </FIELD>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="BDMParticipant_resolveViewCommunication1">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="communicationID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="communicationID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="correspondentConcernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="correspondentConcernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="communicationStatus"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="status"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="communicationFormat"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="communicationFormat"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>
  </LIST>
</VIEW>
