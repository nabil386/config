<?xml version="1.0" encoding="UTF-8"?>

<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- List verifications related to a case.                                  -->
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


  <!-- BEGIN, CR00261681, AKr -->
  <!-- BEGIN, TASK 9408 New Columns on Verification Item List, Call to Cutsom Facade class  -->
  <SERVER_INTERFACE
    CLASS="BDMVerificationApplication"
    NAME="DISPLAY"
    OPERATION="listVerificationDetailsforCaseEvidence1"
    PHASE="DISPLAY"
  />
  <!-- END TASK 9408 -->
  <!-- END, CR00261681 -->


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
  <!-- BEGIN, CR00451497, GK -->
  <ACTION_SET>
    <ACTION_CONTROL
      IMAGE="AddButton"
      LABEL="ActionControl.Label.VerifyAll"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="VerificationApplication_verifyOutstandingEvd"
      >
        <CONNECT>
          <SOURCE
            NAME="CONSTANT"
            PROPERTY="Constant.NoValue"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="VDIEDLinkID"
          />
        </CONNECT>
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
            NAME="CONSTANT"
            PROPERTY="Constant.NoValue"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="wizardStateID"
          />
        </CONNECT>


      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>
  <!-- END, CR00451497 -->
  <LIST PAGINATED="false">
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Evidence_viewVerificationListItemDetails">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="vDIEDLinkID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="VDIEDLinkID"
          />
        </CONNECT>
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
            PROPERTY="evidenceDescriptorID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceDescriptorID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="verifiableDataItemName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="dataItemName"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="verificationStatusDescription"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="verificationStatusDescription"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="evidence"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="summary"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL
        IMAGE="AddButton"
        LABEL="ActionControl.Label.AddItem"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="VerificationApplication_createVerificationItemProvision"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="vDIEDLinkID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="VDIEDLinkID"
            />
          </CONNECT>
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
              PROPERTY="concernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <FIELD
      LABEL="Field.Label.ItemforVerification"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="verifiableDataItemNameXMLOpt"
        />
      </CONNECT>
    </FIELD>


    <!-- <FIELD
      LABEL="Field.Label.EvidenceDetails"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="evidence"
        />
      </CONNECT>
    </FIELD> -->


    <FIELD
      LABEL="Field.Label.EvidenceType"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="evidenceType"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Participant"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="primaryClient"
        />
      </CONNECT>
      <LINK PAGE_ID="Participant_resolve">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseParticipantRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseParticipantRoleID"
          />
        </CONNECT>
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
      </LINK>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Mandatory"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="mandatory"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.DueDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dueDate"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN 9408 New Columns on Verification Item List -->
    <FIELD
      LABEL="Field.Label.DocumentType"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="documentType"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.ProvidedBy"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="documentProvidedBy"
        />
      </CONNECT>
    </FIELD>
        <FIELD
      LABEL="Field.Label.DateReceived"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateReceived"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD
      LABEL="Field.Label.DateAdded"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateAdded"
        />
      </CONNECT>
    </FIELD>
<!-- END TASK 9408 -->

    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="verificationStatus"
        />
      </CONNECT>
    </FIELD>
  </LIST>
</VIEW>
