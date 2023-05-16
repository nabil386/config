<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2005 - 2008, 2011 Curam Software Ltd.                    -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Evidence infrastructure page containing common fields when viewing     -->
<!-- custom evidences                                                       -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <FIELD LABEL="Field.Label.EffectiveDate">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="effectiveFrom"
      />
    </CONNECT>
  </FIELD>


  <FIELD LABEL="Field.Label.UpdatedOn">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="updatedDateTime"
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


  <FIELD LABEL="Field.Label.StatusCode">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="statusCode"
      />
    </CONNECT>
  </FIELD>


  <FIELD LABEL="Field.Label.ReceivedDate">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="receivedDate"
      />
    </CONNECT>
  </FIELD>


  <!-- BEGIN, CR00113557, ROC -->
  <FIELD LABEL="Field.Label.ChangeReceivedDate">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="changeReceivedDate"
      />
    </CONNECT>
  </FIELD>
  <!-- END, CR00113557 -->


  <!-- BEGIN, CR00050298, MR -->
  <!-- BEGIN, HARP 64908, SP -->
  <CONTAINER LABEL="Container.Label.UpdatedBy">


    <FIELD LABEL="Field.Label.UpdatedBy">
      <!-- END, HARP 64908 -->
      <!-- END, CR00050298 -->


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
      >


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
      </LINK>
    </ACTION_CONTROL>
  </CONTAINER>


  <CONTAINER LABEL="Field.Label.ApprovalStatus">
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="approvalRequestStatus"
        />
      </CONNECT>
    </FIELD>


    <ACTION_CONTROL LABEL="ActionControl.Label.ViewHistory">
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Evidence_listAllApprovalRequests1"
      >


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
            NAME="PAGE"
            PROPERTY="contextDescription"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="contextDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </CONTAINER>


</VIEW>
