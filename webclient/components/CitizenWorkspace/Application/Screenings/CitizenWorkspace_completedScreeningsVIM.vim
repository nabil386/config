<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2013,2014. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">    
    <LIST TITLE="Completed.List.Title" DESCRIPTION="Completed.List.Description">
        <FIELD LABEL="Field.Title.Application">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="completedScreeningDtls$applicationName"/>
            </CONNECT>
        </FIELD>
        <FIELD LABEL="Field.Title.DateCreated">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="completedScreeningDtls$startDate"/>
            </CONNECT>
        </FIELD>
        <FIELD LABEL="Field.Title.LastUpdated">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="completedScreeningDtls$lastUpdated"/>
            </CONNECT>
        </FIELD>
        <CONTAINER STYLE="right-align" SEPARATOR="Link.Separator">
            <ACTION_CONTROL LABEL="ActionControl.Label.Rescreen">
                <CONDITION>
                    <IS_TRUE NAME="DISPLAY" PROPERTY="completedScreeningDtls$allowRescreening"/>
                </CONDITION>
                <LINK URI_SOURCE_NAME="DISPLAY" URI_SOURCE_PROPERTY="completedScreeningDtls$rescreeningURL"/>
            </ACTION_CONTROL>
            <ACTION_CONTROL LABEL="ActionControl.Label.ViewResults">
                <LINK URI_SOURCE_NAME="DISPLAY" URI_SOURCE_PROPERTY="completedScreeningDtls$viewResultsURL"/>
            </ACTION_CONTROL>
            <ACTION_CONTROL LABEL="ActionControl.Label.Delete">
                <LINK PAGE_ID="CitizenWorkspace_deleteScreening" OPEN_MODAL="true">
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="completedScreeningDtls$iegExecutionID"/>
                        <TARGET NAME="PAGE" PROPERTY="iegExecutionID"/>
                    </CONNECT>
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="completedScreeningDtls$applicationName"/>
                        <TARGET NAME="PAGE" PROPERTY="applicationName"/>
                    </CONNECT>
                </LINK>
            </ACTION_CONTROL>
        </CONTAINER>
    </LIST>
</VIEW>