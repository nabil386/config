<?xml version="1.0" encoding="UTF-8"?>
<!--
  Licensed Materials - Property of IBM
 
  Copyright IBM Corporation 2012. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure 
  restricted by GSA ADP Schedule Contract with IBM Corp.
-->
<!-- Copyright (c) 2003-2005, 2010 Curam Software Ltd.                      -->
<!-- All rights reserved.                                                   -->
<!-- This software is the confidential and proprietary information of Curam -->
<!-- Software, Ltd. ("Confidential Information"). You shall not disclose    -->
<!-- such Confidential Information and shall use it only in accordance with -->
<!-- the terms of the license agreement you entered into with Curam         -->
<!-- Software.                                                              -->
<!-- Description                                                            -->
<!-- ===========                                                            -->
<!-- The included view to display the details of a case nominee.            -->
<!-- BEGIN, CR00189687, GD -->
<?curam-deprecated Since Curam 6.0, replaced with ProductDelivery_viewNomineeDetailsView1.
  This page doesn't support Multiple Active Nominee Delivery Patterns (HLFR14604) . 
  Hence this page has been deprecated. See release note: CR00189687 ?>
<!-- END, CR00189687 -->
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
    CLASS="Case"
    NAME="DISPLAY"
    OPERATION="viewCaseNomineeDetails"
  />


  <ACTION_SET ALIGNMENT="CENTER">


    <ACTION_CONTROL
      IMAGE="EditButton"
      LABEL="ActionControl.Label.Edit"
    >
      <LINK
        OPEN_MODAL="true"
        PAGE_ID="ProductDelivery_modifyNomineeFromView"
        SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$caseNomineeContextDescription$description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="description"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseNomineeID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseNomineeID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
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
            PROPERTY="caseNomineeDetails$nomineeStatus"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="nomineeStatus"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      IMAGE="ChangeAddressButton"
      LABEL="ActionControl.Label.ChangeAddress"
    >
      <LINK
        PAGE_ID="ProductDelivery_modifyNomineeAddress"
        SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseNomineeDetails$concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseNomineeDetails$caseNomineeID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseNomineeID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$caseNomineeContextDescription$description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="nomineePageDescription"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      IMAGE="ChangeBankAccountButton"
      LABEL="ActionControl.Label.ChangeBankAccount"
    >
      <LINK
        PAGE_ID="ProductDelivery_modifyNomineeBankAccount"
        SAVE_LINK="false"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseNomineeDetails$concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseNomineeDetails$caseNomineeID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseNomineeID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$caseNomineeContextDescription$description"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="nomineePageDescription"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      IMAGE="ExpireButton"
      LABEL="ActionControl.Label.Expire"
    >


      <LINK
        OPEN_MODAL="true"
        PAGE_ID="ProductDelivery_expireNominee"
        SAVE_LINK="true"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseNomineeDetails$caseNomineeID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseNomineeID"
          />
        </CONNECT>


        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseNomineeDetails$caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>


        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseNomineeDetails$versionNo"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="versionNo"
          />
        </CONNECT>


        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$caseNomineeContextDescription$description"
          />


          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>


        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseNomineeViewDetails$nomineeStatus"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="nomineeStatus"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


    <ACTION_CONTROL
      IMAGE="ReactivateButton"
      LABEL="ActionControl.Label.Reactivate"
    >


      <LINK
        OPEN_MODAL="true"
        PAGE_ID="ProductDelivery_reactivateNominee"
        SAVE_LINK="true"
      >
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseNomineeDetails$caseNomineeID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseNomineeID"
          />
        </CONNECT>


        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseNomineeDetails$caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>


        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseNomineeDetails$versionNo"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="versionNo"
          />
        </CONNECT>


        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="result$caseNomineeContextDescription$description"
          />


          <TARGET
            NAME="PAGE"
            PROPERTY="pageDescription"
          />
        </CONNECT>


        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="caseNomineeViewDetails$nomineeStatus"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="nomineeStatus"
          />
        </CONNECT>
      </LINK>
    </ACTION_CONTROL>


  </ACTION_SET>


  <PAGE_PARAMETER NAME="caseNomineeID"/>
  <PAGE_PARAMETER NAME="caseID"/>


  <CONNECT>
    <SOURCE
      NAME="PAGE"
      PROPERTY="caseNomineeID"
    />
    <TARGET
      NAME="DISPLAY"
      PROPERTY="caseNomineeViewKey$caseNomineeID"
    />
  </CONNECT>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.Details"
  >


    <FIELD LABEL="Field.Label.Name">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="concernRoleName"
        />
      </CONNECT>
      <LINK PAGE_ID="Participant_resolveRoleHome">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="concernRoleID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="concernRoleID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="concernRoleType"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="participantType"
          />
        </CONNECT>
      </LINK>
    </FIELD>


    <FIELD LABEL="Field.Label.Relationship">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="nomineeRelationship"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.ReferenceNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="alternateID"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.NomineeStatus">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="caseNomineeViewDetails$nomineeStatus"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.PublicOffice">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="publicOfficeName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.Currency">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="currencyType"
        />
      </CONNECT>
    </FIELD>


    <CONTAINER LABEL="Field.Label.DefaultNominee">
      <FIELD>
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="defaultNomInd"
          />
        </CONNECT>
      </FIELD>
      <ACTION_CONTROL 
        LABEL="ActionControl.Label.Set"
        APPEND_ELLIPSIS="false"
      >
        <LINK
          OPEN_MODAL="true"
          PAGE_ID="ProductDelivery_setDefaultNominee"
          SAVE_LINK="true"
        >
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="caseNomineeDetails$caseNomineeID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseNomineeID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="caseNomineeDetails$caseID"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="caseID"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="caseNomineeDetails$versionNo"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="versionNo"
            />
          </CONNECT>
          <CONNECT>
            <SOURCE
              NAME="DISPLAY"
              PROPERTY="result$caseNomineeContextDescription$description"
            />
            <TARGET
              NAME="PAGE"
              PROPERTY="pageDescription"
            />
          </CONNECT>
        </LINK>
      </ACTION_CONTROL>
    </CONTAINER>


  </CLUSTER>


  <CLUSTER
    LABEL_WIDTH="40"
    NUM_COLS="2"
    TITLE="Cluster.Title.BankAccountDetails"
  >


    <FIELD LABEL="Field.Label.AccountName">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bankAccountName"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AccountType">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bankAccountType"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.AccountNumber">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bankAccountNumber"
        />
      </CONNECT>
    </FIELD>


    <FIELD LABEL="Field.Label.SortCode">
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="bankSortCode"
        />
      </CONNECT>
      <LINK PAGE_ID="ProductDelivery_viewBankBranch">
        <CONNECT>
          <SOURCE
            NAME="DISPLAY"
            PROPERTY="bankBranchID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="bankBranchID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseNomineeID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseNomineeID"
          />
        </CONNECT>
        <CONNECT>
          <SOURCE
            NAME="PAGE"
            PROPERTY="caseID"
          />
          <TARGET
            NAME="PAGE"
            PROPERTY="caseID"
          />
        </CONNECT>
      </LINK>
    </FIELD>


  </CLUSTER>


  <CLUSTER
    SHOW_LABELS="false"
    TITLE="Cluster.Title.Address"
  >


    <FIELD>
      <CONNECT>
        <SOURCE
          NAME="DISPLAY"
          PROPERTY="addressData"
        />
      </CONNECT>
    </FIELD>


  </CLUSTER>


</VIEW>
