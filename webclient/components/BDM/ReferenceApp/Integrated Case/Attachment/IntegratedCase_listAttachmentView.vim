<?xml version="1.0" encoding="UTF-8"?>
<!-- Description -->
<!-- =========== -->
<!-- This is the included view used to display a list of attachments for 
	an -->
<!-- integrated case. -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


	<PAGE_TITLE>
		<CONNECT>
			<SOURCE NAME="TEXT" PROPERTY="PageTitle.StaticText1" />
		</CONNECT>
	</PAGE_TITLE>


<!-- START TASK : 12776  Add documentType , filesource to list : Jp -->
	<SERVER_INTERFACE CLASS="BDMIntegratedCase"
		NAME="DISPLAY" OPERATION="listAttachment" />

<!-- START TASK : 12776  Add documentType , filesource to list : Jp -->

	<ACTION_SET BOTTOM="false">


		<ACTION_CONTROL IMAGE="NewButton"
			LABEL="ActionControl.Label.New">
			<LINK OPEN_MODAL="true" PAGE_ID="Case_createAttachment">
				<CONNECT>
					<SOURCE NAME="PAGE" PROPERTY="caseID" />
					<TARGET NAME="PAGE" PROPERTY="caseID" />
				</CONNECT>
				<CONNECT>
					<SOURCE NAME="DISPLAY"
						PROPERTY="contextDescription$description" />
					<TARGET NAME="PAGE" PROPERTY="pageDescription" />
				</CONNECT>
			</LINK>
		</ACTION_CONTROL>


	</ACTION_SET>


	<PAGE_PARAMETER NAME="caseID" />


	<CONNECT>
		<SOURCE NAME="PAGE" PROPERTY="caseID" />
		<TARGET NAME="DISPLAY" PROPERTY="key$caseID" />
	</CONNECT>


	<LIST>


		<DETAILS_ROW>
			<INLINE_PAGE PAGE_ID="Case_viewAttachment">
				<CONNECT>
					<SOURCE NAME="PAGE" PROPERTY="caseID" />
					<TARGET NAME="PAGE" PROPERTY="caseID" />
				</CONNECT>
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="attachmentID" />
					<TARGET NAME="PAGE" PROPERTY="attachmentID" />
				</CONNECT>
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="caseAttachmentLinkID" />
					<TARGET NAME="PAGE" PROPERTY="caseAttachmentLinkID" />
				</CONNECT>
				<CONNECT>
					<SOURCE NAME="DISPLAY"
						PROPERTY="contextDescription$description" />
					<TARGET NAME="PAGE" PROPERTY="pageDescription" />
				</CONNECT>
			</INLINE_PAGE>
		</DETAILS_ROW>


		<ACTION_SET TYPE="LIST_ROW_MENU">
			<ACTION_CONTROL LABEL="ActionControl.Label.Edit">
				<LINK OPEN_MODAL="true"
					PAGE_ID="Case_modifyAttachmentFromList1">
					<CONNECT>
						<SOURCE NAME="PAGE" PROPERTY="caseID" />
						<TARGET NAME="PAGE" PROPERTY="caseID" />
					</CONNECT>


					<CONNECT>
						<SOURCE NAME="DISPLAY" PROPERTY="attachmentID" />
						<TARGET NAME="PAGE" PROPERTY="attachmentID" />
					</CONNECT>
					<CONNECT>
						<SOURCE NAME="DISPLAY" PROPERTY="caseAttachmentLinkID" />
						<TARGET NAME="PAGE" PROPERTY="caseAttachmentLinkID" />
					</CONNECT>
					<CONNECT>
						<SOURCE NAME="DISPLAY"
							PROPERTY="contextDescription$description" />
						<TARGET NAME="PAGE" PROPERTY="pageDescription" />
					</CONNECT>
				</LINK>
			</ACTION_CONTROL>


			<ACTION_CONTROL LABEL="ActionControl.Label.Delete">
				<LINK OPEN_MODAL="true" PAGE_ID="Case_cancelAttachment">
					<CONNECT>
						<SOURCE NAME="PAGE" PROPERTY="caseID" />
						<TARGET NAME="PAGE" PROPERTY="caseID" />
					</CONNECT>
					<CONNECT>
						<SOURCE NAME="DISPLAY" PROPERTY="attachmentID" />
						<TARGET NAME="PAGE" PROPERTY="attachmentID" />
					</CONNECT>
					<CONNECT>
						<SOURCE NAME="DISPLAY" PROPERTY="caseAttachmentLinkID" />
						<TARGET NAME="PAGE" PROPERTY="caseAttachmentLinkID" />
					</CONNECT>
				</LINK>
			</ACTION_CONTROL>
		</ACTION_SET>


		<FIELD LABEL="Field.Label.Description" WIDTH="50">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="dtls$description" />
			</CONNECT>
		</FIELD>

<!-- TASK: 12776 Custom implementation for list case attachemnt JP -->

		<FIELD LABEL="Field.Label.DocumentType" WIDTH="10">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="documentType" />
			</CONNECT>
		</FIELD>
		<FIELD LABEL="Field.Label.FileSource" WIDTH="10">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="fileSource" />
			</CONNECT>
		</FIELD>
		 
		<FIELD LABEL="Field.Label.ReceiptDate" WIDTH="10">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="dateReceipt" />
			</CONNECT>
		</FIELD>
		<!--END TASK: 12776 Custom implementation for list case attachemnt JP -->

		<FIELD LABEL="Field.Label.Date" WIDTH="10">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="attachmentDate" />
			</CONNECT>
		</FIELD>


		<FIELD LABEL="Field.Label.Status" WIDTH="10">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="statusCode" />
			</CONNECT>
		</FIELD>


	</LIST>


</VIEW>
