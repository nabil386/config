<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010-2011 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- Rights And Responsibility for case page -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_PARAMETER NAME="executionID"/>
  <PAGE_PARAMETER NAME="concernRoleID"/>


  <SERVER_INTERFACE
    CLASS="Application"
    NAME="DISPLAY"
    OPERATION="getRightsAndResponsibilitiesTextFields"
    PHASE="DISPLAY"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="executionID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="executionID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="executionID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="executionID"
    />
  </CONNECT>


  <CLUSTER
    BEHAVIOR="NONE"
    SHOW_LABELS="false"
    TITLE="Cluster.Title.ClientsRightsandResponsibilities"
  >
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rightsAndResponsibilitiesText"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>
  <!-- BEGIN,CR00281055,AM-->
  <CLUSTER
    LABEL_WIDTH="95"
    LAYOUT_ORDER="FIELD"
    STYLE="boldtext-cluster"
    WIDTH="70"
  >
    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.RightsAndResponsibilities"
      WIDTH="100"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="clientsRightsInd"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>
  <!-- END,CR00281055,AM-->


  <CLUSTER
    BEHAVIOR="NONE"
    SHOW_LABELS="false"
    TITLE="Cluster.Title.InformationPolicy"
  >
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="departmentPolicyText"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <!-- BEGIN,CR00281055,AM-->
  <CLUSTER
    LABEL_WIDTH="95"
    LAYOUT_ORDER="FIELD"
    STYLE="boldtext-cluster"
    WIDTH="70"
  >
    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.DepartmentPolicy"
      WIDTH="100"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="departmentPolicyInd"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>
  <!-- END,CR00281055,AM-->


  <CLUSTER
    BEHAVIOR="NONE"
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Authorization"
  >
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="authorizationInformationText"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <!-- BEGIN,CR00281055,AM-->
  <CLUSTER
    LABEL_WIDTH="95"
    LAYOUT_ORDER="FIELD"
    STYLE="boldtext-cluster"
    WIDTH="70"
  >
    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.Authorization"
      WIDTH="100"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="authorizationInd"
        />
      </CONNECT>
    </FIELD>
    <FIELD CONTROL="SKIP"/>
  </CLUSTER>


  <!-- END,CR00281055,AM-->
</VIEW>
