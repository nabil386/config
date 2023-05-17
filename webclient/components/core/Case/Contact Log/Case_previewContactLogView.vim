<?xml version="1.0" encoding="UTF-8"?>
<!--
    Licensed Materials - Property of IBM
    
    Copyright IBM Corporation  2018. All Rights Reserved.
    
    US Government Users Restricted Rights - Use, duplication or disclosure 
    restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<VIEW
  PAGE_ID="Case_previewContactLogView"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.Title"
      />
    </CONNECT>
  </PAGE_TITLE>
  
  <SERVER_INTERFACE
    CLASS="Case"
    NAME="DISPLAY"
    OPERATION="previewCaseContactLogDetails"
    PHASE="DISPLAY"
  />
  <SERVER_INTERFACE
    CLASS="ContactLog"
    NAME="DISPLAYPRINT"
    OPERATION="getPrintContactLogDetails"
    PHASE="DISPLAY"
  />

  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="multiSelectStr"/>
  <PAGE_PARAMETER NAME="wizardStateID"/>

  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
  </CONNECT>

  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="multiSelectStr"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="previewCaseContactLogIDKey$dtls$multiSelectStr"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAYPRINT"
      PROPERTY="previewCaseContactLogKey$casekey$caseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="multiSelectStr"
    />
    <TARGET
      NAME="DISPLAYPRINT"
      PROPERTY="previewCaseContactLogKey$dtls$multiSelectStr"
    />
  </CONNECT>

  <LIST
    PAGINATED="FALSE"
    STYLE="preview-contact-log-wrapper"
  >
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="previewXMLData"
        />
      </CONNECT>
    </FIELD>
  </LIST>

</VIEW>
