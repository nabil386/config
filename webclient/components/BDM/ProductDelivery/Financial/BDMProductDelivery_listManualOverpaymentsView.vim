<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012, 2015. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2010 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Included view used to list the results of a reassessment of a case.    -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

  <SERVER_INTERFACE
    CLASS="BDMManualOverPayment"
    NAME="DISPLAYMANOP"
    OPERATION="searchManualOPByBenefitCaseID"
  />

  <PAGE_PARAMETER NAME="caseID"/>

  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAYMANOP"
      PROPERTY="arg1$caseID"
    />
  </CONNECT>

  <LIST SUMMARY="Summary.ManualOverpayment.OverUnderPayment.Details" TITLE="Summary.ManualOverpayment.OverUnderPayment.Details">
  <ACTION_SET TYPE="LIST_ROW_MENU">
	  <ACTION_CONTROL
	        LABEL="ActionControl.Label.Delete">
	        <LINK
	          OPEN_MODAL="true"
	          PAGE_ID="BDMManualOverPayment_delete"
	          >
	          <CONNECT>
	            <SOURCE
	              NAME="DISPLAYMANOP"
	              PROPERTY="result$dtls$caseID"
	            />
	            <TARGET
	              NAME="PAGE"
	              PROPERTY="caseID"
	            />
	          </CONNECT>
	        </LINK>
	      </ACTION_CONTROL>
      </ACTION_SET>
    <FIELD
      LABEL="Field.ManualOverpayment.Label.Type"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAYMANOP"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.ManualOverpayment.Label.OverUnderPaymentCaseReference"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAYMANOP"
          PROPERTY="caseReference"
        />
      </CONNECT>
      <LINK PAGE_ID="Case_resolveCaseHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAYMANOP"
            PROPERTY="result$dtls$caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
    </FIELD>

    <FIELD
      LABEL="Field.ManualOverpayment.Label.startDate"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAYMANOP"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.ManualOverpayment.Label.Nominee"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAYMANOP"
          PROPERTY="concernRoleName"
        />
      </CONNECT>
    </FIELD>

    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.ManualOverpayment.Label.Amount"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAYMANOP"
          PROPERTY="origAmount"
        />
      </CONNECT>
    </FIELD>

    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.ManualOverpayment.Label.OutstandingAmount"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAYMANOP"
          PROPERTY="outstandingAmount"
        />
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>
