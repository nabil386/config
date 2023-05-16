<?xml version="1.0" encoding="UTF-8"?>
<!-- This page displays search criteria for a Person. -->

<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


	<SERVER_INTERFACE CLASS="Person" NAME="DISPLAY"
		OPERATION="readSearchWithNicknamesIndicator" />


	<CLUSTER LABEL_WIDTH="45" NUM_COLS="2"
		TITLE="Cluster.Title.SearchCriteria">


		<!-- BEGIN, CR00341856, PB -->
		<FIELD LABEL="Cluster.Field.Label.ReferenceNumber">
			<!-- END, CR00341856 -->
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="key$dtls$personSearchKey$referenceNumber" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Cluster.Field.Label.CorrespondenceTrackingNumber">

			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="key$corrTrackingNumber" />
			</CONNECT>
		</FIELD>
	</CLUSTER>

	<CLUSTER NUM_COLS="2"
		TITLE="Cluster.Title.AdditionalSearchCriteria" TAB_ORDER="ROW">

		<FIELD LABEL="Cluster.Field.Label.FirstName">
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="key$dtls$personSearchKey$forename" />
			</CONNECT>
		</FIELD>

		<FIELD CONTROL="SKIP" />

		<FIELD LABEL="Cluster.Field.Label.LastName">
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="key$dtls$personSearchKey$surname" />
			</CONNECT>
		</FIELD>
		<FIELD LABEL="Field.Label.DoubleMetaphone">
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="key$dtls$personSearchKey$soundsLikeInd" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.DateOfBirth" USE_DEFAULT="false">
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="key$dtls$personSearchKey$dateOfBirth" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Cluster.Field.Label.Gender" USE_BLANK="true"
			USE_DEFAULT="false">
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="key$dtls$personSearchKey$gender" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Cluster.Field.Label.BirthLastName">
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="key$dtls$personSearchKey$birthSurname" />
			</CONNECT>
		</FIELD>


		<FIELD LABEL="Cluster.Field.Label.UnitNumber">
			<!-- END, CR00341856 -->
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="key$unitNumber" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Cluster.Field.Label.AddressLineOne">
			
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="key$dtls$personSearchKey$addressDtls$addressLine1" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.AddressLineTwo">
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="key$dtls$personSearchKey$addressDtls$addressLine2" />
			</CONNECT>
		</FIELD>


		<FIELD LABEL="Cluster.Field.Label.City">
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="key$dtls$personSearchKey$addressDtls$city" />
			</CONNECT>
		</FIELD>



		<FIELD LABEL="Cluster.Field.Label.Province">
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="key$stateProvince" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Cluster.Field.Label.Postal">
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="key$postalCode" />
			</CONNECT>
		</FIELD>


		<FIELD LABEL="Cluster.Field.Label.Country" USE_BLANK="TRUE"
			USE_DEFAULT="FALSE">
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="key$countryCode" />
			</CONNECT>
		</FIELD>


	</CLUSTER>


</VIEW>
