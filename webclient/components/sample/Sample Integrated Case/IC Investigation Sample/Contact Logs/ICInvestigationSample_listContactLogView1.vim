<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2009, 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright  2009 Curam Software Ltd.                                 -->
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
    CLASS="IntegratedCase"
    NAME="DISPLAY"
    OPERATION="listInvestigationDeliveryContactLog1"
  />


  <!-- BEGIN, CR00342633, AC-->
  <SERVER_INTERFACE
    CLASS="Case"
    NAME="PREVIEW"
    OPERATION="previewCaseContactLogDetails"
    PHASE="ACTION"
  />
  <!-- END, CR00342633-->


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
    ALIGNMENT="LEFT"
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
      <LINK PAGE_ID="Case_searchContactLog1">
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
      <!-- BEGIN, CR00357485, AC-->
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Case_previewContactLogView"
      >
        <!-- END, CR00357485-->
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


        <!-- BEGIN, CR00342633, AC-->
        <CONNECT>
          <SOURCE
            NAME="PREVIEW"
            PROPERTY="previewCaseContactLogIDKey$dtls$multiSelectStr"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="multiSelectStr"
          />
        </CONNECT>
        <!-- END, CR00342633-->


      </LINK>
    </ACTION_CONTROL>


  </ACTION_SET>
  <LIST>


    <!-- BEGIN, CR00119886, MC -->
    <CONTAINER WIDTH="5">
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


          <!-- BEGIN, CR00342633, AC-->
          <CONNECT>
            <TARGET
              NAME="PREVIEW"
              PROPERTY="previewCaseContactLogIDKey$dtls$multiSelectStr"
            />
          </CONNECT>
          <!-- END, CR00342633-->


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
              NAME="DISPLAY"
              PROPERTY="description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="description"
            />
          </CONNECT>
          <!-- BEGIN, CR00141933, CW -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
          <!-- END, CR00141933 -->
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
        >
          <!-- END, CR00119240 -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
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
            <!-- BEGIN, CR00148040, ELG -->
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="description"
            />
            <!-- END, CR00148040 -->
            <TARGET
              NAME="PAGE"
              PROPERTY="description"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL
        IMAGE="AddAttendeeButton"
        LABEL="ActionControl.Label.AddAttendee"
      >


        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Case_addContactLogAttendee1"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
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
      WIDTH="20"
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
      WIDTH="13"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDateTime"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.EndDateTime"
      WIDTH="12"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDateTime"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Purpose"
      WIDTH="15"
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
      WIDTH="15"
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
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="method"
        />
      </CONNECT>
    </FIELD>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Case_viewContactLog1">
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
