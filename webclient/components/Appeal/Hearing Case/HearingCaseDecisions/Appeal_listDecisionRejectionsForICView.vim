<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2003, 2009-2010 Curam Software.                              -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of       -->
<!-- Curam Software. ("Confidential Information"). You                      -->
<!-- shall not disclose such Confidential Information and shall use it only -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- with Curam Software.                                                   -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows user to list decision rejections for the appeal case. -->
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
    CLASS="DecisionApproval"
    NAME="DISPLAY"
    OPERATION="listRejectionReasonForIC"
  />


  <PAGE_PARAMETER NAME="caseID"/>


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


  <LIST>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Appeal_viewDecisionRejectionDetails">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="decisionRejectionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="decisionRejectionID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="description"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>

    <FIELD
      LABEL="Field.Label.User"
      WIDTH="45"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="userFullName"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Organization_viewUserDetails"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="userName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Date"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="date"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Reason"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reasonCode"
        />
      </CONNECT>
    </FIELD>
  </LIST>
</VIEW>
