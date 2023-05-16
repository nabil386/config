<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2010 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This view is used to create the product delivery case.                 -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="IntegratedCaseSampleProduct"
    NAME="DISPLAY"
    OPERATION="listAllExistingOutcomes"
  />


  <SERVER_INTERFACE
    CLASS="Product"
    NAME="DISPLAYDP"
    OPERATION="listDeliveryPatternPreferredMethodOfPayment1"
  />


  <SERVER_INTERFACE
    CLASS="IntegratedCaseSampleProduct"
    NAME="ACTION"
    OPERATION="createCase"
    PHASE="ACTION"
  />


  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="ACTION"
        PROPERTY="result$warnings$dtls$warning"
      />
    </CONNECT>
  </INFORMATIONAL>


  <PAGE_PARAMETER NAME="concernRoleID"/>
  <PAGE_PARAMETER NAME="productID"/>
  <PAGE_PARAMETER NAME="productProviderID"/>
  <PAGE_PARAMETER NAME="locationID"/>
  <PAGE_PARAMETER NAME="productDeliveryPatternID"/>
  <PAGE_PARAMETER NAME="integratedCaseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="productID"
    />
    <TARGET
      NAME="DISPLAYDP"
      PROPERTY="key$productID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="DISPLAYDP"
      PROPERTY="key$concernRoleID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="clientID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="productID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="productID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="productProviderID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="productProviderID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="locationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="providerLocationID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="integratedCaseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="integratedCaseID"
    />
  </CONNECT>


  <CLUSTER LABEL_WIDTH="39">


    <FIELD LABEL="Field.Label.ReceiptDate">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="receivedDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ExpectedOutcome"
      WIDTH="85"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="outcomeID"
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="expectedOutcome"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.PreferredPaymentMethod">
      <CONNECT>
        <SOURCE
          NAME="DISPLAYDP"
          PROPERTY="preferredDeliveryMethod"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.DeliveryMethod"
      USE_BLANK="false"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="productDeliveryPatternID"
          NAME="DISPLAYDP"
          PROPERTY="name"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="productDeliveryPatternID"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
