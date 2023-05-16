<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view for special caution history.                         -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_PARAMETER NAME="caseID"/>


  <LIST>


    <FIELD
      LABEL="Field.Label.CaseMember"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="participantName"
        />
      </CONNECT>
      <LINK PAGE_ID="Participant_resolve">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Category"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="categoryCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="typeCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.StartDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.EndDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="recordStatus"
        />
      </CONNECT>
    </FIELD>


    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="DefaultICSpecialCaution_viewFromHistory">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="specialCautionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="specialCautionID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


  </LIST>


</VIEW>
