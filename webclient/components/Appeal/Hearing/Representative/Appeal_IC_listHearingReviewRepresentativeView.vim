<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008, 2010 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of       -->
<!-- Curam Software Ltd. ("Confidential Information"). You shall not        -->
<!-- disclose such Confidential Information and shall use it only in        -->
<!-- accordance with the terms of the license agreement you entered into    -->
<!-- with Curam Software.                                                   -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The page allows user to view a list of representatives.                -->
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
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="result$appealContextDescription$description"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="HearingRepresentative"
    NAME="DISPLAY"
    OPERATION="viewHearingRepresentativeListForICByHearingID"
    PHASE="DISPLAY"
  />


  <ACTION_SET BOTTOM="false">
    <ACTION_CONTROL
      IMAGE="NewButton"
      LABEL="ActionControl.Label.New"
      TYPE="ACTION"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Appeal_createRepresentative"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="hearingID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="hearingID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$appealContextDescription$description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="contextDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>


  <PAGE_PARAMETER NAME="hearingID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="hearingID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$hearingIDKeyHR$hearingIDKeyHR$hearingID"
    />
  </CONNECT>


  <LIST>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Appeal_IC_viewHearingReviewRepresentative">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$readDetailsList$readDetailsList$hearingRepresentativeID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="representativeID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$appealContextDescription$description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="contextDescription"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="hearingID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="hearingID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL
        LABEL="ActionControl.Label.Edit"
        TYPE="ACTION"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Appeal_IC_modifyRepresentativeFromList"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$readDetailsList$readDetailsList$hearingRepresentativeID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="representativeID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$appealContextDescription$description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="contextDescription"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$readDetailsList$readDetailsList$name"
        />
      </CONNECT>
      <LINK PAGE_ID="IntegratedCase_resolveParticipantHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$readDetailsList$readDetailsList$caseParticipantRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseParticipantRoleID"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <FIELD LABEL="Field.Label.Type">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$readDetailsList$readDetailsList$typeCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Fee">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$readDetailsList$readDetailsList$feeAmount"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Attendance">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$readDetailsList$readDetailsList$participatedCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$readDetailsList$readDetailsList$recordStatus"
        />
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>
