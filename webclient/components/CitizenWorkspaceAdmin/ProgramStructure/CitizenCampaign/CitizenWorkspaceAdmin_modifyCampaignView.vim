<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->  

<!-- ====================================================================== -->
<!-- Copyright 2010 Curam Software Ltd.                                     -->
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
    <SERVER_INTERFACE CLASS="CitizenCampaign" NAME="DISPLAY" OPERATION="readCitizenCampaignDetails" />
    <SERVER_INTERFACE CLASS="CitizenCampaign" NAME="ACTION" OPERATION="modifyCampaignDetails" PHASE="ACTION" />
    <PAGE_PARAMETER NAME="citizenCampaignID" />
    <CONNECT>
        <SOURCE NAME="PAGE" PROPERTY="citizenCampaignID" />
        <TARGET NAME="DISPLAY" PROPERTY="key$citizenCampaignID" />
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="PAGE" PROPERTY="citizenCampaignID" />
        <TARGET NAME="ACTION" PROPERTY="citizenCampaignID" />
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="versionNo" />
        <TARGET NAME="ACTION" PROPERTY="versionNo" />
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="creoleRulesetName" />
        <TARGET NAME="ACTION" PROPERTY="creoleRulesetName" />
    </CONNECT>
    <CLUSTER  LABEL_WIDTH="45">
        <FIELD LABEL="Field.Title.Name">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="name" />
            </CONNECT>
        </FIELD>
        <FIELD LABEL="Field.Title.Ruleset">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="creoleRulesetName" />
            </CONNECT>
        </FIELD>
        <FIELD LABEL="Field.Title.ExpiryDateTime">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="expiryDateTime" />
            </CONNECT>
            <CONNECT>
                <TARGET NAME="ACTION" PROPERTY="expiryDateTime" />
            </CONNECT>
        </FIELD>
    </CLUSTER>
</VIEW>
