<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
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
<!-- List verification requirements related to evidence type.               -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <SERVER_INTERFACE
    CLASS="VerificationApplication"
    NAME="DISPLAY_VERIFICATIONS"
    OPERATION="listVerificationsForCaseEvidence"
    PHASE="DISPLAY"
  />


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="evidenceType"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY_VERIFICATIONS"
      PROPERTY="key$dtls$caseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="evidenceType"
    />
    <TARGET
      NAME="DISPLAY_VERIFICATIONS"
      PROPERTY="key$dtls$evidenceType"
    />
  </CONNECT>


  <CLUSTER
    SHOW_LABELS="false"
    STYLE="outer-cluster-borderless"
  >
    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY_VERIFICATIONS"
        PROPERTY="result$showVerificationCluster"
      />
    </CONDITION>
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="TEXT"
          PROPERTY="Field.StaticText.VerificationsNotInstalled"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <LIST>


    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY_VERIFICATIONS"
        PROPERTY="result$showVerificationCluster"
      />
    </CONDITION>


    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Evidence_viewVerificationListItemDetails">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY_VERIFICATIONS"
            PROPERTY="vdIEDLinkID"
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
            NAME="DISPLAY_VERIFICATIONS"
            PROPERTY="evidenceDescriptorID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceDescriptorID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY_VERIFICATIONS"
            PROPERTY="dataItemName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="dataItemName"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY_VERIFICATIONS"
            PROPERTY="summary"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="summary"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY_VERIFICATIONS"
            PROPERTY="verificationStatusDescription"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="verificationStatusDescription"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


    <FIELD LABEL="Field.Label.ItemforVerification">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_VERIFICATIONS"
          PROPERTY="verifiableDataItemNameXMLOpt"
        />
      </CONNECT>
    </FIELD>


    <!--<FIELD LABEL="Field.Title.EvidenceDetails">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_VERIFICATIONS"
          PROPERTY="summary"
        />
      </CONNECT>
    </FIELD>-->


    <FIELD LABEL="Field.Title.Participant">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_VERIFICATIONS"
          PROPERTY="concernRoleName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Title.DueDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_VERIFICATIONS"
          PROPERTY="dueDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Title.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY_VERIFICATIONS"
          PROPERTY="verificationStatus"
        />
      </CONNECT>
    </FIELD>


    <ACTION_SET TYPE="LIST_ROW_MENU">


      <ACTION_CONTROL LABEL="ActionControl.Label.AddItem">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="VerificationApplication_createVerificationItemProvision"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY_VERIFICATIONS"
              PROPERTY="vdIEDLinkID"
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
          <!-- must be passed in later -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY_VERIFICATIONS"
              PROPERTY="concernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleID"
            />
          </CONNECT>


        </LINK>
      </ACTION_CONTROL>


      <!-- BEGIN, CR00291958, KRK -->
      <ACTION_CONTROL
        APPEND_ELLIPSIS="false"
        LABEL="ActionControl.Label.ViewEvidence"
      >
        <!-- END, CR00291958 -->
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Evidence_resolveViewEvidencePage"
        >


          <CONNECT>
            <SOURCE
              NAME="DISPLAY_VERIFICATIONS"
              PROPERTY="key$dtls$evidenceType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceType"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY_VERIFICATIONS"
              PROPERTY="relatedID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY_VERIFICATIONS"
              PROPERTY="evidenceDescriptorID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="evidenceDescriptorID"
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
              NAME="DISPLAY_VERIFICATIONS"
              PROPERTY="dataItemName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="contextDescription"
            />
          </CONNECT>


        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


  </LIST>


</VIEW>
