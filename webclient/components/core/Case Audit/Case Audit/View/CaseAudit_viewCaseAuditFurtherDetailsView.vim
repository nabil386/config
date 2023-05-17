<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2009, 2010 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- View containing information common between the auditors and            -->
<!-- coordinators view of a case audit                                      -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <LIST TITLE="List.Title.FocusAreasFindings">


    <ACTION_SET TYPE="LIST_ROW_MENU">


      <ACTION_CONTROL LABEL="ActionControl.Label.AddFinding">
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="result$focusAreaFindings$dtls$showAddFAFindingInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="TRUE"
          PAGE_ID="CaseAudit_addFocusAreaFinding"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$focusAreaFindings$dtls$focusAreaFindingID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="focusAreaFindingID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.EditFinding">
        <CONDITION>
          <IS_FALSE
            NAME="DISPLAY"
            PROPERTY="result$focusAreaFindings$dtls$showAddFAFindingInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="TRUE"
          PAGE_ID="CaseAudit_modifyFocusAreaFinding"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$focusAreaFindings$dtls$focusAreaFindingID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="focusAreaFindingID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.AddAttachment">
        <LINK
          OPEN_MODAL="TRUE"
          PAGE_ID="FocusAreaFinding_createAttachment"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$focusAreaFindings$dtls$focusAreaFindingID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="focusAreaFindingID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>
    <FIELD
      LABEL="List.Title.FocusArea"
      WIDTH="60"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$focusAreaFindings$dtls$focusAreaText"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.FocusAreaSatisfied"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$focusAreaFindings$dtls$focusAreaSatisfied"
        />
      </CONNECT>
    </FIELD>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="CaseAudit_viewFocusAreaFinding">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$focusAreaFindings$dtls$focusAreaFindingID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="focusAreaFindingID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


  </LIST>


  <CLUSTER
    NUM_COLS="1"
    SHOW_LABELS="FALSE"
    TITLE="Cluster.Comments.Title"
  >
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
