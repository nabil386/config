<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010 Curam Software Ltd.                                        -->
<!-- All rights reserved.                                                      -->
<!-- This software is the confidential and proprietary information of Curam    -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose       -->
<!-- such Confidential Information and shall use it only in accordance with    -->
<!-- in accordance with the terms of the license agreement you entered into    -->
<!-- Software.                                                                 -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">
    <PAGE_TITLE>
        <CONNECT>
            <SOURCE NAME="TEXT" PROPERTY="Page.Title" />
        </CONNECT>
    </PAGE_TITLE>
    <PAGE_PARAMETER NAME="outcomePlanActionID" />
    <SERVER_INTERFACE CLASS="ParticipantComment" NAME="DISPLAY" OPERATION="listCommentsByRelatedObject" PHASE="DISPLAY" />
    <LIST>
        <FIELD LABEL="Field.Label.Comments" WIDTH="60">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="comments" />
            </CONNECT>
        </FIELD>
        <FIELD LABEL="Field.Label.Status" WIDTH="40">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="statusText" />
            </CONNECT>
        </FIELD>
    </LIST>
</VIEW>
