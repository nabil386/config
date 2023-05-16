<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to enter prospect person registration details.        -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <!-- BEGIN, CR00378951, PS -->
  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
  >
    <!-- END, CR00378951 -->


    <FIELD LABEL="Field.Label.ReferenceNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_SEARCH_CRITERIA"
          PROPERTY="referenceNumber"
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
          NAME="DISPLAY_SEARCH_CRITERIA"
          PROPERTY="forename"
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
          NAME="DISPLAY_SEARCH_CRITERIA"
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
        <TARGET
          NAME="ACTION"
          PROPERTY="initials"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.MothersLastName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="motherBirthSurname"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Title"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="title"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.MiddleName">
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
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="nameSuffix"
        />
      </CONNECT>
    </FIELD>


    <!--BEGIN, CR00236822, PB-->
    <FIELD LABEL="Field.Label.BirthLastName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_SEARCH_CRITERIA"
          PROPERTY="birthSurname"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personBirthName"
        />
      </CONNECT>
    </FIELD>
    <!--END, CR00236822-->


    <FIELD
      LABEL="Field.Label.Gender"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_SEARCH_CRITERIA"
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


  </CLUSTER>


  <!-- BEGIN, CR00378951, PS -->
  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
  >
    <!-- END, CR00378951 -->


    <FIELD
      LABEL="Field.Label.DateofBirth"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_SEARCH_CRITERIA"
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
      LABEL="Field.Label.FromAge"
      WIDTH="3"
      WIDTH_UNITS="CHARS"
    >
      <!-- BEGIN, CR00386007, VT-->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="estimatedFromAgeOpt"
        />
      </CONNECT>
      <!-- END, CR00386007 -->
    </FIELD>

    <!--  ADO-7089 Start Added new attributes and renamed OOTB Attributes -->
 <FIELD
      LABEL="Field.Label.PreferredLanguage"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="preferredOralLang"
        />
      </CONNECT>
    </FIELD>
<!--  ADO-7089 Start End new attributes and renamed OOTB Attributes -->

<!-- Preferred Method of Correspondence (including tax slips)
 -->
    <FIELD
      LABEL="Field.Label.PreferredCommunication"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="prefCommMethod"
        />
      </CONNECT>
    </FIELD>



    <FIELD LABEL="Field.Label.RegistrationDate">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="registrationDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.ToAge"
      WIDTH="3"
      WIDTH_UNITS="CHARS"
    >
      <!-- BEGIN, CR00386007, VT -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="estimatedToAgeOpt"
        />
      </CONNECT>
      <!-- END, CR00386007 -->
    </FIELD>
<!--  ADO-7089 Start Added new attributes and renamed OOTB Attributes -->
    <FIELD
      LABEL="Field.Label.PreferredWrittenLanguage"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="preferredWrittenLang"
        />
      </CONNECT>
    </FIELD>
<!--  ADO-7089 Start End new attributes and renamed OOTB Attributes -->

    <FIELD LABEL="Field.Label.PreferredPublicOffice">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="preferredPublicOfficeContact"
        />
      </CONNECT>
    </FIELD>

  </CLUSTER>


  <!-- BEGIN, CR00378951, PS -->
  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TAB_ORDER="ROW"
    TITLE="Cluster.Title.PrivateAddress"
  >
    <!-- END, CR00378951 -->


    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_SEARCH_CRITERIA"
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
  <!-- BEGIN, CR00378951, PS -->
  <CLUSTER
    BEHAVIOR="COLLAPSED"
    DESCRIPTION="Cluster.Description.MailingAddress"
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TAB_ORDER="ROW"
    TITLE="Cluster.Title.MailingAddress"
  >
	<CONDITION TYPE="DYNAMIC">
        <SCRIPT EXPRESSION="mailingAddressSame" />
	</CONDITION>

    <FIELD>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="mailingAddressData"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <!-- BEGIN, CR00378951, PS -->
  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TITLE="Cluster.Title.PhoneNumber"
  >
    <!-- END, CR00378951 -->


    <FIELD
      LABEL="Field.Label.PhoneType"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
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
        <TARGET
          NAME="ACTION"
          PROPERTY="phoneExtension"
        />
      </CONNECT>
    </FIELD>

<!-- ADO-7089 Start Changed country code from text field to dropdown -->
    <FIELD
      LABEL="Field.Label.CountryCode"
      WIDTH="5"
      WIDTH_UNITS="CHARS"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="registrationDtls$phoneCountryCode"
        />
      </CONNECT>
    </FIELD>
<!-- ADO-7089 Start End  Changed country code from text field to dropdown -->

    <FIELD LABEL="Field.Label.Phone">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="phoneNumber"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
