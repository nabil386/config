<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2005, 2009-2010 Curam Software Ltd.                          -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows you to display a list of appeals for a product        -->
<!-- delivery on an integrated case.                                        -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.Title"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="Appeal"
    NAME="DISPLAY"
    OPERATION="listAppealByCaseForIC"
  />


  <MENU>
    <ACTION_CONTROL LABEL="ActionControl.Label.NewTask">
      <LINK PAGE_ID="ICProductSample_createTask">
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
            PROPERTY="contextDescription"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.NewActivity">
      <LINK PAGE_ID="ICProductSample_createActivity">
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
            PROPERTY="contextDescription"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </MENU>


  <ACTION_SET BOTTOM="false">
    <ACTION_CONTROL
      IMAGE="CreateHearingCaseButton"
      LABEL="ActionControl.Label.CreateHearingCase"
    >
      <LINK PAGE_ID="Appeal_resolveHearingCaseAppealLevelIC">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="implCaseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="parentCaseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="parentCaseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="contextDescription"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="contextDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
    <ACTION_CONTROL
      IMAGE="CreateHearingReviewButton"
      LABEL="ActionControl.Label.CreateHearingReview"
    >
      <LINK PAGE_ID="Appeal_resolveHearingReviewAppealLevelIC">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="implCaseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="parentCaseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="parentCaseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="contextDescription"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="contextDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
    <ACTION_CONTROL
      IMAGE="CreateJudicialReviewButton"
      LABEL="ActionControl.Label.CreateJudicialReview"
    >
      <LINK PAGE_ID="Appeal_resolveJudicialReviewAppealLevelIC">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="implCaseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="parentCaseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="parentCaseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="contextDescription"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="contextDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>


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


    <FIELD
      LABEL="Field.Label.Reference"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="appealCaseReference"
        />
      </CONNECT>


      <LINK PAGE_ID="Case_resolveCaseHome">


        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="appealCaseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="appealTypeCode"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="appealType"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="parentCaseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="parentCaseID"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="appealTypeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Appellant"
      WIDTH="20"
    >


      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="appellantName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Owner"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="ownerFullName"
        />
      </CONNECT>
      <!-- BEGIN, CR00200487, GP -->
      <LINK PAGE_ID="Organization_viewUserDetails">
        <!-- END, CR00200487 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="ownerUserName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="statusCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Resolution"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="resolutionCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Deadline"
      WIDTH="10"
    >


      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="deadlineDate"
        />
      </CONNECT>
    </FIELD>
  </LIST>


  <!--BEGIN, CR00021291, RKi-->
  <LIST>
    <FIELD WIDTH="10">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="mulAppealCaseReference"
        />
      </CONNECT>


      <LINK PAGE_ID="Case_resolveCaseHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="mulAppealCaseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="mulAppealTypeCode"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="appealType"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="parentCaseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="parentCaseID"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <FIELD WIDTH="15">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="mulAppealTypeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD WIDTH="20">
      <CONNECT>
        <SOURCE
          NAME="TEXT"
          PROPERTY="Field.Label.MultipleAppellants"
        />
      </CONNECT>
      <LINK PAGE_ID="Appeal_listAppellantsForIC">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="mulAppealCaseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <FIELD WIDTH="20">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="mulOwnerFullName"
        />
      </CONNECT>
      <!-- BEGIN, CR00200487, GP -->
      <LINK PAGE_ID="Organization_viewUserDetails">
        <!-- END, CR00200487 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="mulOwnerUserName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD WIDTH="10">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="mulStatusCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD WIDTH="15">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="mulResolutionCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD WIDTH="10">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="mulDeadlineDate"
        />
      </CONNECT>
    </FIELD>


  </LIST>
  <!-- END, CR00021291 -->


</VIEW>
