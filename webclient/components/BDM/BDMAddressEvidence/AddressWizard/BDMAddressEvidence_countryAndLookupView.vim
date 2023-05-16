<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Address look up by postal code view page							    -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

  <!-- Country cluster -->
  <CLUSTER SUMMARY="Summary.Address.Country">
    <FIELD  LABEL="Cluster.Field.Label.Country"  CONTROL_REF="countryRef" >
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="country"/>
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="country"/>
      </CONNECT>
    </FIELD>
  </CLUSTER>
  
  <!-- Address search criteria cluster -->
  <CLUSTER  TITLE="Cluster.Title.SearchCriteria">
    <CONDITION TYPE="DYNAMIC">
        <SCRIPT EXPRESSION="searchByPostalCodeEnabled" />
	</CONDITION>
     
    <FIELD LABEL="Cluster.Field.Label.PostCode" USE_BLANK="true">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step2DataDtls$postalCode"  />
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="wizardDtls$step2DataDtls$postalCode"  />
      </CONNECT>
    </FIELD>
    
    <ACTION_SET ALIGNMENT="CENTER"  TOP="false">
   
      <ACTION_CONTROL LABEL="ActionControl.Label.Search" TYPE="SUBMIT"  ACTION_ID="SEARCH"  DEFAULT="true"  IMAGE="SearchButton"> 
      	<!--  <LINK PAGE_ID="THIS"  SAVE_LINK="false"/> -->
      	<!-- Link Back to this page again -->
        <LINK PAGE_ID="BDMAddressEvidence_createAddressWizardStep2"  DISMISS_MODAL="false"  SAVE_LINK="false">  
          <CONNECT>
              <SOURCE NAME="ACTION" PROPERTY="wizardStateID$wizardStateID"/>
              <TARGET NAME="PAGE" PROPERTY="wizardStateID"/>
          </CONNECT>   
        </LINK>
      </ACTION_CONTROL>
      
      <ACTION_CONTROL LABEL="ActionControl.Label.Reset" TYPE="SUBMIT" ACTION_ID="RESETPAGE"  IMAGE="ResetButton">
        <!-- Link Back to this page again -->
        <LINK PAGE_ID="BDMAddressEvidence_createAddressWizardStep2"  DISMISS_MODAL="false"  SAVE_LINK="false">      
          <CONNECT>
              <SOURCE NAME="ACTION" PROPERTY="wizardStateID$wizardStateID"/>
              <TARGET NAME="PAGE" PROPERTY="wizardStateID"/>
          </CONNECT>   
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>  
    
    <LIST TITLE="List.Title.SearchResults" PAGINATED="false">
       <CONTAINER LABEL="Field.Label.Select">
	      <WIDGET TYPE="SINGLESELECT" LABEL="Field.Label.Select">
	        <WIDGET_PARAMETER NAME="SELECT_SOURCE">
	            <CONNECT>
	                <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step2DataDtls$addressList$addressData" />
	            </CONNECT>
	        </WIDGET_PARAMETER>
	        <WIDGET_PARAMETER NAME="SELECT_TARGET">
	            <CONNECT>
	                <TARGET NAME="ACTION" PROPERTY="step2DataDtls$addressData" />
	            </CONNECT>
	        </WIDGET_PARAMETER>
	      </WIDGET>
      </CONTAINER> 
      
      <FIELD LABEL="Field.Label.Address" WIDTH="80">
        <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step2DataDtls$addressList$formattedAddressData" />
        </CONNECT>
      </FIELD>
    
    </LIST>           
  </CLUSTER>

</VIEW>