<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2004, 2013, 2015. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2004, 2010-2011 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This process allows the user to modify bank branch details.            -->
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
    CLASS="Organization"
    NAME="DISPLAY"
    OPERATION="readBankBranch"
    PHASE="DISPLAY"
  />


  <SERVER_INTERFACE
    CLASS="Organization"
    NAME="ACTION"
    OPERATION="modifyBankBranch"
    PHASE="ACTION"
  />


  <!-- BEGIN, CR00371769, VT -->
  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="IBANINDICATOR"
    OPERATION="readIban"
  />
  <!-- END, CR00371769 -->


  <PAGE_PARAMETER NAME="bankBranchID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="bankBranchID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$bankBranchKeyStruct$bankBranchID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="bankBranchID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$bankBranchStruct$bankBranchID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="bankID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$bankBranchStruct$bankID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="bankName"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$bankBranchStruct$bankName"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$bankBranchStruct$versionNo"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="statusCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$bankBranchStruct$statusCode"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="addressID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$bankBranchStruct$addressID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="emailAddress"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$bankBranchStruct$emailAddress"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="emailAddressID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$bankBranchStruct$emailAddressID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="faxNumberID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$bankBranchStruct$faxNumberID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="phoneNumberID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$bankBranchStruct$phoneNumberID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="emailVersionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$bankBranchStruct$emailVersionNo"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="faxVersionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$bankBranchStruct$faxVersionNo"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="phoneVersionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$bankBranchStruct$phoneVersionNo"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="faxExtension"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$bankBranchStruct$faxExtension"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="addressVersionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$bankBranchStruct$addressVersionNo"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="formattedAddressData"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$bankBranchStruct$formattedAddressData"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="bankBranchStatus"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$bankBranchStruct$bankBranchStatus"
    />
  </CONNECT>


  <!-- BEGIN, CR00280054, PB -->
  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    SHOW_LABELS="true"
  >
    <!-- END, CR00280054 -->
    <!-- BEGIN, CR00371769, VT -->
    <CONDITION>
      <IS_FALSE
        NAME="IBANINDICATOR"
        PROPERTY="ibanInd"
      />
    </CONDITION>
    <!-- END, CR00371769 -->
    <FIELD LABEL="Field.Label.BankName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bankName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.StartDate"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$bankBranchStruct$startDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.SortCode"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bankSortCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$bankBranchStruct$bankSortCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.BranchName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$bankBranchStruct$name"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.EndDate"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$bankBranchStruct$endDate"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <!-- BEGIN, CR00280054, PB -->


  <!-- BEGIN, CR00371769, VT -->
  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    SHOW_LABELS="true"
  >


    <CONDITION>
      <IS_TRUE
        NAME="IBANINDICATOR"
        PROPERTY="ibanInd"
      />
    </CONDITION>


    <FIELD LABEL="Field.Label.BankName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bankName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.StartDate"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$bankBranchStruct$startDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.SortCode"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bankSortCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$bankBranchStruct$bankSortCode"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.BranchName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="name"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$bankBranchStruct$name"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.EndDate"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="endDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$bankBranchStruct$endDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Bic">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$bankBranchStruct$bicOpt"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$bankBranchStruct$bicOpt"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>
  <!-- END, CR00371769 -->


  <CLUSTER
    LABEL_WIDTH="50"
    NUM_COLS="2"
    TAB_ORDER="ROW"
    TITLE="Cluster.Title.Address"
  >
    <!-- END, CR00280054 -->
    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="addressData"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$bankBranchStruct$addressData"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <!-- BEGIN, CR00280054, PB -->
  <CLUSTER
    LABEL_WIDTH="26.5"
    NUM_COLS="1"
    SHOW_LABELS="true"
    TITLE="Cluster.Title.Contact"
  >
    <CONTAINER LABEL="Container.Label.PhoneNumber">
      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.PhoneCountryCode"
        WIDTH="3"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="phoneCountryCode"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="details$bankBranchStruct$phoneCountryCode"
          />
        </CONNECT>
      </FIELD>
      <!-- BEGIN, CR00294489, KRK -->
      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.PhoneAreaCode"
        WIDTH="3"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <!-- END, CR00294489 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="phoneAreaCode"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="details$bankBranchStruct$phoneAreaCode"
          />
        </CONNECT>
      </FIELD>
      <!-- BEGIN, CR00294489, KRK -->
      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.PhoneNumber"
        WIDTH="7"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <!-- END, CR00294489 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="phoneNumber"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="details$bankBranchStruct$phoneNumber"
          />
        </CONNECT>
      </FIELD>
      <!-- BEGIN, CR00294489, KRK -->
      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.PhoneExtension"
        WIDTH="3"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <!-- END, CR00294489 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="phoneExtension"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="details$bankBranchStruct$phoneExtension"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>
    <CONTAINER LABEL="Container.Label.Fax">
      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.FaxCountryCode"
        WIDTH="3"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="faxCountryCode"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="details$bankBranchStruct$faxCountryCode"
          />
        </CONNECT>
      </FIELD>
      <!-- BEGIN, CR00294489 -->
      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.FaxAreaCode"
        WIDTH="3"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <!-- END, CR00294489 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="faxAreaCode"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="details$bankBranchStruct$faxAreaCode"
          />
        </CONNECT>
      </FIELD>
      <!-- BEGIN, CR00294489 -->
      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.FaxNumber"
        WIDTH="7"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <!-- END, CR00294489 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="faxNumber"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="details$bankBranchStruct$faxNumber"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>
    <FIELD
      LABEL="Field.Label.Email"
      WIDTH="49"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="emailAddress"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$bankBranchStruct$emailAddress"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <!-- END, CR00280054 -->
  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >
    <!-- BEGIN, CR00406866, VT -->
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <!-- END, CR00406866 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$bankBranchStruct$comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
</VIEW>
