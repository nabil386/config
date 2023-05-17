<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to link evidence types to cases.             -->
<!-- NB: It is the responsibility of the wrapping UIM to set the following  -->
<!-- fields.                                                                -->
<!-- Phase: DISPLAY, Field: key$caseAdminID                                 -->
<!-- Phase: DISPLAY, Field: key$caseType                                    -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText1"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="EvidenceTypesLink"
    NAME="ACTION"
    OPERATION="createEvidenceLink"
    PHASE="ACTION"
  />
  <SERVER_INTERFACE
    CLASS="EvidenceTypesLink"
    NAME="DISPLAY"
    OPERATION="listAvailableEvidenceTypes"
    PHASE="DISPLAY"
  />


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="key$caseAdminID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="associatedID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="key$caseType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="associatedCaseTypeOpt"
    />
  </CONNECT>


  <CLUSTER LABEL_WIDTH="35">
    <FIELD
      ALIGNMENT="LEFT"
      LABEL="Field.Label.Type"
      USE_BLANK="true"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="evidenceTypeIDAndCode"
          NAME="DISPLAY"
          PROPERTY="evidenceTypeCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="evidenceTypeIDAndCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Category.Field.Label.Type"
      USE_BLANK="true"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="category"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="SortOrder.Field.Label.Type"
      USE_DEFAULT="false"
      WIDTH="10"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="sortOrder"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="QuickLink.Field.Label.Type"
      WIDTH="10"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="quickLinkInd"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>
  <ACTION_SET
    ALIGNMENT="CENTER"
    BOTTOM="false"
    TOP="true"
  >
    <ACTION_CONTROL
      IMAGE="AddButton"
      LABEL="ActionControl.Label.Add"
      TYPE="SUBMIT"
    />
    <ACTION_CONTROL
      IMAGE="CancelButton"
      LABEL="ActionControl.Label.Cancel"
      TYPE="ACTION"
    />
  </ACTION_SET>


</VIEW>
