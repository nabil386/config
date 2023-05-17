<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2016. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE DESCRIPTION="Page.Title.Description">
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="Page.Title"
      />
    </CONNECT>
  </PAGE_TITLE>


  <PAGE_PARAMETER NAME="caseID"/>


  <SERVER_INTERFACE
    CLASS="ManageGuidedChangeNextStep"
    NAME="DISPLAY"
    OPERATION="listCompletedGuidedChangeCOC"
    PHASE="DISPLAY"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseKey$caseID"
    />
  </CONNECT>


  <LIST>
    <FIELD
      LABEL="Field.Label.ChangeType"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$wizardType"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ClientName"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$name"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.DateReported"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$dateReported"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.DateRecorded"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$dateSubmitted"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$status"
        />
      </CONNECT>
    </FIELD>


    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="GuidedChange_listCompletedNextStepsAction">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="guidedChangeCOCID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="guidedChangeCOCID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


  </LIST>


</VIEW>
