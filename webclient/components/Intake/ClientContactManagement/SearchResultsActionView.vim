<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2010 Curam Software Ltd.                           -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This is a search page which allows the user to search                  -->
<!-- for prospect persons and persons.                                      -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <!-- List to display Person records-->
  <LIST>
    <TITLE>
      <CONNECT>
        <SOURCE
          NAME="TEXT"
          PROPERTY="List.Title.Persons"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="result$number"
        />
      </CONNECT>
      <CONNECT>
        <SOURCE
          NAME="TEXT"
          PROPERTY="Field.Label.SearchResultsClosingParanthesis"
        />
      </CONNECT>
    </TITLE>


    <DETAILS_ROW>
      <INLINE_PAGE PAGE_ID="Individual_TabDetails">
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
      </INLINE_PAGE>
    </DETAILS_ROW>


    <CONTAINER
      LABEL="Container.Label.Person"
      WIDTH="30"
    >
      <FIELD WIDTH="30">
        <CONNECT>
          <SOURCE
            NAME="ACTION"
            PROPERTY="name"
          />
        </CONNECT>
        <LINK PAGE_ID="Individual_resolveConcernRoleTypeHome">
          <CONNECT>
            <SOURCE
              NAME="ACTION"
              PROPERTY="concernRoleID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="concernRoleID"
            />
          </CONNECT>


        </LINK>
      </FIELD>
    </CONTAINER>


    <FIELD
      LABEL="Field.Title.Address"
      WIDTH="40"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="address"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.Gender"
      WIDTH="13"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="dtls$gender"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.DateOfBirth"
      WIDTH="13"
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
