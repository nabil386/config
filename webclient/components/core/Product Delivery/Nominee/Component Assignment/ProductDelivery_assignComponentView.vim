<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2009-2010 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- Allows user to select a component and assigned it to a user.           -->
<!-- BEGIN, CR00193859, GD -->
<?curam-deprecated Since Curam 6.0, replaced with ProductDelivery_assignComponentView1.vim.
  This page doesn't support Multiple Active Nominee Delivery Patterns (HLFR14604) . 
  Hence this page has been deprecated. See release note: CR00193859 ?>
<!-- END, CR00193859 -->
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
    CLASS="Case"
    NAME="DISPLAY"
    OPERATION="listCaseObjective"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="Case"
    NAME="ACTION"
    OPERATION="assignObjective1"
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


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
  </CONNECT>


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="caseNomineeID"/>
  <PAGE_PARAMETER NAME="description"/>
  <PAGE_PARAMETER NAME="nomineeName"/>

  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseNomineeID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseNomineeID"
    />
  </CONNECT>


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


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="1"
  >
    <FIELD
      LABEL="Field.Label.Component"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="rulesObjectiveID"
          NAME="DISPLAY"
          PROPERTY="objectiveCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="rulesObjectiveID"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.FromStartDateInd">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fromCaseStartDateInd"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.StartDate"
      USE_DEFAULT="false"
      WIDTH="20"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fromDate"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
