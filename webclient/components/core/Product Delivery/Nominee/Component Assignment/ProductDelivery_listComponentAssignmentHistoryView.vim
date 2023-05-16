<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003-2004, 2009-2010 Curam Software Ltd.                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view to display the assignment history  for a component.  -->
<!-- BEGIN, CR00190939, GD -->
<?curam-deprecated Since Curam 6.0, replaced with ProductDelivery_listComponentAssignmentHistoryView1.
  This page doesn't support Multiple Active Nominee Delivery Patterns (HLFR14604) . 
  Hence this page has been deprecated. See release note: CR00190939 ?>
<!-- END, CR00190939 -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText1"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="Case"
    NAME="DISPLAY"
    OPERATION="listObjectiveHistory"
  />


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="rulesObjectiveID"/>
  <PAGE_PARAMETER NAME="description"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseObjectiveKey$caseID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="rulesObjectiveID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseObjectiveKey$rulesObjectiveID"
    />
  </CONNECT>


  <LIST>
    <FIELD
      LABEL="Field.Label.Nominee"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="concernRoleName"
        />
      </CONNECT>
      <!-- BEGIN, HARP-27142, BF -->
      <LINK PAGE_ID="Participant_resolveRoleHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="concernRoleType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="participantType"
          />
        </CONNECT>
      </LINK>
      <!-- END, HARP-27142-->
    </FIELD>
    <FIELD
      LABEL="Field.Label.FromDate"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="objectiveHistoryDetailsList$fromDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ToDate"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="objectiveHistoryDetailsList$toDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.FromCaseStartDate"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fromCaseStartDateInd"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
