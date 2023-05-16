<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2004, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The include page which allows users to view Delivery Pattern details.  -->
<!-- BEGIN, CR00189687, GD -->
<?curam-deprecated Since Curam 6.0, replaced with ProductDelivery_viewDeliveryPatternView1.
  This page doesn't support Multiple Active Nominee Delivery Patterns (HLFR14604) . 
  Hence this page has been deprecated. See release note: CR00189687 ?>
<!-- END, CR00189687 -->
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
    CLASS="Product"
    NAME="DISPLAY"
    OPERATION="readDeliveryPattern"
  />


  <PAGE_PARAMETER NAME="productDeliveryPatternInfoID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


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


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
  >


    <FIELD LABEL="Field.Label.Method">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="deliveryMethodTypeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Max">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
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
    </FIELD>


    <FIELD LABEL="Field.Label.Offset">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="offset"
        />
      </CONNECT>
    </FIELD>


    <FIELD CONTROL="SKIP"/>


    <FIELD LABEL="Field.Label.Cover">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="coverPattern"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Freq">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="deliveryFrequency"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="recordStatus"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >


    <!-- BEGIN, CR00050298, MR -->
    <!-- BEGIN, HARP 64908, SP -->
    <FIELD LABEL="Field.Label.Comments">
      <!-- END, HARP 64908 -->
      <!-- END, CR00050298 -->


      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
