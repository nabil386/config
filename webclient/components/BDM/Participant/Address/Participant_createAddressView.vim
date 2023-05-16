<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
 
  Copyright IBM Corporation 2003, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003-2006,2009, 2010 Curam Software Ltd.                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Included view to enter address details for a Participant.              -->
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
    CLASS="BDMParticipant"
    NAME="ACTION"
    OPERATION="createAddress"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="concernRoleID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="concernRoleID"
    />
  </CONNECT>


  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="ACTION"
        PROPERTY="dtls$informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>


  <!-- Clusters with address details. -->
  <!-- Label width increased to facilitate globalisation requirements -->
  <CLUSTER
    LABEL_WIDTH="49"
    NUM_COLS="2"
  >


    <!-- BEGIN, CR00141707, MC -->
    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="80"
    >
      <!-- END, CR00141707 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="typeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.FromDate">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Primary">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="primaryAddressInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ToDate"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <!-- Label width increased to facilitate globalisation requirements -->
  <CLUSTER
    LABEL_WIDTH="49"
    NUM_COLS="2"
    TAB_ORDER="ROW"
  >


    <!-- BEGIN, CR00050298, MR -->
    <!-- BEGIN, HARP 64908, SP -->
    <FIELD LABEL="Field.Label.Address">
      <!-- END, HARP 64908 -->
      <!-- END, CR00050298 -->


      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="addressData"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
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
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
