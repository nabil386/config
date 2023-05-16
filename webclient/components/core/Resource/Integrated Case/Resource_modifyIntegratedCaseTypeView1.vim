<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
   PID 5725-H26

   Copyright IBM Corporation 2009, 2014. All rights reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2009-2010, 2012 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to modify an integrated case type.           -->
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
    CLASS="Organization"
    NAME="DISPLAY"
    OPERATION="readAdminIntegratedCase1"
  />


  <!-- BEGIN, CR00440782, SP -->
  <SERVER_INTERFACE
    CLASS="Organization"
    NAME="ACTION"
    OPERATION="modifyAdminIntegratedCaseIndicators"
    PHASE="ACTION"
  />
  <!-- END, CR00440782 -->


  <PAGE_PARAMETER NAME="adminIntegratedCaseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="adminIntegratedCaseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$adminIntegratedCaseID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="adminIntegratedCaseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="adminIntegratedCaseID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>


  <CLUSTER LABEL_WIDTH="50">


    <FIELD LABEL="Field.Title.Type">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="integratedCaseType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="integratedCaseType"
        />
      </CONNECT>
    </FIELD>


    <!--BEGIN, CR00205591, PB-->
    <FIELD LABEL="Field.Title.TranslationRequired">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="adminTranslationRequiredInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="adminTranslationRequiredInd"
        />
      </CONNECT>
    </FIELD>
    <!--END, CR00205591-->


    <FIELD LABEL="Field.Title.ProgramList">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="programListInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="programListInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.CitizenSelfService">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="citizenWorkspaceInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="citizenWorkspaceInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Title.MyCasesFilter">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="myCasesFilterInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="myCasesFilterInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Title.CaseSearchFilter">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseSearchFilterInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="caseSearchFilterInd"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00283741, KOH -->
    <FIELD LABEL="Field.Label.Appealable">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="appealableIndOpt"
        />
      </CONNECT>
      <!-- BEGIN, CR00440782, SP -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="notAppealableInd"
        />
      </CONNECT>
      <!-- END, CR00440782 -->
    </FIELD>
    <!-- END, CR00283741 -->
    <!-- BEGIN, CR00305740, PB -->
    <FIELD
      LABEL="Field.Title.ContactLog"
      WIDTH="95"
    >
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="result$maintainAdminIntegratedCaseDetails$contactLogInd"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$maintainAdminIntegratedCaseDetails$contactLogInd"
        />
      </CONNECT>
      <!-- BEGIN, CR00440782, SP -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="contactLogInd"
        />
      </CONNECT>
      <!-- END, CR00440782 -->
    </FIELD>
    <!-- END, CR00305740 -->
    <!-- BEGIN, CR00205193, SS -->
    <FIELD
      LABEL="Field.Title.OwnershipStrategy"
      WIDTH="25"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="ownershipStrategyName"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="ownershipStrategyName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="ownershipStrategyName"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00205193 -->


  </CLUSTER>


</VIEW>
