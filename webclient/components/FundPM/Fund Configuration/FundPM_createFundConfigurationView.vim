<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2010, 2014. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
    Copyright 2010 Curam Software Ltd.
    All rights reserved.
    
    This software is the confidential and proprietary information of Curam
    Software, Ltd. ("Confidential Information"). You shall not disclose
    such Confidential Information and shall use it only in accordance with the
    terms of the license agreement you entered into with Curam Software.
-->
<!-- This page allows the user to create fund configuration for a fund -->
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


  <SERVER_INTERFACE
    CLASS="MaintainFundConfiguration"
    NAME="ACTION"
    OPERATION="createFundConfiguration"
    PHASE="ACTION"
  />


  <SERVER_INTERFACE
    CLASS="MaintainProgramFund"
    NAME="DISPLAY"
    OPERATION="readProgramFund"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="fundID"/>
  <PAGE_PARAMETER NAME="contextDescription"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="fundID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="programFundID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="fundID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$programFundID"
    />
  </CONNECT>


  <ACTION_SET
    ALIGNMENT="CENTER"
    TOP="false"
  >
    <ACTION_CONTROL
      LABEL="ActionControl.Label.Save"
      TYPE="SUBMIT"
    />
    <ACTION_CONTROL
      LABEL="ActionControl.Label.Cancel"
      TYPE="ACTION"
    />
  </ACTION_SET>


  <CLUSTER>
    <FIELD
      LABEL="Field.Label.EffectiveDate"
      USE_BLANK="true"
      WIDTH="35"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="effectiveDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.OverObligationAllowedWhenCreatingObligation">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="overObligationIndCreateOblign"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.OverObligationAllowedWhenAdjustingObligation">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="overObligationIndAdjustOblign"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00227014, PS -->
    <FIELD LABEL="Field.Label.WaitListAllowed">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="waitListAllowedInd"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00227014 -->
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00426143, GK -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00426143 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
