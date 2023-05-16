<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2010-2011 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page displays eligibility determination details for a CER product -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CLUSTER
    LABEL_WIDTH="35"
    STYLE="outer-cluster-borderless"
    TITLE="Cluster.Title"
  >
    <FIELD LABEL="field.label.openEnded">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="allowOpenEndedCases"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="field.label.displayStrategy">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="detIntSummarizerStrategyType"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="field.label.compStrategy">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="determinationCompStrategyType"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00287600, RB -->
    <FIELD LABEL="field.label.reassessmentStrategyType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reassessmentStrategyType"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00287600 -->


  </CLUSTER>


</VIEW>
