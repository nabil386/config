<?xml version="1.0" encoding="UTF-8"?>
<!-- This page allows the user to check if the person has already been -->
<!-- registered. -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


	<SERVER_INTERFACE CLASS="Person"
		NAME="DISPLAYNICKNAMES" OPERATION="readSearchWithNicknamesIndicator" />


	<!-- BEGIN, CR00378951, PS -->
	<CLUSTER LABEL_WIDTH="55" NUM_COLS="2"
		TITLE="Cluster.Title.SearchCriteria">
		<!-- END, CR00378951 -->


		<FIELD LABEL="Field.Label.ReferenceNumber">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="referenceNumber" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="searchKey$dtls$searchKey$referenceNumber" />
			</CONNECT>
		</FIELD>

		<!-- STRAT : TASK 90012 : add CorrespondenceTrackingNumber -->

		<FIELD LABEL="Field.Label.CorrespondenceTrackingNumber">

			<CONNECT>
				<SOURCE NAME="DISPLAY"
					PROPERTY="correspondenceTrackingNumber" />
			</CONNECT>


			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="searchKey$correspondenceTrackingNumber" />
			</CONNECT>
		</FIELD>
		<!-- END -->


	</CLUSTER>


	<CLUSTER LABEL_WIDTH="45" NUM_COLS="2" TAB_ORDER="ROW"
		TITLE="Cluster.Title.AdditionalSearchCriteria">



		<FIELD LABEL="Field.Label.FirstName">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="forename" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="searchKey$dtls$searchKey$forename" />
			</CONNECT>
		</FIELD>

		<FIELD CONTROL="SKIP" />

		<FIELD LABEL="Field.Label.LastName">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="surname" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="searchKey$dtls$searchKey$surname" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.DoubleMetaphone">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="soundsLikeInd" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="searchKey$dtls$searchKey$soundsLikeInd" />
			</CONNECT>
		</FIELD>


		<FIELD LABEL="Field.Label.DateOfBirth" USE_DEFAULT="false">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="searchKey$dateOfBirth" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="searchKey$dtls$searchKey$dateOfBirth" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.Gender" USE_BLANK="true"
			USE_DEFAULT="false" WIDTH="50">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="gender" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="searchKey$dtls$searchKey$gender" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.BirthLastName">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="birthSurname" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="searchKey$dtls$searchKey$birthSurname" />
			</CONNECT>
		</FIELD>

		<!-- START: Task 93506: DEV: Address Format Validations -->
		<FIELD LABEL="Field.Label.UnitNumber">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="unitNumber" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="searchKey$unitNumber" />
			</CONNECT>
		</FIELD>
		<!-- END: Task 93506: DEV: Address Format Validations -->

		<FIELD LABEL="Field.Label.AddressLineOne">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="addressLine1" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="searchKey$dtls$searchKey$addressDtls$addressLine1" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.AddressLineTwo">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="addressLine1" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="searchKey$dtls$searchKey$addressDtls$addressLine2" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.City">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="city" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION"
					PROPERTY="searchKey$dtls$searchKey$addressDtls$city" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Cluster.Field.Label.Province">
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="searchKey$stateProvince" />
			</CONNECT>
		</FIELD>


		<FIELD LABEL="Cluster.Field.Label.Postal">
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="searchKey$postalCode" />
			</CONNECT>
		</FIELD>



		<FIELD LABEL="Cluster.Field.Label.Country" USE_BLANK="TRUE"
			USE_DEFAULT="FALSE">
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="searchKey$countryCode" />
			</CONNECT>
		</FIELD>


	</CLUSTER>


	<CLUSTER>
		<ACTION_SET ALIGNMENT="CENTER" BOTTOM="false">
			<!-- BEGIN, CR00350228, AC -->
			<ACTION_CONTROL ACTION_ID="SEARCH" DEFAULT="true"
				IMAGE="SearchButton" LABEL="ActionControl.Label.Search"
				TYPE="SUBMIT">
				<!-- END, CR00350228 -->
				<LINK PAGE_ID="THIS" SAVE_LINK="false" />
			</ACTION_CONTROL>


			<!-- BEGIN, CR00304371, PS -->
			<ACTION_CONTROL ACTION_ID="RESETPAGE"
				IMAGE="ResetButton" LABEL="ActionControl.Label.Reset" TYPE="SUBMIT">
				<!-- END, CR00304371 -->
				<LINK DISMISS_MODAL="false"
					PAGE_ID="BDMIORegisterPerson_duplicateCheckForPDCWizard"
					SAVE_LINK="false">
					<CONNECT>
						<SOURCE NAME="ACTION"
							PROPERTY="wizardStateID$wizardStateID" />
						<TARGET NAME="PAGE" PROPERTY="wizardStateID" />
					</CONNECT>
				</LINK>
			</ACTION_CONTROL>
		</ACTION_SET>
	</CLUSTER>


	<LIST PAGINATED="false" TITLE="List.Title.SearchResults">

		<DETAILS_ROW>


			<INLINE_PAGE URI_SOURCE_NAME="ACTION"
				URI_SOURCE_PROPERTY="result$searchResult$personSearchResult$dtlsList$personTabDetailsURL" />


		</DETAILS_ROW>
		<!-- BEGIN, CR00378951, PS -->
		<FIELD LABEL="Field.Title.Name" WIDTH="25">
			<!-- END, CR00378951 -->
			<CONNECT>
				<SOURCE NAME="ACTION"
					PROPERTY="result$searchResult$personSearchResult$dtlsList$xmlPersonData" />
			</CONNECT>


		</FIELD>

		<!-- BEGIN, CR00378951, PS -->
		<FIELD LABEL="Field.Title.DateOfBirth" WIDTH="20">
			<!-- END, CR00378951 -->
			<CONNECT>
				<SOURCE NAME="ACTION"
					PROPERTY="result$searchResult$personSearchResult$dtlsList$dateOfBirth" />
			</CONNECT>
		</FIELD>

		<!-- BEGIN, CR00378951, PS -->
		<FIELD LABEL="Field.Title.SIN" WIDTH="20">
			<CONNECT>
				<SOURCE NAME="ACTION"
					PROPERTY="result$searchResult$personSearchResult$dtlsList$sinNumber" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Title.Address">
			<CONNECT>
				<SOURCE NAME="ACTION"
					PROPERTY="result$searchResult$personSearchResult$dtlsList$formattedAddress" />
			</CONNECT>
		</FIELD>

	</LIST>


</VIEW>
