<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2007-2008, 2010-2011 Curam Software Ltd.                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows you to view the list of thirt party requests.    	    -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <LIST>
    <!-- BEGIN, CR00214223, PS -->
    <ACTION_SET TYPE="LIST_ROW_MENU">


      <ACTION_CONTROL LABEL="ActionControl.Label.SendRequest">
        <CONDITION>
          <IS_TRUE
            NAME="COMMON_VIEW"
            PROPERTY="result$tprDtls$dtls$dtls$tprNotSentInd"
          />
        </CONDITION>
        <!-- BEGIN, CR00237410, AK -->
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="DecisionAssistApplication_sendThirdPartyRequest"
          WINDOW_OPTIONS="width=500"
        >
          <!-- END, CR00237410 -->
          <CONNECT>
            <SOURCE
              NAME="COMMON_VIEW"
              PROPERTY="result$tprDtls$dtls$dtls$thirdPartyRequestID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="thirdPartyRequestID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="COMMON_VIEW"
              PROPERTY="result$ddID$dtls$dtls$determinationDeliveryID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="determinationDeliveryID"
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
      <!-- END, CR00214223 -->
      <ACTION_CONTROL
        LABEL="ActionControl.Label.EditRequest"
        TYPE="ACTION"
      >
        <CONDITION>
          <IS_TRUE
            NAME="COMMON_VIEW"
            PROPERTY="result$tprDtls$dtls$dtls$tprNotSentInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="DecisionAssistApplication_editThirdPartyRequest"
        >
          <CONNECT>
            <SOURCE
              NAME="COMMON_VIEW"
              PROPERTY="result$tprDtls$dtls$dtls$thirdPartyRequestID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="thirdPartyRequestID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="COMMON_VIEW"
              PROPERTY="result$ddID$dtls$dtls$determinationDeliveryID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="determinationDeliveryID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <!-- BEGIN, CR00214223, PS -->
      <ACTION_CONTROL LABEL="ActionControl.Label.CancelRequest">
        <CONDITION>
          <IS_TRUE
            NAME="COMMON_VIEW"
            PROPERTY="result$tprDtls$dtls$dtls$tprNotSentInd"
          />
        </CONDITION>
        <!-- BEGIN, CR00237410, AK -->
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="DecisionAssistApplication_cancelThirdPartyRequest"
          WINDOW_OPTIONS="width=500"
        >
          <!-- END, CR00237410 -->
          <CONNECT>
            <SOURCE
              NAME="COMMON_VIEW"
              PROPERTY="result$tprDtls$dtls$dtls$thirdPartyRequestID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="thirdPartyRequestID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="COMMON_VIEW"
              PROPERTY="result$ddID$dtls$dtls$determinationDeliveryID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="determinationDeliveryID"
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
      <!-- END, CR00214223 -->
      <ACTION_CONTROL
        LABEL="ActionControl.Label.RecordAnswers"
        TYPE="ACTION"
      >
        <CONDITION>
          <IS_TRUE
            NAME="COMMON_VIEW"
            PROPERTY="result$tprDtls$dtls$dtls$tprSentInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="DecisionAssistApplication_recordThirdPartyAnswer"
        >
          <CONNECT>
            <SOURCE
              NAME="COMMON_VIEW"
              PROPERTY="result$tprDtls$dtls$dtls$thirdPartyRequestID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="thirdPartyRequestID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="COMMON_VIEW"
              PROPERTY="result$ddID$dtls$dtls$determinationDeliveryID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="determinationDeliveryID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL
        LABEL="ActionControl.Label.EditAnswers"
        TYPE="ACTION"
      >
        <CONDITION>
          <IS_TRUE
            NAME="COMMON_VIEW"
            PROPERTY="result$tprDtls$dtls$dtls$tprAnsRcvdInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="DecisionAssistApplication_editThirdPartyAnswer"
        >
          <CONNECT>
            <SOURCE
              NAME="COMMON_VIEW"
              PROPERTY="result$tprDtls$dtls$dtls$thirdPartyRequestID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="thirdPartyRequestID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="COMMON_VIEW"
              PROPERTY="result$ddID$dtls$dtls$determinationDeliveryID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="determinationDeliveryID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <!-- BEGIN, CR00214223, PS -->
    </ACTION_SET>


    <FIELD LABEL="Field.Label.ThirdParty">
      <!-- END, CR00214223 -->
      <CONNECT>
        <SOURCE
          NAME="COMMON_VIEW"
          PROPERTY="result$tprDtls$dtls$dtls$thirdPartyName"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00214223, PS -->
    <FIELD LABEL="Field.Label.DateRequested">
      <!-- END, CR00214223 -->
      <CONNECT>
        <SOURCE
          NAME="COMMON_VIEW"
          PROPERTY="result$tprDtls$dtls$dtls$requestedDateTime"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00214223, PS -->
    <FIELD LABEL="Field.Label.CreatedBy">
      <!-- END, CR00214223 -->
      <CONNECT>
        <SOURCE
          NAME="COMMON_VIEW"
          PROPERTY="result$tprDtls$dtls$dtls$createdBy"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00214223, PS -->
    <FIELD LABEL="Field.Label.Status">
      <!-- END, CR00214223 -->
      <CONNECT>
        <SOURCE
          NAME="COMMON_VIEW"
          PROPERTY="result$tprDtls$dtls$dtls$status"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00214223, PS -->
    <FIELD LABEL="Field.Label.LastUpdate">
      <!-- END, CR00214223 -->
      <CONNECT>
        <SOURCE
          NAME="COMMON_VIEW"
          PROPERTY="result$tprDtls$dtls$dtls$lastUpdatedDateTime"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00214223, PS -->
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="DecisionAssistApplication_viewThirdPartyRequest">
        <CONNECT>
          <SOURCE
            NAME="COMMON_VIEW"
            PROPERTY="result$tprDtls$dtls$dtls$thirdPartyRequestID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="thirdPartyRequestID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="COMMON_VIEW"
            PROPERTY="result$ddID$dtls$dtls$determinationDeliveryID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="determinationDeliveryID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>
    <!-- END, CR00214223 -->
  </LIST>
</VIEW>
