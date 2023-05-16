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
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                              -->
<!-- ===========                                              -->
<!-- This page is used to display a list of Actions                         -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <PAGE_PARAMETER NAME="situationID"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="situationID"
    />
    <TARGET
      NAME="DISPLAYACTIONS"
      PROPERTY="situationKey$situationID"
    />
  </CONNECT>
  <SERVER_INTERFACE
    CLASS="Situation"
    NAME="DISPLAYACTIONS"
    OPERATION="listAssociatedActions"
    PHASE="DISPLAY"
  />


  <!-- BEGIN, CR00214099, AK -->
  <LIST>
    <FIELD
      LABEL="Field.Title.Actions"
      WIDTH="100"
    >
      <!-- END, CR00214099 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAYACTIONS"
          PROPERTY="action"
        />
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>
