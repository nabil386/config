<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012, 2017. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2005-2006, 2010 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This Page allows users to modify an Evidence Metadata entry            -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="Product"
    NAME="DISPLAY"
    OPERATION="readEvidenceMetadata"
  />


  <SERVER_INTERFACE
    CLASS="Product"
    NAME="ACTION"
    OPERATION="modifyEvidenceMetadata"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="evidenceMetadataID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="evidenceMetadataID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$evidenceMetadataID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="evidenceMetadataID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="dtls$evidenceMetadataID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="evidenceMetadataID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="key$evidenceMetadataID"
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


  <CLUSTER LABEL_WIDTH="40">
    <FIELD LABEL="Field.Label.EvidenceType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="evidenceType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="evidenceType"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ViewPage">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="viewPageName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="viewPageName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ViewSnapshotPage">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="viewSnapshotPageName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="viewSnapshotPageName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EffectiveFrom">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="effectiveFrom"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="effectiveFrom"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ModifyPage">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="modifyPageName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="modifyPageName"
        />
      </CONNECT>
    </FIELD>

<!-- BEGIN , 184516, CC-->
    <FIELD LABEL="Field.Label.AutoEndDateIndOpt">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="autoEndDateIndOpt"
        />
      </CONNECT>    
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="autoEndDateIndOpt"
        />
      </CONNECT>
      
    </FIELD>
    
    <FIELD LABEL="Field.Label.WizardCreatePageNameOpt">
       <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="wizardCreatePageNameOpt"
        />
      </CONNECT>       
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="wizardCreatePageNameOpt"
        />
      </CONNECT>
    </FIELD>
<!-- END , 184516, CC --> 

    <FIELD LABEL="Field.Label.MaintenancePattern">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="maintenancePattern"
        />
      </CONNECT>    
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="maintenancePattern"
        />
      </CONNECT>
    </FIELD>  

  </CLUSTER>


</VIEW>
