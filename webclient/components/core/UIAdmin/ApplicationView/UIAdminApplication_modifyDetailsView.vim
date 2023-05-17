<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2011 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                    -->
<!-- This software is the confidential and proprietary information of Curam  -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose     -->
<!-- such Confidential Information and shall use it only in accordance with  -->
<!-- the terms of the license agreement you entered into with Curam          -->
<!-- Software.                                                               -->
<!-- Description -->
<!-- =========== -->
<!--   -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>
  <CLUSTER LABEL_WIDTH="18">
    <FIELD
      LABEL="Field.Label.Title"
      WIDTH="60"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="title"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="title"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.SubTitle"
      WIDTH="60"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="subTitle"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="subTitle"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Logo">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="logo"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="logo"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Message">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="message"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="message"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <ACTION_SET>
    <ACTION_CONTROL
      LABEL="Control.Label.Save"
      TYPE="SUBMIT"
    >
        </ACTION_CONTROL>
    <ACTION_CONTROL LABEL="Control.Label.Cancel">
        </ACTION_CONTROL>
  </ACTION_SET>


</VIEW>
