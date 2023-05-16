<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view to display a list of appeals for a product delivery  -->
<!-- on an integrated case.                                                 -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


  <LIST TITLE="List.SearchResults.Title">


    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="Field.Label.Suspend" TYPE="ACTION">
        <LINK OPEN_MODAL="true" PAGE_ID="WorkflowAdministration_suspendProcessInstance" WINDOW_OPTIONS="width=400">
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$piSearchDetails$processInstanceID"/>
            <TARGET NAME="PAGE" PROPERTY="processInstanceID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$piSearchDetails$processName"/>
            <TARGET NAME="PAGE" PROPERTY="processName"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="Field.Label.Resume" TYPE="ACTION">
        <LINK OPEN_MODAL="true" PAGE_ID="WorkflowAdministration_resumeProcessInstance" WINDOW_OPTIONS="width=400">
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$piSearchDetails$processInstanceID"/>
            <TARGET NAME="PAGE" PROPERTY="processInstanceID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$piSearchDetails$processName"/>
            <TARGET NAME="PAGE" PROPERTY="processName"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="Field.Label.Abort" TYPE="ACTION">
        <LINK OPEN_MODAL="true" PAGE_ID="WorkflowAdministration_abortProcessInstance" WINDOW_OPTIONS="width=400">
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$piSearchDetails$processInstanceID"/>
            <TARGET NAME="PAGE" PROPERTY="processInstanceID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$piSearchDetails$processName"/>
            <TARGET NAME="PAGE" PROPERTY="processName"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <FIELD LABEL="Field.Label.ProcessDisplayName" WIDTH="40">
      <CONNECT>
        <SOURCE NAME="ACTION" PROPERTY="result$piSearchDetails$processDisplayName"/>
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ProcessInstanceID" WIDTH="20">
      <CONNECT>
        <SOURCE NAME="ACTION" PROPERTY="result$piSearchDetails$processInstanceID"/>
      </CONNECT>
      <LINK PAGE_ID="WorkflowAdministration_viewProcessInstanceByProcessDetails">
        <CONNECT>
          <SOURCE NAME="ACTION" PROPERTY="result$piSearchDetails$processInstanceID"/>
          <TARGET NAME="PAGE" PROPERTY="processInstanceID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="ACTION" PROPERTY="result$piSearchDetails$processName"/>
          <TARGET NAME="PAGE" PROPERTY="processName"/>
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD LABEL="Field.Label.Status" WIDTH="15">
      <CONNECT>
        <SOURCE NAME="ACTION" PROPERTY="result$piSearchDetails$status"/>
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.StartDate" WIDTH="25">
      <CONNECT>
        <SOURCE NAME="ACTION" PROPERTY="result$piSearchDetails$startDateTime"/>
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>