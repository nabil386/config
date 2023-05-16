<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for a list of participant communications.            -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">

   <PAGE_TITLE>
      <CONNECT>
         <SOURCE NAME="TEXT" PROPERTY="PageTitle.StaticText1" />
      </CONNECT>
   </PAGE_TITLE>   
  
   <SERVER_INTERFACE CLASS="BDMParticipant" NAME="DISPLAY" OPERATION="listCommunication" PHASE="DISPLAY" />   
   <SERVER_INTERFACE CLASS="Participant" NAME="ISDUPLICATE" OPERATION="isParticipantDuplicate" />
   
   <PAGE_PARAMETER NAME="concernRoleID" />
   
   <CONNECT>
      <SOURCE NAME="PAGE" PROPERTY="concernRoleID" />
      <TARGET NAME="DISPLAY" PROPERTY="participantCommKey$concernRoleID" />
   </CONNECT>
   <CONNECT>
      <SOURCE NAME="PAGE" PROPERTY="concernRoleID" />
      <TARGET NAME="ISDUPLICATE" PROPERTY="concernRoleID" />
   </CONNECT>
   
   <ACTION_SET BOTTOM="false">
      <ACTION_CONTROL IMAGE="RecordCommunication" LABEL="ActionControl.Label.RecordCommunication">
         <CONDITION>
            <IS_FALSE NAME="ISDUPLICATE" PROPERTY="statusInd" />
         </CONDITION>
         <LINK OPEN_MODAL="true" PAGE_ID="Participant_getRecordCommCorrespondent">
            <CONNECT>
               <SOURCE NAME="PAGE" PROPERTY="concernRoleID" />
               <TARGET NAME="PAGE" PROPERTY="concernRoleID" />
            </CONNECT>
            <CONNECT>
               <SOURCE NAME="DISPLAY" PROPERTY="description" />
               <TARGET NAME="PAGE" PROPERTY="pageDescription" />
            </CONNECT>
         </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL IMAGE="CreateCorrespondence" LABEL="ActionControl.Label.CorrespondenceCommunication">
         <CONDITION>
            <IS_FALSE NAME="ISDUPLICATE" PROPERTY="statusInd" />
         </CONDITION>
         <LINK OPEN_MODAL="true" PAGE_ID="BDMParticipant_createCorrespondence">
            <CONNECT>
               <SOURCE NAME="PAGE" PROPERTY="concernRoleID" />
               <TARGET NAME="PAGE" PROPERTY="concernRoleID" />
            </CONNECT>
         </LINK>
      </ACTION_CONTROL>
   </ACTION_SET>
   
   <CLUSTER SHOW_LABELS="false" STYLE="tab-renderer">
      <CONDITION>
         <IS_TRUE NAME="DISPLAY" PROPERTY="statusInd" />
      </CONDITION>
      <FIELD>
         <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="data" />
         </CONNECT>
      </FIELD>
   </CLUSTER>
   
   <LIST>
      <INCLUDE FILE_NAME="BDMParticipant_viewListRowMenuActionsView.vim" />

      <DETAILS_ROW>
         <INLINE_PAGE PAGE_ID="BDMParticipant_resolveViewCommunication1">
            <CONNECT>
               <SOURCE NAME="DISPLAY" PROPERTY="communicationID" />
               <TARGET NAME="PAGE" PROPERTY="communicationID" />
            </CONNECT>
            <CONNECT>
               <SOURCE NAME="DISPLAY" PROPERTY="description" />
               <TARGET NAME="PAGE" PROPERTY="pageDescription" />
            </CONNECT>
            <CONNECT>
               <SOURCE NAME="DISPLAY" PROPERTY="correspondentConcernRoleID" />
               <TARGET NAME="PAGE" PROPERTY="correspondentConcernRoleID" />
            </CONNECT>
            <CONNECT>
               <SOURCE NAME="DISPLAY" PROPERTY="communicationStatus" />
               <TARGET NAME="PAGE" PROPERTY="status" />
            </CONNECT>
            <CONNECT>
               <SOURCE NAME="DISPLAY" PROPERTY="communicationFormat" />
               <TARGET NAME="PAGE" PROPERTY="communicationFormat" />
            </CONNECT>
         </INLINE_PAGE>
      </DETAILS_ROW>
      
      <FIELD LABEL="Field.Label.Subject" WIDTH="18">
         <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="subjectText" />
         </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.Type" WIDTH="16">
         <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="communicationFormat" />
         </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.Name" WIDTH="18">
         <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="correspondentName" />
         </CONNECT>
         <LINK PAGE_ID="Participant_resolveRoleHome">
            <CONNECT>
               <SOURCE NAME="DISPLAY" PROPERTY="correspondentConcernRoleID" />
               <TARGET NAME="PAGE" PROPERTY="concernRoleID" />
            </CONNECT>
            <CONNECT>
               <SOURCE NAME="DISPLAY" PROPERTY="correspondentConcernRoleType" />
               <TARGET NAME="PAGE" PROPERTY="participantType" />
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
      <FIELD LABEL="Field.Label.CommStatus" WIDTH="12">
         <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="communicationStatus" />
         </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.Date" WIDTH="12">
         <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="communicationDate" />
         </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.Status" WIDTH="12">
         <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="statusCode" />
         </CONNECT>
      </FIELD>
   </LIST>
</VIEW>