<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2005, 2009-2011 Curam Software Ltd.                          -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows you to display a list of appeals for a standalone     -->
<!-- product delivery.                                                      -->
<?curam-deprecated Since Curam 6.0 SP2, replaced by Appeal_listAppealCaseCombinedListView.vim. See release note: CR00284874?>
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
    OPERATION="listAppealByCase"
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
      <!-- BEGIN, CR00236983, GP -->
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Appeal_resolveHearingCaseAppealLevel"
      >
        <!-- END, CR00236983 -->


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


      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      IMAGE="CreateHearingReviewButton"
      LABEL="ActionControl.Label.CreateHearingReview"
    >
      <!-- BEGIN, CR00247113, GP -->
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Appeal_resolveHearingReviewAppealLevel"
      >
        <!-- END, CR00247113 -->


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


      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      IMAGE="CreateJudicialReviewButton"
      LABEL="ActionControl.Label.CreateJudicialReview"
    >
      <!-- BEGIN, CR00248571, GP -->
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Appeal_resolveJudicialReviewAppealLevel"
      >
        <!-- END, CR00248571 -->


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
  </LIST>


</VIEW>
