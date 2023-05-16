<?xml version="1.0" encoding="UTF-8"?>
<!-- The included view for modifying case attachments. -->

<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


	<PAGE_TITLE>
		<CONNECT>
			<SOURCE NAME="TEXT" PROPERTY="PageTitle.StaticText1" />
		</CONNECT>
	</PAGE_TITLE>

<!-- START : TASL 12776 : Add custom source Field   JP-->
	<SERVER_INTERFACE CLASS="BDMCase" NAME="DISPLAY"
		OPERATION="readCaseAttachmentDetailsForModiy" />


	<SERVER_INTERFACE CLASS="BDMCase" NAME="ACTION"
		OPERATION="modifyCaseAttachment" PHASE="ACTION" />
<!-- END : TASL 12776 : Add custom source Field  JP-->

	<PAGE_PARAMETER NAME="caseID" />
	<PAGE_PARAMETER NAME="attachmentID" />
	<PAGE_PARAMETER NAME="caseAttachmentLinkID" />
	<PAGE_PARAMETER NAME="pageDescription" />


	<CONNECT>
		<SOURCE NAME="PAGE" PROPERTY="caseID" />
		<TARGET NAME="DISPLAY" PROPERTY="listMemberKey$caseID" />
	</CONNECT>
	<CONNECT>
		<SOURCE NAME="PAGE" PROPERTY="attachmentID" />
		<TARGET NAME="DISPLAY" PROPERTY="attachmentID" />
	</CONNECT>
	<CONNECT>
		<SOURCE NAME="PAGE" PROPERTY="caseAttachmentLinkID" />
		<TARGET NAME="DISPLAY" PROPERTY="caseAttachmentLinkID" />
	</CONNECT>
	<CONNECT>
		<SOURCE NAME="PAGE" PROPERTY="attachmentID" />
		<TARGET NAME="ACTION" PROPERTY="attachmentID" />
	</CONNECT>
	<CONNECT>
		<SOURCE NAME="PAGE" PROPERTY="caseAttachmentLinkID" />
		<TARGET NAME="ACTION" PROPERTY="caseAttachmentLinkID" />
	</CONNECT>
	<CONNECT>
		<SOURCE NAME="PAGE" PROPERTY="caseID" />
		<TARGET NAME="ACTION" PROPERTY="caseID" />
	</CONNECT>
	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="versionNo" />
		<TARGET NAME="ACTION" PROPERTY="versionNo" />
	</CONNECT>
	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="linkVersionNo" />
		<TARGET NAME="ACTION" PROPERTY="linkVersionNo" />
	</CONNECT>
	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="attachmentStatus" />
		<TARGET NAME="ACTION" PROPERTY="attachmentStatus" />
	</CONNECT>


	<!--BEGIN CR00109753, SK -->
	<CLUSTER LABEL_WIDTH="40" NUM_COLS="2">
		<!--END CR00109753, SK -->

		<CONDITION>
			<IS_FALSE NAME="DISPLAY" PROPERTY="cancelledInd" />
		</CONDITION>

		<!-- BEGIN, CR00127669, MC -->
		<CONTAINER LABEL="Field.Label.AttachedFile">
			<WIDGET TYPE="FILE_DOWNLOAD">
				<WIDGET_PARAMETER NAME="LINK_TEXT">
					<CONNECT>
						<SOURCE NAME="DISPLAY" PROPERTY="attachmentName" />
					</CONNECT>
				</WIDGET_PARAMETER>
				<WIDGET_PARAMETER NAME="PARAMS">
					<CONNECT>
						<SOURCE NAME="DISPLAY" PROPERTY="attachmentID" />
						<TARGET NAME="PAGE" PROPERTY="attachmentID" />
					</CONNECT>
				</WIDGET_PARAMETER>
			</WIDGET>
		</CONTAINER>
		<!-- END, CR00127669 -->


		<FIELD LABEL="Field.Label.Participant" USE_BLANK="true">
			<CONNECT>
				<INITIAL HIDDEN_PROPERTY="dtls$caseParticipantRoleID"
					NAME="DISPLAY" PROPERTY="nameAndAgeOpt" />
			</CONNECT>
			<CONNECT>
				<SOURCE NAME="DISPLAY"
					PROPERTY="readCaseAttachmentOut$caseParticipantRoleID" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="caseParticipantRoleID" />
			</CONNECT>
		</FIELD>


		<FIELD LABEL="Field.Label.FileLocation">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="fileLocation" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="fileLocation" />
			</CONNECT>
		</FIELD>


		<FIELD LABEL="Field.Label.ReceiptDate">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="receiptDate" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="receiptDate" />
			</CONNECT>
		</FIELD>


		<WIDGET LABEL="Field.Label.NewFile" TYPE="FILE_UPLOAD">
			<WIDGET_PARAMETER NAME="CONTENT">
				<CONNECT>
					<TARGET NAME="ACTION" PROPERTY="attachmentContents" />
				</CONNECT>
			</WIDGET_PARAMETER>


			<WIDGET_PARAMETER NAME="FILE_NAME">
				<CONNECT>
					<TARGET NAME="ACTION" PROPERTY="attachmentName" />
				</CONNECT>
			</WIDGET_PARAMETER>
		</WIDGET>


		<FIELD LABEL="Field.Label.DocumentType" USE_BLANK="true">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="documentType" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="documentType" />
			</CONNECT>
		</FIELD>
		<FIELD LABEL="Field.Label.FileReference">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="fileReference" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="fileReference" />
			</CONNECT>
		</FIELD>
<!--START :  TASK : 12776 Add new field -->
		<FIELD LABEL="Field.Label.FileSource" USE_BLANK="true">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="fileSource" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="fileSource" />
			</CONNECT>
		</FIELD>
		<!-- TASK : 12776 -->
	</CLUSTER>

	<CLUSTER LABEL_WIDTH="40" NUM_COLS="2">

		<CONDITION>
			<IS_TRUE NAME="DISPLAY" PROPERTY="cancelledInd" />
		</CONDITION>

		<FIELD LABEL="Field.Label.AttachedFile">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="attachmentName" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.Participant" USE_BLANK="true">
			<CONNECT>
				<INITIAL HIDDEN_PROPERTY="dtls$caseParticipantRoleID"
					NAME="DISPLAY" PROPERTY="nameAndAgeOpt" />
			</CONNECT>
			<CONNECT>
				<SOURCE NAME="DISPLAY"
					PROPERTY="readCaseAttachmentOut$caseParticipantRoleID" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="caseParticipantRoleID" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.FileLocation">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="fileLocation" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="fileLocation" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.ReceiptDate">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="receiptDate" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="receiptDate" />
			</CONNECT>
		</FIELD>

		<WIDGET LABEL="Field.Label.NewFile" TYPE="FILE_UPLOAD">
			<WIDGET_PARAMETER NAME="CONTENT">
				<CONNECT>
					<TARGET NAME="ACTION" PROPERTY="attachmentContents" />
				</CONNECT>
			</WIDGET_PARAMETER>

			<WIDGET_PARAMETER NAME="FILE_NAME">
				<CONNECT>
					<TARGET NAME="ACTION" PROPERTY="attachmentName" />
				</CONNECT>
			</WIDGET_PARAMETER>
		</WIDGET>

		<FIELD LABEL="Field.Label.DocumentType" USE_BLANK="true">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="documentType" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="documentType" />
			</CONNECT>
		</FIELD>
		
		
		<FIELD LABEL="Field.Label.FileReference">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="fileReference" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="fileReference" />
			</CONNECT>
		</FIELD>
		<!-- TASK : 12776 -->
		<FIELD LABEL="Field.Label.FileSource" USE_BLANK="true">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="fileSource" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="fileSource" />
			</CONNECT>
		</FIELD>
		<!-- TASK : 12776 -->
	</CLUSTER>

	<CLUSTER LABEL_WIDTH="20">
		<FIELD HEIGHT="4" LABEL="Field.Label.Description">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="description" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="description" />
			</CONNECT>
		</FIELD>


	</CLUSTER>


</VIEW>
