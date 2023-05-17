<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText"
      />
    </CONNECT>
  </PAGE_TITLE>
  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="message"
      />
    </CONNECT>
  </INFORMATIONAL>
  <SERVER_INTERFACE
    CLASS="DecisionActivityAdmin"
    NAME="DISPLAY"
    OPERATION="readFreeFormQuestion"
    PHASE="DISPLAY"
  />
  <PAGE_PARAMETER NAME="processID"/>
  <PAGE_PARAMETER NAME="processVersionNo"/>
  <PAGE_PARAMETER NAME="activityID"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="processID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="activityKey$processID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="processVersionNo"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="activityKey$processVersionNo"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="activityID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="activityKey$activityID"
    />
  </CONNECT>
  <ACTION_SET>
    <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="ProcessDefinitionTool_editFreeFormQuestion"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="processID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="processID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="processVersionNo"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="processVersionNo"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="activityID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="activityID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
    <ACTION_CONTROL LABEL="Link.AddParameter">
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="ProcessDefinitionTool_createQuestionTextParameter"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="activityID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="activityID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="processID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="processID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="processVersionNo"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="processVersionNo"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$versionNo"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="versionNo"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$activityName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="activityName"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="filterType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="filterType"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
    <ACTION_CONTROL LABEL="Link.ChangeAnswerFormat">
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="ProcessDefinitionTool_editAnswerFormat"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="activityID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="activityID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="processID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="processID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="processVersionNo"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="processVersionNo"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>
  <CLUSTER
    LABEL_WIDTH="30%"
    NUM_COLS="1"
    TITLE="Cluster.QuestionAndAnswer"
  >
    <FIELD LABEL="Field.Label.AnswerFormat">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$answerFormat"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.QuestionText">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$questionText"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.AnswerDataType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$answerDataType"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ResultMapping">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$resultMapping"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <LIST
    DESCRIPTION="List.Description"
    TITLE="List.Label.QuestionParameters"
  >
    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="Action.Label.Reorder">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="ProcessDefinitionTool_reorderQuestionTextParameter"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="processID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="processID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="processVersionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="processVersionNo"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="activityID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="activityID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$parameters$dtls$workflowDataObjectAttributeID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="wdoAttributeID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="Action.Label.Delete">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="ProcessDefinitionTool_deleteQuestionTextParameter"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="processID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="processID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="processVersionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="processVersionNo"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="activityID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="activityID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$versionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="versionNo"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$parameters$dtls$workflowDataObjectAttributeID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="wdoAttributeID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>
    <FIELD LABEL="Container.Label.WorkflowDataObjectAttribute">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$parameters$dtls$workflowDataObjectAttributeID"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Container.Label.Valid">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="valid"
        />
      </CONNECT>
    </FIELD>
  </LIST>
</VIEW>
