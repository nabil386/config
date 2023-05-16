<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002-2003 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This is the included view for the confirm selected client page.        -->
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
    CLASS="Screening"
    NAME="DISPLAY"
    OPERATION="viewClientDetails"
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


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.PersonalDetails"
  >


    <FIELD LABEL="Field.Label.ReferenceNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="primaryReferenceNo"
        />
      </CONNECT>
      <LINK PAGE_ID="Person_resolveHomePage">
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
    </FIELD>


    <FIELD LABEL="Field.Label.Title">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="title"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.FistName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="firstname"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.MiddleName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="middleName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.LastName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="surname"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Language">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="preferredLanguage"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.DateOfBirth">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dateOfBirth"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.MaritalStatus">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="maritalStatus"
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


    <FIELD LABEL="Field.Label.EthnicOrigin">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="ethnicOrigin"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    DESCRIPTION="Cluster.Description.Address"
    NUM_COLS="2"
    TITLE="Cluster.Title.Address"
  >


    <FIELD LABEL="Field.Label.PrimaryResidence">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="primaryAddressData"
        />
      </CONNECT>
    </FIELD>


    <CONTAINER>
      <ACTION_CONTROL LABEL="ActionControl.Label.NewPrimaryResidence">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Participant_createPrimaryResidence"
        >
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
              PROPERTY="firstname"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="firstName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="surname"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="lastName"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


    <FIELD LABEL="Field.Label.MailingAddress">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="mailingAddressData"
        />
      </CONNECT>
    </FIELD>


    <CONTAINER>
      <ACTION_CONTROL LABEL="ActionControl.Label.NewMailingAddress">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Participant_createMailingAddress"
        >
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
              PROPERTY="mailingAddressID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="mailingAddressID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="firstname"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="firstName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="surname"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="lastName"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


  </CLUSTER>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.PhoneNumbers"
  >


    <CONTAINER LABEL="Field.Label.HomePhone">
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="homePhoneNumber"
          />
        </CONNECT>
      </FIELD>
      <ACTION_CONTROL LABEL="ActionControl.Label.New">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Participant_createHomePhoneNumber"
        >
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
              PROPERTY="firstname"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="firstName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="surname"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="lastName"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


    <CONTAINER LABEL="Field.Label.WorkPhone">
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="workPhoneNumber"
          />
        </CONNECT>
      </FIELD>
      <ACTION_CONTROL LABEL="ActionControl.Label.New">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Participant_createWorkPhoneNumber"
        >
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
              PROPERTY="workPhoneNumberID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="workPhoneNumberID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="firstname"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="firstName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="surname"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="lastName"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


  </CLUSTER>


</VIEW>
