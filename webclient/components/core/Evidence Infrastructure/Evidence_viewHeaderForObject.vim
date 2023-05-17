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
<!-- custom evidences                                                       -->
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


  <FIELD LABEL="Field.Label.ChangeReason">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="changeReason"
      />
    </CONNECT>
  </FIELD>


  <CONTAINER LABEL="Field.Label.ApprovalStatus">
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="approvalRequestStatus"
        />
      </CONNECT>
    </FIELD>


  </CONTAINER>


  <CONTAINER LABEL="Field.Label.UpdatedBy">


    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="updatedBy"
        />
      </CONNECT>
    </FIELD>


  </CONTAINER>


</VIEW>
