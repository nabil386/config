<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2011, 2013. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2011-2012 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for a list of case communications. This list page    -->
<!-- allows the user to filter the communiction by case member.             -->
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
    CLASS="BDMIntegratedCase"
    NAME="ACTION"
    OPERATION="listFilteredCommunication"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="concernRoleID"/>
  <PAGE_PARAMETER NAME="name"/>


  <SERVER_INTERFACE
    CLASS="IntegratedCase"
    NAME="DISPLAY_CASEMEMBERS"
    OPERATION="listActiveCaseMembersForCommunicationsFilter"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY_CASEMEMBERS"
      PROPERTY="caseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="key$caseID"
    />
  </CONNECT>


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


   <!-- <ACTION_CONTROL
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
    </ACTION_CONTROL> -->


    <!--BEGIN, CR00050150, MR-->
    <!--BEGIN, HARP 68190,PN-->
   <!-- <ACTION_CONTROL
      IMAGE="CreateProforma"
      LABEL="ActionControl.Label.CreateProforma"
    > -->
      <!--END HARP, 68190-->
      <!--END, CR00050150-->


     <!-- <LINK
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
    </ACTION_CONTROL> -->


    <!--BEGIN, CR00050150, MR-->
    <!--BEGIN, HARP 68190,PN-->
    <!-- <ACTION_CONTROL
      IMAGE="CreateMSWord"
      LABEL="ActionControl.Label.CreateMSWord"
    > -->
      <!--END HARP, 68190-->
      <!--END, CR00050150-->


      <!-- <LINK
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
    </ACTION_CONTROL> -->
	<ACTION_CONTROL IMAGE="CreateCorrespondence" LABEL="ActionControl.Label.CorrespondenceCommunication">
         <LINK OPEN_MODAL="true" PAGE_ID="BDMParticipant_createCorrespondenceCase">
            <CONNECT>
               <SOURCE NAME="PAGE" PROPERTY="caseID" />
               <TARGET NAME="PAGE" PROPERTY="caseID" />
            </CONNECT>
			<!--<CONNECT>
               <SOURCE NAME="PAGE" PROPERTY="concernRoleID" />
               <TARGET NAME="PAGE" PROPERTY="concernRoleID" />
            </CONNECT>-->
         </LINK>
      </ACTION_CONTROL>
	

  </ACTION_SET>


  <LIST>


    <DETAILS_ROW>


      <INLINE_PAGE PAGE_ID="BDMParticipant_resolveViewCommunication1">
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="communicationID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="communicationID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="correspondentConcernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="correspondentConcernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="communicationStatus"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="status"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="communicationFormat"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="communicationFormat"
          />
        </CONNECT>
      </INLINE_PAGE>


    </DETAILS_ROW>


    <!-- BEGIN Include custom action menu -->
    <INCLUDE FILE_NAME="BDMIntegratedCase_viewListRowMenuActionsViewForActionPhase.vim"/>
    <!-- END -->



    <FIELD
      LABEL="Field.Label.Subject"
      WIDTH="21"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
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
          NAME="ACTION"
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
          NAME="ACTION"
          PROPERTY="correspondentName"
        />
      </CONNECT>
      <LINK PAGE_ID="Participant_resolveRoleHome">
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="correspondentConcernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="ACTION"
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
            <SOURCE NAME="ACTION" PROPERTY="trackingNumber" />
         </CONNECT> 
         <LINK PAGE_ID="BDMParticipant_resolveViewCommunicationExternal" OPEN_MODAL="true" WINDOW_OPTIONS="width=1000">
           <CONDITION>
            	<IS_TRUE NAME="ACTION" PROPERTY="result$communicationDtls$enableTrackingInd" />
            </CONDITION> 
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="workItemID" />
               <TARGET NAME="PAGE" PROPERTY="workItemID" />
            </CONNECT>
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="editActionInd" />
               <TARGET NAME="PAGE" PROPERTY="editActionInd" />
            </CONNECT>
            <CONNECT>
               <SOURCE NAME="ACTION" PROPERTY="communicationID" />
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
          NAME="ACTION"
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
          NAME="ACTION"
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
          NAME="ACTION"
          PROPERTY="statusCode"
        />
      </CONNECT>
    </FIELD>
  </LIST>
</VIEW>
