<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2010 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Evidence infrastructure page that lists the correction history of a    -->
<!-- specific evidence record                                               -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <SERVER_INTERFACE
    CLASS="Evidence"
    NAME="DISPLAY"
    OPERATION="listEvidenceCorrectionHistory"
    PHASE="DISPLAY"
  />
  <PAGE_PARAMETER NAME="evidenceID"/>
  <PAGE_PARAMETER NAME="evidenceType"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="evidenceID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$evidenceID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="evidenceType"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="evidenceType"
    />
  </CONNECT>
  <LIST>


    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Evidence_resolveHistoryRecordFromMetaData">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="evidenceType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceType"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="dtls$evidenceID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


    <FIELD
      LABEL="Field.label.ChangeSummary"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$changeSummary"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.label.Period"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$period"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.label.UpdatedBy"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$updatedBy"
        />
      </CONNECT>
    </FIELD>
  </LIST>
</VIEW>
