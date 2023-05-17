<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to create a Attachment 	                    -->
<VIEW
  PAGE_ID="ApplicationVerificationApplication_addAttachment"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="VerificationApplication"
    NAME="DISPLAY"
    OPERATION="readPageContextDescriptionForAttachment"
  />


  <SERVER_INTERFACE
    CLASS="VerificationApplication"
    NAME="DISPLAY1"
    OPERATION="fetchVerificationDetails"
  />


  <SERVER_INTERFACE
    CLASS="ApplicationVerification"
    NAME="ACTION"
    OPERATION="createVerificationAttachment"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="verificationItemProvidedID"/>
  <PAGE_PARAMETER NAME="description"/>
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
      PROPERTY="verificationItemProvidedID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="verificationItemProvidedID"
    />
  </CONNECT>


  <!-- BEGIN, CR00080781, POH -->
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
  <!-- END, CR00080781 -->


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


  <!-- BEGIN, CR00050198, NRV-->
  <!-- BEGIN, HAPR 64653, NK-->
  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
  >
    <!-- END, HARP 64653-->
    <!-- END, CR00050198-->
    <!--Parent Cluster  -->


    <FIELD LABEL="Field.Label.Description">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="description"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.DateReceived"
      USE_DEFAULT="true"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="receiptDate"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <!-- BEGIN, CR00050198, NRV-->
  <!-- BEGIN, HAPR 64653, NK-->
  <CLUSTER
    DESCRIPTION="Cluster.Description"
    LABEL_WIDTH="45"
    NUM_COLS="2"
  >
    <!-- END, HARP 64653-->
    <!-- END, CR00050198-->


    <!-- FILE_UPLOAD WIDGET TO UPLOAD FILES -->
    <WIDGET
      LABEL="Field.Label.File"
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


  </CLUSTER>


  <!-- BEGIN, CR00050198, NRV-->
  <!-- BEGIN, HAPR 64653, NK-->
  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
  >
    <!-- END, HARP 64653-->
    <!-- END, CR00050198-->
    <FIELD LABEL="Field.Label.FileLocation">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fileLocation"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.FileReference">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fileReference"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <!-- BEGIN, CR00050198, NRV-->
  <!-- BEGIN, HAPR 64653, NK-->
  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
  >
    <!-- END, HARP 64653-->
    <!-- END, CR00050198-->
    <FIELD LABEL="Field.Label.DocumentType">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="documentType"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
