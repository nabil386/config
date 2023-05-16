<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2010, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010, 2011 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows user to view wait list entries for a Person.          -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText"
      />
    </CONNECT>
  </PAGE_TITLE>


  <PAGE_PARAMETER NAME="concernRoleID"/>


  <SERVER_INTERFACE
    CLASS="Person"
    NAME="DISPLAY"
    OPERATION="listWaitList"
    PHASE="DISPLAY"
  />
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="concernRoleID"
    />
  </CONNECT>


  <LIST>


    <!-- BEGIN, CR00369606, PS -->
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="WaitList_viewWaitListEntryForPerson">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="waitListEntryID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="waitListEntryID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>
    <!-- END, CR00369606 -->


    <FIELD
      LABEL="Field.Label.CaseType"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseTypeDescription"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00369606, PS -->
    <FIELD
      LABEL="Field.Label.CaseReference"
      WIDTH="20"
    >
      <!-- END, CR00369606 -->


      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseReference"
        />
      </CONNECT>
      <LINK PAGE_ID="Case_resolveCaseHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <!-- BEGIN, CR00369606, PS -->
    <FIELD
      LABEL="Field.Label.Resource"
      WIDTH="20"
    >
      <!-- END, CR00369606 -->


      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="resource"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00369606, PS -->
    <FIELD
      LABEL="Field.Label.WaitList.Type"
      WIDTH="20"
    >
      <!-- END, CR00369606 -->


      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="type"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00369606, PS -->
    <FIELD
      LABEL="Field.Label.WaitListEntry.DaysOnWaitList"
      WIDTH="20"
    >
      <!-- END, CR00369606 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="daysOnWaitList"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
