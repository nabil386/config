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
            <SOURCE NAME="TEXT" PROPERTY="PageTitle.StaticText1" />
        </CONNECT>
    </PAGE_TITLE>
    <SERVER_INTERFACE CLASS="CitizenWorkspaceAdmin" NAME="DISPLAY" OPERATION="readApplicationCategory" />
    <SERVER_INTERFACE CLASS="CitizenWorkspaceAdmin" NAME="ACTION" OPERATION="modifyApplicationCategory" PHASE="ACTION" />
    <PAGE_PARAMETER NAME="applicationCategoryID" />
    <CONNECT>
        <SOURCE NAME="PAGE" PROPERTY="applicationCategoryID" />
        <TARGET NAME="DISPLAY" PROPERTY="key$applicationCategoryID" />
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="PAGE" PROPERTY="applicationCategoryID" />
        <TARGET NAME="ACTION" PROPERTY="applicationCategoryID" />
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="versionNo" />
        <TARGET NAME="ACTION" PROPERTY="versionNo" />
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="nameTextID" />
        <TARGET NAME="ACTION" PROPERTY="nameTextID" />
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="descriptionTextID" />
        <TARGET NAME="ACTION" PROPERTY="descriptionTextID" />
    </CONNECT>
    <CLUSTER LABEL_WIDTH="35" NUM_COLS="2">
        <FIELD LABEL="Field.Title.Name">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="name" />
            </CONNECT>
            <CONNECT>
                <TARGET NAME="ACTION" PROPERTY="name" />
            </CONNECT>
        </FIELD>
        <FIELD LABEL="Field.Title.URL">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="url" />
            </CONNECT>
            <CONNECT>
                <TARGET NAME="ACTION" PROPERTY="url" />
            </CONNECT>
        </FIELD>
    </CLUSTER>
    <CLUSTER TITLE="Cluster.Title.Description" SHOW_LABELS="false">
        <FIELD HEIGHT="200">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="description" />
            </CONNECT>
            <CONNECT>
                <TARGET NAME="ACTION" PROPERTY="description" />
            </CONNECT>
        </FIELD>
    </CLUSTER>
</VIEW>
