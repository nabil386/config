<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--		Copyright 2010 Curam Software Ltd.	-->
<!--		All rights reserved.	-->
<!--		This software is the confidential and proprietary information of Curam	-->
<!--		Software, Ltd. ("Confidential Information"). You shall not disclose	-->
<!--		such Confidential Information and shall use it only in accordance with	-->
<!--		the terms of the license agreement you entered into with Curam	-->
<!--		Software.	-->
<!--		Description	-->
<!--		===========	-->
<!--		The included view to display the situation details.	-->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <!-- BEGIN, CR00205132, AK -->
  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="1"
    STYLE=" cluster-cpr-grey-background "
  >
    <CLUSTER
      LABEL_WIDTH="50"
      NUM_COLS="2"
      STYLE=" cluster-cpr-no-border"
    >
      <FIELD LABEL="Field.Label.ExpectedEndDate">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="expectedEndDate"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.ActualEndDate">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="actualEndDate"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


    <CLUSTER
      LABEL_WIDTH="50"
      NUM_COLS="2"
      STYLE=" cluster-cpr-no-border"
    >
      <FIELD LABEL="Field.Label.Outcome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="outcome"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


    <!-- BEGIN, CR00235006, AK -->
    <CLUSTER
      NUM_COLS="2"
      STYLE="cluster-cpr-grey-background"
    >


      <LIST
        STYLE="nested-cluster-left"
        TITLE="Cluster.Label.Concerning"
      >
        <FIELD HEIGHT="3">
          <CONNECT>
            <SOURCE
              NAME="DISPLAYCONCERN"
              PROPERTY="name"
            />
          </CONNECT>
          <LINK PAGE_ID="Participant_resolve">
            <CONNECT>
              <SOURCE
                NAME="DISPLAYCONCERN"
                PROPERTY="participantRoleID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="concernRoleID"
              />
            </CONNECT>
          </LINK>
        </FIELD>
      </LIST>


      <LIST
        STYLE="nested-cluster-right"
        TITLE="List.Title.Actions"
      >
        <!-- END, CROO235006 -->
        <ACTION_SET TYPE="LIST_ROW_MENU">
          <ACTION_CONTROL LABEL="ActionControl.Label.Remove">
            <LINK
              OPEN_MODAL="true"
              PAGE_ID="Situation_removeAssociation"
            >
              <CONNECT>
                <SOURCE
                  NAME="PAGE"
                  PROPERTY="situationID"
                />
                <TARGET
                  NAME="PAGE"
                  PROPERTY="situationID"
                />
              </CONNECT>
              <CONNECT>
                <SOURCE
                  NAME="DISPLAYACTIONS"
                  PROPERTY="actionID"
                />
                <TARGET
                  NAME="PAGE"
                  PROPERTY="actionID"
                />
              </CONNECT>
              <CONNECT>
                <SOURCE
                  NAME="PAGE"
                  PROPERTY="description"
                />
                <TARGET
                  NAME="PAGE"
                  PROPERTY="pageDescription"
                />
              </CONNECT>
            </LINK>
          </ACTION_CONTROL>
        </ACTION_SET>
        <FIELD HEIGHT="3">
          <CONNECT>
            <SOURCE
              NAME="DISPLAYACTIONS"
              PROPERTY="action"
            />
          </CONNECT>
          <!-- BEGIN, CR00216915, AK-->
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="Action_viewActionPopup"
          >
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="description"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="description"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAYACTIONS"
                PROPERTY="actionID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="actionID"
              />
            </CONNECT>


          </LINK>
          <!-- END, CR00216915 -->
        </FIELD>
      </LIST>


    </CLUSTER>
  </CLUSTER>


  <!-- END, CR00205132 -->
</VIEW>
