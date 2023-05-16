<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2011 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- Contains list of denied program application(s) to be appealed.  -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="ApplicationAppeal"
    NAME="ACTION"
    OPERATION="formatAppealObjectData"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="applicationID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="applicationID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="applicationID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$applicationCaseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="selectedProgApplications$applicationCaseID"
    />
  </CONNECT>


  <LIST
    
    TITLE="List.Title.ProgramDenials"
  >
    <CONTAINER
      LABEL="Field.Label.Select"
      WIDTH="10"
    >


      <WIDGET TYPE="MULTISELECT">
        <WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$list$programAppID"
            />
          </CONNECT>
        </WIDGET_PARAMETER>


        <WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="selectedPrograms"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
      </WIDGET>
    </CONTAINER>
    <FIELD LABEL="Field.Title.Program">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="programType"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Title.RequestDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="requestDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Title.DisposedOn">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="disposedOn"
        />
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>
