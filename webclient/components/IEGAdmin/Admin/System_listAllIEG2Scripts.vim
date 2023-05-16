<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->  
<!-- ====================================================================== -->
<!--                                                                        -->
<!-- Copyright (c) 2008-2011 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!--                                                                        -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Page used to list all of the available IEG scripts.                    -->
<!--                                                                        -->
<!-- ====================================================================== -->
<?curam-deprecated Since Curam 6.0.5.0, replaced by IEG2_listAllIEG2Scripts.uim?>
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">
    <SERVER_INTERFACE CLASS="IEGScriptAdmin" NAME="DISPLAY" OPERATION="readAllScriptsOmitConfigTypes" PHASE="DISPLAY" />
    <ACTION_SET BOTTOM="false">
        <ACTION_CONTROL LABEL="ActionControl.Label.CreateScript">
            <LINK OPEN_MODAL="true" PAGE_ID="System_createScript"/>
        </ACTION_CONTROL>
        <ACTION_CONTROL LABEL="ActionControl.Label.ImportScript">
            <LINK OPEN_MODAL="true" PAGE_ID="System_importScript"/>
        </ACTION_CONTROL>
    </ACTION_SET>
    <LIST>
        <ACTION_SET TYPE="LIST_ROW_MENU">
            <ACTION_CONTROL LABEL="ActionControl.Label.Edit" APPEND_ELLIPSIS="true">
                <LINK PAGE_ID="System_editScript">
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="scriptID" />
                        <TARGET NAME="PAGE" PROPERTY="scriptID" />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="scriptType" />
                        <TARGET NAME="PAGE" PROPERTY="scriptType" />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="scriptVersion" />
                        <TARGET NAME="PAGE" PROPERTY="scriptVersion" />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="name" />
                        <TARGET NAME="PAGE" PROPERTY="name" />
                    </CONNECT>
                </LINK>
            </ACTION_CONTROL>
            <ACTION_CONTROL LABEL="ActionControl.Label.Run" APPEND_ELLIPSIS="true">
                <LINK PAGE_ID="System_runScript">
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="scriptID" />
                        <TARGET NAME="PAGE" PROPERTY="scriptID" />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="scriptType" />
                        <TARGET NAME="PAGE" PROPERTY="scriptType" />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="scriptVersion" />
                        <TARGET NAME="PAGE" PROPERTY="scriptVersion" />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="name" />
                        <TARGET NAME="PAGE" PROPERTY="name" />
                    </CONNECT>
                </LINK>
            </ACTION_CONTROL>
            <ACTION_CONTROL LABEL="ActionControl.Label.RunInModal" APPEND_ELLIPSIS="true">
                <LINK PAGE_ID="System_runScriptModal" OPEN_MODAL="true" WINDOW_OPTIONS="width=800,height=600">
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="scriptID" />
                        <TARGET NAME="PAGE" PROPERTY="scriptID" />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="scriptType" />
                        <TARGET NAME="PAGE" PROPERTY="scriptType" />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="scriptVersion" />
                        <TARGET NAME="PAGE" PROPERTY="scriptVersion" />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="name" />
                        <TARGET NAME="PAGE" PROPERTY="name" />
                    </CONNECT>
                </LINK>
            </ACTION_CONTROL>			
            <ACTION_CONTROL LABEL="ActionControl.Label.Validate" APPEND_ELLIPSIS="true">
                <LINK PAGE_ID="System_validateScript" OPEN_MODAL="true">
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="scriptID" />
                        <TARGET NAME="PAGE" PROPERTY="scriptID" />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="scriptType" />
                        <TARGET NAME="PAGE" PROPERTY="scriptType" />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="scriptVersion" />
                        <TARGET NAME="PAGE" PROPERTY="scriptVersion" />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="name" />
                        <TARGET NAME="PAGE" PROPERTY="name" />
                    </CONNECT>
                </LINK>
            </ACTION_CONTROL>
            <ACTION_CONTROL LABEL="ActionControl.Label.Export" TYPE="FILE_DOWNLOAD" APPEND_ELLIPSIS="true">
                <LINK>
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="scriptID" />
                        <TARGET NAME="PAGE" PROPERTY="scriptID" />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="scriptType" />
                        <TARGET NAME="PAGE" PROPERTY="scriptType" />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="scriptVersion" />
                        <TARGET NAME="PAGE" PROPERTY="scriptVersion" />
                    </CONNECT>
                </LINK>
            </ACTION_CONTROL>
            <ACTION_CONTROL LABEL="ActionControl.Label.Remove" APPEND_ELLIPSIS="true">
                <LINK OPEN_MODAL="true" PAGE_ID="System_deleteScript">
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="scriptID" />
                        <TARGET NAME="PAGE" PROPERTY="scriptID" />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="scriptType" />
                        <TARGET NAME="PAGE" PROPERTY="scriptType" />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="scriptVersion" />
                        <TARGET NAME="PAGE" PROPERTY="scriptVersion" />
                    </CONNECT>
                </LINK>
            </ACTION_CONTROL>
        </ACTION_SET>
        <FIELD LABEL="Field.Title.ScriptName" WIDTH="35">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="name" />
            </CONNECT>
        </FIELD>
        <FIELD LABEL="Field.Title.ScriptID" WIDTH="35">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="scriptID" />
            </CONNECT>
        </FIELD>
        <FIELD LABEL="Field.Title.ScriptType" WIDTH="20">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="scriptType" />
            </CONNECT>
        </FIELD>
        <FIELD LABEL="Field.Title.ScriptVersion" WIDTH="10">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="scriptVersion" />
            </CONNECT>
        </FIELD>
    </LIST>
</VIEW>
