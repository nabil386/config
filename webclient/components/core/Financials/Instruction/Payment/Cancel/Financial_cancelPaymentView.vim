<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002-2006,2009 Curam Software Ltd.                       -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view for cancelling a payment.                            -->
<?curam-deprecated Since Curam 6.0, replaced by Financial_cancelPaymentView1.vim?>
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
    OPERATION="cancelOrInvalidatePayment"
    PHASE="ACTION"
  />


  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="ACTION"
        PROPERTY="informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>


  <ACTION_SET
    ALIGNMENT="CENTER"
    TOP="false"
  >


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


  <PAGE_PARAMETER NAME="finInstructionID"/>
  <PAGE_PARAMETER NAME="nomineeName"/>
  <PAGE_PARAMETER NAME="effectiveDate"/>
  <PAGE_PARAMETER NAME="description"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="finInstructionID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="finInstructionID"
    />
  </CONNECT>


  <!-- BEGIN, CR00142428, MC -->
  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="1"
    TITLE="Cluster.Title.Details"
  >


    <FIELD
      LABEL="Field.Label.Reason"
      WIDTH="50"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="reasonCode"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00142428 -->


    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="reasonText"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Regenerate">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="regenerateInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Invalidate">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="invalidateInd"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
