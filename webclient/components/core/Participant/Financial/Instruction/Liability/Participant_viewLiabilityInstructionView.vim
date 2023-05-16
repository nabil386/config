<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2009, 2010 Curam Software Ltd.                           -->
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
<?curam-deprecated Since Curam 6.0, replaced by Financial_viewLiabilityInstructionView1.vim?>
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
    <!-- BEGIN, CR00160310, MC -->
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="contextDescription"
      />
    </CONNECT>
    <!-- END, CR00160310 -->
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="Financial"
    NAME="DISPLAY"
    OPERATION="readLiabilityInstruction"
  />


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
    LABEL_WIDTH="45"
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >


    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="concernRoleName"
        />
      </CONNECT>
    </FIELD>


    <CONTAINER LABEL="Field.Label.Amount">
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="liabilityHeaderDetails$currencyType"
          />
        </CONNECT>
      </FIELD>
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="liabilityHeaderDetails$amount"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>


    <FIELD LABEL="Field.Label.NomineeName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="liabilityHeaderDetails$nomineeName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.CoverPeriodFrom">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="liabilityHeaderDetails$coverPeriodFromDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EffectiveDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="liabilityHeaderDetails$effectiveDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.LiabilityReference">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="liabilityHeaderDetails$liabInstrumentID"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="liabilityHeaderDetails$statusCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ForeignCurrency">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="liabilityHeaderDetails$foreignCurrency"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.NomineeAddress">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="liabilityHeaderDetails$nomineeAddress"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.CoverPeriodTo">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="liabilityHeaderDetails$coverPeriodToDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DatePosted">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="liabilityHeaderDetails$datePosted"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <LIST TITLE="Cluster.Title.LineItems">


    <CONTAINER
      LABEL="Container.Label.Action"
      SEPARATOR="Container.Separator"
      WIDTH="20"
    >


      <ACTION_CONTROL LABEL="ActionControl.Label.View">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Participant_viewLiabilityLineItem"
          SAVE_LINK="false"
        >
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
          <!-- BEGIN, CR00160310, MC -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="contextDescription"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="description"
            />
          </CONNECT>
          <!-- END, CR00160310 -->
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="ActionControl.Label.ViewAllocations">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Participant_viewLineItemAllocation"
        >
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
          <!-- BEGIN, CR00160310, MC -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="contextDescription"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="description"
            />
          </CONNECT>
          <!-- END, CR00160310 -->
        </LINK>
      </ACTION_CONTROL>


    </CONTAINER>


    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$liabilityType"
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
          PROPERTY="dtls$effectiveDate"
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
          PROPERTY="dtls$statusCode"
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
      LABEL="Field.Label.Amount"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$amount"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      ALIGNMENT="RIGHT"
      LABEL="Field.Label.OutstandingAmount"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$outstandingAmount"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
