<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2010 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Page to list Integrated Outstanding caes.                              -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
  >
  <LIST>
    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="VerificationApplication_viewIntegratedCaseVerificationList">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="vDIEDLinkID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="VDIEDLinkID"
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
            PROPERTY="verifiableDataItemName"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="dataItemName"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


    <FIELD LABEL="Field.Label.Evidence">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="evidence"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.Verification">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="verifiableDataItemName"
        />
      </CONNECT>
    </FIELD>
    <FIELD LABEL="Field.Label.PrimaryClient">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="primaryClient"
        />
      </CONNECT>
      <LINK PAGE_ID="IntegratedCase_resolveParticipantHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseParticpantRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseParticipantRoleID"
          />
        </CONNECT>
      </LINK>
    </FIELD>
    <!-- BEGIN, CR00021587, NK 
    <FIELD
      LABEL="Field.Label.Mandatory"
      WIDTH="10"
    >
      
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="mandatory"
        />
      </CONNECT>
    </FIELD>
       END, CR00021587 -->


    <FIELD LABEL="Field.Label.DueDate">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="dueDate"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Status">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="verificationStatus"
        />
      </CONNECT>
    </FIELD>
  </LIST>
</VIEW>
