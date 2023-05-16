<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
 
  Copyright IBM Corporation 2005, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2005, 2010-2011 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Modifies an existing Delivery Pattern.                                 -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <!-- BEGIN, CR00236627, GP -->
  <SERVER_INTERFACE
    CLASS="Product"
    NAME="DISPLAY"
    OPERATION="readProductDeliveryPattern"
    PHASE="DISPLAY"
  />


  <!-- BEGIN, CR00280529, MV -->
  <SERVER_INTERFACE
    CLASS="Product"
    NAME="ACTION"
    OPERATION="modifyProductDeliveryPatternInfo"
    PHASE="ACTION"
  />
  <!-- END, CR00280529 -->
  <!-- END, CR00236627 -->


  <PAGE_PARAMETER NAME="productDeliveryPatternInfoID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="productDeliveryPatternInfoID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="adminPDPIIDKey$productDeliveryPatternInfoID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="productDeliveryPatternInfoID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="productDeliveryPatternInfoID"
    />
  </CONNECT>


  <!-- BEGIN, CR00236627, GP -->
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="dtls$productID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="productID"
    />
  </CONNECT>
  <!-- END, CR00236627 -->


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="productDeliveryPatternID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="productDeliveryPatternID"
    />
  </CONNECT>


  <!-- BEGIN, CR00236627, GP -->
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="nameTextID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="nameTextID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="commentsTextID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="commentsTextID"
    />
  </CONNECT>
  <!-- END, CR00236627 -->


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="recordStatus"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="recordStatus"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="moreInGroupInd"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="moreInGroupInd"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="eftInd"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="eftInd"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="43"
    NUM_COLS="2"
    TAB_ORDER="ROW"
  >


    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.DeliveryMethod"
      WIDTH="80"
    >
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="deliveryMethodTypeCode"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="deliveryMethodID"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="deliveryMethodID"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Max"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="maximumAmount"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="maximumAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.From">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fromDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fromDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DeliveryFreq">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="deliveryFrequency"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="deliveryFrequency"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Offset"
      WIDTH="33"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="offset"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="offset"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER LABEL_WIDTH="21.5">
    <FIELD
      LABEL="Field.Label.Cover"
      WIDTH="65"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="coverPattern"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="coverPattern"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00406866, VT -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00406866 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
