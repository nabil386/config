<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2007, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2007 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for a list of client interactions           .        -->
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


  <SHORTCUT_TITLE>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="participantContextDescriptionDetails$description"
      />
    </CONNECT>
  </SHORTCUT_TITLE>


  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="DISPLAY"
    OPERATION="readInteraction"
  />


  <PAGE_PARAMETER NAME="clientInteractionID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="clientInteractionID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="clientInteractionID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="20"
    NUM_COLS="2"
  >


    <FIELD LABEL="Field.Label.InteractionType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="interactionTypeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.InteractionDateTime">


      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="interactionDateTime"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00416277, VT -->
    <FIELD LABEL="Field.Label.Comments">
      <!-- END, CR00416277 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$readInteractionDetails$comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
