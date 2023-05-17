<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2008, 2010, 2011 Curam Software Ltd.                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="IssueAppealProcess"
    NAME="DISPLAY"
    OPERATION="listIssueProcess"
    PHASE="DISPLAY"
  />


  <ACTION_SET BOTTOM="false">
    <ACTION_CONTROL
      IMAGE="NewButton"
      LABEL="ActionControl.Label.New"
      TYPE="ACTION"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Issue_createAppealProcess"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="issueConfigurationID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="issueConfigurationID"
          />
        </CONNECT>


        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="description"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>


  <PAGE_PARAMETER NAME="issueConfigurationID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="issueConfigurationID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseTypeID"
    />
  </CONNECT>


  <LIST>
    <ACTION_SET TYPE="LIST_ROW_MENU">
      
      <ACTION_CONTROL
        IMAGE="NewStage"
        LABEL="ActionControl.Label.NewStage"
        TYPE="ACTION"
        >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Issue_createAppealStage"
          >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="appealProcessID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="appealProcessID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="description"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="issueConfigurationID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="issueConfigurationID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
     
      <ACTION_CONTROL
        IMAGE="DeleteButton"
        LABEL="ActionControl.Label.Delete"
        TYPE="ACTION"
        >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Issue_removeAppealProcess"
          >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="appealProcessID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="appealProcessID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="description"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="issueConfigurationID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="issueConfigurationID"
            />
          </CONNECT>
          
          
        </LINK>
      </ACTION_CONTROL>
      
    </ACTION_SET>
    
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Issue_viewAppealProcess">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="appealProcessID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="appealProcessID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="description"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="issueConfigurationID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="issueConfigurationID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>
    
    
    <FIELD LABEL="Field.Label.StartDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>
    
    
    <FIELD LABEL="Field.Label.FirstStage">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="firstStage"
        />
      </CONNECT>
    </FIELD>
    
    
    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="status"
        />
      </CONNECT>
    </FIELD>
  </LIST>
</VIEW>
