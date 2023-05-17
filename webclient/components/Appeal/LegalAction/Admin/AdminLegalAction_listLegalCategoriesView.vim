<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!--
    * Copyright 2008, 2010-2011 Curam Software Ltd.
    * All rights reserved.
    *
    * This software is the confidential and proprietary information of Curam
    * Software, Ltd. ("Confidential Information"). You shall not disclose
    * such Confidential Information and shall use it only in accordance with
    * the terms of the license agreement you entered into with Curam
    * Software.
    *
    * Description
    * ===========
    * Lists all the legal categories
    *
    *
-->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file:\\Curam\CuramUIMSchema"
>


  <!-- Declare the 'display' server bean -->
  <SERVER_INTERFACE
    CLASS="AdminLegalAction"
    NAME="DISPLAY_LEGALCATEGORY"
    OPERATION="searchAdminLegalCategory"
    PHASE="DISPLAY"
  />


  <!-- BEGIN, CR00169111, SS -->
  <CLUSTER>
    <!-- END, CR00169111 -->
    <!-- List fields on this page -->
    <LIST>
      <!-- BEGIN, CR00206667, GP -->
      <ACTION_SET TYPE="LIST_ROW_MENU">
        <!-- BEGIN, CR00169111, SS -->
        <ACTION_CONTROL LABEL="ActionControl.Label.NewLegalAction">
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="AdminLegalAction_createLegalActionForCategory"
          >
            <CONNECT>
              <SOURCE
                NAME="DISPLAY_LEGALCATEGORY"
                PROPERTY="legalActionCategoryID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="legalCategoryId"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY_LEGALCATEGORY"
                PROPERTY="legalCategoryNameCode"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="legalCategoryNameCode"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>


        <ACTION_CONTROL LABEL="ActionControl.Label.AddExistingLegalAction">
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="AdminLegalAction_createExistingLegalAction"
            SAVE_LINK="true"
          >
            <CONNECT>
              <SOURCE
                NAME="DISPLAY_LEGALCATEGORY"
                PROPERTY="legalActionCategoryID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="legalCategoryId"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY_LEGALCATEGORY"
                PROPERTY="legalCategoryNameCode"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="legalCategoryNameCode"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>
        <!-- END, CR00169111 -->
        <ACTION_CONTROL LABEL="ActionControl.Label.Edit">
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="AdminLegalAction_modifyLegalCategory"
          >
            <CONNECT>
              <SOURCE
                NAME="DISPLAY_LEGALCATEGORY"
                PROPERTY="legalCategoryNameCode"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="legalCategoryNameCode"
              />
            </CONNECT>
            <CONNECT>
              <SOURCE
                NAME="DISPLAY_LEGALCATEGORY"
                PROPERTY="legalActionCategoryID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="legalCategoryId"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>


        <ACTION_CONTROL
          IMAGE="DeleteButton"
          LABEL="ActionControl.Label.Remove"
        >
          <LINK
            OPEN_MODAL="true"
            PAGE_ID="AdminLegalAction_cancelLegalCategory"
          >
            <CONNECT>
              <SOURCE
                NAME="DISPLAY_LEGALCATEGORY"
                PROPERTY="legalActionCategoryID"
              />
              <TARGET
                NAME="PAGE"
                PROPERTY="legalCategoryId"
              />
            </CONNECT>
          </LINK>
        </ACTION_CONTROL>
      </ACTION_SET>
      <!-- END, CR00206667 -->


      <FIELD LABEL="Field.Label.Name">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY_LEGALCATEGORY"
            PROPERTY="legalCategoryNameCode"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.StartDate">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY_LEGALCATEGORY"
            PROPERTY="startDate"
          />
        </CONNECT>
      </FIELD>
      <FIELD LABEL="Field.Label.Status">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY_LEGALCATEGORY"
            PROPERTY="recordStatusCode"
          />
        </CONNECT>
      </FIELD>
      <!-- BEGIN, CR00206667, GP -->
      <DETAILS_ROW>
        <INLINE_PAGE PAGE_ID="AdminLegalAction_viewLegalCategory">
          <CONNECT>
            <SOURCE
              NAME="DISPLAY_LEGALCATEGORY"
              PROPERTY="legalActionCategoryID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="legalCategoryId"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY_LEGALCATEGORY"
              PROPERTY="legalCategoryNameCode"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="legalCategoryNameCode"
            />
          </CONNECT>
        </INLINE_PAGE>
      </DETAILS_ROW>
      <!-- END, CR00206667 -->
    </LIST>
  </CLUSTER>
</VIEW>
