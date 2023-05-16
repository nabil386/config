<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003-2009, 2011 Curam Software Ltd.                                 -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- This page allows the user to modify an integrated case type.           -->
<?curam-deprecated Since Curam 6.0, replaced by Resource_modifyIntegratedCaseTypeView1.vim?>
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


  <SERVER_INTERFACE
    CLASS="Organization"
    NAME="DISPLAY"
    OPERATION="readAdminIntegratedCase"
  />


  <SERVER_INTERFACE
    CLASS="Organization"
    NAME="ACTION"
    OPERATION="modifyAdminIntegratedCase"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="adminIntegratedCaseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="adminIntegratedCaseID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="key$adminIntegratedCaseID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="adminIntegratedCaseID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="adminIntegratedCaseID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="DISPLAY"
      PROPERTY="versionNo"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="versionNo"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="40"
    TITLE="Cluster.Title.Details"
  >


    <FIELD LABEL="Field.Title.Type">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="integratedCaseType"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="integratedCaseType"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.HomePage"
      WIDTH="25"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="homePageName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="homePageName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.MemberHomePage"
      WIDTH="25"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="memberHomePageName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="memberHomePageName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.EmployerHomePage"
      WIDTH="25"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="employerHomePageName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="employerHomePageName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.InfoProviderHomePage"
      WIDTH="25"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="infoProviderHomePageName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="infoProviderHomePageName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.PersonHomePage"
      WIDTH="25"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="personHomePageName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="personHomePageName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.ProspectPersonHomePage"
      WIDTH="25"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="ppHomePageName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="ppHomePageName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.ProductProviderHomePage"
      WIDTH="25"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="productProviderHomePageName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="productProviderHomePageName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.RepresentativeHomePage"
      WIDTH="25"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="representativeHomePageName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="representativeHomePageName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.ServiceSupplierHomePage"
      WIDTH="25"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="serviceSupplierHomePageName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="serviceSupplierHomePageName"
        />
      </CONNECT>
    </FIELD>


    <FIELD
      LABEL="Field.Title.UtilityHomePage"
      WIDTH="25"
      WIDTH_UNITS="CHARS"
    >
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="utilityHomePageName"
        />
      </CONNECT>
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="utilityHomePageName"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
