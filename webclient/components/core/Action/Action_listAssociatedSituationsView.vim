<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010 Curam Software Ltd.                                      -->
<!-- All rights reserved.                                                    -->
<!-- This software is the confidential and proprietary information of Curam  -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose     -->
<!-- such Confidential Information and shall use it only in accordance with  -->
<!-- the terms of the license agreement you entered into with Curam          -->
<!-- Software.                                                               -->
<!-- Description                                                             -->
<!-- ===========                                                             -->
<!-- The included view to list the associated situations.                    -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_PARAMETER NAME="actionID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="actionID"
    />
    <TARGET
      NAME="DISPLAYSITUATIONLIST"
      PROPERTY="actionID"
    />
  </CONNECT>


  <SERVER_INTERFACE
    CLASS="Action"
    NAME="DISPLAYSITUATIONLIST"
    OPERATION="listAssociatedSituations"
    PHASE="DISPLAY"
  />


  <!-- BEGIN, CR00208312, AK -->
  <CLUSTER TITLE="Cluster.Label.AssociatedSituations">
    <LIST>
      <FIELD
        LABEL="Field.Title.SituationCategory"
        WIDTH="30"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAYSITUATIONLIST"
            PROPERTY="situationCategory"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Title.SituationReqAction">
        <CONNECT>
          <SOURCE
            NAME="DISPLAYSITUATIONLIST"
            PROPERTY="situationRequiringAction"
          />
        </CONNECT>
      </FIELD>
    </LIST>
  </CLUSTER>
  <!-- END, CR00208312 -->
</VIEW>
