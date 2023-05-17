<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2007,2009 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view to display an Issue Delivery Case's home page        -->
<!-- details.                                                               -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


  <CLUSTER SHOW_LABELS="false">
    <CONTAINER>
      <ACTION_CONTROL IMAGE="SubmitIcon" LABEL="Field.StaticText.SubmitForApproval">
        <LINK OPEN_MODAL="true" PAGE_ID="ProductDelivery_submit" WINDOW_OPTIONS="width=650">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$contextDescription$description"/>
            <TARGET NAME="PAGE" PROPERTY="pageDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="Field.StaticText.SubmitForApproval">
        <LINK OPEN_MODAL="true" PAGE_ID="ProductDelivery_submit" WINDOW_OPTIONS="width=650,height=150">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$contextDescription$description"/>
            <TARGET NAME="PAGE" PROPERTY="pageDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>
    <CONTAINER>
      <ACTION_CONTROL IMAGE="ActivateIcon" LABEL="Field.StaticText.OnlineActivate">
        <LINK OPEN_MODAL="true" PAGE_ID="ProductDelivery_activate" WINDOW_OPTIONS="width=580">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$contextDescription$description"/>
            <TARGET NAME="PAGE" PROPERTY="pageDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="Field.StaticText.OnlineActivate">
        <LINK OPEN_MODAL="true" PAGE_ID="ProductDelivery_activate" WINDOW_OPTIONS="width=580">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$contextDescription$description"/>
            <TARGET NAME="PAGE" PROPERTY="pageDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>
    <CONTAINER>
      <ACTION_CONTROL IMAGE="CheckEligibilityIcon" LABEL="ActionControl.Label.CheckEligibility">
        <LINK OPEN_MODAL="true" PAGE_ID="ProductDelivery_checkEligibility" WINDOW_OPTIONS="width=1200,height=500">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$contextDescription$description"/>
            <TARGET NAME="PAGE" PROPERTY="pageDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.CheckEligibility">
        <LINK OPEN_MODAL="true" PAGE_ID="ProductDelivery_checkEligibility" WINDOW_OPTIONS="width=1200,height=500">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$contextDescription$description"/>
            <TARGET NAME="PAGE" PROPERTY="pageDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


  </CLUSTER>


  <CLUSTER SHOW_LABELS="false">
    <CONTAINER>
      <ACTION_CONTROL IMAGE="ApproveIcon" LABEL="Field.StaticText.Approve">
        <LINK OPEN_MODAL="true" PAGE_ID="ProductDelivery_approve" WINDOW_OPTIONS="width=400">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$contextDescription$description"/>
            <TARGET NAME="PAGE" PROPERTY="pageDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="Field.StaticText.Approve">
        <LINK OPEN_MODAL="true" PAGE_ID="ProductDelivery_approve" WINDOW_OPTIONS="width=400">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$contextDescription$description"/>
            <TARGET NAME="PAGE" PROPERTY="pageDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


    <CONTAINER>
      <ACTION_CONTROL IMAGE="RejectIcon" LABEL="Field.StaticText.Reject">
        <LINK OPEN_MODAL="true" PAGE_ID="ProductDelivery_reject" WINDOW_OPTIONS="width=710,height=210">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$contextDescription$description"/>
            <TARGET NAME="PAGE" PROPERTY="pageDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="Field.StaticText.Reject">
        <LINK OPEN_MODAL="true" PAGE_ID="ProductDelivery_reject" WINDOW_OPTIONS="width=710,height=210">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$contextDescription$description"/>
            <TARGET NAME="PAGE" PROPERTY="pageDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>
    <CONTAINER>
      <ACTION_CONTROL IMAGE="ReassessIcon" LABEL="ActionControl.Label.Reassess">
        <LINK OPEN_MODAL="true" PAGE_ID="ProductDelivery_reassessBenefit" WINDOW_OPTIONS="width=800,height=190">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$contextDescription$description"/>
            <TARGET NAME="PAGE" PROPERTY="pageDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Reassess">
        <LINK OPEN_MODAL="true" PAGE_ID="ProductDelivery_reassessBenefit" WINDOW_OPTIONS="width=800,height=190">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$contextDescription$description"/>
            <TARGET NAME="PAGE" PROPERTY="pageDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


  </CLUSTER>


  <CLUSTER SHOW_LABELS="false">
    <CONTAINER>
      <ACTION_CONTROL IMAGE="SuspendIcon" LABEL="Field.StaticText.Suspend">
        <LINK OPEN_MODAL="true" PAGE_ID="ProductDelivery_suspend" WINDOW_OPTIONS="width=800,height=270">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$contextDescription$description"/>
            <TARGET NAME="PAGE" PROPERTY="pageDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="Field.StaticText.Suspend">
        <LINK OPEN_MODAL="true" PAGE_ID="ProductDelivery_suspend" WINDOW_OPTIONS="width=800,height=270">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$contextDescription$description"/>
            <TARGET NAME="PAGE" PROPERTY="pageDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


    <CONTAINER>
      <ACTION_CONTROL IMAGE="UnsuspendIcon" LABEL="Field.StaticText.Unsuspend">
        <LINK OPEN_MODAL="true" PAGE_ID="ProductDelivery_unsuspend" WINDOW_OPTIONS="width=800,height=270">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$contextDescription$description"/>
            <TARGET NAME="PAGE" PROPERTY="pageDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="Field.StaticText.Unsuspend">
        <LINK OPEN_MODAL="true" PAGE_ID="ProductDelivery_unsuspend" WINDOW_OPTIONS="width=800,height=270">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$contextDescription$description"/>
            <TARGET NAME="PAGE" PROPERTY="pageDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


    <CONTAINER>
      <ACTION_CONTROL IMAGE="ReactivateIcon" LABEL="Field.StaticText.Reactivate">
        <LINK OPEN_MODAL="true" PAGE_ID="ProductDelivery_reactivate" WINDOW_OPTIONS="width=825,height=260">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$contextDescription$description"/>
            <TARGET NAME="PAGE" PROPERTY="pageDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="Field.StaticText.Reactivate">
        <LINK OPEN_MODAL="true" PAGE_ID="ProductDelivery_reactivate" WINDOW_OPTIONS="width=825,height=260">
          <CONNECT>
            <SOURCE NAME="PAGE" PROPERTY="caseID"/>
            <TARGET NAME="PAGE" PROPERTY="caseID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$contextDescription$description"/>
            <TARGET NAME="PAGE" PROPERTY="pageDescription"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


  </CLUSTER>


</VIEW>