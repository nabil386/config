<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008, 2009 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows users to create a case query.                         -->
<!-- BEGIN, CR00238896, GD -->
<?curam-deprecated Since Curam 6.0, replaced by Case_createQueryView1. 
See CR00202673 ?>
<!-- END, CR00238896 -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    ACTION_ID_PROPERTY="details$actionIDProperty"
    CLASS="Query"
    NAME="ACTION"
    OPERATION="createModifyOrRunCaseQuery"
    PHASE="ACTION"
  />


  <SERVER_INTERFACE
    CLASS="Case"
    NAME="DISPLAY"
    OPERATION="checkForAppealsInstallation"
  />


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
    TITLE="Cluster.Title.QueryName"
  >
    <FIELD
      LABEL="Field.Label.QueryName"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="queryName"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    DESCRIPTION="Cluster.Description.QueryCriteria"
    NUM_COLS="1"
    TITLE="Cluster.Title.QueryCriteria"
  >


    <ACTION_SET
      ALIGNMENT="CENTER"
      TOP="false"
    >
      <ACTION_CONTROL
        ACTION_ID="RUN_QUERY"
        DEFAULT="true"
        LABEL="ActionControl.Label.Run"
        TYPE="SUBMIT"
      >
        <LINK PAGE_ID="THIS"/>
      </ACTION_CONTROL>


      <ACTION_CONTROL
        ACTION_ID="SAVE_QUERY"
        LABEL="ActionControl.Label.Save"
        TYPE="SUBMIT"
      >
        <LINK PAGE_ID="Case_queryList"/>
      </ACTION_CONTROL>


      <ACTION_CONTROL
        IMAGE="ResetButton"
        LABEL="ActionControl.Label.Reset"
      >
        <LINK
          PAGE_ID="Case_createQueryFromList"
          SAVE_LINK="false"
        />
      </ACTION_CONTROL>


    </ACTION_SET>


    <CLUSTER
      LABEL_WIDTH="35"
      NUM_COLS="2"
    >
      <FIELD
        LABEL="Field.Label.ClientName"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="details$searchCriteria$concernRoleID"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.ClientReference"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="details$searchCriteria$primaryAlternateID"
          />
        </CONNECT>
      </FIELD>


      <CONTAINER LABEL="Field.Label.CategoryType">
        <FIELD
          USE_BLANK="true"
          USE_DEFAULT="false"
        >
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="category"
            />
          </CONNECT>
        </FIELD>
        <FIELD>
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="type"
            />
          </CONNECT>
        </FIELD>
      </CONTAINER>


      <FIELD
        LABEL="Field.Label.Status"
        USE_BLANK="true"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="status"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


    <CLUSTER
      LABEL_WIDTH="75"
      NUM_COLS="4"
    >


      <FIELD
        LABEL="Field.Label.DisplayCasesWithServicePlans"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="searchWithServicePlans"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.DisplayCasesWithIssues"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="searchWithIssues"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.DisplayCasesWithInvestigations"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="searchWithInvestigations"
          />
        </CONNECT>
      </FIELD>


      <CLUSTER STYLE="cluster-cpr-no-border">
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="result$appealsInstalledInd"
          />
        </CONDITION>
        <FIELD
          LABEL="Field.Label.DisplayCasesWithAppeals"
          USE_DEFAULT="false"
        >
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="searchWithAppeals"
            />
          </CONNECT>
        </FIELD>
      </CLUSTER>


    </CLUSTER>


    <CLUSTER
      DESCRIPTION="Cluster.Description.DateRange"
      LABEL_WIDTH="35"
      NUM_COLS="2"
    >
      <FIELD
        LABEL="Field.Label.FromDate"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="details$searchCriteria$startDate"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.LastNumberofDays">
        USE_DEFAULT="false"      
      &gt;
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="lastNumberOfDays"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.ToDate"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="endDate"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.LastNumberofWeeks">
        USE_DEFAULT="false"      
      &gt;
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="lastNumberOfWeeks"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


  </CLUSTER>


  <LIST TITLE="List.Title.QueryResult">


    <FIELD LABEL="Field.Label.CaseReference">
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$list$searchDtls$caseReference"
        />
      </CONNECT>
      <LINK PAGE_ID="Case_resolveCaseHome">
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Category"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="caseTypeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="caseCatTypeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.PrimaryClient">
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="concernRoleName"
        />
      </CONNECT>
      <!--BEGIN, CR00139966, MC-->
      <LINK PAGE_ID="Participant_resolve">
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="searchDtls$concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
      </LINK>
      <!--END, CR00139966-->
    </FIELD>


    <FIELD
      LABEL="Field.Label.StartDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$list$searchDtls$startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="statusCode"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
