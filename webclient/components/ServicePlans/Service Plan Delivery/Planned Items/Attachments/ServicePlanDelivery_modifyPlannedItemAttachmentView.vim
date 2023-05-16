<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008, 2010 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for modifying case attachments.                      -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <!-- Display Phase Server Access Beans for displaying Attachment Details -->
  <SERVER_INTERFACE
    CLASS="ServicePlanDelivery"
    NAME="DISPLAY"
    OPERATION="viewAttachment"
    PHASE="DISPLAY"
  />


  <!-- Action Phase Server Access Beans for modifying Attachment Details -->
  <SERVER_INTERFACE
    CLASS="ServicePlanDelivery"
    NAME="ACTION"
    OPERATION="modifyAttachment"
    PHASE="ACTION"
  />


  <!-- Page Level Connect statements used in this page -->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="plannedItemAttachmentLinkID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="plannedItemAttachmentLinkKey$plannedItemAttachmentLinkID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="attachmentID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="attachmentID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$plannedItemID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="plannedItemID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$plannedItemAttachmentLinkID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="plannedItemAttachmentLinkID"
    />
  </CONNECT>
  <!-- Action elements used in this page -->
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


  <!-- Clusters used to display/Edit Details for Selected Planned Item Attachment-->
  <CLUSTER
    LABEL_WIDTH="41"
    NUM_COLS="2"
  >


    <CONTAINER LABEL="Field.Label.AttachedFile">
      
<!-- BEGIN, CR00235147, SS -->
      <WIDGET TYPE="FILE_DOWNLOAD">
        <WIDGET_PARAMETER NAME="LINK_TEXT">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$attachmentName"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
        <WIDGET_PARAMETER NAME="PARAMS">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$plannedItemAttachmentLinkID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="plannedItemAttachmentLinkID"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
      </WIDGET>
 <!-- END, CR00235147 -->
    </CONTAINER>


    <WIDGET
      LABEL="Field.Label.NewFile"
      TYPE="FILE_UPLOAD"
    >
      <WIDGET_PARAMETER NAME="CONTENT">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="attachmentContents"
          />
        </CONNECT>
      </WIDGET_PARAMETER>


      <WIDGET_PARAMETER NAME="FILE_NAME">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="attachmentName"
          />
        </CONNECT>
      </WIDGET_PARAMETER>
    </WIDGET>


    <FIELD LABEL="Field.Label.FileLocation">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fileLocation"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fileLocation"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ReceiptDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$receiptDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="receiptDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.DocumentType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="documentType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="documentType"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.FileReference">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fileReference"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fileReference"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <!-- Cluster to display description added for an a -->
  <CLUSTER
    LABEL_WIDTH="20.5"
    TITLE="Cluster.Title.Description"
  >
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Description"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$description"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="description"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
