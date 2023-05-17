<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2014. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="Evidence"
    NAME="UNEVALEVDINFORMATIONAL"
    OPERATION="getUnevaluatedEvidenceInformation"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="UNEVALEVDINFORMATIONAL"
      PROPERTY="caseID"
    />
  </CONNECT>
  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="UNEVALEVDINFORMATIONAL"
        PROPERTY="result$dtls$msg"
      />
    </CONNECT>
  </INFORMATIONAL>


</VIEW>
