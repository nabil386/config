<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2005, 2010 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of       -->
<!-- Curam Software Ltd. ("Confidential Information"). You shall not        -->
<!-- disclose such Confidential Information and shall use it only in        -->
<!-- accordance with the terms of the license agreement you entered into    -->
<!-- with Curam Software.                                                   -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows user to list hearing participants.                    -->
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


  <PAGE_PARAMETER NAME="hearingID"/>
  <PAGE_PARAMETER NAME="contextDescription"/>


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


  <LIST>
    <CONTAINER
      LABEL="Container.Label.Action"
      SEPARATOR="Container.Separator"
      WIDTH="20"
    >
      <ACTION_CONTROL LABEL="ActionControl.Label.Select">


        <LINK PAGE_ID="Appeal_listParticipantPhoneNumbersNonIC">


          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="concernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
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
              PROPERTY="caseParticipantRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseParticipantRoleID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="concernRoleName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="CONSTANT"
              PROPERTY="SingleMode"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="scheduleConfigMode"
            />
          </CONNECT>


        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


    <FIELD LABEL="Field.Title.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="concernRoleName"
        />
      </CONNECT>
      <!-- BEGIN, CR00198790, GP -->
      <LINK PAGE_ID="Appeal_resolveParticipantHome">
        <!-- END, CR00198790 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseParticipantRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseParticipantRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="concernRoleName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleName"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD LABEL="Field.Title.Type">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseParticipantRoleType"
        />
      </CONNECT>
    </FIELD>


  </LIST>


  <ACTION_SET
    ALIGNMENT="CENTER"
    TOP="false"
  >
    <ACTION_CONTROL
      IMAGE="PreviousButton"
      LABEL="ActionControl.Label.Previous"
    >
      <LINK
        PAGE_ID="Appeal_listPhoneNumber"
        SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="hearingID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="hearingID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>


</VIEW>
