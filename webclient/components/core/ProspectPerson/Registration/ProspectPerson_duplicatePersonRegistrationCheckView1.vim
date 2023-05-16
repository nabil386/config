<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2011 Curam Software Ltd.                                     -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <LIST TITLE="List.Title.SearchResults">


    <!-- BEGIN, CR00236989, AK -->
    <CONTAINER
      LABEL="Container.Label.Action"
      WIDTH="5"
    >
      <!-- END, CR00236989 -->
      <ACTION_CONTROL LABEL="ActionControl.Label.MarkProspectAsDuplicate">
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="Participant_markDuplicate"
        >
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="concernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="originalConcernRoleID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="PAGE"
              PROPERTY="concernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="duplicateConcernRoleID"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


    <!-- BEGIN, CR00236989, AK -->
    <FIELD
      LABEL="Field.Title.ReferenceNumber"
      WIDTH="12"
    >
      <!-- END, CR00236989 -->
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="arg1$personSearchKey$referenceNumber"
        />
      </CONNECT>
      <LINK PAGE_ID="Person_resolveHomePage">
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="result$personSearchResult$dtlsList$concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD
      LABEL="Field.Title.FirstName"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="arg1$personSearchKey$forename"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.LastName"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="arg1$personSearchKey$surname"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.AddressLineOne"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="arg1$personSearchKey$addressDtls$addressLine1"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, CR00236989, AK -->
    <FIELD
      LABEL="Field.Title.City"
      WIDTH="7"
    >
      <!-- END, CR00236989 -->
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="arg1$personSearchKey$addressDtls$city"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.DateOfBirth"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$personSearchResult$dtlsList$dateOfBirth"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
