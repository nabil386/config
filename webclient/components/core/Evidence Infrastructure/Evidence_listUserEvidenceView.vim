<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2006-2008 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Evidence infrastructure page containing a list of evidence records     -->
<!-- for a user which are active, in edit and pending removal               -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <LIST TITLE="List.NewAndUpdated">


    <FIELD
      LABEL="List.Title.Type"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="newAndUpdateList$dtls$evidenceType"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.Name"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="newAndUpdateList$dtls$concernRoleName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.EffectiveDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="newAndUpdateList$dtls$effectiveFrom"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.Details"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="newAndUpdateList$dtls$summary"
        />
      </CONNECT>
    </FIELD>
  </LIST>


  <LIST TITLE="List.PendingRemovals">


    <FIELD
      LABEL="List.Title.Type"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="removeList$dtls$evidenceType"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.Name"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="removeList$dtls$concernRoleName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.EffectiveDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="removeList$dtls$effectiveFrom"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.Details"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="removeList$dtls$summary"
        />
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>
