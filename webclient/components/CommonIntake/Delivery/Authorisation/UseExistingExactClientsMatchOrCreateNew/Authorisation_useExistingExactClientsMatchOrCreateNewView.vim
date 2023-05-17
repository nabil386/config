<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM

  Copyright IBM Corporation 2012-2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Description                                                        -->
<!-- =================================================================  -->
<!-- This page is used for Program Authorization for the strategy       -->
<!-- UseExistingAnyClientsMatchOrCreateNew. The page has a conditional  -->
<!-- cluster which determines if the list of cases is displayed or not. -->
<!-- If there are no matching clients on the application case, then the -->
<!-- list of cases will not be displayed.                               -->

<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">

  <ACTION_SET
    ALIGNMENT="RIGHT"
    TOP="false"
    >
    <ACTION_CONTROL
      IMAGE="SaveButton"
      LABEL="ActionControl.Label.Save"
      TYPE="SUBMIT"
    />
    <ACTION_CONTROL
      IMAGE="CancelButton"
      LABEL="ActionControl.Label.Cancel"
    />
  </ACTION_SET>

  <CLUSTER TITLE="List.Label.Title" LABEL_WIDTH="35" DESCRIPTION="Cluster.Description.ICCreateNew">

    <FIELD LABEL="Field.Label.CreateNewIntegratedCase" >
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="createNewInd"/>
      </CONNECT>
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="createNewInd" />
      </CONNECT>
    </FIELD>

  </CLUSTER>

  <CLUSTER>

    <LIST >

      <CONTAINER>
        <WIDGET TYPE="MULTISELECT">
          <WIDGET_PARAMETER NAME="MULTI_SELECT_SOURCE">
            <CONNECT>
              <SOURCE NAME="DISPLAY" PROPERTY="dtls$caseID" />
            </CONNECT>
          </WIDGET_PARAMETER>
          <WIDGET_PARAMETER NAME="MULTI_SELECT_TARGET">
            <CONNECT>
              <TARGET NAME="ACTION" PROPERTY="caseTabList" />
            </CONNECT>
          </WIDGET_PARAMETER>
        </WIDGET>
      </CONTAINER>

      <FIELD LABEL="Field.Label.CaseReference" WIDTH="36">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="caseReference"/>
        </CONNECT>
      </FIELD>

      <FIELD LABEL="Field.Label.DateCreated" WIDTH="23">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="caseCreationDate"/>
        </CONNECT>
      </FIELD>

      <FIELD LABEL="Field.Label.Owner" WIDTH="41">
        <CONNECT>
          <SOURCE NAME="DISPLAY" PROPERTY="caseOwner"/>
        </CONNECT>
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Case_resolveOrgObjectTypeHome"
          WINDOW_OPTIONS="width=900"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="caseOwnerUsername"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="userName"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="orgObjectReference"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="orgObjectReference"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="orgObjectType"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="orgObjectType"
            />
          </CONNECT>
        </LINK>
      </FIELD>

    </LIST>

  </CLUSTER>

  <CLUSTER TITLE="Cluster.Title.Comments" SHOW_LABELS="false">
    <FIELD HEIGHT="4">
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="opComment"/>
      </CONNECT>
    </FIELD>

  </CLUSTER>

</VIEW>


