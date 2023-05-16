<?xml version="1.0" encoding="UTF-8"?>
<!-- Description -->
<!-- =========== -->
<!-- The included view for modifying participant attachments. -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


	<PAGE_TITLE>
		<CONNECT>
			<SOURCE NAME="TEXT" PROPERTY="PageTitle.StaticText1" />
		</CONNECT>
	</PAGE_TITLE>

<!-- TASK 12776 :  -->
	<SERVER_INTERFACE CLASS="BDMConcernRoleAttachmentLink"
		NAME="DISPLAY" OPERATION="readConcernRoleAttachment" />


	<SERVER_INTERFACE CLASS="BDMConcernRoleAttachmentLink"
		NAME="ACTION" OPERATION="modifyConcernRoleAttachment" PHASE="ACTION" />

<!-- TASK 12776 :  -->
	<PAGE_PARAMETER NAME="attachmentID" />
	<PAGE_PARAMETER NAME="attachmentLinkID" />
	<PAGE_PARAMETER NAME="pageDescription" />


	<CONNECT>
		<SOURCE NAME="PAGE" PROPERTY="attachmentLinkID" />
		<TARGET NAME="DISPLAY" PROPERTY="linkKey$attachmentLinkID" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="PAGE" PROPERTY="attachmentLinkID" />
		<TARGET NAME="DISPLAY" PROPERTY="linkDtls$attachmentLinkID" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="attachmentDtls$attachmentID" />
		<!-- END, CR00088228 -->
		<TARGET NAME="ACTION" PROPERTY="linkDtls$attachmentID" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="attachmentDtls$attachmentID" />
		<!-- END, CR00088228 -->
		<TARGET NAME="ACTION" PROPERTY="attachmentDtls$attachmentID" />
	</CONNECT>


	<!-- BEGIN, CR00088228, ANK -->
	<CONNECT>
		<SOURCE NAME="PAGE" PROPERTY="attachmentLinkID" />
		<!-- END, CR00088228 -->
		<TARGET NAME="ACTION" PROPERTY="attachmentLinkID" />
	</CONNECT>
	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="concernRoleID" />
		<TARGET NAME="ACTION" PROPERTY="concernRoleID" />
	</CONNECT>
	<CONNECT>
		<SOURCE NAME="DISPLAY"
			PROPERTY="attachmentDtls$versionNo" />
		<TARGET NAME="ACTION" PROPERTY="attachmentDtls$versionNo" />
	</CONNECT>
	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="linkDtls$versionNo" />
		<TARGET NAME="ACTION" PROPERTY="linkDtls$versionNo" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="dateReceived" />
		<TARGET NAME="ACTION" PROPERTY="dateReceived" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="attachmentStatus" />
		<TARGET NAME="ACTION" PROPERTY="attachmentStatus" />
	</CONNECT>
	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="concernRoleAltID" />
		<TARGET NAME="ACTION" PROPERTY="concernRoleAltID" />
	</CONNECT>
	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="attachmentDtls$statusCode" />
		<TARGET NAME="ACTION" PROPERTY="attachmentDtls$statusCode" />
	</CONNECT>
	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="linkDtls$statusCode" />
		<TARGET NAME="ACTION" PROPERTY="linkDtls$statusCode" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="concernRoleName" />
		<TARGET NAME="ACTION" PROPERTY="concernRoleName" />
	</CONNECT>


	<CLUSTER LABEL_WIDTH="42" NUM_COLS="2">
		<CONDITION>
			<IS_FALSE NAME="DISPLAY" PROPERTY="isCancelled" />
		</CONDITION>
 
 <FIELD LABEL="Field.Label.AttachedFile" >
    <LINK URI_SOURCE_NAME="DISPLAY"
				URI_SOURCE_PROPERTY="fileLocationURL"  OPEN_NEW= "true"/>
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="fileName" />
      </CONNECT>
    
      
    </FIELD>
 
 <!-- END TASK 93726 : remove file widget  -->
 
 
		<FIELD LABEL="Field.Label.FileLocation">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="fileLocation" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="fileLocation" />
			</CONNECT>
		</FIELD>
		<FIELD LABEL="Field.Label.DocumentType">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="documentType" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="documentType" />
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
		
		<FIELD CONTROL="SKIP" />
		<FIELD LABEL="Field.Label.FileReference">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="fileReference" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="fileReference" />
			</CONNECT>
		</FIELD>
		
		<!--START : TASK 12776 Add file Source : JP  -->		
		<FIELD LABEL="Field.Label.FileSource">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="fileSource" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="fileSource" />
			</CONNECT>
		</FIELD>
		
	</CLUSTER>

	<CLUSTER LABEL_WIDTH="42" NUM_COLS="2">
		<CONDITION>
			<IS_TRUE NAME="DISPLAY" PROPERTY="isCancelled" />
		</CONDITION>

		<FIELD LABEL="Field.Label.AttachedFile">
			<CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="fileName" />
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
		<FIELD CONTROL="SKIP" />
		<FIELD LABEL="Field.Label.DocumentType">
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
<!--START : TASK 12776 Add file Source : JP  -->		
		<FIELD LABEL="Field.Label.FileSource">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="fileSource" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="fileSource" />
			</CONNECT>
		</FIELD>
		<!-- <FIELD CONTROL="SKIP" />-->
		
		<!--END : TASK 12776 Add file Source : JP  -->
	</CLUSTER>


	<CLUSTER LABEL_WIDTH="20.5">
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
