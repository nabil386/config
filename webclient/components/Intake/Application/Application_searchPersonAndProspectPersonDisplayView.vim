<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010-2011 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description 							                                              -->
<!-- =========== 							                                              -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <LIST
    
    TITLE="List.Title.SearchResults"
    WIDTH="95"
  >


    <CONTAINER WIDTH="4">
      <WIDGET TYPE="SINGLESELECT">
        <WIDGET_PARAMETER NAME="SELECT_SOURCE">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="concernRoleID"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
        <WIDGET_PARAMETER NAME="SELECT_TARGET">
          <CONNECT>
            <TARGET
              NAME="SELECTDISPLAY"
              PROPERTY="concernRoleID"
            />
          </CONNECT>
        </WIDGET_PARAMETER>
      </WIDGET>
    </CONTAINER>


    <!-- BEGIN, CR00270437, KG -->
    <FIELD
      LABEL="Field.Title.Name"
      WIDTH="22"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$name"
        />
      </CONNECT>


    </FIELD>


    <FIELD
      LABEL="Field.Title.Address"
      WIDTH="44"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$address"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.Gender"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$gender"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.DateOfBirth"
      WIDTH="15"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="result$dtls$dateOfBirth"
        />
      </CONNECT>
    </FIELD>
    <!-- END, CR00270437 -->


  </LIST>


</VIEW>
