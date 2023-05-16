<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2008,2021. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2008, 2010 Curam Software Ltd.                               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for the modify external party office phone number    -->
<!-- pages.                                                                 -->
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
    CLASS="ExternalParty"
    NAME="DISPLAY"
    OPERATION="readOfficePhoneNumber"
  />


  <SERVER_INTERFACE
    CLASS="ExternalParty"
    NAME="ACTION"
    OPERATION="modifyOfficePhoneNumber"
    PHASE="ACTION"
  />


  <INFORMATIONAL>
    <CONNECT>
      <SOURCE
        NAME="ACTION"
        PROPERTY="informationMsgTxt"
      />
    </CONNECT>
  </INFORMATIONAL>


  <PAGE_PARAMETER NAME="officePhoneNumberID"/>
  <PAGE_PARAMETER NAME="officeName"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="officePhoneNumberID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="officePhoneNumberKey$officePhoneNumberID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="officePhoneNumberID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="officePhoneNumberID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="externalPartyOfficeID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="externalPartyOfficeID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="phoneNumberDtls$phoneNumberID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="phoneNumberDtls$phoneNumberID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="phoneNumberDtls$versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="phoneNumberDtls$versionNo"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="officePhoneNumberDtls$phoneNumberID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="officePhoneNumberDtls$phoneNumberID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="officePhoneNumberDtls$versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="officePhoneNumberDtls$versionNo"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="statusCode"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="statusCode"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="recordStatus"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="recordStatus"
    />
  </CONNECT>


  <CLUSTER LABEL_WIDTH="30">


    <CONTAINER LABEL="Field.Label.Phone">
      <FIELD WIDTH="20" LABEL="Field.Label.PhoneCountryCode">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="phoneCountryCode"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="phoneCountryCode"
          />
        </CONNECT>
      </FIELD>
      <FIELD WIDTH="25" LABEL="Field.Label.PhoneAreaCode">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="phoneAreaCode"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="phoneAreaCode"
          />
        </CONNECT>
      </FIELD>
      <FIELD WIDTH="40" LABEL="Field.Label.PhoneNumber">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="phoneNumber"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="phoneNumber"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>


    <FIELD
      LABEL="Field.Label.Extension"
      WIDTH="5"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="phoneExtension"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="phoneExtension"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.PhoneType"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="phoneType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="phoneType"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.StartDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="startDate"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD CONTROL="SKIP"/>


    <FIELD
      LABEL="Field.Label.EndDate"
      USE_DEFAULT="false"
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
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Primary">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="primaryPhoneInd"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="primaryPhoneInd"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


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
          PROPERTY="phoneNumberDtls$comments"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="phoneNumberDtls$comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
