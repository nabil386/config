<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2007, 2010 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Included view used to view details of a resolution approval              -->
<!-- request.                                                          -->
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
    CLASS="IssueDelivery"
    NAME="DISPLAY"
    OPERATION="viewIssueApprovalRequest"
  />


  <PAGE_PARAMETER NAME="description"/>
  <PAGE_PARAMETER NAME="issueApprovalRequestID"/>
  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="issueApprovalRequestID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="issueApprovalRequestID"
    />
  </CONNECT>




  <CLUSTER LABEL_WIDTH="23">


    <FIELD LABEL="Field.Label.SubmitComments">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="submitComments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="23"
    TITLE="Cluster.Title.Rejection"
  >


    <FIELD LABEL="Field.Label.RejectionReason">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rejectionReason"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER LABEL_WIDTH="23">


    <FIELD LABEL="Field.Label.RejectionComments">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rejectionComments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
