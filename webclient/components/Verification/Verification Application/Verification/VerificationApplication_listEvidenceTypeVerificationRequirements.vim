<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2012, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2004 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Page to list Verification Requirements.                                -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="VerificationApplication"
    NAME="DISPLAY1"
    OPERATION="listEvidenceVerificationDetails"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="evidenceType"/>


  <!-- Connect parameters to display bean-->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY1"
      PROPERTY="key$dtls$caseID"
    />
  </CONNECT>


  <!-- BEGIN, CR00022902, KY -->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="evidenceType"
    />
    <TARGET
      NAME="DISPLAY1"
      PROPERTY="key$dtls$evidenceType"
    />
  </CONNECT>
  <!-- END, CR00022902, KY -->
  <!-- BEGIN, CR00451497, GK -->
  <ACTION_SET>
    <ACTION_CONTROL
      IMAGE="AddButton"
      LABEL="ActionControl.Label.VerifyAll"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="VerificationApplication_verifyOutstandingEvd"
      >
        <CONNECT>
          <SOURCE
            NAME="CONSTANT"
            PROPERTY="Constant.NoValue"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="VDIEDLinkID"
          />
        </CONNECT>
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
        <CONNECT>
          <SOURCE
            NAME="CONSTANT"
            PROPERTY="Constant.NoValue"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="wizardStateID"
          />
        </CONNECT>


      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>
  <!-- END, CR00451497 -->


  <LIST
    DESCRIPTION="Field.Label.Description1"
    TITLE="List.Title.VerificationRequirements"
  >
    <!-- BEGIN, CR00019054, KY -->
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY1"
        PROPERTY="result$showVerificationCluster"
      />
    </CONDITION>
    <!-- END, CR00019054 -->


    <CONTAINER
      LABEL="Container.Label.Action"
      SEPARATOR="Container.Separator1"
      WIDTH="20"
    >
      <ACTION_CONTROL LABEL="ActionControl.Label.View">
        <LINK PAGE_ID="VerificationApplication_viewWorkspaceVerificationDetails">
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
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="contextDescription"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="contextDescription"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY1"
              PROPERTY="evidenceDescriptorID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceDescriptorID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY1"
              PROPERTY="dataItemName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="dataItemName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY1"
              PROPERTY="vdIEDLinkID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="VDIEDLinkID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="ActionControl.Label.AddItem">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="VerificationApplication_createVerificationItemProvision"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY1"
              PROPERTY="vdIEDLinkID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="VDIEDLinkID"
            />
          </CONNECT>
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


    </CONTAINER>


    <FIELD LABEL="Field.Title.Details">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY1"
          PROPERTY="result$evidenceDtls$verificationDtls$summary"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Title.DataItem">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY1"
          PROPERTY="result$evidenceDtls$verificationDtls$dataItemName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Title.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY1"
          PROPERTY="result$evidenceDtls$verificationDtls$verificationStatus"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
