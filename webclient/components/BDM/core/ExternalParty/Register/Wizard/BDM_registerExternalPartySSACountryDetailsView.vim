<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012,2019. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->

<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to enter external party registration details.   -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

	<CLUSTER LABEL_WIDTH="45" NUM_COLS="2"
		TITLE="Cluster.Title.SSACountryDetails">

		<CONDITION TYPE="DYNAMIC">
			<SCRIPT EXPRESSION="isExtPartyTypeSSACountry" />
		</CONDITION>
	
		<FIELD LABEL="Field.Label.CenterOfSpecilizn" USE_BLANK="true" USE_DEFAULT="false">
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="bdmDetails$centerOfSpeclizn" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.forceEntryDate" USE_BLANK="true" USE_DEFAULT="false">
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="bdmDetails$forceEntryDate" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.QCEntryStartDate" USE_BLANK="true" USE_DEFAULT="false">
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="bdmDetails$qcEntryStartDate" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.TotalizationCountry" USE_BLANK="true" USE_DEFAULT="false">
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="bdmDetails$totalizatnCountry" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.IsProtectiveDate" USE_BLANK="true" USE_DEFAULT="false">
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="bdmDetails$isProtectiveDate" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.EndDate" USE_BLANK="true" USE_DEFAULT="false">
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="bdmDetails$ssaEndDate" />
			</CONNECT>
		</FIELD>

		<FIELD CONTROL="SKIP" />

		<FIELD LABEL="Field.Label.RevisedAgrmntDate" USE_BLANK="true" USE_DEFAULT="false">
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="bdmDetails$revisedAgrmntDate" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.QCEntryEndDate" USE_BLANK="true" USE_DEFAULT="false">
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="bdmDetails$qcEntryEndDate" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.StatusOfAgreement" USE_BLANK="true" USE_DEFAULT="false">
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="bdmDetails$statusOfAgreement" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.SSALink">
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="bdmDetails$ssaLink" />
			</CONNECT>
		</FIELD>

		<FIELD CONTROL="SKIP" />

	</CLUSTER>

</VIEW>
