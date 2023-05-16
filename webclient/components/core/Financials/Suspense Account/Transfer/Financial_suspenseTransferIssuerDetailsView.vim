<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
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
<!-- Description -->
<!-- =========== -->
<!-- This page allows the user to enter (or update) issuer details of a     -->
<!-- payment received that was recorded in the suspense account before      -->
<!-- transfer to an employer or a person.                                   -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="Financial"
    NAME="DISPLAY"
    OPERATION="readSuspenseItemIssuerDetails"
  />


  <SERVER_INTERFACE
    CLASS="Financial"
    NAME="ACTION"
    OPERATION="transferFromSuspenseAccount"
    PHASE="ACTION"
  />


  <ACTION_SET ALIGNMENT="CENTER">
    <ACTION_CONTROL
      IMAGE="SaveAndAllocateButton"
      LABEL="ActionControl.Label.SaveAndAllocate"
      TYPE="SUBMIT"
    >
      <LINK
        DISMISS_MODAL="false"
        PAGE_ID="Financial_allocatePaymentReceivedSelectLiability1"
        SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="instructLineItemID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="lineItemID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="finInstructionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="finInstructionID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


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


  <PAGE_PARAMETER NAME="suspenseAccountID"/>
  <PAGE_PARAMETER NAME="concernRoleID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="suspenseAccountID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="suspenseAccountID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="suspenseAccountID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="suspenseAccountID"
    />
  </CONNECT>
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


  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="2"
    TITLE="Cluster.Title.IssuerDetails"
  >


    <FIELD LABEL="Field.Label.IssuerName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="issuerName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="issuerName"
        />
      </CONNECT>
    </FIELD>


    <FIELD CONTROL="SKIP"/>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="2"
    TAB_ORDER="ROW"
  >
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="issuerAddressData"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="issuerAddressData"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
