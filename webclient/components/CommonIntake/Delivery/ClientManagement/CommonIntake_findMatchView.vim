<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->

<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="Person"
    NAME="DISPLAY"
    OPERATION="readSearchWithNicknamesIndicator"
    PHASE="DISPLAY"
  />
  
  <SERVER_INTERFACE
    CLASS="ProspectPerson"
    NAME="DISPLAYPROSPECT"
    OPERATION="readProspectPerson"
    PHASE="DISPLAY"
  />
  
  <PAGE_PARAMETER NAME="concernRoleID"/>
  
  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="concernRoleID" />
    <TARGET NAME="DISPLAYPROSPECT" PROPERTY="maintainConcernRoleKey$concernRoleID" />
  </CONNECT>
  


  <CLUSTER
    LABEL_WIDTH="55"
    NUM_COLS="2"
    TITLE="Cluster.Title.SearchCriteria"
  >


    <FIELD LABEL="Field.Label.ReferenceNumber">
      <CONNECT>
        <SOURCE NAME="DISPLAYPROSPECT" PROPERTY="dtls$primaryAlternateID"/>
      </CONNECT>
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
    LABEL_WIDTH="55"
    NUM_COLS="2"
    TITLE="Cluster.Title.AdditionalSearchCriteria"
  >


    <FIELD LABEL="Field.Label.FirstName">
      <CONNECT>
        <SOURCE NAME="DISPLAYPROSPECT" PROPERTY="dtls$firstForename"/>
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$forename"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.LastName">
      <CONNECT>
        <SOURCE NAME="DISPLAYPROSPECT" PROPERTY="dtls$surname"/>
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$surname"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.DateOfBirth"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <SOURCE NAME="DISPLAYPROSPECT" PROPERTY="dtls$dateOfBirth"/>
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$dateOfBirth"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AddressLineOne">
      <CONNECT>
        <SOURCE NAME="DISPLAYPROSPECT" PROPERTY="dtls$addressLine1"/>
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$addressLine1"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.City">
      <CONNECT>
        <SOURCE NAME="DISPLAYPROSPECT" PROPERTY="dtls$city"/>
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$city"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Nickname">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$searchWithNicknamesInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$nicknameInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DoubleMetaphone">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$soundsLikeInd"
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
          NAME="DISPLAYPROSPECT"
          PROPERTY="dtls$gender"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$gender"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AddressLineTwo">
     
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$addressLine2"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.BirthLastName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAYPROSPECT"
          PROPERTY="dtls$personBirthName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$birthSurname"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
