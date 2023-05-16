<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2006-2007, 2010-2011 Curam Software Ltd.                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to create a Attachment 	                    -->
<VIEW
  PAGE_ID="VerificationApplication_addAttachment"
   SCRIPT_FILE="BDMVerificationDynamicCluster.js"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="VerificationApplication"
    NAME="DISPLAY"
    OPERATION="readPageContextDescriptionForAttachment"
  />


  <SERVER_INTERFACE
    CLASS="BDMVerificationApplication"
    NAME="DISPLAY1"
    OPERATION="fetchVerificationDetails"
  />


  <SERVER_INTERFACE
    CLASS="BDMVerificationApplication"
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
      NAME="PAGE"
      PROPERTY="verificationItemProvidedID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="inputDetails$createVerificationAttachmentDetails$details$createLinkDtls$verificationItemProvidedID"
    />
  </CONNECT>
  <!-- END, CR00080781 -->
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
  <!-- #TASK 90414 -->
   <!-- FILE_UPLOAD WIDGET TO UPLOAD FILES -->
	<!-- <CLUSTER DESCRIPTION="Cluster.Description" LABEL_WIDTH="45" NUM_COLS="2" 
		> <WIDGET LABEL="Field.Label.File" TYPE="FILE_UPLOAD" > <WIDGET_PARAMETER 
		NAME="CONTENT"> <CONNECT> <TARGET NAME="ACTION" PROPERTY="attachmentContents" 
		/> </CONNECT> </WIDGET_PARAMETER> <WIDGET_PARAMETER NAME="FILE_NAME"> <CONNECT> 
		<TARGET NAME="ACTION" PROPERTY="attachmentName" /> </CONNECT> </WIDGET_PARAMETER> 
		</WIDGET> </CLUSTER> -->
 <!-- #TASK 90414 -->

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
<CLUSTER
      LABEL_WIDTH="45"
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
    >
	<FIELD
      LABEL="Field.Label.Acceptance.Status"
      WIDTH="20"  USE_BLANK="true" CONTROL_REF="statusRef"
    >    
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="acceptanceStatus"
        />
      </CONNECT>
    </FIELD>
	<CLUSTER
      LABEL_WIDTH="45"
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
    >

	<FIELD CONTROL="SKIP"/> 
<FIELD
      LABEL="Field.Label.Rejection.Reason"
      WIDTH="20" USE_BLANK="true" CONTROL_REF="rejectionReasonRef"
    >    
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="rejectionReason"
        />
      </CONNECT>
    </FIELD>
	   
     </CLUSTER>
	
	  </CLUSTER>
 

	  <CLUSTER STYLE="cluster-cpr-no-border" >
 
   <FIELD  HEIGHT="2"
      LABEL="Field.Label.Other.Comment"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="otherComments"
        />
      </CONNECT>
    </FIELD>
     
  </CLUSTER>

</VIEW>
