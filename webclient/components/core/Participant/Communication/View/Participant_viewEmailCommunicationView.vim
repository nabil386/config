<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2004, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2004, 2009-2011 Curam Software Ltd.                          -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The page to display the details of an email communication.             -->
<!-- BEGIN, CR00237256, NS -->
<?curam-deprecated Since Curam 6.0, replaced with Participant_viewEmailCommunicationView1.vim. See release note: CR00237256?>
<!-- END, CR00237256 -->
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
    CLASS="Communication"
    NAME="DISPLAY"
    OPERATION="readEmail"
  />


  <PAGE_PARAMETER NAME="communicationID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="communicationID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$communicationID"
    />
  </CONNECT>


  <CLUSTER TITLE="Cluster.Title.CommunicationText">


    <CLUSTER
      LABEL_WIDTH="25"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD LABEL="Field.Label.CommunicationText">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="emailText"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Correspondent"
  >


    <FIELD LABEL="Field.Label.CorrespondentName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="correspondentName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.CorrespondentType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="correspondentType"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EmailAddress">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="emailAddress"
        />
      </CONNECT>
    </FIELD>
    <FIELD CONTROL="SKIP"/>


  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.CommunicationType"
  >


    <FIELD LABEL="Field.Label.FileLocation">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fileLocation"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DocumentLocation">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="documentLocation"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.FileReference">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fileReference"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DocumentReference">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="documentReference"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00416277, VT -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00416277 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
