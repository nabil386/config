<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

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
<!-- This page displays search criteria for an investigation.               -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="InvestigationDelivery"
    NAME="DISPLAY"
    OPERATION="getInvestigationSearchCriteria"
  />


  <!-- BEGIN, CR00276421, ELG -->
  <SERVER_INTERFACE
    CLASS="InvestigationDelivery"
    NAME="ACTION"
    OPERATION="investigationSearch1"
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
  <!-- END, CR00276421 -->

  <CLUSTER
    LABEL_WIDTH="24"
    NUM_COLS="1"
    TITLE="Cluster.Title.SearchCriteria"
  >


    <CLUSTER
      LABEL_WIDTH="24"
      NUM_COLS="2"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        LABEL="Field.Label.CaseReference"
        USE_DEFAULT="false"
        WIDTH="26"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="key$caseReference"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>
    
      <CLUSTER
        LABEL_WIDTH="36"
        NUM_COLS="3"
        SHOW_LABELS="true"
        STYLE="cluster-cpr-no-border"
      >


        <FIELD
          LABEL="Field.Label.ClientReferenceType"
          USE_BLANK="false"
          USE_DEFAULT="true"
        >
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="key$alternateIDType"
            />
          </CONNECT>
        </FIELD>


        <CLUSTER
          NUM_COLS="1"
          SHOW_LABELS="false"
          STYLE="cluster-cpr-no-border"
        >


          <FIELD WIDTH="26">
            <CONNECT>
              <TARGET
                NAME="ACTION"
                PROPERTY="key$alternateID"
              />
            </CONNECT>
          </FIELD>
        </CLUSTER>
      </CLUSTER>


      <CLUSTER
        LABEL_WIDTH="24"
        NUM_COLS="2"
        SHOW_LABELS="true"
      >
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="indexInvestigationSearchEnabledInd"
          />
        </CONDITION>


        <FIELD LABEL="Field.Label.ClientFirstName">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="key$clientFirstName"
            />
          </CONNECT>
        </FIELD>


        <FIELD LABEL="Field.Label.ClientSurname">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="key$clientSurname"
            />
          </CONNECT>
        </FIELD>
      </CLUSTER>
      
      
      <CLUSTER
        NUM_COLS="3"
        SHOW_LABELS="false"
        TITLE="Cluster.Title.Filter"
        STYLE="cluster-cpr-no-border"
      >
        <CLUSTER
          TITLE="Cluster.Title.Type"
          SHOW_LABELS="false"
        >
          <FIELD
            HEIGHT="5"
            USE_BLANK="false"
            CONTROL="CHECKBOXED_LIST"
            WIDTH="100"
          >
            <CONNECT>
              <INITIAL
                HIDDEN_PROPERTY="investigationType"
                NAME="DISPLAY"
                PROPERTY="typeDescription"
              />
            </CONNECT>
            <CONNECT>
              <TARGET
                NAME="ACTION"
                PROPERTY="typeList"
              />
            </CONNECT>
          </FIELD>
        </CLUSTER>
        
        <CLUSTER
          TITLE="Cluster.Title.Subtype"
          SHOW_LABELS="false"
        >
          <FIELD
            HEIGHT="5"
            USE_BLANK="false"
            CONTROL="CHECKBOXED_LIST"
            WIDTH="100"
          >
            <CONNECT>
              <INITIAL
                HIDDEN_PROPERTY="subtype"
                NAME="DISPLAY"
                PROPERTY="subtypeDescription"
              />
            </CONNECT>
            <CONNECT>
              <TARGET
                NAME="ACTION"
                PROPERTY="subtypeList"
              />
            </CONNECT>
          </FIELD>
        </CLUSTER>
        
        <CLUSTER
          TITLE="Cluster.Title.Status"
          SHOW_LABELS="false"
        >
          <FIELD
            HEIGHT="5"
            USE_BLANK="false"
            CONTROL="CHECKBOXED_LIST"
            WIDTH="100"
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
      </CLUSTER>
  
  
      <CLUSTER
        LABEL_WIDTH="48"
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
              <TARGET
                NAME="ACTION"
                PROPERTY="key$startDateFrom"
              />
            </CONNECT>
          </FIELD>


          <FIELD
            LABEL="Field.Label.StartDateTo"
            USE_DEFAULT="false"
          >
            <CONNECT>
              <TARGET
                NAME="ACTION"
                PROPERTY="key$startDateTo"
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
              <TARGET
                NAME="ACTION"
                PROPERTY="key$endDateFrom"
              />
            </CONNECT>
          </FIELD>


          <FIELD
            LABEL="Field.Label.EndDateTo"
            USE_DEFAULT="false"
          >
            <CONNECT>
              <TARGET
                NAME="ACTION"
                PROPERTY="key$endDateTo"
              />
            </CONNECT>
          </FIELD>
        </CLUSTER>


      </CLUSTER>

  </CLUSTER>


</VIEW>
