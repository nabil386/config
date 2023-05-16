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
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This view contains attachment view details.                            -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="BDMVerificationApplication"
    NAME="DISPLAY"
    OPERATION="readVerificationAttachment"
  />


  <SERVER_INTERFACE
    CLASS="BDMVerificationApplication"
    NAME="DISPLAY1"
    OPERATION="fetchVerificationDetails"
  />


  <PAGE_PARAMETER NAME="verificationAttachmentLinkID"/>
  <PAGE_PARAMETER NAME="attachmentID"/>
  <PAGE_PARAMETER NAME="VDIEDLinkID"/>
  <PAGE_PARAMETER NAME="evidenceDescriptorID"/>
  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="contextDescription"/>
  <PAGE_PARAMETER NAME="dataItemName"/>
  <!-- BEGIN, CR00080534, RF -->
  <PAGE_PARAMETER NAME="concernRoleID"/>
  <!-- END, CR00080534 -->


  <!-- Map Attachment details to view Attachment Details -->
  <!-- display-phase bean in order to retrieve details -->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="verificationAttachmentLinkID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$verificationAttachmentLinkID"
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


  <!-- Cluster with Details -->
  <CLUSTER NUM_COLS="2">
    <FIELD LABEL="Field.Label.Description">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="readLinkDtls$description"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.FileLocation">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fileLocation"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DateReceived">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="receiptDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="recordStatus"
        />
      </CONNECT>
    </FIELD>
    
    <CLUSTER  NUM_COLS="2" STYLE="cluster-cpr-no-border">
		 <FIELD LABEL="Field.Label.FileName">
			<LINK URI_SOURCE_NAME="DISPLAY"
				URI_SOURCE_PROPERTY="result$extrenalFileLink$fileLocationURL"  OPEN_NEW= "true"/>
			
				<CONNECT>
					<SOURCE NAME="DISPLAY"
						PROPERTY="result$extrenalFileLink$fileName" />
				</CONNECT>
				
			</FIELD>
			
		</CLUSTER>
<!-- This code is commented as to browse button will be enabled to upload document from Document repository in R2 -->
		
<!--   Remove Browse Button
    <CONTAINER LABEL="Field.Label.FileName">
    
    
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
              PROPERTY="readAttachmentDtls$attachmentID"
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
              PROPERTY="readLinkDtls$verificationAttachmentLinkID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="verificationAttachmentLinkID"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
      </WIDGET>
    </CONTAINER>

-->
    <FIELD LABEL="Field.Label.FileReference">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fileReference"
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
    </FIELD>


  </CLUSTER>


</VIEW>
