<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view to display an Investigation Delivery Case Recent     -->
<!-- Changes cluster details.                                               -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

  <LIST TITLE="List.Title.RecentChanges">
      <DETAILS_ROW>
        <INLINE_PAGE PAGE_ID="Case_resolveViewTransactionLogRecord">
          <CONNECT>
            <SOURCE
              NAME="DISPLAYTRANS"
              PROPERTY="relatedID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="relatedID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAYTRANS"
              PROPERTY="transactionDescription"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="transactionDescription"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAYTRANS"
              PROPERTY="transactionDateTime"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="transactionDateTime"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAYTRANS"
              PROPERTY="userFullName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="userFullName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAYTRANS"
              PROPERTY="eventTypeDesc"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="eventType"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAYTRANS"
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
          NAME="DISPLAYTRANS"
          PROPERTY="result$transactionDetailsList$dtlsList$eventTypeDesc"
        />
      </CONNECT>
    </FIELD>
    <FIELD 
      LABEL="Field.Label.Description"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAYTRANS"
          PROPERTY="transactionDescription"
        />
      </CONNECT>
    </FIELD>
    <FIELD 
      LABEL="Field.Label.TransactionDateTime"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAYTRANS"
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
          NAME="DISPLAYTRANS"
          PROPERTY="userFullName"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Case_resolveOrgObjectTypeHome"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="userName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="orgObjectReference"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="orgObjectReference"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="orgObjectType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="orgObjectType"
          />
        </CONNECT>
      </LINK>
    </FIELD>
  </LIST>

</VIEW>
