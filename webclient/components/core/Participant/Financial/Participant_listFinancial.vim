<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002-2010 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This Page lists all financials for a particular participant            -->
<?curam-deprecated Since Curam 6.0, replaced by Participant_listFinancial1.vim?>
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
    OPERATION="listParticipantFinancial"
  />


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


  <ACTION_SET BOTTOM="false">
    <ACTION_CONTROL
      IMAGE="NewAccountAdjustmentButton"
      LABEL="ActionControl.Label.NewAccountAdjustment"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Participant_newAccountAdjustment"
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
            PROPERTY="contextDescription"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>


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


    <LIST>


      <DETAILS_ROW>


        <INLINE_PAGE PAGE_ID="Participant_resolveInstructionView">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="finInstructionID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="instructionID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="typeCode"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="instructionType"
            />
          </CONNECT>
        </INLINE_PAGE>


      </DETAILS_ROW>


      <FIELD
        LABEL="Field.Label.Type"
        WIDTH="25"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="typeCode"
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
            PROPERTY="statusCode"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.CreationDate"
        WIDTH="15"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="postingDate"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.EffectiveDate"
        WIDTH="15"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="effectiveDate"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.Currency"
        WIDTH="15"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="currencyType"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        ALIGNMENT="RIGHT"
        LABEL="Field.Label.Amount"
        WIDTH="15"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="amount"
          />
        </CONNECT>
      </FIELD>


    </LIST>


  </CLUSTER>


  <LIST>


    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY"
        PROPERTY="statusInd"
      />
    </CONDITION>


    <ACTION_SET BOTTOM="false">


      <ACTION_CONTROL
        IMAGE="NewAccountAdjustmentButton"
        LABEL="ActionControl.Label.NewAccountAdjustment"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Participant_newAccountAdjustment"
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
              PROPERTY="contextDescription"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


    </ACTION_SET>


    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="typeCode"
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
          PROPERTY="statusCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.CreationDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="postingDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.EffectiveDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="effectiveDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Currency"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="currencyType"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.Amount"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="amount"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
