<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2021. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Description                                                                -->
<!-- ===========                                                                -->
<!-- This page allows users to search for an Application Case.                  -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

   <CLUSTER LABEL_WIDTH="32" NUM_COLS="2" SHOW_LABELS="true" TITLE="Cluster.Title.SearchCriteria">
      <FIELD LABEL="Field.Label.CaseReference">
        <CONNECT>
          <TARGET NAME="ACTION" PROPERTY="details$caseReference" />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.ApplicationReference">
        <CONNECT>
          <TARGET NAME="ACTION" PROPERTY="details$applicationReference" />
        </CONNECT>
      </FIELD>
    </CLUSTER>
    
    <CLUSTER LABEL_WIDTH="32" NUM_COLS="2" SHOW_LABELS="true">
      <FIELD CONTROL="CHECKBOXED_LIST" HEIGHT="4" LABEL="Field.Label.Type"
        USE_BLANK="false">
        <CONNECT>
          <INITIAL NAME="DISPLAY" PROPERTY="typeDtls$dtls$caseName"
            HIDDEN_PROPERTY="applicationCaseAdminID" />
        </CONNECT>
        <CONNECT>
          <TARGET NAME="ACTION" PROPERTY="details$caseTypeList" />
        </CONNECT>
      </FIELD>
      <FIELD CONTROL="CHECKBOXED_LIST" HEIGHT="4" LABEL="Field.Label.Status"
        USE_BLANK="false">
        <CONNECT>
          <INITIAL HIDDEN_PROPERTY="caseStatus" NAME="DISPLAY"
            PROPERTY="caseStatusDescription" />
        </CONNECT>
        <CONNECT>
          <TARGET NAME="ACTION" PROPERTY="details$statusList" />
        </CONNECT>
      </FIELD>
    </CLUSTER>

    <CLUSTER LABEL_WIDTH="32" NUM_COLS="2" SHOW_LABELS="true">
      <FIELD LABEL="Field.Label.ApplicationReceivedFrom"
        USE_DEFAULT="false">
        <CONNECT>
          <TARGET NAME="ACTION" PROPERTY="details$applicationDate" />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.Client">
        <CONNECT>
          <TARGET NAME="ACTION" PROPERTY="details$concernRoleID" />
        </CONNECT>
      </FIELD>
    </CLUSTER>
 

  <LIST TITLE="List.Title.SearchResult">
    <CONTAINER
      LABEL="Container.Label.Action"
      WIDTH="10"
    >
      <ACTION_CONTROL
        LABEL="ActionControl.Label.Select"
        TYPE="DISMISS"
      >
        <LINK>
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$dtls$caseReference"/>
            <TARGET NAME="PAGE" PROPERTY="value"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$dtls$caseReference"/>
            <TARGET NAME="PAGE" PROPERTY="description"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>

    <FIELD LABEL="Field.Label.CaseReferenceList" WIDTH="14">
      <CONNECT>
        <SOURCE NAME="ACTION" PROPERTY="result$dtls$caseReference" />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ApplicationReferenceList" WIDTH="13">
      <CONNECT>
        <SOURCE NAME="ACTION" PROPERTY="result$dtls$applicationReference" />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Type" WIDTH="24">
      <CONNECT>
        <SOURCE NAME="ACTION" PROPERTY="caseType" />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Programs" WIDTH="23">
      <CONNECT>
        <SOURCE NAME="ACTION" PROPERTY="casePrograms" />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Clients" WIDTH="24">
      <CONNECT>
        <SOURCE NAME="ACTION" PROPERTY="caseClients" />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Status" WIDTH="15">
      <CONNECT>
        <SOURCE NAME="ACTION" PROPERTY="status" />
      </CONNECT>
    </FIELD>
  </LIST>

</VIEW>
