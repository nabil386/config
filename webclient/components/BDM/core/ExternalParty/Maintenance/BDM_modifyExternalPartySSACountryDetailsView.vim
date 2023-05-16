<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012,2019. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->

<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to modify external party details.   -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">

	<CLUSTER LABEL_WIDTH="45" NUM_COLS="2"
		TITLE="Cluster.Title.SSACountryDetails">

		<CONDITION TYPE="DYNAMIC">
			<SCRIPT EXPRESSION="isExtPartyTypeSSACountry" />
		</CONDITION>

		<FIELD LABEL="Field.Label.CenterOfSpecilizn" USE_BLANK="true" USE_DEFAULT="false">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="result$centerOfSpeclizn" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="details$centerOfSpeclizn" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.forceEntryDate" >
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="result$forceEntryDate" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="details$forceEntryDate" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.QCEntryStartDate">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="result$qcEntryStartDate" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="details$qcEntryStartDate" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.TotalizationCountry" USE_BLANK="true" USE_DEFAULT="false">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="result$totalizatnCountry" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="details$totalizatnCountry" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.IsProtectiveDate" USE_BLANK="true" USE_DEFAULT="false">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="result$isProtectiveDate" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="details$isProtectiveDate" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.EndDate">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="result$ssaEndDate" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="details$ssaEndDate" />
			</CONNECT>
		</FIELD>

		<FIELD CONTROL="SKIP" />

		<FIELD LABEL="Field.Label.RevisedAgrmntDate">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="result$revisedAgrmntDate" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="details$revisedAgrmntDate" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.QCEntryEndDate">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="result$qcEntryEndDate" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="details$qcEntryEndDate" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.StatusOfAgreement" USE_BLANK="true" USE_DEFAULT="false">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="result$statusOfAgreement" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="details$statusOfAgreement" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.SSALink">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="result$ssaLink" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="details$ssaLink" />
			</CONNECT>
		</FIELD>

		<FIELD CONTROL="SKIP" />

	</CLUSTER>

</VIEW>
