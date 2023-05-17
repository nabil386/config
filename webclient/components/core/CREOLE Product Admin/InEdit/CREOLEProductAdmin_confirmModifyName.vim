<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2010 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- in accordance with the terms of the license agreement you entered into -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Links the user to the localizable text record behind the nameID field. -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <!-- Declare the 'action' server bean -->
  <SERVER_INTERFACE
    CLASS="CREOLEProductAdmin"
    NAME="ACTION"
    OPERATION="confirmModifyName"
    PHASE="ACTION"
  />


  <!-- Page parameters to this page -->
  <PAGE_PARAMETER NAME="nameID"/>
  <PAGE_PARAMETER NAME="sandboxID"/>
  <PAGE_PARAMETER NAME="sandboxVersionNo"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="nameID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="localizableTextKey$localizableTextID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="sandboxID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="sandboxKey$sandboxID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="sandboxVersionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="sandboxKey$sandboxVersionNo"
    />
  </CONNECT>


  <ACTION_SET
    ALIGNMENT="CENTER"
    TOP="false"
  >


    <ACTION_CONTROL
      IMAGE="YesButton"
      LABEL="ActionControl.Label.Yes"
      TYPE="SUBMIT"
    >
      <LINK
        DISMISS_MODAL="false"
        PAGE_ID="CREOLEProductAdmin_viewName"
        SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="result$localizableTextID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="localizableTextID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      IMAGE="NoButton"
      LABEL="ActionControl.Label.No"
    />


  </ACTION_SET>


</VIEW>
