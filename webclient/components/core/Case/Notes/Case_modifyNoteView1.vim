<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012,2022. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c)  2010 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view for modifying case notes.  -->
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
    CLASS="Case"
    NAME="DISPLAY"
    OPERATION="readNote1"
  />


  <SERVER_INTERFACE
    CLASS="Case"
    NAME="ACTION"
    OPERATION="modifyNote1"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="caseNoteID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseNoteID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$key$caseNoteID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="key$key$key$caseNoteID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$details$key$caseNoteID"
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
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseID"
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
    
  <CLUSTER>

    <CLUSTER
      LABEL_WIDTH="18.5"
      STYLE="cluster-cpr-no-border"
    >
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


    <CLUSTER
      LABEL_WIDTH="37"
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        LABEL="Field.Label.Priority"
        WIDTH="60"
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
        WIDTH="30"
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


  <CLUSTER
    SHOW_LABELS="true"
    STYLE="cluster-rte-no-padding"
  >
  
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
    
    <FIELD HEIGHT="200" >
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
          PROPERTY="notesText"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  
  <!-- note history -->
  <CLUSTER
    SHOW_LABELS="true"
    STYLE="RemoveFieldPadding"
  >
	    <FIELD
	      HEIGHT="4"
	    >
	      <CONNECT>
	        <SOURCE
	          NAME="DISPLAY"
	          PROPERTY="noteHistoryWithoutUserLinks"
	        />
	      </CONNECT>
	    </FIELD>

  </CLUSTER> 
  
</CLUSTER>

</VIEW>
