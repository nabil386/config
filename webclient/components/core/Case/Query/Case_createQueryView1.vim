<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012-2017. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010 - 2011 Curam Software Ltd.                              -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows users to create a case query.                         -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="CaseQuery"
    NAME="DISPLAY"
    OPERATION="getInitialSearchCriteria"
  />


  <!-- BEGIN, CR00276230, ELG -->
  <SERVER_INTERFACE
    ACTION_ID_PROPERTY="details$actionIDProperty"
    CLASS="CaseQuery"
    NAME="ACTION"
    OPERATION="createOrRunWithMessages"
    PHASE="ACTION"
  />


  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="ACTION"
        PROPERTY="informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>
  <!-- END, CR00276230 -->


  <CLUSTER LABEL_WIDTH="24.5">
    <FIELD
      LABEL="Field.Label.QueryName"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$dtls$queryDetails$queryName"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="24.5"
    NUM_COLS="1"
    TITLE="Cluster.Title.QueryCriteria"
  >


    <FIELD
      HEIGHT="3"
      LABEL="Field.Label.CriteriaType"
      USE_BLANK="true"
      WIDTH="50"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="categoryType"
          NAME="DISPLAY"
          PROPERTY="typeDescription"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="categoryTypeList"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      HEIGHT="3"
      LABEL="Field.Label.Owner"
      USE_BLANK="true"
      WIDTH="50"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="ownerType"
          NAME="DISPLAY"
          PROPERTY="ownerDescription"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="ownerList"
        />
      </CONNECT>
    </FIELD>


    <CLUSTER
      LABEL_WIDTH="49"
      NUM_COLS="2"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >
      <FIELD
        HEIGHT="3"
        LABEL="Field.Label.FilterOption"
        USE_BLANK="true"
      >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="filterOption"
            NAME="DISPLAY"
            PROPERTY="filterOptionDescription"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="filterOptionList"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        HEIGHT="3"
        LABEL="Field.Label.Status"
        USE_BLANK="true"
      >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="statusCode"
            NAME="DISPLAY"
            PROPERTY="statusDescription"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="statusList"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


    <CLUSTER
      DESCRIPTION="Cluster.Description.DateRange"
      LABEL_WIDTH="49"
      NUM_COLS="2"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >
      <FIELD
        LABEL="Field.Label.StartDateFrom"
        USE_DEFAULT="false"
        WIDTH="40"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="startDateFrom"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.EndDateFrom"
        USE_DEFAULT="false"
        WIDTH="40"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="endDateFrom"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.StartDateTo"
        USE_DEFAULT="false"
        WIDTH="40"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="startDateTo"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.EndDateTo"
        USE_DEFAULT="false"
        WIDTH="40"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="endDateTo"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="24.5"
    NUM_COLS="1"
  >


    <!-- BEGIN, CR00358330, DJ -->
    <CLUSTER
      LABEL_WIDTH="24.5"
      NUM_COLS="1"
      SHOW_LABELS="true"
    >


      <CONTAINER
        ALIGNMENT="LEFT"
        LABEL="Field.Label.ClientReferenceType"
      >
        <FIELD
          USE_BLANK="false"
          USE_DEFAULT="true"
          WIDTH="45"
        >
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="alternateIDType"
            />
          </CONNECT>
        </FIELD>


        <FIELD WIDTH="30">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="alternateID"
            />
          </CONNECT>
        </FIELD>


      </CONTAINER>


    </CLUSTER>
    <!-- END, CR00358330 -->


    <CLUSTER
      LABEL_WIDTH="49"
      NUM_COLS="2"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="indexCaseSearchEnabledInd"
        />
      </CONDITION>


      <FIELD LABEL="Field.Label.ClientFirstName">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="clientFirstName"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.ClientSurname">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="clientSurname"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


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
        IMAGE="ResetButton"
        LABEL="ActionControl.Label.Reset"
      >
        <LINK
          PAGE_ID="Case_createQuery"
          SAVE_LINK="false"
        />
      </ACTION_CONTROL>
    </ACTION_SET>


  </CLUSTER>


  <LIST TITLE="List.Title.QueryResult">

	<CONDITION>
	  <IS_FALSE
	    NAME="ACTION"
	    PROPERTY="displayClientsOpt"
	  />
	</CONDITION>
    <FIELD
      LABEL="Field.Label.CaseReference"
      WIDTH="18"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$runResult$list$searchDtls$caseReference"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="18"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$runResult$list$searchDtls$caseCatTypeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.PrimaryClient"
      WIDTH="22"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$runResult$list$searchDtls$concernRoleName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.LatestTransaction"
      WIDTH="27"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$runResult$list$searchDtls$transactionDescription"
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
          PROPERTY="result$runResult$list$searchDtls$statusCode"
        />
      </CONNECT>
    </FIELD>


  </LIST>
 <LIST TITLE="List.Title.QueryResult">

	<CONDITION>
	  <IS_TRUE
	    NAME="ACTION"
	    PROPERTY="displayClientsOpt"
	  />
	</CONDITION>
    <FIELD
      LABEL="Field.Label.CaseReference"
      WIDTH="18"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$runResult$list$searchDtls$caseReference"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="18"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$runResult$list$searchDtls$caseCatTypeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Clients"
      WIDTH="22"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$runResult$list$searchDtls$caseClientsOpt"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.LatestTransaction"
      WIDTH="27"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$runResult$list$searchDtls$transactionDescription"
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
          PROPERTY="result$runResult$list$searchDtls$statusCode"
        />
      </CONNECT>
    </FIELD>


  </LIST>

</VIEW>
