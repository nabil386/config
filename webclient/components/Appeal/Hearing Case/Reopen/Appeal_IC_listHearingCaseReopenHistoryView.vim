<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2003, 2010 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of       -->
<!-- Curam Software Ltd. ("Confidential Information"). You shall not        -->
<!-- disclose such Confidential Information and shall use it only in        -->
<!-- accordance with the terms of the license agreement you entered into    -->
<!-- with Curam Software.                                                   -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows user to view hearing case reopened history items on an-->
<!-- integrated case.                                                       -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="AppealReopenHistory"
    NAME="DISPLAY"
    OPERATION="listForIC"
    PHASE="DISPLAY"
  />


  <INCLUDE FILE_NAME="Appeal_listHearingCaseReopenHistoryBaseView.vim"/>


</VIEW>
