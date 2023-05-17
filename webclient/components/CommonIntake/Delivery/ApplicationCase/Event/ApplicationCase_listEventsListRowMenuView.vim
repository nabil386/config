<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM

  Copyright IBM Corporation 2012,2019. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view to display the list row menu actions appropriate to  -->
<!-- each of the event types. This view applys for most of the integrated   -->
<!-- and product delivery case types.                                       -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


  <!-- Meeting Event -->
  <ACTION_SET TYPE="LIST_ROW_MENU">


    <!-- Review, Referral and Appeal Events -->
    <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
      <CONDITION>
        <IS_TRUE NAME="DISPLAY" PROPERTY="editIndAndNoMinutesRecorded"/>
      </CONDITION>
      <LINK OPEN_MODAL="true" PAGE_ID="Case_resolveEventModify">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="relatedID"/>
          <TARGET NAME="PAGE" PROPERTY="eventID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="typeCode"/>
          <TARGET NAME="PAGE" PROPERTY="eventType"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="PAGE" PROPERTY="caseID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="recurringInd"/>
          <TARGET NAME="PAGE" PROPERTY="recurringInd"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="attendeeInd"/>
          <TARGET NAME="PAGE" PROPERTY="attendeeInd"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="acceptanceInd"/>
          <TARGET NAME="PAGE" PROPERTY="acceptableInd"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="description"/>
          <TARGET NAME="PAGE" PROPERTY="description"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.EditMeetingMinutes" TYPE="ACTION">
      <CONDITION>
        <IS_TRUE NAME="DISPLAY" PROPERTY="meetingWithMinutesRecordedInd"/>
      </CONDITION>
      <LINK OPEN_MODAL="true" PAGE_ID="CalendarMeetingDetails_updateRichTextDetails">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="meetingMinutesID"/>
          <TARGET NAME="PAGE" PROPERTY="meetingID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="PAGE" PROPERTY="caseID"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.EditMeetingMinutesAgenda" TYPE="ACTION">
      <CONDITION>
        <IS_TRUE NAME="DISPLAY" PROPERTY="meetingWithMinutesRecordedInd"/>
      </CONDITION>
      <LINK OPEN_MODAL="true" PAGE_ID="CalendarMeetingDetails_editAgenda">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="meetingMinutesID"/>
          <TARGET NAME="PAGE" PROPERTY="meetingID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="PAGE" PROPERTY="caseID"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.EditMeetingMinutesDecisions" TYPE="ACTION">
      <CONDITION>
        <IS_TRUE NAME="DISPLAY" PROPERTY="meetingWithMinutesRecordedInd"/>
      </CONDITION>
      <LINK OPEN_MODAL="true" PAGE_ID="CalendarMeetingDetails_editDecisions">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="meetingMinutesID"/>
          <TARGET NAME="PAGE" PROPERTY="meetingID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="PAGE" PROPERTY="caseID"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.EditNotes" TYPE="ACTION">
      <CONDITION>
        <IS_TRUE NAME="DISPLAY" PROPERTY="meetingWithMinutesRecordedInd"/>
      </CONDITION>
      <LINK OPEN_MODAL="true" PAGE_ID="CalendarMeetingDetails_updateRichTextNotes">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="meetingMinutesID"/>
          <TARGET NAME="PAGE" PROPERTY="meetingID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="PAGE" PROPERTY="caseID"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.Invite">
      <CONDITION>
        <IS_TRUE NAME="DISPLAY" PROPERTY="organizerInd"/>
      </CONDITION>
      <LINK OPEN_MODAL="true" PAGE_ID="CalendarMeeting_resolveInviteAttendee">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="relatedID"/>
          <TARGET NAME="PAGE" PROPERTY="activityID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="PAGE" PROPERTY="caseID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="subject"/>
          <TARGET NAME="PAGE" PROPERTY="description"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.RecordMeetingMinutes">
      <CONDITION>
        <IS_TRUE NAME="DISPLAY" PROPERTY="meetingNoMinutesRecordedInd"/>
      </CONDITION>
      <LINK OPEN_MODAL="true" PAGE_ID="MeetingMinutesWizard_resolveStart" SAVE_LINK="false"
        WINDOW_OPTIONS="width=900,height=600">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="relatedID"/>
          <TARGET NAME="PAGE" PROPERTY="activityID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="PAGE" PROPERTY="caseID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="meetingMinutesID"/>
          <TARGET NAME="PAGE" PROPERTY="meetingID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="CONSTANT" PROPERTY="ZeroID"/>
          <TARGET NAME="PAGE" PROPERTY="teamID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="CONSTANT" PROPERTY="ListMeetingsForApplicationCasePage"/>
          <TARGET NAME="PAGE" PROPERTY="meetingOriginPage"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.NewAction" TYPE="ACTION">
      <CONDITION>
        <IS_TRUE NAME="DISPLAY" PROPERTY="meetingWithMinutesRecordedInd"/>
      </CONDITION>
      <LINK OPEN_MODAL="true" PAGE_ID="CalendarMeetingMinutes_createAction">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="meetingMinutesID"/>
          <TARGET NAME="PAGE" PROPERTY="meetingID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="PAGE" PROPERTY="caseID"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.AddAttachment" TYPE="ACTION">
      <CONDITION>
        <IS_TRUE NAME="DISPLAY" PROPERTY="meetingWithMinutesRecordedInd"/>
      </CONDITION>
      <LINK OPEN_MODAL="true" PAGE_ID="CalendarMeetingMinutes_addAttachmentsFromView">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="meetingMinutesID"/>
          <TARGET NAME="PAGE" PROPERTY="meetingID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="PAGE" PROPERTY="caseID"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.GeneratePDF" TYPE="FILE_DOWNLOAD">
      <CONDITION>
        <IS_TRUE NAME="DISPLAY" PROPERTY="meetingWithMinutesRecordedInd"/>
      </CONDITION>
      <LINK>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="meetingMinutesID"/>
          <TARGET NAME="PAGE" PROPERTY="meetingID"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.IssueMinutes" TYPE="ACTION">
      <CONDITION>
        <IS_TRUE NAME="DISPLAY" PROPERTY="meetingWithMinutesRecordedInd"/>
      </CONDITION>
      <LINK OPEN_MODAL="true" PAGE_ID="CalendarMeetingMinutes_issueMinutes">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="meetingMinutesID"/>
          <TARGET NAME="PAGE" PROPERTY="meetingID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="PAGE" PROPERTY="caseID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="meetingMinutesVersionNo"/>
          <TARGET NAME="PAGE" PROPERTY="versionNo"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.Cancel" TYPE="ACTION">
      <CONDITION>
        <IS_TRUE NAME="DISPLAY" PROPERTY="meetingWithMinutesRecordedInd"/>
      </CONDITION>
      <LINK OPEN_MODAL="true" PAGE_ID="CalendarMeetingDetails_remove">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="meetingMinutesID"/>
          <TARGET NAME="PAGE" PROPERTY="meetingID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="PAGE" PROPERTY="caseID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="meetingMinutesVersionNo"/>
          <TARGET NAME="PAGE" PROPERTY="versionNo"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
    <!-- END, CR00250750 -->


    <!-- BEGIN, CR00238040, ELG -->
    <ACTION_CONTROL LABEL="ActionControl.Label.Cancel">
      <CONDITION>
        <IS_TRUE NAME="DISPLAY" PROPERTY="organizerIndAndMeetingNoMinutes"/>
      </CONDITION>
      <LINK OPEN_MODAL="true" PAGE_ID="CalendarMeeting_cancelMeeting" SAVE_LINK="true">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="relatedID"/>
          <TARGET NAME="PAGE" PROPERTY="activityID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="subject"/>
          <TARGET NAME="PAGE" PROPERTY="description"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="versionNo"/>
          <TARGET NAME="PAGE" PROPERTY="versionNo"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
    <!-- END, CR00238040 -->


    <!-- Activity Event -->
    <ACTION_CONTROL IMAGE="InviteAttendeesButton" LABEL="ActionControl.Label.Invite">
      <CONDITION>
        <IS_TRUE NAME="DISPLAY" PROPERTY="activityInd"/>
      </CONDITION>
      <LINK OPEN_MODAL="true" PAGE_ID="Activity_inviteAttendeeToStandardActivityFromView">
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="PAGE" PROPERTY="caseID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="relatedID"/>
          <TARGET NAME="PAGE" PROPERTY="activityID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="description"/>
          <TARGET NAME="PAGE" PROPERTY="description"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
    <ACTION_CONTROL IMAGE="DeleteButton" LABEL="ActionControl.Label.Cancel">
      <CONDITION>
        <IS_TRUE NAME="DISPLAY" PROPERTY="activityInd"/>
      </CONDITION>
      <LINK OPEN_MODAL="true" PAGE_ID="Activity_cancelStandardUserActivity">
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="PAGE" PROPERTY="caseID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="relatedID"/>
          <TARGET NAME="PAGE" PROPERTY="activityID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="description"/>
          <TARGET NAME="PAGE" PROPERTY="description"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="versionNo"/>
          <TARGET NAME="PAGE" PROPERTY="versionNo"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <!-- Recurring Activity Event -->


    <ACTION_CONTROL IMAGE="InviteAttendeesButton" LABEL="ActionControl.Label.Invite">
      <CONDITION>
        <IS_TRUE NAME="DISPLAY" PROPERTY="recurringInd"/>
      </CONDITION>
      <LINK OPEN_MODAL="true" PAGE_ID="Activity_inviteAttendeeToRecurringActivityFromView">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="relatedID"/>
          <TARGET NAME="PAGE" PROPERTY="activityID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="description"/>
          <TARGET NAME="PAGE" PROPERTY="description"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL IMAGE="DeleteButton" LABEL="ActionControl.Label.Cancel">
      <CONDITION>
        <IS_TRUE NAME="DISPLAY" PROPERTY="recurringInd"/>
      </CONDITION>
      <LINK OPEN_MODAL="true" PAGE_ID="Activity_cancelRecurringUserActivity">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="relatedID"/>
          <TARGET NAME="PAGE" PROPERTY="activityID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="description"/>
          <TARGET NAME="PAGE" PROPERTY="description"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="versionNo"/>
          <TARGET NAME="PAGE" PROPERTY="versionNo"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <!-- Product Delivery Milestone Event -->
    <ACTION_CONTROL IMAGE="WaiverButton" LABEL="ActionControl.Label.Waiver">
      <CONDITION>
        <IS_TRUE NAME="DISPLAY" PROPERTY="waiverLinkInd"/>
      </CONDITION>
      <LINK OPEN_MODAL="true" PAGE_ID="ICInvestigationDelivery_createWaiverForMilestone">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="relatedID"/>
          <TARGET NAME="PAGE" PROPERTY="milestoneDeliveryID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="description"/>
          <TARGET NAME="PAGE" PROPERTY="description"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="PAGE" PROPERTY="caseID"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL IMAGE="EditButton" LABEL="ActionControl.Label.Edit">
      <CONDITION>
        <IS_TRUE NAME="DISPLAY" PROPERTY="editMilestoneFromViewInd"/>
      </CONDITION>
      <LINK OPEN_MODAL="true" PAGE_ID="ProductDelivery_modifyMilestoneFromView">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="relatedID"/>
          <TARGET NAME="PAGE" PROPERTY="milestoneDeliveryID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="description"/>
          <TARGET NAME="PAGE" PROPERTY="description"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="PAGE" PROPERTY="caseID"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL IMAGE="EditButton" LABEL="ActionControl.Label.Edit">
      <CONDITION>
        <IS_TRUE NAME="DISPLAY" PROPERTY="editMilestoneFromWaiverInd"/>
      </CONDITION>
      <LINK OPEN_MODAL="true" PAGE_ID="ProductDelivery_modifyMilestoneFromViewWaiver">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="relatedID"/>
          <TARGET NAME="PAGE" PROPERTY="milestoneDeliveryID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="description"/>
          <TARGET NAME="PAGE" PROPERTY="description"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="PAGE" PROPERTY="caseID"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL IMAGE="DeleteButton" LABEL="ActionControl.Label.Delete">
      <CONDITION>
        <IS_TRUE NAME="DISPLAY" PROPERTY="waiverLinkInd"/>
      </CONDITION>
      <LINK OPEN_MODAL="true" PAGE_ID="ProductDelivery_removeMilestone">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="relatedID"/>
          <TARGET NAME="PAGE" PROPERTY="milestoneDeliveryID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="description"/>
          <TARGET NAME="PAGE" PROPERTY="description"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="PAGE" PROPERTY="caseID"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <!-- BEGIN, CR00236562, SS -->
    <ACTION_CONTROL APPEND_ELLIPSIS="false" IMAGE="ViewContract"
      LABEL="ActionControl.Label.ViewContract">
      <CONDITION>
        <IS_TRUE NAME="DISPLAY" PROPERTY="isContractPrinted"/>
      </CONDITION>
      <LINK OPEN_MODAL="true" PAGE_ID="ServicePlanDelivery_viewContractHomeDetailsOnly">
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="PAGE" PROPERTY="caseID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="relatedID"/>
          <TARGET NAME="PAGE" PROPERTY="servicePlanContractID"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
    <!-- END, CR00236562 -->
  </ACTION_SET>
</VIEW>
