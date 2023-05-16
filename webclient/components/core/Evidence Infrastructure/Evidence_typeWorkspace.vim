<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2012, 2014. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2010-2011 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Evidence infrastructure page containing a list of evidence records     -->
<!-- for a user which are active, in edit and pending removal               -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <!-- Declare the 'display' server bean -->
  <SERVER_INTERFACE
    CLASS="Evidence"
    NAME="DISPLAY"
    OPERATION="listBusinessObjectsForEvidenceType"
    PHASE="DISPLAY"
  />


  <!-- List fields on this page -->
  <LIST>


    <FIELD WIDTH="5">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="verificationOrIssuesExist"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00426702, GK -->
    <FIELD WIDTH="5">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="isUnevaluatedEvidence"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00426702 -->
    <FIELD
      LABEL="Field.Label.participantName"
      WIDTH="17"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$participantName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.summary"
      WIDTH="26"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$summary"
        />
      </CONNECT>
      <LINK PAGE_ID="Evidence_resolveObjectFromMetaData">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$successionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="successionID"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <FIELD
      LABEL="Field.Label.period"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$period"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.updateCount"
      WIDTH="12"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$updateCount"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.latestActivity"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$latestActivity"
        />
      </CONNECT>
    </FIELD>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Evidence_listEvdInstanceChanges">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$successionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="successionID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>
  </LIST>
</VIEW>
