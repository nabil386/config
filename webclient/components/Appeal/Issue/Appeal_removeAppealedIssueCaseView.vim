<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2008 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <SERVER_INTERFACE
    CLASS="AppealedCase"
    NAME="ACTION"
    OPERATION="remove"
    PHASE="ACTION"
  />
  <PAGE_PARAMETER NAME="appealRelationshipID"/>
  <PAGE_PARAMETER NAME="versionNo"/>
  <PAGE_PARAMETER NAME="appealVersionNo"/>
  <PAGE_PARAMETER NAME="caseHeaderVersionNo"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="appealRelationshipID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="appealRelationshipID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="appealVersionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="appealVersionNo"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseHeaderVersionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseHeaderVersionNo"
    />
  </CONNECT>
  <CLUSTER
    SHOW_LABELS="false"
    STYLE="outer-cluster-borderless"
  >
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="TEXT"
          PROPERTY="Field.StaticText.RemoveCase"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
