<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Views pro forma communication details.                                 -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

  <SERVER_INTERFACE
    CLASS="BDMCommunication"
    NAME="DISPCANCEL"
    OPERATION="viewCancelReason"
  />


  <!--PAGE_PARAMETER NAME="communicationID"/-->

  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="communicationID"
    />
    <TARGET
      NAME="DISPCANCEL"
      PROPERTY="commKey$communicationID"
    />
  </CONNECT>
 
  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.CancelReason"
  >
    <CONDITION>
    <IS_TRUE NAME="DISPCANCEL" PROPERTY="canceledInd" />
    </CONDITION>
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.CancelReason"
    >
      <!-- END, CR00416277 -->
      <CONNECT>
        <SOURCE
          NAME="DISPCANCEL"
          PROPERTY="cancelReason"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>
</VIEW>
