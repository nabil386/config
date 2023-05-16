<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Address details view page										    -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

	<CLUSTER LABEL_WIDTH="45" NUM_COLS="2">
		<FIELD LABEL="Cluster.Field.Label.AddressUnitNumber">
			<CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="clientSelectedUnitNumber"/>
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="clientSelectedUnitNumber"/>
		      </CONNECT>
		    </FIELD>
		    	<FIELD LABEL="Cluster.Field.Label.AddressPOBox">
		   	<CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="clientSelectedPOBox" />
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="clientSelectedPOBox" />
		      </CONNECT>
		    </FIELD>
	</CLUSTER>
	
	<CLUSTER LABEL_WIDTH="45" NUM_COLS="2">
		 <FIELD LABEL="Cluster.Field.Label.StreetNumber">
		    <CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="clientSelectedAddressLine1"/>
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="clientSelectedAddressLine1" />
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
	</CLUSTER>
	
	<CLUSTER LABEL_WIDTH="45" NUM_COLS="2">
		 <FIELD LABEL="Cluster.Field.Label.City">
		    <CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="clientSelectedCity"/>
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="clientSelectedCity"/>
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
	
	<CLUSTER LABEL_WIDTH="45" NUM_COLS="2">
		<FIELD LABEL="Cluster.Field.Label.ZipCode">
			<CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="clientSelectedZipCode"/>
		      </CONNECT>
		      <CONNECT>
		        <TARGET NAME="ACTION" PROPERTY="clientSelectedZipCode"/>
		      </CONNECT>
		    </FIELD>
	</CLUSTER>
	

	
</VIEW>
