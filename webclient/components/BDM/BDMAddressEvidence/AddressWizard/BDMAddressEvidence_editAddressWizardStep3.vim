<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Address look up by postal code view page							    -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  


  <SERVER_INTERFACE CLASS="BDMAddressEvidenceWizard"  NAME="DISPLAY" OPERATION="getAddressEvidenceWizardDetails" PHASE="DISPLAY"/>
  <SERVER_INTERFACE CLASS="BDMAddressEvidenceWizard" NAME="ACTION"  OPERATION="setAddressEvidenceWizardDetails"  PHASE="ACTION"  ACTION_ID_PROPERTY="actionIDProperty" />
  
   <MENU MODE="WIZARD_PROGRESS_BAR">
    <CONNECT>
      <SOURCE NAME="CONSTANT" PROPERTY="EditAddressEvidenceWizard"/>
    </CONNECT>
  </MENU>

  <PAGE_PARAMETER NAME="wizardStateID"/>


  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="wizardStateID"/>
    <TARGET NAME="DISPLAY" PROPERTY="wizardStateID$wizardStateID"/>
  </CONNECT>


  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="wizardStateID" />
    <TARGET NAME="ACTION" PROPERTY="wizardStateID$wizardStateID"/>
  </CONNECT>

 <CONNECT>
    <SOURCE NAME="CONSTANT" PROPERTY="EditAddressEvidenceWizardStep3" />
    <TARGET NAME="ACTION" PROPERTY="currentStepNum"/>
  </CONNECT>
  
  <!-- fulfill the mandatory fields requirements from step 1 -->
  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step1DataDtls$addressType"/>
    <TARGET NAME="ACTION" PROPERTY="wizardDtls$step1DataDtls$addressType"/>
  </CONNECT>
  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step1DataDtls$receiveDate"/>
    <TARGET NAME="ACTION" PROPERTY="wizardDtls$step1DataDtls$receiveDate"/>
  </CONNECT>
   <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step1DataDtls$fromDate"/>
    <TARGET NAME="ACTION" PROPERTY="wizardDtls$step1DataDtls$fromDate"/>
  </CONNECT>
  <CONNECT>
<SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step1DataDtls$editInd"/>
<TARGET NAME="ACTION" PROPERTY="wizardDtls$step1DataDtls$editInd"/>
</CONNECT>
<CONNECT>
<SOURCE NAME="DISPLAY" PROPERTY="result$dtls$caseParticipantRoleID"/>
<TARGET NAME="ACTION" PROPERTY="wizardDtls$caseParticipantRoleID"/>
</CONNECT>
  <!-- fulfill the mandatory fields requirements from step 2 -->
  <CONNECT>
    <SOURCE NAME="DISPLAY" PROPERTY="result$dtls$step2DataDtls$country"/>
    <TARGET NAME="ACTION" PROPERTY="wizardDtls$step2DataDtls$country"/>
  </CONNECT>
  

  <ACTION_SET BOTTOM="true">
	<ACTION_CONTROL LABEL="ActionControl.Label.Back">
      <LINK DISMISS_MODAL="false" PAGE_ID="BDMAddressEvidence_editAddressWizardStep2" SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE  NAME="PAGE" PROPERTY="wizardStateID"  />
          <TARGET NAME="PAGE"  PROPERTY="wizardStateID" />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
    <ACTION_CONTROL LABEL="ActionControl.Label.Save" TYPE="SUBMIT" ACTION_ID="NEXTPAGE">
      <LINK DISMISS_MODAL="false" PAGE_ID="BDMAddressEvidence_resolveAddressWizardPage" SAVE_LINK="false" >
        <CONNECT>
          <SOURCE NAME="ACTION"  PROPERTY="wizardStateID$wizardStateID"/>
          <TARGET NAME="PAGE" PROPERTY="wizardStateID" />
        </CONNECT>
        <CONNECT>
          <SOURCE NAME="CONSTANT"  PROPERTY="EditAddressEvidence"/>
          <TARGET NAME="PAGE" PROPERTY="sourcePageAction" />
        </CONNECT>
         <CONNECT>
          <SOURCE NAME="ACTION"  PROPERTY="isValidAddress"/>
          <TARGET NAME="PAGE" PROPERTY="isValidAddress" />
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
	