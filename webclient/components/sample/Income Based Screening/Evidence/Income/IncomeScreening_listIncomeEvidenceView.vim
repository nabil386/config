<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page displays the list of household income. -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE DESCRIPTION="PageTitle.Description">
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText1"
      />
    </CONNECT>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="description"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="IncomeScreening"
    NAME="DISPLAY"
    OPERATION="listIncomeEvidence"
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


    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL
        IMAGE="NewButton"
        LABEL="ActionControl.Label.New"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="IncomeScreening_createIncomeEvidence"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="caseEvidenceTypeID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseEvidenceTypeID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="pageDescription"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="IncomeScreening_modifyIncomeEvidenceFromList"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="fsHouseholdIncomeID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="householdIncomeID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="caseEvidenceTypeID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseEvidenceTypeID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="pageDescription"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


    </ACTION_SET>


    <FIELD
      LABEL="Field.Label.Name"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="concernRoleName"
        />
      </CONNECT>
      <LINK
        PAGE_ID="Participant_resolveRoleHome"
        SAVE_LINK="false"
      >
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
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="concernRoleType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="participantType"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Relationship"
      WIDTH="18"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rshipToHOH"
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
          PROPERTY="incomeTypeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.MonthlyAmount"
      WIDTH="22"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="currMonthlyAmount"
        />
      </CONNECT>
    </FIELD>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="IncomeScreening_viewIncomeEvidence">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="fsHouseholdIncomeID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="householdIncomeID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseEvidenceTypeID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseEvidenceTypeID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>
  </LIST>
</VIEW>
