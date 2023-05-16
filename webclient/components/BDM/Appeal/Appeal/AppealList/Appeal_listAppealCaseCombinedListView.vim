<?xml version="1.0" encoding="UTF-8"?>
<!-- Task-62999                                                             -->
<!-- ===========                                                            -->
<!-- This page allows you to display a list of appeals for a standalone     -->
<!-- case.                                                                  -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.Title"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="BDMAppeal"
    NAME="DISPLAY"
    OPERATION="listAppealCombinedByCase"
  />


  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
  </CONNECT>


  <LIST>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Appeal_listObjectsByAppeal">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="appealCaseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


    <ACTION_SET TYPE="LIST_ROW_MENU">


      <ACTION_CONTROL
        IMAGE="CreateHearingCaseButton"
        LABEL="ActionControl.Label.NewHearing"
      >
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="hearingIndicator"
          />
        </CONDITION>


        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Appeal_nextStageAppeal"
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
              PROPERTY="nextStageHearing"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="appealTypeCode"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="appealCaseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="priorAppealID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL
        IMAGE="CreateHearingCaseButton"
        LABEL="ActionControl.Label.NewHearingReview"
      >
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="hearingReviewIndicator"
          />
        </CONDITION>


        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Appeal_nextStageAppeal"
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
              PROPERTY="nextStageHearingReview"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="appealTypeCode"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="appealCaseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="priorAppealID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL
        IMAGE="CreateHearingCaseButton"
        LABEL="ActionControl.Label.NewJudicialReview"
      >
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="judicialIndicator"
          />
        </CONDITION>


        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Appeal_nextStageAppeal"
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
              PROPERTY="nextStageJudicialReview"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="appealTypeCode"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="appealCaseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="priorAppealID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


    </ACTION_SET>


    <FIELD
      LABEL="Field.Label.appealDescription"
      WIDTH="20"
    >
      <LINK PAGE_ID="Appeal_resolveHearingCaseHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="appealCaseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="appealTypeCode"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="appealTypeCode"
          />
        </CONNECT>
      </LINK>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="appealDescription"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.ItemsUnderAppeal"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="itemsUnderAppeal"
        />
      </CONNECT>


    </FIELD>


    <FIELD
      LABEL="Field.Label.Appellant"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="appellantName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Owner"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="ownerFullName"
        />
      </CONNECT>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Organization_viewUserDetails"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="ownerUserName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="userName"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Deadline"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="deadlineDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="statusCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Resolution"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="decisionResolutionCode"
        />
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>
