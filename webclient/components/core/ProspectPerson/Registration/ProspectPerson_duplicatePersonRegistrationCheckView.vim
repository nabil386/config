<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright 2002-2008, 2010 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description -->
<!-- =========== -->
<!-- Prospect Person search page                                            -->
<!-- BEGIN, CR00229427, DJ -->
<?curam-deprecated Since Curam V6, replaced with 
  ProspectPerson_duplicatePersonRegistrationCheckView1. 
  As part of the change to this page, all pages relating to this have been 
  replaced.See Release note:CR00229427?>
<!-- END, CR00229427 -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <LIST TITLE="List.Title.SearchResults">


    <CONTAINER
      LABEL="Container.Label.Action"
      WIDTH="10"
    >
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


    <FIELD WIDTH="2">
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="duplicateInd"
        />
      </CONNECT>
      <!-- BEGIN, CR00100763, PDN -->
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="Person_duplicateHomePage"
      >
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="originalConcernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
      </LINK>
      <!-- END, CR00100763 -->
    </FIELD>


    <FIELD
      LABEL="Field.Title.ReferenceNumber"
      WIDTH="5"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="dtls$referenceNumber"
        />
      </CONNECT>
      <LINK PAGE_ID="Person_resolveHomePage">
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="dtls$concernRoleID"
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
          PROPERTY="dtls$forename"
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
          PROPERTY="dtls$surname"
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
          PROPERTY="dtls$address"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.City"
      WIDTH="10"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="dtls$city"
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
          PROPERTY="dtls$dateOfBirth"
        />
      </CONNECT>
    </FIELD>


  </LIST>


</VIEW>
