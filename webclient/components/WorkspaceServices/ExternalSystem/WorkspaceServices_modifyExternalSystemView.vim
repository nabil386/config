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
  <SERVER_INTERFACE CLASS="ExternalSystem" NAME="DISPLAY" OPERATION="readSystem" />
  <SERVER_INTERFACE CLASS="ExternalSystem" NAME="ACTION" OPERATION="modifySystem" PHASE="ACTION" />
  <PAGE_PARAMETER NAME="externalSystemID" />
  <PAGE_PARAMETER NAME="clientAlias" />
  <PAGE_PARAMETER NAME="clientPassword" />
  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="externalSystemID" />
    <TARGET NAME="DISPLAY" PROPERTY="key$externalSystemID" />
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="clientAlias" />
    <TARGET NAME="ACTION" PROPERTY="clientAlias" />
  </CONNECT>   
  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="clientPassword" />
    <TARGET NAME="ACTION" PROPERTY="clientPassword" />
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="externalSystemID" />
    <TARGET NAME="ACTION" PROPERTY="externalSystemID" />
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="versionNo" />
    <TARGET NAME="ACTION" PROPERTY="versionNo" />
  </CONNECT>
  <CLUSTER  LABEL_WIDTH="30">
    <FIELD LABEL="Field.Title.Name">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="name" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="name" />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Title.EncryptionFileName">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="cryptoPropertiesFileName" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="cryptoPropertiesFileName" />
      </CONNECT>      
    </FIELD>   
    <FIELD LABEL="Field.Title.LocationURL">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="locationURL" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="locationURL" />
      </CONNECT>      
    </FIELD>      
    <FIELD LABEL="Field.Title.ClientConfigFileName">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="clientConfigFileName" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="clientConfigFileName" />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER LABEL_WIDTH="30" TITLE="Cluster.Title.CommunicationDetails">
    <FIELD LABEL="Field.Title.CuramClientAlias">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="curamClientAlias" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="curamClientAlias" />
      </CONNECT>
    </FIELD>    
    <FIELD LABEL="Field.Title.CuramClientPassword">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="curamClientPassword" />
      </CONNECT>    
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="curamClientPassword" />
      </CONNECT>
    </FIELD>    
    <FIELD LABEL="Field.Title.PublicKeyAlias">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="publicKeyAlias" />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="publicKeyAlias" />
      </CONNECT>
    </FIELD>    
    <FIELD LABEL="Field.Title.PublicKeyPassword">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="publicKeyPassword" />
      </CONNECT>    
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="publicKeyPassword" />
      </CONNECT>
    </FIELD>      
  </CLUSTER>     
  <CLUSTER TITLE="Cluster.Title.Description" SHOW_LABELS="false">
    <FIELD HEIGHT="200">
        <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="description" />
        </CONNECT>
        <CONNECT>
            <TARGET NAME="ACTION" PROPERTY="description" />
        </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
