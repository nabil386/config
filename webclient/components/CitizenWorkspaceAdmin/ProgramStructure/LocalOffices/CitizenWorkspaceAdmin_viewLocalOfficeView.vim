<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->  

<!-- ====================================================================== -->
<!-- Copyright (c) 2011 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- ====================================================================== -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">
    <PAGE_PARAMETER NAME="programName" />
    <PAGE_PARAMETER NAME="programTypeID" />
    <CONNECT>
        <SOURCE NAME="PAGE" PROPERTY="programTypeID" />
        <TARGET NAME="DISPLAY" PROPERTY="key$programTypeID" />
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="PAGE" PROPERTY="programTypeID" />
        <TARGET NAME="DISPLAYLOCALOFFICE" PROPERTY="key$programTypeID" />
    </CONNECT>
    
   <CLUSTER LABEL_WIDTH="20">
        <CONTAINER LABEL="Field.Title.Name">
            <FIELD>
                <CONNECT>
                    <SOURCE NAME="DISPLAY" PROPERTY="result$details$defaultLocalOfficeName" />
                </CONNECT>
            </FIELD>
            <ACTION_CONTROL LABEL="ActionControl.Label.Change" APPEND_ELLIPSIS="false">
                <LINK PAGE_ID="CitizenWorkspaceAdmin_modifyDefaultLocalOfficeForProgram" OPEN_MODAL="true">
                    <CONNECT>
                        <SOURCE NAME="PAGE" PROPERTY="programTypeID" />
                        <TARGET NAME="PAGE" PROPERTY="programTypeID" />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE NAME="PAGE" PROPERTY="programName" />
                        <TARGET NAME="PAGE" PROPERTY="programName" />
                    </CONNECT>
                </LINK>
            </ACTION_CONTROL>
        </CONTAINER>
    </CLUSTER>
    
      <LIST>
          <ACTION_SET TYPE="LIST_ROW_MENU">
              <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
                  <LINK PAGE_ID="CitizenWorkspaceAdmin_modifyLocalOffice" OPEN_MODAL="true">
                      <CONNECT>
                          <SOURCE NAME="DISPLAY" PROPERTY="dtls$programTypeID" />
                          <TARGET NAME="PAGE" PROPERTY="programTypeID" />
                      </CONNECT>
                      <CONNECT>
                          <SOURCE NAME="DISPLAYLOCALOFFICE" PROPERTY="result$dtlsList$locationID" />
                          <TARGET NAME="PAGE" PROPERTY="locationID" />
                      </CONNECT>
                      <CONNECT>
                          <SOURCE NAME="DISPLAYLOCALOFFICE" PROPERTY="localOfficeName" />
                          <TARGET NAME="PAGE" PROPERTY="name" />
                      </CONNECT>
                  </LINK>
              </ACTION_CONTROL>
              <ACTION_CONTROL LABEL="ActionControl.Label.Remove">
                  <LINK PAGE_ID="CitizenWorkspaceAdmin_removeLocalOffice" OPEN_MODAL="true">
                      <CONNECT>
                          <SOURCE NAME="DISPLAY" PROPERTY="dtls$programTypeID" />
                          <TARGET NAME="PAGE" PROPERTY="programTypeID" />
                      </CONNECT>
                      <CONNECT>
                          <SOURCE NAME="DISPLAYLOCALOFFICE" PROPERTY="result$dtlsList$locationID" />
                          <TARGET NAME="PAGE" PROPERTY="locationID" />
                      </CONNECT>
                      <CONNECT>
                          <SOURCE NAME="DISPLAYLOCALOFFICE" PROPERTY="localOfficeName" />
                          <TARGET NAME="PAGE" PROPERTY="name" />
                      </CONNECT>
                  </LINK>
              </ACTION_CONTROL>
          </ACTION_SET>
        <FIELD LABEL="Field.Title.LocalOffice" WIDTH="30">
            <CONNECT>
                <SOURCE NAME="DISPLAYLOCALOFFICE" PROPERTY="localOfficeName" />
            </CONNECT>
        </FIELD>
        <FIELD LABEL="Field.Title.ServiceAreas" WIDTH="70">
            <CONNECT>
                <SOURCE NAME="DISPLAYLOCALOFFICE" PROPERTY="serviceAreaNameList" />
            </CONNECT>
        </FIELD>
      </LIST>
</VIEW>      
