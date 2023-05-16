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


  <CLUSTER DESCRIPTION="PageTitle.Description">
    <FIELD CONTROL="SKIP"/>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.SearchCriteria"
  >


    <FIELD LABEL="Field.Label.ReferenceNumber">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="referenceNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD CONTROL="SKIP"/>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.AdditionalSearchCriteria"
  >


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
          PROPERTY="personSearchKey$dateOfBirth"
        />
      </CONNECT>
    </FIELD>
<!-- ADO-7089 Start Rearranged Fields  -->
<FIELD LABEL="Field.Label.AddressLineTwo">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="addressLine2"
        />
      </CONNECT>
    </FIELD>
    
<!-- ADO-7089 End Rearranged Fields  -->

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


    <FIELD LABEL="Field.Label.Nickname">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="nicknameInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DoubleMetaphone">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="soundsLikeInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Gender"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="35"
    >
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

<!-- ADO-7089 Start Rearranged Fields  -->
    
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
<!-- ADO-7089 End Rearranged Fields  -->
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


  </CLUSTER>


  <CLUSTER>
    <ACTION_SET
      ALIGNMENT="CENTER"
      BOTTOM="false"
    >
      <ACTION_CONTROL
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


    <DETAILS_ROW>
      <INLINE_PAGE
        URI_SOURCE_NAME="ACTION"
        URI_SOURCE_PROPERTY="personTabDetailsURL"
      />
    </DETAILS_ROW>


    <FIELD
      LABEL="Field.Title.Name"
      WIDTH="44"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="concernRoleName"
        />
      </CONNECT>
      <LINK PAGE_ID="Participant_resolve">
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD
      LABEL="Field.Title.Address"
      WIDTH="44"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="formattedAddress"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.DateOfBirth"
      WIDTH="12"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="dtlsList$dateOfBirth"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
