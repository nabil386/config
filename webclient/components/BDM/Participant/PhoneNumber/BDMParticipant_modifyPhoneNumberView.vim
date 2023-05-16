<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
 
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- The included view for the modify phone number pages.                   -->
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
    CLASS="BDMParticipant"
    NAME="DISPLAY"
    OPERATION="readPhoneNumber"
  />


  <SERVER_INTERFACE
    CLASS="BDMParticipant"
    NAME="ACTION"
    OPERATION="modifyPhoneNumber"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="concernRolePhoneNumberID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRolePhoneNumberID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="readConcernRolePhoneKey$concernRolePhoneNumberID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="readConcernRolePhoneKey$concernRolePhoneNumberID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="concernRolePhoneNumberID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
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
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="concernRoleID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="30"
    NUM_COLS="2"
  >
  	<FIELD
        LABEL="Field.Label.PhoneCountryCode"
        WIDTH="15"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="phoneCountryCode"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="details$phoneCountryCode"
          />
        </CONNECT>
      </FIELD>
    <CONTAINER LABEL="Field.Label.Phone">
      
      <FIELD
        LABEL="Field.Label.PhoneAreaCode"
        WIDTH="20"
      >
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

      <FIELD
        LABEL="Field.Label.PhoneNumber"
        WIDTH="8"
        WIDTH_UNITS="CHARS"
      >
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
      WIDTH="24"
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
      LABEL="Field.Label.Type"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="typeCode"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="typeCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.From">
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


    <FIELD LABEL="Field.Label.To">
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
    <FIELD
      HEIGHT="4"
      LABEL="Field.Label.Comments"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="comments"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
