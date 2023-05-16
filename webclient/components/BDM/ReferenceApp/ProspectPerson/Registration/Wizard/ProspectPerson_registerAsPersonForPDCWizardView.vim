<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to enter person registration details.        -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="primaryAddressID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="primaryAddressID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="mailingAddressID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="mailingAddressID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="primaryPhoneNumberID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="primaryPhoneNumberID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="concernRolePhoneNumberID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="concernRolePhoneNumberID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="commExceptionID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="commExceptionID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="citizenshipID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="citizenshipID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="foreignResidencyID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="foreignResidencyID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="primaryAlternateNameID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="primaryAlternateNameID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="ethnicOriginCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="ethnicOriginCode"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="maritalStatusCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="maritalStatusCode"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="race"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="race"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="countryOfBirth"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="countryOfBirth"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="indigenousPersonInd"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="indigenousPersonInd"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="nationalityCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="nationalityCode"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="placeOfBirth"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="placeOfBirth"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="indigenousGroupCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="indigenousGroupCode"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="commentsOpt"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="commentsOpt"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="60"
    NUM_COLS="2"
  >


    <FIELD LABEL="Field.Label.ReferenceNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="socialSecurityNumber"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="socialSecurityNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.FirstName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="firstForename"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="firstForename"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.LastName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="surname"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="surname"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Initials"
      WIDTH="5"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="initials"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="initials"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.MothersLastName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="motherBirthSurname"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="motherBirthSurname"
        />
      </CONNECT>
    </FIELD>

<FIELD
      LABEL="Field.Label.DateofBirth"
      USE_DEFAULT="false"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateOfBirth"
        />
      </CONNECT>


      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dateOfBirth"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.RegistrationDate"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="registrationDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="registrationDate"
        />
      </CONNECT>
    </FIELD>
    
	<FIELD
      LABEL="Field.Label.PreferredLanguage"
      USE_BLANK="true"
      USE_DEFAULT="false"
       WIDTH="50"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="preferredOralLang"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD
      LABEL="Field.Label.PreferredCommunication"
      USE_BLANK="true"
      USE_DEFAULT="false"
       WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="prefCommMethod"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="prefCommMethod"
        />
      </CONNECT>
    </FIELD>
  
  <FIELD
      LABEL="Field.Label.Title"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="title"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="title"
        />
      </CONNECT>
    </FIELD>
  


    <FIELD LABEL="Field.Label.MiddleName"  WIDTH="50">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="otherForename"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="otherForename"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Suffix"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="nameSuffix"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="nameSuffix"
        />
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

<FIELD
      LABEL="Field.Label.Gender"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="50"
    >
      <!-- END, CR00378951 -->


      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="gender"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="gender"
        />
      </CONNECT>
    </FIELD>
 <!--  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  > -->



<!-- 
    <FIELD
      LABEL="Field.Label.SpecialInterest"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="specialInterestCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="specialInterestCode"
        />
      </CONNECT>
    </FIELD> -->


    <FIELD
      LABEL="Field.Label.DateofDeath"
      USE_DEFAULT="false"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateOfDeath"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dateOfDeath"
        />
      </CONNECT>
    </FIELD>

  <FIELD LABEL="Field.Label.PreferredPublicOffice"  WIDTH="50">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="preferredPublicOfficeName"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="preferredPublicOfficeContact"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="preferredPublicOfficeContact"
        />
      </CONNECT>
    </FIELD>
    
    
    <!--  ADO-7089 Start Added new attributes and renamed OOTB Attributes -->
 
    <FIELD
      LABEL="Field.Label.PreferredWrittenLanguage"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="preferredLanguage"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="preferredWrittenLang"
        />
      </CONNECT>
    </FIELD>
<!--  ADO-7089 Start End new attributes and renamed OOTB Attributes -->
    
    


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TAB_ORDER="ROW"
    TITLE="Cluster.Title.PrimaryAddress"
  >


    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="addressData"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="addressData"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>
  <!--  ADO-7089 Start Added new attributes and renamed OOTB Attributes -->
 <CLUSTER
  	LABEL_WIDTH="50"
    NUM_COLS="2">
  		<FIELD LABEL="Field.Label.MailingAdd" CONTROL_REF="mailBooleanRef">
      		<CONNECT>
        	<TARGET
          	NAME="ACTION"
          	PROPERTY="isMailingAddSame"
        	/>
      		</CONNECT>
    	</FIELD>
    </CLUSTER>
<!--  ADO-7089 Start End new attributes and renamed OOTB Attributes -->

  <CLUSTER
  BEHAVIOR="COLLAPSED"
    DESCRIPTION="Cluster.Description.MailingAddress"
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TAB_ORDER="ROW"
    TITLE="Cluster.Title.MailingAddress"
  >
  
<CONDITION TYPE="DYNAMIC">
        <SCRIPT EXPRESSION="mailingAddressSame" />
	</CONDITION>

    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="mailingAddressData"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="mailingAddressData"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.PhoneNumber"
  >


    <FIELD
      LABEL="Field.Label.PhoneType"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="phoneType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="phoneType"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.AreaCode"
      WIDTH="5"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="phoneAreaCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="phoneAreaCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Extension"
      WIDTH="5"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="phoneExtension"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="phoneExtension"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.CountryCode"
      WIDTH="5"
      WIDTH_UNITS="CHARS"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="phoneCountryCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$phoneCountryCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Phone"  WIDTH="50">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="phoneNumber"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="phoneNumber"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
