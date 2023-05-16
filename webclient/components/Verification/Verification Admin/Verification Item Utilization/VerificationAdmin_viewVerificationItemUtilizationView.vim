<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2011, 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2011 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Page to View Verification Item Utilization.                            -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText1"
      />
    </CONNECT>
  </PAGE_TITLE>
  
  <SERVER_INTERFACE
    CLASS="VerificationAdministration"
    NAME="DISPLAY"
    OPERATION="readVerificationItemUtilization"
    PHASE="DISPLAY"
  />
  
  
  <PAGE_PARAMETER NAME="verificationItemUtilizationID"/>
  
  
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="verificationItemUtilizationID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$dtls$verificationItemUtilizationID"
    />
  </CONNECT>
  
  
  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
  >
    
    <FIELD LABEL="Field.Label.VerificationItem">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$verificationItemName"
        />
      </CONNECT>
    </FIELD>
    
    <!-- BEGIN, CR00348311, SSK -->
     <FIELD LABEL="Field.Label.UsageType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$readDtls$usageType"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00348311 -->
    <FIELD LABEL="Field.Label.FromDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$readDtls$fromDate"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.SuppliedByClient">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$readDtls$clientSupplied"
        />
      </CONNECT>
    </FIELD>
      
    <FIELD LABEL="Field.Label.AddSecurityID">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$readDtls$addSID"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD CONTROL="SKIP"/>
    
    <FIELD LABEL="Field.Label.Level">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$readDtls$verificationLevel"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.Todate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$readDtls$toDate"
        />
      </CONNECT>
    </FIELD>
    
   
    
    <FIELD LABEL="Field.Label.Mandatory">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$readDtls$mandatory"
        />
      </CONNECT>
    </FIELD>
     <FIELD LABEL="Field.Label.RemoveSecurityID">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$readDtls$removeSID"
        />
      </CONNECT>
    </FIELD>
    
  </CLUSTER>
  
  
  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TITLE="Cluster.Title.ExpiryDetails"
  >
    <FIELD LABEL="Field.Label.ExpiryDays">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$readDtls$expiryDays"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.ExpiryDateFrom">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$readDtls$expiryDateFrom"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.WarningDays">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$readDtls$warningDays"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.Label.ExpiryDateEvent">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$readDtls$expiryDateEvent"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  
  
  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Description"
  >
    <FIELD HEIGHT="4">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$readDtls$comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  
</VIEW>
