<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2003-2004, 2007-2008, 2010 Curam Software Ltd.               -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Sample Integrated case member home page.                               -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <PAGE_PARAMETER NAME="caseParticipantRoleID"/>
  <PAGE_PARAMETER NAME="caseID"/>
  <SERVER_INTERFACE
    CLASS="DAAssessment"
    NAME="DISPLAY"
    OPERATION="viewCaseMemberDetails"
    PHASE="DISPLAY"
  />
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseParticipantRoleID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseParticipantRoleID"
    />
  </CONNECT>
  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >
    <CONTAINER
      LABEL="Field.Label.FullName"
      WIDTH="20"
    >
      <FIELD
        LABEL="Field.Label.FullName"
        WIDTH="20"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="fullName"
          />
        </CONNECT>
      </FIELD>
      <ACTION_CONTROL LABEL="ActionControl.Label.Homepage">
        <!-- BEGIN, CR00184681, AK -->
        <LINK PAGE_ID="Participant_resolveRoleHome">
          <!-- END, CR00184681 -->
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$details$concernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="concernRoleType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="participantType"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>
    <FIELD LABEL="Field.Label.DateOfBirth">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateOfBirth"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.PreferredLanguage">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="preferredLanguage"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.RegistrationDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="registrationDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.OtherName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="otherForename"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Gender">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="gender"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.PlaceOfBirth">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="placeOfBirth"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Nationality">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="nationalityCode"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>
  <CLUSTER
    NUM_COLS="2"
    SHOW_LABELS="false"
    STYLE="outer-cluster-borderless"
  >
    <CLUSTER
      SHOW_LABELS="false"
      TITLE="Cluster.Title.Address"
    >
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="formattedAddressData"
          />
        </CONNECT>
      </FIELD>
    </CLUSTER>
    <CLUSTER
      SHOW_LABELS="false"
      TITLE="Cluster.Title.Phone"
    >
      <CONTAINER>
        <FIELD>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="phoneCountryCode"
            />
          </CONNECT>
        </FIELD>
        <FIELD>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="phoneAreaCode"
            />
          </CONNECT>
        </FIELD>
        <FIELD>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="phoneNumber"
            />
          </CONNECT>
        </FIELD>
        <FIELD>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="phoneExtension"
            />
          </CONNECT>
        </FIELD>
      </CONTAINER>
    </CLUSTER>
  </CLUSTER>
  <LIST TITLE="List.Title.Products">
    <FIELD LABEL="Field.Label.CaseReference">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$productDetails$caseReference"
        />
      </CONNECT>
      <LINK PAGE_ID="Case_resolveCaseHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$productDetails$caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <FIELD LABEL="Field.Label.ProductType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="productType"
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
    </FIELD>
    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$productDetails$statusCode"
        />
      </CONNECT>
    </FIELD>
  </LIST>
</VIEW>
