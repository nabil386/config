<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2007, 2010 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Lists resolution status history details                                -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>&gt;


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.Title"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="IssueDelivery"
    NAME="DISPLAY"
    OPERATION="listResolutionStatusHistory"
    PHASE="DISPLAY"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="resolutionID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="resolutionKey$resolutionID"
    />
  </CONNECT>


  <PAGE_PARAMETER NAME="resolutionID"/>
  <PAGE_PARAMETER NAME="caseID"/>


  <LIST>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Appeal_viewStatusDetails">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="resolutionStatusHistoryID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="resolutionStatusHistoryID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="resolutionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="resolutionID"
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
      LABEL="Field.Label.Resolution"
      WIDTH="45"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="resolution"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.EffectiveDate"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="effectiveDate"
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
          PROPERTY="resolutionStatus"
        />
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>
