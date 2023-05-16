<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2009, 2010 Curam Software Ltd.                                              -->
<!-- All rights reserved.                                                                   -->
<!-- This software is the confidential and proprietary information of Curam                 -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose                    -->
<!-- such Confidential Information and shall use it only in accordance with                 -->
<!-- the terms of the license agreement you entered into with Curam                         -->
<!-- Software.                                                                              -->
<!-- Description                                                                            -->
<!-- ===========                                                                            -->
<!-- This page provide a view of an investigation allegation.                               -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.Title"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="InvestigationDelivery"
    NAME="DISPLAY"
    OPERATION="readAllegationHomeDetails"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="allegationID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="allegationID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$allegationID"
    />
  </CONNECT>

    <CLUSTER
      LABEL_WIDTH="30"
      NUM_COLS="2"
    >


      <FIELD LABEL="Field.Label.CreationDate">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="creationDate"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.AddressData">
        <!-- BEGIN, CR00142456, DJ -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$allegationDtls$formattedAddData"
          />
        </CONNECT>
        <!-- END, CR00142456-->
      </FIELD>


    </CLUSTER>


    <!-- BEGIN, CR00120992, GD -->
    <CLUSTER
      LABEL_WIDTH="30"
      NUM_COLS="1"
      SHOW_LABELS="false"
      TITLE="Cluster.Title.Description"
    >
      <!-- END, CR00120992 -->


      <FIELD
        HEIGHT="3"
        LABEL="Field.Label.Description"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$allegationDetails$description"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
    <!-- BEGIN, CR00120992, GD -->
  <!-- END, CR00120992 -->

  <!-- BEGIN, CR00120992, GD -->

    <CLUSTER
      LABEL_WIDTH="30"
      NUM_COLS="2"
      SHOW_LABELS="true"
      TITLE="Field.Label.Finding"
    >
      <!-- END, CR00120992 -->


      <FIELD LABEL="Field.Label.Finding">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="finding"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.EffectiveDate">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="effectiveDate"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>
    <CLUSTER
      NUM_COLS="1"
      STYLE="outer-cluster-borderless"
      LABEL_WIDTH="15"
    >
      <FIELD LABEL="Field.Label.Comments">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="comments"
          />
        </CONNECT>
      </FIELD>

    </CLUSTER>

</VIEW>
