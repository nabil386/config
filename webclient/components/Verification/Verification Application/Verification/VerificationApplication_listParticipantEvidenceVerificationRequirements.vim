<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2007, 2010-2011 Curam Software Ltd.                      -->
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


  <LIST
    DESCRIPTION="Field.Label.Description1"
    TITLE="List.Title.VerificationRequirements"
  >
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY1"
        PROPERTY="showVerificationCluster"
      />
    </CONDITION>
    <ACTION_SET TYPE="LIST_ROW_MENU">
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
              NAME="DISPLAY1"
              PROPERTY="verificationLinkedID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleID"
            />
          </CONNECT>
          <!-- BEGIN, CR00078730,RF-->
          <CONNECT>
            <SOURCE
              NAME="CONSTANT"
              PROPERTY="verification.showCaseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
          <!-- END, CR00078730 -->
          <!-- BEGIN, CR00079515,AL-->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY1"
              PROPERTY="result$evidenceType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceType"
            />
          </CONNECT>
          <!-- END, CR00079515 -->
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <FIELD
      LABEL="Field.Title.DataItem"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY1"
          PROPERTY="dataItemName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.EvidenceType"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY1"
          PROPERTY="result$evidenceType"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.Status"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY1"
          PROPERTY="verificationStatus"
        />
      </CONNECT>
    </FIELD>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="VerificationApplication_viewWorkspaceParticipantVerificationDetails">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY1"
            PROPERTY="verificationLinkedID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY1"
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
        <CONNECT>
          <SOURCE
            NAME="DISPLAY1"
            PROPERTY="result$evidenceType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceType"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


  </LIST>
</VIEW>
