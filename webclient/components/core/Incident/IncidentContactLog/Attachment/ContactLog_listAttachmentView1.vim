<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2009, 2010 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                             -->
<!-- This software is the confidential and proprietary information of Curam           -->
<!-- Software, Ltd. (&quot;Confidential Information&quot;). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with           -->
<!-- the terms of the license agreement you entered into with Curam                   -->
<!-- Software.                                                                        -->
<!-- Description                                                                      -->
<!-- ===========                                                                      -->
<!-- This page lists attachments off a incident contact log  record                   -->
<VIEW>
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE NAME="TEXT" PROPERTY="PageTitle.StaticText1"/>
    </CONNECT>
    <CONNECT>
      <SOURCE NAME="PAGE" PROPERTY="pageDescription"/>
    </CONNECT>
  </PAGE_TITLE>


  <MENU MODE="IN_PAGE_NAVIGATION">
    <ACTION_CONTROL LABEL="ActionControl.Label.MenuNarrative" STYLE="in-page-link">
      <LINK PAGE_ID="IncidentContactLog_viewNarrativeLucene" SAVE_LINK="false">
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="incidentID"/>
          <TARGET NAME="PAGE" PROPERTY="incidentID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="contactLogID"/>
          <TARGET NAME="PAGE" PROPERTY="contactLogID"/>
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
    <ACTION_CONTROL LABEL="ActionControl.Label.MenuContactLog" STYLE="in-page-link">
      <LINK PAGE_ID="IncidentContactLog_viewContactLog1" SAVE_LINK="false">
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="contactLogID"/>
          <TARGET NAME="PAGE" PROPERTY="contactLogID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="incidentID"/>
          <TARGET NAME="PAGE" PROPERTY="incidentID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="pageDescription"/>
          <TARGET NAME="PAGE" PROPERTY="pageDescription"/>
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
    <ACTION_CONTROL LABEL="ActionControl.Label.MenuParticipants" STYLE="in-page-link">
      <LINK PAGE_ID="IncidentContactLog_listAttendee" SAVE_LINK="false">
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="incidentID"/>
          <TARGET NAME="PAGE" PROPERTY="incidentID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="contactLogID"/>
          <TARGET NAME="PAGE" PROPERTY="contactLogID"/>
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
    <ACTION_CONTROL LABEL="ActionControl.Label.MenuAttachments" STYLE="in-page-current-link">
      <LINK PAGE_ID="ContactLog_listAttachment1" SAVE_LINK="false">
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="contactLogID"/>
          <TARGET NAME="PAGE" PROPERTY="contactLogID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="incidentID"/>
          <TARGET NAME="PAGE" PROPERTY="incidentID"/>
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


  <SERVER_INTERFACE CLASS="ContactLog" NAME="DISPLAY" OPERATION="readContactLogAttachments"/>


  <PAGE_PARAMETER NAME="incidentID"/>
  <PAGE_PARAMETER NAME="contactLogID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>
  <PAGE_PARAMETER NAME="narrativeSearchText"/>
  <PAGE_PARAMETER NAME="resultInNarrativeInd"/>


  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="contactLogID"/>
    <TARGET NAME="DISPLAY" PROPERTY="contactLogID"/>
  </CONNECT>


  <LIST>


    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK OPEN_MODAL="true" PAGE_ID="Attachment_modify">
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="attachmentLinkDetails$attachmentLinkDtls$attachmentLinkID"/>
            <TARGET NAME="PAGE" PROPERTY="attachmentLinkID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="pageDescription"/>
            <TARGET NAME="PAGE" PROPERTY="description"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL IMAGE="DeleteButton" LABEL="ActionControl.Label.Delete">
        <LINK OPEN_MODAL="true" PAGE_ID="Attachment_cancel" WINDOW_OPTIONS="width=400">
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="attachmentLinkDetails$attachmentLinkDtls$attachmentLinkID"/>
            <TARGET NAME="PAGE" PROPERTY="attachmentLinkID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="pageDescription"/>
            <TARGET NAME="PAGE" PROPERTY="description"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="attachmentLinkDetails$attachmentLinkDtls$versionNo"/>
            <TARGET NAME="PAGE" PROPERTY="versionNo"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


    </ACTION_SET>


    <FIELD LABEL="Field.Label.Description" WIDTH="50">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="attachmentLinkDetails$attachmentLinkDtls$description"/>
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Date" WIDTH="25">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="attachmentLinkDetails$attachmentDtls$receiptDate"/>
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Status" WIDTH="25">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="attachmentLinkDetails$attachmentLinkDtls$recordStatus"/>
      </CONNECT>
    </FIELD>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Attachment_view">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="attachmentLinkDetails$attachmentLinkDtls$attachmentLinkID"/>
          <TARGET NAME="PAGE" PROPERTY="attachmentLinkID"/>
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


  </LIST>


</VIEW>