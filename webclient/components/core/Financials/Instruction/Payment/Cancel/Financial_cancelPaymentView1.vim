<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012, 2015. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2009-2011 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for cancelling a payment.                            -->
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


  <PAGE_PARAMETER NAME="description"/>
  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="finInstructionID"/>


  <SERVER_INTERFACE
    CLASS="Financial"
    NAME="ACTION"
    OPERATION="cancelRegenerateOrInvalidatePayment"
    PHASE="ACTION"
  />


  <!-- BEGIN, CR00292632, SG -->
  <SERVER_INTERFACE
    CLASS="Case"
    NAME="DISPLAY"
    OPERATION="listCaseNominees"
  />
  <!-- END, CR00292632 -->


  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="ACTION"
        PROPERTY="informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="finInstructionID"
    />
    <!-- BEGIN, CR00292632, SG -->
    <TARGET
      NAME="DISPLAY"
      PROPERTY="financialInstructionID"
    />


  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
  </CONNECT>
  <!-- END, CR00292632 -->


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


  <CLUSTER NUM_COLS="1">
    <CLUSTER
      LABEL_WIDTH="30"
      NUM_COLS="1"
      STYLE="cluster-cpr-no-border"
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


      <FIELD LABEL="Field.Label.Invalidate">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="invalidateInd"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


    <CLUSTER
      DESCRIPTION="Cluster.Description.PaymentReissueDetails"
      LABEL_WIDTH="30"
      NUM_COLS="1"
      STYLE="cluster-cpr-no-border"
    >
      <FIELD LABEL="Field.Label.Regenerate">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="regenerateInd"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="ActionControl.Label.ChangeNominee"
        USE_BLANK="true"
        WIDTH="75"
      >
        <CONNECT>
          <!-- BEGIN, CR00314367, SG -->
          <INITIAL
            HIDDEN_PROPERTY="caseNomineeDetailsList$caseNomineeConcernRoleIDOpt"
            NAME="DISPLAY"
            PROPERTY="concernRoleName"
          />
          <!-- END, CR00314367 -->
        </CONNECT>
        <CONNECT>
          <!-- BEGIN, CR00314367, SG -->
          <TARGET
            NAME="ACTION"
            PROPERTY="caseNomineeConcernRoleIDOpt"
          />
          <!-- END, CR00314367 -->
        </CONNECT>
      </FIELD>
    </CLUSTER>


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
          PROPERTY="reasonText"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
