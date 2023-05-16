<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012,2019. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->

<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to view external party details.   -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">

	<CLUSTER LABEL_WIDTH="45" NUM_COLS="2"
		TITLE="Cluster.Title.SSACountryDetails">
		<CONDITION>
			<IS_TRUE NAME="DISPLAY" PROPERTY="result$ssaCountryDetailsInd" />
		</CONDITION>
		<FIELD LABEL="Field.Label.CountryOfAgreement">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="result$countryOfAgreement" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.CenterOfSpecilizn">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="result$centerOfSpeclizn" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.forceEntryDate">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="result$forceEntryDate" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.RevisedAgrmntDate">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="result$revisedAgrmntDate" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.QCEntryStartDate">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="result$qcEntryStartDate" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.QCEntryEndDate">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="result$qcEntryEndDate" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.TotalizationCountry">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="result$totalizatnCountry" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.StatusOfAgreement">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="result$statusOfAgreement" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.IsProtectiveDate">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="result$isProtectiveDate" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.SSALink">
			<LINK URI_SOURCE_NAME="DISPLAY"
				URI_SOURCE_PROPERTY="result$ssaLink"  OPEN_NEW= "true"/>
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="result$ssaLinkTitle" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.EndDate">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="result$ssaEndDate" />
			</CONNECT>
		</FIELD>

		<FIELD CONTROL="SKIP" />

	</CLUSTER>
</VIEW>
