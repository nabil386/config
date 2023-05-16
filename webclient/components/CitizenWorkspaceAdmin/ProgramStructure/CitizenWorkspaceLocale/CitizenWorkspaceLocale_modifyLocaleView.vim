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
    
    <SERVER_INTERFACE CLASS="CitizenWorkspaceLocale" NAME="DISPLAY" OPERATION="readLocale" />
    <SERVER_INTERFACE CLASS="CitizenWorkspaceLocale" NAME="ACTION" OPERATION="modifyLocale" PHASE="ACTION" />
    <PAGE_PARAMETER NAME="citizenWorkspaceLocaleID" /> 
    <PAGE_PARAMETER NAME="displayName" />    
     
    <CONNECT>
        <SOURCE NAME="PAGE" PROPERTY="citizenWorkspaceLocaleID" />
        <TARGET NAME="DISPLAY" PROPERTY="key$citizenWorkspaceLocaleID" />
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="PAGE" PROPERTY="citizenWorkspaceLocaleID" />
        <TARGET NAME="ACTION" PROPERTY="citizenWorkspaceLocaleID" />
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="versionNo" />
        <TARGET NAME="ACTION" PROPERTY="versionNo" />
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="displayNameTextID" />
        <TARGET NAME="ACTION" PROPERTY="displayNameTextID" />
    </CONNECT>
    <CLUSTER LABEL_WIDTH="45">
        <FIELD LABEL="Field.Title.DisplayName">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="displayName" />
            </CONNECT>
            <CONNECT>
                <TARGET NAME="ACTION" PROPERTY="displayName" />
            </CONNECT>
        </FIELD>
        <FIELD LABEL="Field.Title.Language">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="localeCode" />
            </CONNECT>
            <CONNECT>
                <TARGET NAME="ACTION" PROPERTY="localeCode" />
            </CONNECT>
        </FIELD>    		
        <FIELD LABEL="Field.Label.AttachedImage">
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="imageID" />
            </CONNECT>
        </FIELD>    		
        
        <WIDGET
      		LABEL="Field.Label.NewImage"
      		TYPE="FILE_UPLOAD"
    		>
      		<WIDGET_PARAMETER NAME="CONTENT">
        		<CONNECT>
          		<TARGET
            		NAME="ACTION"
            		PROPERTY="imageContent"
          		/>
        		</CONNECT>
      		</WIDGET_PARAMETER>
      		<WIDGET_PARAMETER NAME="FILE_NAME">
        		<CONNECT>
          		<TARGET
           		 NAME="ACTION"
           		 PROPERTY="fileName"
         		 />
        		</CONNECT>
      		</WIDGET_PARAMETER>      		
    		</WIDGET>         
    	<FIELD LABEL="Field.Title.Content">
          <CONNECT>
              <SOURCE NAME="DISPLAY" PROPERTY="contentType" />
          </CONNECT>
          <CONNECT>
              <TARGET NAME="ACTION" PROPERTY="contentType" />
          </CONNECT>
        </FIELD>    		      
    </CLUSTER>
</VIEW>
