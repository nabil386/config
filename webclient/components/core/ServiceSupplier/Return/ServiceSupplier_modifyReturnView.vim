<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2002, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002-2003 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows the user to modify a return -->
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
    CLASS="ServiceSupplier"
    NAME="DISPLAY"
    OPERATION="readReturn"
  />


  <SERVER_INTERFACE
    CLASS="ServiceSupplier"
    NAME="ACTION"
    OPERATION="modifyReturn"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="supplierReturnID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="supplierReturnID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="readSupplierReturnKey$returnID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="supplierReturnID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="supplierReturnID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="supplierID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="supplierID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="methodOfEntry"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="methodOfEntry"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="statusCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="statusCode"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="supplierInitials"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="supplierInitials"
    />
  </CONNECT>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >
    <FIELD LABEL="Field.Label.ContactName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="contactName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="contactName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.numOfItems">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="numberOfItems"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="numberOfItems"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.TotalAmount">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="totalAmount"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="totalAmount"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.MethodOfEntry">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="methodOfEntry"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ProductName">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="productDescription"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="productID"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="productID"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.RefNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="docReferenceNumber"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="docReferenceNumber"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ReceiptDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="receiptDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="receiptDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="statusCode"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    NUM_COLS="1"
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
