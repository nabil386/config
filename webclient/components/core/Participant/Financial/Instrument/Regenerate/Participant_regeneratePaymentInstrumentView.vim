<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2009 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to regenerate a canceled payament instrument -->
<?curam-deprecated Since Curam 6.0, replaced by Financial_regeneratePaymentInstrumentView.vim?>
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
    <CONNECT>
      <SOURCE
        NAME="PAGE"
        PROPERTY="concernRoleName"
      />
    </CONNECT>
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.Separator"
      />
    </CONNECT>
    <CONNECT>
      <SOURCE
        NAME="PAGE"
        PROPERTY="effectiveDate"
      />
    </CONNECT>
  </PAGE_TITLE>
  <SERVER_INTERFACE
    CLASS="Financial"
    NAME="ACTION"
    OPERATION="regenerateCanceledPaymentInstrument"
    PHASE="ACTION"
  />
  <ACTION_SET
    ALIGNMENT="CENTER"
    TOP="false"
  >
    <ACTION_CONTROL
      IMAGE="YesButton"
      LABEL="ActionControl.Label.Yes"
      TYPE="SUBMIT"
    />
    <ACTION_CONTROL
      IMAGE="NoButton"
      LABEL="ActionControl.Label.No"
    >
      <LINK
        PAGE_ID="Participant_viewPaymentInstrument"
        SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="pmtInstrumentID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="pmtInstrumentID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>
  </ACTION_SET>
  <PAGE_PARAMETER NAME="pmtInstrumentID"/>
  <PAGE_PARAMETER NAME="concernRoleName"/>
  <PAGE_PARAMETER NAME="effectiveDate"/>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="pmtInstrumentID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="pmtInstrumentID"
    />
  </CONNECT>
  <CLUSTER
    SHOW_LABELS="false"
    STYLE="outer-cluster-borderless"
  >
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="TEXT"
          PROPERTY="Field.StaticText.RegeneratePaymentInstrument"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
