<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012, 2013. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010 Curam Software Ltd.                                     -->
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
    NAME="DISPLAYCRITERIA"
    OPERATION="getInitialSearchCriteria"
  />


  <SERVER_INTERFACE
    CLASS="CaseQuery"
    NAME="DISPLAY"
    OPERATION="read"
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


  <CLUSTER
    NUM_COLS="1"
    TITLE="Cluster.Title.QueryCriteria"
  >


    <CLUSTER
      LABEL_WIDTH="29"
      NUM_COLS="2"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        HEIGHT="3"
        LABEL="Field.Label.CriteriaType"
        USE_BLANK="false"
      >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="categoryType"
            NAME="DISPLAYCRITERIA"
            PROPERTY="typeDescription"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="categoryTypeList"
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
        LABEL="Field.Label.Status"
        USE_BLANK="false"
      >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="statusCode"
            NAME="DISPLAYCRITERIA"
            PROPERTY="statusDescription"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="statusList"
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
      LABEL_WIDTH="50"
      NUM_COLS="2"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <CLUSTER
        LABEL_WIDTH="50"
        NUM_COLS="2"
        SHOW_LABELS="true"
        STYLE="cluster-cpr-no-border"
      >


        <FIELD
          LABEL="Field.Label.StartDateFrom"
          USE_DEFAULT="false"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="startDateFrom"
            />
          </CONNECT>
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="startDateFrom"
            />
          </CONNECT>
        </FIELD>


        <FIELD
          LABEL="Field.Label.StartDateTo"
          USE_DEFAULT="false"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="startDateTo"
            />
          </CONNECT>
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="startDateTo"
            />
          </CONNECT>
        </FIELD>


      </CLUSTER>


      <CLUSTER
        LABEL_WIDTH="50"
        NUM_COLS="2"
        SHOW_LABELS="true"
        STYLE="cluster-cpr-no-border"
      >


        <FIELD
          LABEL="Field.Label.EndDateFrom"
          USE_DEFAULT="false"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="endDateFrom"
            />
          </CONNECT>
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="endDateFrom"
            />
          </CONNECT>
        </FIELD>


        <FIELD
          LABEL="Field.Label.EndDateTo"
          USE_DEFAULT="false"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="endDateTo"
            />
          </CONNECT>
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
      LABEL_WIDTH="29"
      NUM_COLS="2"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        HEIGHT="3"
        LABEL="Field.Label.FilterOption"
        USE_BLANK="false"
      >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="filterOption"
            NAME="DISPLAYCRITERIA"
            PROPERTY="filterOptionDescription"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="filterOptionList"
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
        LABEL="Field.Label.Owner"
        USE_BLANK="false"
      >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="ownerType"
            NAME="DISPLAYCRITERIA"
            PROPERTY="ownerDescription"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="ownerList"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="ownerList"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


  </CLUSTER>


  <CLUSTER
    NUM_COLS="1"
    TITLE="Cluster.Title.Client"
  >


    <!-- BEGIN, CR00358330, DJ -->
    <CLUSTER
      LABEL_WIDTH="29"
      NUM_COLS="2"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        LABEL="Field.Label.ClientReferenceType"
        USE_BLANK="false"
        USE_DEFAULT="true"
        WIDTH="60"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="alternateIDType"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="alternateIDType"
          />
        </CONNECT>
      </FIELD>


      <CLUSTER
        LABEL_WIDTH="26.5"
        NUM_COLS="1"
        SHOW_LABELS="true"
        STYLE="cluster-cpr-no-border"
      >


        <FIELD
          LABEL="Field.Label.ClientReferenceNumber"
          WIDTH="46"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="alternateID"
            />
          </CONNECT>
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="alternateID"
            />
          </CONNECT>
        </FIELD>


      </CLUSTER>
      <!-- END, CR00358330 -->


    </CLUSTER>


    <CLUSTER
      LABEL_WIDTH="29"
      NUM_COLS="2"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAYCRITERIA"
          PROPERTY="indexCaseSearchEnabledInd"
        />
      </CONDITION>


      <FIELD LABEL="Field.Label.ClientFirstName">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="clientFirstName"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="clientFirstName"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.ClientSurname">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="clientSurname"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="clientSurname"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


  </CLUSTER>


</VIEW>
