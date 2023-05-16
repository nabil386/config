<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002-2008, 2010-2011 Curam Software Ltd.                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- This page allows the user to view a list of administration roles -->
<!-- for a concern. -->
<?curam-deprecated Since Curam 6.0. Replaced by Participant_listAdministrators.vim. ?>
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


  <SHORTCUT_TITLE>
    <CONNECT>
      <SOURCE
        NAME="DISPLAY"
        PROPERTY="description"
      />
    </CONNECT>
  </SHORTCUT_TITLE>


  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="DISPLAY"
    OPERATION="listAdminRole"
  />


  <PAGE_PARAMETER NAME="concernRoleID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="concernRoleID"
    />
  </CONNECT>


  <CLUSTER
    SHOW_LABELS="false"
    STYLE="tab-renderer"
  >


    <CONDITION>
      <IS_TRUE
        NAME="DISPLAY"
        PROPERTY="statusInd"
      />
    </CONDITION>


    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="data"
        />
      </CONNECT>
    </FIELD>


    <LIST>


      <ACTION_SET BOTTOM="false">


        <ACTION_CONTROL
          IMAGE="NewButton"
          LABEL="ActionControl.Label.New"
        >
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="Participant_createAdminRole"
          >
            <CONNECT>
              <SOURCE
                NAME="PAGE"
                PROPERTY="concernRoleID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="concernRoleID"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY"
                PROPERTY="description"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="pageDescription"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>


      </ACTION_SET>


      <FIELD
        LABEL="Field.Label.Name"
        WIDTH="35"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="fullUserName"
          />
        </CONNECT>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Organization_viewUserDetails"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="userName"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="userName"
            />
          </CONNECT>
        </LINK>
      </FIELD>


      <FIELD
        LABEL="Field.Label.StartDate"
        WIDTH="25"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="startDate"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.EndDate"
        WIDTH="25"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="endDate"
          />
        </CONNECT>
      </FIELD>


      <FIELD
        LABEL="Field.Label.Status"
        WIDTH="15"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="statusCode"
          />
        </CONNECT>
      </FIELD>


    </LIST>


  </CLUSTER>


</VIEW>
