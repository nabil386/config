<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2009, 2022. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright  2009, 2010 Curam Software Ltd.                           -->
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

  <!-- BEGIN, CR00407986, AC-->
  <SERVER_INTERFACE
    CLASS="Case"
    NAME="DISPLAY"
    OPERATION="listCaseContactLogs"
  />
  <!-- END, CR00407986-->

  <!-- BEGIN, CR00342633, AC-->
  <SERVER_INTERFACE
    CLASS="Case"
    NAME="PREVIEW"
    OPERATION="previewCaseContactLogDetails"
    PHASE="ACTION"
  />
  <!-- END, CR00342633-->

  <SERVER_INTERFACE
    CLASS="Case"
    NAME="DISPLAY_SUBJECT"
    OPERATION="checkContactLogSubjectEnabled"
    PHASE="DISPLAY"
  />

  <PAGE_PARAMETER NAME="caseID"/>

  <!-- BEGIN, CR00407986, AC-->
  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>
  <!-- END, CR00407986-->
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

  <MENU MODE="IN_PAGE_NAVIGATION">
    <ACTION_CONTROL
      LABEL="ActionControl.Label.MenuListContactLog"
      STYLE="in-page-current-link"
    >
      <LINK
        PAGE_ID="Case_listContactLog1"
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
      LABEL="ActionControl.Label.MenuSearchContactLog"
      STYLE="in-page-link"
    >
      <LINK
        PAGE_ID="Case_resolveContactLogSearchPage"
        SAVE_LINK="true"
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
  </MENU>

  <ACTION_SET
    ALIGNMENT="LEFT"
    BOTTOM="TRUE"
  >
    <ACTION_CONTROL
      IMAGE="NewButton"
      LABEL="ActionControl.Label.New"
    >
      <!--BEGIN, CR00225856, NRK-->
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="CaseContactLogWizard_createContact"
      >
        <!--END, CR00225856, NRK-->
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
        <!--END, CR00342633-->

      </LINK>
    </ACTION_CONTROL>

  </ACTION_SET>

  <!-- One version of the list contains a contact log subject column -->
  <LIST>
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY_SUBJECT"
        PROPERTY="result$contactLogSubjectEnabledInd"
      />
    </CONDITION>
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

      <ACTION_CONTROL LABEL="ActionControl.Label.AddAttendee">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Case_addContactLogAttendee1"
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
              PROPERTY="contactLogID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="contactLogID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>

      <ACTION_CONTROL LABEL="ActionControl.Label.AddAttachment">
        <!-- BEGIN, CR00407868, SG -->
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="CaseContactLog_addAttachment"
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
          <!-- END, CR00407868 -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="contactLogID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="linkID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>

      <ACTION_CONTROL
        IMAGE="DeleteButton"
        LABEL="ActionControl.Label.Delete"
      >
        <!-- BEGIN, CR00119240, MC -->
        <!-- BEGIN, CR00386535, VT -->
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Case_cancelContactLog"
          SAVE_LINK="true"
        >
          <!-- END, CR00386535 -->
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
              PROPERTY="versionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="versionNo"
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
      <!-- END, CR00119886 -->
    </ACTION_SET>

    <!-- BEGIN, CR00342633, AC-->
    <!-- BEGIN, CR00119886, MC -->
    <CONTAINER WIDTH="5">
      <WIDGET
        HAS_CONFIRM_PAGE="true"
        TYPE="MULTISELECT"
      >
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
              PROPERTY="previewCaseContactLogIDKey$dtls$multiSelectStr"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
      </WIDGET>
    </CONTAINER>
    <!--END, CR00342633-->

	<FIELD
      LABEL="Field.Label.Subject"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtlsList$dtls$subject"
        />
      </CONNECT>
    </FIELD>
    
    <!-- The following field gets mapped and displayed as an icon-->
    <FIELD WIDTH="5" >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="narrativeEditStatusOpt"
        />
      </CONNECT>
    </FIELD>

    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="12"
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
      WIDTH="12"
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
      WIDTH="14"
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
      WIDTH="12"
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
      WIDTH="12"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="method"
        />
      </CONNECT>
    </FIELD>

    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="CaseContactLog_viewNarrativeLucene">
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
        <!-- subjectRichText is blank on the list page, so passing this into             -->
        <!-- CaseContactLog_viewNarrativeLucene as a page parameter out of convenience   -->
        <!-- as we're reusing this page to view the narrative from the list page row     -->
        <!-- elements and the narrativeSearchText needs to be blank in this circumstance -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="subjectRichText"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="narrativeSearchText"
          />
        </CONNECT>
        <!-- resultInNarrativeIndOpt is false on the list page, so passing this into -->
        <!-- CaseContactLog_viewNarrativeLucene as a page parameter as we're reusing -->
        <!-- this page to view the narrative from the list page row elements and the -->
        <!-- resultInNarrativeInd needs to be false in this circumstance             -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="resultInNarrativeIndOpt"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="resultInNarrativeInd"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>
  </LIST>
 
  <!-- One version of the list does not contain a contact log subject column -->
  <LIST>
    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY_SUBJECT"
        PROPERTY="result$contactLogSubjectEnabledInd"
      />
    </CONDITION>
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

      <ACTION_CONTROL LABEL="ActionControl.Label.AddAttendee">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Case_addContactLogAttendee1"
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
              PROPERTY="contactLogID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="contactLogID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>

      <ACTION_CONTROL LABEL="ActionControl.Label.AddAttachment">
        <!-- BEGIN, CR00407868, SG -->
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="CaseContactLog_addAttachment"
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
          <!-- END, CR00407868 -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="contactLogID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="linkID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>

      <ACTION_CONTROL
        IMAGE="DeleteButton"
        LABEL="ActionControl.Label.Delete"
      >
        <!-- BEGIN, CR00119240, MC -->
        <!-- BEGIN, CR00386535, VT -->
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Case_cancelContactLog"
          SAVE_LINK="true"
        >
          <!-- END, CR00386535 -->
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
              PROPERTY="versionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="versionNo"
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
      <!-- END, CR00119886 -->
    </ACTION_SET>

    <!-- BEGIN, CR00342633, AC-->
    <!-- BEGIN, CR00119886, MC -->
    <CONTAINER WIDTH="5">
      <WIDGET
        HAS_CONFIRM_PAGE="true"
        TYPE="MULTISELECT"
      >
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
              PROPERTY="previewCaseContactLogIDKey$dtls$multiSelectStr"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
      </WIDGET>
    </CONTAINER>
    <!--END, CR00342633-->

    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="12"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="contactLogType"
        />
      </CONNECT>
    </FIELD>
    
    <!-- The following field gets mapped and displayed as an icon-->
    <FIELD WIDTH="5" >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="narrativeEditStatusOpt"
        />
      </CONNECT>
    </FIELD>

    <FIELD
      LABEL="Field.Label.StartDateTime"
      WIDTH="12"
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
      WIDTH="14"
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
      WIDTH="12"
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
      WIDTH="12"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="method"
        />
      </CONNECT>
    </FIELD>

    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="CaseContactLog_viewNarrativeLucene">
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
        <!-- subjectRichText is blank on the list page, so passing this into             -->
        <!-- CaseContactLog_viewNarrativeLucene as a page parameter out of convenience   -->
        <!-- as we're reusing this page to view the narrative from the list page row     -->
        <!-- elements and the narrativeSearchText needs to be blank in this circumstance -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="subjectRichText"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="narrativeSearchText"
          />
        </CONNECT>
        <!-- resultInNarrativeIndOpt is false on the list page, so passing this into -->
        <!-- CaseContactLog_viewNarrativeLucene as a page parameter as we're reusing -->
        <!-- this page to view the narrative from the list page row elements and the -->
        <!-- resultInNarrativeInd needs to be false in this circumstance             -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="resultInNarrativeIndOpt"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="resultInNarrativeInd"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>
  </LIST>

</VIEW>
