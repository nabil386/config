<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2010, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010, 2011 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page displays search results for an investigation search.         -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <LIST TITLE="List.Title.SearchResult">


    <DETAILS_ROW>
      <INLINE_PAGE
        URI_SOURCE_NAME="ACTION"
        URI_SOURCE_PROPERTY="result$listDtls$searchDtls$contextPanelName"
      />
    </DETAILS_ROW>


    <FIELD
      LABEL="Field.Label.Reference"
      WIDTH="11"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$listDtls$searchDtls$caseReference"
        />
      </CONNECT>
      <LINK PAGE_ID="Case_resolveCaseHome">
        <CONDITION>
          <IS_FALSE
            NAME="ACTION"
            PROPERTY="result$listDtls$searchDtls$restricted"
          />
        </CONDITION>
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="result$listDtls$searchDtls$caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <!-- BEGIN, CR00359828, PS -->
    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="16"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$listDtls$searchDtls$investigationType"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Client"
      WIDTH="15"
    >
      <CONNECT>
        <!-- END, CR00359828 -->
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$listDtls$searchDtls$concernRoleName"
        />
      </CONNECT>
      <!--BEGIN, CR00139966, MC-->
      <LINK PAGE_ID="Participant_resolve">
        <CONDITION>
          <IS_FALSE
            NAME="ACTION"
            PROPERTY="result$listDtls$searchDtls$restricted"
          />
        </CONDITION>
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="result$listDtls$searchDtls$concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="result$listDtls$searchDtls$concernRoleType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleType"
          />
        </CONNECT>
      </LINK>
      <!--END, CR00139966-->
    </FIELD>


    <FIELD
      LABEL="Field.Label.Resolution"
      WIDTH="13"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$listDtls$searchDtls$resolution"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00359828, PS -->
    <FIELD
      LABEL="Field.Label.AllegationParticipants"
      WIDTH="25"
    >
      <!-- END, CR00359828 -->
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$listDtls$searchDtls$allegationParticipantCount"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Investigation_listAllegationParticipantRole"
        SAVE_LINK="true"
      >
        <CONDITION>
          <IS_FALSE
            NAME="ACTION"
            PROPERTY="result$listDtls$searchDtls$restricted"
          />
        </CONDITION>
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="result$listDtls$searchDtls$caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD
      LABEL="Field.Label.StartDate"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$listDtls$searchDtls$startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$listDtls$searchDtls$statusCode"
        />
      </CONNECT>
    </FIELD>


  </LIST>
</VIEW>
