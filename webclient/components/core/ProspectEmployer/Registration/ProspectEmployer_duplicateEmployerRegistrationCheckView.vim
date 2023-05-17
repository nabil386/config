<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2002-2008, 2010 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows the user to search a prospect employer not already    -->
<!-- registered as employer.                                                -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <LIST TITLE="List.Title.SearchResults">


    <FIELD
      LABEL="Field.Title.ReferenceNumber"
      WIDTH="10"
    >
      <!-- BEGIN, CR00234017, DJ -->
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="key$key$referenceNumber"
        />
      </CONNECT>
      <!-- END, CR00234017 -->
      <LINK PAGE_ID="Employer_home">
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <!-- BEGIN, CR00234017, DJ -->
    <FIELD
      LABEL="Field.Title.TradingName"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$dtls$dtlsList$tradingName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.RegisteredName"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$dtls$dtlsList$registeredName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.AddressLineOne"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="key$key$addressDtls$addressLine1"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.City"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="key$key$addressDtls$city"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00234017 -->


  </LIST>
</VIEW>
