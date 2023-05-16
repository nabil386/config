<?xml version="1.0" encoding="UTF-8"?>
<!-- Description -->
<!-- =========== -->
<!-- This page displays search criteria for a Case.                         -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="Case"
    NAME="DISPLAY"
    OPERATION="getCaseSearchCriteria"
  />


  <!-- BEGIN, CR00274833, ELG -->
  <SERVER_INTERFACE
    CLASS="BDMCaseDisplay"
    NAME="ACTION"
    OPERATION="caseSearch2"
    PHASE="ACTION"
  />


  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="ACTION"
        PROPERTY="informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>
  <!-- END, CR00274833 -->


  <!-- BEGIN, CR00378592, PS -->
  <CLUSTER
    LABEL_WIDTH="25"
    NUM_COLS="1"
    TITLE="Cluster.Title.SearchCriteria"
  >


    <CLUSTER
      LABEL_WIDTH="50"
      NUM_COLS="2"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >


      <FIELD
        LABEL="Field.Label.CaseReference"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="key$caseReference"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


    <!-- BEGIN, CR00358330, DJ -->
    <CLUSTER
      NUM_COLS="1"
      STYLE="cluster-cpr-no-border"
    >


      <CLUSTER
        LABEL_WIDTH="50"
        NUM_COLS="2"
        SHOW_LABELS="true"
      >


        <FIELD
          LABEL="Field.Label.ClientReferenceType"
          USE_BLANK="false"
          USE_DEFAULT="true"
        >
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="key$alternateIDType"
            />
          </CONNECT>
        </FIELD>


        <FIELD LABEL="Field.Label.ClientReferenceNumber">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="key$alternateID"
            />
          </CONNECT>
        </FIELD>


      </CLUSTER>
    </CLUSTER>
    <!-- END, CR00358330 -->


    <CLUSTER
      LABEL_WIDTH="50"
      NUM_COLS="2"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >
      <!-- END, CR00378592 -->


      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="indexCaseSearchEnabledInd"
        />
      </CONDITION>


      <FIELD LABEL="Field.Label.ClientFirstName">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="key$clientFirstName"
          />
        </CONNECT>
      </FIELD>


      <FIELD LABEL="Field.Label.ClientSurname">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="key$clientSurname"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


    <!-- BEGIN, CR00378592, PS -->
    <CLUSTER
      LABEL_WIDTH="50"
      NUM_COLS="2"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >
      <!-- END, CR00378592 -->


      <FIELD
        CONTROL="CHECKBOXED_LIST"
        HEIGHT="4"
        LABEL="Field.Label.CriteriaType"
        USE_BLANK="false"
        WIDTH="100"
      >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="categoryType"
            NAME="DISPLAY"
            PROPERTY="typeDescription"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="categoryTypeList"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        CONTROL="CHECKBOXED_LIST"
        HEIGHT="4"
        LABEL="Field.Label.Status"
        USE_BLANK="false"
        WIDTH="100"
      >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="statusCode"
            NAME="DISPLAY"
            PROPERTY="statusDescription"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="statusList"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


    <!-- BEGIN, CR00378592, PS -->
    <CLUSTER
      LABEL_WIDTH="50"
      NUM_COLS="2"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >
      <!-- BEGIN, CR00335727, GA -->
      <FIELD
        LABEL="Field.Label.StartDateFrom"
        USE_DEFAULT="false"
        WIDTH="80"
      >
        <!-- END, CR00335727 -->
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="key$startDateFrom"
          />
        </CONNECT>
      </FIELD>


      <!-- BEGIN, CR00335727, GA -->
      <!-- BEGIN, CR00335039, PB -->


      <FIELD
        LABEL="Field.Label.StartDateTo"
        USE_DEFAULT="false"
        WIDTH="80"
      >
        <!-- END, CR00335706 -->
        <!-- END, CR00335727 -->
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="key$startDateTo"
          />
        </CONNECT>
      </FIELD>


      <!-- BEGIN, CR00335727, GA -->
      <FIELD
        LABEL="Field.Label.EndDateFrom"
        USE_DEFAULT="false"
        WIDTH="80"
      >
        <!-- END, CR00335727 -->
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="key$endDateFrom"
          />
        </CONNECT>
      </FIELD>


      <!-- BEGIN, CR00335727, GA -->
      <!-- BEGIN, CR00335039, PB -->


      <FIELD
        LABEL="Field.Label.EndDateTo"
        USE_DEFAULT="false"
        WIDTH="80"
      >
        <!-- END, CR00335706 -->
        <!-- END, CR00335727 -->
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="key$endDateTo"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


    <CLUSTER
      LABEL_WIDTH="50"
      NUM_COLS="2"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
      TITLE="Cluster.Title.Filter"
    >
      <!-- END, CR00378592 -->


      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY"
          PROPERTY="appealsInstalledInd"
        />
      </CONDITION>


      <FIELD
        LABEL="Field.Label.DisplayCasesWithAppeals"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="key$searchWithAppeals"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.DisplayCasesWithIssues"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="key$searchWithIssues"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.DisplayCasesWithInvestigations"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="key$searchWithInvestigations"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.DisplayCasesWithServicePlans"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="key$searchWithServicePlans"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


    <!-- BEGIN, CR00378592, PS -->
    <CLUSTER
      LABEL_WIDTH="50"
      NUM_COLS="3"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
      TITLE="Cluster.Title.Filter"
    >
      <!-- END, CR00378592 -->


      <CONDITION>
        <IS_FALSE
          NAME="DISPLAY"
          PROPERTY="appealsInstalledInd"
        />
      </CONDITION>


      <FIELD
        LABEL="Field.Label.DisplayCasesWithInvestigations"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="key$searchWithInvestigations"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.DisplayCasesWithServicePlans"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="key$searchWithServicePlans"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.DisplayCasesWithIssues"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="key$searchWithIssues"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


  </CLUSTER>


</VIEW>
