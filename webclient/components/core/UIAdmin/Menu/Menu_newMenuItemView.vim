<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2011 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                    -->
<!-- This software is the confidential and proprietary information of Curam  -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose     -->
<!-- such Confidential Information and shall use it only in accordance with  -->
<!-- the terms of the license agreement you entered into with Curam          -->
<!-- Software.                                                               -->
<!-- Description -->
<!-- =========== -->
<!--   -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <CLUSTER LABEL_WIDTH="30">
    <FIELD LABEL="Field.Label.MenuID">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="id"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.PageID">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="pageID"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Title">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="title"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.description">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="description"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Tooltip">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="tooltip"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.OpenAs">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="openAs"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER
    LABEL_WIDTH="30"
    TITLE="Cluster.Title.Options"
  >
    <FIELD
      LABEL="Field.Label.Height"
      WIDTH="30"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="height"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Width"
      WIDTH="30"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="width"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
