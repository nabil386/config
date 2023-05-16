<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- ====================================================================== -->
<!-- Copyright 2011 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ====================================================================== -->
<!-- View page for creating a note.                                         -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="QuickNotes"
    NAME="ACTION"
    OPERATION="createQuickNotes"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="relatedID"/>
  <PAGE_PARAMETER NAME="quickNotesType"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="relatedID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="relatedID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="quickNotesType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="quickNotesDetails$quickNotesdtls$type"
    />
  </CONNECT>


  <CLUSTER>
    <CLUSTER
      LABEL_WIDTH="17.5"
      STYLE="cluster-cpr-no-border"
    >
      <FIELD LABEL="Field.Label.Subject">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="quickNotesDetails$noteDtls$subjectText"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


    <CLUSTER
      LABEL_WIDTH="35"
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
    >
      <FIELD
        LABEL="Field.Label.Priority"
        WIDTH="50"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="quickNotesDetails$noteDtls$priorityCode"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.Sensitivity"
        WIDTH="20"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="quickNotesDetails$noteDtls$sensitivityCode"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="FALSE"
    STYLE="cluster-rte-no-padding"
  >
    <FIELD
      HEIGHT="200"
      LABEL="Field.Label.Text"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="quickNotesDetails$noteDtls$notesText"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
