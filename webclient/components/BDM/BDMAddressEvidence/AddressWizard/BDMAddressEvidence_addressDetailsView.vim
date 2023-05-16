<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Address details view page										    -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

	<CLUSTER NUM_COLS="2">
		<CONDITION TYPE="DYNAMIC">
        	<SCRIPT EXPRESSION="addressIsReadOnly" />
		</CONDITION>
		<CLUSTER LABEL_WIDTH="45" NUM_COLS="1">
			<FIELD LABEL="Cluster.Field.Label.AddressUnitNumber">
			<CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step3DataDtls$clientSelectedUnitNumber"/>
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="wizardDtls$step3DataDtls$clientSelectedUnitNumber"/>
		      </CONNECT>
		    </FIELD>
		    
		    <FIELD LABEL="Cluster.Field.Label.StreetNumber">
		    <CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step3DataDtls$clientSelectedAddressLine1"/>
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="wizardDtls$step3DataDtls$clientSelectedAddressLine1"/>
		      </CONNECT>
		    </FIELD>
		
		    <FIELD LABEL="Cluster.Field.Label.City">
		    <CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="clientSelectedCity"/>
		      </CONNECT>
		    </FIELD>
			
			<FIELD LABEL="Cluster.Field.Label.Postal">
			<CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="clientSelectedPostalCode"/>
		      </CONNECT>
		    </FIELD>
			
		</CLUSTER>
		<CLUSTER LABEL_WIDTH="45" NUM_COLS="1">
			
		   	<FIELD LABEL="Cluster.Field.Label.AddressPOBox">
		   	<CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step3DataDtls$clientSelectedPOBox" />
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="wizardDtls$step3DataDtls$clientSelectedPOBox" />
		      </CONNECT>
		    </FIELD>
		    
			<FIELD LABEL="Cluster.Field.Label.StreetName">
			 <CONNECT>
		        <SOURCE NAME="DISPLAY"  PROPERTY="clientSelectedAddressLine2" />
		      </CONNECT>
		    </FIELD>
		
		
			<FIELD LABEL="Cluster.Field.Label.Province">
			<CONNECT>
		        <SOURCE NAME="DISPLAY"  PROPERTY="clientSelectedProvince" />
		      </CONNECT>
		    </FIELD>
		
		</CLUSTER>			
	</CLUSTER>	
		<CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step3DataDtls$clientSelectedCity"/>
				<TARGET NAME="ACTION" PROPERTY="wizardDtls$step3DataDtls$clientSelectedCity"/>
		      </CONNECT>
			<CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step3DataDtls$clientSelectedPostalCode"/>
				<TARGET NAME="ACTION" PROPERTY="wizardDtls$step3DataDtls$clientSelectedPostalCode"/>
		      </CONNECT>
			<CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step3DataDtls$clientSelectedAddressLine2"/>
				<TARGET NAME="ACTION" PROPERTY="wizardDtls$step3DataDtls$clientSelectedAddressLine2"/>
		      </CONNECT>
			<CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step3DataDtls$clientSelectedProvince"/>
				<TARGET NAME="ACTION" PROPERTY="wizardDtls$step3DataDtls$clientSelectedProvince"/>
		      </CONNECT>
	<!--
	<CLUSTER NUM_COLS="2">
		<CONDITION TYPE="DYNAMIC">
        	<SCRIPT EXPRESSION="countryIsCA" />
		</CONDITION>
		<CLUSTER LABEL_WIDTH="45" NUM_COLS="1">
			<FIELD LABEL="Cluster.Field.Label.AddressUnitNumber">
			<CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step3DataDtls$clientSelectedUnitNumber"/>
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="wizardDtls$step3DataDtls$clientSelectedUnitNumber"/>
		      </CONNECT>
		    </FIELD>
		    
		    <FIELD LABEL="Cluster.Field.Label.StreetNumber">
		    <CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step3DataDtls$clientSelectedAddressLine1"/>
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="wizardDtls$step3DataDtls$clientSelectedAddressLine1" />
		      </CONNECT>
		    </FIELD>
		
		    <FIELD LABEL="Cluster.Field.Label.City">
		    <CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step3DataDtls$clientSelectedCity"/>
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="wizardDtls$step3DataDtls$clientSelectedCity"/>
		      </CONNECT>
		    </FIELD>
			
			<FIELD LABEL="Cluster.Field.Label.Postal">
			<CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step3DataDtls$clientSelectedPostalCode"/>
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="wizardDtls$step3DataDtls$clientSelectedPostalCode"/>
		      </CONNECT>
		    </FIELD>
		</CLUSTER>
		<CLUSTER LABEL_WIDTH="45" NUM_COLS="1">
			
		   	<FIELD LABEL="Cluster.Field.Label.AddressPOBox">
		   	<CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step3DataDtls$clientSelectedPOBox" />
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="wizardDtls$step3DataDtls$clientSelectedPOBox" />
		      </CONNECT>
		    </FIELD>
		    
			<FIELD LABEL="Cluster.Field.Label.StreetName">
			 <CONNECT>
		        <SOURCE NAME="DISPLAY"  PROPERTY="result$dtls$step3DataDtls$clientSelectedAddressLine2" />
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION"  PROPERTY="wizardDtls$step3DataDtls$clientSelectedAddressLine2" />
		      </CONNECT>
		    </FIELD>
		
			<FIELD LABEL="Cluster.Field.Label.Province" USE_BLANK="true">
			<CONNECT>
		        <SOURCE NAME="DISPLAY"  PROPERTY="result$dtls$step3DataDtls$clientSelectedProvince" />
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION"  PROPERTY="wizardDtls$step3DataDtls$clientSelectedProvince" />
		      </CONNECT>
		    </FIELD>
		    
		</CLUSTER>			
	</CLUSTER>	

	<CLUSTER NUM_COLS="2">
		<CONDITION TYPE="DYNAMIC">
        	<SCRIPT EXPRESSION="countryIsUS" />
		</CONDITION>
		<CLUSTER LABEL_WIDTH="45" NUM_COLS="1">
			<FIELD LABEL="Cluster.Field.Label.AddressUnitNumber">
			<CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="clientSelectedUnitNumber"/>
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="clientSelectedUnitNumber"/>
		      </CONNECT>
		    </FIELD>
		    
		    <FIELD LABEL="Cluster.Field.Label.StreetNumber">
		    <CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="clientSelectedAddressLine1"/>
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="clientSelectedAddressLine1" />
		      </CONNECT>
		    </FIELD>
		
		    <FIELD LABEL="Cluster.Field.Label.City">
		    <CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="clientSelectedCity"/>
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="clientSelectedCity"/>
		      </CONNECT>
		    </FIELD>
			
			<FIELD LABEL="Cluster.Field.Label.ZipCode">
			<CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="clientSelectedZipCode"/>
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="clientSelectedZipCode"/>
		      </CONNECT>
		    </FIELD>
		</CLUSTER>
		<CLUSTER LABEL_WIDTH="45" NUM_COLS="1">
			
		   	<FIELD LABEL="Cluster.Field.Label.AddressPOBox">
		   	<CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="clientSelectedPOBox" />
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="clientSelectedPOBox" />
		      </CONNECT>
		    </FIELD>
		    
			<FIELD LABEL="Cluster.Field.Label.StreetName">
			 <CONNECT>
		        <SOURCE NAME="DISPLAY"  PROPERTY="clientSelectedAddressLine2" />
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION"  PROPERTY="clientSelectedAddressLine2" />
		      </CONNECT>
		    </FIELD>
		
			<FIELD LABEL="Cluster.Field.Label.State" USE_BLANK="true">
			<CONNECT>
		        <SOURCE NAME="DISPLAY"  PROPERTY="clientSelectedState" />
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION"  PROPERTY="clientSelectedState" />
		      </CONNECT>
		    </FIELD>
		   
		</CLUSTER>			
	</CLUSTER>
	
	<CLUSTER NUM_COLS="2">
		<CONDITION TYPE="DYNAMIC">
        	<SCRIPT EXPRESSION="countryIsIntl" />
		</CONDITION>
			<CLUSTER LABEL_WIDTH="45" NUM_COLS="1">
			<FIELD LABEL="Cluster.Field.Label.AddressUnitNumber">
			<CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="clientSelectedUnitNumber"/>
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="clientSelectedUnitNumber"/>
		      </CONNECT>
		    </FIELD>
		    
		    <FIELD LABEL="Cluster.Field.Label.AddressLineOne">
		    <CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="clientSelectedAddressLine1"/>
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="clientSelectedAddressLine1" />
		      </CONNECT>
		    </FIELD>
		
		    <FIELD LABEL="Cluster.Field.Label.City">
		    <CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="clientSelectedCity"/>
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="clientSelectedCity"/>
		      </CONNECT>
		    </FIELD>
			
			<FIELD LABEL="Cluster.Field.Label.ZipCode">
			<CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="clientSelectedZipCode"/>
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="clientSelectedZipCode"/>
		      </CONNECT>
		    </FIELD>
		</CLUSTER>
		<CLUSTER LABEL_WIDTH="45" NUM_COLS="1">
			
		   	<FIELD LABEL="Cluster.Field.Label.AddressPOBox">
		   	<CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="clientSelectedPOBox" />
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="clientSelectedPOBox" />
		      </CONNECT>
		    </FIELD>
		    
			<FIELD LABEL="Cluster.Field.Label.AddressLineTwo">
			 <CONNECT>
		        <SOURCE NAME="DISPLAY"  PROPERTY="clientSelectedAddressLine2" />
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION"  PROPERTY="clientSelectedAddressLine2" />
		      </CONNECT>
		    </FIELD>
		
			<FIELD LABEL="Cluster.Field.Label.Region">
			<CONNECT>
		        <SOURCE NAME="DISPLAY"  PROPERTY="clientSelectedRegion" />
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION"  PROPERTY="clientSelectedRegion" />
		      </CONNECT>
		    </FIELD>
		</CLUSTER>	
	</CLUSTER> -->
</VIEW>
