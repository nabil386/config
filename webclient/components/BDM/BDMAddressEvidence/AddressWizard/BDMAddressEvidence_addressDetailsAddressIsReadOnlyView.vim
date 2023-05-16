<?xml version="1.0" encoding="UTF-8"?>
<!-- Description -->
<!-- =========== -->
<!-- Address details view page -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">

	<CONNECT>
		<SOURCE NAME="DISPLAY"
			PROPERTY="result$dtls$step3DataDtls$clientSelectedCity" />
		<TARGET NAME="ACTION"
			PROPERTY="wizardDtls$step3DataDtls$clientSelectedCity" />
	</CONNECT>
	<CONNECT>
		<SOURCE NAME="DISPLAY"
			PROPERTY="result$dtls$step3DataDtls$clientSelectedPostalCode" />
		<TARGET NAME="ACTION"
			PROPERTY="wizardDtls$step3DataDtls$clientSelectedPostalCode" />
	</CONNECT>
	<CONNECT>
		<SOURCE NAME="DISPLAY"
			PROPERTY="result$dtls$step3DataDtls$clientSelectedAddressLine2" />
		<TARGET NAME="ACTION"
			PROPERTY="wizardDtls$step3DataDtls$clientSelectedAddressLine2" />
	</CONNECT>
	<CONNECT>
		<SOURCE NAME="DISPLAY"
			PROPERTY="result$dtls$step3DataDtls$clientSelectedProvince" />
		<TARGET NAME="ACTION"
			PROPERTY="wizardDtls$step3DataDtls$clientSelectedProvince" />
	</CONNECT>


	<CLUSTER LABEL_WIDTH="45" NUM_COLS="2">
		<FIELD LABEL="Cluster.Field.Label.AddressUnitNumber">
			<CONNECT>
				<SOURCE NAME="DISPLAY"
					PROPERTY="result$dtls$step3DataDtls$clientSelectedUnitNumber" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="wizardDtls$step3DataDtls$clientSelectedUnitNumber" />
			</CONNECT>
		</FIELD>
		<FIELD LABEL="Cluster.Field.Label.AddressPOBox">
			<CONNECT>
				<SOURCE NAME="DISPLAY"
					PROPERTY="result$dtls$step3DataDtls$clientSelectedPOBox" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="wizardDtls$step3DataDtls$clientSelectedPOBox" />
			</CONNECT>
		</FIELD>
	</CLUSTER>

	<CLUSTER LABEL_WIDTH="45" NUM_COLS="2">
		<FIELD LABEL="Cluster.Field.Label.StreetNumber">
			<!-- START: BUG 101751 -->
			<!-- <CONNECT> <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step3DataDtls$clientSelectedAddressLine1"/> 
				</CONNECT> -->
			<!-- END: BUG 101751 -->
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="wizardDtls$step3DataDtls$clientSelectedAddressLine1" />
			</CONNECT>
		</FIELD>
		<FIELD LABEL="Cluster.Field.Label.StreetName">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="clientSelectedAddressLine2" />
			</CONNECT>
		</FIELD>
	</CLUSTER>

	<CLUSTER LABEL_WIDTH="45" NUM_COLS="2">
		<FIELD LABEL="Cluster.Field.Label.City">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="clientSelectedCity" />
			</CONNECT>
		</FIELD>
		<FIELD LABEL="Cluster.Field.Label.Province">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="clientSelectedProvince" />
			</CONNECT>
		</FIELD>
	</CLUSTER>

	<CLUSTER LABEL_WIDTH="45" NUM_COLS="2">
		<FIELD LABEL="Cluster.Field.Label.Postal">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="clientSelectedPostalCode" />
			</CONNECT>
		</FIELD>
	</CLUSTER>

</VIEW>
