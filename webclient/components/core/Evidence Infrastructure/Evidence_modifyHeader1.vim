<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2010, 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c)2010 Curam Software Ltd.                                  -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Evidence infrastructure page containing common fields when modifying   -->
<!-- custom evidence                                                        -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
  >
  
  
  <CLUSTER
    BEHAVIOR="NONE"
    LABEL_WIDTH="30"
    NUM_COLS="1"
    STYLE="cluster-cpr-no-border"
    >
    <FIELD
      LABEL="Field.Label.ReceivedDate"
      WIDTH="30"
      >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="receivedDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="receivedDate"
        />
      </CONNECT>
    </FIELD>
    
    <!-- BEGIN, CR00335802, SS -->
    <FIELD
      LABEL="Field.Label.ChangeReason"
      WIDTH="80"
      >
      <!-- END, CR00335802 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="changeReason"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="changeReason"
        />
      </CONNECT>
    </FIELD>
    
    <!-- BEGIN, CR00335802, SS -->
    <FIELD
      LABEL="Field.Label.EffectiveDate"
      PROMPT="Field.Prompt.EffectiveDate"
      WIDTH="80"
      >
      <!-- END, CR00335802 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="effectiveFrom"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="effectiveFrom"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  
  
</VIEW>
