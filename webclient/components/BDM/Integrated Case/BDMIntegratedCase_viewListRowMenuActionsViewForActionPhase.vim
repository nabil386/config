<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for a list of communications for an investigation    -->
<!-- delivery on an integrated case.                                        -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


       <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
         <CONDITION>
            <IS_TRUE NAME="ACTION" PROPERTY="editActionInd" />
         </CONDITION>         
         <LINK OPEN_MODAL="true" PAGE_ID="BDMParticipant_resolveEditCommunicationExternal">
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="workItemID" />
               <TARGET NAME="PAGE" PROPERTY="workItemID" />
            </CONNECT>
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="communicationID" />
               <TARGET NAME="PAGE" PROPERTY="communicationID" />
            </CONNECT>
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="correspondentConcernRoleID" />
               <TARGET NAME="PAGE" PROPERTY="correspondentConcernRoleID" />
            </CONNECT>
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="result$communicationDtls$commDtls$concernRoleID" />
               <TARGET NAME="PAGE" PROPERTY="concernRoleID" />
            </CONNECT>
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="communicationStatus" />
               <TARGET NAME="PAGE" PROPERTY="status" />
            </CONNECT>
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="communicationFormat" />
               <TARGET NAME="PAGE" PROPERTY="communicationFormat" />
            </CONNECT>
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="description" />
               <TARGET NAME="PAGE" PROPERTY="pageDescription" />
            </CONNECT>
         </LINK>
      </ACTION_CONTROL>
      
      <ACTION_CONTROL LABEL="ActionControl.Label.SendNow">
         <CONDITION>
            <IS_TRUE NAME="ACTION" PROPERTY="sendNowActionInd" />
         </CONDITION>
         <LINK OPEN_MODAL="true" PAGE_ID="Participant_sendEmail" SAVE_LINK="true">
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="communicationID" />
               <TARGET NAME="PAGE" PROPERTY="communicationID" />
            </CONNECT>
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="subjectText" />
               <TARGET NAME="PAGE" PROPERTY="subject" />
            </CONNECT>
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="description" />
               <TARGET NAME="PAGE" PROPERTY="pageDescription" />
            </CONNECT>
         </LINK>
      </ACTION_CONTROL>
      
      <ACTION_CONTROL LABEL="ActionControl.Label.Preview" TYPE="FILE_DOWNLOAD">
         <CONDITION>
            <IS_TRUE NAME="ACTION" PROPERTY="previewActionInd" />
         </CONDITION>
         <LINK>
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="communicationID" />
               <TARGET NAME="PAGE" PROPERTY="communicationID" />
            </CONNECT>
         </LINK>
      </ACTION_CONTROL>
      
      <ACTION_CONTROL LABEL="ActionControl.Label.View" TYPE="FILE_DOWNLOAD">
         <CONDITION>
            <IS_TRUE NAME="ACTION" PROPERTY="viewActionInd" />
         </CONDITION>
         <LINK>
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="communicationID" />
               <TARGET NAME="PAGE" PROPERTY="communicationID" />
            </CONNECT>
         </LINK>
      </ACTION_CONTROL>
      
      <ACTION_CONTROL LABEL="ActionControl.Label.Submit">
         <CONDITION>
            <IS_TRUE NAME="ACTION" PROPERTY="submitActionInd" />
         </CONDITION>
         <!--  TASK 26537 Add resolver to divert based on communication format -->
         <LINK OPEN_MODAL="true" PAGE_ID="BDMParticipant_resolveSubmitCommunication" SAVE_LINK="true">
            <!--  PAGE_ID="BDMCommunication_submitProFormaCommunication"-->
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="communicationID" />
               <TARGET NAME="PAGE" PROPERTY="communicationID" />
            </CONNECT>
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="communicationFormat" />
               <TARGET NAME="PAGE" PROPERTY="communicationFormat" />
            </CONNECT>
         </LINK>
      </ACTION_CONTROL>
      
      <!-- BUG-91269, Start
      <ACTION_CONTROL LABEL="ActionControl.Label.Void">
         <CONDITION>
            <IS_TRUE NAME="ACTION" PROPERTY="voidActionInd" />
         </CONDITION>
         <LINK OPEN_MODAL="true" PAGE_ID="BDMCommunication_voidProFormaCommunication" SAVE_LINK="true">
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="communicationID" />
               <TARGET NAME="PAGE" PROPERTY="communicationID" />
            </CONNECT>
         </LINK>
      </ACTION_CONTROL>
      BUG-91269, End-->
      
      <!-- START : TASK - 7105 - IMPLEMENT MISDIRECTED -->
       <!-- BUG-91269, Start
      <ACTION_CONTROL LABEL="ActionControl.Label.Misdirected">
         <CONDITION>
            <IS_TRUE NAME="ACTION" PROPERTY="misdirectActionInd" />
         </CONDITION>
         <LINK OPEN_MODAL="true" PAGE_ID="BDMCommunication_misdirectedCommunication" SAVE_LINK="true">
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="communicationID" />
               <TARGET NAME="PAGE" PROPERTY="communicationID" />
            </CONNECT>
         </LINK>
      </ACTION_CONTROL>
      BUG-91269, End-->
      <!-- END : TASK - 7105 - IMPLEMENT MISDIRECTED -->
       <!-- BUG-91269, Start
      <ACTION_CONTROL LABEL="ActionControl.Label.Recall">
         <CONDITION>
            <IS_TRUE NAME="ACTION" PROPERTY="recallActionInd" />
         </CONDITION>
         <LINK OPEN_MODAL="true" PAGE_ID="BDMCommunication_recallProFormaCommunication" SAVE_LINK="true">
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="communicationID" />
               <TARGET NAME="PAGE" PROPERTY="communicationID" />
            </CONNECT>
         </LINK>
      </ACTION_CONTROL>
      
      <ACTION_CONTROL LABEL="ActionControl.Label.Resubmit">
         <CONDITION>
            <IS_TRUE NAME="ACTION" PROPERTY="resubmitActionInd" />
         </CONDITION>
         <LINK OPEN_MODAL="true" PAGE_ID="BDMCommunication_resubmitProFormaCommunication" SAVE_LINK="true">
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="communicationID" />
               <TARGET NAME="PAGE" PROPERTY="communicationID" />
            </CONNECT>
         </LINK>
      </ACTION_CONTROL>
      
      <ACTION_CONTROL LABEL="ActionControl.Label.Return">
         <CONDITION>
            <IS_TRUE NAME="ACTION" PROPERTY="returnActionInd" />
         </CONDITION>
         <LINK OPEN_MODAL="true" PAGE_ID="BDMCommunication_returnProFormaCommunication" SAVE_LINK="true">
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="communicationID" />
               <TARGET NAME="PAGE" PROPERTY="communicationID" />
            </CONNECT>
         </LINK>
      </ACTION_CONTROL>
      BUG-91269, End -->
      
      <ACTION_CONTROL LABEL="ActionControl.Label.EditStatus">
         <CONDITION>
            <IS_TRUE NAME="ACTION" PROPERTY="modifyStatusActionInd" />
         </CONDITION>
         <LINK OPEN_MODAL="true" PAGE_ID="BDMCommunication_modifyStatus">
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="communicationID" />
               <TARGET NAME="PAGE" PROPERTY="communicationID" />
            </CONNECT>
         </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL IMAGE="DeleteButton" LABEL="ActionControl.Label.Delete">
         <CONDITION>
            <IS_TRUE NAME="ACTION" PROPERTY="deleteActionInd" />
         </CONDITION>
         <LINK OPEN_MODAL="true" SAVE_LINK="true" URI_SOURCE_NAME="ACTION" URI_SOURCE_PROPERTY="deletePageName">
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="communicationID" />
               <TARGET NAME="PAGE" PROPERTY="communicationID" />
            </CONNECT>
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="correspondentConcernRoleID" />
               <TARGET NAME="PAGE" PROPERTY="correspondentConcernRoleID" />
            </CONNECT>
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="versionNo" />
               <TARGET NAME="PAGE" PROPERTY="versionNo" />
            </CONNECT>
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="subjectText" />
               <TARGET NAME="PAGE" PROPERTY="subject" />
            </CONNECT>
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="description" />
               <TARGET NAME="PAGE" PROPERTY="pageDescription" />
            </CONNECT>
         </LINK>
      </ACTION_CONTROL>

  </ACTION_SET>


</VIEW>
