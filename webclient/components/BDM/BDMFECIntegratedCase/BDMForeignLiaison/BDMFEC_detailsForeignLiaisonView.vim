<?xml version="1.0" encoding="UTF-8"?>
<!-- Licensed Materials - Property of IBM Copyright IBM Corporation 2012,2019. 
  All Rights Reserved. US Government Users Restricted Rights - Use, duplication 
  or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. -->

<!-- Description -->
<!-- =========== -->
<!-- This page allows the user to view the foreign liaison details and checklist 
  details. -->

<VIEW xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:noNamespaceSchemaLocation="file://Curam/UIMSchema.xsd">

  <CLUSTER NUM_COLS="2">

    <FIELD LABEL="Field.label.LiaisonReference">
      <CONNECT>
        <SOURCE NAME="DISPLAY"
          PROPERTY="result$liaisonRefDesc" />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.label.ForeignOffice">
      <CONNECT>
        <SOURCE NAME="DISPLAY"
          PROPERTY="result$foreignOfficeName" />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.label.ForeignIdentifier">
      <CONNECT>
        <SOURCE NAME="DISPLAY"
          PROPERTY="result$foreignIdntifier" />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.label.BESS">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$bessIndStr" />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.label.DeletionReason">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$deleteReason" />
      </CONNECT>
    </FIELD>

  </CLUSTER>

  <CLUSTER NUM_COLS="3" TITLE="Cluster.Title.Checklist">

    <FIELD LABEL="Field.label.Checklist.Address">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$chkLstAddrStr" />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.label.Checklist.Appealed">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$chkLstAppealedStr" />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.label.Checklist.Application">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$chkLstApplicationStr" />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.label.Checklist.CanadianBenefitRates">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$chkLstCndnBnftRtStr" />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.label.Checklist.Contributions">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$chkLstCntribtnStr" />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.label.Checklist.DateOfBirth">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$chkLstDOBStr" />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.label.Checklist.DateOfDeath">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$chkLstDODStr" />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.label.Checklist.DateOfReceipt">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$chkLstDORStr" />
      </CONNECT>
    </FIELD>

    <FIELD
      LABEL="Field.label.Checklist.DecisionApprovalOfForeignBenefits">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$chkLstDcsnApprvlFBStr" />
      </CONNECT>
    </FIELD>

    <FIELD
      LABEL="Field.label.Checklist.DecisionDenialOfForeignBenefits">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$chkLstDcsnDenialFBStr" />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.label.Checklist.ForeignPensionRate">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$chkLstFrgnPnsnRateStr" />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.label.Checklist.MedicalAppointment">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$chkLstMdclAppmntStr" />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.label.Checklist.MedicalDocumentation">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$chkLstMdclDocmntnStr" />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.label.Checklist.NextOfKin">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$chkLstNxtOfKinStr" />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.label.Checklist.ProtectiveDate">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$chkLstPrtctveDtStr" />
      </CONNECT>
    </FIELD>

    <FIELD LABEL="Field.label.Checklist.Residence">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$chkLstRsdncStr" />
      </CONNECT>
    </FIELD>
    
    <FIELD LABEL="Field.label.Checklist.Other">
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$lisnChkLstOthrDesc" />
      </CONNECT>
    </FIELD>
        
  </CLUSTER>

  <CLUSTER NUM_COLS="1" TITLE="Cluster.Title.Comments">
    <FIELD>
      <CONNECT>
        <SOURCE NAME="DISPLAY" PROPERTY="result$comments" />
      </CONNECT>
    </FIELD>
  </CLUSTER>

</VIEW>
