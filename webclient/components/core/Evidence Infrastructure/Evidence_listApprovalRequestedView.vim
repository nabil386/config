<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2006-2008 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Evidence infrastructure page containing a list of approval requests.   -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CONTAINER
    ALIGNMENT="CENTER"
    LABEL="List.Title.Select"
    WIDTH="6"
  >


    <WIDGET TYPE="MULTISELECT">
      <WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="pendingApprovalList$dtls$evidenceDescriptorID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="pendingApprovalList$dtls$evidenceID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="pendingApprovalList$dtls$evidenceType"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="pendingApprovalList$dtls$correctionSetID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="pendingApprovalList$dtls$successionID"
          />
        </CONNECT>
      </WIDGET_PARAMETER>
      <WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="pendingApprovalList"
          />
        </CONNECT>
      </WIDGET_PARAMETER>
    </WIDGET>
  </CONTAINER>


  <FIELD
    LABEL="List.Title.Type"
    WIDTH="20"
  >
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="pendingApprovalList$dtls$evidenceType"
      />
    </CONNECT>
  </FIELD>
  <FIELD
    LABEL="List.Title.Name"
    WIDTH="20"
  >
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="pendingApprovalList$dtls$concernRoleName"
      />
    </CONNECT>
  </FIELD>
  <FIELD
    LABEL="List.Title.EffectiveDate"
    WIDTH="20"
  >
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="pendingApprovalList$dtls$effectiveFrom"
      />
    </CONNECT>
  </FIELD>
  <FIELD
    LABEL="List.Title.Details"
    WIDTH="40"
  >
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="pendingApprovalList$dtls$summary"
      />
    </CONNECT>
  </FIELD>


</VIEW>
