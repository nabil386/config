<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
 
  Copyright IBM Corporation 2012, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2011-2012 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for a list of communications for an investigation    -->
<!-- delivery on an integrated case.                                        -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <ACTION_SET TYPE="LIST_ROW_MENU">


    <ACTION_CONTROL LABEL="ActionControl.Label.Edit">


      <!--BEGIN CR00080805, GBA-->
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Participant_resolveModifyCommunicationForConcernOnly1"
      >
        <!--END CR00080805-->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="communicationID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="communicationID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="correspondentConcernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="correspondentConcernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$communicationDtls$concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="communicationStatus"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="status"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="communicationFormat"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="communicationFormat"
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


    <ACTION_CONTROL LABEL="ActionControl.Label.SendNow">
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="draftEmailInd"
        />
      </CONDITION>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Participant_sendEmail"
        SAVE_LINK="true"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="communicationID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="communicationID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="subjectText"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="subject"
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


    <ACTION_CONTROL
      LABEL="ActionControl.Label.OpenAttachment"
      TYPE="FILE_DOWNLOAD"
    >
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="msWordInd"
        />
      </CONDITION>
      <!-- BEGIN, CR00246099, AK -->
      <LINK>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="communicationID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="communicationID"
          />
        </CONNECT>
      </LINK>
      <!-- END, CR00246099 -->
    </ACTION_CONTROL>
    <ACTION_CONTROL
      LABEL="ActionControl.Label.Preview"
      TYPE="FILE_DOWNLOAD"
    >
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="proFormaInd"
        />
      </CONDITION>
      <LINK>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="communicationID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="communicationID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      IMAGE="DeleteButton"
      LABEL="ActionControl.Label.Delete"
    >


      <LINK
        OPEN_MODAL="true"
        SAVE_LINK="true"
        URI_SOURCE_NAME="DISPLAY"
        URI_SOURCE_PROPERTY="deletePageName"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="communicationID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="communicationID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="correspondentConcernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="correspondentConcernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="versionNo"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="versionNo"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="subjectText"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="subject"
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


</VIEW>
