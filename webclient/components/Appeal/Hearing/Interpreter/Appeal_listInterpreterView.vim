<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008, 2010 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of       -->
<!-- Curam Software Ltd. ("Confidential Information"). You shall not        -->
<!-- disclose such Confidential Information and shall use it only in        -->
<!-- accordance with the terms of the license agreement you entered into    -->
<!-- with Curam Software.                                                   -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The page allows user to view a list of interpreters.                   -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="HearingInterpreter"
    NAME="DISPLAY"
    OPERATION="list"
    PHASE="DISPLAY"
  />


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="Page.Title"
      />
    </CONNECT>
  </PAGE_TITLE>


  <PAGE_PARAMETER NAME="hearingID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="hearingID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="hearingID"
    />
  </CONNECT>


  <ACTION_SET BOTTOM="false">


    <ACTION_CONTROL
      IMAGE="NewButton"
      LABEL="ActionControl.Label.NewInterpreter"
      TYPE="ACTION"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Appeal_createInterpreter"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="hearingID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="hearingID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="contextDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


  </ACTION_SET>


  <LIST>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Appeal_viewInterpreter">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="hearingServiceSupplierID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="hearingServiceSupplierID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="contextDescription"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL
        LABEL="ActionControl.Label.Edit"
        TYPE="ACTION"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Appeal_modifyInterpreterFromList"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="hearingServiceSupplierID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="hearingServiceSupplierID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="contextDescription"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <FIELD LABEL="Field.Label.Name">


      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fullName"
        />
      </CONNECT>


      <!-- BEGIN, CR00198761, GP -->
      <LINK PAGE_ID="Appeal_resolveParticipantOrUserHome">
        <!-- END, CR00198761 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="supplierType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="supplierType"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="interpreterID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="interpreterID"
          />
        </CONNECT>
      </LINK>


    </FIELD>
    <FIELD LABEL="Field.Label.Attendance">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="participatedCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="recordStatus"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
