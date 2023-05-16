<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
 
  Copyright IBM Corporation 2010, 2013, 2014, 2015. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- TThis is the included view to create a new case nominee.               -->
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
    CLASS="Case"
    NAME="DISPLAY"
    OPERATION="listDetailsForNomineeCreation"
    PHASE="DISPLAY"
  />


  <!-- BEGIN, CR00372248, GA -->
  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="DISPLAY1"
    OPERATION="readIban"
    PHASE="DISPLAY"
  />
  <!-- END, CR00372248 -->


  <SERVER_INTERFACE
    CLASS="Case"
    NAME="ACTION"
    OPERATION="createCaseNominee1"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="pageDescription"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseID"
    />
  </CONNECT>


  <!-- BEGIN, CR00463142, EC -->
  <CLUSTER
    DESCRIPTION="Cluster.Description.CaseParticipantDetails"
    LABEL_WIDTH="45"
    NUM_COLS="2"
    SUMMARY="Summary.CaseParticipant.Details"
  >
    <!-- END, CR00463142 -->


    <FIELD
      LABEL="Field.Label.CaseParticipant"
      USE_BLANK="TRUE"
    >
      <CONNECT>
        <INITIAL
          HIDDEN_PROPERTY="participantRoleID"
          NAME="DISPLAY"
          PROPERTY="nameAndAgeOpt"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$caseParticipantRoleID"
        />
      </CONNECT>
    </FIELD>
    <FIELD CONTROL="SKIP"/>
  </CLUSTER>


  <!-- BEGIN, CR00463142, EC -->
  <CLUSTER
    DESCRIPTION="Cluster.Description.ParticipantDetails"
    LABEL_WIDTH="22.5"
    NUM_COLS="1"
    SUMMARY="Summary.Participant.Details"
  >
    <!-- END, CR00463142 -->
    <CONTAINER LABEL="Field.Label.Participant">
      <FIELD WIDTH="30">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="concernRoleType"
          />
        </CONNECT>
      </FIELD>
      <FIELD WIDTH="45">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="details$concernRoleID"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>
  </CLUSTER>


  <!-- BEGIN, CR00463142, EC -->
  <CLUSTER
    DESCRIPTION="Cluster.Description.RegisterDetails"
    SUMMARY="Summary.Register.Details"
  >
    <!-- END, CR00463142 -->


    <CLUSTER STYLE="cluster-cpr-no-border">
      <CLUSTER
        LABEL_WIDTH="45"
        NUM_COLS="2"
        STYLE="cluster-cpr-no-border"
      >
        <FIELD LABEL="Field.Label.Name">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="representativeName"
            />
          </CONNECT>
        </FIELD>
      </CLUSTER>
      <CLUSTER
        LABEL_WIDTH="45"
        NUM_COLS="2"
        STYLE="cluster-cpr-no-border"
        TAB_ORDER="ROW"
      >
        <FIELD>
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="addressData"
            />
          </CONNECT>
        </FIELD>
      </CLUSTER>
    </CLUSTER>


    <!-- BEGIN, CR00463142, EC -->
    <CLUSTER
      DESCRIPTION="Cluster.Description.PhoneDetails"
      LABEL_WIDTH="45"
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
      SUMMARY="Summary.Phone.Details"
    >
      <!-- END, CR00463142 -->
      <FIELD
        LABEL="Field.Label.Type"
        USE_BLANK="true"
        USE_DEFAULT="false"
        WIDTH="70"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="phoneType"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.Area"
        WIDTH="5"
        WIDTH_UNITS="CHARS"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="phoneAreaCode"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.Extension"
        WIDTH="5"
        WIDTH_UNITS="CHARS"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="phoneExtension"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.Country"
        WIDTH="5"
        WIDTH_UNITS="CHARS"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="phoneCountryCode"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.Number"
        WIDTH="10"
        WIDTH_UNITS="CHARS"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="phoneNumber"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


    <!-- BEGIN, CR00463142, EC -->
    <CLUSTER
      DESCRIPTION="Cluster.Description.BankDetails"
      LABEL_WIDTH="45"
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
      SUMMARY="Summary.Bank.Details"
    >
      <!-- END, CR00463142 -->


      <!-- BEGIN, CR00372248, GA -->
      <CONDITION>
        <IS_FALSE
          NAME="DISPLAY1"
          PROPERTY="ibanInd"
        />
      </CONDITION>
      <!-- END, CR00372248 -->
      <FIELD LABEL="Field.Label.AccountName">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="bankAccountName"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.AccountType"
        USE_BLANK="true"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="bankAccountType"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.Joint">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="jointAccountInd"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.AccountNumber">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="bankAccountNumber"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.BankBranchName">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="details$bankSortCode"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


    <!-- BEGIN, CR00372248, GA -->
    <CLUSTER
      DESCRIPTION="Cluster.Description.BankDetails"
      LABEL_WIDTH="45"
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
    >
      <CONDITION>
        <IS_TRUE
          NAME="DISPLAY1"
          PROPERTY="ibanInd"
        />
      </CONDITION>
      <FIELD LABEL="Field.Label.AccountName">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="bankAccountName"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.IBAN"
        USE_BLANK="true"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="ibanOpt"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.AccountNumber">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="bankAccountNumber"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.Joint">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="jointAccountInd"
          />
        </CONNECT>
      </FIELD>
      <FIELD
        LABEL="Field.Label.AccountType"
        USE_BLANK="true"
        USE_DEFAULT="false"
      >
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="bankAccountType"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.BIC">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="bicOpt"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.BankBranchName">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="details$bankSortCode"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
    <!-- END, CR00372248 -->


    <!-- BEGIN, CR00463142, EC -->
    <CLUSTER
      DESCRIPTION="Cluster.Description.DeliveryDetails"
      LABEL_WIDTH="45"
      NUM_COLS="2"
      STYLE="cluster-cpr-no-border"
      SUMMARY="Summary.Delivery.Details"
    >
      <!-- END, CR00463142 -->


      <FIELD LABEL="Field.Label.Currency">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="caseNomineeDtls$currencyType"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.Relationship">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="nomineeRelationship"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.PreferredOffice">
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="publicOfficeID"
          />
        </CONNECT>
      </FIELD>


    </CLUSTER>


    <CLUSTER
      LABEL_WIDTH="22.5"
      STYLE="cluster-cpr-no-border"
    >
      <FIELD
        LABEL="Field.Label.DeliveryPattern"
        USE_BLANK="true"
        WIDTH="70"
      >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="productDeliveryPatternID"
            NAME="DISPLAY"
            PROPERTY="productDeliveryPatternName"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="productDeliveryPatternID"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>


  </CLUSTER>


</VIEW>
