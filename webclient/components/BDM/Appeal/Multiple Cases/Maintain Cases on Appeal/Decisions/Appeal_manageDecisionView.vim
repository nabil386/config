<?xml version="1.0" encoding="UTF-8"?>
<!-- TASK 56872 Appeals Issue Edit Decision -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="Appeal"
    NAME="DISPLAYITEMS"
    OPERATION="listAllRelatedByAppealCase"
    PHASE="DISPLAY"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAYITEMS"
      PROPERTY="caseID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >
    <CONTAINER LABEL="Field.Label.DecisionResolution">
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="decisionResolutionCode"
          />
        </CONNECT>
      </FIELD>


      <ACTION_CONTROL
        APPEND_ELLIPSIS="false"
        LABEL="ActionControl.Label.EditComments"
      >
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAYITEMS"
            PROPERTY="decisionIndicator"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Appeal_modifyDecisionComments"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="decisionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="hearingDecisionID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>

    </CONTAINER>
    <FIELD LABEL="Field.Label.DecisionStatus">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="decisionStatus"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <LIST TITLE="List.Title.Resolutions">
  
    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAYITEMS"
            PROPERTY="decisionIndicator"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Appeal_resolveModifyAppealedCaseResolution"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAYITEMS"
              PROPERTY="appealRelationshipID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="appealRelationshipID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAYITEMS"
              PROPERTY="priorAppealIndicator"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="priorAppealIndicator"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>

    <FIELD
      LABEL="Field.Label.ItemUnderAppeal"
      WIDTH="40"
    >
      <LINK
        URI_SOURCE_NAME="DISPLAYITEMS"
        URI_SOURCE_PROPERTY="appealItemHomePageURI"
      />
      <CONNECT>
        <SOURCE
          NAME="DISPLAYITEMS"
          PROPERTY="appealObjectDescription"
        />
      </CONNECT>
    </FIELD>

    <FIELD
      LABEL="Field.Label.AppealedCaseType"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAYITEMS"
          PROPERTY="appealObectType"
        />
      </CONNECT>
    </FIELD>

    <FIELD
      LABEL="Field.Label.Resolution"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAYITEMS"
          PROPERTY="dtls$resolutionCode"
        />
      </CONNECT>
    </FIELD>
    
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="BDMAppeal_viewIssueDecisionForHearingCase">
        <CONNECT>
            <SOURCE
              NAME="DISPLAYITEMS"
              PROPERTY="appealRelationshipID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="appealRelationshipID"
            />
          </CONNECT>          
      </INLINE_PAGE>
    </DETAILS_ROW>
    
  </LIST>


  <LIST TITLE="List.Title.DecisionAttachments">


    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Appeal_resolveViewDecisionAttachment">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="attachmentLinkID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="attachmentLinkID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="decisionAttachmentType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="decisionAttachmentType"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="appealCaseID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="description"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="decisionStatus"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="decisionStatus"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL
        IMAGE="OpenButton"
        LABEL="ActionControl.Label.Open"
        TYPE="FILE_DOWNLOAD"
      >
        <LINK>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="attachmentLinkID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="attachmentLinkID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="ActionControl.Label.EditAttachment">
        <CONDITION>
          <IS_TRUE
            NAME="DISPLAY"
            PROPERTY="externalInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Appeal_resolveModifyDecisionAttachment"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="attachmentLinkID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="attachmentLinkID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="decisionAttachmentType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="decisionAttachmentType"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="appealCaseID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="description"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="CONSTANT"
              PROPERTY="SearchPageID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="searchPage"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL
        IMAGE="DeleteButton"
        LABEL="ActionControl.Label.Delete"
        TYPE="ACTION"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Appeal_deleteDecisionAttachment"
        >
          <!-- BEGIN, CR00249646, SS -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="decisionDetails$activeAttachmentDetailsList$dtls$versionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="versionNo"
            />
          </CONNECT>
          <!-- END, CR00249646 -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="attachmentLinkID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="attachmentLinkID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="description"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <FIELD
      LABEL="Field.Label.Date"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="attachmentDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.AttachmentType"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="decisionAttachmentType"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="recordStatus"
        />
      </CONNECT>
    </FIELD>


  </LIST>
</VIEW>
