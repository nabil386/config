<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== --> 
<!-- Views an Address Snapshot.                                          -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

  <SERVER_INTERFACE 
    CLASS="Participant" 
    NAME="DISPLAY" 
    OPERATION="readAddress" 
  />

  <PAGE_PARAMETER NAME="concernRoleAddressID" />

  <CONNECT>
    <SOURCE NAME="PAGE" PROPERTY="concernRoleAddressID" />
    <TARGET NAME="DISPLAY" PROPERTY="readConcernRoleAddressKey$concernRoleAddressID" />
  </CONNECT>


  <CLUSTER NUM_COLS="2" TITLE="Cluster.Title.Details">


    <FIELD LABEL="Field.Label.Type">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="typeCode" />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.FromDate">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="startDate" />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Address">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="formattedAddressData" />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Primary">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="primaryAddressInd" />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ToDate">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="endDate" />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="statusCode" />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER SHOW_LABELS="false" TITLE="Cluster.Title.Comments">
    <FIELD LABEL="Field.Label.Comments"> 
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="comments" />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  
  
</VIEW>