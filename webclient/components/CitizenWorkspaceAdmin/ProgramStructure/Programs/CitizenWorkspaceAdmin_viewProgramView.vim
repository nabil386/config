<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->  

<!-- ====================================================================== -->
<!-- Copyright (c) 2008 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- ====================================================================== -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">
	<SERVER_INTERFACE CLASS="CitizenWorkspaceAdmin" NAME="DISPLAY" OPERATION="viewProgram"/>
	<PAGE_PARAMETER NAME="programTypeID"/>
	<CONNECT>
		<SOURCE NAME="PAGE" PROPERTY="programTypeID"/>
		<TARGET NAME="DISPLAY" PROPERTY="key$programTypeID"/>
	</CONNECT>
	
	<CLUSTER NUM_COLS="1" LABEL_WIDTH="25">
		<FIELD LABEL="Field.Title.URL">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="url"/>
			</CONNECT>
			<LINK OPEN_NEW="true" URI_SOURCE_NAME="DISPLAY" URI_SOURCE_PROPERTY="url"/>
		</FIELD>
		<CONTAINER LABEL="Field.Title.Text.PDFForm" WIDTH="30">
			<WIDGET TYPE="FILE_DOWNLOAD">
				<WIDGET_PARAMETER NAME="LINK_TEXT">
					<CONNECT>
						<SOURCE NAME="DISPLAY" PROPERTY="pdfFormName"/>
					</CONNECT>
				</WIDGET_PARAMETER>
				<WIDGET_PARAMETER NAME="PARAMS">
					<CONNECT>
						<SOURCE NAME="DISPLAY" PROPERTY="pdfFormName"/>
						<TARGET NAME="PAGE" PROPERTY="pdfFormResourceName"/>
					</CONNECT>
					
					<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="details$locale"/>
						<TARGET NAME="PAGE" PROPERTY="locale"/>
						</CONNECT> 
						</WIDGET_PARAMETER>
			</WIDGET>
		</CONTAINER>
		<FIELD LABEL="Field.Title.Icon">
	        <CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="icon"/>
		    </CONNECT>    
        </FIELD>
		<FIELD LABEL="Field.Title.MultipleApplications">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="multipleAppIndicator"/>
			</CONNECT>
		</FIELD>

		<FIELD CONTROL="SKIP"/>
	</CLUSTER>

	<CLUSTER TITLE="Cluster.Title.Summary" SHOW_LABELS="false">
		<CONTAINER>
			<FIELD>
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="summary"/>
				</CONNECT>
			</FIELD>
			<ACTION_CONTROL IMAGE="CALocalizableTextTranslation"
				LABEL="ActionControl.Label.Translations">
				<LINK PAGE_ID="LocalizableText_viewLocalizableText" OPEN_MODAL="true">
					<CONNECT>
						<SOURCE NAME="DISPLAY" PROPERTY="summaryTextID"/>
						<TARGET NAME="PAGE" PROPERTY="localizableTextID"/>
					</CONNECT>
				</LINK>
			</ACTION_CONTROL>
		</CONTAINER>
	</CLUSTER>
	<CLUSTER TITLE="Cluster.Title.Description" SHOW_LABELS="false">
		<CONTAINER>
			<FIELD>
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="details$description"/>
				</CONNECT>
			</FIELD>
			<ACTION_CONTROL IMAGE="CALocalizableTextTranslation"
				LABEL="ActionControl.Label.Translations">
				<LINK PAGE_ID="LocalizableText_viewLocalizableText" OPEN_MODAL="true">
					<CONNECT>
						<SOURCE NAME="DISPLAY" PROPERTY="details$dtls$descriptionTextID"/>
						<TARGET NAME="PAGE" PROPERTY="localizableTextID"/>
					</CONNECT>
				</LINK>
			</ACTION_CONTROL>
		</CONTAINER>
	</CLUSTER>

	<CLUSTER TITLE="Cluster.Title.OnlineApplicationDetails" NUM_COLS="1" LABEL_WIDTH="35">
		<FIELD LABEL="Field.Title.ApplyAtLocalOffice">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="localOfficeInd"/>
			</CONNECT>
		</FIELD>
		<CONTAINER LABEL="Field.Title.LocalOfficeDescription">
			<FIELD>
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="localOfficeDescription"/>
				</CONNECT>
			</FIELD>
			<ACTION_CONTROL IMAGE="CALocalizableTextTranslation"
				LABEL="ActionControl.Label.Translations">
				<LINK PAGE_ID="LocalizableText_viewLocalizableText" OPEN_MODAL="true">
					<CONNECT>
						<SOURCE NAME="DISPLAY" PROPERTY="localOfficeTextID"/>
						<TARGET NAME="PAGE" PROPERTY="localizableTextID"/>
					</CONNECT>
				</LINK>
			</ACTION_CONTROL>
		</CONTAINER>
	</CLUSTER>

</VIEW>
