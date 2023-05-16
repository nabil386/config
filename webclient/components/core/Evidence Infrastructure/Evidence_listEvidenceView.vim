<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2005-2008,2010 Curam Software Ltd.                       -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Evidence infrastructure page containing a list of evidence records     -->
<!-- which are active, in edit and pending removal                          -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <LIST TITLE="List.NewAndUpdated">


    <!-- BEGIN, CR00050298, MR -->
    <!-- BEGIN, HARP 64908, SP -->
    <CONTAINER
      ALIGNMENT="CENTER"
      WIDTH="7"
    >


      <WIDGET TYPE="MULTISELECT">
        <!-- END, HARP 64908 -->
        <!-- END, CR00050298 -->


        <WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="newAndUpdateList$dtls$evidenceDescriptorID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="newAndUpdateList$dtls$evidenceID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="newAndUpdateList$dtls$evidenceType"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="newAndUpdateList$dtls$correctionSetID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="newAndUpdateList$dtls$successionID"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
        <WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="newAndUpdateList"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
      </WIDGET>
    </CONTAINER>


    <FIELD
      LABEL="List.Title.Type"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="newAndUpdateList$dtls$evidenceType"
        />
      </CONNECT>
      <!--
      <LINK PAGE_ID="Sample_listSportingActivity">
        <CONNECT>
          <SOURCE NAME="PAGE" PROPERTY="caseID" />
          <TARGET NAME="PAGE" PROPERTY="caseID" />
        </CONNECT>
      </LINK>
      -->
    </FIELD>
    <FIELD
      LABEL="List.Title.Name"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="newAndUpdateList$dtls$concernRoleName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.EffectiveDate"
      WIDTH="17"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="newAndUpdateList$dtls$effectiveFrom"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="List.Title.Details"
      WIDTH="36"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="newAndUpdateList$dtls$summary"
        />
      </CONNECT>
    </FIELD>
  </LIST>


  <LIST TITLE="List.PendingRemovals">


    <CONTAINER
      ALIGNMENT="CENTER"
      LABEL="List.Title.Select"
      WIDTH="7"
    >


      <WIDGET TYPE="MULTISELECT">


        <WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="removeList$dtls$evidenceDescriptorID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="removeList$dtls$evidenceID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="removeList$dtls$evidenceType"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="removeList$dtls$correctionSetID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="removeList$dtls$successionID"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
        <WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="removeList"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
      </WIDGET>
    </CONTAINER>


    <FIELD
      LABEL="List.Title.Type"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="removeList$dtls$evidenceType"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.Name"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="removeList$dtls$concernRoleName"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.EffectiveDate"
      WIDTH="17"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="removeList$dtls$effectiveFrom"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="List.Title.Details"
      WIDTH="36"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="removeList$dtls$summary"
        />
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>
