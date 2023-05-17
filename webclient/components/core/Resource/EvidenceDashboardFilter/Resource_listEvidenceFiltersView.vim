<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2015. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <SERVER_INTERFACE
    CLASS="EvidenceDashboardFilter"
    NAME="DISPLAY"
    OPERATION="listCurrentCaseTypeDashboardFilters"
    PHASE="DISPLAY"
  />


  <LIST>
    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Resource_editEvidenceFilter"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$dtls$name"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="name"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$dtls$dashBoardFilterID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="dashBoardFilterID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$dtls$caseType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseTypeCode"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$dtls$caseTypeCodeID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseTypeCodeID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="ActionControl.Label.Delete">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Resource_deleteEvidenceFilter"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$dtls$dashBoardFilterID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="dashBoardFilterID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$dtls$caseType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseTypeCode"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$dtls$caseTypeCodeID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseTypeCodeID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$name"
        />
      </CONNECT>
    </FIELD>


    <DETAILS_ROW MINIMUM_EXPANDED_HEIGHT="110">
      <INLINE_PAGE PAGE_ID="Resource_listCaseTypeEvidenceTypesForFilter">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$dashBoardFilterID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="dashBoardFilterID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>
  </LIST>
</VIEW>
