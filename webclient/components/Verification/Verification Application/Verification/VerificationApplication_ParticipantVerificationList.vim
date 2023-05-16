<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
   PID 5725-H26
   
  Copyright IBM Corporation 2007, 2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2007-2008, 2011 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- View to list all the participant verification list.                    -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText"
      />
    </CONNECT>


  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="VerificationApplication"
    NAME="DISPLAY"
    OPERATION="listParticipantVerificationDetails"
    PHASE="DISPLAY"
  />
  <!-- BEGIN, CR00451497, GK -->
  <ACTION_SET>
    <ACTION_CONTROL
      IMAGE="AddButton"
      LABEL="ActionControl.Label.VerifyAll"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="VerificationApplication_verifyOutstandingEvdForPDC"
      >


        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
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
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$participantID"
    />
  </CONNECT>


  <CLUSTER
    SHOW_LABELS="false"
    STYLE="tab-renderer"
  >


    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="statusInd"
      />
    </CONDITION>


    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="data"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <LIST>


    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="VerificationApplication_viewWorkspaceParticipantVerificationDetails">
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
            PROPERTY="contextDescription"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="contextDescription"
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
            PROPERTY="vDIEDLinkID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="VDIEDLinkID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


    <!-- BEGIN, CR00351373, KRK -->
    <ACTION_SET TYPE="LIST_ROW_MENU">


      <ACTION_CONTROL
        IMAGE="AddButton"
        LABEL="ActionControl.Label.AddItem"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="VerificationApplication_createParticipantVerificationItemProvision"
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
    <!-- END, CR00351373 -->


    <FIELD
      LABEL="Field.Label.Verification"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="verifiableDataItemName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Evidence"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="evidenceTypeOpt"
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


    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="15"
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
