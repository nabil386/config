<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2008,2019. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->

<!-- Description -->
<!-- =========== -->
<!-- This page allows user to modify phone contacts for integrated cases.   -->
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
    CLASS="HearingPhoneContact"
    NAME="ACTION"
    OPERATION="modifyPhoneContact"
    PHASE="ACTION"
  />


  <SERVER_INTERFACE
    CLASS="HearingPhoneContact"
    NAME="DISPLAY"
    OPERATION="readForModify"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="contextDescription"/>
  <PAGE_PARAMETER NAME="hearingPhoneContactID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="hearingPhoneContactID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="hearingPhoneContactID"
    />
  </CONNECT>


  <!-- BEGIN, CR00093627, RKi -->
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="phoneStartDate"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="phoneStartDate"
    />
  </CONNECT>
  <!-- END, CR00093627 -->


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="hearingPhoneContactID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="hearingPhoneContactID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
  >
    <FIELD
      LABEL="Field.Label.PhoneCountryCode"
      WIDTH="55"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="phoneCountryCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="phoneCountryCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.PhoneAreaCode"
      WIDTH="55"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="phoneAreaCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="phoneAreaCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Phone">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="phoneNumber"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="phoneNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.PhoneExt"
      WIDTH="55"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="phoneExtension"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="phoneExtension"
        />
      </CONNECT>
    </FIELD>


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
