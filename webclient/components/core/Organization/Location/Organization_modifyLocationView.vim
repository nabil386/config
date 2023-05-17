<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
 
  Copyright IBM Corporation 2004, 2013, 2015. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2004, 2009, 2010 Curam Software Ltd.                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view for the modify location pages. -->
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
    OPERATION="readLocation"
  />


  <SERVER_INTERFACE
    CLASS="Organization"
    NAME="ACTION"
    OPERATION="modifyLocation"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="locationID"/>
  <PAGE_PARAMETER NAME="locationStructureID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="locationID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="locationKeyRef$locationID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="locationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="locationDetails$locationID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="locationStructureID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="locationStructureID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="organisationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="organisationID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="emailAddressID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="emailAddressID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="faxNumberID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="faxNumberID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="phoneNumberID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="phoneNumberID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="addressID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="addressID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="phoneVersionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="phoneVersionNo"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="faxVersionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="faxVersionNo"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="emailVersionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="emailVersionNo"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="locationVersionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="locationVersionNo"
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
      PROPERTY="result$locationDetails$locationStatus"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="locationStatus"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="creationDate"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="creationDate"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="parentLocationID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="parentLocationID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="rootLocationIndicator"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="rootLocationIndicator"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="28"
    NUM_COLS="1"
  >
    <FIELD
      LABEL="Field.Label.Name"
      WIDTH="60"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="locationName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="locationName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Type"
      USE_BLANK="true"
      USE_DEFAULT="false"
      WIDTH="60"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="locationType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="locationType"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <!-- BEGIN, CR00103741, CL -->
  <CLUSTER
    LABEL_WIDTH="56"
    NUM_COLS="2"
  >
    <!-- END, CR00103741, -->
    <FIELD LABEL="Field.Label.ReadSID">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="readSID"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="readSID"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="readSID"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.CreateSID">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="createLocationSID"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="createLocationSID"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="createLocationSID"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.PublicOffice">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="publicOfficeIndicator"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="publicOfficeIndicator"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.MaintainSID">
      <CONNECT>
        <INITIAL
          NAME="DISPLAY"
          PROPERTY="maintainSID"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="maintainSID"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="maintainSID"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <!-- BEGIN, CR00103741, CL -->
  <CLUSTER
    LABEL_WIDTH="56"
    NUM_COLS="2"
    TAB_ORDER="ROW"
    TITLE="Cluster.Title.Address"
  >
    <!-- END, CR00103741 -->
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
          PROPERTY="addressData"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <!-- BEGIN, CR00103741, CL -->
  <CLUSTER
    LABEL_WIDTH="28"
    NUM_COLS="1"
    TITLE="Cluster.Title.ContactDetails"
  >
    <!-- END, CR00103741 -->


    <CONTAINER LABEL="Field.Label.Phone">
      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.PhoneCountryCode1"
        WIDTH="5"
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
            PROPERTY="phoneCountryCode"
          />
        </CONNECT>
      </FIELD>


      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.PhoneAreaCode1"
        WIDTH="5"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
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


      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.PhoneNumber1"
        WIDTH="10"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
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


    <CONTAINER LABEL="Field.Label.Fax">


      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.FaxCountryCode1"
        WIDTH="5"
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
            PROPERTY="faxCountryCode"
          />
        </CONNECT>
      </FIELD>


      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.FaxAreaCode1"
        WIDTH="5"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="faxAreaCode"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="faxAreaCode"
          />
        </CONNECT>
      </FIELD>


      <!-- BEGIN, CR00463142, EC -->
      <FIELD
        LABEL="Field.Label.FaxNumber1"
        WIDTH="10"
        WIDTH_UNITS="CHARS"
      >
        <!-- END, CR00463142 -->
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="faxNumber"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="faxNumber"
          />
        </CONNECT>
      </FIELD>
    </CONTAINER>


    <FIELD
      LABEL="Field.Label.Email"
      WIDTH="60"
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
          PROPERTY="emailAddress"
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
          PROPERTY="locationDescription"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="locationDescription"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
