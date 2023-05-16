<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to check if the person has already been      -->
<!-- registered.                                                            -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="Person"
    NAME="DISPLAYNICKNAMES"
    OPERATION="readSearchWithNicknamesIndicator"
  />

	<CLUSTER LABEL_WIDTH="40" NUM_COLS="2"
		TITLE="Cluster.Title.SearchCriteria">

		<FIELD LABEL="Field.Label.ReferenceNumber">
			<CONNECT>
				<SOURCE NAME="DISPLAY" PROPERTY="alternateRefNumber" />
			</CONNECT>
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="referenceNumber" />
			</CONNECT>
		</FIELD>

		<FIELD LABEL="Field.Label.CorrespondenceTrackingNumber">
			<CONNECT>
				<TARGET NAME="ACTION" PROPERTY="correspondenceTrackingNumber" />
			</CONNECT>
		</FIELD>


	</CLUSTER>


    <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.AdditionalSearchCriteria">

	<CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="1">


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
          PROPERTY="forename"
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
      LABEL="Field.Label.DateOfBirth"
      USE_DEFAULT="false"
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
          PROPERTY="searchKey$dateOfBirth"
        />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.Label.BirthLastName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="personBirthName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="birthSurname"
        />
      </CONNECT>
    </FIELD>

	<FIELD LABEL="Field.Label.AddressLineTwo">
      
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="addressLine2"
        />
      </CONNECT>
    </FIELD>
   

	<FIELD LABEL="Cluster.Field.Label.Province">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="stateProvince"
        />
      </CONNECT>
    </FIELD>
	
	<FIELD LABEL="Cluster.Field.Label.Country" USE_BLANK="TRUE"
        USE_DEFAULT="FALSE">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="countryCode"
        />
      </CONNECT>
    </FIELD>

	</CLUSTER>
	
	<CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="1">

	<FIELD CONTROL="SKIP"/>
	<FIELD CONTROL="SKIP"/>
	<FIELD CONTROL="SKIP"/>
	<FIELD CONTROL="SKIP"/>
	<FIELD CONTROL="SKIP"/>
	<FIELD CONTROL="SKIP"/>
	<FIELD CONTROL="SKIP"/>

    <FIELD LABEL="Field.Label.DoubleMetaphone">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="soundsLikeInd"
        />
      </CONNECT>
    </FIELD>

	<FIELD CONTROL="SKIP"/>
	
    <!-- BEGIN, CR00378951, PS -->
    <FIELD
      LABEL="Field.Label.Gender"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="60"
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
   
	<FIELD LABEL="Field.Label.AddressLineOne">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="addressLine1"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="addressLine1"
        />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.Label.City">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="city"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="city"
        />
      </CONNECT>
    </FIELD>    
	
	<FIELD LABEL="Cluster.Field.Label.Postal">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="postalCode"
        />
      </CONNECT>
    </FIELD>

	</CLUSTER>

	</CLUSTER>


  <CLUSTER>
    <ACTION_SET
      ALIGNMENT="CENTER"
      BOTTOM="false"
    >
      <ACTION_CONTROL
      	ACTION_ID="SEARCH"
      	DEFAULT="true"
        IMAGE="SearchButton"
        LABEL="ActionControl.Label.Search"
        TYPE="SUBMIT"
      >
        <LINK
          PAGE_ID="THIS"
          SAVE_LINK="false"
        />
      </ACTION_CONTROL>


      <ACTION_CONTROL
      	ACTION_ID="RESETPAGE"
        IMAGE="ResetButton"
        LABEL="ActionControl.Label.Reset"
        TYPE="SUBMIT"
      >
        <LINK
          DISMISS_MODAL="false"
          PAGE_ID="ProspectPerson_duplicateCheckForPDCWizard"
          SAVE_LINK="false"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="concernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>
  </CLUSTER>


  <LIST
    PAGINATED="false"    
    TITLE="List.Title.SearchResults"
  >


    <!-- BEGIN, CR00378951, PS -->
    <FIELD
      LABEL="Field.Title.Name"
      WIDTH="25"
    >
      <!-- END, CR00378951 -->

      <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="xmlPersonData"
          />
      </CONNECT>
    </FIELD>

    <!-- BEGIN, CR00378951, PS -->
    <FIELD
      LABEL="Field.Title.DateOfBirth"
      WIDTH="24"
    >
      <!-- END, CR00378951 -->


      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="dtlsList$dateOfBirth"
        />
      </CONNECT>
    </FIELD>

    <FIELD
      LABEL="Field.Title.SIN"
      WIDTH="20">
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="sinNumber"
        />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.Title.Address">
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="formattedAddress"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
