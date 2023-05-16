<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008, 2010-2011 Curam Software Ltd.                          -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows user to list all phone numbers belonging to a         -->
<!-- particular participant and allows user to create new phone numbers     -->
<!-- for a hearing for an integrated case.                                  -->
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


  <PAGE_PARAMETER NAME="concernRoleID"/>
  <PAGE_PARAMETER NAME="hearingID"/>
  <PAGE_PARAMETER NAME="caseParticipantRoleID"/>
  <PAGE_PARAMETER NAME="concernRoleName"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="hearingID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="hearingParticipantKey$hearingID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="concernRoleID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseParticipantRoleID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="hearingParticipantKey$caseParticipantRoleID"
    />
  </CONNECT>


  <ACTION_SET>
    <ACTION_CONTROL
      IMAGE="NewButton"
      LABEL="ActionControl.Label.NewPhoneNumber"
      TYPE="ACTION"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Appeal_createPhoneNumber"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="hearingParticipantKey$caseParticipantRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseParticipantRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="hearingParticipantKey$hearingID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="hearingID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="concernRoleName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleName"
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


    <ACTION_CONTROL
      IMAGE="CancelButton"
      LABEL="ActionControl.Label.Cancel"
    />
  </ACTION_SET>


  <LIST>
    <CONTAINER
      LABEL="Container.Label.Action"
      SEPARATOR="Container.Separator"
      WIDTH="12"
    >
      <ACTION_CONTROL LABEL="ActionControl.Label.Select">
        <LINK
          DISMISS_MODAL="false"
          OPEN_MODAL="false"
          PAGE_ID="Appeal_addPhoneNumber"
          SAVE_LINK="false"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="hearingParticipantKey$hearingID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="hearingID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="hearingParticipantKey$caseParticipantRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseParticipantRoleID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="concernRolePhoneNumberID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRolePhoneNumberID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="hearingParticipantKey$concernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
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
    </CONTAINER>


    <FIELD
      LABEL="Field.Label.AreaCode"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="areaCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.CountryCode"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="countryCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.PhoneNumber"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="phoneNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Extension"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="extension"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
