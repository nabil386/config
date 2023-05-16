<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2002-2003, 2010 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- Check to see if the product provider has registered previously.        -->
<?curam-deprecated Since Curam 6.0, replaced with RegisterProductProviderWizard_duplicateCheck.uim?>
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <PAGE_TITLE DESCRIPTION="PageTitle.Description">
    <CONNECT>
      <SOURCE
        NAME="TEXT"
        PROPERTY="PageTitle.StaticText1"
      />
    </CONNECT>
  </PAGE_TITLE>


  <SERVER_INTERFACE
    CLASS="ProductProvider"
    NAME="ACTION"
    OPERATION="search"
    PHASE="ACTION"
  />


  <CLUSTER
    LABEL_WIDTH="33"
    NUM_COLS="2"
    TITLE="Cluster.Title.SearchCriteria"
  >
    <FIELD
      LABEL="Field.Label.ReferenceNumber"
      WIDTH="90"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="productProviderSearchKey$referenceNumber"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="33"
    NUM_COLS="2"
  >
    <FIELD
      LABEL="Field.Label.Name"
      WIDTH="94"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="productProviderSearchKey$name"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.City"
      WIDTH="90"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="productProviderSearchKey$city"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Label.AddressLineOne"
      WIDTH="90"
    >
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="productProviderSearchKey$address"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


  <CLUSTER>
    <ACTION_SET
      ALIGNMENT="LEFT"
      BOTTOM="false"
    >
      <ACTION_CONTROL
        DEFAULT="true"
        IMAGE="SearchButton"
        LABEL="ActionControl.Label.Search"
        TYPE="SUBMIT"
      >
        <LINK PAGE_ID="THIS"/>
      </ACTION_CONTROL>
      <ACTION_CONTROL
        IMAGE="ResetButton"
        LABEL="ActionControl.Label.Reset"
      >
        <LINK PAGE_ID="ProductProvider_duplicateRegistrationCheck"/>
      </ACTION_CONTROL>
    </ACTION_SET>
  </CLUSTER>


  <LIST TITLE="List.Title.SearchResults">
    <FIELD
      LABEL="Field.Title.ReferenceNumber"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="dtls$referenceNumber"
        />
      </CONNECT>
      <LINK PAGE_ID="ProductProvider_home">
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
    <FIELD
      LABEL="Field.Title.Name"
      WIDTH="20"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="dtls$name"
        />
      </CONNECT>
    </FIELD>
    <FIELD
      LABEL="Field.Title.AddressLineOne"
      WIDTH="30"
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
      WIDTH="30"
    >
      <CONNECT>
        <SOURCE
          NAME="ACTION"
          PROPERTY="dtls$city"
        />
      </CONNECT>
    </FIELD>
  </LIST>


</VIEW>
