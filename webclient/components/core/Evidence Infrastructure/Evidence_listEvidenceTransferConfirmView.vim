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
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Evidence transfer confirmation page containing a list of evidence      -->
<!-- records to be tarnsfered to another case                               -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <CLUSTER
    NUM_COLS="1"
    SHOW_LABELS="false"
    TITLE="Cluster.Title.ConfirmationMessage"
  >
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="confirmationMessage"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <LIST>
    <FIELD
      LABEL="List.Title.Type"
      WIDTH="17"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="newAndUpdateList$dtls$evidenceType"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.Participant"
      WIDTH="17"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="newAndUpdateList$dtls$concernRoleName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.Description"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="newAndUpdateList$dtls$summary"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.Period"
      WIDTH="18"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="period"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.LatestActivity"
      WIDTH="23"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="latestActivity"
        />
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>
