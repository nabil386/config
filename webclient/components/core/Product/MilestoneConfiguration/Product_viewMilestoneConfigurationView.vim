<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2007, 2009-2010 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows a user to view details of an existing milestone       -->
<!-- configuration                                                          -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CLUSTER
    LABEL_WIDTH="60"
    NUM_COLS="2"
  >


    <FIELD LABEL="Field.Label.EarliestStartDay">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="earliestStartDay"
        />
      </CONNECT>
    </FIELD>
    <!-- BEGIN, CR00138778, MC-->


    <FIELD
      LABEL="Field.Label.WaiverAllowed"
      USE_DEFAULT="false"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="waiverAllowed"
        />
      </CONNECT>


    </FIELD>


    <!--END, CR00138778-->


    <FIELD LABEL="Field.Label.Duration">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="duration"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.CreationDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="creationDate"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00138778, MC-->


    <FIELD LABEL="Field.Label.WaiverRequired">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="waiverRequired"
        />
      </CONNECT>


    </FIELD>


    <!--END, CR00138778-->


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="60"
    NUM_COLS="2"
    TITLE="Cluster.Title.EventDetails"
  >


    <FIELD LABEL="Field.Label.AddedEvent">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="addedEvent"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ExpctdEndDateEvent">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="expctdEndDateEvent"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ExpctdStDateEvent">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="expctdStDateEvent"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.CompleteEvent">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="completeEvent"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >


    <!-- BEGIN, CR00234817, GP -->
    <CONTAINER>
      <!-- END, CR00234817 -->
      <FIELD HEIGHT="3">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="comments"
          />
        </CONNECT>
      </FIELD>
      <!-- BEGIN, CR00234817, GP -->
      <ACTION_CONTROL
        IMAGE="LocalizableTextTranslation"
        LABEL="ActionControl.Label.TextTranslation"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Product_listLocalizableMilestoneCommentsText"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="commentsTextID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="localizableTextID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtls$milestoneConfigurationID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="milestoneConfigurationID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="versionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="versionNo"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>
    <!-- END, CR00234817  -->


  </CLUSTER>
</VIEW>
