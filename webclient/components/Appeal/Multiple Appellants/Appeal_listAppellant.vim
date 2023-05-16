<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008, 2010 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows user to list all verification items.                  -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="Appellant"
    NAME="DISPLAY"
    OPERATION="listAppellants"
  />


  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$caseID"
    />
  </CONNECT>
  
  
  <ACTION_SET
    ALIGNMENT="CENTER"
    TOP="false"
  >
    <ACTION_CONTROL
      IMAGE="NewButton"
      LABEL="ActionControl.Label.New"
    >
      <!-- BEGIN, CR00198210, PS -->
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Appeal_createAppellant"
      >
        <!-- END, CR00198210 -->
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$listAppDtls$contextDescription$description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="contextDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
      
    <ACTION_CONTROL
      IMAGE="AcknowledgeReceiptsButton"
      LABEL="ActionControl.Label.AcknowledgedReceipts"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Appeal_acknowledgeReceiptsAppellant"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$listAppDtls$contextDescription$description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>


  <LIST>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Appeal_viewAppellant">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="appellantID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="appellantID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$listAppDtls$listDtls$dtls$appellantName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="appellantName"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$listAppDtls$contextDescription$description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="contextDescription"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>
    
    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Appeal_modifyAppellant"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="appellantID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="appellantID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$listAppDtls$listDtls$dtls$appellantName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="appellantName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$listAppDtls$contextDescription$description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="contextDescription"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      
      
      <ACTION_CONTROL
        IMAGE="DeleteButton"
        LABEL="ActionControl.Label.Delete"
      >
        <!-- BEGIN, CR00198210, PS -->
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Appeal_deleteAppellant"
        >
          <!-- END, CR00198210 -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="appellantID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="appellantID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>
    
    <FIELD
      LABEL="Field.Label.Appellant"
      WIDTH="28"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$listAppDtls$listDtls$dtls$appellantName"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="28"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="appellantTypeCode"
        />
      </CONNECT>
    </FIELD>
    
    <!-- BEGIN, CR00198210, PS -->
    <FIELD
      LABEL="Field.Label.FromDate"
      WIDTH="12"
    >
      <!-- END, CR00198210 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fromDate"
        />
      </CONNECT>
    </FIELD>
    
    <!-- BEGIN, CR00198210, PS -->
    <FIELD
      LABEL="Field.Label.ToDate"
      WIDTH="12"
    >
      <!-- END, CR00198210 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="toDate"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD
      LABEL="Field.Label.ReceiptAcknowledged"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="receiptNoticeIndicator"
        />
      </CONNECT>
    </FIELD>
  </LIST>
</VIEW>
