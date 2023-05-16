<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2002, 2022. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002-2003, 2010 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for the modify note pages.                           -->
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
    CLASS="Participant"
    NAME="DISPLAY"
    OPERATION="readNote1"
  />


  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="ACTION"
    OPERATION="modifyNote1"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="participantNoteID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="participantNoteID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$details$key$participantNoteID"
    />
  </CONNECT>
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
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="userName"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="userName"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="participantID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="participantID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="noteID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="noteID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="noteEditableInd"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="editLatestNoteTextIndOpt"
    />
  </CONNECT>

  <!-- A message is displayed if the note is temporarily locked against editing. -->
  <CLUSTER>
   <CONDITION>
      <IS_TRUE NAME="DISPLAY" PROPERTY="noteLockedInd" />
    </CONDITION>
    <CONTAINER>
      <IMAGE IMAGE="LockedIcon" LABEL="Icon.Locked"/>
      <FIELD>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="editableNoteMessageOpt" />
        </CONNECT>
      </FIELD>
    </CONTAINER>
  </CLUSTER>


  <!-- All the regular fields are only displayed if the note is not temporarily locked against editing -->
  <CLUSTER>
 	<CONDITION>
      <IS_FALSE NAME="DISPLAY" PROPERTY="noteLockedInd" />
    </CONDITION>
    
  <CLUSTER STYLE="cluster-cpr-no-internal-padding">

    <!-- BEGIN, CR00334789, MR -->
    <CLUSTER
      LABEL_WIDTH="20.5"
      STYLE="cluster-cpr-no-border"
    >
      <!-- END, CR00334789 -->


      <FIELD LABEL="Field.Label.Subject">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="subjectText"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="subjectText"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


    <!-- BEGIN, CR00334789, MR -->
    <CLUSTER
      LABEL_WIDTH="41"
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
    >
      <!-- END, CR00334789 -->
      <FIELD
        LABEL="Field.Label.Priority"
        WIDTH="50"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="priorityCode"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="priorityCode"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.Sensitivity"
        WIDTH="20"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="sensitivityCode"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="sensitivityCode"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>
  </CLUSTER>
  
  <!-- conditional cluster that shows an icon and message if the note is editable -->
  <CLUSTER>
    <CONDITION>
      <IS_TRUE NAME="DISPLAY" PROPERTY="noteEditableInd"/>
    </CONDITION>
    <CONTAINER>
      <IMAGE IMAGE="UnlockedIcon" LABEL="Icon.Editable"/>
      <FIELD>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="editableNoteMessageOpt" />
       </CONNECT>
      </FIELD>
    </CONTAINER>
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="true"
    STYLE="cluster-rte-no-padding"
  >
    <FIELD
      HEIGHT="200"
    >
      <LABEL>
	    <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="noteTextFieldLabelOpt" />
        </CONNECT>
      </LABEL>
      <CONNECT>
        <SOURCE
            NAME="DISPLAY"
            PROPERTY="latestNoteTextForEdit"
          />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$details$participantNoteDetails$modifyNoteDetails$notesText"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="noteHistoryWithoutUserLinksOpt"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  
  </CLUSTER>


</VIEW>
