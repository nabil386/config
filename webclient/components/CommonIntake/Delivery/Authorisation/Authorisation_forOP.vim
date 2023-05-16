<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM

  Copyright IBM Corporation 2013,2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Description                                                        -->
<!-- =================================================================  -->
<!-- This page will be invoked when the Outcome Plan strategy is defined-->
<!-- This page has conditional clusters which are related to            -->
<!-- outcome plan details                                               -->

<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

  
  <CONNECT>
    <SOURCE NAME="CONSTANT" PROPERTY="Constant.True"/>
    <TARGET NAME="ACTION" PROPERTY="opStrategyInd" />
  </CONNECT>

  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="opDtls$caseTabList"/>
    <TARGET NAME="ACTION" PROPERTY="systemOPCaseTabList" />
  </CONNECT>  

  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="opDtls$clientTabList"/>
    <TARGET NAME="ACTION" PROPERTY="systemOPClientTabList" />
  </CONNECT>  

  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="opDtls$createNewInd"/>
    <TARGET NAME="ACTION" PROPERTY="systemOPCreateNewInd" />
  </CONNECT>  
	<CLUSTER TITLE="Cluster.Title.OutcomePlan" SHOW_LABELS="false">
		<CONDITION>
			<IS_TRUE
				NAME="DISPLAY"
				PROPERTY="opDetailsInd"
			/>
		</CONDITION>
		<CLUSTER LABEL_WIDTH="35" DESCRIPTION="Cluster.Description.CreateNew" >
			<CONDITION>
				<IS_TRUE
					NAME="DISPLAY"
					PROPERTY="opDtls$showCreateNewInd"
				/>
			</CONDITION>
			<FIELD LABEL="Field.Label.CreateNewOutcomePlan" >
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="opDtls$createNewInd"/>
				</CONNECT>
				<CONNECT>
					<TARGET NAME="ACTION" PROPERTY="createOPNewInd" />
				</CONNECT>
			</FIELD>
		</CLUSTER>
		<LIST >
			<CONDITION>
				<IS_TRUE
					NAME="DISPLAY"
					PROPERTY="caseListInd"
				/>
			</CONDITION>      
		
			<CONTAINER>
				<WIDGET TYPE="MULTISELECT">				    
					<WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">					
						<CONNECT>
							<SOURCE NAME="DISPLAY" PROPERTY="opDtls$caseDtls$dtls$caseID" />
						</CONNECT>
						<CONNECT>
							<SOURCE NAME="DISPLAY" PROPERTY="opDtls$caseDtls$dtls$caseClientConcernRoles" />
						</CONNECT>
					</WIDGET_PARAMETER>
					<WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
						<CONNECT>
							<TARGET NAME="ACTION" PROPERTY="opCaseTabList" />
						</CONNECT>
					</WIDGET_PARAMETER>
				</WIDGET>
			</CONTAINER>

			<FIELD LABEL="Field.Label.CaseReference" WIDTH="18">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="opDtls$caseDtls$dtls$caseReference"/>
				</CONNECT>
			</FIELD>
			<FIELD LABEL="Field.Label.Clients" WIDTH="34">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="opDtls$caseDtls$dtls$caseClients"/>
				</CONNECT>
			</FIELD>

			<FIELD LABEL="Field.Label.Type" WIDTH="24">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="opDtls$caseDtls$dtls$name"/>
				</CONNECT>
			</FIELD>

			<FIELD LABEL="Field.Label.Owner" WIDTH="24">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="opDtls$caseDtls$dtls$caseOwner"/>
				</CONNECT>
				<LINK
					OPEN_MODAL="true"
					PAGE_ID="Case_resolveOrgObjectTypeHome"
					WINDOW_OPTIONS="width=900"
				>
					<CONNECT>
						<SOURCE
							NAME="DISPLAY"
							PROPERTY="opDtls$caseDtls$dtls$caseOwnerUsername"
						/>
						<TARGET
							NAME="PAGE"
							PROPERTY="userName"
						/>
					</CONNECT>
					<CONNECT>
						<SOURCE
							NAME="DISPLAY"
							PROPERTY="opDtls$caseDtls$dtls$orgObjectReference"
						/>
						<TARGET
							NAME="PAGE"
							PROPERTY="orgObjectReference"
						/>
					</CONNECT>
					<CONNECT>
						<SOURCE
							NAME="DISPLAY"
							PROPERTY="opDtls$caseDtls$dtls$orgObjectType"
						/>
						<TARGET
							NAME="PAGE"
							PROPERTY="orgObjectType"
						/>
					</CONNECT>
				</LINK>
			</FIELD>
		</LIST>

		<LIST  DESCRIPTION="Cluster.Description.CaseList">
			<CONDITION>
				<IS_TRUE
					NAME="DISPLAY"
					PROPERTY="opDtls$isCaseListOnlyInd"
				/>
			</CONDITION>      
		
			<CONTAINER>
				<WIDGET TYPE="MULTISELECT">
					<WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
						<CONNECT>
							<SOURCE NAME="DISPLAY" PROPERTY="opDtls$caseDtls$dtls$caseID" />
						</CONNECT>
						<CONNECT>
							<SOURCE NAME="DISPLAY" PROPERTY="opDtls$caseDtls$dtls$caseClientConcernRoles" />
						</CONNECT>
					</WIDGET_PARAMETER>
					<WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
						<CONNECT>
							<TARGET NAME="ACTION" PROPERTY="opCaseTabList" />
						</CONNECT>
					</WIDGET_PARAMETER>
				</WIDGET>
			</CONTAINER>

			<FIELD LABEL="Field.Label.CaseReference" WIDTH="18">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="opDtls$caseDtls$dtls$caseReference"/>
				</CONNECT>
			</FIELD>

			<FIELD LABEL="Field.Label.Clients" WIDTH="34">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="opDtls$caseDtls$dtls$caseClients" />
				</CONNECT>
			</FIELD>

			<FIELD LABEL="Field.Label.Type" WIDTH="24">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="opDtls$caseDtls$dtls$name"/>
				</CONNECT>
			</FIELD>

			<FIELD LABEL="Field.Label.Owner" WIDTH="24">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="opDtls$caseDtls$dtls$caseOwner"/>
				</CONNECT>
				<LINK
					OPEN_MODAL="true"
					PAGE_ID="Case_resolveOrgObjectTypeHome"
					WINDOW_OPTIONS="width=900"
				>
					<CONNECT>
						<SOURCE
							NAME="DISPLAY"
							PROPERTY="opDtls$caseDtls$dtls$caseOwnerUsername"
						/>
						<TARGET
							NAME="PAGE"
							PROPERTY="userName"
						/>
					</CONNECT>
					<CONNECT>
						<SOURCE
							NAME="DISPLAY"
							PROPERTY="opDtls$caseDtls$dtls$orgObjectReference"
						/>
						<TARGET
							NAME="PAGE"
							PROPERTY="orgObjectReference"
						/>
					</CONNECT>
					<CONNECT>
						<SOURCE
							NAME="DISPLAY"
							PROPERTY="opDtls$caseDtls$dtls$orgObjectType"
						/>
						<TARGET
							NAME="PAGE"
							PROPERTY="orgObjectType"
						/>
					</CONNECT>
				</LINK>
			</FIELD>
		</LIST>

		<LIST DESCRIPTION="Cluster.Description.ClientList" >
			<CONDITION>
				<IS_TRUE
					NAME="DISPLAY"
					PROPERTY="opDtls$clientListInd"
				/>
			</CONDITION>
		 
			<CONTAINER>
				<WIDGET TYPE="MULTISELECT">
					<WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
						<CONNECT>
							<SOURCE NAME="DISPLAY" PROPERTY="opDtls$clientDtls$nameAndAgeDetailsList$dtls$participantID"/>
						</CONNECT>
					</WIDGET_PARAMETER>
					<WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
						<CONNECT>
							<TARGET NAME="ACTION" PROPERTY="opClientTabList" />
						</CONNECT>
					</WIDGET_PARAMETER>
				</WIDGET>
			</CONTAINER>
			<FIELD LABEL="Client.Label.Name" WIDTH="50">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="opDtls$clientDtls$nameAndAgeDetailsList$dtls$participantName"/>
				</CONNECT>
			</FIELD>
			<FIELD LABEL="Client.Label.Age" WIDTH="50">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="opDtls$clientDtls$nameAndAgeDetailsList$dtls$age"/>
				</CONNECT>
			</FIELD>
		</LIST>
		
		<LIST DESCRIPTION="Cluster.Description.CreateNew.ClientList" >
			<CONDITION>
				<IS_TRUE
					NAME="DISPLAY"
					PROPERTY="opDtls$clientListIndWithNewInd"
				/>
			</CONDITION>
		 
			<CONTAINER>
				<WIDGET TYPE="MULTISELECT">
					<WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
						<CONNECT>
							<SOURCE NAME="DISPLAY" PROPERTY="opDtls$clientDtls$nameAndAgeDetailsList$dtls$participantID"/>
						</CONNECT>
					</WIDGET_PARAMETER>
					<WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
						<CONNECT>
							<TARGET NAME="ACTION" PROPERTY="opClientTabList" />
						</CONNECT>
					</WIDGET_PARAMETER>
				</WIDGET>
			</CONTAINER>
			<FIELD LABEL="Client.Label.Name" WIDTH="50">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="opDtls$clientDtls$nameAndAgeDetailsList$dtls$participantName"/>
				</CONNECT>
			</FIELD>
			<FIELD LABEL="Client.Label.Age" WIDTH="50">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="opDtls$clientDtls$nameAndAgeDetailsList$dtls$age"/>
				</CONNECT>
			</FIELD>
		</LIST>
		
	</CLUSTER>

	<CLUSTER TITLE="Cluster.Title.Comments" SHOW_LABELS="false">
		<CONDITION>
			<IS_FALSE
				NAME="DISPLAY"
				PROPERTY="opDtls$isCommentsOnlyInd"
			/>
		</CONDITION>
    <FIELD LABEL="Field.Label.Comments" HEIGHT="4">
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="opComment"/>
      </CONNECT>
    </FIELD>
  </CLUSTER>

	<CLUSTER SHOW_LABELS="false">
		<CONDITION>
			<IS_TRUE
				NAME="DISPLAY"
				PROPERTY="opDtls$isCommentsOnlyInd"
			/>
		</CONDITION>
    <FIELD LABEL="Field.Label.Comments" HEIGHT="4">
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="opComment"/>
      </CONNECT>
    </FIELD>
  </CLUSTER>
  
</VIEW>


