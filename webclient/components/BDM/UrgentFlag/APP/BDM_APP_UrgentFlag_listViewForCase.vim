<?xml version="1.0" encoding="UTF-8"?>
<!-- Description -->
<!-- =========== -->
<!-- The included view for special cautions for the  case.                  -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="BDMCaseUrgentFlag"
    NAME="DISPLAY"
    OPERATION="listCurrentUrgentFlags"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="caseID"/>
  
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$caseID"
    />
  </CONNECT>

  <LIST>
    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <LINK
          OPEN_MODAL="TRUE"
          PAGE_ID="BDM_APP_UrgentFlag_modifyFromCase"
        >
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
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="key$caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
          
        </LINK>
      </ACTION_CONTROL>
      <ACTION_CONTROL LABEL="ActionControl.Label.Delete">
        <LINK
          OPEN_MODAL="TRUE"
          PAGE_ID="BDM_APP_UrgentFlag_deleteFromCase"
        >
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
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>
    <FIELD
      LABEL="Field.Label.UrgentFlagType"
      WIDTH="25"
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
          PROPERTY="type"
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
  </LIST>
</VIEW>
