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
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd" POPUP_PAGE="true">
    <PAGE_TITLE>
        <CONNECT>
            <SOURCE NAME="TEXT" PROPERTY="PageTitle.StaticText1" />
        </CONNECT>
    </PAGE_TITLE>
    <SERVER_INTERFACE CLASS="LocalizableText" NAME="DISPLAY" OPERATION="viewLocalizableText" />
    <PAGE_PARAMETER NAME="localizableTextID" />
    <CONNECT>
        <SOURCE NAME="PAGE" PROPERTY="localizableTextID" />
        <TARGET NAME="DISPLAY" PROPERTY="key$localizableTextID" />
    </CONNECT>
    <LIST TITLE="List.Title.Translations">
        <CONTAINER LABEL="Container.Label.Action" WIDTH="15">
            <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
                <LINK PAGE_ID="LocalizableText_resolveModifyTextTranslation" OPEN_MODAL="true" WINDOW_OPTIONS="width=800">
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="textTranslationID" />
                        <TARGET NAME="PAGE" PROPERTY="textTranslationID" />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="richTextInd" />
                        <TARGET NAME="PAGE" PROPERTY="richTextInd" />
                    </CONNECT>
                </LINK>
            </ACTION_CONTROL>
        </CONTAINER>
        <FIELD LABEL="Field.Title.Language" WIDTH="20">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="localeCode" />
            </CONNECT>
        </FIELD>
        <FIELD LABEL="Field.Title.Text">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="richText" />
            </CONNECT>
        </FIELD>
    </LIST>
</VIEW>
