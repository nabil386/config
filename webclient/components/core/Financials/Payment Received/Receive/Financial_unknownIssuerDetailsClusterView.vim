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
<!-- The included view used to allow the user to enter the details of the   -->
<!-- issuer of a received payment. This view is used when recording a       -->
<!-- payment in the suspense account as the issuer details can't be         -->
<!-- defaulted to the client (none has been identified).                    -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
    TITLE="Cluster.Title.IssuerDetails"
  >


    <FIELD LABEL="Field.Label.IssuerName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="issuerName"
        />
      </CONNECT>
    </FIELD>


    <FIELD CONTROL="SKIP"/>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
    TAB_ORDER="ROW"
  >
    <FIELD>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="issuerAddressData"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
