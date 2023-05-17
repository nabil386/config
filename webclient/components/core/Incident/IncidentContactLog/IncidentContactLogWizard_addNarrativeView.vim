<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM

  Copyright IBM Corporation 2012, 2023. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->

<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to create a contact log for a case.          -->

<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

<CLUSTER BEHAVIOR="NONE">
  
    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY_SUBJECT"
        PROPERTY="result$contactLogSubjectEnabledInd"
      />
    </CONDITION>
    
    <FIELD
     LABEL="Field.Label.Subject"
      USE_BLANK="false"
    >
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="result$incidentWizardDetails$wizardDetails$narrativeDetails$subject"
    />
    </CONNECT>
    <CONNECT>
      <TARGET
        NAME="ACTION"
        PROPERTY="narrativeDetails$incidentWizardDetails$wizardDetails$narrativeDetails$subject"
    />
    </CONNECT>
   </FIELD>
    <FIELD
      HEIGHT="180"
      LABEL="Field.Label.Text"
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
      HEIGHT="180"
      LABEL="Field.Label.Text"
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
