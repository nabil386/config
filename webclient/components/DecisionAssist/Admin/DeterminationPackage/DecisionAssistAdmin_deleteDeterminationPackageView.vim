<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2006-2008, 2010 Curam Software Ltd.                          -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows the user to check if the person has already been -->
<!-- registered.                                                       -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <SERVER_INTERFACE
    CLASS="DeterminationPackage"
    NAME="DELETE_DP"
    OPERATION="deleteDeterminationPackage"
    PHASE="ACTION"
  />
  <PAGE_PARAMETER NAME="determinationPackageVersionID"/>
  <PAGE_PARAMETER NAME="versionNumber"/>
  <PAGE_PARAMETER NAME="determinationPackageVersionName"/>
  <PAGE_PARAMETER NAME="determinationConfigID"/>
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText"
      />
    </CONNECT>
  </PAGE_TITLE>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="determinationPackageVersionID"
    />
    <TARGET
      NAME="DELETE_DP"
      PROPERTY="determinationPackageVersionID"
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
          PROPERTY="Field.StaticText.DeleteDeterminationPackage"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
