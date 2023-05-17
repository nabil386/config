<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2009, 2010 Curam Software Ltd.                                  -->
<!-- All rights reserved.                                                          -->
<!-- This software is the confidential and proprietary information of Curam        -->
<!-- Software, Ltd. (&quot;Confidential Information&quot;). You shall not disclose -->
<!-- such Confidential Information and shall use it only in accordance with        -->
<!-- the terms of the license agreement you entered into with Curam                -->
<!-- Software.                                                                     -->
<!-- Description                                                                   -->
<!-- ===========                                                                   -->
<!-- This page lists attachments off a contact log  record                         -->
<VIEW>
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText1"
      />
    </CONNECT>
    <!-- BEGIN, CR00148040, ELG -->


    <!-- END, CR00148040 -->
  </PAGE_TITLE>


  <MENU MODE="IN_PAGE_NAVIGATION">
    <ACTION_CONTROL
      LABEL="ActionControl.Label.MenuNarrative"
      STYLE="in-page-link"
    >
      <LINK
        PAGE_ID="CaseContactLog_viewNarrativeLucene"
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
        <CONNECT>
          <SOURCE
            NAME="PAGE"
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
	      PROPERTY="narrativeSearchText"
	    />
	    <TARGET
	      NAME="PAGE"
	      PROPERTY="narrativeSearchText"
	    />
		</CONNECT>
		  
		<CONNECT>
		  <SOURCE
		    NAME="PAGE"
		    PROPERTY="resultInNarrativeInd"
		  />
		  <TARGET
		    NAME="PAGE"
		    PROPERTY="resultInNarrativeInd"
		  />
	   </CONNECT>
      </LINK>
    </ACTION_CONTROL>
    <ACTION_CONTROL
      LABEL="ActionControl.Label.MenuContactLogDetails"
      STYLE="in-page-link"
    >
      <LINK
        PAGE_ID="Case_viewContactLog1"
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
        <CONNECT>
          <SOURCE
            NAME="PAGE"
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
	      PROPERTY="narrativeSearchText"
	    />
	    <TARGET
	      NAME="PAGE"
	      PROPERTY="narrativeSearchText"
	    />
		</CONNECT>
		  
		<CONNECT>
		  <SOURCE
		    NAME="PAGE"
		    PROPERTY="resultInNarrativeInd"
		  />
		  <TARGET
		    NAME="PAGE"
		    PROPERTY="resultInNarrativeInd"
		  />
	   </CONNECT>
      </LINK>
    </ACTION_CONTROL>
    <ACTION_CONTROL
      LABEL="ActionControl.Label.MenuParticipants"
      STYLE="in-page-link"
    >
      <LINK
        PAGE_ID="CaseContactLog_listAttendee"
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
        <CONNECT>
          <SOURCE
            NAME="PAGE"
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
	      PROPERTY="narrativeSearchText"
	    />
	    <TARGET
	      NAME="PAGE"
	      PROPERTY="narrativeSearchText"
	    />
		</CONNECT>
		  
		<CONNECT>
		  <SOURCE
		    NAME="PAGE"
		    PROPERTY="resultInNarrativeInd"
		  />
		  <TARGET
		    NAME="PAGE"
		    PROPERTY="resultInNarrativeInd"
		  />
	   </CONNECT>
      </LINK>
    </ACTION_CONTROL>
    <ACTION_CONTROL
      LABEL="ActionControl.Label.MenuAttachments"
      STYLE="in-page-current-link"
    >
      <LINK
        PAGE_ID="CaseContactLog_listAttachment1"
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
	      PROPERTY="narrativeSearchText"
	    />
	    <TARGET
	      NAME="PAGE"
	      PROPERTY="narrativeSearchText"
	    />
		</CONNECT>
		  
		<CONNECT>
		  <SOURCE
		    NAME="PAGE"
		    PROPERTY="resultInNarrativeInd"
		  />
		  <TARGET
		    NAME="PAGE"
		    PROPERTY="resultInNarrativeInd"
		  />
	   </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </MENU>


  <!-- BEGIN, CR00160632, Joao.Costa -->
  <!-- BEGIN, CR00148040, ELG -->
  <SERVER_INTERFACE
    CLASS="Case"
    NAME="DISPLAY"
    OPERATION="readCaseContactLogAttachments"
  />
  <!-- END, CR00148040 -->
  <!-- END, CR00160632 -->


  <!-- BEGIN, CR00148040, ELG -->
  <PAGE_PARAMETER NAME="contactLogID"/>
  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="narrativeSearchText"/>
  <PAGE_PARAMETER NAME="resultInNarrativeInd"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="contactLogID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$contactLogID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$caseID"
    />
  </CONNECT>
  <!-- END, CR00148040 -->


  <LIST>


    <DETAILS_ROW>


      <INLINE_PAGE PAGE_ID="Attachment_view">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="attachmentLinkDetails$attachmentLinkDtls$attachmentLinkID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="attachmentLinkID"
          />
        </CONNECT>
      </INLINE_PAGE>


    </DETAILS_ROW>


    <ACTION_SET TYPE="LIST_ROW_MENU">


      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Attachment_modify"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="attachmentLinkDetails$attachmentLinkDtls$attachmentLinkID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="attachmentLinkID"
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
          PAGE_ID="Attachment_cancel"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="attachmentLinkDetails$attachmentLinkDtls$attachmentLinkID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="attachmentLinkID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$description$description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="description"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="attachmentLinkDtls$versionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="versionNo"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


    </ACTION_SET>


    <FIELD
      LABEL="Field.Label.Description"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="attachmentLinkDetails$attachmentLinkDtls$description"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Date"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="attachmentLinkDetails$attachmentDtls$receiptDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="attachmentLinkDetails$attachmentLinkDtls$recordStatus"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
