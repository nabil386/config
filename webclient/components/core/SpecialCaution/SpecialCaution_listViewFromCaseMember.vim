<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view for the modify alternate name pages. -->
<!-- BEGIN, CR00251307, MC -->
<?curam-deprecated Since Curam 6.0, this file is no longer referenced in the
application.?>
<!-- END, CR00251307 -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


  <!-- The page title for this page -->
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE NAME="TEXT" PROPERTY="Page.Title"/>
    </CONNECT>
    <CONNECT>
      <SOURCE NAME="DISPLAY" PROPERTY="contextDesc$description"/>
    </CONNECT>
  </PAGE_TITLE>


  <!--  Server Interface Element  -->
  <SERVER_INTERFACE CLASS="SpecialCaution" NAME="DISPLAY" OPERATION="listSpecialCautions" PHASE="DISPLAY"/>


  <!-- Page Parameter -->
  <PAGE_PARAMETER NAME="concernRoleID"/>


  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="concernRoleID"/>
    <TARGET NAME="DISPLAY" PROPERTY="key$key$concernRoleID"/>
  </CONNECT>


  <!--List of Special Cautions -->
  <LIST>
    <CONTAINER LABEL="Container.Label.Action" SEPARATOR="Container.Separator">
      <ACTION_CONTROL LABEL="ActionControl.Label.View">
        <LINK PAGE_ID="SpecialCaution_viewFromCaseMember">
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="specialCautionID"/>
            <TARGET NAME="PAGE" PROPERTY="specialCautionID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="concernRoleID"/>
            <TARGET NAME="PAGE" PROPERTY="concernRoleID"/>
          </CONNECT>
        </LINK>


      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK OPEN_MODAL="TRUE" PAGE_ID="SpecialCaution_modify">
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="specialCautionID"/>
            <TARGET NAME="PAGE" PROPERTY="specialCautionID"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Delete">
        <LINK OPEN_MODAL="TRUE" PAGE_ID="SpecialCaution_delete" WINDOW_OPTIONS="width=400">
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="specialCautionID"/>
            <TARGET NAME="PAGE" PROPERTY="specialCautionID"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>
    <FIELD LABEL="Field.Label.Category">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="categoryCode"/>
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Type">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="typeCode"/>
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.StartDate">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="startDate"/>
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$list$list$recordStatus"/>
      </CONNECT>
    </FIELD>
  </LIST>
</VIEW>