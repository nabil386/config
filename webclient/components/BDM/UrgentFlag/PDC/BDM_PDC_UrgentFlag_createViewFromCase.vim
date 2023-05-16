<?xml version="1.0" encoding="UTF-8"?>
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for the modify alternate name pages.                 -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="Page.Title"
      />
    </CONNECT>
  </PAGE_TITLE>

  <SERVER_INTERFACE
    CLASS="BDMCaseUrgentFlag"
    NAME="DISPLAY"
    OPERATION="setCurrentDateAsDefaultStartDate"
    PHASE="DISPLAY"
  />

  <SERVER_INTERFACE
    CLASS="BDMCaseUrgentFlag"
    NAME="ACTION"
    OPERATION="createCaseUrgentFlag"
    PHASE="ACTION"
  />

  <PAGE_PARAMETER NAME="caseID"/>
  
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="key$caseID"
    />
  </CONNECT>
  
  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="result$startDate"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="key$startDate"
    />
  </CONNECT>

  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="1"
  >
    <FIELD
      CONTROL="CT_HIERARCHY_HORIZONTAL"
      LABEL="Cluster.Title.UrgentFlagType"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="90"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$type"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.StartDate"
      USE_BLANK="false"
      USE_DEFAULT="true"
      WIDTH="35"
    >
	  <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$startDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.EndDate"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="35"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$endDate"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
