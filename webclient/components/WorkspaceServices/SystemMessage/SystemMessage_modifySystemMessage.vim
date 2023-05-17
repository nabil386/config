<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012, 2022. All Rights Reserved.
 
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
    
    <SERVER_INTERFACE CLASS="SystemMessage" NAME="DISPLAY" OPERATION="readSystemMessage" />
    <SERVER_INTERFACE CLASS="SystemMessage" NAME="ACTION" OPERATION="modifySystemMessage" PHASE="ACTION" />
    <PAGE_PARAMETER NAME="systemMessageID" />    
    <CONNECT>
        <SOURCE NAME="PAGE" PROPERTY="systemMessageID" />
        <TARGET NAME="DISPLAY" PROPERTY="systemMessageKey$systemMessageID" />
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="PAGE" PROPERTY="systemMessageID" />
        <TARGET NAME="ACTION" PROPERTY="systemMessageID" />
    </CONNECT>
    <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="versionNo" />
        <TARGET NAME="ACTION" PROPERTY="versionNo" />
    </CONNECT>   
    
    <CLUSTER  LABEL_WIDTH="35">
        <FIELD LABEL="Field.Title.TitleDescription" >
            <CONNECT>
                <SOURCE NAME="DISPLAY" PROPERTY="titleDescription" />
            </CONNECT>
            <CONNECT>
                <TARGET  NAME="ACTION" PROPERTY="titleDescription" />
            </CONNECT>
        </FIELD>        
        <FIELD LABEL="Field.Title.MessageDescription" HEIGHT="160" >        
            <CONNECT>
                <SOURCE  NAME="DISPLAY" PROPERTY="result$messageDescription" />
            </CONNECT>
            <CONNECT>
                <TARGET  NAME="ACTION" PROPERTY="sysMessageDetails$messageDescription" />
            </CONNECT>
        </FIELD>
    </CLUSTER> 
    <CLUSTER NUM_COLS="2" STYLE="outer-cluster-borderless">
      <FIELD LABEL="Field.Title.Visibility" USE_BLANK="true" USE_DEFAULT="false" WIDTH="70">
        <CONNECT>
           <SOURCE  NAME="DISPLAY" PROPERTY="result$visibility" />
        </CONNECT>
        <CONNECT>
           <TARGET  NAME="ACTION" PROPERTY="sysMessageDetails$visibility" />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Title.EffectiveDateTime" WIDTH="70" >
        <CONNECT>
          <SOURCE  NAME="DISPLAY" PROPERTY="effectiveDateTime"/>
        </CONNECT>
        <CONNECT>
          <TARGET  NAME="ACTION" PROPERTY="effectiveDateTime" />
        </CONNECT>            
      </FIELD>
      <FIELD LABEL="Field.Title.Priority" WIDTH="70">
        <CONNECT>
          <SOURCE  NAME="DISPLAY" PROPERTY="priorityInd" />
        </CONNECT>
        <CONNECT>
          <TARGET  NAME="ACTION" PROPERTY="priorityInd" />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Title.ExpiryDateTime" WIDTH="70">
        <CONNECT>
          <SOURCE  NAME="DISPLAY" PROPERTY="expiryDateTime" />
        </CONNECT>
        <CONNECT>
          <TARGET  NAME="ACTION" PROPERTY="expiryDateTime" />
        </CONNECT>
      </FIELD>
    </CLUSTER>    
    
</VIEW>
