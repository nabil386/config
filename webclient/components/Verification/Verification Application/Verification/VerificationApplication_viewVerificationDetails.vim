<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2003, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003-2007,2009 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to view a Verification Details Page.   -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <PAGE_PARAMETER NAME="VDIEDLinkID"/>
  <PAGE_PARAMETER NAME="evidenceDescriptorID"/>
  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="contextDescription"/>
  <PAGE_PARAMETER NAME="dataItemName"/>
  <SERVER_INTERFACE
    CLASS="VerificationApplication"
    NAME="DISPLAY"
    OPERATION="fetchVerificationDetails"
  />
  <!-- BEGIN, CR00023145, KY -->
  <!-- BEGIN, CR00148586, CW -->
  <SERVER_INTERFACE
    CLASS="VerificationApplication"
    NAME="DISPLAY1"
    OPERATION="readEvidenceTypeAndRelatedID"
  />
  <!-- END, CR00148586 -->
  <!-- END, CR00023145 -->
  <!-- Map verificationDetails parameter to viewVerificationDetails -->
  <!-- display-phase bean in order to retrieve details -->
  <!-- BEGIN, CR00023145, KY -->
  <!-- BEGIN, CR00148586, CW -->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="evidenceDescriptorID"
    />
    <TARGET
      NAME="DISPLAY1"
      PROPERTY="evidenceDescriptorKey$evidenceDescriptorID"
    />
  </CONNECT>
  <!-- END, CR00148586 -->
  <!-- END, CR00023145 -->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="VDIEDLinkID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="vdIEDLinkID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="evidenceDescriptorID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="evidenceDescriptorID"
    />
  </CONNECT>
  <!-- BEGIN, CR00075582, RF -->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="verificationLinkedID"
    />
  </CONNECT>
  <!-- END, CR00075582 -->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="contextDescription"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="description"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="dataItemName"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="evidenceAndDataItemNameDetails$dataItemName"
    />
  </CONNECT>
  <!-- Cluster with Details -->
  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >
    <FIELD LABEL="Field.Label.Verification">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="evidenceAndDataItemNameDetails$dataItemName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.EvidenceStatus">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="evidenceStatus"
        />
      </CONNECT>
    </FIELD>
    <CONTAINER LABEL="Field.Label.EvidenceLink">
      <ACTION_CONTROL LABEL="ActionControl.Label.Evidence">
        <!-- BEGIN, CR00023145, KY -->
        <LINK PAGE_ID="Evidence_resolveWorkspace">
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
              NAME="PAGE"
              PROPERTY="contextDescription"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="contextDescription"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY1"
              PROPERTY="result$evidenceType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceType"
            />
          </CONNECT>
          <!-- END, CR00023145 -->
          <!-- BEGIN, CR00148441, CW -->
          <!-- BEGIN, CR00148586, CW -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY1"
              PROPERTY="result$relatedID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceID"
            />
          </CONNECT>
          <!-- END, CR00148441 -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY1"
              PROPERTY="result$parentEvidenceType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="parentEvidenceType"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY1"
              PROPERTY="result$parentRelatedID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="parentEvidenceID"
            />
            <!-- END, CR00148586 -->
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>
    <FIELD LABEL="Field.Label.Client">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="clientName"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <!-- Cluster of Applies To -->
  <LIST TITLE="Cluster.Title.AppliesTo">
    <!-- BEGIN, CR00023061, KY -->
    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="3"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="verificationRequirementSummaryDetailsList$verificationStatus"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Mandatory"
      WIDTH="3"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="mandatory"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00023061 -->
    <FIELD LABEL="Field.Label.CaseReference">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseReference"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Type">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseType"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Level">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="verificationRequirementSummaryDetailsList$level"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.MinimumItems">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="minimumItems"
        />
      </CONNECT>
    </FIELD>
    <CONTAINER
      LABEL="Container.Label.DueDate"
      WIDTH="25"
    >
      <FIELD WIDTH="15">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="dueDate"
          />
        </CONNECT>
      </FIELD>
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="dueDateModifiableInd"
          />
        </CONNECT>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="VerificationApplication_modifyDueDate"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="verificationID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="verificationID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dueDate"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="result$dtls$summaryDetailsList$dueDate"
            />
          </CONNECT>
        </LINK>
      </FIELD>
    </CONTAINER>
  </LIST>
  <!-- Cluster of Verification Items Received  -->
  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.VerificationItemsReceived"
  >


    <!-- An action set containing the single Add button. -->
    <ACTION_SET
      BOTTOM="false"
      TOP="true"
    >
      <!-- BEGIN, CR00451497, GK -->
      <ACTION_CONTROL
        IMAGE="AddButton"
        LABEL="ActionControl.Label.VerifyAll"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="VerificationApplication_verifyOutstandingEvd"
        >
          <CONNECT>
            <SOURCE
              NAME="CONSTANT"
              PROPERTY="Constant.NoValue"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="VDIEDLinkID"
            />
          </CONNECT>
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
              NAME="CONSTANT"
              PROPERTY="Constant.NoValue"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="wizardStateID"
            />
          </CONNECT>


        </LINK>
      </ACTION_CONTROL>
      <!-- END, CR00451497 -->
      <ACTION_CONTROL
        IMAGE="AddButton"
        LABEL="ActionControl.Label.Add"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="VerificationApplication_createVerificationItemProvision"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="vdIEDLinkID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="VDIEDLinkID"
            />
          </CONNECT>
          <!-- BEGIN, CR00075582, RF -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="evidenceAndDataItemNameDetails$caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
          <!-- END, CR00075582 -->
          <!-- BEGIN, CR00080534, RF -->
          <CONNECT>
            <SOURCE
              NAME="CONSTANT"
              PROPERTY="verification.showConcernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleID"
            />
          </CONNECT>
          <!-- END, CR00080534 -->
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <!-- List to show the Verification Items Received -->
    <LIST>
      <!-- Container with two action controls to view and remove each row. -->
      <CONTAINER
        LABEL="Container.Label.Action"
        SEPARATOR="Container.Separator"
        WIDTH="15"
      >
        <ACTION_CONTROL LABEL="ActionControl.Label.View">
          <!-- Link to a page displaying non-editable details of the -->
          <!-- verification details -->
          <LINK PAGE_ID="VerificationApplication_viewVerificationItemProvisionDetails">
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="verificationItemProvidedID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="verificationItemProvidedID"
              />
            </CONNECT>
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
                NAME="PAGE"
                PROPERTY="contextDescription"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="contextDescription"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="evidenceDescriptorID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="evidenceDescriptorID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="dataItemName"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="dataItemName"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="VDIEDLinkID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="VDIEDLinkID"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>
        <ACTION_CONTROL LABEL="ActionControl.Label.Remove">
          <!-- Link to page remove Verification Items received -->
          <!-- Verification Details -->
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="VerificationApplication_removeVerificationItemProvision"
            WINDOW_OPTIONS="width=500,height=150"
          >
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="verificationItemProvidedID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="verificationItemProvidedID"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>
      </CONTAINER>
      <FIELD LABEL="Field.Label.Item">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="verificationItemName"
          />
        </CONNECT>
      </FIELD>
      <!-- BEGIN, CR 3140, RK -->
      <FIELD LABEL="Field.Label.ExpiryDate">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="expiryDate"
          />
        </CONNECT>
      </FIELD>
      <!-- END, CR 3140 -->
      <FIELD LABEL="Field.Label.Providedby">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="receivedFrom"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.Date">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="dateReceived"
          />
        </CONNECT>
      </FIELD>
      <!-- END, CR00018000, KY -->
      <FIELD LABEL="Field.Label.ReceivedLevel">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="userProvidedVerificationItemDetails$level"
          />
        </CONNECT>
      </FIELD>
      <!-- END, CR00018000 -->
    </LIST>
  </CLUSTER>
</VIEW>
