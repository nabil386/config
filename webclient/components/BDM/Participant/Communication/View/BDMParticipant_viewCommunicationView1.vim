<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2011, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2011 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for the record communication details.                -->
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
    CLASS="BDMCommunication"
    NAME="DISPLAY"
    OPERATION="readRecordedCommunicationAndCaseMember"
  />
  
  <SERVER_INTERFACE
    CLASS="BDMCommunication"
    NAME="EXTERNALFILELINK"
    OPERATION="retrieveExternalFileLink"
  />


  <PAGE_PARAMETER NAME="communicationID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>
  <PAGE_PARAMETER NAME="correspondentConcernRoleID"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="communicationID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="recordedCommKey$communicationID"
    />
  </CONNECT>
  
    <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="communicationID"
    />
    <TARGET
      NAME="EXTERNALFILELINK"
      PROPERTY="recordedCommKey$communicationID"
    />
  </CONNECT>
  
  <CLUSTER
    LABEL_WIDTH="25"
    TITLE="Cluster.Title.CommunicationText"
  >


    <FIELD LABEL="Field.Label.CommunicationText">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="communicationText"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >
    <FIELD LABEL="Field.Label.CaseMember">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="clientConcernRoleName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Method">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="methodTypeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD CONTROL="SKIP"/>


    <FIELD LABEL="Field.Label.CommunicationType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="communicationTypeCode"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="50"
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


    <FIELD LABEL="Field.Label.Address">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="formattedAddressData"
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
      <!-- BEGIN, CR00100449, GSP -->
    </FIELD>
    <FIELD LABEL="Field.Label.CorrespondentType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="correspondentType"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00100449 -->
    <FIELD LABEL="Field.Label.PhoneNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="phoneNumberString"
        />
      </CONNECT>
    </FIELD>
    <FIELD CONTROL="SKIP"/>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TITLE="Cluster.Title.CommunicationType"
  >

<!-- TASK 91944 remove widget -->

		
		<FIELD LABEL="Field.Label.Name" >
    <LINK URI_SOURCE_NAME="EXTERNALFILELINK"
				URI_SOURCE_PROPERTY="fileLocationURL"  OPEN_NEW= "true"/>
      <CONNECT>
        <SOURCE NAME="EXTERNALFILELINK" PROPERTY="fileName" />
      </CONNECT>
    </FIELD>
<!-- TASK 91944 remove widget -->



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
    <FIELD CONTROL="SKIP"/>
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
