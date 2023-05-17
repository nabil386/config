<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  Copyright IBM Corporation 2012, 2021. All Rights Reserved.
  
  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Description -->
<!-- =========== -->
<!-- This page displays search criteria for an Employer.                     -->
<VIEW
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd"
>


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
    TITLE="Cluster.Title.SearchCriteria"
  >


    <FIELD LABEL="Field.Label.ReferenceNumber">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$referenceNumber"
        />
      </CONNECT>
    </FIELD>


    <!-- BEGIN, 268814, SH -->
	<FIELD LABEL="Field.Label.ShowProspectEmployers">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$showProspectEmployersIndOpt"
        />
      </CONNECT>
    </FIELD>
	<!-- END, 268814 -->


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="45"
    NUM_COLS="2"
    TITLE="Cluster.Title.AdditionalSearchCriteria"
  >


    <FIELD LABEL="Field.Label.TradingName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$tradingName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AddressLineOne">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$addressDtls$addressLine1"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.City">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$addressDtls$city"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.RegisteredName">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$registeredName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AddressLineTwo">
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="key$addressDtls$addressLine2"
        />
      </CONNECT>
    </FIELD>

  </CLUSTER>


</VIEW>
