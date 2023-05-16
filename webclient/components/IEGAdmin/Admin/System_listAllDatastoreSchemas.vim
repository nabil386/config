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
<!-- Page used to list all of the available Datastore schemas.              -->
<!--                                                                        -->
<!-- ====================================================================== -->
<?curam-deprecated Since Curam 6.0.5.0, replaced by IEG2_listAllDatastoreSchemas.uim?>
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <SERVER_INTERFACE CLASS="DatastoreSchema" NAME="DISPLAY" OPERATION="listSchemasOmitConfigTypes" PHASE="DISPLAY" />
  <ACTION_SET BOTTOM="false">
    <ACTION_CONTROL LABEL="ActionControl.Label.CreateBlankSchema">
      <LINK PAGE_ID="Datastore_createBlankSchema" OPEN_MODAL="true"/>
    </ACTION_CONTROL>
    <ACTION_CONTROL LABEL="ActionControl.Label.AddSchema">
        <LINK PAGE_ID="Datastore_createSchema" OPEN_MODAL="true"/>
    </ACTION_CONTROL>    
  </ACTION_SET>
  <LIST>
    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit" APPEND_ELLIPSIS="true">
        <LINK PAGE_ID="System_editSchema">
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="schemaName" />
            <TARGET NAME="PAGE" PROPERTY="schemaName" />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>      
      <ACTION_CONTROL LABEL="ActionControl.Label.Download" TYPE="FILE_DOWNLOAD" APPEND_ELLIPSIS="true">
        <LINK>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="schemaName" />
            <TARGET NAME="PAGE" PROPERTY="schemaName" />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Delete">
        <LINK OPEN_MODAL="true" PAGE_ID="Datastore_deleteSchema">
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="schemaName" />
            <TARGET NAME="PAGE" PROPERTY="schemaName" />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>
    <FIELD LABEL="Field.Title.Name">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="schemaName" />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Title.Type">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="schemaType" />
      </CONNECT>
    </FIELD>
  </LIST>
</VIEW>