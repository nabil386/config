<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2006-2007, 2010 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to view a list of client interaction with    -->
<!-- the organization                                                       -->
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


  <PAGE_PARAMETER NAME="linkID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="linkID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="linkID"
    />
  </CONNECT>


  <SERVER_INTERFACE
    CLASS="ExternalLinkedParticipant"
    NAME="DISPLAY"
    OPERATION="listInteractions"
  />
  <LIST>
    <FIELD
      LABEL="Field.Label.InteractionType"
      WIDTH="70"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="interactionTypeCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.InteractionDateTime"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="interactionDateTime"
        />
      </CONNECT>
    </FIELD>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="SPAE_Participant_viewInteractionFromList">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="clientInteractionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="clientInteractionID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>
  </LIST>
</VIEW>
