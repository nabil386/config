<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2007, 2015. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2007, 2011 Curam Software Ltd.                           -->
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
  <PAGE_PARAMETER NAME="concernRoleID"/>
  <PAGE_PARAMETER NAME="contextDescription"/>
  <PAGE_PARAMETER NAME="dataItemName"/>


  <SERVER_INTERFACE
    CLASS="VerificationApplication"
    NAME="DISPLAY"
    OPERATION="fetchVerificationDetails"
  />
  <SERVER_INTERFACE
    CLASS="VerificationApplication"
    NAME="DISPLAY1"
    OPERATION="readEvidenceTypeDescription"
  />
  <!-- BEGIN, CR00451648 GK -->
  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="message"
      />
    </CONNECT>
  </INFORMATIONAL>
  <!-- END, CR00451648 -->
  <!-- Map verificationDetails parameter to viewVerificationDetails -->
  <!-- display-phase bean in order to retrieve details -->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="evidenceDescriptorID"
    />
    <TARGET
      NAME="DISPLAY1"
      PROPERTY="evidenceDescriptorID"
    />
  </CONNECT>
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
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="verificationLinkedID"
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


  <MENU MODE="IN_PAGE_NAVIGATION">
    <ACTION_CONTROL
      LABEL="ActionControl.Label.Details"
      STYLE="in-page-current-link"
    >
      <LINK
        PAGE_ID="VerificationApplication_viewWorkspaceParticipantVerificationDetails"
        SAVE_LINK="false"
      >
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
            NAME="DISPLAY1"
            PROPERTY="evidenceType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceType"
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
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      LABEL="ActionControl.Label.AppliesTo"
      STYLE="in-page-link"
    >
      <LINK PAGE_ID="VerificationApplication_viewVerificationListItemAppliesTo">
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
            NAME="PAGE"
            PROPERTY="evidenceType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceType"
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
      </LINK>
    </ACTION_CONTROL>
  </MENU>


  <CLUSTER
    LABEL_WIDTH="20"
    NUM_COLS="2"
    TITLE="Cluster.Title.EvidenceDetails"
  >


    <FIELD LABEL="Field.Label.EvidenceDescription">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY1"
          PROPERTY="description"
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
  </CLUSTER>


  <!-- List to show the Verification Items Received -->
  <LIST TITLE="Cluster.Title.VerificationItemsReceived">
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="VerificationApplication_viewVerificationItemProvisionDetails">
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
            PROPERTY="concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
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
      </INLINE_PAGE>
    </DETAILS_ROW>


    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL
        IMAGE="AddButton"
        LABEL="ActionControl.Label.AddAttachment"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="VerificationApplication_addAttachmentForItem"
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
    </ACTION_SET>


    <FIELD LABEL="Field.Label.Item">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="verificationItemName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ExpiryDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="expiryDate"
        />
      </CONNECT>
    </FIELD>
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
    <FIELD LABEL="Field.Label.ReceivedLevel">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="userProvidedVerificationItemDetails$level"
        />
      </CONNECT>
    </FIELD>
  </LIST>
</VIEW>
