<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2005, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2005, 2007-2008, 2010-2011 Curam Software Ltd.           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Create new Sporting Activity Expense evidence.                         -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="SampleMaintainSportingActivity"
    NAME="DISPLAY1"
    OPERATION="listSampleSportingActivityEvidence"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="SampleMaintainSportingActivityExpense"
    NAME="ACTION"
    OPERATION="createSampleSportingActivityExpense"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="parEvType"/>
  <PAGE_PARAMETER NAME="contextDescription"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY1"
      PROPERTY="caseID"
    />
  </CONNECT>


  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="ACTION"
        PROPERTY="result$warnings$dtls$msg"
      />
    </CONNECT>
  </INFORMATIONAL>


  <CLUSTER LABEL_WIDTH="25">


    <INCLUDE FILE_NAME="Evidence_createHeader.vim"/>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
  >


    <FIELD
      LABEL="Field.Label.Amount"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="amount"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.StartDate"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.SportingExpenseType">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="sportingExpenseType"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.EndDate"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >


    <!-- BEGIN, CR00406866, VT -->
    <FIELD HEIGHT="4" LABEL="Field.Label.Comments">
      <!-- END, CR00406866 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER>


    <LIST>
      <CONTAINER WIDTH="5">
        <WIDGET TYPE="SINGLESELECT">
          <WIDGET_PARAMETER NAME="SELECT_SOURCE">
            <CONNECT>
              <SOURCE
                NAME="DISPLAY1"
                PROPERTY="newAndUpdateList$dtls$evidenceID"
              />
            </CONNECT>
          </WIDGET_PARAMETER>
          <WIDGET_PARAMETER NAME="SELECT_TARGET">
            <CONNECT>
              <TARGET
                NAME="ACTION"
                PROPERTY="sportingActivityID"
              />
            </CONNECT>
          </WIDGET_PARAMETER>
        </WIDGET>
      </CONTAINER>
      <FIELD
        LABEL="List.Title.Participant"
        WIDTH="15"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY1"
            PROPERTY="newAndUpdateList$dtls$concernRoleName"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="List.Title.Description"
        WIDTH="40"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY1"
            PROPERTY="newAndUpdateList$dtls$summary"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="List.Title.StartDate"
        WIDTH="20"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY1"
            PROPERTY="newAndUpdateList$dtls$startDate"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="List.Title.EndDate"
        WIDTH="20"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY1"
            PROPERTY="newAndUpdateList$dtls$endDate"
          />
        </CONNECT>
      </FIELD>
    </LIST>
  </CLUSTER>


</VIEW>
