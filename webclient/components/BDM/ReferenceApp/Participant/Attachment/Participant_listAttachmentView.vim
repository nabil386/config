<?xml version="1.0" encoding="UTF-8"?>
<!-- Description -->
<!-- =========== -->
<!-- This page lists attachments off a participant record -->
<VIEW>
	<PAGE_TITLE>
		<CONNECT>
			<SOURCE NAME="TEXT" PROPERTY="PageTitle.StaticText1" />
		</CONNECT>
	</PAGE_TITLE>

	<!-- TASK: 12776 Custom implementation for list attachment -->
	<SERVER_INTERFACE CLASS="BDMConcernRoleAttachmentLink"
		NAME="DISPLAY" OPERATION="listConcernRoleAttachments" />


	<ACTION_SET BOTTOM="false">


		<ACTION_CONTROL IMAGE="NewButton"
			LABEL="ActionControl.Label.New">
			<LINK OPEN_MODAL="true" PAGE_ID="Participant_createAttachment">
				<CONNECT>
					<SOURCE NAME="PAGE" PROPERTY="concernRoleID" />
					<TARGET NAME="PAGE" PROPERTY="concernRoleID" />
				</CONNECT>
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="personName" />
					<TARGET NAME="PAGE" PROPERTY="pageDescription" />
				</CONNECT>
			</LINK>
		</ACTION_CONTROL>


	</ACTION_SET>


	<PAGE_PARAMETER NAME="concernRoleID" />


	<CONNECT>
		<SOURCE NAME="PAGE" PROPERTY="concernRoleID" />
		<TARGET NAME="DISPLAY"
			PROPERTY="listAttachmentsKey$concernRoleID" />
	</CONNECT>


	<LIST>


		<ACTION_SET TYPE="LIST_ROW_MENU">


			<ACTION_CONTROL LABEL="ActionControl.Label.Edit">
				<LINK OPEN_MODAL="true"
					PAGE_ID="Participant_modifyAttachmentFromList">
					<CONNECT>
						<SOURCE NAME="DISPLAY" PROPERTY="bdmCRDetails$attachmentID" />
						<TARGET NAME="PAGE" PROPERTY="attachmentID" />
					</CONNECT>
					<CONNECT>
						<SOURCE NAME="DISPLAY" PROPERTY="bdmCRDetails$attachmentLinkID" />
						<TARGET NAME="PAGE" PROPERTY="attachmentLinkID" />
					</CONNECT>
					<CONNECT>
						<SOURCE NAME="DISPLAY" PROPERTY="personName" />
						<TARGET NAME="PAGE" PROPERTY="pageDescription" />
					</CONNECT>
				</LINK>
			</ACTION_CONTROL>


			<ACTION_CONTROL IMAGE="DeleteButton"
				LABEL="ActionControl.Label.Delete">
				<LINK OPEN_MODAL="true" PAGE_ID="Participant_cancelAttachment">
					<CONNECT>
						<SOURCE NAME="DISPLAY" PROPERTY="bdmCRDetails$attachmentLinkID" />
						<TARGET NAME="PAGE" PROPERTY="attachmentLinkID" />
					</CONNECT>
				</LINK>
			</ACTION_CONTROL>


		</ACTION_SET>


		<DETAILS_ROW>


			<INLINE_PAGE PAGE_ID="Participant_viewAttachment">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="bdmCRDetails$attachmentID" />
					<TARGET NAME="PAGE" PROPERTY="attachmentID" />
				</CONNECT>
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="bdmCRDetails$attachmentLinkID" />
					<TARGET NAME="PAGE" PROPERTY="attachmentLinkID" />
				</CONNECT>
			</INLINE_PAGE>


		</DETAILS_ROW>


		<FIELD LABEL="Field.Label.Description" WIDTH="40">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="bdmCRDetails$description" />
			</CONNECT>
		</FIELD>
		<!-- START : 12776 Add fields to list screen JP -->

		<FIELD LABEL="Field.Label.DocumentType" WIDTH="15">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="documentType" />
			</CONNECT>
		</FIELD>
		 <FIELD LABEL="Field.Label.FileSource" WIDTH="15">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="fileSource" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.ReceiptDate" WIDTH="10">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="dateReceipt" />
			</CONNECT>
		</FIELD>
		<!-- END : 12776 Add fields to list screen JP -->


		<FIELD LABEL="Field.Label.Date" WIDTH="10">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="bdmCRDetails$dateReceived" />
			</CONNECT>
		</FIELD>


		<FIELD LABEL="Field.Label.Status" WIDTH="10">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="bdmCRDetails$statusCode" />
			</CONNECT>
		</FIELD>


	</LIST>


</VIEW>
