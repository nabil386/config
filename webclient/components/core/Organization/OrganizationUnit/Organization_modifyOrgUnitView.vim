<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
 
  Copyright IBM Corporation 2003, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2003-2008, 2010 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This process allows the user to modify organization unit details.      -->
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
        NAME="DISPLAY"
        PROPERTY="organisationUnitDetails$name"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="Organization"
    NAME="DISPLAY"
    OPERATION="viewOrganisationUnit"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="Organization"
    NAME="ACTION"
    OPERATION="modifyOrganisationUnit"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="organisationUnitID"/>
  <PAGE_PARAMETER NAME="organisationStructureID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="organisationUnitID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="orgStructAndOrgUnitKey$organisationUnitID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="organisationStructureID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="orgStructAndOrgUnitKey$organisationStructureID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="parentOrganisationUnitID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="parentOrganisationUnitID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="organisationStructureID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="organisationStructureID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="organisationUnitDetails$organisationUnitID"
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


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
    SHOW_LABELS="true"
    TITLE="Cluster.Title.Details"
  >
    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ParentUnit">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="parentOrganisationUnitName"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00094034, MM -->
    <FIELD
      LABEL="Field.Label.ReadSID"
      WIDTH="100"
    >
      <!-- END, CR00094034 -->
      <CONNECT>


        <INITIAL
          NAME="DISPLAY"
          PROPERTY="organisationUnitDetails$readSID"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="organisationUnitDetails$readSID"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="orgUnitDetails$readSID"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00094034, MM -->
    <FIELD
      LABEL="Field.Label.CreateUnitID"
      WIDTH="100"
    >
      <!-- END, CR00094034 -->


      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="organisationUnitDetails$createSID"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="organisationUnitDetails$createSID"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="orgUnitDetails$createUnitSID"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Type">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="businessTypeCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="businessTypeCode"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00281699, AC -->
    <FIELD LABEL="Field.Label.UnitStatus">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="statusCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="statusCode"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00281699  -->
    <!-- BEGIN, CR00094034, MM -->
    <FIELD
      LABEL="Field.Label.MaintainSID"
      WIDTH="100"
    >
      <!-- END, CR00094034 -->
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="organisationUnitDetails$maintainSID"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="organisationUnitDetails$maintainSID"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="orgUnitDetails$maintainSID"
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
      HEIGHT="3"
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
