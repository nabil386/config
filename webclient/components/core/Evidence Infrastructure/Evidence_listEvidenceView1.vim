<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2011, 2013. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2011 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!--  Infrastructure pages used for validating evidence changes             -->
<VIEW
  PAGE_ID="Evidence_listEvidenceView1"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_PARAMETER NAME="caseID"/>


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


  <LIST
    PAGINATED="false"
  >


    <CONDITION>
      <IS_FALSE
        NAME="DISPLAY"
        PROPERTY="filterByTypeInd"
      />
    </CONDITION>


    <CONTAINER
      ALIGNMENT="LEFT"
      WIDTH="3"
    >


      <WIDGET TYPE="MULTISELECT">


        <WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtls$evidenceDescriptorID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtls$evidenceID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtls$evidenceType"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtls$correctionSetID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtls$successionID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtls$newOrUpdatedInd"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
        <WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="changeList"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
      </WIDGET>


    </CONTAINER>


	<!-- BEGIN, CR00332326, SSK -->
    <FIELD
      LABEL="List.Title.EvidenceType"
      WIDTH="20"
    >
    <!-- END, CR00332326 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$evidenceType"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="List.Title.Participant"
      WIDTH="17"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$concernRoleName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="List.Title.Description"
      WIDTH="22"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$summary"
        />
      </CONNECT>
    </FIELD>
	
	<!-- BEGIN, CR00332326, SSK -->
    <FIELD
      LABEL="List.Title.Period"
      WIDTH="15"
    >
    <!-- END, CR00332326 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$period"
        />
      </CONNECT>
    </FIELD>


	<!-- BEGIN, CR00332326, SSK -->
    <FIELD
      LABEL="List.Title.LatestActivity"
      WIDTH="25"
    >
    <!-- END, CR00332326 -->
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$latestActivity"
        />
      </CONNECT>
    </FIELD>


  </LIST>


  <LIST
    PAGINATED="false"
    
  >


    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="filterByTypeInd"
      />
    </CONDITION>


    <CONTAINER
      ALIGNMENT="LEFT"
      WIDTH="3"
    >


      <WIDGET TYPE="MULTISELECT">


        <WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtls$evidenceDescriptorID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtls$evidenceID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtls$evidenceType"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtls$correctionSetID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtls$successionID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="dtls$newOrUpdatedInd"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
        <WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="changeList"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
      </WIDGET>


    </CONTAINER>


    <FIELD
      LABEL="List.Title.Participant"
      WIDTH="14"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$concernRoleName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="List.Title.Description"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$summary"
        />
      </CONNECT>
    </FIELD>

    
    <FIELD
      LABEL="List.Title.ChangeSummary"
      WIDTH="17"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$description"
        />
      </CONNECT>
    </FIELD>
    
    
    <FIELD
      LABEL="List.Title.Period"
      WIDTH="16"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$period"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="List.Title.LatestActivity"
      WIDTH="25"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dtls$latestActivity"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
