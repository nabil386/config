<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2009 - 2011 Curam Software Ltd.                          -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- View containing details of a case audit for the auditors view          -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <ACTION_SET>
    <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
      <LINK
        OPEN_MODAL="TRUE"
        PAGE_ID="CaseAudit_modifyCaseAudit"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseAuditID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseAuditID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.AddFindings">
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="result$showAddFindingsLinkInd"
        />
      </CONDITION>
      <LINK
        OPEN_MODAL="TRUE"
        PAGE_ID="CaseAudit_createCaseAuditFinding"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseAuditID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseAuditID"
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
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseAuditReference"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseAuditReference"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      APPEND_ELLIPSIS="false"
      LABEL="ActionControl.Label.ViewFindings"
    >
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="result$showViewFindingsLinkInd"
        />
      </CONDITION>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="CaseAudit_viewCaseAuditFinding"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseAuditID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseAuditID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.ModifyFindings">
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="result$showModifyFindingsLinkInd"
        />
      </CONDITION>
      <LINK
        OPEN_MODAL="TRUE"
        PAGE_ID="CaseAudit_modifyCaseAuditFinding"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseAuditID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseAuditID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.CompleteFindings">
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="result$showCompleteFindingsInd"
        />
      </CONDITION>
      <LINK
        OPEN_MODAL="TRUE"
        PAGE_ID="CaseAudit_confirmCompleteFindings"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseAuditID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseAuditID"
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
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL LABEL="ActionControl.Label.CompleteCaseAudit">
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="result$showCompleteAuditInd"
        />
      </CONDITION>
      <LINK
        OPEN_MODAL="TRUE"
        PAGE_ID="CaseAudit_confirmCompleteAudit"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseAuditID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseAuditID"
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
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>


</VIEW>
