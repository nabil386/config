<?xml version="1.0" encoding="UTF-8"?>
<!-- Description -->
<!-- =========== -->
<!-- The included view for creating a deduction type.                       -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <!-- BEGIN, CR00463142, EC -->
  <CLUSTER
    LABEL_WIDTH="47"
    NUM_COLS="2"
    SUMMARY="Summary.NewDeductions.Details"
  >
    <!-- END, CR00463142 -->
    <FIELD
      LABEL="Field.Label.Name"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="deductionName"
        />
      </CONNECT>
    </FIELD>
    <!-- START Task 25847-->
    <FIELD
      LABEL="Field.Label.DeductionType"
      USE_BLANK="true"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="deductionType"
        />
      </CONNECT>
    </FIELD>
    <!-- END Task 25847-->
    <FIELD LABEL="Field.Label.MaxDeductionAmount">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="maxAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.MaxDeductionPercentage">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="maxPercentage"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.MinDeductionAmount">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="minAmount"
        />
      </CONNECT>
    </FIELD>
    
    <!--BEGIN - TASK 16272 - CK - Third Party Deduction Mapping -->
    <FIELD LABEL="Field.Label.ThirdPartyConcernRole">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$dtls$thirdPartyConcernRoleID"
        />
      </CONNECT>
    </FIELD>
    <!--END - TASK 16272 - CK - Third Party Deduction Mapping -->

    <FIELD LABEL="Field.Label.Priority">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="priority"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.PreventOverlappingInd">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="preventOverlappingInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Category"
      USE_DEFAULT="true"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="category"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DefaultAmount">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="defaultAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DefaultPercentage">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="defaultPercentage"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ActionType"
      USE_DEFAULT="true"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="actionType"
        />
      </CONNECT>
    </FIELD>
    
    <!--BEGIN - TASK 16272 - CK - Third Party Deduction Mapping -->
    <FIELD LABEL="Field.Label.ExternalLiabilityType" USE_BLANK="true">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="externalLiabilityType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$dtls$externalLiabilityType"
        />
      </CONNECT>
    </FIELD>
    <!--END - TASK 16272 - CK - Third Party Deduction Mapping -->
    
    <FIELD LABEL="Field.Label.ManagedBy">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$dtls$managedBy"
        />
      </CONNECT>
    </FIELD>
    

    <FIELD LABEL="Field.Label.AssignNextPriorityInd">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="applyNextPriorityInd"
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
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
