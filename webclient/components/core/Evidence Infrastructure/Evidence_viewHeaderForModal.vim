<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010-2011 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Evidence infrastructure page containing common fields when viewing     -->
<!-- custom evidences. All OPEN_NEW links and action controls have been     -->
<!-- replaced with OPEN_MODAL links and action controls in this version of  -->
<!-- the Evidence_viewHeader view page.                                     -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <FIELD LABEL="Field.Label.UpdatedOn">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="updatedDateTime"
      />
    </CONNECT>
  </FIELD>


  <FIELD LABEL="Field.Label.EffectiveDate">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="effectiveFrom"
      />
    </CONNECT>
  </FIELD>


  <FIELD LABEL="Field.Label.ApprovalRequestedInd">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="approvalRequestedInd"
      />
    </CONNECT>
  </FIELD>


  <CONTAINER LABEL="Field.Label.UpdatedBy">


    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="updatedBy"
        />
      </CONNECT>
    </FIELD>


    <ACTION_CONTROL
      APPEND_ELLIPSIS="false"
      LABEL="ActionControl.Label.ViewHistory"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Evidence_trackEvidence1"
        SAVE_LINK="false"
      >
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
            PROPERTY="evidenceID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="evidenceType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evType"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


  </CONTAINER>


  <FIELD CONTROL="SKIP"/>


  <CONTAINER LABEL="Field.Label.ApprovalStatus">
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="approvalRequestStatus"
        />
      </CONNECT>
    </FIELD>


    <ACTION_CONTROL
      APPEND_ELLIPSIS="false"
      LABEL="ActionControl.Label.ViewHistory"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Evidence_listAllApprovalRequests1"
        SAVE_LINK="false"
      >
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
            PROPERTY="evidenceID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="evidenceType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceType"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="evidenceDescriptorID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceDescriptorID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </CONTAINER>


</VIEW>
