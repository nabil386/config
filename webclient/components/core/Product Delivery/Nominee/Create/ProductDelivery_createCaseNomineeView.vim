<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002-2004, 2010 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view that allows the user to create case nominee details. -->
<?curam-deprecated Since Curam 6.0, replaced with ProductDelivery_createCaseNomineeView1.
  This page doesn't support Multiple Active Nominee Delivery Patterns (HLFR14604) . 
  Hence this page has been deprecated. See release note: CR00189687 ?>
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


  <PAGE_PARAMETER NAME="concernRoleID"/>
  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="concernRoleID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >


    <FIELD LABEL="Field.Label.StartDate">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fromDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Currency">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="currencyType"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Relationship">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="nomineeRelationship"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.PreferredOffice">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="publicOfficeID"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>