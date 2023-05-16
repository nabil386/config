<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002-2009, 2010 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This is the included view used to display a list of client roles for an-->
<!-- integrated case.  -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <LIST TITLE="List.Title.ListOfParticipants">


    <ACTION_SET TYPE="LIST_ROW_MENU">


      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="InvestigationDelivery_modifyClientRoleFromList"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="participantDtls$caseParticipantRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernCaseRoleID"
            />
          </CONNECT>
          <!-- BEGIN, CR00121937, GD -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="participantDtls$participantRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleID"
            />
          </CONNECT>
          <!-- END, CR00121937 -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="contextDtls$description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="pageDescription"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


    </ACTION_SET>


    <CONTAINER
      LABEL="Field.Label.Name"
      WIDTH="60"
    >
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="participantDtls$name"
          />
        </CONNECT>
      </FIELD>
      <ACTION_CONTROL IMAGE="SpecialCautionDefaultIcon">
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="participantDtls$specialCautionInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="TRUE"
          PAGE_ID="SpecialCaution_listFromCase"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="participantDtls$participantRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleID"
            />
          </CONNECT>
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
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="participantDtls$typeCode"
        />
      </CONNECT>
    </FIELD>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="InvestigationDelivery_viewClientRole">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="participantDtls$caseParticipantRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernCaseRoleID"
          />
        </CONNECT>
        <!-- BEGIN, CR00121937, GD -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="participantDtls$participantRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <!-- END, CR00121937 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="contextDtls$description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


  </LIST>


</VIEW>
