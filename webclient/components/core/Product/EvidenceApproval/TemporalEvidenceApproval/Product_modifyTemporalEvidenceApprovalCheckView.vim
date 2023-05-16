<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012, 2015. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2006, 2009, 2010 Curam Software Ltd.                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to modify a temporal evidence approval check -->
<!-- entry.                                                                 -->
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
    OPERATION="readTemporalEvidenceApprovalCheck"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="Product"
    NAME="DISPLAY2"
    OPERATION="listProductsSharingEvidence"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="Product"
    NAME="ACTION"
    OPERATION="modifyTemporalEvidenceApprovalCheck"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="productID"/>
  <PAGE_PARAMETER NAME="temporalEvApprovalCheckID"/>
  <PAGE_PARAMETER NAME="evidenceType"/>
  <PAGE_PARAMETER NAME="contextDescription"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="temporalEvApprovalCheckID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$key$key$temporalEvApprovalCheckID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="productID"
    />
    <TARGET
      NAME="DISPLAY2"
      PROPERTY="key$productID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="evidenceType"
    />
    <TARGET
      NAME="DISPLAY2"
      PROPERTY="key$evidenceType"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$temporalEvApprovalCheckID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="temporalEvApprovalCheckID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="recordStatus"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="recordStatus"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="evidenceType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="evidenceType"
    />
  </CONNECT>


  <CLUSTER LABEL_WIDTH="20">


    <FIELD LABEL="Field.Label.EvidenceType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="evidenceType"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00052256, ANK -->
    <FIELD
      LABEL="Field.Label.Percentage"
      WIDTH="4"
      WIDTH_UNITS="CHARS"
    >
      <!-- END, CR00052256 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="percentage"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="percentage"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Field.Label.Comments"
  >
    <!-- BEGIN, CR00463142, EC -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00463142 -->


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


  <LIST TITLE="Cluster.Title.Shared">


    <CONTAINER
      LABEL="Container.Label.Action"
      WIDTH="10"
    >


      <ACTION_CONTROL LABEL="ActionControl.Label.View">


        <LINK PAGE_ID="Product_benefitHome1">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY2"
              PROPERTY="list$productID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="productID"
            />
          </CONNECT>
        </LINK>


      </ACTION_CONTROL>


    </CONTAINER>


    <FIELD
      LABEL="Field.Label.ProoductName"
      WIDTH="85"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY2"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
