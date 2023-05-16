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
<!-- This page displays the list of household expenses. -->
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
    OPERATION="listExpenseEvidence"
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


  <LIST TITLE="List.Title.MedicalExpenses">
    <!-- An action set containing the single new button. -->
    <ACTION_SET BOTTOM="false">
      <ACTION_CONTROL
        IMAGE="NewButton"
        LABEL="ActionControl.Label.NewMedicalExpense"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="IncomeScreening_createMedicalExpenseEvidence"
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
    </ACTION_SET>


    <CONTAINER
      LABEL="Container.Label.Action"
      SEPARATOR="Container.Separator"
      WIDTH="15"
    >
      <ACTION_CONTROL LABEL="ActionControl.Label.View">
        <LINK PAGE_ID="IncomeScreening_viewMedicalExpenseEvidence">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="medExpList$fsHouseholdExpensesID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="householdExpensesID"
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
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="IncomeScreening_modifyMedicalExpenseEvidenceFromList"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="medExpList$fsHouseholdExpensesID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="householdExpensesID"
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
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


    <FIELD
      LABEL="Field.Label.Name"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="medExpList$concernRoleName"
        />
      </CONNECT>
      <LINK
        PAGE_ID="Participant_resolveRoleHome"
        SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="medExpList$concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="medExpList$concernRoleType"
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
          PROPERTY="medExpList$rshipToHOH"
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
          PROPERTY="medExpList$expenseTypeCode"
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
          PROPERTY="medExpList$currMthlyExpenseAmount"
        />
      </CONNECT>
    </FIELD>


  </LIST>


  <!--Blank rows to prvide space between lists -->
  <CLUSTER SHOW_LABELS="false">


    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="TEXT"
          PROPERTY="Field.StaticText.Space"
        />
      </CONNECT>
    </FIELD>


    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="TEXT"
          PROPERTY="Field.StaticText.Space"
        />
      </CONNECT>
    </FIELD>


    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="TEXT"
          PROPERTY="Field.StaticText.Space"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <LIST TITLE="List.Title.ShelterExpenses">
    <!-- An action set containing the single new button. -->
    <ACTION_SET BOTTOM="false">
      <ACTION_CONTROL
        IMAGE="NewButton"
        LABEL="ActionControl.Label.NewShelterExpense"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="IncomeScreening_createShelterExpenseEvidence"
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
    </ACTION_SET>


    <CONTAINER
      LABEL="Container.Label.Action"
      SEPARATOR="Container.Separator"
      WIDTH="15"
    >
      <ACTION_CONTROL LABEL="ActionControl.Label.View">
        <LINK PAGE_ID="IncomeScreening_viewShelterExpenseEvidence">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="shelterExpList$fsHouseholdExpensesID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="householdExpensesID"
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
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="IncomeScreening_modifyShelterExpenseEvidenceFromList"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="shelterExpList$fsHouseholdExpensesID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="householdExpensesID"
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
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


    <FIELD
      LABEL="Field.Label.Name"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="shelterExpList$concernRoleName"
        />
      </CONNECT>
      <LINK
        PAGE_ID="Participant_resolveRoleHome"
        SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="shelterExpList$concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="shelterExpList$concernRoleType"
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
          PROPERTY="shelterExpList$rshipToHOH"
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
          PROPERTY="shelterExpList$expenseTypeCode"
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
          PROPERTY="shelterExpList$currMthlyExpenseAmount"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
