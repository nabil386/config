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
<!-- This page allows the user to create a new product billing pattern,     -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <!-- BEGIN, CR00280529, MV -->
  <SERVER_INTERFACE
    CLASS="Product"
    NAME="ACTION"
    OPERATION="createDeliveryPatternInfo"
    PHASE="ACTION"
  />
  <!-- END, CR00280529 -->


  <PAGE_PARAMETER NAME="productID"/>


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


  <CLUSTER
    LABEL_WIDTH="43"
    NUM_COLS="2"
    TAB_ORDER="ROW"
  >


    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Method">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="deliveryMethodID"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Max"
      USE_DEFAULT="false"
      WIDTH="40"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="maximumAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.From">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fromDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DeliveryFreq">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="deliveryFrequency"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Offset"
      USE_DEFAULT="false"
      WIDTH="33"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="offset"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Default">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="defaultPatternInd"
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
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
