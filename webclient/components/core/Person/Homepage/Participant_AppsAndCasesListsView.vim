<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page list pending applications and current cases on a PDC         -->
<!-- homepage.                                                              -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="PDCPerson"
    NAME="LISTAPPLICATIONS"
    OPERATION="listPendingApplications"
  />


  <SERVER_INTERFACE
    CLASS="PDCPerson"
    NAME="LISTCASES"
    OPERATION="listCurrentCases"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="LISTAPPLICATIONS"
      PROPERTY="concernRoleID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="LISTCASES"
      PROPERTY="concernRoleID"
    />
  </CONNECT>


  <CLUSTER NUM_COLS="2">


    <LIST TITLE="List.Title.PendingApplications">


      <FIELD
        LABEL="Field.Label.ApplicationReference"
        WIDTH="25"
      >
        <CONNECT>
          <SOURCE
            NAME="LISTAPPLICATIONS"
            PROPERTY="reference"
          />
        </CONNECT>
        <LINK
          URI_SOURCE_NAME="LISTAPPLICATIONS"
          URI_SOURCE_PROPERTY="caseURL"
        />
      </FIELD>


      <FIELD
        LABEL="Field.Label.ApplicationType"
        WIDTH="45"
      >
        <CONNECT>
          <SOURCE
            NAME="LISTAPPLICATIONS"
            PROPERTY="typeDescription"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.ApplicationExpiryDate"
        WIDTH="30"
      >
        <CONNECT>
          <SOURCE
            NAME="LISTAPPLICATIONS"
            PROPERTY="expiryDate"
          />
        </CONNECT>
      </FIELD>


    </LIST>


    <LIST TITLE="List.Title.CurrentCases">


      <FIELD
        LABEL="Field.Label.CaseReference"
        WIDTH="25"
      >
        <CONNECT>
          <SOURCE
            NAME="LISTCASES"
            PROPERTY="reference"
          />
        </CONNECT>
        <LINK
          URI_SOURCE_NAME="LISTCASES"
          URI_SOURCE_PROPERTY="caseURL"
        />
      </FIELD>


      <FIELD
        LABEL="Field.Label.CaseType"
        WIDTH="45"
      >
        <CONNECT>
          <SOURCE
            NAME="LISTCASES"
            PROPERTY="typeDescription"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.CaseStartDate"
        WIDTH="30"
      >
        <CONNECT>
          <SOURCE
            NAME="LISTCASES"
            PROPERTY="startDate"
          />
        </CONNECT>
      </FIELD>


    </LIST>


  </CLUSTER>


</VIEW>
