<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Address look up by postal code view page							    -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE CLASS="BDMAddressEvidenceWizard" NAME="DISPLAY"
      OPERATION="getAddressSearchWizardDetails" />     
  <SERVER_INTERFACE CLASS="BDMAddressEvidenceWizard" NAME="ACTION"
      OPERATION="searchAddressWizardDetails" PHASE="ACTION" ACTION_ID_PROPERTY="actionIDProperty" />
  
   <MENU MODE="WIZARD_PROGRESS_BAR">
    <CONNECT>
      <SOURCE NAME="CONSTANT" PROPERTY="SearchAddressEvidenceWizard"/>
    </CONNECT>
  </MENU>

  <PAGE_PARAMETER NAME="wizardStateID"/>
  <PAGE_PARAMETER NAME="searchID"/>
  <PAGE_PARAMETER NAME="addressDetails" />
  <PAGE_PARAMETER NAME="countryID" />

  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="wizardStateID"/>
    <TARGET NAME="DISPLAY" PROPERTY="wizardStateID$wizardStateID"/>
  </CONNECT>


  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="wizardStateID" />
    <TARGET NAME="ACTION" PROPERTY="wizardStateID$wizardStateID"/>
  </CONNECT>

 <CONNECT>
    <SOURCE NAME="CONSTANT" PROPERTY="SearchAddressEvidenceWizardStep2" />
    <TARGET NAME="ACTION" PROPERTY="currentStepNum"/>
  </CONNECT>
  
  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step2DataDtls$country"/>
    <TARGET NAME="ACTION" PROPERTY="wizardDtls$step2DataDtls$country"/>
  </CONNECT>
  

  <ACTION_SET BOTTOM="true">
	<ACTION_CONTROL LABEL="ActionControl.Label.Back">
      <LINK DISMISS_MODAL="false" PAGE_ID="BDMSearchAddressPopupStep1" SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE  NAME="PAGE" PROPERTY="wizardStateID"  />
          <TARGET NAME="PAGE"  PROPERTY="wizardStateID" />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
    <ACTION_CONTROL LABEL="ActionControl.Label.Save" TYPE="SUBMIT" ACTION_ID="NEXTPAGE">
      <LINK DISMISS_MODAL="false" PAGE_ID="BDMAddressSearch_resolveAddressWizardPage" SAVE_LINK="false" >
        <CONNECT>
          <SOURCE NAME="ACTION"  PROPERTY="wizardStateID$wizardStateID"/>
          <TARGET NAME="PAGE" PROPERTY="wizardStateID" />
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="CONSTANT"  PROPERTY="SearchAddressEvidenceWizard"/>
          <TARGET NAME="PAGE" PROPERTY="sourcePageAction" />
        </CONNECT>
        <CONNECT>
          <SOURCE  NAME="PAGE"  PROPERTY="searchID"  />
          <TARGET NAME="PAGE" PROPERTY="searchID"/>
        </CONNECT>
         <CONNECT>
          <SOURCE  NAME="PAGE"  PROPERTY="addressDetails"  />
          <TARGET NAME="PAGE" PROPERTY="addressDetails"/>
        </CONNECT>
         <CONNECT>
          <SOURCE  NAME="PAGE"  PROPERTY="countryID"  />
          <TARGET NAME="PAGE" PROPERTY="countryID"/>
        </CONNECT>
		
		 
		    <CONNECT>
		        <SOURCE NAME="ACTION" PROPERTY="clientSelectedAddressLine1"/>		      
		        <TARGET NAME="PAGE" PROPERTY="clientSelectedAddressLine1"/>
		      </CONNECT>
		  
		  <CONNECT>
		        <SOURCE NAME="ACTION"  PROPERTY="clientSelectedAddressLine2" />
				  <TARGET NAME="PAGE" PROPERTY="clientSelectedAddressLine2" />
		      </CONNECT>
		
		   	<CONNECT>
		        <SOURCE NAME="ACTION" PROPERTY="clientSelectedPOBox" />		      
		        <TARGET NAME="PAGE" PROPERTY="clientSelectedPOBox" />
		      </CONNECT>
		  
			
		
		
		
		
		
		
      </LINK>
    </ACTION_CONTROL>
    <ACTION_CONTROL ALIGNMENT="LEFT" LABEL="ActionControl.Label.Cancel" />
  </ACTION_SET>
  
  <CONNECT>
	  <SOURCE NAME="DISPLAY" PROPERTY="country"/>
	  <TARGET NAME="JSCRIPT_REF" PROPERTY="countryRef"/>
  </CONNECT>
  
  <CONNECT>
	  <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step2DataDtls$addressSelectedInd"/>
	  <TARGET NAME="JSCRIPT_REF" PROPERTY="addressSelectedRef"/>
  </CONNECT>
	
  </VIEW>	
	