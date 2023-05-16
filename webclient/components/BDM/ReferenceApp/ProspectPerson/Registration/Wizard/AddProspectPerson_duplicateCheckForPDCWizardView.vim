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

  <!-- BEGIN, CR00378951, PS -->
  <CLUSTER
    LABEL_WIDTH="60"
    NUM_COLS="2"
    TITLE="Cluster.Title.SearchCriteria"
  >
    <!-- END, CR00378951 -->


    <FIELD LABEL="Field.Label.ReferenceNumber">


      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="referenceNumber"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="referenceNumber"
        />
      </CONNECT>
    </FIELD>


    <!-- STRAT : TASK 90012 : add CorrespondenceTrackingNumber -->
   
    <FIELD LABEL="Field.Label.CorrespondenceTrackingNumber">
       <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="correspondenceTrackingNumber"
        />
      </CONNECT> 
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="correspondenceTrackingNumber"
        />
      </CONNECT>
    </FIELD>
<!-- END  -->


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
          PROPERTY="forename"
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
          PROPERTY="birthSurname"
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
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="addressLine2"
        />
      </CONNECT>
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
    <!-- START: Task 93506: DEV: Address Format Validations --> 
	<FIELD LABEL="Field.Label.UnitNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="unitNumber"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="searchKey$unitNumber"
        />
      </CONNECT>
    </FIELD> 
   <!-- END: Task 93506: DEV: Address Format Validations --> 
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


      <!-- BEGIN, CR00304371, PS -->
      <ACTION_CONTROL
        ACTION_ID="RESETPAGE"
        LABEL="ActionControl.Label.Reset"
        TYPE="SUBMIT"
      >
        <!-- END, CR00304371 -->
        <LINK
          PAGE_ID="AddProspectPerson_duplicateCheckForPDCWizard"
          SAVE_LINK="false"
        >
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="wizardStateID$wizardStateID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="wizardStateID"
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
