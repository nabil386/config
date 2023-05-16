<?xml version="1.0" encoding="UTF-8"?>
<!-- Description -->
<!-- =========== -->
<!-- This page displays search criteria for a Participant Search            -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="ACTION"
        PROPERTY="informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>

  <SERVER_INTERFACE
    CLASS="BDMThirdPartyParticipantSearch"
    NAME="ACTION"
    OPERATION="searchParticipant"
    PHASE="ACTION"
  />

  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.SearchCriteria"
  >
    <FIELD LABEL="Field.Label.ReferenceNumber">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$referenceNumber"
        />
      </CONNECT>
    </FIELD>

    <FIELD CONTROL="SKIP"/>

  </CLUSTER>

  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    STYLE="cluster-cpr-no-internal-padding"
    TITLE="Cluster.Title.AdditionalSearchCriteria"
  >

    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$concernRoleName"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD
      LABEL="Field.Title.UnitNumber">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$unitNumber"
        />
      </CONNECT>
    </FIELD>
  
    <FIELD
      LABEL="Field.Label.AddressLineTwo">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="addressLine2"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD
      LABEL="Field.Title.StateProvince">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$stateProvince"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD
      LABEL="Field.Title.PostalZip">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$postalCode"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD CONTROL="SKIP"/>
  
    <FIELD
      LABEL="Field.Title.AddressLineOne">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$addressLine1"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD
      LABEL="Field.Title.City">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$city"
        />
      </CONNECT>
    </FIELD>
  
    <FIELD
      LABEL="Field.Title.Country" USE_BLANK="TRUE"
        USE_DEFAULT="FALSE">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$countryCode"
        />
      </CONNECT>
    </FIELD>

    <FIELD CONTROL="SKIP"/>

  </CLUSTER>

</VIEW>
