<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Address basic information page										    -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


	<!--Address Information cluster -->
  <CLUSTER
    LABEL_WIDTH="49"
    NUM_COLS="2"
    TITLE="Cluster.Title.AddressInformation"
  >
		<CLUSTER LABEL_WIDTH="45" NUM_COLS="1">
		    <FIELD LABEL="Cluster.Field.Label.ReceiveDate" WIDTH="80"  >
		      <CONNECT>
		        <SOURCE NAME="DISPLAY"  PROPERTY="receiveDate"/>
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION"  PROPERTY="receiveDate"/>
		      </CONNECT>
		    </FIELD>
		    <FIELD LABEL="Cluster.Field.Label.From">
		      <CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="fromDate" />
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="fromDate" />
		      </CONNECT>
		    </FIELD>
		   <CLUSTER  >
			    <CONDITION TYPE="DYNAMIC">
			        <SCRIPT EXPRESSION="addressIsResidential" />
				</CONDITION> 
			   <FIELD LABEL="Cluster.Field.Label.Preferred"  >
			      <CONNECT>
			        <SOURCE NAME="DISPLAY"  PROPERTY="preferredInd" />
			      </CONNECT>
			      <CONNECT>
			        <TARGET NAME="ACTION"  PROPERTY="preferredInd" />
			      </CONNECT>
			    </FIELD>
			</CLUSTER>
		 </CLUSTER>
		 <CLUSTER LABEL_WIDTH="45" NUM_COLS="1">
		    <FIELD LABEL="Cluster.Field.Label.Type"  CONTROL_REF="addressTypeRef">
		      <CONNECT>
		      
		      <!--BEGIN TASK-28446 replace it with addressType form results -->
		      <SOURCE NAME="EXISTING_ADDRESS"  PROPERTY="result$addressType"/>
		        <!--SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step1DataDtls$addressType"/
		        END TASK-28446-->
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="wizardDtls$step1DataDtls$addressType"/>
		      </CONNECT>
		    </FIELD>
		    <FIELD LABEL="Cluster.Field.Label.To" USE_DEFAULT="false" >
		      <CONNECT>
		        <SOURCE  NAME="DISPLAY"  PROPERTY="toDate" />
		      </CONNECT>
		      <CONNECT>
		        <TARGET  NAME="ACTION"  PROPERTY="toDate" />
		      </CONNECT>
		    </FIELD>
		    <FIELD LABEL="Cluster.Field.Label.ChangeReason" USE_DEFAULT="false" >
		      <CONNECT>
		        <SOURCE  NAME="DISPLAY"  PROPERTY="result$dtls$step1DataDtls$changeReason" />
		      </CONNECT>
		      <CONNECT>
		        <TARGET  NAME="ACTION"  PROPERTY="wizardDtls$step1DataDtls$changeReason" />
		      </CONNECT>
		    </FIELD>
		  </CLUSTER>
	</CLUSTER>
	
		<!-- BEGIN 61872 DEV: Address Changes -->
		<CLUSTER NUM_COLS="2" TITLE="Cluster.Title.ReceiptDetailsView">
			<CLUSTER NUM_COLS="1">
			  	<FIELD LABEL="Field.Label.ReceivedFromView">
			  		<CONNECT>
			  			<SOURCE NAME="DISPLAY"  PROPERTY="result$dtls$step1DataDtls$bdmReceivedFrom" />
			  		</CONNECT>
			  		<CONNECT>
			  			<TARGET NAME="ACTION"  PROPERTY="wizardDtls$step1DataDtls$bdmReceivedFrom" />
			  		</CONNECT>
			  	</FIELD>
			</CLUSTER>
			<CLUSTER NUM_COLS="1" >
		  		<FIELD LABEL="Field.Label.ReceivedFromCountryView"  USE_DEFAULT="false" USE_BLANK="true">
		  			<CONNECT>
		  				<SOURCE NAME="DISPLAY"  PROPERTY="result$dtls$step1DataDtls$bdmReceivedFromCountry" />
		  			</CONNECT>
		  			<CONNECT>
		  				<TARGET NAME="ACTION"  PROPERTY="wizardDtls$step1DataDtls$bdmReceivedFromCountry" />
		  			</CONNECT>
		  		</FIELD>
			  	<FIELD LABEL="Field.Label.ModeOfReceiptView"  USE_DEFAULT="false" USE_BLANK="true">
			  		<CONNECT>
			  			<SOURCE NAME="DISPLAY"  PROPERTY="result$dtls$step1DataDtls$bdmModeOfReceipt" />
			  		</CONNECT>
			  		<CONNECT>
			  			<TARGET NAME="ACTION"  PROPERTY="wizardDtls$step1DataDtls$bdmModeOfReceipt" />
			  		</CONNECT>
			  	</FIELD>
			</CLUSTER>
		</CLUSTER>
		<!-- END 61872 DEV: Address Changes -->

</VIEW>