<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012, 2015. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view for writing off an amount of a financial line item.  -->
<?curam-deprecated Since Curam 6.0, replaced by Financial_createWriteOffView1.vim?>
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
    CLASS="Financial"
    NAME="ACTION"
    OPERATION="writeOffInstructionLineItem"
    PHASE="ACTION"
  />


  <ACTION_SET ALIGNMENT="CENTER">


    <ACTION_CONTROL
      IMAGE="SaveButton"
      LABEL="ActionControl.Label.Save"
      TYPE="SUBMIT"
    />


    <ACTION_CONTROL
      IMAGE="CancelButton"
      LABEL="ActionControl.Label.Cancel"
    />


  </ACTION_SET>


  <PAGE_PARAMETER NAME="lineItemID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="lineItemID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="debitInstructLineItemID"
    />
  </CONNECT>


  <!-- BEGIN, CR00142869, MC -->
  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >


    <FIELD
      LABEL="Field.Label.Amount"
      WIDTH="10"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="amount"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Reason">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="reasonCode"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00142869 -->


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Field.Label.Comments"
  >
    <!-- BEGIN, CR00463142, EC -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00463142 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
