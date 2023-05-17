<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
 
  Copyright IBM Corporation 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <ACTION_SET TYPE="LIST_ROW_MENU">


    <ACTION_CONTROL LABEL="ActionControl.Label.View">
      <LINK
        URI_SOURCE_NAME="DISPLAY"
        URI_SOURCE_PROPERTY="result$dtls$requestURI"
      />
    </ACTION_CONTROL>


    <!--
    <ACTION_CONTROL
      LABEL="ActionControl.Label.View"
      TYPE="FILE_DOWNLOAD"
    >
      <LINK>
      <CONNECT>
        <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$requestID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="requestID"
          />
          </CONNECT>
      </LINK>
    </ACTION_CONTROL>
-->


    <ACTION_CONTROL
      IMAGE="DeleteButton"
      LABEL="ActionControl.Label.Delete"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="CaseOnlineAppealRequests_delete"
        SAVE_LINK="true"
      >


        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$dtls$appealCaseLinkID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="linkID"
          />
        </CONNECT>


      </LINK>
    </ACTION_CONTROL>


  </ACTION_SET>


</VIEW>
