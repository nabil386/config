<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010-2012 Curam Software Ltd.                                -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Evidence infrastructure view page to be included in the client page    -->
<!-- for listing In Edit case evidence                                      -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_PARAMETER NAME="successionID"/>


  <SERVER_INTERFACE
    CLASS="Evidence"
    NAME="DISPLAY"
    OPERATION="listInEditEvdInstanceChanges"
  />


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="successionID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$successionID"
    />
  </CONNECT>


  <LIST>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Evidence_resolveViewEvidencePage">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="dtlsList$evidenceType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceType"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="dtlsList$evidenceID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="evidenceDescriptorID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="evidenceDescriptorID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>
  </LIST>
</VIEW>
