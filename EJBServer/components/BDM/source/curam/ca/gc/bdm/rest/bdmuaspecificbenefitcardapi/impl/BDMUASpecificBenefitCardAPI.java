package curam.ca.gc.bdm.rest.bdmuaspecificbenefitcardapi.impl;

import curam.ca.gc.bdm.impl.BDMConstants;
import curam.ca.gc.bdm.rest.bdmuaspecificbenefitcardapi.struct.BDMUASpecificBenefitDtls;
import curam.ca.gc.bdm.rest.bdmuaspecificbenefitcardapi.struct.BDMUASpecificBenefitKey;
import curam.codetable.CASEEVIDENCE;
import curam.codetable.EVIDENCEDESCRIPTORSTATUS;
import curam.codetable.impl.CASETYPECODEEntry;
import curam.core.fact.CaseHeaderFactory;
import curam.core.intf.CaseHeader;
import curam.core.sl.infrastructure.entity.fact.EvidenceDescriptorFactory;
import curam.core.sl.infrastructure.entity.intf.EvidenceDescriptor;
import curam.core.sl.infrastructure.entity.struct.CaseIDAndEvidenceTypeKey;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtls;
import curam.core.sl.infrastructure.entity.struct.EvidenceDescriptorDtlsList;
import curam.core.struct.CaseHeaderDtls;
import curam.core.struct.CaseReference;
import curam.dynamicevidence.sl.entity.fact.DynamicEvidenceDataAttributeFactory;
import curam.dynamicevidence.sl.entity.intf.DynamicEvidenceDataAttribute;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataAttributeDtlsList;
import curam.dynamicevidence.sl.entity.struct.DynamicEvidenceDataIDAndAttrKey;
import curam.util.exception.AppException;
import curam.util.exception.InformationalException;
import curam.util.type.Date;
import curam.util.type.NotFoundIndicator;

public class BDMUASpecificBenefitCardAPI extends
  curam.ca.gc.bdm.rest.bdmuaspecificbenefitcardapi.base.BDMUASpecificBenefitCardAPI {

  @Override
  public BDMUASpecificBenefitDtls getSpecificBenefitDetails(
    final BDMUASpecificBenefitKey BDMUASpecificBenefitkey)
    throws AppException, InformationalException {

    // initialise required objs and structs
    final BDMUASpecificBenefitDtls bdmuaSpecificBenefitDtls =
      new BDMUASpecificBenefitDtls();
    final CaseHeader caseHeaderObj = CaseHeaderFactory.newInstance();
    final CaseReference caseReferenceKey = new CaseReference();
    CaseHeaderDtls caseHeaderDtls = new CaseHeaderDtls();
    final NotFoundIndicator notFoundIndicator = new NotFoundIndicator();
    final EvidenceDescriptor evidenceDescriptorObj =
      EvidenceDescriptorFactory.newInstance();
    EvidenceDescriptorDtlsList evidenceDescriptorDtlsList =
      new EvidenceDescriptorDtlsList();
    final CaseIDAndEvidenceTypeKey caseIDAndEvidenceTypeKey =
      new CaseIDAndEvidenceTypeKey();
    final DynamicEvidenceDataAttribute dynEvidDataAttributeObj =
      DynamicEvidenceDataAttributeFactory.newInstance();

    // Take the BenefitID, and find the Current Active Income Evidence that is
    // associated to that Benefit, and return the Income Amount

    // add initial info to the return struct
    bdmuaSpecificBenefitDtls.case_id = 0l;
    bdmuaSpecificBenefitDtls.incomeAmount = "N/A";
    bdmuaSpecificBenefitDtls.incomeYear = "N/A";

    // Read on Cases, find matching case reference
    bdmuaSpecificBenefitDtls.case_reference =
      BDMUASpecificBenefitkey.case_reference;
    caseReferenceKey.caseReference = BDMUASpecificBenefitkey.case_reference;

    caseHeaderDtls =
      caseHeaderObj.readByCaseReference(notFoundIndicator, caseReferenceKey);

    // if no case is found, handle by returning an empty struct
    if (notFoundIndicator.isNotFound()) {
      return bdmuaSpecificBenefitDtls;
    }

    // Find evidences associated with the specific benefit/application case in
    // the EvidenceDescriptor
    if (caseHeaderDtls.caseTypeCode
      .equals(CASETYPECODEEntry.APPLICATION_CASE.getCode())) {
      caseIDAndEvidenceTypeKey.caseID = caseHeaderDtls.caseID;
    } else {
      caseIDAndEvidenceTypeKey.caseID = caseHeaderDtls.integratedCaseID;
    }
    caseIDAndEvidenceTypeKey.evidenceType = CASEEVIDENCE.BDMINCOME;

    evidenceDescriptorDtlsList = evidenceDescriptorObj
      .readByCaseIDAndEvidenceType(caseIDAndEvidenceTypeKey);

    // if no income is found, handle by returning an empty struct
    if (evidenceDescriptorDtlsList.dtls.isEmpty()) {
      return bdmuaSpecificBenefitDtls;
    }

    // create and initialise a date that will be used to compare and find the
    // most recent Income. Date initialises to 1/1/0001, will assume all
    // recieved Dates will be greater than this
    Date evidenceDate = new Date();

    // Find Income Evidence, If there is more than 1 active income, then choose
    // and return the most recently created /updated
    // Verification status of the evidence is not considered
    // Both Active and In edit Evidence considered
    for (final EvidenceDescriptorDtls evidenceDescriptorDtls : evidenceDescriptorDtlsList.dtls) {

      // Check if Active or in edit
      if (evidenceDescriptorDtls.statusCode
        .equals(EVIDENCEDESCRIPTORSTATUS.ACTIVE)
        || evidenceDescriptorDtls.statusCode
          .equals(EVIDENCEDESCRIPTORSTATUS.INEDIT)) {

        // if income is newer than previously checked income then update the
        // incomeAmount
        if (evidenceDate.before(evidenceDescriptorDtls.receivedDate)) {
          evidenceDate = evidenceDescriptorDtls.receivedDate;

          final DynamicEvidenceDataIDAndAttrKey dynEvidDataIDAndAttrKey =
            new DynamicEvidenceDataIDAndAttrKey();
          DynamicEvidenceDataAttributeDtlsList dynEvidDataAttributeDtlsList =
            new DynamicEvidenceDataAttributeDtlsList();

          dynEvidDataIDAndAttrKey.evidenceID =
            evidenceDescriptorDtls.relatedID;
          dynEvidDataIDAndAttrKey.name = BDMConstants.kIncomeAttribute;

          dynEvidDataAttributeDtlsList = dynEvidDataAttributeObj
            .searchByEvidenceIDAndAttribute(dynEvidDataIDAndAttrKey);

          // if no income is found, handle by breaking the current loop
          if (dynEvidDataAttributeDtlsList.dtls.isEmpty()) {
            break;
          }

          // set the income
          final String incomeAmount =
            dynEvidDataAttributeDtlsList.dtls.get(0).value;
          bdmuaSpecificBenefitDtls.incomeAmount = incomeAmount;
        }
      }
    }

    return bdmuaSpecificBenefitDtls;
  }

}
