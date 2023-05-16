<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
 
  Copyright IBM Corporation 2008,2019. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- This page allows a user to modify an announcement.                     -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.Title"
      />
    </CONNECT>


  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="Organization"
    NAME="DISPLAY"
    OPERATION="readAnnouncement"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="Organization"
    NAME="ACTION"
    OPERATION="modifyAnnouncement"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="announcementID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="announcementID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$announcementID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="announcementID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="announcementID"
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
      PROPERTY="recordStatus"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="recordStatus"
    />
  </CONNECT>


  <CLUSTER LABEL_WIDTH="17.5">
    <FIELD 
      LABEL="Field.Label.Title"
      WIDTH="100"
     >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="title"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="title"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
  >
    <FIELD
      LABEL="Field.Label.StartDateTime"

    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDateTime"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDateTime"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
  >
    <FIELD
      LABEL="Field.Label.EndDateTime"

    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDateTime"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDateTime"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER LABEL_WIDTH="17.5">


    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Text"
      WIDTH="96"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="announcementText"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="announcementText"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >


    <!-- BEGIN, CR00406866, VT -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
      WIDTH="97"
    >
      <!-- END, CR00406866 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
