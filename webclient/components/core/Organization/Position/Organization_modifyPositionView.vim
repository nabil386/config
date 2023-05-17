<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26

  Copyright IBM Corporation 2003, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003-2004, 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This process allows the user to modify position details.               -->
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
    CLASS="Organization"
    NAME="DISPLAY"
    OPERATION="viewPosition"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="Organization"
    NAME="ACTION"
    OPERATION="modifyPosition"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="positionID"/>
  <PAGE_PARAMETER NAME="organisationStructureID"/>
  <PAGE_PARAMETER NAME="organisationUnitID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="positionID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="orgStructureAndPositionKey$positionID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="organisationStructureID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="orgStructureAndPositionKey$organisationStructureID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="organisationUnitID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="orgStructureAndPositionKey$organisationUnitID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="positionID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="modifyPositionDetails$positionDetails$positionID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="organisationStructureID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="modifyPositionDetails$positionOrgDetails$organisationStructureID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="organisationUnitID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="organisationUnitID"
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
      PROPERTY="recordStatus"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="recordStatus"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    SHOW_LABELS="true"
  >
    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="positionName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.FromDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="fromDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="fromDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Job">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="jobName"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="jobID"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="jobID"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.LeadPosition">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="leadPositionInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="modifyPositionDetails$positionDetails$leadPositionInd"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    DESCRIPTION="Cluster.Description.Reporting"
    LABEL_WIDTH="32.5"
    SHOW_LABELS="true"
    TITLE="Cluster.Title.Reporting"
  >
    <FIELD LABEL="Field.Label.ReportsToLead">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="reportsToLeadPositionID"
        />
      </CONNECT>
      <LINK>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="viewPositionDetails$organisationUnitID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="organisationUnitID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="organisationStructureID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="organisationStructureID"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <FIELD LABEL="Field.Label.OrgUnitPositions">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="orgUnitPositionID"
        />
      </CONNECT>
      <LINK>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="viewPositionDetails$organisationUnitID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="organisationUnitID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="organisationStructureID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="organisationStructureID"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <FIELD LABEL="Field.Label.ParentOrgUnitPositions">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="parentOrgUnitPositionID"
        />
      </CONNECT>
      <LINK>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="parentOrganisationUnitID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="organisationUnitID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="organisationStructureID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="organisationStructureID"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <FIELD LABEL="Field.Label.PositionSearch">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="searchPositionID"
        />
      </CONNECT>
      <LINK>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="organisationStructureID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="organisationStructureID"
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
