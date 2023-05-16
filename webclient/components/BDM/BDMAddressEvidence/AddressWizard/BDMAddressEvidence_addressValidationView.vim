<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Address validation view page										    -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

	<CLUSTER >
			<!--<FIELD ALIGNMENT="CENTER">
			CONNECT>
		        <SOURCE NAME="TEXT" PROPERTY="Cluster.Field.Text.InformationText"/>
		      </CONNECT>
		    </FIELD-->
		    
		    <FIELD ALIGNMENT="CENTER">
		    <CONNECT>
		        <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step4DataDtls$formattedAddressData"/>
		      </CONNECT>
		    </FIELD>
				
	</CLUSTER>	

</VIEW>