<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002-2003, 2009, 2010 Curam Software Ltd.                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view for modifying case notes.  -->
<?curam-deprecated Since Curam 6.0, replaced by ICMember_modifyNoteView1.vim?>
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
    CLASS="IntegratedCase"
    NAME="DISPLAY"
    OPERATION="readMemberNote"
  />


  <SERVER_INTERFACE
    CLASS="IntegratedCase"
    NAME="ACTION"
    OPERATION="modifyMemberNote"
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
      PROPERTY="integratedCaseMemberNoteID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseNoteID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="integratedCaseMemberNoteID"
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
      PROPERTY="participantID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="participantID"
    />
  </CONNECT>


  <CLUSTER>


    <CLUSTER
      LABEL_WIDTH="30"
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
    >


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


  <CLUSTER
    SHOW_LABELS="FALSE"
    TITLE="Cluster.Title.Comments"
  >
    <FIELD
      LABEL="Field.Label.Text"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="notesText"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
