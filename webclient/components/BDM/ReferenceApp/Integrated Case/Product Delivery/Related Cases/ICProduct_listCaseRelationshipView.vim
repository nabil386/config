<?xml version="1.0" encoding="UTF-8"?>
<!-- Description -->
<!-- =========== -->
<!-- The included view to display a list of related cases for an integrated -->
<!-- case.                                                                  -->
<VIEW
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

  <!-- BEGIN - TASK 10634 - CK - Liability Status  -->
  <SERVER_INTERFACE
    CLASS="BDMCaseDisplay"
    NAME="DISPLAY"
    OPERATION="listProductDeliveryCaseRelationship"
  />
  <!-- END - TASK 10634 - CK - Liability Status  -->


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


  <ACTION_SET BOTTOM="false">


    <ACTION_CONTROL
      IMAGE="NewButton"
      LABEL="ActionControl.Label.New"
    >


      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Case_createCaseRelationship"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
      </LINK>


    </ACTION_CONTROL>


  </ACTION_SET>
  <LIST>


    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">


        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Case_modifyCaseRelationshipFromList"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="caseRelationshipID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseRelationshipID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="pageDescription"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtls$caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


    </ACTION_SET>


    <CONTAINER
      LABEL="Container.Label.RelatedCase"
      SEPARATOR="Container.HyphenSeparator"
      WIDTH="55"
    >
      <FIELD LABEL="Field.Label.RelatedCaseReference">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="relatedCaseReference"
          />
        </CONNECT>
        <LINK PAGE_ID="Case_resolveCaseHome">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="relatedCaseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
        </LINK>
      </FIELD>

      <!-- BEGIN - TASK 10634 - CK - Liability Status  -->
      <FIELD LABEL="Field.Label.RelatedCaseType">
        <!-- BEGIN, CR00427963, KRK -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseType"
          />
        </CONNECT>
        <!-- END, CR00427963 -->
        <LINK PAGE_ID="Case_resolveCaseHome">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="relatedCaseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
        </LINK>
      </FIELD>
      <!-- END - TASK 10634 - CK - Liability Status  -->
    </CONTAINER>


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

    <!-- BEGIN - TASK 10634 - CK - Liability Status  -->
    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="status"
        />
      </CONNECT>
    </FIELD>
    <!-- END - TASK 10634 - CK - Liability Status  -->


    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Case_viewCaseRelationship">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseRelationshipID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseRelationshipID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="dtls$caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>
  </LIST>


</VIEW>
