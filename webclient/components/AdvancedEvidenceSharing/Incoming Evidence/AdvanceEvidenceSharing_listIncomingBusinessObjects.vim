<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2017. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">
	<PAGE_TITLE>
	    <CONNECT>
	        <SOURCE NAME="TEXT" PROPERTY="Page.Title"/>
	    </CONNECT>
	</PAGE_TITLE>
	<PAGE_PARAMETER NAME="caseID"/>
	<SERVER_INTERFACE CLASS="IncomingEvidence" NAME="DISPLAY" OPERATION="getIncomingBusinessObjectDetailsList"/>
	<CONNECT>
	    <SOURCE NAME="PAGE" PROPERTY="caseID"/>
	    <TARGET NAME="DISPLAY" PROPERTY="caseID"/>
	</CONNECT>
	
	<INFORMATIONAL>
		<CONNECT>
			<SOURCE NAME="DISPLAY" PROPERTY="messageText" />
		</CONNECT>
	</INFORMATIONAL>
			
	<CLUSTER>
	    <LIST>
	        <DETAILS_ROW>
	            <INLINE_PAGE PAGE_ID="AdvancedEvidenceSharing_compareBusinessObject">
	                <CONNECT>
	                    <SOURCE NAME="DISPLAY" PROPERTY="commaSeparatedEvidenceDescriptorIDs"/>
	                    <TARGET NAME="PAGE" PROPERTY="commaSeparatedEvidenceDescriptorIDs"/>
	                </CONNECT>
	            </INLINE_PAGE>
	        </DETAILS_ROW>
	        <FIELD LABEL="List.Title.EvidenceType" WIDTH="18">
	            <CONNECT>
	                <SOURCE NAME="DISPLAY" PROPERTY="type"/>
	            </CONNECT>
	        </FIELD>
	        <FIELD LABEL="List.Title.Participant" WIDTH="12">
	            <CONNECT>
	                <SOURCE NAME="DISPLAY" PROPERTY="participantName"/>
	            </CONNECT>
	        </FIELD>
	        <FIELD LABEL="List.Title.Description" WIDTH="42">
	            <CONNECT>
	                <SOURCE NAME="DISPLAY" PROPERTY="description"/>
	            </CONNECT>
	        </FIELD>
	        <FIELD LABEL="List.Title.Period" WIDTH="14">
	            <CONNECT>
	                <SOURCE NAME="DISPLAY" PROPERTY="period"/>
	            </CONNECT>
	        </FIELD>
	        <FIELD LABEL="List.Title.LastUpdate" WIDTH="14" >
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="lastUpdateDesc"/>
				</CONNECT>
				<LINK OPEN_MODAL="true" PAGE_ID="Organization_viewUserDetails" WINDOW_OPTIONS="width=925">
		       		<CONDITION>
		      			<IS_TRUE
					        NAME="DISPLAY"
					        PROPERTY="result$dtls$latestUpdate$internalUserInd"
		      			/>
	    			</CONDITION> 
					<CONNECT>
						<SOURCE NAME="DISPLAY" PROPERTY="createdBy"/>
						<TARGET NAME="PAGE" PROPERTY="userName"/>
					</CONNECT>
				</LINK>
			 </FIELD>	
	    </LIST>
	</CLUSTER>
</VIEW>
