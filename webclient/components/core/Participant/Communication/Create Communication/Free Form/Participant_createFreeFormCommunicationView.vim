<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
 
  Copyright IBM Corporation 2002, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002-2003, 2010-2011 Curam Software Ltd.                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for the record communication details.                -->
<?curam-deprecated Since Curam 6.0. Replaced by Participant_createMSWordCommunicationView.vim. ?>
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
    CLASS="Participant"
    NAME="ACTION"
    OPERATION="recordExistingCommunication"
    PHASE="ACTION"
  />


  <ACTION_SET ALIGNMENT="CENTER">
    <ACTION_CONTROL
      IMAGE="SaveButton"
      LABEL="ActionControl.Label.Save"
      TYPE="SUBMIT"
    >
    </ACTION_CONTROL>
    <ACTION_CONTROL
      IMAGE="CancelButton"
      LABEL="ActionControl.Label.Cancel"
    >
    </ACTION_CONTROL>
  </ACTION_SET>


  <PAGE_PARAMETER NAME="pageDescription"/>


  <CLUSTER
    NUM_COLS="2"
    TITLE="Cluster.Title.Correspondent"
  >


    <FIELD LABEL="Field.Label.ClientAsCorrespondent">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="clientIsCorrespondentInd"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.CorrespondentType">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="correspondentTypeCode"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    DESCRIPTION="Cluster.CorrespondentContact.Description"
    NUM_COLS="2"
  >


    <FIELD LABEL="Field.Label.CorrespondentName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="correspondentName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.EmailAddress">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="emailAddress"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER NUM_COLS="2">


    <FIELD>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="addressData"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER NUM_COLS="2">


    <FIELD
      LABEL="Field.Label.CountryCode"
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


    <FIELD
      LABEL="Field.Label.AreaCode"
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
          PROPERTY="phoneExtensionCode"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >


    <!-- BEGIN, CR00406866, VT -->
    <FIELD HEIGHT="4" LABEL="Field.Label.Comments">
      <!-- END, CR00406866 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="comments"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <!--
  <CLUSTER>

    <WIDGET TYPE="FILE_EDIT">

      <WIDGET_PARAMETER NAME="CONTENT">
        <CONNECT>
          <TARGET NAME="ACTION" PROPERTY="newAttachmentContents"/>
        </CONNECT>
      </WIDGET_PARAMETER>
      
      <WIDGET_PARAMETER NAME="FILE_NAME">
        <CONNECT>
          <TARGET NAME="ACTION" PROPERTY="newAttachmentName"/>
        </CONNECT>
      </WIDGET_PARAMETER>
      
      <WIDGET_PARAMETER NAME="OPEN_BUTTON_CAPTION">
        <CONNECT>
          <SOURCE NAME="TEXT" PROPERTY="ActionControl.Label.Open"/>
        </CONNECT>
      </WIDGET_PARAMETER>
      
      <WIDGET_PARAMETER NAME="SAVE_BUTTON_CAPTION">
        <CONNECT>
          <SOURCE NAME="TEXT" PROPERTY="ActionControl.Label.Save"/>
        </CONNECT>
      </WIDGET_PARAMETER>

    </WIDGET>

  </CLUSTER>
-->


</VIEW>