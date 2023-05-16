<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2008-2010 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page is used to display a list of Contact Logs for a case         -->
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
    OPERATION="listContactLog"
  />


  <SERVER_INTERFACE
    CLASS="Case"
    NAME="PREVIEW"
    OPERATION="previewCaseContactLogs"
    PHASE="ACTION"
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


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="PREVIEW"
      PROPERTY="caseID"
    />
  </CONNECT>
  <ACTION_SET
    ALIGNMENT="CENTER"
    BOTTOM="true"
  >
    <ACTION_CONTROL
      IMAGE="NewButton"
      LABEL="ActionControl.Label.New"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="CaseContactLogWizard_createContact"
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
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="description"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      IMAGE="SearchButton"
      LABEL="ActionControl.Label.Search"
    >
      <LINK PAGE_ID="Case_searchContactLog">
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
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="description"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      IMAGE="PreviewButton"
      LABEL="ActionControl.Label.Preview"
      TYPE="SUBMIT"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Case_previewContactLogs"
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
            NAME="PREVIEW"
            PROPERTY="multiSelectStr"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="multiSelectStr"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


  </ACTION_SET>


  <LIST>


    <!-- BEGIN, CR00119886, MC -->
    <CONTAINER>
      <WIDGET TYPE="MULTISELECT">
        <WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="contactLogID"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
        <WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
          <CONNECT>
            <TARGET
              NAME="PREVIEW"
              PROPERTY="multiSelectStr"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
      </WIDGET>
    </CONTAINER>


    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Case_modifyContactLogFromList"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="contactLogID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="contactLogID"
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
      <ACTION_CONTROL
        IMAGE="DeleteButton"
        LABEL="ActionControl.Label.Delete"
      >
        <!-- BEGIN, CR00119240, MC -->
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Case_cancelContactLog"
          SAVE_LINK="false"
        >
          <!-- END, CR00119240 -->
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
              PROPERTY="contactLogID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="contactLogID"
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
    <!-- END, CR00119886 -->


    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="18"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="contactLogType"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.StartDateTime"
      WIDTH="16"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDateTime"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Purpose"
      WIDTH="28"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="purpose"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Location"
      WIDTH="14"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="location"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Method"
      WIDTH="14"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="method"
        />
      </CONNECT>
    </FIELD>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Case_viewContactLog">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="contactLogID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="contactLogID"
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
      </INLINE_PAGE>
    </DETAILS_ROW>
  </LIST>


</VIEW>
