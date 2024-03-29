<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<VIEW
  PAGE_ID="ProcessDefinitionTool_createNotificationActionDetails"
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
  <PAGE_PARAMETER NAME="activityID"/>
  <PAGE_PARAMETER NAME="processID"/>
  <PAGE_PARAMETER NAME="processVersionNo"/>
  <PAGE_PARAMETER NAME="versionNo"/>
  <PAGE_PARAMETER NAME="activityName"/>
  <PAGE_PARAMETER NAME="activityType"/>
  <CLUSTER
    LABEL_WIDTH="25%"
    TITLE="Cluster.Title"
  >
    <FIELD LABEL="Field.Label.PageID">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="pageID"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.ActionText">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="actionText"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.OpenInModal">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="openModalInd"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER
    LABEL_WIDTH="25%"
    TITLE="Cluster.Ocurring.Title"
  >
    <FIELD LABEL="Field.Label.WDOToUse">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="listWDOToUse"
        />
      </CONNECT>
      <LINK>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="processID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="processID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="processVersionNo"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="processVersionNo"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="activityID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="activityID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="CONSTANT"
            PROPERTY="Filter.Type.ListOnly"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="filterType"
          />
        </CONNECT>
      </LINK>
    </FIELD>
  </CLUSTER>
</VIEW>
