<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003, 2009, 2011 Curam Software Ltd.                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view to display the details of a reversed financial       -->
<!-- instruction.                                                           -->
<?curam-deprecated Since Curam 6.0. Replaced by Financial_ReversedLiabilityInstructionView.vim. ?>
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
    CLASS="Financial"
    NAME="DISPLAY"
    OPERATION="readReversalInstruction"
  />


  <ACTION_SET ALIGNMENT="CENTER">
    <ACTION_CONTROL
      IMAGE="ViewReversedItemButton"
      LABEL="ActionControl.Label.ViewReversedItem"
    >
      <LINK
        PAGE_ID="Financial_resolveInstructionView"
        SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="reversalFinInstructionID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="instructionID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="reversalTypeCode"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="instructionType"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      IMAGE="CloseButton"
      LABEL="ActionControl.Label.Close"
    />


  </ACTION_SET>


  <PAGE_PARAMETER NAME="finInstructionID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="finInstructionID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$finInstructionID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="35"
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >


    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reversalInstructionHeaderDetails$concernRoleName"
        />
      </CONNECT>
      <LINK PAGE_ID="Participant_resolveRoleHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="reversalInstructionHeaderDetails$concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="reversalInstructionHeaderDetails$concernRoleType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="participantType"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <CONTAINER LABEL="Field.Label.Amount">
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="reversalInstructionHeaderDetails$currencyType"
          />
        </CONNECT>
      </FIELD>
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="reversalInstructionHeaderDetails$amount"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>


    <FIELD LABEL="Field.Label.ReversalDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reversalInstructionHeaderDetails$effectiveDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Comments">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reversalInstructionHeaderDetails$comments"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reversalInstructionHeaderDetails$statusCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ForeignCurrency">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reversalInstructionHeaderDetails$foreignCurrency"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ReversalReason">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="reversalInstructionHeaderDetails$reasonCodeDescription"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <LIST TITLE="Cluster.Title.ReversedLineItems">


    <CONTAINER
      LABEL="Container.Label.Action"
      SEPARATOR="Container.Separator"
      WIDTH="15"
    >


      <ACTION_CONTROL LABEL="ActionControl.Label.View">
        <!-- BEGIN, CR00156182, SAI-->
        <LINK PAGE_ID="Financial_resolveViewReversalLineItem">
          <!-- END, CR00156182 -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtls$instructionLineItemID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="lineItemID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="ActionControl.Label.ViewAllocations">
        <LINK PAGE_ID="Financial_viewLineItemAllocation">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtls$instructionLineItemID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="lineItemID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


    </CONTAINER>


    <FIELD
      LABEL="Field.Label.CaseReference"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseReference"
        />
      </CONNECT>
      <LINK PAGE_ID="Case_resolveCaseHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
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
      LABEL="Field.Label.EffectiveDate"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$effectiveDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Currency"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$currencyType"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.Debit"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="debitAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.Credit"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="creditAmount"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.OutstandingAmount"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="outstandingAmount"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
