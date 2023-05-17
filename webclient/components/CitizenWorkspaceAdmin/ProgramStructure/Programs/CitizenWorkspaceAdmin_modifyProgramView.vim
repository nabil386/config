<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->  

<!-- ====================================================================== -->
<!-- Copyright (c) 2008 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- ====================================================================== -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">
    <PAGE_TITLE>
        <CONNECT>
            <SOURCE NAME="TEXT" PROPERTY="PageTitle.StaticText1"/>
        </CONNECT>
    </PAGE_TITLE>
    <SERVER_INTERFACE CLASS="CitizenWorkspaceAdmin" NAME="DISPLAY" OPERATION="viewProgram"/>
    <SERVER_INTERFACE CLASS="CitizenWorkspaceAdmin" NAME="ACTION" OPERATION="modifyProgram" PHASE="ACTION"/>
    <PAGE_PARAMETER NAME="programTypeID"/>
    <CONNECT>
        <SOURCE NAME="PAGE" PROPERTY="programTypeID"/>
        <TARGET NAME="DISPLAY" PROPERTY="key$programTypeID"/>
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="PAGE" PROPERTY="programTypeID"/>
        <TARGET NAME="ACTION" PROPERTY="programTypeID"/>
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="details$dtls$versionNo"/>
        <TARGET NAME="ACTION" PROPERTY="versionNo"/>
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$details$dtls$nameTextID"/>
        <TARGET NAME="ACTION" PROPERTY="nameTextID"/>
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="summaryTextID"/>
        <TARGET NAME="ACTION" PROPERTY="summaryTextID"/>
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="details$dtls$descriptionTextID"/>
        <TARGET NAME="ACTION" PROPERTY="descriptionTextID"/>
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$details$pdfFormProgramLinkVersionNo"/>
        <TARGET NAME="ACTION" PROPERTY="details$pdfFormProgramLinkVersionNo"/>
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="url"/>
        <TARGET NAME="ACTION" PROPERTY="url"/>
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="multipleAppIndicator"/>
        <TARGET NAME="ACTION" PROPERTY="multipleAppIndicator"/>
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$details$pdfFormID"/>
        <TARGET NAME="ACTION" PROPERTY="details$pdfFormID"/>
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="summary"/>
        <TARGET NAME="ACTION" PROPERTY="summary"/>
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="details$description"/>
        <TARGET NAME="ACTION" PROPERTY="description"/>
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="localOfficeInd"/>
        <TARGET NAME="ACTION" PROPERTY="localOfficeInd"/>
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="localOfficeDescription"/>
        <TARGET NAME="ACTION" PROPERTY="localOfficeDescription"/>
    </CONNECT>
    
    <CLUSTER LABEL_WIDTH="30">
        <FIELD LABEL="Field.Title.Name">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="details$name"/>
            </CONNECT>
            <CONNECT>
                <TARGET NAME="ACTION" PROPERTY="name"/>
            </CONNECT>
        </FIELD>
        <FIELD LABEL="Field.Title.Reference" >
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="programTypeReference"/>
            </CONNECT>
            <CONNECT>
                <TARGET NAME="ACTION" PROPERTY="programTypeReference"/>
            </CONNECT>
        </FIELD>
        <FIELD USE_BLANK="true" LABEL="Field.Title.TargetSystem">
            <CONNECT>
                <INITIAL NAME="DISPLAY" PROPERTY="systemName" HIDDEN_PROPERTY="systemID" />
            </CONNECT>
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="result$details$targetSystemID"/>
            </CONNECT>
            <CONNECT>
                <TARGET NAME="ACTION" PROPERTY="details$targetSystemID"/>
            </CONNECT>
        </FIELD>
        <FIELD CONTROL="SKIP"/>
        <FIELD LABEL="Field.Title.CuramCaseType" USE_BLANK="true">
            <CONNECT>
                <INITIAL HIDDEN_PROPERTY="listAdminIntegratedCaseDetails$dtls$integratedCaseType"
                    NAME="DISPLAY" PROPERTY="listAdminIntegratedCaseDetails$dtls$integratedCaseType"
                />
            </CONNECT>
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="details$dtls$integratedCaseType"/>
            </CONNECT>
            <CONNECT>
                <TARGET NAME="ACTION" PROPERTY="integratedCaseType"/>
            </CONNECT>
        </FIELD>
        <FIELD WIDTH_UNITS="CHARS" WIDTH="4" LABEL="Field.Title.DispositionPeriod">
            <CONNECT>
               <SOURCE NAME="DISPLAY" PROPERTY="result$details$dtls$numDispositionDays"/> 
            </CONNECT>
            <CONNECT>
                <TARGET NAME="ACTION" PROPERTY="dtls$numDispositionDays"/>
            </CONNECT>
        </FIELD>
        <FIELD CONTROL="SKIP"/>
    </CLUSTER>
    <CLUSTER TITLE="Cluster.Title.Description" SHOW_LABELS="false">
        <FIELD HEIGHT="100">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="details$internalDescription"/>
            </CONNECT>
            <CONNECT>
                <TARGET NAME="ACTION" PROPERTY="internalDescription"/>
            </CONNECT>
        </FIELD>
    </CLUSTER>
</VIEW>
