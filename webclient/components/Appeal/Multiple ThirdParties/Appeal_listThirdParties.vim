<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008, 2010 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows user to list all third parties for an appeal case.    -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="ThirdParty"
    NAME="DISPLAY"
    OPERATION="listThirdParties"
  />


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="implCaseID"/>
  <PAGE_PARAMETER NAME="priorAppealCaseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$caseID"
    />
  </CONNECT>


  <ACTION_SET
    ALIGNMENT="LEFT"
    TOP="false"
  >
    <ACTION_CONTROL
      IMAGE="NewButton"
      LABEL="ActionControl.Label.New"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Appeal_createThirdParty"
      >
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
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$listDtls$contextDescription$description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="contextDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>


  <LIST>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Appeal_viewThirdParty">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$listDtls$listDtls$dtls$thirdPartyID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="thirdPartyID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$listDtls$listDtls$dtls$thirdPartyName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="thirdPartyName"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$listDtls$contextDescription$description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="contextDescription"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$listDtls$listDtls$dtls$concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
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
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="implCaseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="implCaseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="priorAppealCaseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="priorAppealCaseID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>
    
    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <!-- BEGIN, CR00198210, PS -->
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Appeal_modifyThirdParty"
        >
          <!-- END, CR00198210 -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$listDtls$listDtls$dtls$thirdPartyID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="thirdPartyID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$listDtls$listDtls$dtls$thirdPartyName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="thirdPartyName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$listDtls$contextDescription$description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="contextDescription"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$listDtls$listDtls$dtls$concernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL
        IMAGE="RemoveButton"
        LABEL="ActionControl.Label.Remove"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Appeal_removeThirdParty"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$listDtls$listDtls$dtls$thirdPartyID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="thirdPartyID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$listDtls$listDtls$dtls$thirdPartyName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="thirdPartyName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$listDtls$contextDescription$description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="contextDescription"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$listDtls$listDtls$dtls$concernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleID"
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
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>
    
    <FIELD
      LABEL="Field.Label.ThirdPartyName"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$listDtls$listDtls$dtls$thirdPartyName"
        />
      </CONNECT>
      <LINK PAGE_ID="IntegratedCase_resolveParticipantHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$listDtls$listDtls$dtls$caseParticipantRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseParticipantRoleID"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    
    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="thirdPartyTypeCode"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD
      LABEL="Field.Label.AttendanceRequired"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="attendanceRequired"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD
      LABEL="Field.Label.FromDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fromDate"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD
      LABEL="Field.Label.ToDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="toDate"
        />
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>
