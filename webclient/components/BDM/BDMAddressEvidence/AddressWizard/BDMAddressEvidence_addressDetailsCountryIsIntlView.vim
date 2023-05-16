<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Address details view page										    -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

	
	<CONNECT>
		<SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step2DataDtls$country"/>
	    <TARGET NAME="ACTION" PROPERTY="clientSelectedCity"/>
	</CONNECT>
	
	<CLUSTER LABEL_WIDTH="45" NUM_COLS="1">
		<FIELD LABEL="Cluster.Field.Label.AddressLineOne">
			<CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="clientSelectedUnitNumber"/>
		    </CONNECT>
		    <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="clientSelectedUnitNumber"/>
		    </CONNECT>
	    </FIELD>
	    <FIELD LABEL="Cluster.Field.Label.AddressLineTwo">
		    <CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="clientSelectedAddressLine1"/>
		    </CONNECT>
		    <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="clientSelectedAddressLine1" />
		    </CONNECT>
	    </FIELD>
	    <FIELD LABEL="Cluster.Field.Label.AddressLineThree">
		    <CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="clientSelectedAddressLine2"/>
		    </CONNECT>
		    <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="clientSelectedAddressLine2" />
		    </CONNECT>
	    </FIELD>
	    <!-- <FIELD LABEL="Cluster.Field.Label.Country">
			<CONNECT>
		        <SOURCE NAME="PAGE" PROPERTY="country"/>
		    </CONNECT>
		</FIELD> -->
		 <FIELD LABEL="Cluster.Field.Label.Country">
			<CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step2DataDtls$country"/>
		    </CONNECT>
		</FIELD>
	</CLUSTER>
</VIEW>
