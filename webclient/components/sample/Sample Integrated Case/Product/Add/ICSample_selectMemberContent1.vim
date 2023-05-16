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
<!-- View for the page that allows the user to select the case member whom  -->
<!-- the new product delivery case is being created for.                    -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="IntegratedCase"
    NAME="DISPLAY"
    OPERATION="listCaseMembersExcProspectPerson"
  />


  <SERVER_INTERFACE
    CLASS="Product"
    NAME="DISPLAYPRODUCT"
    OPERATION="listProductsForCategoryAndPerson"
  />


  <SERVER_INTERFACE
    CLASS="IntegratedCase"
    NAME="ACTION"
    OPERATION="createCaseHelper"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$caseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAYPRODUCT"
      PROPERTY="caseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseCreationDetails$integratedCaseID"
    />
  </CONNECT>


  <CLUSTER LABEL_WIDTH="20">
    <FIELD
      LABEL="Field.Label.Member"
      USE_BLANK="false"
      WIDTH="60"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="participantRoleID"
          NAME="DISPLAY"
          PROPERTY="result$participantList$name"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="caseCreationDetails$concernRoleID"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Product"
      USE_BLANK="false"
      WIDTH="80"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="productID"
          NAME="DISPLAYPRODUCT"
          PROPERTY="name"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="caseCreationDetails$productID"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
