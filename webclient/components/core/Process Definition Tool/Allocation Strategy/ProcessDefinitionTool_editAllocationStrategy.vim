<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012,2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<VIEW
  PAGE_ID="ProcessDefinitionTool_editAllocationStrategy"
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
  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="result$dtls$dtls$message"
      />
    </CONNECT>
  </INFORMATIONAL>
  <CLUSTER
    DESCRIPTION="Cluster.Description"
    LABEL_WIDTH="50"
  >
    <CONTAINER LABEL="Container.Label.AllocationStrategyType">
      <FIELD
        USE_BLANK="true"
        WIDTH="30"
      >
        <SCRIPT
          ACTION="clearAllocationStrategy()"
          EVENT="ONCHANGE"
          SCRIPT_FILE="PDT.js"
        />
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$allocationStrategyType"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="allocationStrategyType"
          />
        </CONNECT>
      </FIELD>
      <FIELD WIDTH="60">
        <LINK>
          <CONNECT>
            <SOURCE
              NAME="CONSTANT"
              PROPERTY="Filter.Type.Allocation.BPO.Method"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="typeFilter"
            />
          </CONNECT>
        </LINK>
        <CONNECT>
          <INITIAL
            NAME="DISPLAY"
            PROPERTY="result$allocationStrategyDisplayValue"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="allocationStrategyValue"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="allocationStrategyValue"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>


  </CLUSTER>
</VIEW>
