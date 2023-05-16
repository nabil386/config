<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2010, 2020. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2010-2011 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to view a Verification Details Page.         -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_PARAMETER NAME="VDIEDLinkID"/>
  <PAGE_PARAMETER NAME="evidenceDescriptorID"/>
  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="dataItemName"/>


  <SERVER_INTERFACE
    CLASS="BDMVerificationApplication"
    NAME="DISPLAY"
    OPERATION="fetchVerificationDetails"
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
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="verificationLinkedID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="25"
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >


    <FIELD LABEL="Field.Label.EvidenceDescription">
      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="summary"
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

  <LIST TITLE="List.Title.SubmittedDocuments" STYLE="submitted-documents-list-style">
  	<CONDITION>
  		<IS_TRUE 
  			NAME="DISPLAY"
  		  	PROPERTY="isDisplayEnabledInd"
  		>
  		</IS_TRUE>
  	</CONDITION>
    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL
        IMAGE="AddButton"
        LABEL="ActionControl.Label.AcceptDocument"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="VerificationApplication_acceptSubmittedDocumentForItem"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$submittedDocumentDetails$submittedDocumentDetailsList$attachmentLinkID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="attachmentLinkID"
            />
          </CONNECT>          
	    </LINK>
	  </ACTION_CONTROL>
	      
	  <ACTION_CONTROL LABEL="ActionControl.Label.RejectDocument">
	  	<LINK OPEN_MODAL="true"
		  PAGE_ID="VerificationApplication_rejectSubmittedDocumentForItem"
		 >
		  <CONNECT>
			<SOURCE
				NAME="DISPLAY"
		        PROPERTY="submittedDocumentDetailsList$attachmentLinkID"
		    />
		    <TARGET
		        NAME="PAGE"
		        PROPERTY="attachmentLinkID"
		    />
		  </CONNECT>
		</LINK>
	  </ACTION_CONTROL>
	</ACTION_SET>
	
	<CONTAINER LABEL="Field.Label.SubmittedDocumentType">
		<FIELD>
		  <CONNECT>
		    <SOURCE
		      NAME="DISPLAY"
		      PROPERTY="submittedDocumentDetailsList$type"
		    />
		  </CONNECT>
		</FIELD>
		<WIDGET TYPE="FILE_DOWNLOAD">
		    <WIDGET_PARAMETER NAME="LINK_TEXT">
		      <CONNECT>
		        <SOURCE
		          NAME="DISPLAY"
		          PROPERTY="submittedDocumentDetailsList$name"
		        />
		      </CONNECT>
		    </WIDGET_PARAMETER>
		    <WIDGET_PARAMETER NAME="PARAMS">
		      <CONNECT>
		        <SOURCE
		          NAME="DISPLAY"
		          PROPERTY="submittedDocumentDetailsList$attachmentLinkID"
		        />
		        <TARGET
		          NAME="PAGE"
		          PROPERTY="attachmentLinkID"
		        />
		      </CONNECT>
		    </WIDGET_PARAMETER>
	    </WIDGET>
	</CONTAINER>

    <FIELD
      LABEL="Field.Label.Providedby"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="submittedDocumentDetailsList$providedBy"
        />
      </CONNECT>
    </FIELD>

    <FIELD
      LABEL="Field.Label.Date"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="submittedDocumentDetailsList$receivedDate"
        />
      </CONNECT>
	</FIELD>
  </LIST>
	
  <CLUSTER>
  	<CONDITION>
  		<IS_TRUE 
  		  NAME="DISPLAY"
  		  PROPERTY="isDisplayEnabledInd"
  		>
  		</IS_TRUE>
  	</CONDITION>
	<ACTION_SET
		TOP="false"
		ALIGNMENT="RIGHT"
	>
	  <ACTION_CONTROL
	    IMAGE="AddButton"
	    LABEL="ActionControl.Label.AcceptAll"
	  >
	  	<LINK
	     	OPEN_MODAL="true"
	        PAGE_ID="VerificationApplication_acceptAllSubmittedDocumentsForItem"
	     >
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
	              PROPERTY="caseID"
	            />
	            <TARGET
	              NAME="PAGE"
	              PROPERTY="caseID"
	            />
	        </CONNECT>
          
	        </LINK>
	  </ACTION_CONTROL>
	  
	  <ACTION_CONTROL LABEL="ActionControl.Label.RejectAll">
	  	 <LINK
	     	OPEN_MODAL="true"
	        PAGE_ID="VerificationApplication_rejectAllSubmittedDocumentsForItem"
	     >
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
	              PROPERTY="caseID"
	            />
	            <TARGET
	              NAME="PAGE"
	              PROPERTY="caseID"
	            />
	        </CONNECT>
          
	        </LINK>
	  </ACTION_CONTROL>
	</ACTION_SET>
</CLUSTER>


 









  <LIST TITLE="List.Title.VerificationItemsReceived">
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
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
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
              PROPERTY="result$readVerificationDetails$userProvidedVerificationItemDetailsList$userProvidedVerificationItemDetails$verificationItemProvidedID"
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
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="VerificationApplication_removeVerificationItemProvision"
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


    <FIELD
      LABEL="Field.Label.Item"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="verificationItemName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Providedby"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="receivedFrom"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Date"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateReceived"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ExpiryDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="expiryDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ReceivedLevel"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="userProvidedVerificationItemDetails$level"
        />
      </CONNECT>
    </FIELD>
  </LIST>
  

 <LIST TITLE="List.Title.RejectedDocuments" STYLE="submitted-documents-list-style">
  	<CONDITION>
  		<IS_TRUE 
  			NAME="DISPLAY"
  		  	PROPERTY="isDisplayEnabledInd"
  		>
  		</IS_TRUE>
  	</CONDITION>
   
	<CONTAINER LABEL="Field.Label.SubmittedDocumentType">
		<FIELD>
		  <CONNECT>
		    <SOURCE
		      NAME="DISPLAY"
		      PROPERTY="dtls$type"
		    />
		  </CONNECT>
		</FIELD>
		<WIDGET TYPE="FILE_DOWNLOAD">
		    <WIDGET_PARAMETER NAME="LINK_TEXT">
		      <CONNECT>
		        <SOURCE
		          NAME="DISPLAY"
		          PROPERTY="dtls$name"
		        />
		      </CONNECT>
		    </WIDGET_PARAMETER>
		    <WIDGET_PARAMETER NAME="PARAMS">
		      <CONNECT>
		        <SOURCE
		          NAME="DISPLAY"
		          PROPERTY="dtls$attachmentLinkID"
		        />
		        <TARGET
		          NAME="PAGE"
		          PROPERTY="attachmentLinkID"
		        />
		      </CONNECT>
		    </WIDGET_PARAMETER>
	    </WIDGET>
	</CONTAINER>

    <FIELD
      LABEL="Field.Label.Providedby"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$providedBy"
        />
      </CONNECT>
    </FIELD>

    <FIELD
      LABEL="Field.Label.Date"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$receivedDate"
        />
      </CONNECT>
	</FIELD>
	
	<FIELD
      LABEL="Field.Label.Rejection.Reason"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="rejectionReason"
        />
      </CONNECT>
    </FIELD>
	<FIELD
       LABEL="Field.Label.Other.Comment"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="otherComments"
        />
      </CONNECT>
    </FIELD>
  </LIST>
	

</VIEW>
