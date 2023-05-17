<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010 Curam Software Ltd.                                      -->
<!-- All rights reserved.                                                    -->
<!-- This software is the confidential and proprietary information of Curam  -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose     -->
<!-- such Confidential Information and shall use it only in accordance with  -->
<!-- the terms of the license agreement you entered into with Curam          -->
<!-- Software.                                                               -->
<!-- Description                                                             -->
<!-- ===========                                                             -->
<!-- The included view to view an action details.                            -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <!-- BEGIN, CR00205132, AK -->
  <CLUSTER
    NUM_COLS="2"
    STYLE="cluster-cpr-grey-background "
  >
    <!-- END, CR00205132 -->
    <!-- BEGIN, CR00235006, AK -->
    <CLUSTER
      NUM_COLS="1"
      SHOW_LABELS="false"
      STYLE="nested-cluster-left"
      TITLE="Cluster.Label.ParticipantOwners"
      WIDTH="95"
    >
      <LIST>
        <FIELD HEIGHT="3">
          <!-- END, CR00235006 -->
          <!-- BEGIN, CR00205132, AK -->
          <!-- BEGIN, CR00214099, AK -->
          <LINK PAGE_ID="Participant_resolve">
            <!-- END, CR00214099 -->
            <CONNECT>
              <SOURCE
                NAME="GETACTIONOWNERS"
                PROPERTY="participantRoleID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="concernRoleID"
              />
            </CONNECT>
          </LINK>
          <CONNECT>
            <SOURCE
              NAME="GETACTIONOWNERS"
              PROPERTY="name"
            />
          </CONNECT>
        </FIELD>
      </LIST>
    </CLUSTER>
    <!-- BEGIN, CR00214099, AK -->
    <!-- BEGIN, CR00235006, AK -->
    <CLUSTER
      NUM_COLS="1"
      SHOW_LABELS="false"
      STYLE="nested-cluster-right"
      TITLE="Cluster.Label.UserOwner"
    >
      <FIELD
        HEIGHT="3"
        WIDTH="50"
      >
        <CONNECT>
          <!-- BEGIN, CR00216915, AK-->
          <SOURCE
            NAME="DISPLAYACTION"
            PROPERTY="userOwnerFullName"
          />
          <!-- END, CR00235006 -->
        </CONNECT>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Organization_viewUserDetails"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAYACTION"
              PROPERTY="userOwner"
            />
            <!-- END, CR00216915-->
            <TARGET
              NAME="PAGE"
              PROPERTY="userName"
            />
          </CONNECT>
          <!-- END, CR00214099 -->
        </LINK>
      </FIELD>
    </CLUSTER>
  </CLUSTER>
  <!-- END, CR00205132 -->
</VIEW>
