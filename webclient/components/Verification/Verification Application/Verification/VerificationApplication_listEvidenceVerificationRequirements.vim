<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2012, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2008, 2010 Curam Software Ltd.                                 -->
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
    <!-- BEGIN, CR00100757, CH -->
    <!-- BEGIN, CR00077979, CH -->
    <!-- This condition is being removed as a temporary measure to facilitate CGMA -->
    <!-- BEGIN, CR 16459, RK -->
    <!--<CONDITION>
      <IS_TRUE NAME="DISPLAY" PROPERTY="showVerificationCluster" />
    </CONDITION>-->
    <!-- BEGIN, CR 16459 -->
    <!-- END, CR00077979 -->
    <!-- END, CR00100757 -->


    <ACTION_SET TYPE="LIST_ROW_MENU">


      <ACTION_CONTROL LABEL="ActionControl.Label.AddItem">
        <LINK PAGE_ID="VerificationApplication_createVerificationItemProvision">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
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
    </ACTION_SET>
    <!-- END, CR 3023 -->


    <FIELD LABEL="Field.Title.Details">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="verificationDtls$summary"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Title.DataItem">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dataItemName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Title.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="verificationStatus"
        />
      </CONNECT>
    </FIELD>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Evidence_resolveViewEvidencePage">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="verificationDtls$evidenceType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceType"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="relatedID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="verificationDtls$evidenceDescriptorID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceDescriptorID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="contextDescription"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="contextDescription"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


  </LIST>


</VIEW>
