<?xml version="1.0" encoding="UTF-8"?>
<!-- Description -->
<!-- =========== -->
<!-- The included view for the modify alternate name pages. -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <!-- Page Parameter -->
  <PAGE_PARAMETER NAME="specialCautionID"/>
  <PAGE_PARAMETER NAME="concernRoleID"/>


  <!-- Cluster for displaying special caution details -->


  <CLUSTER
    SHOW_LABELS="FALSE"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00416277, VT -->
    <FIELD LABEL="Field.Label.Comments">
      <!-- END, CR00416277 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
