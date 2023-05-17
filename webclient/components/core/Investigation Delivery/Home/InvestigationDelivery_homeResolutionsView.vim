<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008-2010 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view to display an Investigation Delivery Case home page  -->
<!-- details.                                                               -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

    <!-- Resolution Details cluster -->
    <CLUSTER
      TITLE="Cluster.Title.Resolution"
      NUM_COLS="2"
      LABEL_WIDTH="30"
    >
      <FIELD LABEL="Field.Label.Resolution">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="resolution"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.CreatedBy">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="createdByFullName"
          />
        </CONNECT>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Organization_viewUserDetails"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="createdBy"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="userName"
            />
          </CONNECT>
        </LINK>
      </FIELD>
      <FIELD LABEL="Field.Label.ResolutionStatus">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="resolutionStatus"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.CreationDate">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="resolutionDtls$creationDate"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
</VIEW>
