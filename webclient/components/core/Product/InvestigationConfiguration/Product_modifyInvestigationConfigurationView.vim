<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2008, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008, 2010, 2012 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows a user to modify an existing investigation            -->
<!-- configuration record.                                                  -->
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
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="Product"
    NAME="DISPLAY"
    OPERATION="readInvestigationConfiguration"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="Product"
    NAME="ACTION"
    OPERATION="modifyInvestigationConfiguration"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="investigationConfigID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="investigationConfigID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$investigationConfigID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="investigationConfigID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="dtls$investigationConfigID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="type"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="type"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="creationDate"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="creationDate"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="recordStatus"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="recordStatus"
    />
  </CONNECT>


  <!-- BEGIN, CR00372416, VT -->
  <CLUSTER
    LABEL_WIDTH="64"
    NUM_COLS="2"
  >
    <!-- END, CR00372416 -->


    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="72"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="type"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.StartDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00205192, SS -->
    <FIELD
      LABEL="Field.Title.OwnershipStrategy"
      WIDTH="95"
    >
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="result$dtls$dtls$ownershipStrategyName"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$dtls$ownershipStrategyName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$dtls$dtls$ownershipStrategyName"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00205192 -->
    <!-- BEGIN, CR00305740, PB -->
    <FIELD
      LABEL="Field.Title.ContactLog"
      WIDTH="95"
    >
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="result$dtls$dtls$contactLogInd"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$dtls$contactLogInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$dtls$dtls$contactLogInd"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00305740 -->


    <FIELD LABEL="Field.Label.HomePageIdentifier">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="homePageIdentifier"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="homePageIdentifier"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EndDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>


    <!--BEGIN, CR00205591, PB-->
    <FIELD LABEL="Field.Label.TranslationRequired">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="adminTranslationRequiredInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="adminTranslationRequiredInd"
        />
      </CONNECT>
    </FIELD>
    <!--END, CR00205591-->
  </CLUSTER>


  <!-- BEGIN, CR00372416, VT -->
  <CLUSTER
    LABEL_WIDTH="64"
    NUM_COLS="2"
    TITLE="Cluster.Title.MDTSharingDetails"
  >
    <!-- END, CR00372416 -->


    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="displayCollaborationSharingDetailsInd"
      />
    </CONDITION>


    <FIELD
      LABEL="Field.Title.MDTType"
      USE_BLANK="true"
      WIDTH="60"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="collaborationType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="collaborationType"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.MDTPortal"
      WIDTH="100"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="collaborationPortalHomePage"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="collaborationPortalHomePage"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Title.MDTSharing">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="collaborationSharingInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="collaborationSharingInd"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <!-- BEGIN, CR00372416, VT -->
  <CLUSTER
    LABEL_WIDTH="32"
    TITLE="Cluster.Title.EventDetails"
  >
    <!-- END, CR00372416 -->


    <FIELD
      LABEL="Field.Label.CreateEvent"
      WIDTH="95"
    >
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="createEvent"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="createEvent"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="createEvent"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.CloseEvent"
      WIDTH="95"
    >
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="closeEvent"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="closeEvent"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="closeEvent"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <!-- BEGIN, CR00372416, VT -->
  <CLUSTER
    LABEL_WIDTH="32"
    TITLE="Cluster.Label.SecurityRestrictions"
  >
    <!-- END, CR00372416 -->


    <FIELD
      LABEL="Field.Label.CreateRights"
      WIDTH="95"
    >
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="securityDtls$createSecurity"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="securityDtls$createSecurity"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="securityDtls$createSecurity"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ReadRights"
      WIDTH="95"
    >
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="securityDtls$readSecurity"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="securityDtls$readSecurity"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="securityDtls$readSecurity"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ApproveRights"
      WIDTH="95"
    >
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="securityDtls$approveSecurity"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="securityDtls$approveSecurity"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="securityDtls$approveSecurity"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.MaintainRights"
      WIDTH="95"
    >
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="securityDtls$maintainSecurity"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="securityDtls$maintainSecurity"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="securityDtls$maintainSecurity"
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
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
