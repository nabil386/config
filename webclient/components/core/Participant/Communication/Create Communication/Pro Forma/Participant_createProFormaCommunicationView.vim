<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2004, 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2004, 2009, 2011 Curam Software Ltd.                         -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for the record communication details.                -->

<!-- BEGIN, CR00236672, NS -->
<?curam-deprecated Since Curam 6.0, replaced with Participant_createProFormaCommunicationView1.vim. See release note: CR00236672?>
<!-- END, CR00236672 -->

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
    <CONNECT>
      <SOURCE
        NAME="PAGE"
        PROPERTY="pageDescription"
      />
    </CONNECT>
  </PAGE_TITLE>
  
  
  <SERVER_INTERFACE
    CLASS="Communication"
    NAME="ACTION"
    OPERATION="createProForma"
    PHASE="ACTION"
  />
  
  
  <ACTION_SET ALIGNMENT="CENTER">
    
    
    <ACTION_CONTROL
      IMAGE="SaveButton"
      LABEL="ActionControl.Label.Save"
      TYPE="SUBMIT"
    />
    
    
    <ACTION_CONTROL
      IMAGE="CancelButton"
      LABEL="ActionControl.Label.Cancel"
    />
    
    
  </ACTION_SET>
  
  
  <PAGE_PARAMETER NAME="proFormaID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>
  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="concernRoleID"/>
  <PAGE_PARAMETER NAME="correspondentParticipantRoleID"/>
  <PAGE_PARAMETER NAME="correspondentName"/>
  <PAGE_PARAMETER NAME="templateName"/>
  <PAGE_PARAMETER NAME="proFormaVersionNo"/>
  <PAGE_PARAMETER NAME="correspondentParticipantRoleType"/>
  <PAGE_PARAMETER NAME="communicationType"/>
  <PAGE_PARAMETER NAME="caseParticipantRoleID"/>
  <!-- BEGIN, CR00145755, SK -->
  <PAGE_PARAMETER NAME="localeIdentifier"/>
  <!-- END, CR00145755 -->
  
  
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="clientParticipantRoleID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="correspondentParticipantRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="correspondentParticipantRoleID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="correspondentName"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="correspondentName"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="proFormaID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="proFormaID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="templateName"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="subject"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="proFormaVersionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="proFormaVersionNo"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="communicationType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="communicationTypeCode"
    />
  </CONNECT>
  
  
  <!-- BEGIN, CR00145755, SK -->
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="localeIdentifier"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="localeIdentifier"
    />
  </CONNECT>
  <!-- END, CR00145755 -->
  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.CommunicationDetails"
    >
    
    
    <FIELD LABEL="Field.Label.PrintNow">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="printInd"
        />
      </CONNECT>
    </FIELD>
    
    
    <FIELD LABEL="Field.Label.CorrespondentType">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="correspondentType"
        />
      </CONNECT>
    </FIELD>
    
    
  </CLUSTER>
  
  
  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.CorrespondentDetails"
    >
    
    
    <FIELD LABEL="Field.Label.CorrespondentName">
      <CONNECT>
        <SOURCE
          NAME="PAGE"
          PROPERTY="correspondentName"
        />
      </CONNECT>
      <LINK
        OPEN_NEW="TRUE"
        PAGE_ID="Participant_resolveHome"
        >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseParticipantRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseParticipantRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="correspondentParticipantRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="correspondentParticipantRoleType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="participantType"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    
    
    <FIELD LABEL="Field.Label.Address">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="addressID"
        />
      </CONNECT>
      <LINK>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="correspondentParticipantRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    
    
  </CLUSTER>
  
  
  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
    >
    
    <!-- BEGIN, CR00406866, VT -->
    <FIELD HEIGHT="3" LABEL="Field.Label.Comments">
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
