<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2008,2019. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- The included view for the modify alternate name pages. -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <!-- The page title for this page -->
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="Page.Title"
      />
    </CONNECT>
  </PAGE_TITLE>


  <!--  Server Interface Element  -->
  <SERVER_INTERFACE
    CLASS="SpecialCaution"
    NAME="ACTION"
    OPERATION="createSpecialCaution"
    PHASE="ACTION"
  />
  <SERVER_INTERFACE
    CLASS="SpecialCaution"
    NAME="LIST"
    OPERATION="listSpecialCautions"
    PHASE="DISPLAY"
  />


  <!-- Page Parameter -->
  <PAGE_PARAMETER NAME="concernRoleID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="LIST"
      PROPERTY="key$key$concernRoleID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$dtls$concernRoleID"
    />
  </CONNECT>


  <!-- Cluster for the special caution to be created -->
  <CLUSTER
    LABEL_WIDTH="46">
    <CLUSTER
      NUM_COLS="1"
      SHOW_LABELS="false"
    >
      <!-- BEGIN, CR00273322, KRK -->
      <FIELD
        ALIGNMENT="LEFT"
        CONFIG="CT_DISPLAY_LABELS"
        CONTROL="CT_HIERARCHY_VERTICAL"
        LABEL="Field.Label.Type"
        USE_BLANK="true"
        USE_DEFAULT="false"
      >
        <!-- END, CR00273322 -->


        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="details$dtls$typeCode"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
    
    <FIELD
      LABEL="Field.Label.StartDate"
      USE_BLANK="true"
      USE_DEFAULT="true"
      WIDTH="55"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$dtls$startDate"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD
      LABEL="Field.Label.EndDate"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="55"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$dtls$endDate"
        />
      </CONNECT>
    </FIELD>
    
  </CLUSTER>
  
  <CLUSTER
    SHOW_LABELS="FALSE"
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
          PROPERTY="details$dtls$comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>