<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2004, 2009-2011 Curam Software Ltd.                          -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for the create template communication.               -->
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
    CLASS="Communication"
    NAME="ACTION"
    OPERATION="listTemplateByTypeAndParticipant"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="pageDescription"/>
  <PAGE_PARAMETER NAME="concernRoleID"/>
  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="correspondentParticipantRoleID"/>
  <PAGE_PARAMETER NAME="correspondentName"/>
  <PAGE_PARAMETER NAME="correspondentParticipantRoleType"/>
  <PAGE_PARAMETER NAME="caseParticipantRoleID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="participantRoleID"
    />
  </CONNECT>


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


  <ACTION_SET
    ALIGNMENT="CENTER"
    TOP="false"
  >


    <ACTION_CONTROL LABEL="ActionControl.PrevButton.label">
      <LINK
        DISMISS_MODAL="false"
        OPEN_MODAL="false"
        PAGE_ID="Participant_getProFormaCorrespondent"
        SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <!--BEGIN, CR00229271, SS-->
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
        <!-- END, CR00229271-->
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      IMAGE="CancelButton"
      LABEL="ActionControl.Label.Cancel"
    />
  </ACTION_SET>


  <CLUSTER
    LABEL_WIDTH="25"
    NUM_COLS="2"
    TITLE="Cluster.Title.CommunicationType"
  >


    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="65"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="templateType"
        />
      </CONNECT>
    </FIELD>
    
    
    
    <ACTION_SET
      TOP="false"
    >
      <ACTION_CONTROL
        DEFAULT="true"
        IMAGE="SearchButton"
        LABEL="ActionControl.Label.Search"
        TYPE="SUBMIT"
      >
        <LINK PAGE_ID="THIS"/>
      </ACTION_CONTROL>
    </ACTION_SET>

  </CLUSTER>


  <LIST
    
  >
    <CONTAINER
      LABEL="Container.Label.Action"
      WIDTH="15"
    >
      <ACTION_CONTROL LABEL="ActionControl.Label.Select">
        <LINK
          DISMISS_MODAL="false"
          PAGE_ID="Participant_createProFormaCommunication1"
          SAVE_LINK="false"
        >
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="templateID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="proFormaID"
            />
          </CONNECT>
          <!-- BEGIN, CR00145755, SK -->
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="localeIdentifier"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="localeIdentifier"
            />
          </CONNECT>
          <!-- END, CR00145755 -->
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="templateType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="communicationType"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="concernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="correspondentParticipantRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="correspondentParticipantRoleID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="correspondentParticipantRoleType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="correspondentParticipantRoleType"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="pageDescription"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="pageDescription"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="correspondentName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="correspondentName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="templateName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="templateName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="latestVersion"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="proFormaVersionNo"
            />
          </CONNECT>
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
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


    <FIELD
      LABEL="Field.Label.ProFormaName"
      WIDTH="60"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="templateName"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00145755, SK -->
    <FIELD
      LABEL="Field.Label.Locale"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$searchTemplatesByConcernAndTypeResult$xslTemplateDetailsListOut$dtls$localeIdentifier"
        />


      </CONNECT>
    </FIELD>
    <!-- END, CR00145755 -->
  </LIST>


</VIEW>
