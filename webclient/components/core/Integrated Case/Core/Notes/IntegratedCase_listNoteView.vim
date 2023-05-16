<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2002-2003, 2009, 2010-2011 Curam Software Ltd.               -->
<!-- All rights reserved.                                                   --><!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view to display a list of integrated case notes.          -->

<!-- BEGIN, CR00246976, SS --><?curam-deprecated Since Curam 5.2 SP4, replaced with 
  IC_listAccessibleNoteView.
  As part of the change to links which are not to be made available to the user 
  for the unaccessible notes, this page has been deprecated. 
  See Release note: CR00246976
?>
<!-- END, CR00246976 -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE NAME="TEXT" PROPERTY="PageTitle.StaticText1"/>
    </CONNECT>
    <CONNECT>
      <SOURCE NAME="DISPLAY" PROPERTY="description"/>
    </CONNECT>
  </PAGE_TITLE>
  
  
  <SERVER_INTERFACE CLASS="IntegratedCase" NAME="DISPLAY" OPERATION="listNote"/>
  
  
  <MENU MODE="INTEGRATED_CASE">
    <CONNECT>
      <SOURCE NAME="DISPLAY" PROPERTY="result$icMenuData$menuData"/>
    </CONNECT>
  </MENU>
  
  
  <MENU>
    <ACTION_CONTROL LABEL="ActionControl.Label.NewTask">
      <LINK OPEN_MODAL="true" PAGE_ID="IntegratedCase_createTask">
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="PAGE" PROPERTY="caseID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="description"/>
          <TARGET NAME="PAGE" PROPERTY="pageDescription"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
    <ACTION_CONTROL LABEL="ActionControl.Label.NewActivity">
      <LINK OPEN_MODAL="true" PAGE_ID="IntegratedCase_createActivity">
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID"/>
          <TARGET NAME="PAGE" PROPERTY="caseID"/>
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="description"/>
          <TARGET NAME="PAGE" PROPERTY="pageDescription"/>
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </MENU>
  
  
  <PAGE_PARAMETER NAME="caseID"/>
  
  
  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="caseID"/>
    <TARGET NAME="DISPLAY" PROPERTY="key$caseID"/>
  </CONNECT>
  
  
  <LIST>
    
    
    <ACTION_SET BOTTOM="false">
      <ACTION_CONTROL IMAGE="NewButton" LABEL="ActionControl.Label.New">
        <LINK OPEN_MODAL="true" PAGE_ID="Case_createNote">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="description"/>
            <TARGET NAME="PAGE" PROPERTY="pageDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>
    
    
    <CONTAINER LABEL="Container.Label.Action" SEPARATOR="Container.Separator" WIDTH="15">
      <ACTION_CONTROL LABEL="ActionControl.Label.View">
        <LINK PAGE_ID="Case_viewNote">
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="caseNoteID"/>
            <TARGET NAME="PAGE" PROPERTY="caseNoteID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="description"/>
            <TARGET NAME="PAGE" PROPERTY="pageDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK OPEN_MODAL="true" PAGE_ID="Case_modifyNoteFromList">
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="caseNoteID"/>
            <TARGET NAME="PAGE" PROPERTY="caseNoteID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="description"/>
            <TARGET NAME="PAGE" PROPERTY="pageDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>
    
    
    <FIELD LABEL="Field.Label.EnteredBy" WIDTH="15">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="fullname"/>
      </CONNECT>
      <LINK OPEN_NEW="true" PAGE_ID="Organization_userHome">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="userName"/>
          <TARGET NAME="PAGE" PROPERTY="userName"/>
        </CONNECT>
      </LINK>
    </FIELD>
    
    
    <FIELD LABEL="Field.Label.CreationDateTime" WIDTH="15">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="creationDateTime"/>
      </CONNECT>
    </FIELD>
    
    
    <FIELD LABEL="Field.Label.Text" WIDTH="25">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="notesText"/>
      </CONNECT>
    </FIELD>
    
    
    <FIELD LABEL="Field.Label.Sensitivity" WIDTH="10">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="sensitivityCode"/>
      </CONNECT>
    </FIELD>
    
    
    <FIELD LABEL="Field.Label.Priority" WIDTH="10">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="priorityCode"/>
      </CONNECT>
    </FIELD>
    
    
    <FIELD LABEL="Field.Label.Status" WIDTH="10">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="recordStatus"/>
      </CONNECT>
    </FIELD>
    
    
  </LIST>


</VIEW>