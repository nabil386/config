<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2013,2014. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>    
    <LIST TITLE="List.Title" DESCRIPTION="List.Description">
        <FIELD LABEL="Field.Title.Application">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="screeningDtls$applicationName" />
            </CONNECT>
        </FIELD>
        <FIELD LABEL="Field.Title.DateCreated">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="screeningDtls$startDate" />
            </CONNECT>
        </FIELD>
        <FIELD LABEL="Field.Title.LastUpdated">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="screeningDtls$lastUpdated" />
            </CONNECT>
        </FIELD>
        <CONTAINER SEPARATOR="Link.Separator">
            <FIELD>
                <CONNECT>
                    <SOURCE NAME="DISPLAY" PROPERTY="screeningDtls$resumeScreeningURL" />
                </CONNECT>
            </FIELD>
            <ACTION_CONTROL LABEL="ActionControl.Label.Delete">
                <LINK PAGE_ID="CitizenWorkspace_deleteScreening" OPEN_MODAL="true">
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="screeningDtls$iegExecutionID" />
                        <TARGET NAME="PAGE" PROPERTY="iegExecutionID" />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE NAME="DISPLAY" PROPERTY="screeningDtls$applicationName" />
                        <TARGET NAME="PAGE" PROPERTY="applicationName" />
                    </CONNECT>
                </LINK>
            </ACTION_CONTROL>
        </CONTAINER>
    </LIST>
</VIEW>