<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2012, 2020. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010-2011 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
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


  <!-- BEGIN, CR00261681, AKr -->
  <!-- BEGIN, TASK 27331 set documentSubmitted ind for Participant Type verification  -->
  <SERVER_INTERFACE
    CLASS="BDMVerificationApplication"
    NAME="DISPLAY"
    OPERATION="listOutstandingVerificationDetailsForCaseEvidence"
    PHASE="DISPLAY"
  />
  <!-- END, TASK 27331 set documentSubmitted ind for Participant Type verification  -->
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
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="verifiableDataItemName"
        />
      </CONNECT>
    </FIELD>


    <!--<FIELD
      LABEL="Field.Label.EvidenceDetails"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="evidence"
        />
      </CONNECT>
    </FIELD>-->


    <FIELD
      LABEL="Field.Label.EvidenceType"
      WIDTH="25"
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
  </LIST>
  <!-- BEGIN, CR00426702, GK -->
  <INCLUDE FILE_NAME="Evidence_unevaluatedEvidenceMsgView.vim"/>
  <!-- END, CR00426702 -->


</VIEW>
