<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Description -->
<!-- =========== -->
<!-- This is the included view for process Instance Errors Search By process details. -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


  <SERVER_INTERFACE CLASS="WorkflowAdministration" NAME="ACTION" OPERATION="processInstanceErrorsSearchByProcessDetails" PHASE="ACTION"/>


  <ACTION_SET>
    <ACTION_CONTROL LABEL="ActionControl.Label.ResolveAll">
      <CONDITION>
        <IS_TRUE NAME="ACTION" PROPERTY="result$allowResolveDPEnabled"/>
      </CONDITION>
      <LINK OPEN_MODAL="true" PAGE_ID="WorkflowAdministration_resolveAllDPErrorMessage">
        <CONNECT>
          <SOURCE NAME="ACTION" PROPERTY="result$idList"/>
          <TARGET NAME="PAGE" PROPERTY="messageIDsString"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>


  <INFORMATIONAL>
    <CONNECT>
      <SOURCE NAME="ACTION" PROPERTY="result$infoList$dtls$informationMsgTxt"/>
    </CONNECT>
  </INFORMATIONAL>


  <LIST TITLE="List.Title.SearchResults">


    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Retry">
        <CONDITION>
          <IS_TRUE NAME="ACTION" PROPERTY="allowWorkflowActions"/>
        </CONDITION>
        <LINK OPEN_MODAL="true" PAGE_ID="WorkflowAdministration_retryMessagePIE" WINDOW_OPTIONS="width=400">
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$processInstanceErrorSummary$messageID"/>
            <TARGET NAME="PAGE" PROPERTY="messageID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$processInstanceErrorSummary$processInstanceID"/>
            <TARGET NAME="PAGE" PROPERTY="processInstanceID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$processInstanceErrorSummary$processName"/>
            <TARGET NAME="PAGE" PROPERTY="processName"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Abort">
        <CONDITION>
          <IS_TRUE NAME="ACTION" PROPERTY="allowWorkflowActions"/>
        </CONDITION>
        <LINK OPEN_MODAL="true" PAGE_ID="WorkflowAdministration_abortMessagePIE" WINDOW_OPTIONS="width=400">
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$processInstanceErrorSummary$messageID"/>
            <TARGET NAME="PAGE" PROPERTY="messageID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$processInstanceErrorSummary$processInstanceID"/>
            <TARGET NAME="PAGE" PROPERTY="processInstanceID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$processInstanceErrorSummary$processName"/>
            <TARGET NAME="PAGE" PROPERTY="processName"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL IMAGE="AbortAllButton" LABEL="Button.Label.AbortAll" TYPE="ACTION">
        <CONDITION>
          <IS_TRUE NAME="ACTION" PROPERTY="allowWorkflowActions"/>
        </CONDITION>
        <LINK OPEN_MODAL="true" PAGE_ID="WorkflowAdministration_abortAllMessagesPIE" WINDOW_OPTIONS="width=400">
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$processInstanceErrorSummary$processName"/>
            <TARGET NAME="PAGE" PROPERTY="processName"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$processInstanceErrorSummary$processID"/>
            <TARGET NAME="PAGE" PROPERTY="processID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="processVersion"/>
            <TARGET NAME="PAGE" PROPERTY="processVersion"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL IMAGE="RetryAllButton" LABEL="Button.Label.RetryAll" TYPE="ACTION">
        <CONDITION>
          <IS_TRUE NAME="ACTION" PROPERTY="allowWorkflowActions"/>
        </CONDITION>
        <LINK OPEN_MODAL="true" PAGE_ID="WorkflowAdministration_retryAllMessagesPIE" WINDOW_OPTIONS="width=400">
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$processInstanceErrorSummary$processName"/>
            <TARGET NAME="PAGE" PROPERTY="processName"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$processInstanceErrorSummary$processID"/>
            <TARGET NAME="PAGE" PROPERTY="processID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="processVersion"/>
            <TARGET NAME="PAGE" PROPERTY="processVersion"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="Button.Label.ResolveDP" TYPE="ACTION">
        <CONDITION>
          <IS_TRUE NAME="ACTION" PROPERTY="allowDPActions"/>
        </CONDITION>
        <LINK OPEN_MODAL="true" PAGE_ID="WorkflowAdministration_resolveDPErrorMessage" WINDOW_OPTIONS="width=400">
          <CONNECT>
            <SOURCE NAME="ACTION" PROPERTY="result$processInstanceErrorSummary$messageID"/>
            <TARGET NAME="PAGE" PROPERTY="messageID"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <FIELD LABEL="Field.Label.ProcessDisplayName" WIDTH="30">
      <CONNECT>
        <SOURCE NAME="ACTION" PROPERTY="result$processInstanceErrorSummary$processDisplayName"/>
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ProcessType" WIDTH="15">
      <CONNECT>
        <SOURCE NAME="ACTION" PROPERTY="result$processInstanceErrorSummary$processInstanceErrorType"/>
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ProcessInstanceData" WIDTH="15">
      <CONNECT>
        <SOURCE NAME="ACTION" PROPERTY="result$processInstanceErrorSummary$processInstanceID"/>
      </CONNECT>
      <LINK PAGE_ID="WorkflowAdministration_resolveProcessInstanceType">
        <CONNECT>
          <SOURCE NAME="ACTION" PROPERTY="result$processInstanceErrorSummary$processInstanceErrorType"/>
          <TARGET NAME="PAGE" PROPERTY="processInstanceType"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="ACTION" PROPERTY="result$processInstanceErrorSummary$processInstanceID"/>
          <TARGET NAME="PAGE" PROPERTY="processInstanceID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="ACTION" PROPERTY="result$processInstanceErrorSummary$processDisplayName"/>
          <TARGET NAME="PAGE" PROPERTY="processName"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="ACTION" PROPERTY="result$processInstanceErrorSummary$startDateTime"/>
          <TARGET NAME="PAGE" PROPERTY="startDateTime"/>
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD LABEL="Field.Label.FailureType" WIDTH="15">
      <CONNECT>
        <SOURCE NAME="ACTION" PROPERTY="result$processInstanceErrorSummary$failureText"/>
      </CONNECT>
      <LINK OPEN_MODAL="true" PAGE_ID="WorkflowAdministration_processInstanceLoggedErrors">
        <CONNECT>
          <SOURCE NAME="ACTION" PROPERTY="result$processInstanceErrorSummary$messageID"/>
          <TARGET NAME="PAGE" PROPERTY="messageID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="ACTION" PROPERTY="result$processInstanceErrorSummary$processInstanceErrorType"/>
          <TARGET NAME="PAGE" PROPERTY="type"/>
        </CONNECT>
      </LINK>
    </FIELD>
    <FIELD LABEL="Field.Label.FailureDateTime" WIDTH="15">
      <CONNECT>
        <SOURCE NAME="ACTION" PROPERTY="result$processInstanceErrorSummary$failureDateTime"/>
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ResolutionStatus" WIDTH="10">
      <CONNECT>
        <SOURCE NAME="ACTION" PROPERTY="result$processInstanceErrorSummary$status"/>
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>