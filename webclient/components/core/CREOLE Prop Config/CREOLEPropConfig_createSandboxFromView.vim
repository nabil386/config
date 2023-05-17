<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to create a propagation configuration.       -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="RuleObjectPropagatorConfiguration"
    NAME="DISPLAY"
    OPERATION="readConfiguration"
    PHASE="DISPLAY"
  />


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$dtls$snapshotID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$dtls$snapshotID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$versionNo"
    />
  </CONNECT>


  <ACTION_SET>
    <ACTION_CONTROL
      LABEL="control.label.save"
      TYPE="SUBMIT"
    />
    <ACTION_CONTROL LABEL="control.label.cancel"/>
  </ACTION_SET>


</VIEW>
