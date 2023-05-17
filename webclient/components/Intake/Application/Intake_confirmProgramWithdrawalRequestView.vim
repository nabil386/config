<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2010 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This Page views a particular program withdrawal request and conform   -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="1"
  >
    <FIELD LABEL="Field.Label.Program">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$program"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.RequestedBy">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$clientName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.WithdrawalDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$withdrawalDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.WithdrawalMethod">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$withdrawalMethod"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.WithdrawalReason">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$withdrawalReason"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      HEIGHT="3"
      LABEL="Field.Label.WithdrawalOtherReason"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$withdrawalOtherReason"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
