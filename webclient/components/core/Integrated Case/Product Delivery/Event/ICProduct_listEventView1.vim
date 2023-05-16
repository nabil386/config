<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012,2019. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Description -->
<!-- =========== -->
<!-- The included view to display a list of events for a product delivery   -->
<!-- on an integrated case.                                                 -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE NAME="TEXT" PROPERTY="PageTitle.StaticText1"/>
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE CLASS="IntegratedCase" NAME="DISPLAY" OPERATION="listProductDeliveryEvent1"/>


  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="caseID"/>
    <TARGET NAME="DISPLAY" PROPERTY="key$caseID"/>
  </CONNECT>


  <!-- BEGIN, CR00050298, MR -->
  <!-- BEGIN, HARP 64908, SP -->
  <LIST TITLE="List.Title.EventList">
    <!-- END, HARP 64908 -->
    <!-- END, CR00050298 -->


    <!-- BEGIN, CR00218063, MC -->
    <ACTION_SET TYPE="LIST_ROW_MENU">

      <!-- Review, Referral and Appeal Events -->
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <CONDITION>
          <IS_TRUE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$displayIndicators$editIndAndNoMinutesRecorded"/>
        </CONDITION>
        <LINK 
          PAGE_ID="Case_resolveEventModify"
          OPEN_MODAL="true"
        >
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$relatedID"/>
            <TARGET NAME="PAGE" PROPERTY="eventID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$typeCode"/>
            <TARGET NAME="PAGE" PROPERTY="eventType"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$recurringInd"/>
            <TARGET NAME="PAGE" PROPERTY="recurringInd"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$attendeeInd"/>
            <TARGET NAME="PAGE" PROPERTY="attendeeInd"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$acceptanceInd"/>
            <TARGET NAME="PAGE" PROPERTY="acceptableInd"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="description"/>
            <TARGET NAME="PAGE" PROPERTY="description"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>

      
          <!-- BEGIN, CR00250750, CW -->

        <ACTION_CONTROL
          LABEL="ActionControl.Label.EditMeetingMinutes"
          TYPE="ACTION"
        >
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="result$dtlsList$dtls$displayIndicators$meetingWithMinutesRecordedInd"
          />
        </CONDITION>
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="CalendarMeetingDetails_updateRichTextDetails"
          >
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="result$dtlsList$dtls$displayIndicators$meetingMinutesID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="meetingID"
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
          </LINK>
      </ACTION_CONTROL>
      
      <ACTION_CONTROL
        LABEL="ActionControl.Label.EditAgenda"
        TYPE="ACTION"
      >
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="result$dtlsList$dtls$displayIndicators$meetingWithMinutesRecordedInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="CalendarMeetingDetails_editAgenda"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$dtlsList$dtls$displayIndicators$meetingMinutesID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="meetingID"
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
        </LINK>
      </ACTION_CONTROL>
      
      
      <ACTION_CONTROL
        LABEL="ActionControl.Label.EditDecisions"
        TYPE="ACTION"
      >
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="result$dtlsList$dtls$displayIndicators$meetingWithMinutesRecordedInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="CalendarMeetingDetails_editDecisions"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
                PROPERTY="result$dtlsList$dtls$displayIndicators$meetingMinutesID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="meetingID"
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
        </LINK>
      </ACTION_CONTROL>
      <!-- Meeting Event -->


      <ACTION_CONTROL LABEL="ActionControl.Label.Invite">
        <CONDITION>
          <IS_TRUE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$displayIndicators$organizerInd"/>
        </CONDITION>
        <LINK OPEN_MODAL="true" PAGE_ID="CalendarMeeting_resolveInviteAttendee">
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$relatedID"/>
            <TARGET NAME="PAGE" PROPERTY="activityID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$subject"/>
            <TARGET NAME="PAGE" PROPERTY="description"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Cancel">
        <CONDITION>
          <IS_TRUE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$displayIndicators$organizerIndAndMeetingNoMinutes"/>
        </CONDITION>
        <LINK OPEN_MODAL="true" PAGE_ID="CalendarMeeting_cancelMeeting" SAVE_LINK="false">
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$relatedID"/>
            <TARGET NAME="PAGE" PROPERTY="activityID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$subject"/>
            <TARGET NAME="PAGE" PROPERTY="description"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$versionNo"/>
            <TARGET NAME="PAGE" PROPERTY="versionNo"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="ActionControl.Label.RecordMeetingMinutes">
        <CONDITION>
          <IS_TRUE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$displayIndicators$meetingNoMinutesRecordedInd"/>
        </CONDITION>
        <LINK OPEN_MODAL="true" PAGE_ID="MeetingMinutesWizard_resolveStart" SAVE_LINK="false" WINDOW_OPTIONS="width=900,height=600">
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$relatedID"/>
            <TARGET NAME="PAGE" PROPERTY="activityID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$displayIndicators$meetingMinutesID"/>
            <TARGET NAME="PAGE" PROPERTY="meetingID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="CONSTANT" PROPERTY="ZeroID"/>
            <TARGET NAME="PAGE" PROPERTY="teamID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="CONSTANT" PROPERTY="ListMeetingsForCasePage"/>
            <TARGET NAME="PAGE" PROPERTY="meetingOriginPage"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>

      
            <ACTION_CONTROL
              LABEL="ActionControl.Label.EditNotes"
              TYPE="ACTION"
            >
            <CONDITION>
              <IS_TRUE
                NAME="DISPLAY"
                PROPERTY="result$dtlsList$dtls$displayIndicators$meetingWithMinutesRecordedInd"
              />
            </CONDITION>
              <LINK
                OPEN_MODAL="true"
                PAGE_ID="CalendarMeetingDetails_updateRichTextNotes"
              >
                <CONNECT>
                  <SOURCE
                    NAME="DISPLAY"
                    PROPERTY="result$dtlsList$dtls$displayIndicators$meetingMinutesID"
                  />
                  <TARGET
                    NAME="PAGE"
                    PROPERTY="meetingID"
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
              </LINK>
            </ACTION_CONTROL>
      
      
            <ACTION_CONTROL
              LABEL="ActionControl.Label.NewAction"
              TYPE="ACTION"
            >
            <CONDITION>
              <IS_TRUE
                NAME="DISPLAY"
                PROPERTY="result$dtlsList$dtls$displayIndicators$meetingWithMinutesRecordedInd"
              />
            </CONDITION>
              <LINK
                OPEN_MODAL="true"
                PAGE_ID="CalendarMeetingMinutes_createAction"
              >
                <CONNECT>
                  <SOURCE
                    NAME="DISPLAY"
                    PROPERTY="result$dtlsList$dtls$displayIndicators$meetingMinutesID"
                  />
                  <TARGET
                    NAME="PAGE"
                    PROPERTY="meetingID"
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
              </LINK>
            </ACTION_CONTROL>
      
      
            <ACTION_CONTROL
              LABEL="ActionControl.Label.AddAttachment"
              TYPE="ACTION"
            >
            <CONDITION>
              <IS_TRUE
                NAME="DISPLAY"
                PROPERTY="result$dtlsList$dtls$displayIndicators$meetingWithMinutesRecordedInd"
              />
            </CONDITION>
              <LINK
                OPEN_MODAL="true"
                PAGE_ID="CalendarMeetingMinutes_addAttachmentsFromView"
              >
                <CONNECT>
                  <SOURCE
                    NAME="DISPLAY"
                    PROPERTY="result$dtlsList$dtls$displayIndicators$meetingMinutesID"
                  />
                  <TARGET
                    NAME="PAGE"
                    PROPERTY="meetingID"
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
              </LINK>
            </ACTION_CONTROL>
      
      
            <ACTION_CONTROL
              LABEL="ActionControl.Label.GeneratePDF"
              TYPE="FILE_DOWNLOAD"
            >
            <CONDITION>
              <IS_TRUE
                NAME="DISPLAY"
                PROPERTY="result$dtlsList$dtls$displayIndicators$meetingWithMinutesRecordedInd"
              />
            </CONDITION>
              <LINK>
                <CONNECT>
                  <SOURCE
                    NAME="DISPLAY"
                    PROPERTY="result$dtlsList$dtls$displayIndicators$meetingMinutesID"
                  />
                  <TARGET
                    NAME="PAGE"
                    PROPERTY="meetingID"
                  />
                </CONNECT>
              </LINK>
            </ACTION_CONTROL>
      
      
            <ACTION_CONTROL
              LABEL="ActionControl.Label.IssueMinutes"
              TYPE="ACTION"
            >
            <CONDITION>
              <IS_TRUE
                NAME="DISPLAY"
                PROPERTY="result$dtlsList$dtls$displayIndicators$meetingWithMinutesRecordedInd"
              />
            </CONDITION>
      
              <LINK
                OPEN_MODAL="true"
                PAGE_ID="CalendarMeetingMinutes_issueMinutes"
              >
                <CONNECT>
                  <SOURCE
                    NAME="DISPLAY"
                    PROPERTY="result$dtlsList$dtls$displayIndicators$meetingMinutesID"
                  />
                  <TARGET
                    NAME="PAGE"
                    PROPERTY="meetingID"
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
                    NAME="DISPLAY"
                    PROPERTY="result$dtlsList$dtls$displayIndicators$meetingMinutesVersionNo"
                  />
                  <TARGET
                    NAME="PAGE"
                    PROPERTY="versionNo"
                  />
                </CONNECT>
              </LINK>
            </ACTION_CONTROL>
      
      
            <ACTION_CONTROL
              LABEL="ActionControl.Label.Delete"
              TYPE="ACTION"
            >
            <CONDITION>
              <IS_TRUE
                NAME="DISPLAY"
                PROPERTY="result$dtlsList$dtls$displayIndicators$meetingWithMinutesRecordedInd"
              />
            </CONDITION>
              <LINK
                OPEN_MODAL="true"
                PAGE_ID="CalendarMeetingDetails_remove"
              >
                <CONNECT>
                  <SOURCE
                    NAME="DISPLAY"
                    PROPERTY="result$dtlsList$dtls$displayIndicators$meetingMinutesID"
                  />
                  <TARGET
                    NAME="PAGE"
                    PROPERTY="meetingID"
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
                    NAME="DISPLAY"
                    PROPERTY="result$dtlsList$dtls$displayIndicators$meetingMinutesVersionNo"
                  />
                  <TARGET
                    NAME="PAGE"
                    PROPERTY="versionNo"
                  />
                </CONNECT>
              </LINK>
            </ACTION_CONTROL>
            <!-- END, CR00250750 -->   

      <!-- Activity Event -->
      <ACTION_CONTROL IMAGE="InviteAttendeesButton" LABEL="ActionControl.Label.Invite">
        <CONDITION>
          <IS_TRUE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$displayIndicators$activityInd"/>
        </CONDITION>
        <LINK OPEN_MODAL="true" PAGE_ID="Activity_inviteAttendeeToStandardActivityFromView" SAVE_LINK="false">
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$relatedID"/>
            <TARGET NAME="PAGE" PROPERTY="activityID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="description"/>
            <TARGET NAME="PAGE" PROPERTY="description"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL IMAGE="CancelButton" LABEL="ActionControl.Label.Cancel">
        <CONDITION>
          <IS_TRUE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$displayIndicators$activityInd"/>
        </CONDITION>


        <LINK OPEN_MODAL="true" PAGE_ID="Activity_cancelStandardUserActivity" SAVE_LINK="false">
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$caseEventID"/>
            <TARGET NAME="PAGE" PROPERTY="activityID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="description"/>
            <TARGET NAME="PAGE" PROPERTY="description"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$versionNo"/>
            <TARGET NAME="PAGE" PROPERTY="versionNo"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>

      <ACTION_CONTROL IMAGE="InviteAttendeesButton" LABEL="ActionControl.Label.Invite">
        <CONDITION>
          <IS_TRUE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$recurringInd"/>
        </CONDITION>


        <LINK OPEN_MODAL="true" PAGE_ID="Activity_inviteAttendeeToRecurringActivityFromView">
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$caseEventID"/>
            <TARGET NAME="PAGE" PROPERTY="activityID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="description"/>
            <TARGET NAME="PAGE" PROPERTY="description"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL IMAGE="DeleteButton" LABEL="ActionControl.Label.Delete">
        <CONDITION>
          <IS_TRUE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$recurringInd"/>
        </CONDITION>


        <LINK OPEN_MODAL="true" PAGE_ID="Activity_cancelRecurringUserActivity">
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$caseEventID"/>
            <TARGET NAME="PAGE" PROPERTY="activityID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="description"/>
            <TARGET NAME="PAGE" PROPERTY="description"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$versionNo"/>
            <TARGET NAME="PAGE" PROPERTY="versionNo"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>
    <!-- END, CR00218063 -->


    <FIELD LABEL="Field.Label.Event" WIDTH="70">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$typeCode"/>
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.StartDate" WIDTH="15">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$startDate"/>
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EndDate" WIDTH="15">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$endDate"/>
      </CONNECT>
    </FIELD>


    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Case_resolveEventView">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="description"/>
          <TARGET NAME="PAGE" PROPERTY="description"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$recurringInd"/>
          <TARGET NAME="PAGE" PROPERTY="recurringInd"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$attendeeInd"/>
          <TARGET NAME="PAGE" PROPERTY="attendeeInd"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$acceptanceInd"/>
          <TARGET NAME="PAGE" PROPERTY="acceptableInd"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$relatedID"/>
          <TARGET NAME="PAGE" PROPERTY="eventID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$typeCode"/>
          <TARGET NAME="PAGE" PROPERTY="eventType"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="result$dtlsList$dtls$caseID"/>
          <TARGET NAME="PAGE" PROPERTY="caseID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtlsList$dtls$displayIndicators$meetingMinutesID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="meetingMinutesID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>
  </LIST>


</VIEW>
