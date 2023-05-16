<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM

  Copyright IBM Corporation 2012,2014. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Description                                                        -->
<!-- =================================================================  -->
<!-- This page is used for authorizing a single program when the        -->
<!-- program authorizing strategy is set to CreateNew.                  -->

<!-- This view contains details of the create new authorisation page.   -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>

  <CONNECT>
    <SOURCE NAME="CONSTANT" PROPERTY="Constant.True"/>
    <TARGET NAME="ACTION" PROPERTY="createNewInd" />
  </CONNECT>

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

  <CLUSTER TITLE="Cluster.Title.Comments" SHOW_LABELS="false">
    <FIELD LABEL="Cluster.Title.Comments" HEIGHT="150">
      <CONNECT>
        <TARGET NAME="ACTION" PROPERTY="opComment"/>
      </CONNECT>
    </FIELD>

  </CLUSTER>

</VIEW>
