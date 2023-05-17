<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2013,2020. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
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
  <PAGE_PARAMETER NAME="applicationID"/>


  <SERVER_INTERFACE
    CLASS="VerificationApplication"
    NAME="DISPLAY"
    OPERATION="fetchVerificationDetails"
  />


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
    LABEL_WIDTH="20"
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
              PROPERTY="attachmentLinkID"
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
		        PROPERTY="attachmentLinkID"
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
		      PROPERTY="type"
		    />
		  </CONNECT>
		</FIELD>
		<WIDGET TYPE="FILE_DOWNLOAD">
		    <WIDGET_PARAMETER NAME="LINK_TEXT">
		      <CONNECT>
		        <SOURCE
		          NAME="DISPLAY"
		          PROPERTY="name"
		        />
		      </CONNECT>
		    </WIDGET_PARAMETER>
		    <WIDGET_PARAMETER NAME="PARAMS">
		      <CONNECT>
		        <SOURCE
		          NAME="DISPLAY"
		          PROPERTY="attachmentLinkID"
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
          PROPERTY="providedBy"
        />
      </CONNECT>
    </FIELD>

    <FIELD
      LABEL="Field.Label.Date"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="receivedDate"
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
      <INLINE_PAGE PAGE_ID="ApplicationVerification_viewVerificationItemProvisionDetails">
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
            PROPERTY="applicationID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="applicationID"
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
          PAGE_ID="ApplicationVerificationApplication_addAttachmentForItem"
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
              PROPERTY="applicationID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="applicationID"
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


</VIEW>
