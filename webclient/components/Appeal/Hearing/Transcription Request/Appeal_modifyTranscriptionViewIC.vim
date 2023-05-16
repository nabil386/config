<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2003, 2014. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2003, 2010-2011 Curam Software Ltd.                          -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of       -->
<!-- Curam Software Ltd. ("Confidential Information"). You shall not        -->
<!-- disclose such Confidential Information and shall use it only in        -->
<!-- accordance with the terms of the license agreement you entered into    -->
<!-- with Curam Software.                                                   -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows user to modify transcription requests for IC.         -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="Page.Title"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="HearingTranscription"
    NAME="DISPLAY"
    OPERATION="readRequest"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="HearingTranscription"
    NAME="ACTION"
    OPERATION="modifyRequest"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="contextDescription"/>
  <PAGE_PARAMETER NAME="hearingTranscriptionRequestID"/>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="hearingTranscriptionRequestID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="hearingTranscriptionRequestID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="hearingTranscriptionRequestID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="hearingTranscriptionRequestID"
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
      PROPERTY="requestedByID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="requestedByID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="attachmentVersion"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="attachmentVersion"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="55"
    NUM_COLS="2"
  >
    <FIELD LABEL="Field.Label.DateRequested">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateRequested"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dateRequested"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DueDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateDue"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dateDue"
        />
      </CONNECT>
    </FIELD>


    <FIELD CONTROL="SKIP"/>


    <FIELD LABEL="Field.Label.DocumentReference">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fileReferenceNumber"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fileReferenceNumber"
        />
      </CONNECT>
    </FIELD>


    <CONTAINER LABEL="Field.Label.AttachedFile">
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="attachmentName"
          />
        </CONNECT>
      </FIELD>


      <ACTION_CONTROL
        LABEL="ActionControl.Label.View"
        TYPE="FILE_DOWNLOAD"
      >
        <LINK>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="attachmentID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="attachmentID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


    <FIELD LABEL="Field.Label.DateSentToTranscriber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateSentToTranscriber"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dateSentToTranscriber"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DateSentToRequester">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateSentToRequester"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dateSentToRequester"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DateReceivedFromTranscriber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateReceivedFromTranscriber"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="dateReceivedFromTranscriber"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DocumentLocation">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fileLocation"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fileLocation"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    DESCRIPTION="Cluster.Description.AttachFile"
    LABEL_WIDTH="20"
    TITLE="Cluster.Title.AttachFile"
  >
    <WIDGET
      LABEL="Field.Label.File"
      TYPE="FILE_UPLOAD"
    >
      <WIDGET_PARAMETER NAME="CONTENT">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="attachmentContents"
          />
        </CONNECT>
      </WIDGET_PARAMETER>
      <WIDGET_PARAMETER NAME="FILE_NAME">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="attachmentName"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="attachmentName"
          />
        </CONNECT>
      </WIDGET_PARAMETER>
    </WIDGET>
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00407812, RB -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00407812 -->
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
