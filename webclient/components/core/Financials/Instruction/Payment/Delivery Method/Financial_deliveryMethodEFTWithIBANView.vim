<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2013. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- This page is used to display the delivery method details of a          -->
<!-- financial instruction.                                                 -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <FIELD LABEL="Field.Label.NomineeName">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="nomineeName"
      />
    </CONNECT>
  </FIELD>


  <FIELD LABEL="Field.Label.BankSortCode">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="bankAccountSortCode"
      />
    </CONNECT>
    <LINK
      OPEN_MODAL="true"
      PAGE_ID="Organization_viewBankBranchModal"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bankBranchID"
        />
        <TARGET
          NAME="PAGE"
          PROPERTY="bankBranchID"
        />
      </CONNECT>
    </LINK>
  </FIELD>
  

  <FIELD LABEL="Field.Label.Bic">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="bicOpt"
      />
    </CONNECT>
   <LINK
      OPEN_MODAL="true"
      PAGE_ID="Organization_viewBankBranchModal"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bankBranchID"
        />
        <TARGET
          NAME="PAGE"
          PROPERTY="bankBranchID"
        />
      </CONNECT>
    </LINK>
  </FIELD>

  <FIELD LABEL="Field.Label.Method">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="methodOfDelivery"
      />
    </CONNECT>
  </FIELD>

  <!-- BEGIN, CR00372632, GA -->
  <FIELD LABEL="Field.Label.BankAccountRef">
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="bankAccountNumberOpt"
      />
    </CONNECT>
  </FIELD>
  <!-- END, CR00372632 -->


</VIEW>
