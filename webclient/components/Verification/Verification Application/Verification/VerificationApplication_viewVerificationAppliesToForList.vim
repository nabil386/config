<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2011 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to view a Verification Details Page.         -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_PARAMETER NAME="VDIEDLinkID"/>
  <PAGE_PARAMETER NAME="evidenceDescriptorID"/>
  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="dataItemName"/>


  <SERVER_INTERFACE
    CLASS="VerificationApplication"
    NAME="DISPLAY"
    OPERATION="fetchVerificationDetails"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="VDIEDLinkID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="vdIEDLinkID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="evidenceDescriptorID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="evidenceDescriptorID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="verificationLinkedID"
    />
  </CONNECT>


  <LIST>
    <FIELD
      LABEL="Field.Label.CaseReference"
      WIDTH="13"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseReference"
        />
      </CONNECT>
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
    </FIELD>


    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="18"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseType"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="12"
    >
      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="verificationStatusDescription"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Mandatory"
      WIDTH="14"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="mandatory"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Level"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="verificationRequirementSummaryDetailsList$level"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.MinimumItems"
      WIDTH="18"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="minimumItems"
        />
      </CONNECT>
    </FIELD>


    <CONTAINER
      LABEL="Container.Label.DueDate"
      WIDTH="15"
    >
      <FIELD WIDTH="10">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="dueDate"
          />
        </CONNECT>
      </FIELD>
      <FIELD WIDTH="5">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="dueDateModifiableInd"
          />
        </CONNECT>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="VerificationApplication_modifyDueDate"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="verificationID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="verificationID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dueDate"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="dueDate"
            />
          </CONNECT>
        </LINK>
      </FIELD>
    </CONTAINER>
  </LIST>


</VIEW>
