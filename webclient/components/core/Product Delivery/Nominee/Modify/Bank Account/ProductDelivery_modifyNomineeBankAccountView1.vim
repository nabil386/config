<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
  
  PID 5725-H26
  
  Copyright IBM Corporation 2009, 2013. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2009 Curam Software Ltd.                            -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view to allow the user to modify the bank account for     -->
<!-- a case nominee.                                                        -->
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
    CLASS="Participant"
    NAME="DISPLAY"
    OPERATION="listBankAccountString"
    PHASE="DISPLAY"
  />

 <!-- BEGIN, CR00372248, GA --> 
  <SERVER_INTERFACE
    CLASS="Participant"
    NAME="DISPLAY1"
    OPERATION="readIban"
    PHASE="DISPLAY"
  />
  <!-- END, CR00372248 -->

  <SERVER_INTERFACE
    CLASS="Case"
    NAME="ACTION"
    OPERATION="modifyNomineeBankAccount"
    PHASE="ACTION"
  />


  <PAGE_PARAMETER NAME="concernRoleID"/>
  <PAGE_PARAMETER NAME="caseNomineeID"/>
  <PAGE_PARAMETER NAME="caseID"/>
  <PAGE_PARAMETER NAME="description"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="concernRoleID"
    />
  </CONNECT>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="concernRoleID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="details$concernRoleID"
    />
  </CONNECT>
  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseNomineeID"
    />
    <TARGET
      NAME="ACTION"
      PROPERTY="caseNomineeID"
    />
  </CONNECT>


  <CLUSTER LABEL_WIDTH="47.5">


    <CLUSTER
      DESCRIPTION="Cluster.Description.BankAcDetails"
      LABEL_WIDTH="24.5"
      SHOW_LABELS="true"
      STYLE="cluster-cpr-no-border"
    >

      <FIELD
        LABEL="Field.Label.SelectBankAc"
        USE_BLANK="true"
        WIDTH="100"
      >
        <CONNECT>
          <INITIAL
            HIDDEN_PROPERTY="bankAccountID"
            NAME="DISPLAY"
            PROPERTY="bankAccountString"
          />
        </CONNECT>
        <CONNECT>
          <TARGET
            NAME="ACTION"
            PROPERTY="accountDetails$bankAccountID"
          />
        </CONNECT>
      </FIELD>


      <CLUSTER
        LABEL_WIDTH="50"
        NUM_COLS="2"
        SHOW_LABELS="true"
        STYLE="cluster-cpr-no-border"
        TAB_ORDER="ROW"
      >

      <!-- BEGIN, CR00372248, GA --> 
     <CONDITION>
      <IS_FALSE
        NAME="DISPLAY1"
        PROPERTY="ibanInd"
      />
     </CONDITION>
      <!-- END, CR00372248 --> 

        <FIELD
          LABEL="Field.Label.AccountName"
          WIDTH="100"
        >
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="name"
            />
          </CONNECT>
        </FIELD>


        <FIELD
          LABEL="Field.Label.AccountNumber"
          WIDTH="100"
        >
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="accountNumber"
            />
          </CONNECT>
        </FIELD>


        <FIELD
          LABEL="Field.Label.AccountType"
          WIDTH="100"
        >
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="typeCode"
            />
          </CONNECT>
        </FIELD>


        <FIELD
          LABEL="Field.Label.SortCode"
          WIDTH="100"
        >
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="modifyDetails$bankSortCode"
            />
          </CONNECT>
        </FIELD>


        <FIELD
          LABEL="Field.Label.StartDate"
          WIDTH="85"
        >
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="startDate"
            />
          </CONNECT>
        </FIELD>


        <FIELD LABEL="Field.Label.Primary">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="primaryBankInd"
            />
          </CONNECT>
        </FIELD>


        <FIELD LABEL="Field.Label.Joint">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="jointAccountInd"
            />
          </CONNECT>
        </FIELD>


      </CLUSTER>
    
        <!-- BEGIN, CR00372248, GA -->  
     <CLUSTER
        LABEL_WIDTH="50"
        NUM_COLS="2"
        SHOW_LABELS="true"
        STYLE="cluster-cpr-no-border"
        TAB_ORDER="ROW"
      >
     <CONDITION>
      <IS_TRUE
        NAME="DISPLAY1"
        PROPERTY="ibanInd"
      />
     </CONDITION>

        <FIELD
          LABEL="Field.Label.AccountName"
          WIDTH="100"
        >
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="name"
            />
          </CONNECT>
        </FIELD>
        
        <FIELD
          LABEL="Field.Label.AccountType"
          WIDTH="100"
        >
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="typeCode"
            />
          </CONNECT>
        </FIELD>
        
        <FIELD
          LABEL="Field.Label.IBAN"
          WIDTH="100"
        >
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="ibanOpt"
            />
          </CONNECT>
        </FIELD>
        
        <FIELD
          LABEL="Field.Label.BIC"
          WIDTH="100"
        >
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="bicOpt"
            />
          </CONNECT>
        </FIELD>

        <FIELD
          LABEL="Field.Label.AccountNumber"
          WIDTH="100"
        >
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="accountNumber"
            />
          </CONNECT>
        </FIELD>


        <FIELD
          LABEL="Field.Label.SortCode"
          WIDTH="100"
        >
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="modifyDetails$bankSortCode"
            />
          </CONNECT>
        </FIELD>


        <FIELD
          LABEL="Field.Label.StartDate"
          WIDTH="85"
        >
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="startDate"
            />
          </CONNECT>
        </FIELD>


        <FIELD LABEL="Field.Label.Primary">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="primaryBankInd"
            />
          </CONNECT>
        </FIELD>


        <FIELD LABEL="Field.Label.Joint">
          <CONNECT>
            <TARGET
              NAME="ACTION"
              PROPERTY="jointAccountInd"
            />
          </CONNECT>
        </FIELD>
      </CLUSTER>
     <!-- END, CR00372248 --> 
    </CLUSTER>
    
  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Comments"
  >


    <!-- BEGIN, CR00406866, VT -->
    <FIELD HEIGHT="4" LABEL="Field.Label.Comments">
      <!-- END, CR00406866 -->
      <CONNECT>
        <TARGET
          NAME="ACTION"
          PROPERTY="details$comments"
        />
      </CONNECT>
    </FIELD>
  </CLUSTER>


</VIEW>
