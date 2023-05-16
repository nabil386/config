<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2010,2021. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010, 2012 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This Page lists all financials for a particular participant            -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="DISPLAY"
    OPERATION="listParticipantFinancial1"
  />

  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="concernRoleID"/>

  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="concernRoleID"
    />
  </CONNECT>
  
  <CLUSTER
    SHOW_LABELS="false"
    STYLE="tab-renderer"
  >
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="statusInd"
      />
    </CONDITION>

    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="data"
        />
      </CONNECT>
    </FIELD>

  </CLUSTER>

  <ACTION_SET BOTTOM="false">

    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY"
        PROPERTY="statusInd"
      />
    </CONDITION>

    <ACTION_CONTROL
      IMAGE="NewAccountAdjustmentButton"
      LABEL="ActionControl.Label.NewAccountAdjustment"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Participant_newAccountAdjustment"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="contextDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>

  </ACTION_SET>

  <!-- BEGIN, CR00321600, AC -->
  <INCLUDE FILE_NAME="Financial_listParticipantFinInstructionView.vim"/>
  <!-- END, CR00321600 -->

</VIEW>