<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003 - 2008, 2010 Curam Software Ltd.                    -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of       -->
<!-- Curam Software, Ltd. (&quot;Confidential Information&quot;). You       -->
<!-- shall not disclose such Confidential Information and shall use it only -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- with Curam Software.                                                   -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Page displaying the details of a sub-rule set.                         -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText1"
      />
    </CONNECT>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="ruleSetName"
      />
    </CONNECT>
  </PAGE_TITLE>
  <SERVER_INTERFACE
    CLASS="RulesEditor"
    NAME="DISPLAY"
    OPERATION="readSubRuleSetSummary"
  />
  <!-- BEGIN, CR00021717, KY -->
  <SERVER_INTERFACE
    CLASS="RulesEditor"
    NAME="DISPLAY1"
    OPERATION="listTranslation"
  />
  <SERVER_INTERFACE
    CLASS="RulesEditor"
    NAME="DISPLAY2"
    OPERATION="listRulesDataObjectsForRuleSet"
  />
  <!-- END, CR00021717 -->
  
  <PAGE_PARAMETER NAME="ruleSetID"/>
  <PAGE_PARAMETER NAME="nodeID"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="ruleSetID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="subRuleSetSummaryReadkey$ruleSetID"
    />
  </CONNECT>
  <!-- BEGIN, CR00021717, KY -->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="ruleSetID"
    />
    <TARGET
      NAME="DISPLAY1"
      PROPERTY="ruleSetNodeKey$ruleSetID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="ruleSetID"
    />
    <TARGET
      NAME="DISPLAY2"
      PROPERTY="ruleSetID"
    />
  </CONNECT>
  <!-- &lt;CONNECT&gt;
      &lt;SOURCE
        NAME=&quot;PAGE&quot;
        PROPERTY=&quot;nodeID&quot;
      /&gt;
      &lt;TARGET
        NAME=&quot;DISPLAY1&quot;
        PROPERTY=&quot;ruleSetNodeKey$nodeID&quot;
      /&gt;
&lt;/CONNECT&gt; -->
  <!-- END, CR00021717 -->
  
  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Label.RuleSetID">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$subRuleSetDtls$ruleSetID"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Highlight">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="highlightOnFailureInd"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="ruleSetName"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER
    LABEL_WIDTH="20"
    TITLE="Cluster.Title.ResultText"
  >
    <FIELD LABEL="Field.Label.Success">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="successText"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Failure">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="failureText"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <LIST TITLE="List.Title.Items">
    <CONTAINER
      LABEL="Container.Label.Action"
      SEPARATOR="Container.Separator"
      WIDTH="30"
    >
      <ACTION_CONTROL LABEL="ActionControl.Label.View">
        <LINK PAGE_ID="RulesEditor_resolveViewRuleElementDetails">
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="ruleSetID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="ruleSetID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="nodeDetails$nodeID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="nodeID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="nodeType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="nodeType"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="versionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="versionNo"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK PAGE_ID="RulesEditor_resolveEditRuleElementDetails">
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="ruleSetID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="ruleSetID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="nodeDetails$nodeID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="nodeID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="nodeType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="nodeType"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="versionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="versionNo"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Up">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="RulesEditor_moveItemUp"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="ruleSetID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="ruleSetID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="nodeDetails$nodeID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="nodeID"
            />
          </CONNECT>
          <!-- BEGIN, CR00051913, "PA" -->
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="nodeID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="parentNodeID"
            />
          </CONNECT>
          <!-- END, CR00051913 -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="versionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="versionNo"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="nodeName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="pageDescription"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Down">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="RulesEditor_moveItemDown"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="ruleSetID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="ruleSetID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="nodeDetails$nodeID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="nodeID"
            />
          </CONNECT>
          <!-- BEGIN, CR00051913, "PA" -->
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="nodeID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="parentNodeID"
            />
          </CONNECT>
          <!-- END, CR00051913 -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="versionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="versionNo"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="nodeName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="pageDescription"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>
    <FIELD
      LABEL="Field.Title.Name"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="nodeName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.Type"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="nodeType"
        />
      </CONNECT>
    </FIELD>
  </LIST>
  <!-- BEGIN, CR00021717, KY -->
  <LIST TITLE="List.Title.Translations">
    <CONTAINER
      LABEL="Container.Label.Action"
      SEPARATOR="Container.Separator"
      WIDTH="30"
    >
      <ACTION_CONTROL LABEL="ActionControl.Label.ViewTranslation">
        <LINK PAGE_ID="RulesEditor_viewSubRuleSetTranslation">
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="ruleSetID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="ruleSetID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="subRuleSetDtls$nodeID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="nodeID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY1"
              PROPERTY="languageCode"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="languageCode"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="ruleSetName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="pageDescription"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.EditTranslation">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="RulesEditor_modifySubRuleSetTranslation"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="ruleSetID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="ruleSetID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="subRuleSetDtls$nodeID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="nodeID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY1"
              PROPERTY="languageCode"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="languageCode"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="ruleSetName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="pageDescription"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>
    <FIELD
      LABEL="Field.Title.Language"
      WIDTH="70"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY1"
          PROPERTY="languageCode"
        />
      </CONNECT>
    </FIELD>
  </LIST>
  <LIST TITLE="List.Title.RulesDataObjects">
    <CONTAINER
      LABEL="Container.Label.Action"
      SEPARATOR="Container.Separator"
      WIDTH="30"
    >
      <ACTION_CONTROL LABEL="ActionControl.Label.ViewRDO">
        <LINK PAGE_ID="RulesEditor_viewRDOForSubRuleSet">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY2"
              PROPERTY="rulesDataObjectID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="rulesDataObjectID"
            />
          </CONNECT>
          <!-- BEGIN, CR00023238, LP -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY2"
              PROPERTY="rulesDataObjectQualifier"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="RDOQualifier"
            />
          </CONNECT>


          <CONNECT>
            <SOURCE
              NAME="DISPLAY2"
              PROPERTY="rulesDataObjectFinal"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="RDOFinal"
            />
          </CONNECT>
          <!-- END, CR00023238 -->


        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Remove">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="RulesEditor_removeRulesDataObject"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="ruleSetID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="ruleSetID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY2"
              PROPERTY="rulesDataObjectID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="rulesDataObjectID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY2"
              PROPERTY="rulesDataObjectName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="pageDescription"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY2"
              PROPERTY="versionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="versionNo"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>
    <FIELD
      LABEL="Field.Title.RdoName"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY2"
          PROPERTY="rulesDataObjectName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.Type"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY2"
          PROPERTY="rulesDataObjectType"
        />


      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.Qualifier"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY2"
          PROPERTY="rulesDataObjectQualifier"
        />


      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.Final"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY2"
          PROPERTY="rulesDataObjectFinal"
        />


      </CONNECT>
    </FIELD>
  </LIST>
  <!-- END, CR00021717 -->
  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
