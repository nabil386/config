<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2006-2008, 2010-2011 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This view allows the user to modify the attachment details.            -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="VerificationApplication"
    NAME="DISPLAY"
    OPERATION="readVerificationAttachment"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="VerificationApplication"
    NAME="DISPLAY1"
    OPERATION="fetchVerificationDetails"
  />


  <SERVER_INTERFACE
    CLASS="ApplicationVerification"
    NAME="ACTION"
    OPERATION="modifyVerificationAttachment"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="verificationAttachmentLinkID"/>
  <PAGE_PARAMETER NAME="verificationItemProvidedID"/>
  <PAGE_PARAMETER NAME="attachmentID"/>
  <PAGE_PARAMETER NAME="VDIEDLinkID"/>
  <PAGE_PARAMETER NAME="evidenceDescriptorID"/>
  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="contextDescription"/>
  <PAGE_PARAMETER NAME="dataItemName"/>
  <!-- BEGIN, CR00080534, RF -->
  <PAGE_PARAMETER NAME="concernRoleID"/>
  <!-- END, CR00080534 -->
  <PAGE_PARAMETER NAME="applicationID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="VDIEDLinkID"
    />
    <TARGET
      NAME="DISPLAY1"
      PROPERTY="vdIEDLinkID"
    />
  </CONNECT>
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
      PROPERTY="caseID"
    />
    <!-- BEGIN, CR00075582, RF -->
    <TARGET
      NAME="DISPLAY1"
      PROPERTY="evidenceAndDataItemNameDetails$caseID"
    />
    <!-- END, CR00075582 -->
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="contextDescription"
    />
    <TARGET
      NAME="DISPLAY1"
      PROPERTY="description"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="dataItemName"
    />
    <TARGET
      NAME="DISPLAY1"
      PROPERTY="evidenceAndDataItemNameDetails$dataItemName"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="verificationAttachmentLinkID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$key$verificationAttachmentLinkID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="attachmentID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$key$attachmentID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="applicationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="applicationID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$details$readLinkDtls$verificationAttachmentLinkID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$details$modifyLinkDtls$verificationAttachmentLinkID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$details$readAttachmentDtls$attachmentStatus"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$details$modifyAttachmentDtls$attachmentStatus"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="verificationItemProvidedID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="verificationItemProvidedID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="statusCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="statusCode"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="recordStatus"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="recordStatus"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$details$readAttachmentDtls$versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$details$modifyAttachmentDtls$versionNo"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$details$readAttachmentDtls$attachmentID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$details$modifyAttachmentDtls$attachmentID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="42.5"
    TITLE="Cluster.Title.Details"
  >
    <CLUSTER
      LABEL_WIDTH="45"
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
    >
      <FIELD LABEL="Field.Label.Description">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$details$readLinkDtls$description"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="details$details$modifyLinkDtls$description"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.DateReceived">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$details$readAttachmentDtls$receiptDate"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="details$details$modifyAttachmentDtls$receiptDate"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
    <CLUSTER
      LABEL_WIDTH="45"
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
    >
      <CONTAINER LABEL="Field.Label.AttachedFile">
        <WIDGET TYPE="FILE_DOWNLOAD">
          <WIDGET_PARAMETER NAME="LINK_TEXT">
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="result$details$readAttachmentDtls$attachmentName"
              />
            </CONNECT>
          </WIDGET_PARAMETER>
          <WIDGET_PARAMETER NAME="PARAMS">
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="key$key$attachmentID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="attachmentID"
              />
            </CONNECT>
          </WIDGET_PARAMETER>
          <WIDGET_PARAMETER NAME="PARAMS">
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="key$key$verificationAttachmentLinkID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="verificationAttachmentLinkID"
              />
            </CONNECT>
          </WIDGET_PARAMETER>
        </WIDGET>
      </CONTAINER>
      <!-- FILE_UPLOAD WIDGET TO UPLOAD FILES -->
      <WIDGET
        LABEL="Field.Label.NewFile"
        TYPE="FILE_UPLOAD"
      >
        <WIDGET_PARAMETER NAME="CONTENT">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="details$details$modifyAttachmentDtls$attachmentContents"
            />
          </CONNECT>
        </WIDGET_PARAMETER>


        <WIDGET_PARAMETER NAME="FILE_NAME">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="details$details$modifyAttachmentDtls$attachmentName"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
      </WIDGET>
    </CLUSTER>
    <CLUSTER
      LABEL_WIDTH="45"
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
    >
      <FIELD LABEL="Field.Label.FileLocation">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$details$readAttachmentDtls$fileLocation"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="details$details$modifyAttachmentDtls$fileLocation"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.FileReference">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$details$readAttachmentDtls$fileReference"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="details$details$modifyAttachmentDtls$fileReference"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
    <!-- BEGIN, CR00292013, DJ-->
    <CLUSTER
      LABEL_WIDTH="45"
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
    >
      <!-- END, CR00292013 -->
      <FIELD LABEL="Field.Label.DocumentType">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$details$readAttachmentDtls$documentType"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="details$details$modifyAttachmentDtls$documentType"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
  </CLUSTER>


</VIEW>
