<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows the user to cancel evidence                           -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE NAME="TEXT" PROPERTY="PageTitle.StaticText1" />
    </CONNECT>
    <CONNECT>
      <SOURCE NAME="PAGE" PROPERTY="pageDescription" />
    </CONNECT>
  </PAGE_TITLE>
  <SERVER_INTERFACE CLASS="SampleBenefit" NAME="ACTION" OPERATION="cancelEvidence" PHASE="ACTION" />
  <PAGE_PARAMETER NAME="evidenceID" />
  <PAGE_PARAMETER NAME="versionNo" />
  <PAGE_PARAMETER NAME="pageDescription" />
  <ACTION_SET ALIGNMENT="CENTER" TOP="false">
    <ACTION_CONTROL IMAGE="YesButton" LABEL="ActionControl.Label.Yes" TYPE="SUBMIT" />
    <ACTION_CONTROL IMAGE="NoButton" LABEL="ActionControl.Label.No" />
  </ACTION_SET>
  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="evidenceID" />
    <TARGET NAME="ACTION" PROPERTY="caseEvidenceTreeID" />
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="versionNo" />
    <TARGET NAME="ACTION" PROPERTY="versionNo" />
  </CONNECT>
  <CLUSTER SHOW_LABELS="false" STYLE="outer-cluster-borderless">
    <FIELD>
      <CONNECT>
        <SOURCE NAME="TEXT" PROPERTY="Field.StaticText.ActivateCase" />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>

