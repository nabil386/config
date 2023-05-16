<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to enter person registration details.        -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CONNECT>
	<SOURCE NAME="DISPLAY" PROPERTY="primaryAddressID" />
	<TARGET NAME="ACTION" PROPERTY="primaryAddressID" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="mailingAddressID" />
		<TARGET NAME="ACTION" PROPERTY="mailingAddressID" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="primaryPhoneNumberID" />
		<TARGET NAME="ACTION" PROPERTY="primaryPhoneNumberID" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="concernRolePhoneNumberID" />
		<TARGET NAME="ACTION" PROPERTY="concernRolePhoneNumberID" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="commExceptionID" />
		<TARGET NAME="ACTION" PROPERTY="commExceptionID" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="citizenshipID" />
		<TARGET NAME="ACTION" PROPERTY="citizenshipID" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="foreignResidencyID" />
		<TARGET NAME="ACTION" PROPERTY="foreignResidencyID" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="primaryAlternateNameID" />
		<TARGET NAME="ACTION" PROPERTY="primaryAlternateNameID" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="ethnicOriginCode" />
		<TARGET NAME="ACTION" PROPERTY="ethnicOriginCode" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="maritalStatusCode" />
		<TARGET NAME="ACTION" PROPERTY="maritalStatusCode" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="race" />
		<TARGET NAME="ACTION" PROPERTY="race" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="countryOfBirth" />
		<TARGET NAME="ACTION" PROPERTY="countryOfBirth" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="indigenousPersonInd" />
		<TARGET NAME="ACTION" PROPERTY="indigenousPersonInd" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="nationalityCode" />
		<TARGET NAME="ACTION" PROPERTY="nationalityCode" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="placeOfBirth" />
		<TARGET NAME="ACTION" PROPERTY="placeOfBirth" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="indigenousGroupCode" />
		<TARGET NAME="ACTION" PROPERTY="indigenousGroupCode" />
	</CONNECT>


	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="commentsOpt" />
		<TARGET NAME="ACTION" PROPERTY="commentsOpt" />
	</CONNECT>
	
	<CONNECT>
		<SOURCE NAME="DISPLAYSIN" PROPERTY="existingSINNumber" />
		<TARGET NAME="ACTION" PROPERTY="socialSecurityNumber" />
	</CONNECT>


  <CLUSTER
    LABEL_WIDTH="60"
    NUM_COLS="2"
  >

	<CONDITION>
      <IS_TRUE
        NAME="DISPLAYSIN"
        PROPERTY="doesSINNumberExist"
      />
    </CONDITION>

    <FIELD LABEL="Field.Label.SINNumberReadOnly">
		<CONNECT>
			<SOURCE NAME="DISPLAYSIN" PROPERTY="existingSINNumber" />
		</CONNECT>
	</FIELD>
	
	<FIELD LABEL="Field.Label.FirstName">
		<CONNECT>
			<SOURCE NAME="DISPLAY" PROPERTY="firstForename" />
		</CONNECT>
		<CONNECT>
			<TARGET NAME="ACTION" PROPERTY="firstForename" />
		</CONNECT>
	</FIELD>


	<FIELD LABEL="Field.Label.LastName">
		<CONNECT>
			<SOURCE NAME="DISPLAY" PROPERTY="surname" />
		</CONNECT>
		<CONNECT>
			<TARGET NAME="ACTION" PROPERTY="surname" />
		</CONNECT>
	</FIELD>

	<FIELD LABEL="Field.Label.Initials" WIDTH="5" WIDTH_UNITS="CHARS">
		<CONNECT>
			<SOURCE NAME="DISPLAY" PROPERTY="initials" />
		</CONNECT>
		<CONNECT>
			<TARGET NAME="ACTION" PROPERTY="initials" />
		</CONNECT>
	</FIELD>

	<FIELD LABEL="Field.Label.MothersLastName" WIDTH="50">
		<CONNECT>
			<SOURCE NAME="DISPLAY" PROPERTY="motherBirthSurname" />
		</CONNECT>
		<CONNECT>
			<TARGET NAME="ACTION" PROPERTY="motherBirthSurname" />
		</CONNECT>
	</FIELD>

	<FIELD LABEL="Field.Label.RegistrationDate" WIDTH="50">
		<CONNECT>
			<SOURCE NAME="DISPLAY" PROPERTY="registrationDate" />
		</CONNECT>
		<CONNECT>
			<TARGET NAME="ACTION" PROPERTY="registrationDate" />
		</CONNECT>
	</FIELD>

    <!--START: Bug 103166 -->
    <FIELD LABEL="Field.Label.PreferredWrittenLanguage"
      USE_DEFAULT="false" WIDTH="30">
      <CONNECT>
        <SOURCE NAME="DISPLAYCONTACTPREFERENCES" PROPERTY="preferredWrittenLang" />
      </CONNECT>      
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="preferredWrittenLang" />
      </CONNECT>
    </FIELD>
    <!--END: Bug 103166 -->
    
  <FIELD LABEL="Field.Label.Title" USE_BLANK="true"
	USE_DEFAULT="false" WIDTH="50">
		<CONNECT>
			<SOURCE NAME="DISPLAY" PROPERTY="title" />
		</CONNECT>
		<CONNECT>
			<TARGET NAME="ACTION" PROPERTY="title" />
		</CONNECT>
	</FIELD>

	<FIELD LABEL="Field.Label.MiddleName" WIDTH="50">
		<CONNECT>
			<SOURCE NAME="DISPLAY" PROPERTY="otherForename" />
		</CONNECT>
		<CONNECT>
			<TARGET NAME="ACTION" PROPERTY="otherForename" />
		</CONNECT>
	</FIELD>
	
	<FIELD LABEL="Field.Label.Suffix" USE_BLANK="true"
		USE_DEFAULT="false" WIDTH="50">
		<CONNECT>
			<SOURCE NAME="DISPLAY" PROPERTY="nameSuffix" />
		</CONNECT>
		<CONNECT>
			<TARGET NAME="ACTION" PROPERTY="nameSuffix" />
		</CONNECT>
	</FIELD>


    <FIELD LABEL="Field.Label.BirthLastName"  WIDTH="50">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="personBirthName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personBirthName"
        />
      </CONNECT>
    </FIELD>

	<CLUSTER NUM_COLS="1">
		<CONDITION TYPE="DYNAMIC">
			<SCRIPT EXPRESSION="isRestrictedCountryAndForignGovtSelected" />
		</CONDITION>
		<FIELD LABEL="Field.Label.Gender" USE_DEFAULT="true" WIDTH="50">

			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="gender" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="gender" />
			</CONNECT>
		</FIELD>
	</CLUSTER>

	<FIELD LABEL="Field.Label.DateofBirth" USE_DEFAULT="false"
		WIDTH="50">
		<CONNECT>
			<SOURCE NAME="DISPLAY" PROPERTY="dateOfBirth" />
		</CONNECT>
		<CONNECT>
			<TARGET NAME="ACTION" PROPERTY="dateOfBirth" />
		</CONNECT>
	</FIELD>
    
    <!--START: Bug 103166 -->
    <FIELD LABEL="Field.Label.PreferredLanguage" USE_DEFAULT="false"
      WIDTH="30">       
      <CONNECT>
        <SOURCE NAME="DISPLAYCONTACTPREFERENCES" PROPERTY="preferredOralLang" />
      </CONNECT>      
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="preferredOralLang" />
      </CONNECT>
    </FIELD>
    <!--END: Bug 103166 -->
    
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="60"
    NUM_COLS="2"
  >

	<CONDITION>
      <IS_FALSE
        NAME="DISPLAYSIN"
        PROPERTY="doesSINNumberExist"
      />
    </CONDITION>

    <FIELD LABEL="Field.Label.SINNumber">
		<CONNECT>
			<SOURCE NAME="DISPLAYSIN" PROPERTY="existingSINNumber" />
		</CONNECT>
		<CONNECT>
			<TARGET NAME="ACTION" PROPERTY="socialSecurityNumber" />
		</CONNECT>
	</FIELD>


	<FIELD LABEL="Field.Label.FirstName">
		<CONNECT>
			<SOURCE NAME="DISPLAY" PROPERTY="firstForename" />
		</CONNECT>
		<CONNECT>
			<TARGET NAME="ACTION" PROPERTY="firstForename" />
		</CONNECT>
	</FIELD>


	<FIELD LABEL="Field.Label.LastName">
		<CONNECT>
			<SOURCE NAME="DISPLAY" PROPERTY="surname" />
		</CONNECT>
		<CONNECT>
			<TARGET NAME="ACTION" PROPERTY="surname" />
		</CONNECT>
	</FIELD>

	<FIELD LABEL="Field.Label.Initials" WIDTH="5" WIDTH_UNITS="CHARS">
		<CONNECT>
			<SOURCE NAME="DISPLAY" PROPERTY="initials" />
		</CONNECT>
		<CONNECT>
			<TARGET NAME="ACTION" PROPERTY="initials" />
		</CONNECT>
	</FIELD>

	<FIELD LABEL="Field.Label.MothersLastName" WIDTH="50">
		<CONNECT>
			<SOURCE NAME="DISPLAY" PROPERTY="motherBirthSurname" />
		</CONNECT>
		<CONNECT>
			<TARGET NAME="ACTION" PROPERTY="motherBirthSurname" />
		</CONNECT>
	</FIELD>

	<FIELD LABEL="Field.Label.RegistrationDate" WIDTH="50">
		<CONNECT>
			<SOURCE NAME="DISPLAY" PROPERTY="registrationDate" />
		</CONNECT>
		<CONNECT>
			<TARGET NAME="ACTION" PROPERTY="registrationDate" />
		</CONNECT>
	</FIELD>

    <!--START: Bug 103166 -->
    <FIELD LABEL="Field.Label.PreferredWrittenLanguage"
      USE_DEFAULT="false" WIDTH="30">
      <CONNECT>
        <SOURCE NAME="DISPLAYCONTACTPREFERENCES" PROPERTY="preferredWrittenLang" />
      </CONNECT>      
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="preferredWrittenLang" />
      </CONNECT>
    </FIELD>
    <!--END: Bug 103166 -->
    
  <FIELD LABEL="Field.Label.Title" USE_BLANK="true"
	USE_DEFAULT="false" WIDTH="50">
		<CONNECT>
			<SOURCE NAME="DISPLAY" PROPERTY="title" />
		</CONNECT>
		<CONNECT>
			<TARGET NAME="ACTION" PROPERTY="title" />
		</CONNECT>
	</FIELD>

	<FIELD LABEL="Field.Label.MiddleName" WIDTH="50">
		<CONNECT>
			<SOURCE NAME="DISPLAY" PROPERTY="otherForename" />
		</CONNECT>
		<CONNECT>
			<TARGET NAME="ACTION" PROPERTY="otherForename" />
		</CONNECT>
	</FIELD>
	
	<FIELD LABEL="Field.Label.Suffix" USE_BLANK="true"
		USE_DEFAULT="false" WIDTH="50">
		<CONNECT>
			<SOURCE NAME="DISPLAY" PROPERTY="nameSuffix" />
		</CONNECT>
		<CONNECT>
			<TARGET NAME="ACTION" PROPERTY="nameSuffix" />
		</CONNECT>
	</FIELD>


    <FIELD LABEL="Field.Label.BirthLastName"  WIDTH="50">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="personBirthName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personBirthName"
        />
      </CONNECT>
    </FIELD>

	<CLUSTER NUM_COLS="1">
		<CONDITION TYPE="DYNAMIC">
			<SCRIPT EXPRESSION="isRestrictedCountryAndForignGovtSelected" />
		</CONDITION>
		<FIELD LABEL="Field.Label.Gender" USE_DEFAULT="true" WIDTH="50">

			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="gender" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="gender" />
			</CONNECT>
		</FIELD>
	</CLUSTER>

	<FIELD LABEL="Field.Label.DateofBirth" USE_DEFAULT="false"
		WIDTH="50">
		<CONNECT>
			<SOURCE NAME="DISPLAY" PROPERTY="dateOfBirth" />
		</CONNECT>
		<CONNECT>
			<TARGET NAME="ACTION" PROPERTY="dateOfBirth" />
		</CONNECT>
	</FIELD>
    
    <!--START: Bug 103166 -->
    <FIELD LABEL="Field.Label.PreferredLanguage" USE_DEFAULT="false"
      WIDTH="30">       
      <CONNECT>
        <SOURCE NAME="DISPLAYCONTACTPREFERENCES" PROPERTY="preferredOralLang" />
      </CONNECT>      
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="preferredOralLang" />
      </CONNECT>
    </FIELD>
    <!--END: Bug 103166 -->
    
  </CLUSTER>



  <CLUSTER LABEL_WIDTH="40" NUM_COLS="2" TAB_ORDER="ROW"
	TITLE="Cluster.Title.PrimaryAddress">
	<FIELD>
		<CONNECT>
			<SOURCE NAME="DISPLAY" PROPERTY="addressData" />
		</CONNECT>
		<CONNECT>
			<TARGET NAME="ACTION" PROPERTY="addressData" />
		</CONNECT>
	</FIELD>
	</CLUSTER>

	<CLUSTER LABEL_WIDTH="50" NUM_COLS="2">
		<FIELD LABEL="Field.Label.MailingAdd"
			CONTROL_REF="mailBooleanRef">
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="isMailingAddSame" />
			</CONNECT>
		</FIELD>
	</CLUSTER>
<!--  ADO-7089 Start End new attributes and renamed OOTB Attributes -->

	<CLUSTER BEHAVIOR="COLLAPSED"
		DESCRIPTION="Cluster.Description.MailingAddress" LABEL_WIDTH="40"
		NUM_COLS="2" TAB_ORDER="ROW" TITLE="Cluster.Title.MailingAddress">

		<CONDITION TYPE="DYNAMIC">
			<SCRIPT EXPRESSION="mailingAddressSame" />
		</CONDITION>

		<FIELD>
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="mailingAddressData" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="mailingAddressData" />
			</CONNECT>
		</FIELD>
	</CLUSTER>
</VIEW>
