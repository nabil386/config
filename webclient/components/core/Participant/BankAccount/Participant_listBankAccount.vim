<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2002, 2013. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2002-2008, 2010, 2011 Curam Software Ltd.                    -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Lists existing bank accounts.                                          -->
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


  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="DISPLAY"
    OPERATION="listBankAccount"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="ISDUPLICATE"
    OPERATION="isParticipantDuplicate"
  />


  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="informationalMsgDtlsList$dtls$informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>


  <PAGE_PARAMETER NAME="concernRoleID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="concernRoleID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="ISDUPLICATE"
      PROPERTY="concernRoleID"
    />
  </CONNECT>


  <!-- BEGIN, CR00075582, RF -->
  <SERVER_INTERFACE
    CLASS="VerificationApplication"
    NAME="DISPLAY1"
    OPERATION="listBankAccountVerificationDetails"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="DISPLAY1"
      PROPERTY="participantID"
    />
  </CONNECT>
  <!-- END, CR00075582 -->


  <ACTION_SET BOTTOM="false">


    <ACTION_CONTROL
      IMAGE="NewButton"
      LABEL="ActionControl.Label.New"
    >
      <CONDITION>
        <IS_FALSE
          NAME="ISDUPLICATE"
          PROPERTY="statusInd"
        />
      </CONDITION>
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Participant_createBankAccount"
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


  <LIST>


    <ACTION_SET TYPE="LIST_ROW_MENU">


      <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
        <CONDITION>
          <IS_FALSE
            NAME="ISDUPLICATE"
            PROPERTY="statusInd"
          />
        </CONDITION>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Participant_modifyBankAccountFromList"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="concernRoleBankAccountID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleBankAccountID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL
        IMAGE="TransferButton"
        LABEL="ActionControl.Label.Transfer"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Participant_confirmUpdatePaymentBankAccount"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="bankAccountID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="oldBankAccountID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="concernRoleBankAccountID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleBankAccountID"
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
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="bankBranchName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="oldBankBranchName"
            />
          </CONNECT>
          <!-- BEGIN, CR00372632, VT -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="bankAccountNumberOpt"
            />
            <!-- END, CR00372632 -->
            <TARGET
              NAME="PAGE"
              PROPERTY="oldAccountNumber"
            />
          </CONNECT>


          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="name"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="oldAccountName"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL
        IMAGE="DeleteButton"
        LABEL="ActionControl.Label.Delete"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Participant_cancelBankAccount"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="concernRoleBankAccountID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleBankAccountID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="versionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="versionNo"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


    </ACTION_SET>


    <DETAILS_ROW>


      <INLINE_PAGE PAGE_ID="Participant_viewBankAccount">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="concernRoleBankAccountID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleBankAccountID"
          />
        </CONNECT>
      </INLINE_PAGE>


    </DETAILS_ROW>


    <FIELD
      LABEL="Field.Label.Primary"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="primaryInd"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00406453, SS -->
    <FIELD
      LABEL="Field.Label.AccountName"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.AccountType"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="typeCode"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00371769, VT -->
    <FIELD
      LABEL="Field.Label.BankAccount"
      WIDTH="25"
    >
      <!-- END, CR00371769 -->
      <!-- BEGIN, CR00372632, VT -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bankAccountNumberOpt"
        />
      </CONNECT>
      <!-- END, CR00372632 -->
    </FIELD>
    <FIELD
      LABEL="Field.Label.Status"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="statusCode"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00406453 -->


  </LIST>


  <!-- BEGIN, CR00075582, RF -->
  <INCLUDE FILE_NAME="VerificationApplication_listParticipantEvidenceVerificationRequirements.vim"/>
  <!-- END, CR00075582 -->


</VIEW>
