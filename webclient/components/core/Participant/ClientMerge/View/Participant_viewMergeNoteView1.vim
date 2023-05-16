<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008, 2010-2011 Curam Software Ltd.                          -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This Page views a Note related to a particular participant             -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="DISPLAY"
    OPERATION="readNote1"
  />


  <PAGE_PARAMETER NAME="participantNoteID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="participantNoteID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$key$key$participantNoteID"
    />
  </CONNECT>


  <CLUSTER>


    <CLUSTER
      LABEL_WIDTH="28"
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD LABEL="Field.Label.Sensitivity">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="sensitivityCode"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    STYLE="RemoveFieldPadding"
    TITLE="Cluster.Title.NoteHistory"
  >
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="notesText"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
