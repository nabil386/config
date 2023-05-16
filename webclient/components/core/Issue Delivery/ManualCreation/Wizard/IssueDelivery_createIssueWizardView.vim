<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  PID 5725-H26
  
  Copyright IBM Corporation 2011, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2011 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view for modifying  the closure details for an Issue Delivery Case.-->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CLUSTER
    NUM_COLS="2"
    LABEL_WIDTH="30"
  >
  
    <FIELD
      LABEL="Field.Label.Type"
      WIDTH="80"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAYISSUE"
          PROPERTY="result$dtls$dtls$type"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.StartDate"
      USE_DEFAULT="FALSE"
      WIDTH="50"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="startDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.Priority"
      USE_BLANK="TRUE"
      WIDTH="50"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAYISSUE"
          PROPERTY="result$dtls$dtls$priority"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="priorityCode"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Member">
      <CONNECT>
        <SOURCE
          NAME="DISPLAYMEMBER"
          PROPERTY="result$details$fullName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Label.EndDate"
      USE_DEFAULT="FALSE"
      WIDTH="50"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="endDate"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


  <LIST 
    TITLE="List.AddEvidenceList"
    PAGINATED="false"
    
  >
    <CONTAINER
      ALIGNMENT="CENTER"
      WIDTH="3"
    >
      <WIDGET TYPE="MULTISELECT">
        <WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
          <CONNECT>
            <SOURCE
              NAME="DISPLAYEVIDENCE"
              PROPERTY="result$dtls$activeList$dtls$evidenceDescriptorID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAYEVIDENCE"
              PROPERTY="result$dtls$activeList$dtls$evidenceID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAYEVIDENCE"
              PROPERTY="result$dtls$activeList$dtls$evidenceType"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAYEVIDENCE"
              PROPERTY="result$dtls$activeList$dtls$correctionSetID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAYEVIDENCE"
              PROPERTY="result$dtls$activeList$dtls$successionID"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
        <WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="details$dtls$evidenceList$addEvidenceList"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
      </WIDGET>
    </CONTAINER>
    
    <FIELD
      LABEL="List.Title.Name"
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAYEVIDENCE"
          PROPERTY="result$dtls$activeList$dtls$concernRoleName"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD
      LABEL="List.Title.EffectiveDate"
      WIDTH="19"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAYEVIDENCE"
          PROPERTY="result$dtls$activeList$dtls$effectiveFrom"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD
      LABEL="List.Title.Details"
      WIDTH="35"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAYEVIDENCE"
          PROPERTY="result$dtls$activeList$dtls$summary"
        />
      </CONNECT>
    </FIELD>
    
    <FIELD
      LABEL="List.Title.Status"
      WIDTH="13"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAYEVIDENCE"
          PROPERTY="result$dtls$activeList$dtls$statusCode"
        />
      </CONNECT>
    </FIELD>
  </LIST>


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
          PROPERTY="details$dtls$dtls$comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>

</VIEW>
