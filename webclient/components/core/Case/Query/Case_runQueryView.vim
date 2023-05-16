<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008, 2011 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page displays results of a stored case query.                     -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <!-- BEGIN, CR00276230, ELG -->
  <SERVER_INTERFACE
    CLASS="CaseQuery"
    NAME="DISPLAY"
    OPERATION="listAndRunWithMessages"
  />
  <!-- END, CR00276230 -->


  <SERVER_INTERFACE
    CLASS="Case"
    NAME="DISPLAY2"
    OPERATION="checkForAppealsInstallation"
  />


  <PAGE_PARAMETER NAME="queryID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="queryID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$queryID"
    />
  </CONNECT>


  <!-- BEGIN, CR00276230, ELG -->
  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>
  <!-- END, CR00276230 -->


  <CLUSTER
    BEHAVIOR="NONE"
    LABEL_WIDTH="20"
    NUM_COLS="1"
    TITLE="Cluster.Title.QueryName"
  >
    <FIELD
      LABEL="Field.Label.MyQueryName"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="queryName"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    NUM_COLS="1"
    TITLE="Cluster.Title.SearchCriteria"
  >
    <CLUSTER
      LABEL_WIDTH="35"
      NUM_COLS="2"
    >


      <FIELD LABEL="Field.Label.ClientName">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="concernRoleName"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.ClientReference"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="primaryAlternateID"
          />
        </CONNECT>
      </FIELD>


      <CONTAINER LABEL="Field.Label.CategoryType">
        <FIELD
          USE_BLANK="true"
          USE_DEFAULT="false"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="productTypeCode"
            />
          </CONNECT>
        </FIELD>
        <FIELD>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="caseCatTypeCode"
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
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="statusCode"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


    <!--   <CLUSTER
      LABEL_WIDTH="75"
      NUM_COLS="4"
    >


      <FIELD
        LABEL="Field.Label.DisplayCasesWithServicePlans"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="searchWithServicePlans"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.DisplayCasesWithIssues"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="searchWithIssues"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.DisplayCasesWithInvestigations"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="searchWithInvestigations"
          />
        </CONNECT>
      </FIELD>


      <CLUSTER STYLE="cluster-cpr-no-border">
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY2"
            PROPERTY="result$appealsInstalledInd"
          />
        </CONDITION>
        <FIELD
          LABEL="Field.Label.DisplayCasesWithAppeals"
          USE_DEFAULT="false"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="searchWithAppeals"
            />
          </CONNECT>
        </FIELD>
      </CLUSTER>


    </CLUSTER>-->


    <CLUSTER
      LABEL_WIDTH="35"
      NUM_COLS="2"
    >
      <FIELD
        LABEL="Field.Label.FromDate"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="startDate"
          />
        </CONNECT>
      </FIELD>


      <!-- <FIELD LABEL="Field.Label.LastNumberofDays">
        USE_DEFAULT="false"      
      &gt;
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="lastNumberOfDays"
          />
        </CONNECT>
      </FIELD>-->


      <!-- <FIELD
        LABEL="Field.Label.ToDate"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="endDate"
          />
        </CONNECT>
      </FIELD>-->


      <!--<FIELD LABEL="Field.Label.LastNumberofWeeks">
        USE_DEFAULT="false"      
      &gt;
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="lastNumberOfWeeks"
          />
        </CONNECT>
      </FIELD>-->


    </CLUSTER>


  </CLUSTER>


</VIEW>
