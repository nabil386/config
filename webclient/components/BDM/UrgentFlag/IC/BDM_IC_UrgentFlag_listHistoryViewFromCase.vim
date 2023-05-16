<?xml version="1.0" encoding="UTF-8"?>
<!-- Description -->
<!-- =========== -->
<!-- The included view for special caution history.                         -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_PARAMETER NAME="caseID"/>


  <LIST>
    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="type"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.CreatedBy"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="createdByFullName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.StartDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.EndDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="recordStatus"
        />
      </CONNECT>
    </FIELD>


    <!--<DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="BDM_IC_UrgentFlag_viewFromHistory">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="bdmCaseUrgentFlagID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="bdmCaseUrgentFlagID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>-->


  </LIST>


</VIEW>
