<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2012 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Infrastructure page containing common fields when capturing custom     -->
<!-- evidence                                                               -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <ACTION_CONTROL LABEL="ActionControl.Label.NewEvidence">
    <!-- BEGIN CR00424202, JD -->
  	<CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="caseUpdatesAllowed"
        />
    </CONDITION>
	<!-- END CR00424202, JD -->
    <LINK
      OPEN_MODAL="true"
      PAGE_ID="Evidence_addNewEvidence"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$caseID"
        />
        <TARGET
          NAME="PAGE"
          PROPERTY="caseID"
        />
      </CONNECT>
    </LINK>
  </ACTION_CONTROL>

<ACTION_CONTROL LABEL="ActionControl.Label.ValidateChanges">
	<!-- BEGIN CR00424202, JD -->
	<CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="caseUpdatesAllowed"
        />
    </CONDITION>
	<!-- END CR00424202, JD -->
    <LINK
      OPEN_MODAL="true"
      PAGE_ID="Evidence_validateChanges"
    >
      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="caseID"
        />
        <TARGET
          NAME="PAGE"
          PROPERTY="caseID"
        />
      </CONNECT>
    </LINK>
  </ACTION_CONTROL>

</VIEW>
