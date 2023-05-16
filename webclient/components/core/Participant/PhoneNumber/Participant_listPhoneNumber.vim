<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2002-2010, 2012 Curam Software Ltd.                          -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This Page lists all Phone Numbers belong to a particular participant   -->
<!-- and contains links to view edit and create Phone Numbers               -->
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
    CLASS="Participant"
    NAME="DISPLAY"
    OPERATION="listPhoneNumber"
  />


  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="ISDUPLICATE"
    OPERATION="isParticipantDuplicate"
  />


  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="informationalMsgDtlsList$dtls$informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>


  <PAGE_PARAMETER NAME="concernRoleID"/>


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
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="ISDUPLICATE"
      PROPERTY="concernRoleID"
    />
  </CONNECT>


  <ACTION_SET BOTTOM="false">


    <ACTION_CONTROL
      IMAGE="NewButton"
      LABEL="ActionControl.Label.New"
    >
      <CONDITION>
        <IS_FALSE
          NAME="ISDUPLICATE"
          PROPERTY="statusInd"
        />
      </CONDITION>
      <!-- BEGIN, CR00141707, MC -->
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Participant_createPhoneNumber"
      >
        <!-- END, CR00141707 -->
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


  </ACTION_SET>


  <LIST>


    <ACTION_SET TYPE="LIST_ROW_MENU">


      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">


        <CONDITION>
          <IS_FALSE
            NAME="ISDUPLICATE"
            PROPERTY="statusInd"
          />
        </CONDITION>
        <!-- BEGIN, CR00141707, MC -->
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Participant_modifyPhoneNumberFromList"
        >
          <!-- END, CR00141707 -->
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
        </LINK>


      </ACTION_CONTROL>


      <ACTION_CONTROL
        IMAGE="DeleteButton"
        LABEL="ActionControl.Label.Delete"
      >
        <CONDITION>
          <IS_FALSE
            NAME="ISDUPLICATE"
            PROPERTY="statusInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Participant_cancelPhoneNumber"
        >
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
              PROPERTY="concernRolePhoneNumberVersionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRolePhoneNumberVersionNo"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="phoneNumberVersionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="phoneNumberVersionNo"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <DETAILS_ROW>


      <INLINE_PAGE PAGE_ID="Participant_viewPhoneNumber">
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
      </INLINE_PAGE>


    </DETAILS_ROW>


    <FIELD
      LABEL="Field.Label.Primary"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="primaryInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="17"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="typeCode"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00305982, IBM -->
    <CONTAINER LABEL="Field.Label.PhoneNumber">
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="phoneCountryCode"
          />
        </CONNECT>
      </FIELD>


      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="phoneAreaCode"
          />
        </CONNECT>
      </FIELD>


      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="phoneNumber"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>
    <!-- END, CR00305982 -->


    <FIELD
      LABEL="Field.Label.Extension"
      WIDTH="12"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="phoneExtension"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.From"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.To"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="9"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="statusCode"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
