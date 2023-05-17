<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008, 2010 Curam Software Ltd.                                               -->
<!-- All rights reserved.                                                                   -->
<!-- This software is the confidential and proprietary information of Curam                 -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose                    -->
<!-- such Confidential Information and shall use it only in accordance with                 -->
<!-- the terms of the license agreement you entered into with Curam                         -->
<!-- Software.                                                                              -->
<!-- Description                                                                            -->
<!-- ===========                                                                            -->
<!-- This page is used to view a finding details                                            -->
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
    CLASS="InvestigationDelivery"
    NAME="DISPLAY"
    OPERATION="readFinding"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="findingID"/>
  <PAGE_PARAMETER NAME="allegationID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>
  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="findingID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$findingID"
    />
  </CONNECT>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
