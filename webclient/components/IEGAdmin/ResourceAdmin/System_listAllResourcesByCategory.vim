<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012, 2017. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->  
<!-- ====================================================================== -->
<!--                                                                        -->
<!-- Copyright (c) 2004-2011 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!--                                                                        -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Page used to list all of the resources.                                -->
<!--                                                                        -->
<!-- ====================================================================== -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">
   
   <PAGE_TITLE>
	    <CONNECT>
	      <SOURCE
	        NAME="TEXT"
	        PROPERTY="PageTitle.title"
	      />
	    </CONNECT>
	  </PAGE_TITLE>
   
    <SERVER_INTERFACE CLASS="ResourceAdmin" NAME="ACTION"  OPERATION="readResourcesByCategory" PHASE="ACTION"/>
    <ACTION_SET BOTTOM="false">
      <ACTION_CONTROL LABEL="ActionControl.Label.AddResource">
        <LINK OPEN_MODAL="true" PAGE_ID="System_createResource"/>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Publish">
        <LINK OPEN_MODAL="true" PAGE_ID="System_publishResourceChanges"/>
      </ACTION_CONTROL>
    </ACTION_SET>
    <CLUSTER LABEL_WIDTH="25" TITLE="Cluster.Filter.Title">
      <ACTION_SET TOP="false" ALIGNMENT="LEFT">
        <ACTION_CONTROL DEFAULT="true" LABEL="ActionControl.Label.Search" TYPE="SUBMIT">
          <LINK PAGE_ID="THIS"/>
        </ACTION_CONTROL>
      </ACTION_SET>
      <FIELD LABEL="Field.Label.Category" USE_BLANK="true" WIDTH_UNITS="CHARS" WIDTH="10">
        <CONNECT>
          <TARGET NAME="ACTION" PROPERTY="category$category"/>
        </CONNECT>
      </FIELD>
    </CLUSTER>
    <LIST>
        <DETAILS_ROW>
            <INLINE_PAGE PAGE_ID="System_viewResource">
                <CONNECT>
                    <SOURCE NAME="ACTION" PROPERTY="resourceID" />
                    <TARGET NAME="PAGE" PROPERTY="resourceID" />
                </CONNECT>
            </INLINE_PAGE>
        </DETAILS_ROW>
        <ACTION_SET TYPE="LIST_ROW_MENU">
            <ACTION_CONTROL LABEL="ActionControl.Label.Edit" APPEND_ELLIPSIS="true">
                <LINK OPEN_MODAL="true" PAGE_ID="System_modifyResource">
                    <CONNECT>
                        <SOURCE NAME="ACTION" PROPERTY="resourceID" />
                        <TARGET NAME="PAGE" PROPERTY="resourceID" />
                    </CONNECT>
                </LINK>
            </ACTION_CONTROL>
            <ACTION_CONTROL LABEL="ActionControl.Label.Export" TYPE="FILE_DOWNLOAD" APPEND_ELLIPSIS="true">
                <LINK>
                    <CONNECT>
                        <SOURCE NAME="ACTION" PROPERTY="name" />
                        <TARGET NAME="PAGE" PROPERTY="name" />
                    </CONNECT>
                    <CONNECT>
                        <SOURCE NAME="ACTION" PROPERTY="locale" />
                        <TARGET NAME="PAGE" PROPERTY="locale" />
                    </CONNECT>
                </LINK>
            </ACTION_CONTROL>
            <ACTION_CONTROL LABEL="ActionControl.Label.Remove" APPEND_ELLIPSIS="true">
                <LINK OPEN_MODAL="true" PAGE_ID="System_deleteResource">
                    <CONNECT>
                        <SOURCE NAME="ACTION" PROPERTY="resourceID" />
                        <TARGET NAME="PAGE" PROPERTY="resourceID" />
                    </CONNECT>
                </LINK>
            </ACTION_CONTROL>
        </ACTION_SET>
        <FIELD LABEL="Field.Title.Name">
            <CONNECT>
                <SOURCE NAME="ACTION" PROPERTY="dtls$name" />
            </CONNECT>
        </FIELD>
        <FIELD LABEL="Field.Title.Locale">
            <CONNECT>
                <SOURCE NAME="ACTION" PROPERTY="locale" />
            </CONNECT>
        </FIELD>
        <FIELD LABEL="Field.Title.ContentType">
            <CONNECT>
                <SOURCE NAME="ACTION" PROPERTY="contentType" />
            </CONNECT>
        </FIELD>
    </LIST>
</VIEW>
