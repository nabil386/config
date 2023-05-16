<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2010, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Views a AlternateName.                                                   -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="Person"
    NAME="DISPLAY"
    OPERATION="readAlternativeName"
  />


  <PAGE_PARAMETER NAME="alternateNameID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="alternateNameID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="readPersonNameKey$alternateNameID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="25"
    NUM_COLS="2"
  >


    <FIELD LABEL="Field.Label.Title">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="title"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Suffix">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="nameSuffix"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Initials">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="initials"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="nameStatus"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Label.Comments"
  >


    <!-- BEGIN, CR00416277, VT -->
    <FIELD LABEL="Field.Label.Comments">
      <!-- END, CR00416277 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
