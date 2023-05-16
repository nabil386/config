<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2007, 2010 Curam Software Ltd.                                         -->
<!-- All rights reserved.                                                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose        -->
<!-- such Confidential Information and shall use it only in accordance with   -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                                                                  -->
<!-- Description                                                                                                -->
<!-- ===========                                                                                    -->
<!-- The included view to modify a resolution.                                                -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.Title"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="IssueDelivery"
    NAME="DISPLAY"
    OPERATION="listActiveResolutionsForIssue"
  />


  <SERVER_INTERFACE
    CLASS="IssueDelivery"
    NAME="ACTION"
    OPERATION="modifyResolution"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="issueConfigurationID"/>
  <PAGE_PARAMETER NAME="resolutionID"/>
  <PAGE_PARAMETER NAME="description"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="resolutionID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="resolutionID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="issueConfigurationID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="issueConfigurationID"
    />
  </CONNECT>


  <ACTION_SET ALIGNMENT="CENTER">


    <ACTION_CONTROL
      IMAGE="SaveButton"
      LABEL="ActionControl.Label.Save"
      TYPE="SUBMIT"
    >
      <LINK
        PAGE_ID="IssueDelivery_home"
        SAVE_LINK="false"
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
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      IMAGE="CancelButton"
      LABEL="ActionControl.Label.Cancel"
    >
      <LINK
        PAGE_ID="IssueDelivery_home"
        SAVE_LINK="false"
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
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>


  <CLUSTER LABEL_WIDTH="27">

    <FIELD
      LABEL="Field.Label.Resolution"
      WIDTH="65"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="resolutionConfigurationID"
          NAME="DISPLAY"
          PROPERTY="resolution"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="resolutionConfigurationID"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
