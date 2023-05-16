<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008-2010 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of       -->
<!-- Curam Software Ltd. ("Confidential Information"). You shall not        -->
<!-- disclose such Confidential Information and shall use it only in        -->
<!-- accordance with the terms of the license agreement you entered into    -->
<!-- with Curam Software.                                                   -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to view a list of rejection reasons.         -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_PARAMETER NAME="appealRelationshipID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="appealRelationshipID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$appealRelationshipKey$appealRelationshipID"
    />
  </CONNECT>


  <LIST>
    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.EditRejectionComment">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="AppealedCase_modifyRejectionComment"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="appealedCaseRejectionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="appealedCaseRejectionID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="contextDescription"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="description"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>
    
    <FIELD LABEL="Field.Title.User">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="userName"
        />
      </CONNECT>
      <!-- BEGIN, CR00200487, GP -->
      <LINK PAGE_ID="Organization_viewUserDetails">
        <!-- END, CR00200487 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="userName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    
    <FIELD LABEL="Field.Title.Date">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rejectionDate"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Title.Reason">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reasonCode"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Title.Comment">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reasonText"
        />
      </CONNECT>
    </FIELD>
  </LIST>
  
</VIEW>
