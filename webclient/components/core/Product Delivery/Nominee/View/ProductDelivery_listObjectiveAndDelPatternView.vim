<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2010 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view to display a list all objectives for a case and      -->
<!-- their current delivery pattern.                                        -->
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
    CLASS="Case"
    NAME="DISPLAY"
    OPERATION="listObjectiveAndDelPattern"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
  </CONNECT>


  <LIST>


    <ACTION_SET TYPE="LIST_ROW_MENU">


      <ACTION_CONTROL LABEL="ActionControl.Label.Assign">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="ProductDelivery_assignObjectiveToNominee1"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="rulesObjectiveID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="rulesObjectiveID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


    </ACTION_SET>


    <FIELD
      LABEL="Field.Label.Component"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="objectiveCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Nominee"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="concernRoleName"
        />
      </CONNECT>
      <LINK PAGE_ID="Participant_resolveRoleHome">
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
            NAME="DISPLAY"
            PROPERTY="concernRoleType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="participantType"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <FIELD
      LABEL="Field.Label.DeliveryPattern"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="deliveryPatternName"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="ProductDelivery_viewDeliveryPattern1"
        SAVE_LINK="true"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="productDeliveryPatternInfoID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="productDeliveryPatternInfoID"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="ProductDelivery_listComponentAssignmentHistoryNoNavigation">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="rulesObjectiveID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="rulesObjectiveID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


  </LIST>


</VIEW>
