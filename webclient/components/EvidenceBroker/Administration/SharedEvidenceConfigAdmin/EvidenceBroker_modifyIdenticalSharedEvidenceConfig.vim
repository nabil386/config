<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2008, 2012, 2017. All Rights Reserved.
 
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008, 2010 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Modifies evidence sharing configuration for identical evidence sharing -->
<?curam-deprecated Since Curam 7.0.2.0, this functionality has been replaced with the Advanced Evidence Sharing component.?>
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


  <!-- BEGIN, CR00205401, GYH -->
  <SERVER_INTERFACE
    CLASS="EvidenceBrokerAdmin"
    NAME="DISPLAY"
    OPERATION="readEvidenceSharingConfigDetails"
    PHASE="DISPLAY"
  />
  <!-- END, CR00205401 -->


  <SERVER_INTERFACE
    CLASS="EvidenceBrokerAdmin"
    NAME="ACTION"
    OPERATION="modifyEvidenceBrokerConfig"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="evidenceBrokerConfigID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="evidenceBrokerConfigID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$evidenceBrokerConfigID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="sharingConfigDetails$evidenceBrokerConfigID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="evidenceBrokerConfigID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="sourceID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="sourceID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="sourceType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="sourceType"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="targetID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="targetID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="targetType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="targetType"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="sourceEvidenceType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="sourceEvidenceType"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="targetEvidenceType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="targetEvidenceType"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="sourceSystemID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="sourceSystemID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="externalSystem"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="externalSystem"
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
      PROPERTY="sharedType"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="sharedType"
    />
  </CONNECT>


  <!-- BEGIN, CR00342079, GYH -->
  <CLUSTER NUM_COLS="2">
    <!-- END, CR00342079 -->


    <CLUSTER LABEL_WIDTH="50">
      <!-- BEGIN, CR00121528, CL -->
      <FIELD LABEL="Field.Label.AutoAccept">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="autoAcceptInd"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="autoAcceptInd"
          />
        </CONNECT>
      </FIELD>
      <!-- END, CR00121528 -->


      <!-- BEGIN, CR00342079, GYH -->
      <FIELD LABEL="Field.Label.AutoActivate">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="autoActivateInd"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="autoActivateInd"
          />
        </CONNECT>
      </FIELD>
      <!-- END, CR00342079 -->
    </CLUSTER>
    <!-- BEGIN, CR00343036, AKr -->
    <!-- BEGIN, CR00358719, SSK -->
    <CLUSTER LABEL_WIDTH="60">
      <FIELD LABEL="Field.Label.ShareVerifications">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="shareVerificationsOption"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="shareVerificationsOption"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
    <!-- END, CR00358719 -->
    <!-- END, CR00343036 -->
  </CLUSTER>


  <!-- BEGIN, CR00351647, LKS -->
  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
    SHOW_LABELS="false"
    TITLE="Cluster.Title.LimitSharingTo"
  >
    <FIELD LABEL="Field.Label.ShareLatestOnly">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="shareLimitType"
        />
      </CONNECT>
    </FIELD>
    <CLUSTER
      NUM_COLS="2"
      SHOW_LABELS="false"
    >
      <FIELD LABEL="Field.Label.ShareLast">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="shareLastNumber"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.ShareLast"
        USE_BLANK="true"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="shareLastOption"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
  </CLUSTER>
  <!-- END, CR00351647 -->
</VIEW>
