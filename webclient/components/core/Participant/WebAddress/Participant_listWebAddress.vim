<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2006 - 2011 Curam Software Ltd.                              -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This Page lists all Web Addresses belong to a particular participant   -->
<!-- and contains links to view edit and create Web addresses.              -->
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
    OPERATION="listWebAddress"
  />


  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="ISDUPLICATE"
    OPERATION="isParticipantDuplicate"
  />


  <!-- BEGIN, CR00123062, DJ -->
  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="result$messagelist$dtls$informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>


  <!-- END CR00123062 -->


  <PAGE_PARAMETER NAME="concernRoleID"/>


  <!-- Map concernRoleID parameter to display-phase bean -->
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


  <!-- An action set containing the single new button. -->
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
        PAGE_ID="Participant_createWebAddress"
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
        <!-- Link to page displaying editable details of the web address-->


        <CONDITION>
          <IS_FALSE
            NAME="ISDUPLICATE"
            PROPERTY="statusInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Participant_modifyWebAddressFromList"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="concernRoleWebAddressID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleWebAddressID"
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
          PAGE_ID="Participant_cancelWebAddress"
        >
          <!-- Map parameters in order to delete correct Web Address -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="concernRoleWebAddressID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleWebAddressID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="webAddressVersionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="webAddressVersionNo"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <DETAILS_ROW>


      <INLINE_PAGE PAGE_ID="Participant_viewWebAddress">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="concernRoleWebAddressID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleWebAddressID"
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
          PROPERTY="primaryWebAddressInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.WebAddress"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="webAddress"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="typeCode"
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
      WIDTH="10"
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
