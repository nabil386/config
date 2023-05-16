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


  <CLUSTER STYLE="cluster-cpr-no-internal-padding">
    <CLUSTER
      LABEL_WIDTH="20"
      NUM_COLS="1"
    >
      <FIELD
        HEIGHT="3"
        LABEL="Field.Label.Action"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAYACTION"
            PROPERTY="action"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


    <CLUSTER
      NUM_COLS="2"
      STYLE="cluster-cpr-grey-background"
    >


      <!-- BEGIN, CR00235006, AK -->
      <CLUSTER
        NUM_COLS="1"
        SHOW_LABELS="false"
        STYLE="nested-cluster-left"
        TITLE="Cluster.Label.ParticipantOwners"
      >
        <LIST>
          <FIELD HEIGHT="3">
            <CONNECT>
              <SOURCE
                NAME="GETACTIONOWNERS"
                PROPERTY="name"
              />
            </CONNECT>
          </FIELD>
        </LIST>
      </CLUSTER>


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
            <SOURCE
              NAME="DISPLAYACTION"
              PROPERTY="userOwnerFullName"
            />
            <!-- END, CR00235006 -->
          </CONNECT>
        </FIELD>
      </CLUSTER>
    </CLUSTER>
  </CLUSTER>


</VIEW>
