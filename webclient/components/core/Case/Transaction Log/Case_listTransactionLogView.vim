<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2009, 2010 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view to display Case's recent transactions.               -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
  </CONNECT>


  <LIST>


    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Case_resolveViewTransactionLogRecord">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="relatedID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="relatedID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="transactionDescription"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="transactionDescription"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="transactionDateTime"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="transactionDateTime"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="userFullName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userFullName"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="eventTypeDesc"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="eventType"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="pageID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageID"
          />
        </CONNECT>


      </INLINE_PAGE>
    </DETAILS_ROW>


    <FIELD
      LABEL="Field.Label.EventType"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="eventTypeDesc"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Description"
      WIDTH="45"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="transactionDescription"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.TransactionDateTime"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="transactionDateTime"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.UserFullname"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="userFullName"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Organization_viewUserDetails"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="username"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
      </LINK>
      <!--END CR00108134-->
    </FIELD>
  </LIST>


</VIEW>
