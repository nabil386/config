<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008-2010 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- This is view Wait List Entry details Page                              -->
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
    <CONNECT>
      <SOURCE
        NAME="PAGE"
        PROPERTY="contextDescription"
      />
    </CONNECT>
  </PAGE_TITLE>


  <PAGE_PARAMETER NAME="waitListEntryID"/>
  <PAGE_PARAMETER NAME="contextDescription"/>
  <PAGE_PARAMETER NAME="fundName"/>
  <PAGE_PARAMETER NAME="caseRefernceNo"/>
  <PAGE_PARAMETER NAME="createDate"/>
  <!-- BEGIN, CR00200567, AS -->
  <PAGE_PARAMETER NAME="fundFiscalYearID"/>
  <PAGE_PARAMETER NAME="fundFiscalYearName"/>
  <PAGE_PARAMETER NAME="participantName"/>
  <PAGE_PARAMETER NAME="position"/>
  <PAGE_PARAMETER NAME="priority"/>
  <PAGE_PARAMETER NAME="expiryDate"/>
  <PAGE_PARAMETER NAME="reviewDate"/>
  <!-- END, CR00200567 -->


  <SERVER_INTERFACE
    CLASS="MaintainFundWaitList"
    NAME="DISPLAY"
    OPERATION="viewWaitListEntry"
    PHASE="DISPLAY"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="waitListEntryID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$waitListEntryID"
    />
  </CONNECT>
  <!-- BEGIN, CR00205389, AS -->
  <MENU MODE="IN_PAGE_NAVIGATION">
    <ACTION_CONTROL
      LABEL="ActionControl.Label.Home"
      STYLE="in-page-current-link"
    >
      <LINK PAGE_ID="FundPM_viewWaitListEntry">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="waitListEntryID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="waitListEntryID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="fundFiscalYearName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="fundFiscalYearName"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="participantName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="participantName"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="position"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="position"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="priority"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="priority"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="expiryDate"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="expiryDate"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="reviewDate"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="reviewDate"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
    <ACTION_CONTROL
      LABEL="ActionControl.Label.History"
      STYLE="in-paget-link"
    >
      <LINK PAGE_ID="FundPM_listWaitListEntryStatusHistory">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="waitListEntryID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="waitListEntryID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="fundFiscalYearName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="fundFiscalYearName"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="participantName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="participantName"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="position"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="position"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="priority"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="priority"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="expiryDate"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="expiryDate"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="reviewDate"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="reviewDate"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


  </MENU>
  <!-- END, CR00205389 -->
  <CLUSTER NUM_COLS="2">


    <FIELD LABEL="Field.Label.WaitListEntry.RemovalReason">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="removalReason"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.WaitListEntry.Comments">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
