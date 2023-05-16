<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003-2005, 2009-2010 Curam Software Ltd.                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view to display the assignment history  for a component.  -->
<!-- BEGIN, CR00190939, GD -->
<?curam-deprecated Since Curam 6.0, replaced with ProductDelivery_listNomineeAssignmentHistoryView1.
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
    OPERATION="listCaseNomineeHistory"
  />


  <PAGE_PARAMETER NAME="caseNomineeID"/>
  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseNomineeID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseNomineeID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="33"
    WIDTH="33"
  >
    <FIELD LABEL="Field.Label.Nominee">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="nomineeName"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <ACTION_SET
    ALIGNMENT="CENTER"
    TOP="FALSE"
  >
    <ACTION_CONTROL
      IMAGE="AssignComponentButton"
      LABEL="ActionControl.Label.AssignComponent"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="ProductDelivery_assignComponent"
        SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseNomineeID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseNomineeID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="description"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>


  <LIST>


    <FIELD
      LABEL="Field.Label.Component"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="objectiveCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.From"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fromDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.To"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="toDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.FromCaseStartDate"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fromCaseStartDateInd"
        />
      </CONNECT>
    </FIELD>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="ProductDelivery_listComponentAssignmentHistory">
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="rulesObjectiveID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="rulesObjectiveID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseNomineeID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseNomineeID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="description"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


  </LIST>


</VIEW>
