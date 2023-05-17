<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM

  Copyright IBM Corporation 2013,2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Description                                                        -->
<!-- =================================================================  -->
<!-- This page will be invoked when the Integrated Case strategy is defined-->
<!-- This page has conditional clusters which are related to            -->
<!-- Integrated Case details                                            -->

<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="icDtls$createNewInd"/>
    <TARGET NAME="ACTION" PROPERTY="systemICCreateNewInd" />
  </CONNECT>

  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="icCaseTabList"/>
    <TARGET NAME="ACTION" PROPERTY="systemICCaseTabList" />
  </CONNECT>  

  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="icClientTabList"/>
    <TARGET NAME="ACTION" PROPERTY="systemICClientTabList" />
  </CONNECT>  

  
	<CLUSTER TITLE="Cluster.Title.IntegratedCase" SHOW_LABELS="false">
		<CONDITION>
			<IS_TRUE
				NAME="DISPLAY"
				PROPERTY="icDetailsInd"
			/>
		</CONDITION>
		<CLUSTER LABEL_WIDTH="35" DESCRIPTION="Cluster.Description.ICCreateNew" >
			<CONDITION>
				<IS_TRUE
					NAME="DISPLAY"
					PROPERTY="icDtls$showCreateNewInd"
				/>
			</CONDITION>
			<FIELD LABEL="Field.Label.CreateNewIC" >
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="icDtls$createNewInd"/>
				</CONNECT>
				<CONNECT>
					<TARGET NAME="ACTION" PROPERTY="createNewInd" />
				</CONNECT>
			</FIELD>
		</CLUSTER>

		<LIST >
			<CONDITION>
				<IS_TRUE
					NAME="DISPLAY"
					PROPERTY="icCaseListInd"
				/>
			</CONDITION>      
		
			<CONTAINER>
				<WIDGET TYPE="MULTISELECT">
					<WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
						<CONNECT>
							<SOURCE NAME="DISPLAY" PROPERTY="icDtls$caseDtls$dtls$caseID" />
						</CONNECT>
					</WIDGET_PARAMETER>
					<WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
						<CONNECT>
							<TARGET NAME="ACTION" PROPERTY="caseTabList" />
						</CONNECT>
					</WIDGET_PARAMETER>
				</WIDGET>
			</CONTAINER>

			<FIELD LABEL="Field.Label.CaseReference" WIDTH="18">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="icDtls$caseDtls$dtls$caseReference"/>
				</CONNECT>
			</FIELD>

			<FIELD LABEL="Field.Label.Clients" WIDTH="34">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="icDtls$caseDtls$dtls$caseClients"/>
				</CONNECT>
			</FIELD>

			<FIELD LABEL="Field.Label.Date" WIDTH="24">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="icDtls$caseDtls$dtls$caseCreationDate"/>
				</CONNECT>
			</FIELD>

			<FIELD LABEL="Field.Label.Owner" WIDTH="24">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="icDtls$caseDtls$dtls$caseOwner"/>
				</CONNECT>
				<LINK
					OPEN_MODAL="true"
					PAGE_ID="Case_resolveOrgObjectTypeHome"
					WINDOW_OPTIONS="width=900"
				>
					<CONNECT>
						<SOURCE
							NAME="DISPLAY"
							PROPERTY="icDtls$caseDtls$dtls$caseOwnerUsername"
						/>
						<TARGET
							NAME="PAGE"
							PROPERTY="userName"
						/>
					</CONNECT>
					<CONNECT>
						<SOURCE
							NAME="DISPLAY"
							PROPERTY="icDtls$caseDtls$dtls$orgObjectReference"
						/>
						<TARGET
							NAME="PAGE"
							PROPERTY="orgObjectReference"
						/>
					</CONNECT>
					<CONNECT>
						<SOURCE
							NAME="DISPLAY"
							PROPERTY="icDtls$caseDtls$dtls$orgObjectType"
						/>
						<TARGET
							NAME="PAGE"
							PROPERTY="orgObjectType"
						/>
					</CONNECT>
				</LINK>
			</FIELD>
		</LIST>

		<LIST  DESCRIPTION="Cluster.Description.ICCaseList">
			<CONDITION>
				<IS_TRUE
					NAME="DISPLAY"
					PROPERTY="icDtls$isCaseListOnlyInd"
				/>
			</CONDITION>      
		
			<CONTAINER>
				<WIDGET TYPE="MULTISELECT">
					<WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
						<CONNECT>
							<SOURCE NAME="DISPLAY" PROPERTY="icDtls$caseDtls$dtls$caseID" />
						</CONNECT>
					</WIDGET_PARAMETER>
					<WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
						<CONNECT>
							<TARGET NAME="ACTION" PROPERTY="caseTabList" />
						</CONNECT>
					</WIDGET_PARAMETER>
				</WIDGET>
			</CONTAINER>

			<FIELD LABEL="Field.Label.CaseReference" WIDTH="18">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="icDtls$caseDtls$dtls$caseReference"/>
				</CONNECT>
			</FIELD>

			<FIELD LABEL="Field.Label.Clients" WIDTH="34">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="icDtls$caseDtls$dtls$caseClients"/>
				</CONNECT>
			</FIELD>

			<FIELD LABEL="Field.Label.Date" WIDTH="24">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="icDtls$caseDtls$dtls$caseCreationDate"/>
				</CONNECT>
			</FIELD>

			<FIELD LABEL="Field.Label.Owner" WIDTH="24">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="icDtls$caseDtls$dtls$caseOwner"/>
				</CONNECT>
				<LINK
					OPEN_MODAL="true"
					PAGE_ID="Case_resolveOrgObjectTypeHome"
					WINDOW_OPTIONS="width=900"
				>
					<CONNECT>
						<SOURCE
							NAME="DISPLAY"
							PROPERTY="icDtls$caseDtls$dtls$caseOwnerUsername"
						/>
						<TARGET
							NAME="PAGE"
							PROPERTY="userName"
						/>
					</CONNECT>
					<CONNECT>
						<SOURCE
							NAME="DISPLAY"
							PROPERTY="icDtls$caseDtls$dtls$orgObjectReference"
						/>
						<TARGET
							NAME="PAGE"
							PROPERTY="orgObjectReference"
						/>
					</CONNECT>
					<CONNECT>
						<SOURCE
							NAME="DISPLAY"
							PROPERTY="icDtls$caseDtls$dtls$orgObjectType"
						/>
						<TARGET
							NAME="PAGE"
							PROPERTY="orgObjectType"
						/>
					</CONNECT>
				</LINK>
			</FIELD>
		</LIST>

  <CLUSTER
    LABEL_WIDTH="28"
    NUM_COLS="1"
    DESCRIPTION="Cluster.Description.PrimaryClient"
  >
			<CONDITION>
				<IS_TRUE
					NAME="DISPLAY"
					PROPERTY="icDtls$clientListInd"
				/>
			</CONDITION>

    <FIELD
      LABEL="Field.Label.PrimaryClient"
      USE_BLANK="false"
      WIDTH="50"
    >
      	<SCRIPT 
      		ACTION="disablecheck()"
      		EVENT="ONCHANGE"  
			SCRIPT_FILE="EnableCheckBoxes.js"
  		/>
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="icDtls$clientDtls$nameAndAgeDetailsList$dtls$participantID"
          NAME="DISPLAY"
          PROPERTY="icDtls$clientDtls$nameAndAgeDetailsList$dtls$participantName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="primaryClientID"
        />
      </CONNECT>
    </FIELD>

  </CLUSTER>
  
		<LIST DESCRIPTION="Cluster.Description.ICClientList" >
			<CONDITION>
				<IS_TRUE
					NAME="DISPLAY"
					PROPERTY="icDtls$clientListInd"
				/>
			</CONDITION>
		 
			<CONTAINER>
				<WIDGET TYPE="MULTISELECT">
					<WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
						<CONNECT>
							<SOURCE NAME="DISPLAY" PROPERTY="icDtls$clientDtls$nameAndAgeDetailsList$dtls$participantID"/>
						</CONNECT>
					</WIDGET_PARAMETER>
					<WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
						<CONNECT>
							<TARGET NAME="ACTION" PROPERTY="clientTabList" />
						</CONNECT>
					</WIDGET_PARAMETER>
				</WIDGET>
			</CONTAINER>
			<FIELD LABEL="Client.Label.Name" WIDTH="50">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="icDtls$clientDtls$nameAndAgeDetailsList$dtls$participantName"/>
				</CONNECT>
			</FIELD>
			<FIELD LABEL="Client.Label.Age" WIDTH="50">
				<CONNECT>
					<SOURCE NAME="DISPLAY" PROPERTY="icDtls$clientDtls$nameAndAgeDetailsList$dtls$age"/>
				</CONNECT>
			</FIELD>
		</LIST>
				
	</CLUSTER>

</VIEW>


