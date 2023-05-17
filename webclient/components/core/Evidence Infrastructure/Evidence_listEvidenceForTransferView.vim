<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2007-2008, 2010 Curam Software Ltd.                          -->
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
<!-- BEGIN, CR00216737, GYH -->
<?curam-deprecated Since Curam 6.0, replaced with 
  Evidence_listEvidenceTransferView. As part of evidence workspace changes, now 
  the transfer evidence has been changed from two stage process to one screen 
  with confirmation screen ask user confirmation evidence transfer. 
  See Release note: CR00216737
?>
<!-- END, CR00216737 -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <LIST
    DESCRIPTION="Cluster.Evidence.Description"
    TITLE="Cluster.Evidence.Title"
  >
    <CONTAINER
      ALIGNMENT="CENTER"
      LABEL="List.Title.Select"
      WIDTH="6"
    >
      <WIDGET TYPE="MULTISELECT">
        <WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="activeList$dtls$evidenceDescriptorID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="activeList$dtls$evidenceID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="activeList$dtls$evidenceType"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="activeList$dtls$correctionSetID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="activeList$dtls$successionID"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
        <WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="evidenceTransferList"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
      </WIDGET>
    </CONTAINER>
    <FIELD
      LABEL="List.Title.Type"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="activeList$dtls$evidenceType"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.StartDate"
      WIDTH="11"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="activeList$dtls$startDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.EndDate"
      WIDTH="11"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="activeList$dtls$endDate"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.EffectiveDate"
      WIDTH="11"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="activeList$dtls$effectiveFrom"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="List.Title.Details"
      WIDTH="29"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="activeList$dtls$summary"
        />
      </CONNECT>
    </FIELD>
  </LIST>
</VIEW>
