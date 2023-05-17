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
<!-- This page allows the user to modify the fund configuration.       -->
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
    OPERATION="modifyFundConfiguration"
    PHASE="ACTION"
  />


  <SERVER_INTERFACE
    CLASS="MaintainProgramFund"
    NAME="DISPLAY1"
    OPERATION="readProgramFund"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="MaintainFundConfiguration"
    NAME="DISPLAY"
    OPERATION="viewFundConfiguration"
    PHASE="DISPLAY"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="fundID"
    />
    <TARGET
      NAME="DISPLAY1"
      PROPERTY="key$programFundID"
    />
  </CONNECT>


  <PAGE_PARAMETER NAME="obligationConfigurationID"/>
  <PAGE_PARAMETER NAME="versionNo"/>
  <PAGE_PARAMETER NAME="fundID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="obligationConfigurationID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$fundConfigurationID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="obligationConfigurationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="fundConfigurationID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>


  <CLUSTER>
    <FIELD
      LABEL="Field.Label.EffectiveDate"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="effectiveDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="effectiveDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.OverObligationAllowedWhenCreatingObligation">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="overObligationIndCreateOblign"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="overObligationIndCreateOblign"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.OverObligationAllowedWhenAdjustingObligation">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="overObligationIndAdjustOblign"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="overObligationIndAdjustOblign"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.WaitListAllowed">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="waitListAllowedInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="waitListAllowedInd"
        />
      </CONNECT>
    </FIELD>
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
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
