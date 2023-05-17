<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003-2006, 2009-2010 Curam Software Ltd.                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Included view used to check a client's eligibility to receive a        -->
<!-- product delivery.                                                      -->
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
    NAME="ACTION"
    OPERATION="checkEligibility"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>
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


  <CLUSTER>
    <ACTION_SET
      ALIGNMENT="CENTER"
      TOP="false"
    >


      <!-- BEGIN, CR00050298, MR -->
      <!-- BEGIN, HARP 64908, SP -->
      <ACTION_CONTROL
        IMAGE="CheckEligibilityButton"
        LABEL="ActionControl.Label.CheckEligibility"
        TYPE="SUBMIT"
      >
        <!-- END, HARP 64908 -->
        <!-- END, CR00050298 -->


        <LINK PAGE_ID="THIS"/>
      </ACTION_CONTROL>


      <!-- BEGIN, CR00050298, MR -->
      <!-- BEGIN, HARP 64908, SP -->
      <ACTION_CONTROL
        IMAGE="CloseButton"
        LABEL="ActionControl.Label.Close"
      >
        <LINK PAGE_ID="Case_resolveCaseHome">
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <!-- END, HARP 64908 -->
      <!-- END, CR00050298 -->


    </ACTION_SET>
  </CLUSTER>


  <CLUSTER
    DESCRIPTION="Cluster.Description"
    LABEL_WIDTH="56"
    NUM_COLS="3"
    TITLE="Cluster.Title.CheckEligibility"
  >
    <FIELD LABEL="Field.Label.EligibilityDate">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="evaluationDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.CaseStartDate">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="forCaseStartDateInd"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ActiveEvidenceOnly">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="forActiveEvidenceInd"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER TITLE="List.Title.Objectives">
    <LIST STYLE="cluster-cpr-no-border">
      <FIELD
        LABEL="Field.Label.Name"
        WIDTH="40"
      >
        <CONNECT>
          <SOURCE
            NAME="ACTION"
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
            NAME="ACTION"
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
            NAME="ACTION"
            PROPERTY="value"
          />
        </CONNECT>
      </FIELD>
    </LIST>
  </CLUSTER>
  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Rules"
  >
    <FIELD
      CONFIG="ActionBeanDecisionID"
      CONTROL="DYNAMIC"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="resultText"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
