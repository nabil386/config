<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003-2006, 2010 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view to display the details of a decision made on         -->
<!-- a product delivery including the full rules tree view.                 -->
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
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="ProductDelivery"
    NAME="DISPLAY"
    OPERATION="readDecision"
  />


  <PAGE_PARAMETER NAME="decisionID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="decisionID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseDecisionID"
    />
  </CONNECT>


  <LIST TITLE="List.Title.Objectives">


    <FIELD
      LABEL="Field.Label.Name"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="type"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Value"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="value"
        />
      </CONNECT>
    </FIELD>


  </LIST>


  <CLUSTER
    NUM_COLS="1"
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Rules"
  >


    <ACTION_SET BOTTOM="false">
      <ACTION_CONTROL
        IMAGE="ViewStaticTreeButton"
        LABEL="ActionControl.Label.ViewStaticTree"
      >
        <LINK
          PAGE_ID="ProductDelivery_viewDecisionWithStaticRulesTree"
          SAVE_LINK="false"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="decisionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="decisionID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <!-- BEGIN, CR00050298, MR -->
    <!-- BEGIN, HARP 64908, SP -->
    <FIELD
      CONTROL="DYNAMIC_FULL_TREE"
      LABEL="Field.Label.ResultText"
    >
      <!-- END, HARP 64908 -->
      <!-- END, CR00050298 -->


      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="resultText"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
