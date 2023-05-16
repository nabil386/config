<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2008, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008, 2010 Curam Software Ltd.                                               -->
<!-- All rights reserved.                                                                   -->
<!-- This software is the confidential and proprietary information of Curam                 -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose                    -->
<!-- such Confidential Information and shall use it only in accordance with                 -->
<!-- the terms of the license agreement you entered into with Curam                         -->
<!-- Software.                                                                              -->
<!-- Description                                                                            -->
<!-- ===========                                                                            -->
<!-- This page is used to display an Action details                                         -->
<?curam-deprecated Since Curam 6.0, as part of the Action Plan Enhancement, 
  replaced by Action_viewActionView, as the situation details are captured independent of the action.
  See release note:CEF-722 : ActionPlan Enhancement
?>
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.Title"
      />
    </CONNECT>
    <CONNECT>
      <SOURCE
        NAME="PAGE"
        PROPERTY="description"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="InvestigationDelivery"
    NAME="DISPLAY"
    OPERATION="readActionAndAllegationDetails"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="parentLinkID"/>
  <PAGE_PARAMETER NAME="actionID"/>
  <PAGE_PARAMETER NAME="description"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="actionID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$actionID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Label.Details"
  >


    <FIELD LABEL="Field.Label.SituationCategory">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="situationCategory"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.SituationRequiringAction">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="situationRequiringAction"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Action">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="action"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Owner">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="ownerName"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Action_resolveOwnerHome"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="ownerConcernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="ownerConcernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="ownerName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="ownerName"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="ownerType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="ownerType"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD LABEL="Field.Label.ExpectedEndDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="expectedEndDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ActualEndDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="actualEndDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Outcome">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="outcome"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$actionDetails$dtls$recordStatus"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <LIST TITLE="List.Title.Allegation">


    <CONTAINER
      LABEL="Container.Label.Action"
      SEPARATOR="Container.Separator"
      WIDTH="15"
    >
      <ACTION_CONTROL LABEL="ActionControl.Label.View">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="InvestigationDelivery_viewActionAllegation"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="parentLinkID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="parentLinkID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="allegationID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="allegationID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="description"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Remove">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="InvestigationDelivery_removeActionAllegation"
        >
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="actionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="actionID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="parentLinkID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="parentLinkID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="actionChildLinkID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="actionChildLinkID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$allegationList$versionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="versionNo"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="description"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


    <FIELD
      LABEL="Field.Label.AllegationType"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="type"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.AllegationDate"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="allegationDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.AllegationLocation"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="location"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.AllegationStatus"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$allegationList$recordStatus"
        />
      </CONNECT>
    </FIELD>


  </LIST>


  <CLUSTER
    LABEL_WIDTH="20"
    NUM_COLS="1"
    SHOW_LABELS="false"
    TITLE="Cluster.Label.Comments"
  >
    <!-- BEGIN, CR00416277, VT -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00416277 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
