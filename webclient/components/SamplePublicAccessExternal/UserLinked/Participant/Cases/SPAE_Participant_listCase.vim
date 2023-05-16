<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2006-2007, 2010 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to view a list of cases for a Person.        -->
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


  <PAGE_PARAMETER NAME="linkID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="linkID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="linkID"
    />
  </CONNECT>


  <SERVER_INTERFACE
    CLASS="ExternalLinkedParticipant"
    NAME="DISPLAY"
    OPERATION="listCases"
  />
  <LIST>
    <FIELD
      LABEL="Field.Title.CaseReference"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseHeaderConcernRoleDetailsList$dtls$caseReference"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.Type"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseHeaderConcernRoleDetailsList$dtls$productTypeDesc"
        />
      </CONNECT>
    </FIELD>
    <!--
        <FIELD LABEL="Field.Title.Owner" WIDTH="25">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="caseHeaderConcernRoleDetailsList$dtls$ownerName" />
            </CONNECT>
        </FIELD>
 -->
    <FIELD
      LABEL="Field.Title.StartDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseHeaderConcernRoleDetailsList$dtls$startDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.Status"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseHeaderConcernRoleDetailsList$dtls$statusCode"
        />
      </CONNECT>
    </FIELD>
  </LIST>
</VIEW>
