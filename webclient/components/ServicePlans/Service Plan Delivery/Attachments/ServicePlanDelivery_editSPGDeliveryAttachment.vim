<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2009, 2011 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view for modifying service plan group delivery case attachments. -->
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
    CLASS="ServicePlanDelivery"
    NAME="DISPLAY_DESC"
    OPERATION="getServicePlanGroupContextDescription"
    PHASE="DISPLAY"
  />
  <SERVER_INTERFACE
    CLASS="SPGDeliveryAttachment"
    NAME="DISPLAY"
    OPERATION="viewAttachment"
  />
  <SERVER_INTERFACE
    CLASS="SPGDeliveryAttachment"
    NAME="ACTION"
    OPERATION="modifyAttachment"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="attachmentID"/>
  <PAGE_PARAMETER NAME="deliveryAttachmentLinkId"/>
  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="servicePlanGroupDeliveryId"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="servicePlanGroupDeliveryId"
    />
    <TARGET
      NAME="DISPLAY_DESC"
      PROPERTY="servicePlanGroupDeliveryID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="attachmentID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$attachmentID"
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
      NAME="PAGE"
      PROPERTY="deliveryAttachmentLinkId"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="deliveryAttachmentLinkId"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="attachmentDtls$versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="attachmentDtls$versionNo"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="spgAttachmentDtls$versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="spgAttachmentDtls$versionNo"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="attachmentStatus"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="attachmentStatus"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="caseParticipantRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseParticipantRoleID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="servicePlanGroupDeliveryId"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="servicePlanGroupDeliveryId"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="status"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="status"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="attachmentDate"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="attachmentDate"
    />
  </CONNECT>

  <!-- BEGIN, CR00246725, MR -->
  <CLUSTER
    LABEL_WIDTH="41"
    NUM_COLS="2"
  >


    <CONTAINER LABEL="Field.Label.AttachedFile">

      <WIDGET TYPE="FILE_DOWNLOAD">
        <WIDGET_PARAMETER NAME="LINK_TEXT">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="attachmentName"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
        <WIDGET_PARAMETER NAME="PARAMS">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="key$attachmentID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="attachmentID"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
      </WIDGET>
      <!-- END, CR00246725 -->
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
          PROPERTY="receiptDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="receiptDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ParticipantName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseParticipantName"
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
          PROPERTY="description"
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
