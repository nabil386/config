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
    OPERATION="listVerificationWaiverDetails"
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


  <LIST TITLE="List.Title.VerificationWaiver">


    <FIELD
      LABEL="Field.Label.WaiverStartDate"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.WaiverEndDate"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.LastModifiedByUser"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="lastModifiedByUserFullName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.WaiverStatus"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="recordStatus"
        />
      </CONNECT>
    </FIELD>
  </LIST>
</VIEW>
