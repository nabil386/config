<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page lists the Evidence types associated with the  participant    -->
<!-- participant data case type.                                            -->
<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">


  <SERVER_INTERFACE CLASS="EvidenceTypesLink" NAME="DISPLAY" OPERATION="listPDCEvidenceLinks" PHASE="DISPLAY"/>


  <LIST>


    <ACTION_SET TYPE="LIST_ROW_MENU">
      <ACTION_CONTROL LABEL="ActionControl.Label.Delete" TYPE="ACTION">
        <LINK OPEN_MODAL="true" PAGE_ID="PDCEvidenceLink_cancel" WINDOW_OPTIONS="width=400">
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="evidenceTypeLinkID"/>
            <TARGET NAME="PAGE" PROPERTY="evidenceTypeLinkID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="evidenceTypeCode"/>
            <TARGET NAME="PAGE" PROPERTY="evidenceTypeCode"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="versionNo"/>
            <TARGET NAME="PAGE" PROPERTY="versionNo"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="ActionControl.Label.Set">
        <CONDITION>
          <IS_FALSE NAME="DISPLAY" PROPERTY="shareableInd"/>
        </CONDITION>
        <LINK OPEN_MODAL="true" PAGE_ID="PDCEvidenceLink_setShareable" WINDOW_OPTIONS="width=400">
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="associatedID"/>
            <TARGET NAME="PAGE" PROPERTY="associatedID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="evidenceTypeLinkID"/>
            <TARGET NAME="PAGE" PROPERTY="evidenceTypeLinkID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="evidenceTypeCode"/>
            <TARGET NAME="PAGE" PROPERTY="evidenceTypeCode"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="evidenceTypeID"/>
            <TARGET NAME="PAGE" PROPERTY="evidenceTypeID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="versionNo"/>
            <TARGET NAME="PAGE" PROPERTY="versionNo"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>


      <ACTION_CONTROL LABEL="ActionControl.Label.Unset">
        <CONDITION>
          <IS_TRUE NAME="DISPLAY" PROPERTY="shareableInd"/>
        </CONDITION>
        <LINK OPEN_MODAL="true" PAGE_ID="PDCEvidenceLink_unsetShareable" WINDOW_OPTIONS="width=400">
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="associatedID"/>
            <TARGET NAME="PAGE" PROPERTY="associatedID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="evidenceTypeLinkID"/>
            <TARGET NAME="PAGE" PROPERTY="evidenceTypeLinkID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="evidenceTypeCode"/>
            <TARGET NAME="PAGE" PROPERTY="evidenceTypeCode"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="evidenceTypeID"/>
            <TARGET NAME="PAGE" PROPERTY="evidenceTypeID"/>
          </CONNECT>
          <CONNECT>
            <SOURCE NAME="DISPLAY" PROPERTY="versionNo"/>
            <TARGET NAME="PAGE" PROPERTY="versionNo"/>
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </ACTION_SET>


    <FIELD LABEL="Field.Label.Type" WIDTH="50">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="evidenceTypeCode"/>
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.SharedInd" WIDTH="50">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="shareableInd"/>
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>