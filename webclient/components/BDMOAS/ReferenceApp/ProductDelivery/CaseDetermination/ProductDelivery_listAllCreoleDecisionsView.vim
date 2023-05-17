<?xml version="1.0" encoding="UTF-8"?>
<!-- Licensed Materials - Property of IBM PID 5725-H26 Copyright IBM Corporation 
	2012-2017. All Rights Reserved. US Government Users Restricted Rights - Use, 
	duplication or disclosure restricted by GSA ADP Schedule Contract with IBM 
	Corp. -->
<!-- Copyright (c) 2009-2010 Curam Software Ltd. -->
<!-- All rights reserved. -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam -->
<!-- Software. -->
<!-- Description -->
<!-- =========== -->
<!-- This page is used to display a list of all decisions for the product -->
<!-- delivery -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


	<PAGE_TITLE>
		<CONNECT>
			<SOURCE NAME="TEXT" PROPERTY="PageTitle.StaticText1" />
		</CONNECT>


	</PAGE_TITLE>


	<SERVER_INTERFACE CLASS="BDMOASCaseDetermination"
		NAME="DISPLAY" OPERATION="listDecisionPeriodsForDetermination" />


	<PAGE_PARAMETER NAME="caseID" />


	<CONNECT>
		<SOURCE NAME="PAGE" PROPERTY="caseID" />
		<TARGET NAME="DISPLAY" PROPERTY="key$caseID" />
	</CONNECT>

	<ACTION_SET>
		<ACTION_CONTROL LABEL="ActionControl.Label.Reassess">
			<LINK OPEN_MODAL="true"
				PAGE_ID="ProductDelivery_reassessCaseDetermination"
				WINDOW_OPTIONS="width=800">
				<CONNECT>
					<SOURCE NAME="PAGE" PROPERTY="caseID" />
					<TARGET NAME="PAGE" PROPERTY="caseID" />
				</CONNECT>
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="caseDesc$description" />
					<TARGET NAME="PAGE" PROPERTY="pageDescription" />
				</CONNECT>
			</LINK>
		</ACTION_CONTROL>
	</ACTION_SET>


	<CLUSTER SHOW_LABELS="false" STYLE="outer-cluster-borderless">
		<CONDITION>
			<IS_FALSE NAME="DISPLAY"
				PROPERTY="dateTimeDescOpt$lastReassessmentDateTimeAvailable" />
		</CONDITION>
		<FIELD>
			<CONNECT>
				<SOURCE NAME="DISPLAY"
					PROPERTY="determinationDesc$description" />
			</CONNECT>
		</FIELD>
	</CLUSTER>

	<CLUSTER SHOW_LABELS="false" STYLE="outer-cluster-borderless">
		<CONDITION>
			<IS_TRUE NAME="DISPLAY"
				PROPERTY="dateTimeDescOpt$lastReassessmentDateTimeAvailable" />
		</CONDITION>
		<FIELD>
			<CONNECT>
				<SOURCE NAME="DISPLAY"
					PROPERTY="determinationDesc$description" />
			</CONNECT>
		</FIELD>
		<FIELD>
			<CONNECT>
				<SOURCE NAME="DISPLAY"
					PROPERTY="dateTimeDescOpt$lastReassessmentDateTime" />
			</CONNECT>
		</FIELD>
	</CLUSTER>

	<!-- BEGIN, 187319, JAF -->
	<CLUSTER SHOW_LABELS="false" STYLE="outer-cluster-borderless">
		<CONDITION>
			<IS_TRUE NAME="DISPLAY"
				PROPERTY="reassessmentStatusMessageOpt$caseReassessmentStatusMessageAvailable" />
		</CONDITION>
		<FIELD>
			<CONNECT>
				<SOURCE NAME="DISPLAY"
					PROPERTY="reassessmentStatusMessageOpt$caseReassessmentStatusMessage" />
			</CONNECT>
		</FIELD>
	</CLUSTER>
	<!-- END, 187319, JAF -->

	<!-- BEGIN, CR00463142, EC -->
	<LIST SUMMARY="Summary.Decisions.Details">
		<!-- END, CR00463142 -->


		<DETAILS_ROW>
			<INLINE_PAGE
				PAGE_ID="ProductDelivery_resolveDecisionSummary">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="result$determinationID" />
					<TARGET NAME="PAGE" PROPERTY="determinationID" />
				</CONNECT>
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="decisionFromDate" />
					<TARGET NAME="PAGE" PROPERTY="displayDate" />
				</CONNECT>
			</INLINE_PAGE>
		</DETAILS_ROW>


		<FIELD LABEL="Field.Label.CoverPeriod" WIDTH="33">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="coverPeriod" />
			</CONNECT>
			<LINK PAGE_ID="ProductDelivery_resolveDecisionDisplayTab">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="result$determinationID" />
					<TARGET NAME="PAGE" PROPERTY="determinationID" />
				</CONNECT>
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="decisionFromDate" />
					<TARGET NAME="PAGE" PROPERTY="displayDate" />
				</CONNECT>
			</LINK>


		</FIELD>


		<FIELD LABEL="Field.Label.Decision" WIDTH="33">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="resultCode" />
			</CONNECT>
		</FIELD>


		<FIELD LABEL="Field.Label.Summary" WIDTH="34">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="summary" />
			</CONNECT>
		</FIELD>


	</LIST>


</VIEW>
