<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM

  Copyright IBM Corporation 2012,2023. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2011 Curam Software Ltd..                                    -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to create a contact log for a case.          -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  
  <CLUSTER
    BEHAVIOR="NONE"
  >
  	<CONDITION>
        <IS_TRUE
          NAME="DISPLAY_SUBJECT"
          PROPERTY="result$contactLogSubjectEnabledInd"
        />
    </CONDITION>
  
    <FIELD
     LABEL="Cluster.Label.Subject"
      USE_BLANK="false"
    >
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="result$wizardDetails$narrativeDetails$subject"
    />
    </CONNECT>
    <CONNECT>
      <TARGET
        NAME="ACTION"
        PROPERTY="narrativeDetails$narrativeTextWizardDetails$subject"
    />
    </CONNECT>
   </FIELD>
   
   <FIELD
      HEIGHT="250"
      LABEL="Cluster.Label.NotesText"
    >
      <!-- END, CR00463142 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="notesText"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="notesText"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER> 
  
  <CLUSTER
    BEHAVIOR="NONE"
    SHOW_LABELS="false"
  >
  <CONDITION>
        <IS_FALSE
          NAME="DISPLAY_SUBJECT"
          PROPERTY="result$contactLogSubjectEnabledInd"
        />
    </CONDITION>
    <!-- BEGIN, CR00463142, EC -->
    <FIELD
      HEIGHT="250"
      LABEL="Cluster.Label.NotesText"
      INITIAL_FOCUS="true"
    >
      <!-- END, CR00463142 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="notesText"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="notesText"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
