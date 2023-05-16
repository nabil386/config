<?xml version="1.0" encoding="UTF-8"?>
<!-- Licensed Materials - Property of IBM Copyright IBM Corporation 2012. 
	All Rights Reserved. US Government Users Restricted Rights - Use, duplication 
	or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. -->
<!-- Copyright 2006-2008, 2010-2011 Curam Software Ltd. -->
<!-- All rights reserved. -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam -->
<!-- Software. -->
<!-- Description -->
<!-- =========== -->
<!-- This view allows the user to modify the attachment details. -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


	<SERVER_INTERFACE CLASS="BDMVerificationApplication"
		NAME="DISPLAY" OPERATION="readVerificationAttachment" PHASE="DISPLAY" />


	<SERVER_INTERFACE CLASS="VerificationApplication"
		NAME="DISPLAY1" OPERATION="fetchVerificationDetails" />


	<SERVER_INTERFACE CLASS="BDMVerificationApplication"
		NAME="ACTION" OPERATION="modifyVerificationAttachment" PHASE="ACTION" />


	<PAGE_PARAMETER NAME="verificationAttachmentLinkID" />
	<PAGE_PARAMETER NAME="verificationItemProvidedID" />
	<PAGE_PARAMETER NAME="attachmentID" />
	<PAGE_PARAMETER NAME="VDIEDLinkID" />
	<PAGE_PARAMETER NAME="evidenceDescriptorID" />
	<PAGE_PARAMETER NAME="caseID" />
	<PAGE_PARAMETER NAME="contextDescription" />
	<PAGE_PARAMETER NAME="dataItemName" />
	<!-- BEGIN, CR00080534, RF -->
	<PAGE_PARAMETER NAME="concernRoleID" />
	<!-- END, CR00080534 -->


	<CONNECT>
		<SOURCE NAME="PAGE" PROPERTY="VDIEDLinkID" />
		<TARGET NAME="DISPLAY1" PROPERTY="vdIEDLinkID" />
	</CONNECT>
	<CONNECT>
		<SOURCE NAME="PAGE" PROPERTY="evidenceDescriptorID" />
		<TARGET NAME="DISPLAY1" PROPERTY="evidenceDescriptorID" />
	</CONNECT>
	<CONNECT>
		<SOURCE NAME="PAGE" PROPERTY="caseID" />
		<!-- BEGIN, CR00075582, RF -->
		<TARGET NAME="DISPLAY1"
			PROPERTY="evidenceAndDataItemNameDetails$caseID" />
		<!-- END, CR00075582 -->
	</CONNECT>
	<CONNECT>
		<SOURCE NAME="PAGE" PROPERTY="contextDescription" />
		<TARGET NAME="DISPLAY1" PROPERTY="description" />
	</CONNECT>
	<CONNECT>
		<SOURCE NAME="PAGE" PROPERTY="dataItemName" />
		<TARGET NAME="DISPLAY1"
			PROPERTY="evidenceAndDataItemNameDetails$dataItemName" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="PAGE" PROPERTY="verificationAttachmentLinkID" />
		<TARGET NAME="DISPLAY"
			PROPERTY="key$verificationAttachmentLinkID" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="PAGE" PROPERTY="attachmentID" />
		<TARGET NAME="DISPLAY" PROPERTY="key$attachmentID" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY"
			PROPERTY="result$bdmDtls$details$readLinkDtls$verificationAttachmentLinkID" />
		<TARGET NAME="ACTION"
			PROPERTY="modifyLinkDtls$verificationAttachmentLinkID" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY"
			PROPERTY="result$bdmDtls$details$readAttachmentDtls$attachmentStatus" />
		<TARGET NAME="ACTION" PROPERTY="attachmentStatus" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY"
			PROPERTY="result$bdmDtls$details$readLinkDtls$verificationItemProvidedID" />
		<TARGET NAME="ACTION"
			PROPERTY="details$bdmDtls$details$modifyLinkDtls$verificationItemProvidedID" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="statusCode" />
		<TARGET NAME="ACTION" PROPERTY="statusCode" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="recordStatus" />
		<TARGET NAME="ACTION" PROPERTY="recordStatus" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY"
			PROPERTY="result$bdmDtls$details$readAttachmentDtls$versionNo" />
		<TARGET NAME="ACTION" PROPERTY="modifyAttachmentDtls$versionNo" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY"
			PROPERTY="result$bdmDtls$details$readAttachmentDtls$attachmentID" />
		<TARGET NAME="ACTION"
			PROPERTY="modifyAttachmentDtls$attachmentID" />
	</CONNECT>


	<CLUSTER LABEL_WIDTH="42.5" TITLE="Cluster.Title.Details">
		<CLUSTER LABEL_WIDTH="45" NUM_COLS="2"
			STYLE="cluster-cpr-no-border">
			<FIELD LABEL="Field.Label.Description">
				<CONNECT>
					<SOURCE NAME="DISPLAY"
						PROPERTY="result$bdmDtls$details$readLinkDtls$description" />
				</CONNECT>
				<CONNECT>
					<TARGET NAME="ACTION"
						PROPERTY="details$bdmDtls$details$modifyLinkDtls$description" />
				</CONNECT>
			</FIELD>


			<FIELD LABEL="Field.Label.DateReceived">
				<CONNECT>
					<SOURCE NAME="DISPLAY"
						PROPERTY="result$bdmDtls$details$readAttachmentDtls$receiptDate" />
				</CONNECT>
				<CONNECT>
					<TARGET NAME="ACTION"
						PROPERTY="details$bdmDtls$details$modifyAttachmentDtls$receiptDate" />
				</CONNECT>
			</FIELD>
		</CLUSTER>
		
		 <CLUSTER LABEL_WIDTH="45" NUM_COLS="2" STYLE="cluster-cpr-no-border">
		 <FIELD LABEL="Field.Label.AttachedFile">
			<LINK URI_SOURCE_NAME="DISPLAY"
				URI_SOURCE_PROPERTY="result$extrenalFileLink$fileLocationURL"  OPEN_NEW= "true"/>
			
				<CONNECT>
					<SOURCE NAME="DISPLAY"
						PROPERTY="result$extrenalFileLink$fileName" />
				</CONNECT>
				
			</FIELD>
			
		</CLUSTER>
		
		<!-- Remove file upload and download widget -->
		<!-- This code is commented as to browse button will be enabled to upload document from Document repository in R2 -->
		<!-- <CLUSTER LABEL_WIDTH="45" NUM_COLS="2" STYLE="cluster-cpr-no-border" 
			> <CONTAINER LABEL="Field.Label.AttachedFile"> <WIDGET TYPE="FILE_DOWNLOAD"> 
			<WIDGET_PARAMETER NAME="LINK_TEXT"> <CONNECT> <SOURCE NAME="DISPLAY" PROPERTY="result$bdmDtls$details$readAttachmentDtls$attachmentName" 
			/> </CONNECT> </WIDGET_PARAMETER> <WIDGET_PARAMETER NAME="PARAMS"> <CONNECT> 
			<SOURCE NAME="DISPLAY" PROPERTY="details$dtlsReadVerificationAttachmentLinkKey$key$attachmentID" 
			/> <TARGET NAME="PAGE" PROPERTY="attachmentID" /> </CONNECT> </WIDGET_PARAMETER> 
			<WIDGET_PARAMETER NAME="PARAMS"> <CONNECT> <SOURCE NAME="DISPLAY" PROPERTY="details$dtlsReadVerificationAttachmentLinkKey$key$verificationAttachmentLinkID" 
			/> <TARGET NAME="PAGE" PROPERTY="verificationAttachmentLinkID" /> </CONNECT> 
			</WIDGET_PARAMETER> </WIDGET> </CONTAINER> -->
		<!-- FILE_UPLOAD WIDGET TO UPLOAD FILES -->
		<!-- <WIDGET LABEL="Field.Label.NewFile" TYPE="FILE_UPLOAD" > <WIDGET_PARAMETER 
			NAME="CONTENT"> <CONNECT> <TARGET NAME="ACTION" PROPERTY="details$bdmDtls$details$modifyAttachmentDtls$attachmentContents" 
			/> </CONNECT> </WIDGET_PARAMETER> <WIDGET_PARAMETER NAME="FILE_NAME"> <CONNECT> 
			<TARGET NAME="ACTION" PROPERTY="details$bdmDtls$details$modifyAttachmentDtls$attachmentName" 
			/> </CONNECT> </WIDGET_PARAMETER> </WIDGET> </CLUSTER> -->
		<CLUSTER LABEL_WIDTH="45" NUM_COLS="2"
			STYLE="cluster-cpr-no-border">
			<FIELD LABEL="Field.Label.FileLocation">
				<CONNECT>
					<SOURCE NAME="DISPLAY"
						PROPERTY="result$bdmDtls$details$readAttachmentDtls$fileLocation" />
				</CONNECT>
				<CONNECT>
					<TARGET NAME="ACTION"
						PROPERTY="details$bdmDtls$details$modifyAttachmentDtls$fileLocation" />
				</CONNECT>
			</FIELD>
			<FIELD LABEL="Field.Label.FileReference">
				<CONNECT>
					<SOURCE NAME="DISPLAY"
						PROPERTY="result$bdmDtls$details$readAttachmentDtls$fileReference" />
				</CONNECT>
				<CONNECT>
					<TARGET NAME="ACTION"
						PROPERTY="details$bdmDtls$details$modifyAttachmentDtls$fileReference" />
				</CONNECT>
			</FIELD>
		</CLUSTER>
		<!-- BEGIN, CR00292013, DJ -->
		<CLUSTER LABEL_WIDTH="45" NUM_COLS="2"
			STYLE="cluster-cpr-no-border">
			<!-- END, CR00292013 -->
			<FIELD LABEL="Field.Label.DocumentType">
				<CONNECT>
					<SOURCE NAME="DISPLAY"
						PROPERTY="result$bdmDtls$details$readAttachmentDtls$documentType" />
				</CONNECT>
				<CONNECT>
					<TARGET NAME="ACTION"
						PROPERTY="details$bdmDtls$details$modifyAttachmentDtls$documentType" />
				</CONNECT>
			</FIELD>
		</CLUSTER>
	</CLUSTER>
	<CLUSTER LABEL_WIDTH="45" NUM_COLS="2"
		STYLE="cluster-cpr-no-border">
		<FIELD LABEL="Field.Label.Acceptance.Status" WIDTH="20"
			USE_BLANK="true" CONTROL_REF="statusRef">
			<CONNECT>
				<SOURCE NAME="DISPLAY"
					PROPERTY="result$bdmVerificationAttachmentLinkDetails$acceptanceStatus" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="details$bdmVerificationAttachmentLinkDetails$acceptanceStatus" />
			</CONNECT>
		</FIELD>
		<CLUSTER NUM_COLS="2" STYLE="cluster-cpr-no-border">


			<FIELD CONTROL="SKIP" />
			<FIELD LABEL="Field.Label.Rejection.Reason" USE_BLANK="true"
				CONTROL_REF="rejectionReasonRef">
				<CONNECT>
					<SOURCE NAME="DISPLAY"
						PROPERTY="result$bdmVerificationAttachmentLinkDetails$rejectionReason" />
				</CONNECT>
				<CONNECT>
					<TARGET NAME="ACTION"
						PROPERTY="details$bdmVerificationAttachmentLinkDetails$rejectionReason" />
				</CONNECT>
			</FIELD>

		</CLUSTER>
	</CLUSTER>


	<CLUSTER  STYLE="cluster-cpr-no-border">

		<FIELD CONTROL="SKIP" />
		<FIELD HEIGHT="2" LABEL="Field.Label.Other.Comment">
			<CONNECT>
				<SOURCE NAME="DISPLAY"
					PROPERTY="result$bdmVerificationAttachmentLinkDetails$otherComments" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="details$bdmVerificationAttachmentLinkDetails$otherComments" />
			</CONNECT>
		</FIELD>

	</CLUSTER>

</VIEW>
